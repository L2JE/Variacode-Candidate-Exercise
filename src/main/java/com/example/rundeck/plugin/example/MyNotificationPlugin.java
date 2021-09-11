package com.example.rundeck.plugin.example;

import HTTPClient.*;
import Validation.Binder;
import com.dtolabs.rundeck.plugins.descriptions.*;
import com.dtolabs.rundeck.plugins.notification.NotificationPlugin;
import com.dtolabs.rundeck.core.plugins.Plugin;

import java.util.*;

/**
 * Plugin that allows users to define their own custom notification for any server.
 * Inspired on postman api
 */
@Plugin(service="Notification",name="my-example")
@PluginDescription(title="Custom HTTP request", description="Define a custom http-request to notify url when trigger is fired.")
public class MyNotificationPlugin implements NotificationPlugin{
    //TODO: Replace url regex with the best url regex found written in php @^(https?|ftp)://[^\s/$.?#].[^\s]*$@iS

    private static final Map<String, DefaultContents> defaultValidContentTypes = new HashMap<>();
    private static final String validURLRegex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

    @PluginProperty(name = "methodField", title = "Method", description = "http method", defaultValue = "GET", required = true)
    @SelectValues(values = {"GET", "POST", "DELETE", "PUT", "PATCH"})
    private String methodField;

    @PluginProperty(name = "urlField",title = "URL",description = "URL to send the request", required = true)
    private String urlField;

    @PluginProperty(name = "contentTypeField",title = "Content Type",description = "Select predefined content type or enter manually with valid mime-type format: type/subtype")
    @SelectValues(values = {"JSON", "XML", "Javascript", "HTML", "Plaintext"}, freeSelect = true)
    private String contentTypeField;

    @PluginProperty(name = "bodyField",title = "Body",description = "Content of the request")
    @TextArea
    private String bodyField;

    public MyNotificationPlugin(){
        generateValidContents();
    }

    /**
     * generates entries in the map for each default content-type
     */
    private void generateValidContents() {
        defaultValidContentTypes.put("JSON", DefaultContents.JSON);
        defaultValidContentTypes.put("XML", DefaultContents.XML);
        defaultValidContentTypes.put("Javascript", DefaultContents.JAVASCRIPT);
        defaultValidContentTypes.put("HTML", DefaultContents.HTML);
        defaultValidContentTypes.put("Plaintext", DefaultContents.PLAIN_TEXT);
    }

    /**
     * @param trigger the reason that fired the method (start, success, failure)
     * @param executionData all data from running environment
     * @param config object attributes
     * @return returns true if the response code from http request is 2xx
     */
    public boolean postNotification(String trigger, Map executionData, Map config) {
        HTTPBuilder builder = new HTTPApacheBuilder();
        HTTPResponse response = null;
        Binder binder = new Binder();

        try{
            String configMethod = (String) config.get("methodField");
            binder.forField(configMethod)
                    .withValidator(field -> field != null)
                    .execute(commandArg -> builder.setMethod((String) commandArg));

            binder.forField((String) config.get("urlField"))
                    .withValidator(field -> field != null)
                    .withValidator(field -> ((String) field).matches(validURLRegex))
                    .execute(commandArg -> builder.setURL((String) commandArg));

            if(!(configMethod.equals("GET") || configMethod.equals("DELETE"))){
                binder.forField((String) config.get("contentTypeField"))
                        .withValidator(field -> true)
                        .withConverter(field -> {
                            DefaultContents defaultContent = defaultValidContentTypes.get(field.toString());
                            return (defaultContent != null)? defaultContent : field;
                        })
                        .execute(field -> builder.setContentType(field.toString()));

                binder.forField((String) config.get("bodyField"))
                        .execute(commandArg -> builder.setBody((String) commandArg));
            }

            response = builder.construct().send();
        }catch (Exception e){
            System.err.printf("ERROR: %s", e);
        }

        return notifyStatus(response);
    }

    /**
     * Verifies the response status and prints its content on the stdout
     * @param response from a previously executed request
     * @return true if the status code is 2xx
     */
    private boolean notifyStatus(HTTPResponse response) {
        int responseCode = (response != null)? response.getStatusCode() : -1;
        boolean isSuccess = (responseCode > 199)&&(responseCode < 300);

        System.out.flush();
        if(isSuccess){
            System.out.printf("Notification successfully sent. Status Code (%s)\n", responseCode);
            System.out.println(response.getContent());
        }else{
            System.err.printf("Notification sent with errors. Status Code (%s)\n", responseCode);
            if(responseCode > -1){
                System.err.println(response.getContent());
            }
        }
        return isSuccess;
    }

}