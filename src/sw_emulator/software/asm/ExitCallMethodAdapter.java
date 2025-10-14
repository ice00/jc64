/**
 * @(#)ExitCallMethodAdapter 2025/10/14
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

import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.jar.asm.Opcodes;

public class ExitCallMethodAdapter extends MethodVisitor {

  public ExitCallMethodAdapter(MethodVisitor mv) {
    super(Opcodes.ASM9, mv);
  }

  @Override
  public void visitMethodInsn(int opcode, String owner, String name,
          String descriptor, boolean isInterface) {
    // Sostituisce System.exit(int) con una chiamata al nostro metodo
    if (opcode == Opcodes.INVOKESTATIC
            && "java/lang/System".equals(owner)
            && "exit".equals(name)
            && "(I)V".equals(descriptor)) {

      // Invece di System.exit(status), chiama il nostro metodo
      super.visitMethodInsn(Opcodes.INVOKESTATIC,
              "com/yourpackage/ExitInterceptor",
              "throwInsteadOfExit",
              "(I)V",
              false);
    } else {
      super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }
  }
}
