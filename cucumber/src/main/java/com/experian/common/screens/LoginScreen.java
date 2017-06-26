package com.experian.common.screens;

import com.experian.common.WebClient;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginScreen extends Screen
{
    public String url = "/";

    @FindBy(id = "IDToken1")
    public WebElement usernameText;

    @FindBy(id = "IDToken2")
    public WebElement passwordText;

    @FindBy(xpath = "//div[@class='logBtn']")
    public WebElement loginBtn;

    public LoginScreen(WebClient client)
    {
        super(client);
    }

    public void goToURL() {
        appendBaseURLandGoTo(this.url);
        waitForScreen(this.loginBtn);
    }
}
