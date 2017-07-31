package com.experian.common.screens.crb;

import com.experian.common.WebClient;
import com.experian.common.screens.Screen;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Created by B04342A on 6/21/2017.
 */
public class BasicApplicationScreen extends Screen {

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


    @FindBy(xpath = "//label[@id='PageTitleLbl' or @id='PageTitleLBL']")
    public WebElement pageTitleLabel;

    //div[@id='..' and @class='...]

    @FindBy(id = "NavigationCont-wrapper")
    public WebElement navigationContainer;

    public BasicApplicationScreen(WebClient webClient) {
        super(webClient);
    }

    public void selectDetailsTab(String tabName) {
        waitForElements(tabs);
        for(WebElement tab: tabs){
            String name = tab.getText();
            if(tab.getText().equals(tabName)) {
                tab.click();
                break;
            }
        }
    }

    public void proceedNext() {
        jsClick(buttonNext);
        if(isElementPresented(ajaxLoader)) waitForElementToDisappear(ajaxLoader);
    }

    public void edit(){
        waitForScreen(buttonEdit);
        buttonEdit.click();
    }

    public String getPageTitleLabel(){
        return pageTitleLabel.getText();
    }
}
