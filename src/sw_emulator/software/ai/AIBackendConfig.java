/**
 * @(#)AIBackendConfig 2026/03/22
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
 * Describes a complete AI backend configuration.
 *
 * A backend is any service that exposes an OpenAI-compatible
 * /v1/chat/completions endpoint. This includes:
 *
 *   - LM Studio (local, no key)
 *   - Anthropic Claude  (https://api.anthropic.com/v1/chat/completions)
 *   - Google Gemini     (https://generativelanguage.googleapis.com/v1beta/openai/chat/completions)
 *   - OpenAI            (https://api.openai.com/v1/chat/completions)
 *   - OpenRouter        (https://openrouter.ai/api/v1/chat/completions)
 *   - any other compatible service
 *
 * Pre-built instances are available as static factory methods, e.g.:
 *   AIBackendConfig cfg = AIBackendConfig.lmStudio();
 *   AIBackendConfig cfg = AIBackendConfig.anthropic("sk-ant-...", "claude-haiku-4-5-20251001");
 *   AIBackendConfig cfg = AIBackendConfig.gemini("AIza...", "gemini-2.0-flash");
 *
 * Custom backends:
 *   AIBackendConfig cfg = new AIBackendConfig.Builder("My provider")
 *           .endpoint("https://my-provider.com/v1/chat/completions")
 *           .model("my-model-id")
 *           .apiKey("my-key")
 *           .authHeader("Authorization", "Bearer")   // default
 *           .extraHeader("x-custom-header", "value")
 *           .maxOutputTokens(4096)
 *           .build();
 *
 * Instances are immutable and safe to share across threads.
 *
 * @author ice
 */
public final class AIBackendConfig {
  
   /** 
    * Name of the AI
    */ 
   public enum AI {
     LM_STUDIO_LOC {
       @Override
       public String getName() {
         return "LM Studio local";
       }
     },     
     LM_STUDIO_CUST {
       @Override
       public String getName() {
         return "LM Studio custom";
       }
     },     
     CLAUDE {
       @Override
       public String getName() {
         return "Antropic Claude";
       }
     },     
     GEMINI {
       @Override
       public String getName() {
         return "Google Gemini";
       }
     },     
     OPENAI {
       @Override
       public String getName() {
         return "OpenAI";
       }
     },     
     OPEN_ROUTE {
       @Override
       public String getName() {
         return "Open Route";
       }
     },
     OPEN_CODE {
      @Override
      public String getName() {
        return "OpenCode.ai";
      }
     };        
    
     /**
      * Get the name of assembler
      *
      * @return the name of assembler
      */
     public abstract String getName();
   }  
  

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /** Human-readable name shown in the UI (e.g. "Anthropic Claude"). */
  private final String displayName;

  /** Full URL of the /v1/chat/completions endpoint. */
  private final String endpoint;

  /** Model identifier sent in the request body. */
  private final String model;

  /** API key, or null if not required (e.g. LM Studio). */
  private final String apiKey;

  /**
   * Name of the HTTP header used to carry the API key.
   * Default: "Authorization"  (value will be "Bearer <apiKey>").
   * Some providers use custom headers (e.g. "x-goog-api-key" for Gemini).
   */
  private final String authHeaderName;

  /**
   * Prefix for the auth header value, or null for raw key.
   * e.g. "Bearer" → "Authorization: Bearer sk-ant-..."
   *      null     → "x-goog-api-key: AIza..."
   */
  private final String authHeaderPrefix;

  /**
   * Extra headers required by some providers (e.g. Anthropic needs
   * "anthropic-version: 2023-06-01").
   * Stored as alternating name/value pairs: [name0, value0, name1, value1, …]
   */
  private final String[] extraHeaders;

  /** Maximum tokens the model may generate in its response. */
  private final int maxOutputTokens;

  /**
   * Whether this backend requires local TCP reachability check before
   * the HTTP call. True for LM Studio, false for cloud services.
   */
  private final boolean localReachabilityCheck;

  /** Local host for reachability check (only used when localReachabilityCheck=true). */
  private final String localHost;

  /** Local port for reachability check (only used when localReachabilityCheck=true). */
  private final int localPort;

  // ---------------------------------------------------------------------------
  // Private constructor – use Builder or factory methods
  // ---------------------------------------------------------------------------

  private AIBackendConfig(Builder b) {
    this.displayName            = b.displayName;
    this.endpoint               = b.endpoint;
    this.model                  = b.model;
    this.apiKey                 = b.apiKey;
    this.authHeaderName         = b.authHeaderName;
    this.authHeaderPrefix       = b.authHeaderPrefix;
    this.extraHeaders           = b.extraHeaders.toArray(new String[0]);
    this.maxOutputTokens        = b.maxOutputTokens;
    this.localReachabilityCheck = b.localReachabilityCheck;
    this.localHost              = b.localHost;
    this.localPort              = b.localPort;
  }

  // ---------------------------------------------------------------------------
  // Getters
  // ---------------------------------------------------------------------------

  public String  getDisplayName()            { return displayName; }
  public String  getEndpoint()               { return endpoint; }
  public String  getModel()                  { return model; }
  public String  getApiKey()                 { return apiKey; }
  public String  getAuthHeaderName()         { return authHeaderName; }
  public String  getAuthHeaderPrefix()       { return authHeaderPrefix; }
  public String[] getExtraHeaders()          { return extraHeaders.clone(); }
  public int     getMaxOutputTokens()        { return maxOutputTokens; }
  public boolean isLocalReachabilityCheck()  { return localReachabilityCheck; }
  public String  getLocalHost()              { return localHost; }
  public int     getLocalPort()              { return localPort; }

  public boolean requiresApiKey() { return apiKey != null && !apiKey.isBlank(); }

  @Override
  public String toString() {
    return displayName + " [" + model + "] → " + endpoint;
  }

  // ---------------------------------------------------------------------------
  // Pre-built factory methods
  // ---------------------------------------------------------------------------

  /**
   * LM Studio running on localhost:1234.
   * No API key required.
   * 
   * @return LM Studio instance
   */
  public static AIBackendConfig lmStudio() {
    return new Builder("LM Studio (local)")
            .endpoint("http://localhost:1234/v1/chat/completions")
            .model("local-model")
            .localReachabilityCheck("localhost", 1234)
            .maxOutputTokens(8192)
            .build();
  }

  /**
   * LM Studio on a custom host/port.
   * 
   * @param host the host to use
   * @param port the port to use
   * @return LM Studio instance
   * 
   */
  public static AIBackendConfig lmStudio(String host, int port) {
    return new Builder("LM Studio (" + host + ":" + port + ")")
            .endpoint("http://" + host + ":" + port + "/v1/chat/completions")
            .model("local-model")
            .localReachabilityCheck(host, port)
            .maxOutputTokens(8192)
            .build();
  }

  /**
   * Anthropic Claude via the OpenAI-compatible endpoint.
   *
   * @param apiKey  Anthropic API key (sk-ant-...)
   * @param model   e.g. "claude-haiku-4-5-20251001", "claude-sonnet-4-6"
   * @return Claude instance
   */
  public static AIBackendConfig anthropic(String apiKey, String model) {
    return new Builder("Anthropic Claude")
            .endpoint("https://api.anthropic.com/v1/chat/completions")
            .model(model)
            .apiKey(apiKey)
            .extraHeader("anthropic-version", "2023-06-01")
            .maxOutputTokens(8192)
            .build();
  }

  /**
   * Google Gemini via its OpenAI-compatible endpoint.
   * Gemini uses a raw key in a custom header instead of Bearer auth.
   *
   * @param apiKey  Google AI API key (AIza...)
   * @param model   e.g. "gemini-2.0-flash", "gemini-1.5-pro"
   * @return Gemini instance
   */
  public static AIBackendConfig gemini(String apiKey, String model) {
    return new Builder("Google Gemini")
            .endpoint("https://generativelanguage.googleapis.com/v1beta/openai/chat/completions")
            .model(model)
            .apiKey(apiKey)
            .authHeader("x-goog-api-key", null)  // raw key, no "Bearer" prefix
            .maxOutputTokens(8192)
            .build();
  }

  /**
   * OpenAI GPT models.
   *
   * @param apiKey  OpenAI API key (sk-...)
   * @param model   e.g. "gpt-4o-mini", "gpt-4o"
   * @return OpenAI instance
   */
  public static AIBackendConfig openAI(String apiKey, String model) {
    return new Builder("OpenAI")
            .endpoint("https://api.openai.com/v1/chat/completions")
            .model(model)
            .apiKey(apiKey)
            .maxOutputTokens(8192)
            .build();
  }

  /**
   * OpenRouter – routes to many providers with a single API key.
   *
   * @param apiKey  OpenRouter API key
   * @param model   e.g. "mistralai/mistral-7b-instruct", "google/gemma-3-4b"
   * @return Open Router instance
   */
  public static AIBackendConfig openRouter(String apiKey, String model) {
    return new Builder("OpenRouter")
            .endpoint("https://openrouter.ai/api/v1/chat/completions")
            .model(model)
            .apiKey(apiKey)
            .maxOutputTokens(8192)
            .build();
  }
  
  /**
   * OpenCode.ai – OpenAI‑compatible endpoint.
   *
   * @param apiKey  OpenCode API key
   * @param model   e.g. "opencode/llama-3.1-8b", "opencode/qwen-2.5-7b"
   * @return OpenCode instance
   */
  public static AIBackendConfig openCode(String apiKey, String model) {
      return new Builder("OpenCode.ai")
              .endpoint("https://api.opencode.ai/v1/chat/completions")
              .model(model)
              .apiKey(apiKey)
              .maxOutputTokens(8192)
              .build();
  }


  // ---------------------------------------------------------------------------
  // Builder
  // ---------------------------------------------------------------------------

  /**
   * Builder for the configuration
   */
  public static final class Builder {

    private final String            displayName;
    private String                  endpoint         = "";
    private String                  model            = "";
    private String                  apiKey           = null;
    private String                  authHeaderName   = "Authorization";
    private String                  authHeaderPrefix = "Bearer";
    private final java.util.List<String> extraHeaders = new java.util.ArrayList<>();
    private int                     maxOutputTokens  = 8192;
    private boolean                 localReachabilityCheck = false;
    private String                  localHost        = "localhost";
    private int                     localPort        = 1234;

    /**
     * Build with the given display name
     * 
     * @param displayName the name to display
     */
    public Builder(String displayName) {
      this.displayName = displayName;
    }

    public Builder endpoint(String endpoint)          { this.endpoint = endpoint; return this; }
    public Builder model(String model)                { this.model = model; return this; }
    public Builder apiKey(String apiKey)              { this.apiKey = apiKey; return this; }
    public Builder maxOutputTokens(int max)           { this.maxOutputTokens = max; return this; }

    /**
     * Sets the auth header name and optional prefix.
     *
     * @param headerName   e.g. "Authorization" or "x-goog-api-key"
     * @param valuePrefix  e.g. "Bearer", or null for raw key
     * @return the auth header
     */
    public Builder authHeader(String headerName, String valuePrefix) {
      this.authHeaderName   = headerName;
      this.authHeaderPrefix = valuePrefix;
      return this;
    }

    /** 
     * Adds an extra HTTP header (e.g. "anthropic-version", "2023-06-01").
     * 
     * @param name name of property
     * @param value value of property
     * @return the extra header
     */
    public Builder extraHeader(String name, String value) {
      extraHeaders.add(name);
      extraHeaders.add(value);
      return this;
    }

    /** 
     * Enables TCP reachability pre-check for local backends.
     * 
     * @param host the host to use
     * @param port the port to use
     * @return the local reachability check
     */
    public Builder localReachabilityCheck(String host, int port) {
      this.localReachabilityCheck = true;
      this.localHost = host;
      this.localPort = port;
      return this;
    }

    /**
     * Build the AI config
     * 
     * @return the backend config
     */
    public AIBackendConfig build() {
      if (endpoint.isBlank()) throw new IllegalStateException("endpoint is required");
      if (model.isBlank())    throw new IllegalStateException("model is required");
      return new AIBackendConfig(this);
    }
  }
}

