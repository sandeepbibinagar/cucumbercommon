/**
 * Copyright (c) Experian, 2017. All rights reserved.
 */
package com.experian.automation.saas.steps.security;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import com.experian.automation.harnesses.TestHarness;
import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.helpers.Config;
import com.experian.automation.logger.Logger;
import com.experian.automation.saas.steps.security.helpers.BurpSuiteRestClient;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * Created by c01266a on 8/31/2017.
 */
public class BurpSuiteScanSteps {

  private final TestHarness testHarness;
  private final WebHarness webHarness;
  private final Logger logger = Logger.getLogger(this.getClass());

  private BurpSuiteRestClient client;
  private String siteId;
  private String scanId;

  public BurpSuiteScanSteps(TestHarness testHarness, WebHarness webHarness) {
    this.testHarness = testHarness;
    this.webHarness = webHarness;
  }

  @Given("^I log into Burp Suite Enterprise with valid credentials")
  public void iLogIntoBurpSuiteEnterpriseOnURLWithUsernameAndPassword() {
    String burpUrl = Config.get("burp.url");
    String burpUsername = Config.get("burp.username");
    String burpPassword = Config.get("burp.password");
    client = new BurpSuiteRestClient(burpUrl, burpUsername, burpPassword.toCharArray());
  }

  @Given("^I create Burp Suite Site with username \"([^\"]*)\" and password \"([^\"]*)\"$")
  public void iCreateBurpSuiteSiteWithUsernameAndPassword(String username, String password) {
    String baseURL = Config.get("base.url");
    String appName = Config.get("app.name");
    siteId = client.burpScannerCreateSite(baseURL, appName, username, password.toCharArray());
  }

  @When("^I run scan$")
  public void iRunScan() {
    String scheduleItemId = client.burpScannerCreateScan(siteId);

    // wait for scan to complete, check every minute
    // max time wait 1 hour
    long totalExecutionTime = 60 * 60 * 1000;
    long startTime = System.currentTimeMillis();

    while (System.currentTimeMillis() - startTime < totalExecutionTime) {
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
