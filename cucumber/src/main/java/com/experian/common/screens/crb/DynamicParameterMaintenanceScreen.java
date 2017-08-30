package com.experian.common.screens.crb;

import com.experian.common.WebClient;
import com.experian.common.screens.Screen;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;

import java.net.URL;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.List;

public class DynamicParameterMaintenanceScreen extends Screen {

    public String window = "myWindow";

    @FindBy(xpath="//div//h1[contains(text(),'Dynamic Parameter Administration')]")
    public WebElement header;

    @FindBy(xpath="//li[@title]/span")
    public List<WebElement> dynamicParameters;

    @FindBy(xpath="//li[@class='import']")
    public WebElement importButton;

    @FindBy(id="input_file")
    public WebElement fileUploadInput;

    @FindBy(id="import-ok-btn")
    public WebElement importParameterButton;

    @FindBy(id="import-cancel-btn")
    public WebElement imporCancelButton;

    @FindBy(xpath="//legend[contains(text(),'Import Result')]")
    public WebElement importResult;

    public DynamicParameterMaintenanceScreen(WebClient webClient) {
        super(webClient);
        switchToWindow(window);
        waitForElement(header);
    }

    public void uploadDynamicalParameter(String parameterName, String fileName) throws MalformedURLException, NoSuchFileException {
        boolean found = false;
        for (WebElement parameterElement : dynamicParameters) {
            if (parameterElement.getText().equals(parameterName)) {
                found = true;
                parameterElement.click();
                new Actions(webClient.driver).contextClick(parameterElement).build().perform();
                importButton.click();
                fileUploadInput.sendKeys(webClient.config.get("solution.parameters.dir") + fileName);
                importParameterButton.click();
                waitForElement(importResult);
                imporCancelButton.click();
                break;
            }
        }
        if(found==false){
            throw new NoSuchElementException("Element with name "+parameterName+" not found.");
        }
    }

}
