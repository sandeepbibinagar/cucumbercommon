package com.experian.automation.saas.screens.BiReporting;

import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.helpers.Variables;
import com.experian.automation.saas.steps.BiReport.ReportFilterSelectionStep;
import com.experian.automation.screens.Screen;
import com.experian.automation.transformers.VariablesTransformer;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

public class CommonReportScreen extends Screen {

  @FindBy(xpath = "//div[@class='mstrmojo-DocLayout ']")
  public WebElement docLayoutViewer;

  @FindBy(xpath = "//div[@id='mstr92']")
  public WebElement dropdown;

  @FindBy(xpath = "//div[@id='mstr93']")
  public WebElement dropdownPopup;

  @FindBy(xpath = "//div[@id='mstr93']/div/div//div")
  public List<WebElement> dropdownValues;

  @FindBy(xpath = "//div[@class='mstrmojo-portlet-slot-content']/div[@class='mstrmojo-Xtab ']//div[@class='mstrmojo-Xtab-content ']/table/tbody/tr/td/div/div[@id]//tr[1]/td[1]")
  public List<WebElement> firstRowHeaderElements;

  @FindBy(xpath = "//div[@class='mstrmojo-portlet-slot-content']/div[@class='mstrmojo-Xtab ']//div[@class='mstrmojo-Xtab-content ']/table/tbody/tr/td/div/div[@id]//tr[2]/td[position()>1]")
  public List<WebElement> secondRowHeaderElements;

  @FindBy(xpath = "//div[@class='mstrmojo-portlet-slot-content']/div[@class='mstrmojo-Xtab ']//div[@class='mstrmojo-Xtab-content ']/table/tbody/tr/td/div/div[@id]//tr[position()>2]/td")
  public List<WebElement> allCellsExceptHeader;

  @FindBy(xpath = "(//div[@class='mstrmojo-XtabZone']/table)[1]/tbody")
  public WebElement table;

  @FindBy(xpath = "//div[contains(text(),'Loading Data...')]")
  public WebElement loader;

  public CommonReportScreen(WebHarness webHarness) {
    super(webHarness);
    waitForElement(docLayoutViewer);
  }

  public void setDropDownValueBy(String label) {
    waitForElementToDisappear(loader);
    dropdown.click();
    waitForElement(dropdownPopup);
    WebElement selectedValue = getElementByText(dropdownValues, label);
    clickWithScrollToView(selectedValue);
  }

  public void setRadioButtonBy(String label) {
    waitForElementToDisappear(loader);
    WebElement radioButton = webHarness.driver.findElement(
        By.xpath("//label[contains(text(),'" + label + "')]"));
    clickWithScrollToView(radioButton);
  }

  public void setDataPickerValueBy(String label, String date) {
    date = VariablesTransformer.transformSingleValue(date);
    waitForElementToDisappear(loader);
    WebElement element = webHarness.driver.findElement(
        By.xpath("//div[contains(text(),'" + label + "')]/following-sibling::div/input"));
    clickWithScrollToView(element);
    element.clear();
    element.sendKeys(date);
    element.sendKeys(Keys.TAB);
  }

  public void assertDataInTable(List<List<String>> expectedData,
      List<WebElement> tableHeaders,
      List<WebElement> tableCells)
      throws NoSuchElementException {
    //returned list with found row indexes
    List<Integer> foundRowsIndexes = new ArrayList<>();

    //Actual data headers
    List<String> actualTableHeaders = new ArrayList<>();
    Integer actualTableColumnsCount = tableHeaders.size();
    for (WebElement header : tableHeaders) {
      actualTableHeaders.add(header.getText().trim());
    }

    //Expected data headers, if findUsingAllColumns is equal to true, check for all headers and expectedData must not contain header values
    List<String> expectedDataHeaders;
    Integer expectedDataStartIndex;

    //First list from expectedData is the header
    expectedDataHeaders = expectedData.get(0);
    expectedDataStartIndex = 1;

    //Check that all expected headers are present in the table
    for (String expectedHeader : expectedDataHeaders) {
      if (!actualTableHeaders.contains(expectedHeader)) {
        throw new NoSuchElementException(
            "Table does not have header " + expectedHeader + ", found headers "
                + actualTableHeaders);
      }
    }

    List<String> allCellsText = getElementsText(tableCells);
    List<List<String>> actualData = Lists.partition(allCellsText, expectedDataHeaders.size());
    Integer actualDataStartIndex = 0;
    //Skip the first row since it is header
    for (int i = expectedDataStartIndex; i < expectedData.size(); i++) {
      List<String> expectedRow = expectedData.get(i);
      //Check that the row is present on the screen
      Boolean found = false;
      for (Integer actualRowIndex = actualDataStartIndex; actualRowIndex < actualData.size();
          actualRowIndex++) {
        List<String> actualRow = actualData.get(actualRowIndex);
        if (areTableRowValuesEqual(actualRow, expectedRow) && (!foundRowsIndexes
            .contains(actualRowIndex))) {
          foundRowsIndexes.add(actualRowIndex);
          actualDataStartIndex = 0;
          found = true;
          break;
        }
      }
      if (!found) {
        throw new NoSuchElementException("Row with values '" + expectedRow.toString() +
                                             " for columns " + expectedDataHeaders + "' does not exist in table!");
      }
    }
  }
}
