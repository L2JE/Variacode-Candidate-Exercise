package HTTPClient;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

public class HTTPApacheBuilderTest extends TestCase {

    private class MockRequest implements HTTPRequest{

        final String method;
        final String url;
        final String contentType;
        final String body;

        public MockRequest(String method, String url, String contentType, String body) {
            this.method = method;
            this.url = url;
            this.contentType = contentType;
            this.body = body;
        }

        @Override
        public String getMethod() {
            return this.method;
        }

        @Override
        public String getURI() {
            return this.url;
        }

        @Override
        public Map<String, String> getHeaders() {
            Map<String, String> map = new HashMap<>();
            map.put("Content-type", this.contentType);
            return map;
        }

        @Override
        public String getContent() {
            return this.body;
        }

        @Override
        public HTTPResponse send() {
            return null;
        }

        @Override
        public boolean equals(Object o) {
            boolean equals = super.equals(o);
            if(!equals && o instanceof HTTPRequest){
                HTTPRequest otherReq = (HTTPRequest) o;
                equals = otherReq.getMethod().equals(this.method) &&
                        otherReq.getURI().equals(this.url) &&
                        otherReq.getHeaders().equals(this.getHeaders()) &&
                        otherReq.getContent().equals(this.body);
            }
            return equals;
        }
    }

    /**
     * A test to check if builds a valid request given valid data
     */
    public void testShouldConstructValidRequest() {
        HTTPApacheBuilder builder = new HTTPApacheBuilder();
        final String method = "POST";
        final String url = "https://reqres.in/api/users";
        final String contentType = "application/json";
        final String body = "{\n" +
                "    \"name\": \"morpheus\",\n" +
                "    \"job\": \"leader\"\n" +
                "}";
        MockRequest expected = new MockRequest(method, url, contentType, body);

        builder.setMethod(method);
        builder.setContentType(contentType);
        builder.setBody(body);
        builder.setURL(url);

        HTTPRequest request = null;
        try {
            request = builder.construct();
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(expected, request);
    }
}