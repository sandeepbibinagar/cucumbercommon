package com.experian.automation.screens.crb;

import com.experian.automation.WebClient;
import com.experian.automation.screens.Screen;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import java.net.MalformedURLException;

import java.nio.file.NoSuchFileException;
import java.util.List;

public class DynamicParameterMaintenanceScreen extends Screen {

    public String window = "myWindow";

    @FindBy(xpath = "//div//h1[contains(text(),'Dynamic Parameter Administration')]")
    public WebElement header;

    @FindBy(xpath = "//li[@title]/span")
    public List<WebElement> dynamicParameters;

    @FindBy(xpath = "//li[@class='import']")
    public WebElement importButton;

    @FindBy(id = "input_file")
    public WebElement fileUploadInput;

    @FindBy(id = "import-ok-btn")
    public WebElement importParameterButton;

    @FindBy(id = "import-cancel-btn")
    public WebElement imporCancelButton;

    @FindBy(xpath = "//legend[contains(text(),'Import Result')]")
    public WebElement importResult;

    @FindBy(xpath = "//div[@class='param_list param_level_1']")
    public WebElement parameterValuesMenu;

    @FindBy(xpath = "//li[@title and ancestor::div[@class='param_list param_level_1']]")
    public List<WebElement> parameterValuesList;

    public DynamicParameterMaintenanceScreen(WebClient webClient) {
        super(webClient);
        switchToWindow(window);
        waitForElement(header);
    }

    public void uploadDynamicalParameter(String parameterName, String fileName) throws MalformedURLException, NoSuchFileException {
        getElementWithText(dynamicParameters, parameterName).click();
        waitForElement(parameterValuesMenu);
        if (parameterValuesList.size() == 0) {
            new Actions(webClient.driver).contextClick(getElementWithText(dynamicParameters, parameterName)).build().perform();
            importButton.click();
            fileUploadInput.sendKeys(webClient.config.get("solution.parameters.dir") + fileName);
            importParameterButton.click();
            waitForElement(importResult);
            imporCancelButton.click();
        }
    }
}
