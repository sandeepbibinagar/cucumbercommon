package com.experian.automation.saas.steps.AdminPortal;

import com.experian.automation.harnesses.TestHarness;
import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.logger.Logger;
import com.experian.automation.saas.screens.AdminPortal.PortalHomeScreen;
import com.experian.automation.saas.steps.CommonSteps;
import com.experian.automation.saas.steps.TacticalParameters.DynamicalParametersScreenSteps;
import com.experian.automation.saas.steps.TacticalParameters.TacticalParametersScreenSteps;
import com.experian.automation.saas.steps.crb.UserManagementSteps;
import cucumber.api.java.en.And;
import org.openqa.selenium.NoSuchElementException;

import java.util.HashMap;
import java.util.Map;

public class PortalSolutionSelectionSteps {

    private final Logger logger = Logger.getLogger(this.getClass());
    private final TestHarness testHarness;
    private final WebHarness webHarness;

    public PortalSolutionSelectionSteps(TestHarness testHarness, WebHarness webHarness) {
        this.testHarness = testHarness;
        this.webHarness = webHarness;
    }

    @And("^I select solution - (PowerCurve Originations|PoweCurve Admin Portal|Options)$")
    public void selectSolution(String solution) {
        PortalHomeScreen portalScreen = new PortalHomeScreen(testHarness, webHarness);
        portalScreen.clickWithScrollToView(portalScreen.solutionsListButton);
        switch (solution) {
            case "PowerCurve Originations":
                portalScreen.clickWithScrollToView(portalScreen.solutionsListButton);
                portalScreen.clickWithScrollToView(portalScreen.originationsSolution);
                break;
            case "PowerCurve Admin Portal":
                portalScreen.adminPortal.click();
                break;
            case "Options":
                portalScreen.clickWithScrollToView(portalScreen.options);
                break;
            default:
                throw new NoSuchElementException("No such element" + solution);
        }
    }

    @And("^I check user permissions and parameters for solution - (PowerCurve Originations|Admin Portal)$")
    public void initialSolutionSetup(String solution) throws Throwable {
        switch (solution) {
            case "PowerCurve Originations":

                Map<String, String> tacticalParams = new HashMap<>();
                tacticalParams.put("ProveID Parameters - Get ProveID Parameters", "ALL-CRBPT-TacticalParam.xml");
                tacticalParams.put("Experian Bureau Parameters - Get Experian Bureau Parameters", "ALL-CRBPT-TacticalParam.xml");
                tacticalParams.put("Experian Product Parameters - Get Experian Product Parameters", "ALL-CRBPT-TacticalParam.xml");
                tacticalParams.put("Client Parameters - Get Client Parameters", "ALL-CRBPT-TacticalParam.xml");
                tacticalParams.put("Experian Interfaces Parameters - Get Experian Interfaces Parameters", "ALL-CRBPT-TacticalParam.xml");
                tacticalParams.put("Quotation Parameters - Get Quotation Parameters", "ALL-CRBPT-TacticalParam.xml");
                tacticalParams.put("Loan Parameters - Get Loan Parameters", "ALL-CRBPT-TacticalParam.xml");
                tacticalParams.put("Current Account Parameters - Get Current Account Parameters", "ALL-CRBPT-TacticalParam.xml");
                tacticalParams.put("Saving Account Parameters - Get Saving Account Parameters", "ALL-CRBPT-TacticalParam.xml");
                tacticalParams.put("Mortgage Parameters - Get Mortage Parameters", "ALL-CRBPT-TacticalParam.xml");
                tacticalParams.put("Credit Card Parameters - Get Credit Card Parameters", "ALL-CRBPT-TacticalParam.xml");
                tacticalParams.put("Experian Solution Parameters - Get Experian Solution Parameters", "ALL-CRBPT-TacticalParam.xml");

                Map<String, String> dynamicalParams = new HashMap<>();
                dynamicalParams.put("Purpose of Purchase", "Purpose of Purchase.csv");
                dynamicalParams.put("Education", "Education.csv");
                dynamicalParams.put("Occupation", "Occupation.csv");
                dynamicalParams.put("Document Type", "Document Type.csv");
                dynamicalParams.put("Override Reason", "Override Reason.csv");
                dynamicalParams.put("Contact method", "Contact method.csv");
                dynamicalParams.put("Gender", "Gender.csv");
                dynamicalParams.put("Country", "Country.csv");
                dynamicalParams.put("Marital Status", "Marital Status.csv");
                dynamicalParams.put("Purpose of Loan", "Purpose of Loan.csv");
                dynamicalParams.put("Residential Status", "Residential Status.csv");
                dynamicalParams.put("Title", "Title.csv");
                dynamicalParams.put("Employment Status", "Employment Status.csv ");
                dynamicalParams.put("Current Lender", "Current Lender.csv");
                dynamicalParams.put("Relationship to applicant", "Relationship to applicant.csv ");
                dynamicalParams.put("Master Product-Second Product-Third Product", "ALL-Master Product-Second Product-Third Product.csv");

                new UserManagementSteps(testHarness, webHarness).setAllPermissions();
                new CommonSteps(testHarness, webHarness).solutionLogout();
                new PortalSolutionSelectionSteps(testHarness, webHarness).selectSolution(solution);
                new TacticalParametersScreenSteps(testHarness, webHarness).importTacticalParameters(tacticalParams);
                new DynamicalParametersScreenSteps(testHarness, webHarness).importDynamicParameters(dynamicalParams);

                break;

            default:
                throw new NoSuchElementException("No such element" + solution);
        }
    }
}
