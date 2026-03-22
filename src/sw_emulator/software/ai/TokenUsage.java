/**
 * @(#)TokenUsage 2026/03/20
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
package sw_emulator.software.ai;

/**
 * Token usage as reported by the API for a single request.
 *
 *  * @author ice
 */
public class TokenUsage {
  private final int promptTokens;
  private final int completionTokens;
  private final int totalTokens;

  public TokenUsage(int promptTokens, int completionTokens, int totalTokens) {
    this.promptTokens = promptTokens;
    this.completionTokens = completionTokens;
    this.totalTokens = totalTokens;
  }

  /**
   * Tokens consumed by the system prompt + user message.
   */
  public int getPromptTokens() {
    return promptTokens;
  }

  /**
   * Tokens generated in the reply.
   */
  public int getCompletionTokens() {
    return completionTokens;
  }

  /**
   * promptTokens + completionTokens.
   */
  public int getTotalTokens() {
    return totalTokens;
  }

  @Override
  public String toString() {
    return "TokenUsage{prompt=" + promptTokens
            + ", completion=" + completionTokens
            + ", total=" + totalTokens + "}";
  }
}
