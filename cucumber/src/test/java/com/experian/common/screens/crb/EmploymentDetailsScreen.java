package com.experian.common.screens.crb;

import com.experian.common.WebClient;
import com.experian.common.screens.DateTimePickerScreen;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Created by b04342a on 6/29/2017.
 */
public class EmploymentDetailsScreen extends BasicApplicationScreen {

    @FindBy(id = "OccupationDdl")
    public WebElement occupation;

    @FindBy(id = "employerTxt")
    public WebElement employer;

    @FindBy(id = "workAddressLine1Txt")
    public WebElement premise;

    @FindBy(id = "workAddressLine2Txt")
    public WebElement street;

    @FindBy(id = "workAddressLine3Txt")
    public WebElement street2;

    @FindBy(id = "workAddressLine4Txt")
    public WebElement street3;

    @FindBy(id = "workAddressLine5Txt")
    public WebElement city;

    @FindBy(id = "workAddressPostCodeTxt")
    public WebElement postCode;

    @FindBy(id = "CountryDDL")
    public WebElement country;

    @FindBy(xpath = "//input[contains(@name,'StartDateOfEmployment')]")
    public WebElement startDate;

    @FindBy(xpath = "//div[@id='StartEndDateEmploymentCont-wrapper']//img[contains(@src,'calendar')]")
    public WebElement startDateCalendarBtn;

    @FindBy(id = "workPhoneNoTxt")
    public WebElement workPhone;

    public EmploymentDetailsScreen(WebClient webClient) {
        super(webClient);
    }

    public void set(Map<String, String> dataTable) {

        for (Map.Entry<String, String> entry : dataTable.entrySet()) {

            switch (entry.getKey()) {
                case "Occupation":
                    new Select(occupation).selectByVisibleText(entry.getValue());
                    break;

                case "Employer":
                    typeWithClear(employer, entry.getValue());
                    break;

                case "Premise":
                    typeWithClear(premise, entry.getValue());
                    break;

                case "Street":
                    typeWithClear(street, entry.getValue());
                    break;

                case "Street2":
                    typeWithClear(street2, entry.getValue());
                    break;

                case "Street3":
                    typeWithClear(street3, entry.getValue());
                    break;

                case "City":
                    typeWithClear(city, entry.getValue());
                    break;

                case "Post Code":
                    typeWithClear(postCode, entry.getValue());
                    break;

                case "Country":
                    new Select(country).selectByVisibleText(entry.getValue());
                    break;

                case "Start Date":
                    new DateTimePickerScreen(webClient).setDate(startDateCalendarBtn, entry.getValue(), "yyyy MM dd");
                    break;

                case "Work Phone":
                    typeWithClear(workPhone, entry.getValue());
                    break;

                default:
                    throw new NoSuchElementException("Unable to set value for field " + entry.getKey());
            }
        }
    }

}
