package com.experian.automation.saas.screens.crb;

import com.experian.automation.harnesses.TestHarness;
import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.screens.Screen;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.List;

/**
 * Created by c08358a on 7/19/2017.
 */
public class AuditTrailScreen extends Screen {

    public String window = "auditTrailWindow";

    @FindBy(xpath = "//table")
    public WebElement tableAuditTrail;

    @FindBy(id = "closeAuditTrail")
    public WebElement buttonCloseAuditTrail;

    @FindBy(xpath = "//div//h1")
    public WebElement pageTitleLabel;

    @FindBy(xpath = "//table//thead//tr//th")
    public List<WebElement> tableHeadersListTableAuditTrail;

    @FindBy(xpath = "//table//tbody//tr//td")
    public List<WebElement> tableCellsListTableAuditTrail;

    public AuditTrailScreen(TestHarness testHarness, WebHarness webHarness) {
        super(testHarness, webHarness);
        switchToWindow(this.window);
    }

    public String getPageTitleLabel(){
        return pageTitleLabel.getText();
    }

}
