package com.experian.automation.saas.steps;

import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.logger.Logger;
import com.experian.automation.steps.CucumberSteps;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import java.io.File;

public class ApplicationSteps {

  private final WebHarness webHarness;

  private final Logger logger = Logger.getLogger(this.getClass());

  public ApplicationSteps(WebHarness webHarness) {
    this.webHarness = webHarness;
  }

  @Before
  public void beforeScenario(Scenario scenario) throws Throwable {
    new CucumberSteps(webHarness).beforeScenario(scenario);
  }

  @After
  public void afterScenario(Scenario scenario) throws Throwable {
    new CucumberSteps(webHarness).afterScenario(scenario);
  }
}