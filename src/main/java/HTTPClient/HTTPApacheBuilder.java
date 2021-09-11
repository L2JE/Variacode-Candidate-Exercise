package HTTPClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

/**
 * Basic Builder implementation using apache http library
 *
 */
public class HTTPApacheBuilder implements HTTPBuilder {

    String methodName;
    String url;
    String contentType;
    String body;

    HttpUriRequest request;

    @Override
    public void setMethod(String method) {
        this.methodName = method;
    }

    @Override
    public void setURL(String url) {
        this.url = url;
    }

    /**
     * sets content-type http header
     * @param contentType
     */
    @Override
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Generates a HTTP generic request with the previously given data
     * @return an implementation for HTTPRequest
     * @throws Exception if is trying to be constructed with invalid data
     */
    @Override
    public HTTPRequest construct() throws Exception {

        switch (this.methodName){
            case "GET":
                request = new HttpGet(this.url);
                break;
            case "DELETE":
                request = new HttpDelete(this.url);
                break;
            case "POST":
                request = new HttpPost(this.url);
                break;
            case "PUT":
                request = new HttpPut(this.url);
                break;
            case "PATCH":
                request = new HttpPatch(this.url);
                break;
            default:
                throw new Exception("No Method defined");
        }

        if (request instanceof HttpEntityEnclosingRequestBase){
            request.setHeader("Content-type", this.contentType);
            ((HttpEntityEnclosingRequestBase)request).setEntity(new StringEntity(this.body));
        }

        return new HTTPApacheRequest(HttpClients.createDefault(), this.request);
    }
}
