package HTTPClient;

/**
 * Builder generic class based on builder design pattern for any type of HTTPClient
 */
public interface HTTPBuilder {
    void setMethod(String method);
    void setURL(String url);
    void setContentType(String contentType);
    void setBody(String body);

    /**
     * Constructs the HTTP Request using previously given data
     * @return a generic HTTPRequest that can be sent
     * @throws Exception for any invalid data
     */
    HTTPRequest construct() throws Exception;
}
