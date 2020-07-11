/**
 * @(#)ContrastTheme.java 2020/07/08
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
 * 
 * 
 * Copyright (c) 2007 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * -Redistribution of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *
 * -Redistribution in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MIDROSYSTEMS, INC. ("SUN")
 * AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE,
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of any
 * nuclear facility.
 */
package sw_emulator.swing.plaf.metal;

import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.border.LineBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.border.Border;
import javax.swing.UIDefaults;

/**
 * This class describes a higher-contrast Metal Theme.
 * 
 * @author Michael C. Albers
 */
public class ContrastTheme extends DefaultMetalTheme {
  public String getName() { return "Contrast"; }

  private final ColorUIResource primary1 = new ColorUIResource(0, 0, 0);
  private final ColorUIResource primary2 = new ColorUIResource(204, 204, 204);
  private final ColorUIResource primary3 = new ColorUIResource(255, 255, 255);
  private final ColorUIResource primaryHighlight = new ColorUIResource(102,102,102);

  private final ColorUIResource secondary2 = new ColorUIResource(204, 204, 204);
  private final ColorUIResource secondary3 = new ColorUIResource(255, 255, 255);
  private final ColorUIResource controlHighlight = new ColorUIResource(102,102,102);

  protected ColorUIResource getPrimary1() { return primary1; }
  protected ColorUIResource getPrimary2() { return primary2; }
  protected ColorUIResource getPrimary3() { return primary3; }
  public ColorUIResource getPrimaryControlHighlight() { return primaryHighlight;}

  protected ColorUIResource getSecondary2() { return secondary2; }
  protected ColorUIResource getSecondary3() { return secondary3; }
  public ColorUIResource getControlHighlight() { return super.getSecondary3(); }

  public ColorUIResource getFocusColor() { return getBlack(); }

  public ColorUIResource getTextHighlightColor() { return getBlack(); }
  public ColorUIResource getHighlightedTextColor() { return getWhite(); }

  public ColorUIResource getMenuSelectedBackground() { return getBlack(); }
  public ColorUIResource getMenuSelectedForeground() { return getWhite(); }
  public ColorUIResource getAcceleratorForeground() { return getBlack(); }
  public ColorUIResource getAcceleratorSelectedForeground() { return getWhite(); }


  public void addCustomEntriesToTable(UIDefaults table) {
    Border blackLineBorder = new BorderUIResource(new LineBorder( getBlack() ));

    Object textBorder = new BorderUIResource( new CompoundBorder(
                                                 blackLineBorder,
                                              new BasicBorders.MarginBorder()));

    table.put( "ToolTip.border", blackLineBorder);
    table.put( "TitledBorder.border", blackLineBorder);

    table.put( "TextField.border", textBorder);
    table.put( "PasswordField.border", textBorder);
    table.put( "TextArea.border", textBorder);
    table.put( "TextPane.border", textBorder);
    table.put( "EditorPane.border", textBorder);
  }
}

