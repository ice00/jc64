/**
 * @(#)VicII_IO.java 1999/12/29
 *
 * ICE Team Free Software Group
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

package sw_emulator.hardware.io;

import sw_emulator.hardware.signaller;

/**
 * Emulate the IO of the VIC II chip
 * The methods <code>notifySignal</code> may be implementded for give the right
 * connection of the signal being changed in the machine where the Vic is used.
 * So this class is perfectly abstract.
 *
 * @author Ice
 * @version 1.00 29/12/1999
 */
public abstract class VicII_IO implements signaller {
}