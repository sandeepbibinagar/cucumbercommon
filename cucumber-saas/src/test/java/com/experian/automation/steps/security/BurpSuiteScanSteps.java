/**
 * Copyright (c) Experian, 2017. All rights reserved.
 */
package com.experian.automation.steps.security;

import com.experian.automation.WebClient;
import com.experian.automation.logger.Logger;
import com.experian.automation.steps.security.helpers.BurpSuiteRestClient;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Created by c01266a on 8/31/2017.
 */
public class BurpSuiteScanSteps {
    private final WebClient webClient;
    private final Logger logger = Logger.getLogger(this.getClass());

    private BurpSuiteRestClient client;
    private String siteId;
    private String scanId;

    public BurpSuiteScanSteps(WebClient webClient) {
        this.webClient = webClient;
    }

    @Given("^I log into Burp Suite Enterprise with valid credentials")
    public void iLogIntoBurpSuiteEnterpriseOnURLWithUsernameAndPassword() {
        String burpUrl = webClient.config.get("burp.url");
        String burpUsername = webClient.config.get("burp.username");
        String burpPassword = webClient.config.get("burp.password");
        client = new BurpSuiteRestClient(burpUrl, burpUsername, burpPassword.toCharArray());
    }

    @Given("^I create Burp Suite Site with username \"([^\"]*)\" and password \"([^\"]*)\"$")
    public void iCreateBurpSuiteSiteWithUsernameAndPassword(String username, String password) {
        String baseURL = webClient.config.get("base.url");
        String appName = webClient.config.get("app.name");
        siteId = client.burpScannerCreateSite(baseURL, appName, username, password.toCharArray());
    }

    @When("^I run scan$")
    public void iRunScan() {
        String scheduleItemId = client.burpScannerCreateScan(siteId);

        // wait for scan to complete, check every minute
        // max time wait 1 hour
        long totalExecutionTime = 60*60*1000;
        long startTime = System.currentTimeMillis();

        while(System.currentTimeMillis() - startTime < totalExecutionTime) {
            scanId = client.checkScanFinished(scheduleItemId);
            if (scanId != null) {
                break;
            }
            try {
                Thread.sleep(60000);
            } catch (InterruptedException ie) {
                logger.error("Wait for scan to finish interrupted...");
            }
        }
    }

    @Then("^I check for vulnerabilities$")
    public void iCheckForVulnerabilities() {
        assertFalse(client.failBurpScannerReportScanResultsTest(scanId),
                "High or medium severity issues found. Security baseline failed.");
    }

    @And("^I delete the site$")
    public void iDeleteTheSite() {
        assertTrue(client.burpScannerDeleteSite(siteId), "Site not deleted.");
    }
}
