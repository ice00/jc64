/**
 * @(#)XRefToolTipManager.java 2025/10/18
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
package sw_emulator.swing.main;

import sw_emulator.software.memory.XRefManager;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.*;
import java.util.List;
import java.util.function.Function;
import static sw_emulator.swing.main.XRefUIConstants.NATIVE_TOOLTIP_DISMISS_DELAY_MS;
import static sw_emulator.swing.main.XRefUIConstants.NATIVE_TOOLTIP_INITIAL_DELAY_MS;
import static sw_emulator.swing.main.XRefUIConstants.TOOLTIP_AUTO_HIDE_DELAY_MS;
import static sw_emulator.swing.main.XRefUIConstants.TOOLTIP_FORCE_HIDE_DELAY_MS;
import static sw_emulator.swing.main.XRefUIConstants.TOOLTIP_SHOW_DELAY_MS;

/**
 * XRef tooltip manager
 */
public class XRefToolTipManager {

  private final RSyntaxTextArea textArea;
  private final XRefManager xrefManager;
  private final Function<Integer, Integer> addressResolver;

  private Timer showTimer;
  private Timer hideTimer;
  private int currentAddress = -1;

  /**
   * Construct the tool tip manager for the given text area
   * 
   * @param textArea the text area associated with the tooltip
   * @param xrefManager the xref manager
   * @param addressResolver the function that resolve the address
   */
  public XRefToolTipManager(RSyntaxTextArea textArea, XRefManager xrefManager,
          Function<Integer, Integer> addressResolver) {
    this.textArea = textArea;
    this.xrefManager = xrefManager;
    this.addressResolver = addressResolver;
    initialize();
  }

  /**
   * Initialize the timers of tooltip
   */
  private void initialize() {
    showTimer = new Timer(TOOLTIP_SHOW_DELAY_MS, e -> updateTooltip());
    showTimer.setRepeats(false);

    hideTimer = new Timer(TOOLTIP_FORCE_HIDE_DELAY_MS, e -> hideTooltip());
    hideTimer.setRepeats(false);

    setupEventHandlers();

    ToolTipManager.sharedInstance().setInitialDelay(NATIVE_TOOLTIP_INITIAL_DELAY_MS);
    ToolTipManager.sharedInstance().setDismissDelay(NATIVE_TOOLTIP_DISMISS_DELAY_MS);
  }

  /**
   * Setup the event handlters for mouse actions
   */
  private void setupEventHandlers() {
    textArea.addMouseMotionListener(new MouseMotionAdapter() {
      @Override
      public void mouseMoved(MouseEvent e) {
        handleMouseMove(e);
      }
    });

    textArea.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseExited(MouseEvent e) {
        hideTooltip();
      }

      @Override
      public void mousePressed(MouseEvent e) {
        hideTooltip();
      }
    });
  }
  
  /**
   * Handle the mouse moving actions
   * 
   * @param e the mopuse event
   */
  private void handleMouseMove(MouseEvent e) {
    showTimer.stop();
    hideTimer.stop();

    int caretPos = textArea.viewToModel(e.getPoint());
    if (caretPos < 0) {
      hideTooltip();
      return;
    }

    int address = addressResolver.apply(caretPos);

    if (address != -1 && xrefManager.hasXRefs(address)) {
      currentAddress = address;
      showTimer.start();
    } else hideTooltip();
  }

  /**
   * Update tooltip if on an address
   */
  private void updateTooltip() {
    if (currentAddress == -1) {
      return;
    }

    String tooltipText = generateDetailedTooltipText(currentAddress);
    textArea.setToolTipText(tooltipText);

    // Force tooltip update
    if (textArea.getMousePosition() != null) {
      ToolTipManager.sharedInstance().mouseMoved(
              new MouseEvent(textArea, MouseEvent.MOUSE_MOVED,
                      System.currentTimeMillis(), 0,
                      (int) textArea.getMousePosition().getX(),
                      (int) textArea.getMousePosition().getY(), 0, false)
      );
    }

    hideTimer.setInitialDelay(TOOLTIP_AUTO_HIDE_DELAY_MS);
    hideTimer.start();
  }

  /**
   * Hide the tooltip
   */
  private void hideTooltip() {
    textArea.setToolTipText(null);
    showTimer.stop();
    hideTimer.stop();
    currentAddress = -1;
  }

  /**
   * Generate the HTML tooltip text for the given address
   * 
   * @param address the address for tooltip
   * @return the HTML text
   */
  private String generateDetailedTooltipText(int address) {
    XRefManager.XRefStats stats = xrefManager.getStatsForAddress(address);
    List<XRefManager.XRef> xrefs = xrefManager.getXRefsForAddress(address);

    StringBuilder sb = new StringBuilder();
    sb.append("<html><body style='font-family: Monospaced; font-size: 11px; max-width: 400px;'>");

    // Header
    sb.append("<b>Cross-Reference: $").append(String.format("%04X", address)).append("</b><br>");
    sb.append("<hr style='margin: 3px 0; border: 0; border-top: 1px solid #ccc;'>");

    // Complete statistics
    sb.append("<table style='width: 100%; border-collapse: collapse;'>");
    if (stats.readCount > 0) {
      sb.append("<tr><td style='padding: 1px 5px;'>• Read:</td><td style='padding: 1px 5px;'>").append(stats.readCount).append(" times</td></tr>");
    }
    if (stats.writeCount > 0) {
      sb.append("<tr><td style='padding: 1px 5px;'>• Write:</td><td style='padding: 1px 5px;'>").append(stats.writeCount).append(" times</td></tr>");
    }
    if (stats.callCount > 0) {
      sb.append("<tr><td style='padding: 1px 5px;'>• Called:</td><td style='padding: 1px 5px;'>").append(stats.callCount).append(" times</td></tr>");
    }
    if (stats.jumpCount > 0) {
      sb.append("<tr><td style='padding: 1px 5px;'>• Jumped:</td><td style='padding: 1px 5px;'>").append(stats.jumpCount).append(" times</td></tr>");
    }
    if (stats.branchCount > 0) {
      sb.append("<tr><td style='padding: 1px 5px;'>• Branched:</td><td style='padding: 1px 5px;'>").append(stats.branchCount).append(" times</td></tr>");
    }
    if (stats.pointerCount > 0) {
      sb.append("<tr><td style='padding: 1px 5px;'>• Pointer:</td><td style='padding: 1px 5px;'>").append(stats.pointerCount).append(" times</td></tr>");
    }
    if (stats.compareCount > 0) {
      sb.append("<tr><td style='padding: 1px 5px;'>• Compare:</td><td style='padding: 1px 5px;'>").append(stats.compareCount).append(" times</td></tr>");
    }
    if (stats.modifyCount > 0) {
      sb.append("<tr><td style='padding: 1px 5px;'>• Modify:</td><td style='padding: 1px 5px;'>").append(stats.modifyCount).append(" times</td></tr>");
    }
    if (stats.bitTestCount > 0) {
      sb.append("<tr><td style='padding: 1px 5px;'>• Bit Test:</td><td style='padding: 1px 5px;'>").append(stats.bitTestCount).append(" times</td></tr>");
    }

    sb.append("</table>");

    // Max 5 references
    if (!xrefs.isEmpty()) {
      sb.append("<hr style='margin: 5px 0; border: 0; border-top: 1px solid #ccc;'>");
      sb.append("<b>Reference Examples:</b><br>");
      sb.append("<table style='width: 100%; border-collapse: collapse; font-size: 10px;'>");

      int count = Math.min(5, xrefs.size());
      for (int i = 0; i < count; i++) {
        XRefManager.XRef xref = xrefs.get(i);
        String typeColor = getTypeColor(xref.type);
        String typeText = getTypeText(xref.type);

        sb.append("<tr>");
        sb.append("<td style='padding: 1px 3px; color: ").append(typeColor).append(";'>")
                .append(typeText).append("</td>");
        sb.append("<td style='padding: 1px 3px;'><b>$")
                .append(String.format("%04X", xref.sourceAddress)).append("</b></td>");
        sb.append("<td style='padding: 1px 3px; color: #666;'>")
                .append(escapeHtml(xref.instruction)).append("</td>");
        sb.append("</tr>");

        if (xref.context != null && !xref.context.isEmpty()) {
          sb.append("<tr><td colspan='3' style='padding: 1px 3px 2px 3px; color: #888; font-style: italic;'>")
                  .append("&nbsp;&nbsp;↳ ").append(escapeHtml(xref.context)).append("</td></tr>");
        }
      }

      if (xrefs.size() > 5) {
        sb.append("<tr><td colspan='3' style='padding: 2px 3px; color: #666; font-style: italic;'>")
                .append("... and ").append(xrefs.size() - 5).append(" more references</td></tr>");
      }

      sb.append("</table>");
    }

    // Footer with total
    sb.append("<hr style='margin: 5px 0; border: 0; border-top: 1px solid #ccc;'>");
    sb.append("<div style='color: #666; font-style: italic;'>")
            .append("Total: ").append(stats.totalReferences).append(" references</div>");

    sb.append("</body></html>");

    return sb.toString();
  }

  /**
   * Escape for html
   * 
   * @param text the text to escape
   * @return the escaped text
   */
  private String escapeHtml(String text) {
    if (text == null) {
      return "";
    }
    return text.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;");
  }

  /**
   * Get the type color 
   * 
   * @param type the type
   * @return the color for this type
   */
  private String getTypeColor(XRefManager.XRefType type) {
    switch (type) {
      case READ:
        return "#0066CC";   // Blue
      case WRITE:
        return "#CC0000";   // Red
      case CALL:
        return "#009900";   // Green
      case JUMP:
        return "#990099";   // Purple
      case BRANCH:
        return "#FF6600";   // Orange
      case POINTER:
        return "#666666";   // Gray
      case COMPARE:
        return "#006666";   // Dark green
      case MODIFY:
        return "#CC6600";   // Brown
      case BIT_TEST:
        return "#6600CC";   // Dark purple
      default:
        return "#000000";   // Black
    }
  }

  /**
   * Get the text for the type 
   * 
   * @param type the type 
   * @return the text for the type
   */
  private String getTypeText(XRefManager.XRefType type) {
    switch (type) {
      case READ:
        return "READ";
      case WRITE:
        return "WRITE";
      case CALL:
        return "CALL";
      case JUMP:
        return "JUMP";
      case BRANCH:
        return "BRANCH";
      case POINTER:
        return "POINTER";
      case COMPARE:
        return "COMPARE";
      case MODIFY:
        return "MODIFY";
      case BIT_TEST:
        return "BIT TEST";
      default:
        return "REF";
    }
  }

  /**
   * Set the show and hi delay time
   * 
   * @param showDelay the time for show delay
   * @param hideDelay the time for hide delay
   */
  public void setDelays(int showDelay, int hideDelay) {
    showTimer.setDelay(showDelay);
    hideTimer.setInitialDelay(hideDelay);
    ToolTipManager.sharedInstance().setInitialDelay(showDelay);
    ToolTipManager.sharedInstance().setDismissDelay(hideDelay);
  }

  /**
   * Dispose the tooltip
   */
  public void dispose() {
    hideTooltip();
  }
}
