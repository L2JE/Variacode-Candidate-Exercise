package HTTPClient;

import java.util.Map;

/**
 * Generic readOnly HTTPRequest that can be implemented with different http libraries
 */
public interface HTTPRequest {
    String getMethod();
    String getURI();
    Map<String, String> getHeaders();
    String getContent();

    /**
     * Sends the request using corresponding http client
     * @return a generic http response
     */
    HTTPResponse send();
}
