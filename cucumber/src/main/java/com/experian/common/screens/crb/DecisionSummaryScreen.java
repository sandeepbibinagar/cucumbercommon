package com.experian.common.screens.crb;

import com.experian.common.WebClient;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Created by B04342A on 6/21/2017.
 */
public class DecisionSummaryScreen extends BasicApplicationScreen {

    @FindBy(id = "DecisionAcceptedImage")
    public WebElement acceptApplicationImage;

    public DecisionSummaryScreen(WebClient webClient) { super(webClient); }

}
