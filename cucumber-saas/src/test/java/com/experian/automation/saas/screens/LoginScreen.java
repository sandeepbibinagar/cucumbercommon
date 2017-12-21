package com.experian.automation.saas.screens;

import com.experian.automation.harnesses.TestHarness;
import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.screens.Screen;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginScreen extends Screen {

  public String url = "${base.url}/";

  @FindBy(id = "IDToken1")
  public WebElement usernameText;

  @FindBy(id = "IDToken2")
  public WebElement passwordText;

  @FindBy(xpath = "//div[@class='logBtn']")
  public WebElement loginBtn;

  public LoginScreen(TestHarness testHarness, WebHarness webHarness) {
    super(testHarness, webHarness);
  }

  public void goToURL() {
    goToURL(this.url);
    waitForScreen(this.loginBtn);
  }
}
