package com.experian.automation.saas.steps.webservices;

import com.experian.automation.harnesses.TestHarness;
import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.saas.helpers.RESTWSOperations;
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

    private final TestHarness testHarness;
    private final WebHarness webHarness;
    private final Logger logger = Logger.getLogger(this.getClass());

    public NewApplicationSteps(TestHarness testHarness, WebHarness webHarness) throws IOException {
        this.testHarness = testHarness;
        this.webHarness = webHarness;
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
        String response = restwsOperations.execute(HttpMethod.POST, username, password,body,expectedStatusCode,testHarness.stepData);
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
        String responseVariable = testHarness.getStepData(RESTWSOperations.RESPONSE_VARIABLE);
        HashMap map = JsonPath.read(responseVariable, "$.data");
        assertTrue(map.containsKey(responseEntryKey)&&map.get(responseEntryKey).equals(responseEntryValue),"Expected key-value pair "+responseEntryKey+" : "+responseEntryValue);
    }
}
