package HTTPClient;

import junit.framework.TestCase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.UnsupportedEncodingException;

public class HTTPApacheRequestTest extends TestCase {
    private HTTPApacheRequest generateTestPostRequest(String url, String contentType, String content) {
        HttpPost apacheReq = new HttpPost(url);
        apacheReq.setHeader("Content-type", contentType);
        try {
            apacheReq.setEntity(new StringEntity(content));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new HTTPApacheRequest(HttpClients.createDefault(), apacheReq);
    }


    /**
     * A test when sending a valid post request with content entered by user
     * the server exists and responses with data
     * In this case it uses the api from https://reqres.in/
     */
    public void testShouldReceiveExpectedContentResponse() {
        HTTPApacheRequest request = generateTestPostRequest("https://reqres.in/api/users",
                "application/json",
                "{\n" +
                        "    \"name\": \"morpheus\",\n" +
                        "    \"job\": \"leader\"\n" +
                        "}");

        String expectedResponse = request.getContent()
                                    .replaceAll(" |}", "")
                                    .replaceAll("\n", "")
                                    .concat(",\"id\":\"");

        assertTrue(request.send().getContent().contains(expectedResponse));
    }
}