package com.experian.common.screens.AdminPortal;

import com.experian.common.WebClient;
import com.experian.common.screens.Screen;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class PortalLoginScreen extends Screen {

    public String url = "/";

    @FindBy(id= "input-username")
    public WebElement usernameInput;

    @FindBy(id = "input-password")
    public WebElement passwordInput;

    @FindBy(id = "login-button")
    public WebElement loginButton;

    public PortalLoginScreen(WebClient webClient){
        super(webClient);
    }

    public void goToURL() {
        appendBaseURLandGoTo(this.url);
        waitForScreen(this.loginButton);
    }

}
