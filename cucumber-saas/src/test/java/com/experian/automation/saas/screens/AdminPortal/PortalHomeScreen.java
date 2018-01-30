package com.experian.automation.saas.screens.AdminPortal;

import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.screens.Screen;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class PortalHomeScreen extends Screen {

  @FindBy(xpath = "//nav//span[contains(text(),'SaaS Portal')]")
  public WebElement portalHeader;

  @FindBy(xpath = "//i[@class='exp-icon-apps']")
  public WebElement solutionsListButton;

  @FindBy(xpath = "//div[@class='destination']")
  public List<WebElement> solutions;

  @FindBy(xpath = "//a[@aria-expanded='true' and child::i[@class='exp-icon-apps']]")
  public WebElement menuExpanded;

  @FindBy(xpath = "//a[child::p[contains(text(),'Options')]]")
  public WebElement options;

  @FindBy(id = "app-switcher-originations")
  public WebElement originationsSolution;

  @FindBy(id = "admin-portal")
  public WebElement adminPortal;

  public PortalHomeScreen(WebHarness webHarness) {
    super(webHarness);
    waitForElement(solutionsListButton);
  }

}
