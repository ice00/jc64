/**
 * @(#)SafeLibraryExecutor 2025/10/19
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

import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import sw_emulator.swing.main.Option;

/**
 * Execute a class for compilation without having the JVM to exit
 */
public class SafeLibraryExecutor {

  /**
   * Execute the given compilation class avoiding an JVM exit
   * 
   * @param mainClassName the name of the main class to execute
   * @param args the arguments of the program
   * @param packagesToTransform the transformation package 
   * @param option the option
   * @return the resulting output
   */
  public static String executeInIsolatedClassLoader(String mainClassName,
          String[] args,
          Set<String> packagesToTransform, Option option) {
    PrintStream orgStream = System.out;
    PrintStream fileStream = null;

    try {
      fileStream = new PrintStream(option.tmpPath + File.separator + "tmp.tmp");
      System.setOut(fileStream);
      
      ClassLoader transformingLoader = new TransformingClassLoader(
              SafeLibraryExecutor.class.getClassLoader(),
              packagesToTransform
      );

      Class<?> mainClass = transformingLoader.loadClass(mainClassName);
      Method mainMethod = mainClass.getMethod("main", String[].class);

      try {
        mainMethod.invoke(null, (Object) args);
        return "Compilation done";

      } catch (InvocationTargetException e) {
        Throwable target = e.getTargetException();
        if (target instanceof RuntimeException
                && target.getMessage() != null
                && target.getMessage().contains("System.exit")) {
          return "Compilation done (exit prevented)";
        } else {
          return "Compilation failed: " + target.getMessage();
        }
      }

    } catch (Exception e) {
      return "Error: " + e.getMessage();
    } finally {
      if (fileStream != null) {
        fileStream.close();
      }
      System.setOut(orgStream);
    }
  }

  /**
   * Get the compilation outout
   *
   * @param option the option
   * @return the error messages of compilation
   */
  public static String getCompilationOutput(Option option) {
    try {
      String result = new String(Files.readAllBytes(Paths.get(option.tmpPath + File.separator + "tmp.tmp")),
              StandardCharsets.UTF_8);
      int pos = result.indexOf("org.ibex.nestedvm.Runtime$ExecutionException:");
      if (pos > 0) {
        result = result.substring(0, pos);
      }
      if ("".equals(result)) {
        result = "Compilation done";
      }
      return result;
    } catch (Exception e) {
      return "Error reading compilation result: " + e.getMessage();
    }
  }
}
