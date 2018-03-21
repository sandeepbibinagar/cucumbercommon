package com.experian.automation.saas.steps.BiReport;

import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.logger.Logger;
import com.experian.automation.saas.screens.BiReporting.BiHomeScreen;
import cucumber.api.java.en.And;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class TestEnvSelectionStep {

  private final Logger logger = Logger.getLogger(this.getClass());
  private final WebHarness webHarness;

  public TestEnvSelectionStep(WebHarness webHarness){
    this.webHarness = webHarness;
  }

  @And("^I select test environment \"([^\"]*)\"$")
  public void selectTestEnvironment(String testEnv) {
    BiHomeScreen screen = new BiHomeScreen(webHarness);
    screen.waitForElementToDisappear(screen.loaders);
    screen.waitForElement(screen.testEnvList);
    WebElement project = webHarness.driver.findElement(By.linkText("" + testEnv + ""));
    screen.jsClick(project);
  }
}
