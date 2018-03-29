package com.experian.automation.saas.screens.AdminPortal;

import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.screens.Screen;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class PortalHomeScreen extends Screen {

  @FindBy(xpath = "//nav//span[contains(text(),'SaaS Portal')]")
  public WebElement portalHeader;

  @FindBy(xpath = "//saas-app-manager/div/a")
  public WebElement solutionsListButton;

  @FindBy(xpath = "//div[@class='dropdown-item style-scope saas-app-manager']")
  public List<WebElement> solutions;

//  @FindBy(xpath = "//a[@aria-expanded='true' and child::i[@class='exp-icon-apps style-scope saas-app-manager']]")
  @FindBy(xpath = "//div[@class='dropdown d-inline-block show style-scope saas-app-manager']")
  public WebElement menuExpanded;

  @FindBy(xpath = "//a[child::p[contains(text(),'Options')]]")
  public WebElement options;

  @FindBy(xpath = "//a[child::p[contains(text(),'Acquire Customers Faster')]]")
  public WebElement originationsSolution;

  @FindBy(xpath = "//a[child::p[contains(text(),'BI Reporting')]]")
  public WebElement biSolution;

  @FindBy(xpath = "//a[child::p[contains(text(),'Administration')]]")
  public WebElement adminPortal;

  public PortalHomeScreen(WebHarness webHarness) {
    super(webHarness);
    waitForScreen(solutionsListButton);
  }

}
