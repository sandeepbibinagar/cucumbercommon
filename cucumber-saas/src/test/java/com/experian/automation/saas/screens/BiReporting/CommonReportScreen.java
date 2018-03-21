package com.experian.automation.saas.screens.BiReporting;

import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.helpers.Variables;
import com.experian.automation.saas.steps.BiReport.ReportFilterSelectionStep;
import com.experian.automation.screens.Screen;
import com.experian.automation.transformers.VariablesTransformer;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

  @FindBy(xpath = "(//div[@class='mstrmojo-XtabZone']/table)[1]/tbody")
  public WebElement table;

  @FindBy(xpath = "//div[contains(text(),'Loading Data...')]")
  public WebElement loader;

  public CommonReportScreen(WebHarness webHarness) {
    super(webHarness);
    waitForElement(docLayoutViewer);
  }

  public void setDropDownValueBy(String label) throws InterruptedException {
    waitForElementToDisappear(loader);
    WebElement dropdown = webHarness.driver.findElement(By.xpath("//div[@id='mstr92']"));
    dropdown.click();
    waitForElementPresence(By.xpath("//div[@id='mstr93']"));
    WebElement option = webHarness.driver.findElement(
        By.xpath("//div[@id='mstr93']//*[contains(text(),'" + label + "')]"));
    clickWithScrollToView(option);
    waitForElementToDisappear(loader);
  }

  public void setRadioButtonBy(String label) throws InterruptedException {
    waitForElementToDisappear(loader);
    WebElement radioButton = webHarness.driver.findElement(
        By.xpath("//label[contains(text(),'" + label + "')]"));
    clickWithScrollToView(radioButton);
    waitForElementToDisappear(loader);
  }

  public void setDataPickerValueBy(String label, String date) throws InterruptedException {
    date = VariablesTransformer.transformSingleValue(date);
    waitForElementToDisappear(loader);
    WebElement element = webHarness.driver.findElement(
        By.xpath("//div[contains(text(),'" + label + "')]/following-sibling::div/input"));
    clickWithScrollToView(element);
    element.clear();
    element.sendKeys(date);
    element.sendKeys(Keys.TAB);
  }
}
