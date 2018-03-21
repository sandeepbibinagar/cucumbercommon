package com.experian.automation.saas.steps.BiReport;

import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.saas.screens.BiReporting.CommonReportScreen;
import com.experian.automation.transformers.VariablesTransformer;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ReportFilterSelectionStep {

  private final WebHarness webHarness;

  public ReportFilterSelectionStep(WebHarness webHarness) {
    this.webHarness = webHarness;
  }

  @And("^I select:$")
  public void iSelect(Map<String, String> data) {
    CommonReportScreen reportScreen = new CommonReportScreen(webHarness);
    reportScreen.waitForElement(reportScreen.docLayoutViewer);
    for (Map.Entry<String, String> entry : data.entrySet()) {
      setReportFilters(entry);
    }
  }

  public void setReportFilters(Map.Entry<String, String> entry) {
    CommonReportScreen reportScreen = new CommonReportScreen(webHarness);
    switch (entry.getKey()) {
      case ("Group Data By"):
        try {
          reportScreen.setDropDownValueBy(entry.getValue());
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        break;
      case ("Show Data By"):
        switch (entry.getValue()) {
          case ("Number of Applications"):
          case ("Applications Amount"):
            try {
              reportScreen.setRadioButtonBy(entry.getValue());
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
            break;
          default:
            throw new NoSuchElementException("No such element: " + entry.getValue());
        }
        break;
      case ("Time Interval"):
        switch (entry.getValue()) {
          case ("Month"):
          case ("Week"):
          case ("Date"):
            try {
              reportScreen.setRadioButtonBy(entry.getValue());
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
            break;
          default:
            throw new NoSuchElementException("No such element: " + entry.getValue());
        }
        break;
      case ("From"):
      case ("To"):
        try {
          reportScreen.setDataPickerValueBy(entry.getKey(), entry.getValue());
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        break;
      default:
        throw new org.openqa.selenium.NoSuchElementException("Illegal element type argument: " + entry.getKey());
    }
  }


  @Then("^I verify that the result:$")
  public void iVerifyThatTheResult(List<List<String>> data) throws Throwable {
    if (data == null || data.isEmpty()) {
      throw new IllegalArgumentException("The given argument as result is null or empty!");
    }
    data = VariablesTransformer.transformTable(data);
    CommonReportScreen reportScreen = new CommonReportScreen(webHarness);
    reportScreen.waitForElementToDisappear(reportScreen.loader);
    reportScreen.waitForElement(reportScreen.table);

    List<WebElement> tableHeaders = new ArrayList<>();

    tableHeaders = reportScreen.waitForElementsPresence(
        By.xpath("(//div[@class='mstrmojo-XtabZone']/table)[1]/tbody/tr[1]/td[1]"));
    tableHeaders.addAll(
        reportScreen.waitForElementsPresence(
            By.xpath("(//div[@class='mstrmojo-XtabZone']/table)[1]/tbody/tr[2]/td[position()>1]")));

    List<WebElement> tableCells = new ArrayList<>(tableHeaders);
    tableCells.addAll(reportScreen.waitForElementsPresence(
        By.xpath("(//div[@class='mstrmojo-XtabZone']/table)[1]/tbody//tr[position()>2]/td")));

    reportScreen.findDataInTable(data, tableHeaders, tableCells, true, false, false);
  }

}
