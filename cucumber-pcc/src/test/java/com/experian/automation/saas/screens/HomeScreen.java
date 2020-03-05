package com.experian.automation.saas.screens;

import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.screens.Screen;
import java.util.List;
import java.util.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomeScreen extends Screen {

  public String url = "/";

  public String windowTitle = "PowerCurve Collections";

  @FindBy(xpath = "//a[contains(text(),'Home')]")
  public WebElement homeButton;

  @FindBy(xpath = "//ul[@id='menu']/li/a")
  public List<WebElement> mainMenuItems;

  @FindBy(xpath = "//form[@id='loadAccountForm']/div[1]/select[@id='selAccountNo']")
  public WebElement accountNumberDropDown;

  @FindBy(xpath = "//div[@id='experian-navbar']/ul[2]/li[2]")
  public WebElement mainDropDownMenu;

  @FindBy(xpath = "//div[@class='navbar-header']")
  public WebElement navBarHeader;

  //@FindBy(xpath = "//ul[contains(text(),'nav navbar-nav')]/li[2]")
  @FindBy(xpath = "//div[@class='navbar navbar-blue']/ul/li[3]/a")
  public WebElement searchIcon;

  @FindBy(xpath = "//div[@class='main-content']/h2")
  public WebElement searchHeader;


  @FindBy(xpath = "//li[@class='dropdown open']/ul//li[1]")
  public WebElement highContrastModeMenu;

  @FindBy(xpath = "//li[@class='dropdown open']/ul//li[1]")
  public WebElement defaultModeMenu;

  @FindBy(xpath = "//li[@class='dropdown open']/ul//li[3]")
  public WebElement logOffMenu;

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

  @FindBy(id = "ifrUserTools")
  public WebElement userToolsFrame;



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

  public void getCSSValue(WebElement element, String attribute){
   System.out.println(element.getCssValue(attribute).toString());
  }



  public void selectDropDownMenu(WebElement mainMenuItemName, WebElement subMenuItemName) throws InterruptedException {
      if (subMenuList.size() > 0) {
        webHarness.driver.navigate().refresh();
        initElements();
      }
      clickWithScrollToView(mainMenuItemName);
      clickWithScrollToView(subMenuItemName);
    }

  public void validateHighContrastSolution() throws InterruptedException{
  selectDropDownMenu(mainDropDownMenu,highContrastModeMenu);
  waitForScreen(accountNumberDropDown);
  getCSSValue(navBarHeader, "color");
  selectDropDownMenu(mainDropDownMenu, defaultModeMenu);
  waitForScreen(accountNumberDropDown);
  getCSSValue(navBarHeader, "color");
  }


  public void clickSearchIcon() throws InterruptedException{
   clickElement(searchIcon);
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
