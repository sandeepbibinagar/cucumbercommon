package com.experian.automation.saas.screens.BiReporting;

import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.screens.Screen;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SharedReportingScreen extends Screen{
  @FindBy(xpath = "//a[@title='Applications Received']")
  public WebElement appReceivedReport;

  @FindBy(xpath = "//a[@title='Automated Decision Trend']")
  public WebElement autoDecisionTrendReport;

  @FindBy(xpath = "//a[@title='Manual Decision Trend']")
  public WebElement manualDecisionTrendReport;

  public SharedReportingScreen(WebHarness webHarness) {
    super(webHarness);
    waitForElement(appReceivedReport);
  }
}
