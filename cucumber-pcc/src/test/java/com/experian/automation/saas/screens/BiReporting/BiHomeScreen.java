package com.experian.automation.saas.screens.BiReporting;

import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.screens.Screen;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class BiHomeScreen extends Screen {

  @FindBy(xpath = "//div[@id='projects_ProjectsStyle']")
  public WebElement testEnvList;

  @FindBy(xpath = "//div[@class='mstrIcon-wait']")
  public WebElement loaders;

  public BiHomeScreen(WebHarness webHarness) {
    super(webHarness);
    waitForElement(testEnvList);
  }
}
