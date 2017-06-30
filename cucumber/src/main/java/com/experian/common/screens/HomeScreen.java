package com.experian.common.screens;

import com.experian.common.WebClient;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomeScreen extends Screen
{
    public String url = "/";

    @FindBy(linkText = "Logout")
    public WebElement btnLogout;

    @FindBy(linkText = "System")
    public WebElement menuSystem;

    @FindBy(linkText = "Home")
    public WebElement menuHome;

    @FindBy(linkText = "Apply")
    public WebElement menuApply;

    @FindBy(id = "id2")
    public WebElement newApplicationLink;

    @FindBy(linkText = "Experian")
    public WebElement linkExperian;

    public HomeScreen(WebClient client) {
        super(client);
    }


    public void selectSubMenu(WebElement mainMenuItem, WebElement subMenuItem){
        clickElement(mainMenuItem);
        waitForElement(subMenuItem);
        clickElement(subMenuItem);
    }
}
