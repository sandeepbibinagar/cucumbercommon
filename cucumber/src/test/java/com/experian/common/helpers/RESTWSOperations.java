package com.experian.common.helpers;

import com.experian.common.helpers.Config;
import com.experian.common.helpers.RESTOperations;
import com.mashape.unirest.http.Headers;
import com.mashape.unirest.http.HttpMethod;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.w3c.dom.Document;

import java.util.*;

public class RESTWSOperations extends RESTOperations {

    public final Config config;
    public final static String RESPONSE_VARIABLE = "RESTResponse";

    public RESTWSOperations(String uri) throws Exception {
        config = new Config();
        url = config.get("base.webservices.url") + uri;
        Unirest.setDefaultHeader("Content-Type", "application/json");
    }

    public String execute(HttpMethod method, String userName, String password, Map<String, String> queryParams, String body, int expectedStatusCode, Map<String, String> stepData) throws Exception {

        HttpRequest request;

        switch (method) {
            case GET:
                request = Unirest.get(getUrl());
                break;
            case POST:
                request = Unirest.post(getUrl());
                ((HttpRequestWithBody) request).body(body);
                break;
            case PUT:
                request = Unirest.put(getUrl());
                ((HttpRequestWithBody) request).body(body);
                break;
            default:
                throw new RuntimeException("Unsupported REST HTTP method: " + method);

        }
        request.queryString(Collections.<String, Object>unmodifiableMap(queryParams));
        setAuthentication(request, userName, password);

        HttpResponse<String> response = request.asString();

        checkResponse(response, expectedStatusCode, stepData);

        return stepData.get(RESPONSE_VARIABLE);
    }

    public String execute(HttpMethod method, String userName, String password, Map<String, String> queryParams, Document body, int expectedStatusCode, Map<String, String> stepData) throws Exception {
        return execute(method, userName, password, queryParams, documentToString(body), expectedStatusCode, stepData);
    }

    public String execute(HttpMethod method, String userName, String password, String body, int expectedStatusCode, Map<String, String> stepData) throws Exception {
        return execute(method, userName, password, new HashMap<String, String>(), body, expectedStatusCode, stepData);
    }

    public String execute(HttpMethod method, String userName, String password, Document body, int expectedStatusCode, Map<String, String> stepData) throws Exception {
        return execute(method, userName, password, new HashMap<String, String>(), documentToString(body), expectedStatusCode, stepData);
    }

     public String execute(HttpMethod method, String userName, String password, int expectedStatusCode, Map<String, String> stepData) throws Exception {
        return execute(method, userName, password, new HashMap<String, String>(), "", expectedStatusCode, stepData);
    }

    public String execute(HttpMethod method, String userName, String password, Map<String, String> queryParams, int expectedStatusCode, Map<String, String> stepData) throws Exception {
        return execute(method, userName, password, queryParams, "", expectedStatusCode, stepData);
    }

    public void checkResponse(HttpResponse<String> response, int expectedStatusCode, Map<String, String> stepData) {
        if (response.getStatus() != expectedStatusCode) {
            throw new RuntimeException("Actual response status code " + response.getStatus() + " is not equal to expected " + expectedStatusCode);
        }
        //Check for body existence
        String responseBody = response.getBody();

        if (expectedStatusCode != HttpStatus.SC_NO_CONTENT) {
            if (responseBody == null) {
                throw new RuntimeException("Response body is null, but it should not be");
            }
            stepData.put(RESPONSE_VARIABLE, removeXmlNamespaces(responseBody));
        } else {
            if (responseBody != null) {
                throw new RuntimeException("Response body is not null, but it should be");
            }
            stepData.remove(RESPONSE_VARIABLE);
        }
    }
}
