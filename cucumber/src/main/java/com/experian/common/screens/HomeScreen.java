package com.experian.common.screens;

import com.experian.common.WebClient;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class HomeScreen extends Screen {
    public String url = "/";

    @FindBy(xpath = "//ul[@id='menu']/li/a")
    public List<WebElement> mainMenuItems;

    @FindBy(xpath = "//ul[@id='menu']/li//ul//li/a")
    public List<WebElement> subMenuItems;

    @FindBy(xpath = "//ul[@id='menu']/li/div[@class='sub-container mega']")
    public WebElement subContainer;

    public HomeScreen(WebClient client) {
        super(client);
    }


    public void selectMenu(String mainMenuItemName, String subMenuItemName) throws InterruptedException {
        clickWithScrollToView(getElementWithText(mainMenuItems, mainMenuItemName));
        clickWithScrollToView(getElementWithText(subMenuItems, subMenuItemName));
    }
}
