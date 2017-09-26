package com.experian.automation.screens.crb;

import com.experian.automation.WebClient;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Created by c08358a on 7/19/2017.
 */
public class AdvancedSearchScreen extends ApplicationSearchScreen {

    //OK
    @FindBy(id = "textfieldCreateUserId")
    public WebElement userId;

    //OK
    @FindBy(xpath = "//input[contains(@name,'datepickerApplicationDate:datePickerInput')]")
    public WebElement dateFrom;

    //OK
    @FindBy(xpath = "//div[@id='datepickerApplicationDate']//img[contains(@src,'calendar')]")
    public WebElement deteFromCalendarBtn;

    //OK
    @FindBy(xpath = "//input[contains(@name,'datepickerApplicationDateTo:datePickerInput')]")
    public WebElement dateTo;

    //OK
    @FindBy(xpath = "//div[@id='datepickerApplicationDateTo']//img[contains(@src,'calendar')]")
    public WebElement deteToCalendarBtn;

    //OK
    @FindBy(id = "textfieldApplicationTime")
    public WebElement timeFrom;

    //OK
    @FindBy(id = "textfieldApplicationTimeTo")
    public WebElement timeTo;

    //OK
    @FindBy(id = "dropdownlistFinalDecision")
    public WebElement finalDecision;

    //OK
    @FindBy(id = "StatusDropdownlist")
    public WebElement status;


    public AdvancedSearchScreen(WebClient webClient) {
        super(webClient);
        //waitForScreen(applicationNumber);
    }
}
