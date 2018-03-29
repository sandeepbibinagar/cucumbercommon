package com.experian.automation.saas.screens;

import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.screens.Screen;
import java.util.List;
import java.util.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomeScreen extends Screen {

  public String url = "/";

  public String windowTitle = "Home Page";

  @FindBy(xpath = "//a[contains(text(),'Home')]")
  public WebElement homeButton;

  @FindBy(xpath = "//ul[@id='menu']/li/a")
  public List<WebElement> mainMenuItems;

  @FindBy(id = "image")
  public WebElement logo;

  @FindBy(xpath = "//ul[@id='menu']/li//ul//li/a")
  public List<WebElement> subMenuItems;

  @FindBy(xpath = "//ul[@style='display: block;' and ancestor::ul[@id='menu']]")
  public List<WebElement> subMenuList;

  @FindBy(xpath = "//ul[@id='menu']/li/div[@class='sub-container mega']")
  public WebElement subContainer;

  @FindBy(xpath = "//a[ancestor::ul[preceding-sibling::a[contains(text(),'General Enquiry')]]]")
  public List<WebElement> generalEnquiry;

  @FindBy(xpath = "//a[ancestor::ul[preceding-sibling::a[contains(text(),'Audit Trail Query')]]]")
  public List<WebElement> auditTrailQuery;

  public HomeScreen(WebHarness webHarness) {
    super(webHarness);
    waitForWindowWithTitle(windowTitle);
  }

  public void selectMenu(String mainMenuItemName, String subMenuItemName) throws InterruptedException {
    if (subMenuList.size() > 0) {
      webHarness.driver.navigate().refresh();
      initElements();
    }
    clickWithScrollToView(getElementWithText(mainMenuItems, mainMenuItemName));
    clickWithScrollToView(getElementWithText(subMenuItems, subMenuItemName));
  }

  public void selectMenu(String mainMenuItemName, String subMenuItemName, String label) throws InterruptedException {
    List<WebElement> clickableSubMenuItem;

    switch (label) {
      case "General Enquiry":
        clickableSubMenuItem = generalEnquiry;
        break;
      case "Audit Trail Enquiry":
        clickableSubMenuItem = auditTrailQuery;
        break;
      default:
        throw new NoSuchElementException("Unable to locate " + label + " label.");
    }

    clickWithScrollToView(getElementWithText(mainMenuItems, mainMenuItemName));
    clickWithScrollToView(getElementWithText(clickableSubMenuItem, subMenuItemName));
  }

}
