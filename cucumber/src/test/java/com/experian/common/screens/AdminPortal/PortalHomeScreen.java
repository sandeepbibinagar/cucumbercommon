package com.experian.common.screens.AdminPortal;

import com.experian.common.WebClient;
import com.experian.common.screens.Screen;
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

    public PortalHomeScreen(WebClient webClient){
        super(webClient);
        waitForElements(solutions);
    }

}
