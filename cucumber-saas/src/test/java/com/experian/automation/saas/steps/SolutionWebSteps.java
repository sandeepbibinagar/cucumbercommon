package com.experian.automation.saas.steps;


import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.helpers.Config;
import com.experian.automation.saas.screens.SolutionScreen;
import cucumber.api.java.en.And;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import static org.testng.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SolutionWebSteps {

  private final WebHarness webHarness;

  public SolutionWebSteps(WebHarness webHarness) throws IOException, ConfigurationException {
    this.webHarness = webHarness;
  }

  String pageObjectFileName = "page-object-" + FilenameUtils.removeExtension(
      new File(Config.get("deployable.file")).getName()) + ".json";
  File pageObjecFile = new File(Config.get("temp.dir") + pageObjectFileName);
  String pageObjectModel = FileUtils.readFileToString(pageObjecFile, "UTF-8");

  /*
  * Usage example(s):
  *
  * And I select menu Apply/New Application on page homeStartPage
  *
  */
  @And("^I select menu (.*) on page (.*)$")
  public void selectMenu(String menu, String page) throws IOException, ConfigurationException {
    SolutionScreen screen = new SolutionScreen(webHarness, pageObjectModel);
    screen.selectMenu(menu, page);
  }


  /*
  * Usage example(s):
  *
  * And I select item Pending in section Pending Applications in menu Apply on page homeStartPage
  *
  */
  @And("^I select item (.*) in section (.*) in menu (.*) on page (.*)$")
  public void selectMenuWithSection(String subMenu, String sectionItemText, String menu, String pageTitle)
      throws IOException, ConfigurationException {
    SolutionScreen screen = new SolutionScreen(webHarness, pageObjectModel);
    screen.selectMenu(menu, pageTitle);
    screen.selectSubMenuSectionItem(sectionItemText, subMenu, pageTitle);
  }

  /*
  * Usage example(s):
  *
  * And I enter values for fields on page Query All Search Page
  *  | Surname            | McIver         |
  *  | Forename           | Rita           |
  *  | Application Number | BK000000002210 |
  *
  */
  @And("I enter values for fields on page (.*)")
  public void enterValuesForFields(String page, Map<String, String> fields) throws IOException, ConfigurationException {
    SolutionScreen screen = new SolutionScreen(webHarness, pageObjectModel);
    screen.waitForElementPresence(By.xpath("//*[contains(text(),'" + page + "')]"));
    for (Map.Entry<String, String> entry : fields.entrySet()) {
      String label = entry.getKey();
      String value = entry.getValue();
      String elementType = screen.getPageObjectTypeByLabel(label, page);
      screen.waitForElementPresence(By.xpath("//*[contains(text(),'" + label + "')]"));
      switch (elementType) {
        case ("textfield"):
          screen.setTextfieldValueByLabel(label, value, page);
          break;
        case ("dropdown"):
          screen.setDropdownValueByLabel(label, value, page);
          break;
        case ("datepicker"):
          screen.setDatepickerValueByLabel(label, value, page);
          break;
        default:
          throw new IllegalArgumentException("Illegal element type argument: " + elementType);
      }
    }
  }

  /*
  * Usage example(s):
  *
  * And I enter value A455 for textfield with id DocumentIdNumberTxt
  *
  */
  @And("^I enter value (.*) for textfield with (id|class|name|value) (.*)$")
  public void enterTextFieldValBySelector(String fieldValue, String selector, String selectorValue)
      throws IOException, ConfigurationException {
    SolutionScreen screen = new SolutionScreen(webHarness, pageObjectModel);
    switch (selector) {
      case ("id"):
        screen.typeWithClear(screen.getElementById(selectorValue), fieldValue);
        break;
      case ("class"):
        screen.typeWithClear(screen.getElementByClass(selectorValue), fieldValue);
        break;
      case ("name"):
        screen.typeWithClear(screen.getElementByName(selectorValue), fieldValue);
        break;
      case ("value"):
        screen.typeWithClear(screen.getElementByValue(selectorValue), fieldValue);
        break;
      default:
        throw new IllegalArgumentException("Illegal value " + selector + " for selector.");
    }
  }

  /*
  * Usage example(s):
  *
  * And I enter value Mrs for dropdown with name Title
  *
  */
  @And("^I enter value (.*) for dropdown with (id|class|name) (.*)$")
  public void enterDropdownValBySelector(String fieldValue, String selector, String selectorValue)
      throws IOException, ConfigurationException {
    SolutionScreen screen = new SolutionScreen(webHarness, pageObjectModel);
    switch (selector) {
      case ("id"):
        new Select(screen.getElementById(selectorValue)).selectByVisibleText(fieldValue);
        break;
      case ("class"):
        new Select(screen.getElementByClass(selectorValue)).selectByVisibleText(fieldValue);
        break;
      case ("name"):
        new Select(screen.getElementByName(selectorValue)).selectByVisibleText(fieldValue);
        break;
      default:
        throw new IllegalArgumentException("Illegal value " + selector + " for selector.");
    }
  }

  /*
  * Usage example(s):
  *
  * And I fill multiple inputs with label Time at Address on page Applicant and Address Details Page:
  *  | 10 |
  *  | 5  |
 */
  @And("^I fill multiple inputs with label (.*) on page (.*):$")
  public void fillInputs(String label, String page, List<String> data) throws IOException, ConfigurationException {
    SolutionScreen screen = new SolutionScreen(webHarness, pageObjectModel);
    screen.fillMultipleInputs(label, data, page);
  }

  /*
   * Usage example(s):
   *
   * And I select value Credit Card from dropdown field Payment Type on page productsPaymentPage
   *
   */
  @And("^I select value (.*) from dropdown field (.*) on page (.*)$")
  public void selectValueForDropdown(String value, String dropdownLabelTitle, String pageTitle)
      throws IOException, ConfigurationException {
    SolutionScreen screen = new SolutionScreen(webHarness, pageObjectModel);
    screen.setDropdownValueByLabel(dropdownLabelTitle, value, pageTitle);
  }

  /*
   * Usage example(s):
   *
   * And I select value 27/10/2002 for datepicker Date Of Birth on page Basic Application Details page
   *
   */
  @And("^I enter value (.*) for datepicker (.*) on page (.*)$")
  public void enterDatepickerValue(String value, String fieldLabel, String pageTitle)
      throws IOException, ConfigurationException {
    SolutionScreen screen = new SolutionScreen(webHarness, pageObjectModel);
    screen.setDatepickerValueByLabel(fieldLabel, value, pageTitle);
  }

  /*
 * Usage example(s):
 *
 * I select tab with text Products on page Decision Analytics Page
 *
 */
  @And("^I select tab with text (.*) on page (.*)$")
  public void selectTab(String tabText, String pageTitle) throws IOException, ConfigurationException {
    SolutionScreen screen = new SolutionScreen(webHarness, pageObjectModel);
    screen.selectTabWithTitle(tabText, pageTitle);
  }

  /*
  * Usage example(s):
  *
  * I click on link with text Calculate
  *
  */
  @And("^I click on link with text (.*)$")
  public void clickOnElementWithText(String text) throws IOException, ConfigurationException {
    SolutionScreen screen = new SolutionScreen(webHarness, pageObjectModel);
    screen.clickOnLinkWithText(text);
  }


  /*
 * Usage example(s):
 *
 * I click on button with text Cancel on page Decision Page
 *
 */
  @And("^I click on button with (text|id|value) (.*) on page (.*)$")
  public void buttonClick(String identifier, String value, String pageTitle)
      throws IOException, ConfigurationException {
    SolutionScreen screen = new SolutionScreen(webHarness, pageObjectModel);
    if (identifier.equals("text")) {
      screen.clickButton(value, pageTitle);
    } else if (identifier.equals("value")) {
      screen.clickButtonBy("value", value);
    } else {
      screen.clickButtonBy("id", value);
    }
  }

  /*
  * Usage example(s):
  *
  *   I click on table cell link with text Audit Trail in column Action on row identified by cell text BK000000002210 in column Application Number on table with id datagrid
  *
  */
  @And("^I click on table cell link with text (.*) in column (.*) on row identified by cell text (.*) in column (.*) on table with (id|class|name) (.*)$")
  public void selectLinkInTable(String elementText, String columnText, String searchColumnRowValue, String searchColumn,
      String tableLocator, String locatorValue) throws IOException, ConfigurationException {
    SolutionScreen screen = new SolutionScreen(webHarness, pageObjectModel);
    screen.clickOnElementWithTitleInTable(elementText, columnText, searchColumn, searchColumnRowValue, tableLocator,
                                          locatorValue);
  }

  /*
  * Usage example(s):
  *
  *   I click on table cell with text McIver in column Surname on table with id datagrid
  *
  */
  @And("^I click on table cell with text (.*) in column (.*) on table with (id|class|name) (.*)$")
  public void clickOnCellInColumn(String elementText, String columnText, String locator, String locatorValue)
      throws IOException, ConfigurationException {
    SolutionScreen screen = new SolutionScreen(webHarness, pageObjectModel);
    screen.clickOnCellWithText(elementText, columnText, locator, locatorValue);
  }

  /*
 * Usage example(s):
 *
 *  And I verify values for fields on page Basic Application Details Page:
 *   | Surname             | McIver             |
 *   | Forename            | Rita               |
 *   | Home Phone Number   | (+45) 787 567 8999 |
 *   | Email Address       | Rita@gmail.com     |
 *   | Total Annual Income | 72,500.00          |
 *   | Title               | Mrs                |
 */
  @And("^I verify values for fields on page (.*):$")
  public void verifyFieldsValues(String page, Map<String, String> data) throws IOException, ConfigurationException {
    SolutionScreen screen = new SolutionScreen(webHarness, pageObjectModel);
    for (Map.Entry<String, String> entry : data.entrySet()) {
      String elementType = screen.getPageObjectTypeByLabel(entry.getKey(), page);
      assertTrue(screen.verifyFieldValue(page, elementType, entry.getKey(), entry.getValue()),
                 "Element " + entry.getKey() + " contains value: " + entry.getValue());
    }
  }


  /*
   * And I verify data in table with class audit-trail-table:
   *   | Date & Time        | Duration | User ID | Channel | Service ID | BPF Name        | Status  | Worklist |
   *   | 10/10/2017 1:42 PM | 00:01:29 | admin   | Web     | 1          | New Application | Pending | Pending  |
   *   | 10/10/2017 1:42 PM | 00:01:16 | admin   | Web     | 1          | New Application | Pending | Pending  |
   */
  @And("^I verify data in table with (id|class) (.*):$")
  public void verifyDataInTableWithId(String selector, String selectorValue, List<List<String>> data)
      throws IOException, ConfigurationException {
    SolutionScreen screen = new SolutionScreen(webHarness, pageObjectModel);
    screen.verifyDataInTable(selector, selectorValue, data);
  }

  /*
  * Usage example(s):
  *
  *  And I verify that field Capture Data is located in the TOP-LEFT part of the Home Page screen
  *
  *  And I verify that field Application Number is located in the TOP-MIDDLE part of the P - New Application Screen screen
  *
  *  And I verify that field Last Name is located in the CENTER part of the P - New Application Screen screen
  *
  *  And I verify that field Payment Protection is located in the BOTTOM-MIDDLE part of the P - New Application Screen screen
  */
  @And("^I verify that element (.*) is located in the (CENTER|(TOP|CENTER|BOTTOM)-(LEFT|MIDDLE|RIGHT)) part of the (.*) screen$")
  public void verifyFieldPosition(String field, String isCenter, String firstPosition, String secondPosition,
      String page) throws Throwable {
    SolutionScreen screen = new SolutionScreen(webHarness, pageObjectModel);
    String position = StringUtils.isNotEmpty(isCenter) ? isCenter : firstPosition + "-" + secondPosition;
    assertTrue(screen.checkElementPositionInViewPort(position, field, page),
               "Field is located on " + position + " position of the screen.");

  }
  
  /*
 * Usage example(s):
 *
 *  I compare web image with label to local image:
 *   | Credit Card    | Accept.png             |
 *   | Loan           | Bundled.png            |
 */
  @And("^I compare web image with label to local image:$")
  public void verifyImageWithLabel(Map<String, String> data)
      throws IOException, ConfigurationException, InterruptedException {
    SolutionScreen screen = new SolutionScreen(webHarness, pageObjectModel);
    for (Map.Entry<String, String> entry : data.entrySet()) {
      screen.verifyImageWithLabel(entry.getKey(), entry.getValue());
    }
  }


    /*
    *
    * And I switch to page with title Bureau Data
    *
    */

  @And("^I switch to page with title: (.*)$")
  public void switchToPageWithTitle(String title) throws IOException, ConfigurationException {
    SolutionScreen screen = new SolutionScreen(webHarness, pageObjectModel);
    screen.waitForWindowWithTitle(title);
  }

}

