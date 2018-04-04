package com.experian.automation.saas.screens.WebEngine;

import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.helpers.Config;
import com.experian.automation.screens.Screen;
import java.net.MalformedURLException;
import java.nio.file.NoSuchFileException;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

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

  public DynamicParameterMaintenanceScreen(WebHarness webHarness) {
    super(webHarness);
    switchToWindow(window);
    waitForElement(header);
  }

  public void uploadDynamicalParameter(String parameterName, String fileName)
      throws MalformedURLException, NoSuchFileException {
    getElementWithText(dynamicParameters, parameterName).click();
    waitForElement(parameterValuesMenu);
    if (parameterValuesList.size() == 0) {
      new Actions(webHarness.driver).contextClick(
          getElementWithText(dynamicParameters, parameterName)).build().perform();
      importButton.click();
      fileUploadInput.sendKeys(Config.get("features.path") + fileName);
      importParameterButton.click();
      waitForElement(importResult);
      imporCancelButton.click();
    }
  }
}
