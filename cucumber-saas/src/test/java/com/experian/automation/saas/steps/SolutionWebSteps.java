package com.experian.automation.saas.steps;


import com.experian.automation.harnesses.TestHarness;
import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.helpers.Config;
import com.experian.automation.saas.screens.SolutionScreen;
import cucumber.api.java.en.And;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SolutionWebSteps {

    private final WebHarness webHarness;
    private final TestHarness testHarness;

    public SolutionWebSteps(WebHarness webHarness, TestHarness testHarness) throws IOException, ConfigurationException {
        this.webHarness = webHarness;
        this.testHarness = testHarness;
    }

    File pageObjectModelFile = new File(new Config().get("temp.dir") + "page-object-" + FilenameUtils.removeExtension(new File(new Config().get("deployable.file")).getName()) + ".json");
    String pageObjectModel = FileUtils.readFileToString(pageObjectModelFile);

    @And("^I select menu item (.*) and sub-menu item (.*) on page (.*)$")
    public void selectMenu(String menu, String subMenu, String pageTitle) throws IOException, ConfigurationException {
        SolutionScreen screen = new SolutionScreen(testHarness, webHarness, pageObjectModel);
        screen.selectMenu(menu, subMenu, pageTitle);
    }

    @And("^I select sub-menu item (.*) in section (.*) in menu (.*) on page (.*)$")
    public void selectMenuWithSection(String subMenu, String sectionItemText, String menu, String pageTitle) throws IOException, ConfigurationException {
        SolutionScreen screen = new SolutionScreen(testHarness, webHarness, pageObjectModel);
        screen.selectMainMenu(menu, pageTitle);
        screen.selectSubMenuSectionItem(sectionItemText, subMenu, pageTitle);
    }

    @And("^I enter value (.*) for field (.*) on page (.*)$")
    public void enterTextFieldValue(String value, String fieldLabel, String pageTitle) throws IOException, ConfigurationException {
        SolutionScreen screen = new SolutionScreen(testHarness, webHarness, pageObjectModel);
        screen.setTextfieldValueByLabel(fieldLabel, value, pageTitle);
    }

    @And("^I enter enter values for textfields on page (.*)$")
    public void enterTextFieldValues(String page, Map<String, String> data) throws IOException, ConfigurationException {
        SolutionScreen screen = new SolutionScreen(testHarness, webHarness, pageObjectModel);
        screen.waitForElementToDisappear(screen.getScreenLoader());
        for (Map.Entry<String, String> entry : data.entrySet()) {
            screen.setTextfieldValueByLabel(entry.getKey(), entry.getValue(), page);
        }
    }

    @And("^I enter enter values for dropdowns on page (.*)$")
    public void enterSelectFieldsValues(String page, Map<String, String> data) throws IOException, ConfigurationException {
        SolutionScreen screen = new SolutionScreen(testHarness, webHarness, pageObjectModel);
        screen.waitForElementToDisappear(screen.getScreenLoader());
        for (Map.Entry<String, String> entry : data.entrySet()) {
            screen.setDropdownValueByLabel(entry.getKey(), entry.getValue(), page);
        }
    }

    @And("^I select value (.*) from dropdown field (.*) on page (.*)$")
    public void selectValueForDropdown(String value, String dropdownLabelTitle, String pageTitle) throws IOException, ConfigurationException {
        SolutionScreen screen = new SolutionScreen(testHarness, webHarness, pageObjectModel);
        screen.setDropdownValueByLabel(dropdownLabelTitle, value, pageTitle);
    }

    @And("^I select value (.*) from dropdown field wit id (.*)$")
    public void selectDropdownValueById(String value, String id) throws IOException, ConfigurationException {
        SolutionScreen screen = new SolutionScreen(testHarness, webHarness, pageObjectModel);
        screen.selectDynamicComboBoxById(id, value);
    }


    @And("^I enter value (.*) for datepicker (.*) on page (.*)$")
    public void enterDatepickerValue(String value, String fieldLabel, String pageTitle) throws IOException, ConfigurationException {
        SolutionScreen screen = new SolutionScreen(testHarness, webHarness, pageObjectModel);
        screen.setDatepickerValueByLabel(fieldLabel, value, pageTitle);
    }

    @And("^I select tab with text (.*) on page (.*)$")
    public void selectTab(String tabText, String pageTitle) throws IOException, ConfigurationException {
        SolutionScreen screen = new SolutionScreen(testHarness, webHarness, pageObjectModel);
        screen.selectTabWithTitle(tabText, pageTitle);
    }

    @And("^I click on element with text (.*)$")
    public void clickOnElementWithText(String text) throws IOException, ConfigurationException {
        SolutionScreen screen = new SolutionScreen(testHarness, webHarness, pageObjectModel);
        screen.clickOnElementWithText(text);
    }

    @And("^I select value (.*) from select with ID (.*)$")
    public void dynamicComboSelect(String value, String selector) throws IOException, ConfigurationException {
        SolutionScreen screen = new SolutionScreen(testHarness, webHarness, pageObjectModel);
        screen.selectDynamicComboBoxById(selector, value);

    }

    @And("^I click on button with (text|id) (.*) on page (.*)$")
    public void buttonClick(String identifier, String value, String pageTitle) throws IOException, ConfigurationException {
        SolutionScreen screen = new SolutionScreen(testHarness, webHarness, pageObjectModel);
        if (identifier.equals("text")) {
            screen.clickButton(value, pageTitle);
        } else {
            screen.clickButton("id", value);
        }
    }

    @And("^I click on button with value (.*)$")
    public void buttonClick(String value) throws IOException, ConfigurationException {
        SolutionScreen screen = new SolutionScreen(testHarness, webHarness, pageObjectModel);
        screen.clickButtonBy("value", value);
    }

    @And("^I select element with title (.*) in column (.*) ,where column (.*) contains value (.*) in table with id (.*)$")
    public void selectLinkInTable(String elementText, String columnText, String searchColumn, String searchColumnRowValue, String tableId) throws IOException, ConfigurationException {
        SolutionScreen screen = new SolutionScreen(testHarness, webHarness, pageObjectModel);
        screen.clickOnElementWithTitleInTable(elementText, columnText, searchColumn, searchColumnRowValue, tableId);
    }

    @And("^I switch to page with title: (.*)$")
    public void switchToPageWithTitle(String title) throws IOException, ConfigurationException {
        SolutionScreen screen = new SolutionScreen(testHarness, webHarness, pageObjectModel);
        screen.waitForWindowWithTitle(title);
    }

    @And("^I verify data in table with (id|class) (.*):$")
    public void verifyDataInTableWithId(String selector, String selectorValue, List<List<String>> data) throws IOException, ConfigurationException {
        SolutionScreen screen = new SolutionScreen(testHarness, webHarness, pageObjectModel);
        screen.verifyDataInTable(selector, selectorValue, data);
    }


    @And("^I verify pairs in section with label (.*):$")
    public void verifyPairs(String label, Map<String, String> values) throws IOException, ConfigurationException, InterruptedException {
        SolutionScreen screen = new SolutionScreen(testHarness, webHarness, pageObjectModel);
        screen.verifyLabelInputPairInSection(label, values);
    }

    @And("^I verify input-label pairs on page (.*):$")
    public void verifyInpuLabelPairsInSection(String page, Map<String, String> data) throws IOException, ConfigurationException {
        SolutionScreen screen = new SolutionScreen(testHarness, webHarness, pageObjectModel);
        screen.verifyInputLabelPairs(page, data);
    }

    @And("^I verify select-label pairs on page (.*):$")
    public void verifySelectLabelPair(String page, Map<String, String> data) throws IOException, ConfigurationException {
        SolutionScreen screen = new SolutionScreen(testHarness, webHarness, pageObjectModel);
        screen.verifySelectLabelPairs(page, data);
    }

    @And("^I fill consecutive inputs with label (.*) on page (.*):$")
    public void fillInputs(String label, String page, List<String> data) throws IOException, ConfigurationException {
        SolutionScreen screen = new SolutionScreen(testHarness, webHarness, pageObjectModel);
        screen.fillConsequtiveInputs(label, data, page);
    }

}
