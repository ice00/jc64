/**
 * @(#)TransformingClassLoader 2025/10/14
 *
 * ICE Team free software group
 *
 * This file is part of C64 Java Software Emulator.
 * See README for copyright notice.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 *  02111-1307  USA.
 */
package sw_emulator.software.asm;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import net.bytebuddy.jar.asm.ClassReader;
import net.bytebuddy.jar.asm.ClassVisitor;
import net.bytebuddy.jar.asm.ClassWriter;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.jar.asm.Opcodes;

/**
 * Load a class by replacing System.exit to avoid termination of JVM
 * 
 * @author ice
 */
public class TransformingClassLoader extends ClassLoader {

  private final Map<String, Class<?>> loadedClasses = new ConcurrentHashMap<>();
  private final Set<String> packagesToTransform;

  /**
   * Construct the class loader with the given package to intercept
   * 
   * @param parent the normal class loader
   * @param packagesToTransform the packages to intercept
   */
  public TransformingClassLoader(ClassLoader parent, Set<String> packagesToTransform) {
    super(parent);
    this.packagesToTransform = packagesToTransform;
  }

  @Override
  protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
    // Se è una classe da trasformare, caricala con trasformazione
    if (shouldTransform(name)) {
      synchronized (getClassLoadingLock(name)) {
        Class<?> c = loadedClasses.get(name);
        if (c == null) {
          c = findClass(name);
          if (resolve) {
            resolveClass(c);
          }
          loadedClasses.put(name, c);
        }
        return c;
      }
    }
    return super.loadClass(name, resolve);
  }

  @Override
  protected Class<?> findClass(String name) throws ClassNotFoundException {
    String path = name.replace('.', '/') + ".class";

    if (shouldTransform(name)) {
      try (InputStream is = getParent().getResourceAsStream(path)) {
        if (is == null) {
          throw new ClassNotFoundException("Class not found: " + name);
        }

        byte[] bytecode = readAllBytes(is);
        byte[] transformedBytecode = transformBytecode(bytecode, name);
        return defineClass(name, transformedBytecode, 0, transformedBytecode.length);

      } catch (IOException e) {
        throw new ClassNotFoundException("Failed to load class: " + name, e);
      }
    }

    return super.findClass(name);
  }

  /**
   * Determine if the given package is to transform
   * 
   * @param className the package to check
   * @return true if it is to transform
   */
  private boolean shouldTransform(String className) {
    return packagesToTransform.stream().anyMatch(className::startsWith);
  }
  
 /**
  * Read all bytes for the stream
  * 
  * @param is the stream to read
  * @returnthe read bytes
  * @throws IOException  in case of errors
  */
  private byte[] readAllBytes(InputStream is) throws IOException {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    byte[] data = new byte[8192];
    int bytesRead;

    while ((bytesRead = is.read(data, 0, data.length)) != -1) {
      buffer.write(data, 0, bytesRead);
    }

    return buffer.toByteArray();
  }

  /**
   * Transform the given bytecodes to the patched one
   * 
   * @param originalBytecode the orginal bytecodes
   * @param className the class name to process
   * @return the converted bytecodes
   */
  private byte[] transformBytecode(byte[] originalBytecode, String className) {
    try {
      ClassReader cr = new ClassReader(originalBytecode);
      ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
      ClassVisitor cv = new ExitCallClassAdapter(cw);
      cr.accept(cv, 0);
      return cw.toByteArray();
    } catch (Exception e) {
      System.err.println("Error transforming class " + className + ": " + e.getMessage());
      return originalBytecode;
    }
  }
}


/**
 * Adapter for exit calling
 */
class ExitCallClassAdapter extends ClassVisitor {

  public ExitCallClassAdapter(ClassVisitor cv) {
    super(Opcodes.ASM9, cv);
  }

  @Override
  public MethodVisitor visitMethod(int access, String name, String descriptor,
          String signature, String[] exceptions) {
    MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
    return new ExitCallMethodAdapter(mv);
  }
}

/**
 * New method for exit calling 
 */
class ExitCallMethodAdapter extends MethodVisitor {
  public ExitCallMethodAdapter(MethodVisitor mv) {
    super(Opcodes.ASM9, mv);
  }

  @Override
  public void visitMethodInsn(int opcode, String owner, String name,
          String descriptor, boolean isInterface) {
    if (opcode == Opcodes.INVOKESTATIC
            && "java/lang/System".equals(owner)
            && "exit".equals(name)
            && "(I)V".equals(descriptor)) {

      super.visitMethodInsn(Opcodes.INVOKESTATIC,
              "sw_emulator/software/asm/ExitInterceptor",
              "throwInsteadOfExit",
              "(I)V",
              false);
    } else {
      super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }
  }
}
