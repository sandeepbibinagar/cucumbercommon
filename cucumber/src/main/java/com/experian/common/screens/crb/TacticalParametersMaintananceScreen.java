package com.experian.common.screens.crb;

import com.experian.common.WebClient;
import com.experian.common.screens.Screen;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.Map;

public class TacticalParametersMaintananceScreen extends Screen {

    public String window = "myWindow";

    @FindBy(xpath="//div//h1[contains(text(),'Tactical Parameter Administration')]")
    public WebElement header;

    @FindBy(xpath="//ul[@id='parameter_list_view']/li/div[@class='group_parameter_names_list_item']")
    public List<WebElement> tacticalParameters;

    @FindBy(xpath="//label/span[contains(text(),'Imp./Exp.')]")
    public WebElement importButton;

    @FindBy(id="importFile")
    public WebElement importFile;

    @FindBy(id="param_choice")
    public WebElement parameterSelect;

    @FindBy(xpath="//input[@type='file' and preceding-sibling::label[contains(text(),'File')]]")
    public WebElement fileUploadInput;

    @FindBy(xpath="//input[following-sibling::label[position()=1 and contains(text(),'Overwrite - current version is overwritten if not deployed, import aborted otherwise.')]]")
    public WebElement overwriteVersionInput;

    @FindBy(id="import-ok-btn")
    public WebElement okButton;

    @FindBy(xpath="//button[child::span[contains(text(),'OK')] and not(@id)]")
    public WebElement importMessageOkButton;

    public TacticalParametersMaintananceScreen(WebClient webClient) {
        super(webClient);
        switchToWindow(window);
        waitForElement(header);
    }

    public void uploadTacticalParameter(String parameter, String file) {
        getElementWithText(tacticalParameters, parameter);
        importButton.click();
        waitForElement(importFile);
        importFile.click();
        waitForElement(parameterSelect);
        new Select(parameterSelect).selectByVisibleText(getElementWithText(tacticalParameters, parameter).getText());
        fileUploadInput.sendKeys(webClient.config.get("solution.parameters.dir") + file);
        overwriteVersionInput.click();
        okButton.click();
        importMessageOkButton.click();
    }
}

