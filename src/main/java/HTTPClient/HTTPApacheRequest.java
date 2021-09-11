package HTTPClient;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Basic Http request using apache java library
 */
public class HTTPApacheRequest implements HTTPRequest {
    private final CloseableHttpClient apacheClient;
    private final HttpUriRequest request;
    private String content = "";

    public HTTPApacheRequest(CloseableHttpClient apacheClient, HttpUriRequest request){
        this.apacheClient = apacheClient;
        this.request = request;
    }

    @Override
    public String getMethod() {
        return this.request.getMethod();
    }

    @Override
    public String getURI() {
        return this.request.getURI().toString();
    }

    @Override
    public Map<String, String> getHeaders() {
        HashMap<String,String> headersMap = new HashMap<>();
        Header[] headers = this.request.getAllHeaders();

        for(Header header : headers){
            headersMap.put(header.getName(), header.getValue());
        }

        return headersMap;
    }

    @Override
    public String getContent() {
        if((content.equals("")) && (request instanceof HttpEntityEnclosingRequestBase)) {
            final String encoding = "UTF-8";
            InputStream bodyStream;
            ByteArrayOutputStream result = new ByteArrayOutputStream();

            try {
                bodyStream = ((HttpEntityEnclosingRequestBase)request).getEntity().getContent();
                byte[] buffer = new byte[1024];
                for (int length; (length = bodyStream.read(buffer)) != -1; ) {
                    result.write(buffer, 0, length);
                }
                content = result.toString(encoding);
            } catch (UnsupportedOperationException | IOException e) {
                System.err.println(e.getLocalizedMessage());
            }
        }

        return content;
    }

    public HTTPResponse send() {
        HTTPApacheResponse response = null;
        try {
            response = new HTTPApacheResponse(apacheClient.execute(request));
        } catch (IOException e) {
            System.err.println("An error when SENDING THE REQUEST has occurred");
        } finally {
            try {
                apacheClient.close();
            } catch (IOException e) {
                System.err.println("An error when CLOSING THE CLIENT has occurred");
            }
        }
        return response;
    }
}
