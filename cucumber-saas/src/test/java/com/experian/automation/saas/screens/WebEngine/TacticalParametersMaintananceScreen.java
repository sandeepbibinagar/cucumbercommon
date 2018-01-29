package com.experian.automation.saas.screens.WebEngine;

import com.experian.automation.harnesses.TestHarness;
import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.helpers.Config;
import com.experian.automation.screens.Screen;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

public class TacticalParametersMaintananceScreen extends Screen {

  public String window = "Decision Analytics platform - Tactical Parameter Administration";

  @FindBy(xpath = "//div//h1[contains(text(),'Tactical Parameter Administration')]")
  public WebElement header;

  @FindBy(xpath = "//ul[@id='parameter_list_view']/li/div[starts-with(@class,'group_parameter_names_list_item')]")
  public List<WebElement> tacticalParameters;

  @FindBy(xpath = "//label/span[contains(text(),'Imp./Exp.')]")
  public WebElement impExpButton;

  @FindBy(id = "logoutLink")
  public WebElement logout;

  @FindBy(id = "importFile")
  public WebElement importFile;

  @FindBy(id = "param_choice")
  public WebElement parameterSelect;

  @FindBy(xpath = "//div[preceding-sibling::a[@class='ui-tabs-anchor'] and not(not(@onclick))]")
  public WebElement paramTabCloseButton;

  @FindBy(xpath = "//input[@type='file' and preceding-sibling::label[contains(text(),'File')]]")
  public WebElement fileUploadInput;

  @FindBy(xpath = "//input[following-sibling::label[position()=1 and contains(text(),'Overwrite - current version is overwritten if not deployed, import aborted otherwise.')]]")
  public WebElement overwriteVersionInput;

  @FindBy(xpath = "//span[contains(text(),'Version 1')]/..")
  public WebElement parameterVersion;

  @FindBy(xpath = "//label[contains(text(),'Description*')]")
  public WebElement discriptionLabel;

  @FindBy(xpath = "//input[not(not(@value)) and preceding-sibling::label[contains(text(),'Description*')]]")
  public List<WebElement> inputsWithValue;

  @FindBy(id = "import-ok-btn")
  public WebElement okButton;

  @FindBy(xpath = "//button[child::span[contains(text(),'OK')] and not(@id)]")
  public WebElement importMessageOkButton;

  public TacticalParametersMaintananceScreen(TestHarness testHarness, WebHarness webHarness) {
    super(testHarness, webHarness);
    switchWindowByTitle(window);
    waitForElement(header);
  }

  public void uploadTacticalParameter(String parameter, String file) {
    getElementWithText(tacticalParameters, parameter).click();
    waitForElement(parameterVersion);
    parameterVersion.click();
    waitForElement(discriptionLabel);
    if (inputsWithValue.size() != 0) {
      paramTabCloseButton.click();
      waitForElementToDisappear(discriptionLabel);
      initElements();
      getElementWithText(tacticalParameters, parameter).click();
      waitForElementToDisappear(parameterVersion);
    } else {
      waitForElement(impExpButton);
      impExpButton.click();
      waitForElement(importFile);
      importFile.click();
      waitForElement(parameterSelect);
      new Select(parameterSelect).selectByVisibleText(parameter);
      fileUploadInput.sendKeys(Config.get("solution.parameters.dir") + file);
      overwriteVersionInput.click();
      okButton.click();
      importMessageOkButton.click();
      waitForElementToDisappear(importMessageOkButton);
      waitForElementToDisappear(parameterVersion);
    }
  }
}

