package com.experian.automation.steps.webservices;

import com.experian.automation.WebClient;
import com.experian.automation.helpers.RESTWSOperations;
import com.experian.automation.logger.Logger;

import com.jayway.jsonpath.JsonPath;
import com.mashape.unirest.http.HttpMethod;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;

import java.io.IOException;
import java.util.HashMap;

import static org.testng.Assert.assertTrue;

/**
 * Created by c13200a on 7/3/2017.
 * Steps for creating a new application through the REST api
 */
public class NewApplicationSteps {

    private final WebClient webClient;
    private final Logger logger = Logger.getLogger(this.getClass());

    public NewApplicationSteps(WebClient webClient) throws IOException {
        this.webClient = webClient;
    }


    /**
     * Send a post request to the base application URL + the provided resource
     * @param resource Is the path to the resource (e.g. /v1/application/tenantID/serviceID)
     * @param body Is the content of the JSON file for the body of the request
     * @param username Is the username for authentication
     * @param password Is the password for authentication
     * @throws Throwable
     */
    @And("^I send POST request to (.*) with username (.*) and password ([^\\s]+) and receive status code HTTP (\\d+):$")
    public void sendPostRequestWithJSONStringBody(String resource, String username, String password,Integer expectedStatusCode, String body) throws Throwable {

        RESTWSOperations restwsOperations = new RESTWSOperations(resource);
        String response = restwsOperations.execute(HttpMethod.POST, username, password,body,expectedStatusCode,webClient.stepData);
        logger.info(RESTWSOperations.RESPONSE_VARIABLE+":"+response);
    }



    /**
     * Check the value of an entry in the JSON response body of the application request
     * @param responseEntryKey The key of the response entry
     * @param responseEntryValue The expected value of the response entry
     * @throws Throwable
     */

    @Then("^I verify that the expected decision for (.*) is (.*)$")
    public void checkResponseEntryValue(String responseEntryKey, String responseEntryValue) throws Throwable {
        String responseVariable = webClient.getStepData(RESTWSOperations.RESPONSE_VARIABLE);
        HashMap map = JsonPath.read(responseVariable, "$.data");
        assertTrue(map.containsKey(responseEntryKey)&&map.get(responseEntryKey).equals(responseEntryValue),"Expected key-value pair "+responseEntryKey+" : "+responseEntryValue);
    }
}
