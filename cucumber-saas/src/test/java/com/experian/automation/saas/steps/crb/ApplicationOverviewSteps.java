package com.experian.automation.saas.steps.crb;

import com.experian.automation.harnesses.TestHarness;
import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.logger.Logger;
import com.experian.automation.saas.screens.crb.ApplicationOverviewScreen;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;

/**
 * Created by c08358a on 8/4/2017.
 */
public class ApplicationOverviewSteps {

    private final TestHarness testHarness;
    private final WebHarness webHarness;
    private final Logger logger = Logger.getLogger(this.getClass());

    public ApplicationOverviewSteps(TestHarness testHarness, WebHarness webHarness) {
        this.testHarness = testHarness;
        this.webHarness = webHarness;
    }

    @Then("^I should see Applicant Summary section with details:$")
    public void verifyApplicantSummaryDetails(List<List<String>> details) throws Throwable {
        ApplicationOverviewScreen applicationOverviewScreen = new ApplicationOverviewScreen(testHarness, webHarness);
        applicationOverviewScreen.waitForElement(applicationOverviewScreen.applicantSummaryLabel);

        assertEquals(applicationOverviewScreen.applicantSummaryLabel.getText(),"Applicant Summary",
                "Mismatch: ");

        List <Integer> isOK = applicationOverviewScreen.findDataInTable(details,
                applicationOverviewScreen.tableHeadersListTableApplicantSummary,
                applicationOverviewScreen.tableCellsListTableApplicantSummary,
                true, false, false );
    }

    @And("^I should see Product Summary section with details:$")
    public void verifyProductSummaryDetails(Map<String, String> details) {
        ApplicationOverviewScreen applicationOverviewScreen = new ApplicationOverviewScreen(testHarness, webHarness);
        applicationOverviewScreen.waitForElement(applicationOverviewScreen.productSummaryLabel);

        assertEquals(applicationOverviewScreen.productSummaryLabel.getText(),"Product Summary",
                "Mismatch: ");

        for (Map.Entry<String, String> entry : details.entrySet()) {
            String output = applicationOverviewScreen.get(entry.getKey());
            assertEquals(entry.getValue(),output,"Element "+entry.getKey());
        }
    }

    @And ("^I should see Application Decisions section with details:$")
    public void verifyApplicationDecisionsDetails(Map<String, String> details) {
        ApplicationOverviewScreen applicationOverviewScreen = new ApplicationOverviewScreen(testHarness, webHarness);
        applicationOverviewScreen.waitForElement(applicationOverviewScreen.applicationDecisionsLabel);

        assertEquals(applicationOverviewScreen.applicationDecisionsLabel.getText(),"Application Decisions",
                "Mismatch: ");

        for (Map.Entry<String, String> entry : details.entrySet()) {
            String output = applicationOverviewScreen.get(entry.getKey());
            assertEquals(entry.getValue(),output,"Element "+entry.getKey());
        }
    }
}