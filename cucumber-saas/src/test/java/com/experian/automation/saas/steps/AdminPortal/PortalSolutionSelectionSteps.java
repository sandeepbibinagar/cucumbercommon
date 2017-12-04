package com.experian.automation.saas.steps.AdminPortal;

import com.experian.automation.harnesses.TestHarness;
import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.logger.Logger;
import com.experian.automation.saas.screens.AdminPortal.PortalHomeScreen;
import com.experian.automation.saas.steps.CommonSteps;
import com.experian.automation.saas.steps.TacticalParameters.DynamicalParametersScreenSteps;
import com.experian.automation.saas.steps.TacticalParameters.TacticalParametersScreenSteps;
import com.experian.automation.saas.steps.WebEngine.UserManagementSteps;
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
        portalScreen.waitForElement(portalScreen.menuExpanded);
        switch (solution) {
            case "PowerCurve Originations":
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
}
