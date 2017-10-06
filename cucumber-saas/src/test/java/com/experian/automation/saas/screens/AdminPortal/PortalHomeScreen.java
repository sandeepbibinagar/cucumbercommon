package com.experian.automation.saas.screens.AdminPortal;

import com.experian.automation.harnesses.TestHarness;
import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.screens.Screen;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class PortalHomeScreen extends Screen {

    @FindBy(xpath = "//nav//span[contains(text(),'SaaS Portal')]")
    public WebElement portalHeader;

    @FindBy(xpath="//div[@class='destination']")
    public List<WebElement> solutions;

    @FindBy(id = "originations")
    public WebElement originationsSolution;

    @FindBy(id = "admin-portal")
    public WebElement adminPortal;

    public PortalHomeScreen(TestHarness testHarness, WebHarness webHarness){
        super(testHarness, webHarness);
        waitForElements(solutions);
    }

}
