package com.experian.automation.screens.crb;

import com.experian.automation.WebClient;
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

    public AuditTrailScreen(WebClient webClient) {
        super(webClient);
        switchToWindow(this.window);
    }

    public String getPageTitleLabel(){
        return pageTitleLabel.getText();
    }

}
