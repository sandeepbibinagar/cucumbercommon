package com.experian.common.steps.AdminPortal;

import com.experian.common.WebClient;
import com.experian.common.core.logger.Logger;
import com.experian.common.screens.AdminPortal.PortalHomeScreen;
import com.experian.common.screens.HomeScreen;
import cucumber.api.java.en.And;
import org.openqa.selenium.NoSuchElementException;

public class PortalSolutionSelectionSteps {

    private final Logger logger = Logger.getLogger(this.getClass());
    private final WebClient webClient;

    public PortalSolutionSelectionSteps(WebClient webClient) {
        this.webClient = webClient;
    }

    @And("^I select solution - (PowerCurve Originations|PoweCurve Admin Portal)$")
    public void selectSolution(String solution) {
        PortalHomeScreen portalScreen = new PortalHomeScreen(webClient);
        switch (solution) {
            case "PowerCurve Originations":
                portalScreen.originationsSolution.click();
                HomeScreen hm = new HomeScreen(webClient);
                hm.waitForElement(hm.homeButton);
                break;
            case "PowerCurve Admin Portal":
                portalScreen.adminPortal.click();
                break;
            default:
                throw new NoSuchElementException("No such element" + solution);
        }
    }
}
