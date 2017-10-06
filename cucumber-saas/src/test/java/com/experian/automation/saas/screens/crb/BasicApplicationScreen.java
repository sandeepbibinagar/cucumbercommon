package com.experian.automation.saas.screens.crb;

import com.experian.automation.harnesses.TestHarness;
import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.screens.Screen;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Created by B04342A on 6/21/2017.
 */
public class BasicApplicationScreen extends Screen {

    public String windowTitle = "Basic Application Details Page";

    @FindBy(xpath = "//ul[@role='tablist']/li[@role='tab']/a")
    public List<WebElement> tabs;

    @FindBy(xpath = "//button[contains(@name,'Next')]")
    public WebElement buttonNext;

    @FindBy(xpath = "//div[contains(@class,'ajax_loader')]")
    public WebElement ajaxLoader;

    @FindBy(id = "ButtonSaveBt")
    public WebElement buttonSave;

    @FindBy(id = "EditBT")
    public WebElement buttonEdit;

    @FindBy(xpath = "//label[@id='PageTitleLbl' or @id='PageTitileLBL' or @id='PageTitleLBL']")
    public WebElement pageTitleLabel;

    @FindBy(id = "NavigationCont-wrapper")
    public WebElement navigationContainer;

    public BasicApplicationScreen(TestHarness testHarness, WebHarness webHarness) {
        super(testHarness, webHarness);
    }

    public void selectDetailsTab(String tabName) {
        waitForElementToDisappear(ajaxLoader);
        getElementWithText(tabs,tabName).click();
    }

    public void proceedNext() {
        jsClick(buttonNext);
        if (isElementPresented(ajaxLoader)) waitForElementToDisappear(ajaxLoader);
    }

    public void edit() {
        waitForScreen(buttonEdit);
        buttonEdit.click();
    }

    public String getPageTitleLabel() {
        return pageTitleLabel.getText();
    }
}
