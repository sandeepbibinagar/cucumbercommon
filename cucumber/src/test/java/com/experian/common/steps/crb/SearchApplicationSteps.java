package com.experian.common.steps.crb;

import com.experian.common.WebClient;
import com.experian.common.logger.Logger;
import com.experian.common.screens.HomeScreen;
import com.experian.common.screens.crb.*;
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

    private final WebClient webClient;
    private final Logger logger = Logger.getLogger(this.getClass());

    public SearchApplicationSteps(WebClient webClient) {
        this.webClient = webClient;
    }

    @And("^I start a pending application$")
    public void startPendingApplication() throws Throwable {
        HomeScreen homeScreen = new HomeScreen(webClient);
        homeScreen.selectMenu("Apply", "Pending");
        ApplicationSearchScreen applicationSearchScreen = new ApplicationSearchScreen(webClient);
    }

    @And("^I enter search details:$")
    public void enterSearchDetails(Map<String, String> details) throws Throwable {
        SimpleSearchScreen simpleSearchScreen = new SimpleSearchScreen(webClient);
        simpleSearchScreen.set(details);
    }

    @And("^I search for an application$")
    public void searchApplication() {
        ApplicationSearchScreen applicationSearchScreen = new ApplicationSearchScreen(webClient);
        applicationSearchScreen.buttonSearch.click();

    }

    @Then("^I should see Personal Details tab with details:$")
    public void verifyPersonalDetails(Map<String, String> details) throws Throwable {
        PersonalDetailsScreen screen = new PersonalDetailsScreen(webClient);

        for (Map.Entry<String, String> entry : details.entrySet()) {
            String output = screen.get(entry.getKey());
            assertEquals(entry.getValue(),output,"Element "+entry.getKey());
        }
    }

    @And("^I open Basic Application Details with Application Number (.*)$")
    public void selectOpenForApplication(String applicationNumber) throws Throwable {
        ApplicationSearchScreen applicationSearchScreen = new ApplicationSearchScreen(webClient);
        applicationSearchScreen.openBasicAppDetailsByAppNumber(applicationNumber);
    }

    @And("^I open Audit Trail with Application Number (.*)$")
    public void selectAuditTrailForApplication(String applicationNumber) throws Throwable {
        ApplicationSearchScreen applicationSearchScreen = new ApplicationSearchScreen(webClient);
        applicationSearchScreen.openAuditTrailByAppNumber(applicationNumber);

        AuditTrailScreen auditTrailScreen = new AuditTrailScreen(webClient);
        auditTrailScreen.waitForScreen(auditTrailScreen.tableAuditTrail);
        String expectedPageTitle = "Audit Trail for "+ applicationNumber;
        assertEquals(auditTrailScreen.getPageTitleLabel(),expectedPageTitle,"Mismatch: ");
    }

    @Then("^I should see Audit Trail page with details:$")
    public void verifyAuditTrailDetails(List<List<String>> details) throws Throwable {
        AuditTrailScreen auditTrailScreen = new AuditTrailScreen(webClient);

        List <Integer> isOK = auditTrailScreen.findDataInTable(details,
                auditTrailScreen.tableHeadersListTableAuditTrail,
                auditTrailScreen.tableCellsListTableAuditTrail,
                true, false, false );
    }

    @And("^I start a general enquiry$")
    public void startGeneralEnquiry() throws Throwable {
        HomeScreen homeScreen = new HomeScreen(webClient);
        homeScreen.selectMenu("Query", "All", "General Enquiry");
        ApplicationSearchScreen applicationSearchScreen = new ApplicationSearchScreen(webClient);
    }

    @And ("^I open Application Overview with Application Number (.*)$")
    public void selectApplicationOverviewForApplication(String applicationNumber) throws Throwable {
        ApplicationSearchScreen applicationSearchScreen = new ApplicationSearchScreen(webClient);
        applicationSearchScreen.openApplicationOverviewDetailsByAppNumber(applicationNumber);
    }
}