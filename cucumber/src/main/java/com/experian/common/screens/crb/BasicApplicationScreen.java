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

    @FindBy(xpath = "//div[@id='tabcontainer']/ul/li/a")
    public List<WebElement> tabs;



    public BasicApplicationScreen(WebClient webClient) {
        super(webClient);
    }

    public void selectDetailsTab(String tabName) {
        for(WebElement tab: tabs){
            if(tab.getText().equals(tabName)) {
                tab.click();
            }
        }
    }
}
