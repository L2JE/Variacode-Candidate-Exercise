package HTTPClient;

import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Basic Http request using apache java library
 */
public class HTTPApacheResponse implements HTTPResponse{

    private final String encoding = "UTF-8";
    private final CloseableHttpResponse apacheResponse;
    private int status;
    private String content = "";

    public HTTPApacheResponse(CloseableHttpResponse apacheResponse) {
        this.apacheResponse = apacheResponse;
    }

    /**
     * @return the status code from the wrapped apache response
     */
    @Override
    public int getStatusCode() {
        this.status = apacheResponse.getStatusLine().getStatusCode();
        return this.status;
    }

    /**
     * Converts the content input stream to a String and closes the stream
     * @return the body of the http response
     */
    @Override
    public String getContent() {
        if(this.content.equals("")){
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            try {
                InputStream bodyStream = this.apacheResponse.getEntity().getContent();
                byte[] buffer = new byte[1024];

                for (int length; (length = bodyStream.read(buffer)) != -1; ) {
                    result.write(buffer, 0, length);
                }

                this.content = result.toString(this.encoding);
                this.apacheResponse.close();
            } catch (UnsupportedOperationException | IOException e) {
                System.err.println(e.getLocalizedMessage());
            }
        }
        return this.content;
    }
}
