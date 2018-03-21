package com.experian.automation.saas.steps.BiReport;

import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.logger.Logger;
import com.experian.automation.saas.screens.BiReporting.SharedReportingScreen;
import cucumber.api.java.en.And;
import org.openqa.selenium.NoSuchElementException;

public class SharedReportsSelectionStep {

  private final Logger logger = Logger.getLogger(this.getClass());
  private final WebHarness webHarness;

  public SharedReportsSelectionStep(WebHarness webHarness) {
    this.webHarness = webHarness;
  }

  @And("^I select report \"(Applications Received|Automated Decision Trend)\"$")
  public void selectSolution(String report) throws NoSuchElementException {
    SharedReportingScreen reportsScreen = new SharedReportingScreen(webHarness);
    switch (report) {
      case "Applications Received":
        reportsScreen.jsClick(reportsScreen.appReceivedReport);
        break;
      case "Automated Decision Trend":
        reportsScreen.jsClick(reportsScreen.autoDecisionTrendReport);
        break;
      default:
        throw new NoSuchElementException("No such element" + report);
    }
  }
}
