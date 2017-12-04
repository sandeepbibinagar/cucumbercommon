package com.experian.automation.saas.screens.WebEngine;

import com.experian.automation.harnesses.TestHarness;
import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.screens.Screen;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class WebEngineHome extends Screen {

    private String window = "Home Page";
    private String url = testHarness.config.get("webengine.base.url");

    @FindBy(xpath = "//ul[@id='menu']/li/a")
    public List<WebElement> mainMenuItems;

    @FindBy(xpath = "//ul[@id='menu']/li//ul//li/a")
    public List<WebElement> subMenuItems;

    public WebEngineHome(TestHarness testHarness, WebHarness webHarness) {
        super(testHarness, webHarness);
        goToURL(url);
    }

    public void selectMainMenu(String mainMenu) {
       clickWithScrollToView(getElementWithPartialText(mainMenuItems, mainMenu));
    }

    public void selectSubMenu(String subMenu) {
        clickWithScrollToView(getElementByText(subMenuItems, subMenu));
    }

    public void selectMenu(String mainMenu, String subMenu) {
        selectMainMenu(mainMenu);
        selectSubMenu(subMenu);

    }
}
