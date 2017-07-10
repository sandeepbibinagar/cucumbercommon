package com.experian.common.steps.webservices;

import com.experian.common.WebClient;
import com.experian.common.core.logger.Logger;
import com.experian.common.helpers.Config;
import com.experian.common.helpers.RestOperation;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.json.JSONArray;

import java.io.IOException;

import static org.testng.Assert.assertEquals;

/**
 * Created by c13200a on 7/3/2017.
 * Steps for creating a new application through the REST api
 */
public class NewApplicationSteps {

    private final WebClient webClient;
    private final Logger logger = Logger.getLogger(this.getClass());
    private final RestOperation restOperation;
    private final Config config;

    public NewApplicationSteps(WebClient webClient, RestOperation restOperation) throws IOException {
        this.webClient = webClient;
        this.restOperation = restOperation;
        this.config = new Config();
    }

    /**
     * Send a post request to the base application URL + the provided resource
     * @param resource Is the path to the resource (e.g. /v1/application/tenantID/serviceID)
     * @param filePath Is the path to the JSON file that contains the body of the request
     * @param username Is the username for authentication
     * @param password Is the password for authentication
     * @throws Throwable
     */
    @When("^I send POST request to resource (.*) with JSON file body (.*) authenticated by username (.*) and password (.*)$")
    public void sendPostRequestWithJSONFileBody(String resource, String filePath, String username, String password) throws Throwable {
        this.restOperation.sendPostRequestWithJSONFile(this.config.get("base.webservices.url") + resource,
                this.config.get("data.dir") + filePath,
                username,
                password);
    }

    /**
     * Send a post request to the base application URL + the provided resource
     * @param resource Is the path to the resource (e.g. /v1/application/tenantID/serviceID)
     * @param body Is the content of the JSON file for the body of the request
     * @param username Is the username for authentication
     * @param password Is the password for authentication
     * @throws Throwable
     */
    @When("^I send POST request to resource (.*) authenticated by username (.*) and password (.*) with body:$")
    public void sendPostRequestWithJSONStringBody(String resource, String username, String password, String body) throws Throwable {
        this.restOperation.sendPostRequestWithJSONString(this.config.get("base.webservices.url") + resource,
                body,
                username,
                password);
    }

    /**
     * Check the status text and status code of a REST call
     * @param statusText The expected status text of the response (e.g. OK)
     * @param statusCode The expected status code of the response (e.g. 200)
     * @throws Throwable
     */
    @Then("^I should receive a response from the application with status text (.*) and status code (\\d+)$")
    public void checkResponseStatus(String statusText, int statusCode) throws Throwable {
        assertEquals(this.restOperation.getStatusText(), statusText);
        this.logger.info("Response status code:  \"" + this.restOperation.getStatusCode() + "\"");
        assertEquals(this.restOperation.getStatusCode(), statusCode);
        this.logger.info("Response status text:  \"" + this.restOperation.getStatusText() + "\"");
    }

    /**
     * Check the value of an entry in the JSON response body of the application request
     * @param responseEntryKey The key of the response entry
     * @param responseEntryValue The expected value of the response entry
     * @throws Throwable
     */
    @And("^I should see that the expected decision for (.*) is (.*)$")
    public void checkResponseEntryValue(String responseEntryKey, String responseEntryValue) throws Throwable {
        JSONArray responseBody = this.restOperation.getResponseJsonBody();
        assertEquals(responseBody.getJSONObject(0).getJSONObject("data").getString(responseEntryKey), responseEntryValue);
        this.logger.info("Response value for: " + responseEntryKey + " is: " + responseBody.getJSONObject(0).getJSONObject("data").getString(responseEntryKey));
    }
}
