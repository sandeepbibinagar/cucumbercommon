package com.experian.automation.saas.screens.crb;

import com.experian.automation.harnesses.TestHarness;
import com.experian.automation.harnesses.WebHarness;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Created by B04342A on 6/21/2017.
 */
public class DecisionSummaryScreen extends BasicApplicationScreen {

    @FindBy(id = "DecisionAcceptedImage")
    public WebElement acceptApplicationImage;

    public DecisionSummaryScreen(TestHarness testHarness, WebHarness webHarness) { super(testHarness, webHarness); }

}
