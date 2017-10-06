package com.experian.automation.saas.steps.crb;

import com.experian.automation.harnesses.TestHarness;
import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.logger.Logger;
import com.experian.automation.saas.screens.HomeScreen;
import com.experian.automation.saas.screens.crb.ApplicationSearchScreen;
import com.experian.automation.saas.screens.crb.AuditTrailScreen;
import com.experian.automation.saas.screens.crb.PersonalDetailsScreen;
import com.experian.automation.saas.screens.crb.SimpleSearchScreen;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Map;

/**
 * Created by c08358a on 7/19/2017.
 */
public class SearchApplicationSteps {

    private final TestHarness testHarness;
    private final WebHarness webHarness;
    private final Logger logger = Logger.getLogger(this.getClass());

    public SearchApplicationSteps(TestHarness testHarness, WebHarness webHarness) {
        this.testHarness = testHarness;
        this.webHarness = webHarness;
    }

    @And("^I start a pending application$")
    public void startPendingApplication() throws Throwable {
        HomeScreen homeScreen = new HomeScreen(testHarness, webHarness);
        homeScreen.selectMenu("Apply", "Pending");
        ApplicationSearchScreen applicationSearchScreen = new ApplicationSearchScreen(testHarness, webHarness);
    }

    @And("^I enter search details:$")
    public void enterSearchDetails(Map<String, String> details) throws Throwable {
        SimpleSearchScreen simpleSearchScreen = new SimpleSearchScreen(testHarness, webHarness);
        simpleSearchScreen.set(details);
    }

    @And("^I search for an application$")
    public void searchApplication() {
        ApplicationSearchScreen applicationSearchScreen = new ApplicationSearchScreen(testHarness, webHarness);
        applicationSearchScreen.buttonSearch.click();

    }

    @Then("^I should see Personal Details tab with details:$")
    public void verifyPersonalDetails(Map<String, String> details) throws Throwable {
        PersonalDetailsScreen screen = new PersonalDetailsScreen(testHarness, webHarness);

        for (Map.Entry<String, String> entry : details.entrySet()) {
            String output = screen.get(entry.getKey());
            assertEquals(entry.getValue(),output,"Element "+entry.getKey());
        }
    }

    @And("^I open Basic Application Details with Application Number (.*)$")
    public void selectOpenForApplication(String applicationNumber) throws Throwable {
        ApplicationSearchScreen applicationSearchScreen = new ApplicationSearchScreen(testHarness, webHarness);
        applicationSearchScreen.openBasicAppDetailsByAppNumber(applicationNumber);
    }

    @And("^I open Audit Trail with Application Number (.*)$")
    public void selectAuditTrailForApplication(String applicationNumber) throws Throwable {
        ApplicationSearchScreen applicationSearchScreen = new ApplicationSearchScreen(testHarness, webHarness);
        applicationSearchScreen.openAuditTrailByAppNumber(applicationNumber);

        AuditTrailScreen auditTrailScreen = new AuditTrailScreen(testHarness, webHarness);
        auditTrailScreen.waitForScreen(auditTrailScreen.tableAuditTrail);
        String expectedPageTitle = "Audit Trail for "+ applicationNumber;
        assertEquals(auditTrailScreen.getPageTitleLabel(),expectedPageTitle,"Mismatch: ");
    }

    @Then("^I should see Audit Trail page with details:$")
    public void verifyAuditTrailDetails(List<List<String>> details) throws Throwable {
        AuditTrailScreen auditTrailScreen = new AuditTrailScreen(testHarness, webHarness);

        List <Integer> isOK = auditTrailScreen.findDataInTable(details,
                auditTrailScreen.tableHeadersListTableAuditTrail,
                auditTrailScreen.tableCellsListTableAuditTrail,
                true, false, false );
    }

    @And("^I start a general enquiry$")
    public void startGeneralEnquiry() throws Throwable {
        HomeScreen homeScreen = new HomeScreen(testHarness, webHarness);
        homeScreen.selectMenu("Query", "All", "General Enquiry");
        ApplicationSearchScreen applicationSearchScreen = new ApplicationSearchScreen(testHarness, webHarness);
    }

    @And ("^I open Application Overview with Application Number (.*)$")
    public void selectApplicationOverviewForApplication(String applicationNumber) throws Throwable {
        ApplicationSearchScreen applicationSearchScreen = new ApplicationSearchScreen(testHarness, webHarness);
        applicationSearchScreen.openApplicationOverviewDetailsByAppNumber(applicationNumber);
    }
}