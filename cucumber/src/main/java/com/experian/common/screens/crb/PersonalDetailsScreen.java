package com.experian.common.screens.crb;

import com.experian.common.WebClient;
import com.experian.common.screens.DateTimePickerScreen;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.*;

/**
 * Created by B04342A on 6/21/2017.
 */
public class PersonalDetailsScreen extends BasicApplicationScreen {

    @FindBy(xpath = "//select[contains(@name, 'TitleCont:Title')]")
    public WebElement titleList;

    @FindBy(id = "DocumentTypeDDL")
    public WebElement documentType;

    @FindBy(id = "DocumentIdNumberTxt")
    public WebElement documentIdNumber;

    @FindBy(xpath = "//input[contains(@name,'DocumentIdExpiryDateDate:datePickerInput')]")
    public WebElement documentIdExpiryDate;

    @FindBy(id = "SurnameTxt")
    public WebElement surname;

    @FindBy(id = "ForenameTxt")
    public WebElement forename;

    @FindBy(xpath = "//input[contains(@name,'DOBDate')]")
    public WebElement dateOfBirth;

    @FindBy(id = "ExistingCustomerFlagDL")
    public WebElement existingCustomer;

    @FindBy(id = "ExistingCustomerNumberTxt")
    public WebElement existingCustomerNumber;

    @FindBy(id = "MaritalStatusDddl")
    public WebElement maritalStatus;

    @FindBy(id = "HomePhoneNumberTxt")
    public WebElement homePhoneNumber;

    @FindBy(id = "EmailAddressTxt")
    public WebElement emailAddress;

    @FindBy(id = "ResidentialStatusDddl")
    public WebElement residentialStatus;

    @FindBy(id = "EmployStatusDdl")
    public WebElement employmentStatus;

    @FindBy(id = "TotalIncomeTxt")
    public WebElement totalAnnualIncome;

    @FindBy(xpath = "//div[@id='DOBCont']//img[contains(@src,'calendar')]")
    public WebElement dateOfBirthCalendarBtn;

    @FindBy(xpath = "//div[@id='DocumentIdNumberTypeCont']//img[contains(@src,'calendar')]")
    public WebElement expiryDateCalendarBtn;


    public PersonalDetailsScreen(WebClient webClient) {
        super(webClient);
        waitForScreen(titleList);
    }

    public void set(Map<String, String> dataTable) {
        waitForElementToDisappear(ajaxLoader);
        for (Map.Entry<String, String> entry : dataTable.entrySet()) {

            switch (entry.getKey()) {
                case "Title":
                    new Select(titleList).selectByVisibleText(entry.getValue());
                    break;

                case "Surname":
                    typeWithClear(surname, entry.getValue());
                    break;

                case "Identification Type":
                    new Select(documentType).selectByVisibleText(entry.getValue());
                    break;

                case "ID Number":
                    typeWithClear(documentIdNumber, entry.getValue());
                    break;

                case "Expiry Date":

                    new DateTimePickerScreen(webClient).setDate(expiryDateCalendarBtn,entry.getValue(),"yyyy MM dd");
                    break;

                case "Forename":
                    typeWithClear(forename, entry.getValue());
                    break;

                case "Date Of Birth":

                    new DateTimePickerScreen(webClient).setDate(dateOfBirthCalendarBtn, entry.getValue(), "yyyy MM dd");
                    break;

                case "Existing Customer":
                    new Select(existingCustomer).selectByVisibleText(entry.getValue());
                    break;

                case "Existing Customer Number":
                    waitForScreen(existingCustomerNumber);
                    typeWithClear(existingCustomerNumber, entry.getValue());
                    break;

                case "Marital Status":
                    new Select(maritalStatus).selectByVisibleText(entry.getValue());
                    break;

                case "Home Phone Number":
                    typeWithClear(homePhoneNumber, entry.getValue());
                    break;

                case "Email":
                    typeWithClear(emailAddress, entry.getValue());
                    break;

                case "Residential Status":
                    new Select(residentialStatus).selectByVisibleText(entry.getValue());
                    break;

                case "Employment Status":
                    new Select(employmentStatus).selectByVisibleText(entry.getValue());
                    break;

                case "Total Annual Income":
                    typeWithValueReplace(totalAnnualIncome, entry.getValue());
                    break;

                default:
                    throw new NoSuchElementException("Unable to set value for field " + entry.getKey());
            }
        }
    }
}
