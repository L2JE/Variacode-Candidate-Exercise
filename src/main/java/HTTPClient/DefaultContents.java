package HTTPClient;
/**
 * Default content types that are shown to user
 * this class maps each type to the mime-type that is used in http requests
 */
public enum DefaultContents {
    JSON("application/json"),
    PLAIN_TEXT("text/plain"),
    XML("text/xml"),
    JAVASCRIPT("text/javascript"),
    HTML("text/html");

    protected final String methodName;

    DefaultContents(String methodName) {
        this.methodName = methodName;
    }
}
