package com.experian.common.screens.crb;

import com.experian.common.WebClient;
import com.experian.common.screens.Screen;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by c08358a on 7/19/2017.
 */
public class ApplicationSearchScreen extends Screen {

    //OK
    @FindBy(xpath = "//ul[@role='tablist']/li[@role='tab']/a")
    public List<WebElement> tabs;

    //???
    @FindBy(xpath = "//div[contains(@class,'ajax_loader')]")
    public WebElement ajaxLoader;

    //OK
    @FindBy(xpath = "//table[@id='datagrid']//tbody//tr//td[8]//a[child::img[@title='Open']]")
    public List<WebElement> iconOpenList;

    //OK
    @FindBy(xpath = "//table[@id='datagrid']//tbody//tr//td[8]//a[child::img[@title='Audit Trail']]")
    public List<WebElement> iconAuditTrailList;

    //OK
    @FindBy(id = "searchbutton")
    public WebElement buttonSearch;

    //OK
    @FindBy(id = "Clearbutton")
    public WebElement buttonClear;

    //OK
    @FindBy(xpath = "//label[@id='labelPageTitle']")
    public WebElement pageTitleLabel;

    //div[@id='..' and @class='...]

    //OK
    @FindBy(id = "containerNavigation-wrapper")
    public WebElement navigationContainer;

    //OK
    @FindBy(id = "datagrid")
    public WebElement searchTable;

    //OK
    @FindBy(xpath = "//table[@id='datagrid']//tbody//tr//td//div")
    public WebElement tableRows;

    @FindBy(xpath = "//table[@id='datagrid']//tbody//tr//td[1]//div")
    public List<WebElement> tableApplicationNumbers;

    //OK
    public ApplicationSearchScreen(WebClient webClient) {
        super(webClient);
    }

    //OK
    public void selectSearchTab(String tabName) {
        waitForElements(tabs);
        for(WebElement tab: tabs){
            String name = tab.getText();
            if(tab.getText().equals(tabName)) {
                tab.click();
                break;
            }
        }
    }

    public void searchApplication() {
        jsClick(buttonSearch);
        waitForElementToDisappear(ajaxLoader);
    }

    public String getPageTitleLabel(){
        return pageTitleLabel.getText();
    }

    public void openBasicAppDetailsByAppNumber (String applicationNumber) throws Throwable {
        ApplicationSearchScreen screen = new ApplicationSearchScreen(webClient);
        screen.waitForElements(screen.iconOpenList);

        List<String> applicationNumbersText = getElementsText(tableApplicationNumbers);

        int applicationNumberIndex = applicationNumbersText.indexOf(applicationNumber);

        screen.iconOpenList.get(applicationNumberIndex).click();
    }

    public void openAuditTrailByAppNumber (String applicationNumber) throws Throwable {
        ApplicationSearchScreen screen = new ApplicationSearchScreen(webClient);
        screen.waitForElements(screen.iconAuditTrailList);

        List<String> applicationNumbersText = getElementsText(tableApplicationNumbers);

        int applicationNumberIndex = applicationNumbersText.indexOf(applicationNumber);

        screen.iconAuditTrailList.get(applicationNumberIndex).click();

    }
}


