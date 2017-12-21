package com.experian.automation.saas.screens.AdminPortal;

import com.experian.automation.harnesses.TestHarness;
import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.screens.Screen;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class PortalLoginScreen extends Screen {

  public String url = "${base.url}/";

  @FindBy(id = "input-username")
  public WebElement usernameInput;

  @FindBy(id = "input-password")
  public WebElement passwordInput;

  @FindBy(id = "login-button")
  public WebElement loginButton;

  public PortalLoginScreen(TestHarness testHarness, WebHarness webHarness) {
    super(testHarness, webHarness);
  }

  public void goToURL() {
    goToURL(this.url);
    waitForScreen(this.loginButton);
  }

}
