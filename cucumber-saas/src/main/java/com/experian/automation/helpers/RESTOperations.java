package com.experian.automation.helpers;

import com.mashape.unirest.request.HttpRequest;

public class RESTOperations extends APIOperations {

    public void setAuthentication(HttpRequest request, String userName, String password) {
        request.basicAuth(userName, password);
    }
}