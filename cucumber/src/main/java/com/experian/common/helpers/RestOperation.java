package com.experian.common.helpers;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by c13200a on 7/3/2017.
 * This class encapsulates all features of the Unirest framework for executing REST requests
 */
public class RestOperation {

    private HttpRequest request;

    public RestOperation() throws IOException {
        Unirest.clearDefaultHeaders();
    }

    /**
     * Parse a JSON file to a JSON object by given filepath
     * @param bodyFilePath the path to the JSON file
     * @return
     * @throws Throwable
     */
    private String parseJSONFile(String bodyFilePath) throws Throwable {
        JSONParser parser = new JSONParser();
        JSONObject body = (JSONObject)parser.parse(new FileReader(new File(bodyFilePath)));
        return body.toJSONString();
    }

    /**
     * Send a post request by given resource, String for body, username and password
     * @param url The path to the resource to which the request will be send
     * @param body The contents of the JSON file for the body of the request
     * @param username Username for authentication of the request
     * @param password Password for authentication of the request
     */
    private void post(String url, String body, String username, String password) {
        this.request = Unirest.post(url)
                .basicAuth(username, password).header("Content-Type","application/json")
                .body(body)
                .getHttpRequest();
    }

    /**
     * Send a post request by given resource, pathToJSONFIle, username and password
     * @param url The path to the resource to which the request will be send
     * @param bodyFilePath Path to the JSON file that is going to be the body of the request
     * @param username Username for authentication of the request
     * @param password Password for authentication of the request
     * @throws Throwable
     */
    public void sendPostRequestWithJSONFile(String url, String bodyFilePath, String username, String password) throws Throwable {
        this.post(url, this.parseJSONFile(bodyFilePath), username, password);
    }

    /**
     * Send a post request by given resource, JSON String for body, username and password
     * @param url The path to the resource to which the request will be send
     * @param body The contents of the JSON file for the body of the request
     * @param username Username for authentication of the request
     * @param password Password for authentication of the request
     */
    public void sendPostRequestWithJSONString(String url, String body, String username, String password) {
        this.post(url, body, username, password);
    }

    /**
     * Get the status text for the current request
     * @return the contents of the status text
     * @throws UnirestException
     */
    public String getStatusText() throws UnirestException {
        return this.request.asString().getStatusText();
    }

    /**
     * Get the status code for the current request
     * @return integer value representing the exit code for the request (e.g. 200, 404, 500 etc)
     * @throws UnirestException
     */
    public int getStatusCode() throws UnirestException {
        return this.request.asString().getStatus();
    }

    /**
     * Get the body of the current request as a JSONArray
     * @return An array of maps that represents JSOn nodes form the response
     * @throws UnirestException
     */
    public JSONArray getResponseJsonBody() throws UnirestException {
        return this.request.asJson().getBody().getArray();
    }
}
