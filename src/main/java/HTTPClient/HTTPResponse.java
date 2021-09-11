package HTTPClient;

/**
 * Generic readOnly HTTPResponse that can be implemented with different http libraries
 */
public interface HTTPResponse {
    int getStatusCode();
    String getContent();
}
