package com.experian.automation.saas.steps.AdminPortal;

import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.logger.Logger;
import com.experian.automation.saas.screens.AdminPortal.PortalHomeScreen;
import cucumber.api.java.en.And;
import org.openqa.selenium.NoSuchElementException;

public class PortalSolutionSelectionSteps {

  private final Logger logger = Logger.getLogger(this.getClass());
  private final WebHarness webHarness;

  public PortalSolutionSelectionSteps(WebHarness webHarness) {
    this.webHarness = webHarness;
  }

  @And("^I select solution - (PowerCurve Originations|PoweCurve Admin Portal|Options|BI)$")
  public void selectSolution(String solution) {
    PortalHomeScreen portalScreen = new PortalHomeScreen(webHarness);

    portalScreen.waitForElement(portalScreen.solutionsListButton);
    portalScreen.jsClick(portalScreen.solutionsListButton);
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
      case "BI":
        portalScreen.waitForElement(portalScreen.biSolution);
        portalScreen.jsClick(portalScreen.biSolution);
        break;
      default:
        throw new NoSuchElementException("No such element" + solution);
    }
  }
}
