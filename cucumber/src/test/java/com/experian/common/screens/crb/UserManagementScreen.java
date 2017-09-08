package com.experian.common.screens.crb;

import com.experian.common.WebClient;
import com.experian.common.screens.Screen;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.Map;

public class UserManagementScreen extends Screen{

    public String window = "myWindow";

    @FindBy(xpath="//a[contains(text(),'Security Profiles')]")
    public WebElement securityProfilesButton;

    @FindBy(xpath="//ul[@id='draggableSecuProfiles']/li/a[contains(text(),'Administrator')]")
    public WebElement administratorProfile;

    @FindBy(xpath="//a[@title='Edit']")
    public WebElement profilePermissionsEditButton;

    @FindBy(xpath="//a[contains(text(),' Allow all')]")
    public WebElement allowAllButton;

    @FindBy(xpath="//span[contains(text(),'Ok') and parent::button]")
    public WebElement okButton;

    @FindBy(xpath="//thead/tr/th[@class='profileColHeader ' or not(@class)]")
    public List<WebElement> businessRulesTableHeaderCells;

    @FindBy(xpath="//table[@id='spTable']//tbody/tr/td")
    public List<WebElement> businessRulesTableRowCells;

    @FindBy(xpath="//td[@class='deny']")
    public List<WebElement> listOfDeniedPermissions;

    public UserManagementScreen(WebClient webClient) {
        super(webClient);
        switchToWindow(window);
    }

    public void selectBusinessProcessRules(String permission, String feature) {

        Integer headerIndex = -1;
        Integer rowIndex = -1;
        Integer elementToClickPosition = -1;

        for (int headerCell = 0; headerCell <= businessRulesTableHeaderCells.size(); headerCell++) {
            if (businessRulesTableHeaderCells.get(headerCell).getText().equals(permission)) {
                headerIndex = headerCell;
                break;
            }
        }

        for (int rowCellIndex = 0; rowCellIndex <= businessRulesTableRowCells.size(); rowCellIndex++) {
            if (businessRulesTableRowCells.get(rowCellIndex).getText().equals(feature)) {
                rowIndex = rowCellIndex;
                break;
            }
        }
        elementToClickPosition = headerIndex + rowIndex;
        if (headerIndex < 0 || rowIndex < 0) {
            throw new NoSuchElementException("No such element at row: " + rowIndex + " position:" + headerIndex);
        } else {
            String oldClassAttribute = businessRulesTableRowCells.get(elementToClickPosition).getAttribute("class");
            businessRulesTableRowCells.get(elementToClickPosition).click();
            waitForElementAttributeChange(businessRulesTableRowCells.get(elementToClickPosition),"class",oldClassAttribute);
        }
    }

}
