package com.experian.automation.saas.steps.BiReport;

import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.saas.screens.BiReporting.CommonReportScreen;
import com.experian.automation.transformers.VariablesTransformer;
import cucumber.api.PendingException;
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
  private CommonReportScreen reportScreen;

  public ReportFilterSelectionStep(WebHarness webHarness) {
    this.webHarness = webHarness;
  }

  @Then("^I verify that the following data is displayed in the table:$")
  public void iVerifyTheDataIsDisplayed(List<List<String>> data) throws Throwable {
    data = VariablesTransformer.transformTable(data);
    CommonReportScreen reportScreen = new CommonReportScreen(webHarness);
    reportScreen.waitForElementToDisappear(reportScreen.loader);
    reportScreen.waitForElement(reportScreen.table);
    reportScreen.waitForElements(reportScreen.firstRowHeaderElements);

    List<WebElement> tableHeaders = new ArrayList<>();
    tableHeaders.addAll(reportScreen.firstRowHeaderElements);
    reportScreen.waitForElements(reportScreen.secondRowHeaderElements);
    for (int i = 0; i < reportScreen.secondRowHeaderElements.size(); i++) {
      tableHeaders.add(reportScreen.secondRowHeaderElements.get(i));
    }

    List<WebElement> tableCells = new ArrayList<>(tableHeaders);
    for (int i = 0; i < reportScreen.allCellsExceptHeader.size(); i++) {
      tableCells.add(reportScreen.allCellsExceptHeader.get(i));
    }

    reportScreen.findDataInTable(data, tableHeaders, tableCells, true, false, false);
  }

  @And("^I filter group data by (.*)$")
  public void iFilterGroupDataBy(String label) {
    reportScreen = new CommonReportScreen(webHarness);
    reportScreen.setDropDownValueBy(label);
  }

  @And("^I filter show data by (.*)$")
  public void iFilterShowDataBy(String label) {
    reportScreen = new CommonReportScreen(webHarness);
    reportScreen.setRadioButtonBy(label);
  }

  @And("^I set time interval to (.*)$")
  public void iSetTimeIntervalTo(String label) {
    reportScreen = new CommonReportScreen(webHarness);
    reportScreen.setRadioButtonBy(label);
  }

  @And("^I set (From|To) date to (.*)$")
  public void iSetFromDateTo(String label, String date) {
    reportScreen = new CommonReportScreen(webHarness);
    reportScreen.setDataPickerValueBy(label, date);
  }
}
