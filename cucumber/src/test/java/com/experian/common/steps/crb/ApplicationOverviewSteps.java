package com.experian.common.steps.crb;

import com.experian.common.WebClient;
import com.experian.common.logger.Logger;
import com.experian.common.screens.crb.ApplicationOverviewScreen;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;

/**
 * Created by c08358a on 8/4/2017.
 */
public class ApplicationOverviewSteps {

    private final WebClient webClient;
    private final Logger logger = Logger.getLogger(this.getClass());

    public ApplicationOverviewSteps(WebClient webClient) {
        this.webClient = webClient;
    }

    @Then("^I should see Applicant Summary section with details:$")
    public void verifyApplicantSummaryDetails(List<List<String>> details) throws Throwable {
        ApplicationOverviewScreen applicationOverviewScreen = new ApplicationOverviewScreen(webClient);
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
        ApplicationOverviewScreen applicationOverviewScreen = new ApplicationOverviewScreen(webClient);
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
        ApplicationOverviewScreen applicationOverviewScreen = new ApplicationOverviewScreen(webClient);
        applicationOverviewScreen.waitForElement(applicationOverviewScreen.applicationDecisionsLabel);

        assertEquals(applicationOverviewScreen.applicationDecisionsLabel.getText(),"Application Decisions",
                "Mismatch: ");

        for (Map.Entry<String, String> entry : details.entrySet()) {
            String output = applicationOverviewScreen.get(entry.getKey());
            assertEquals(entry.getValue(),output,"Element "+entry.getKey());
        }
    }
}
