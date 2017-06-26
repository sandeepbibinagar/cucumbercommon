package com.experian.common.screens.crb;

import com.experian.common.WebClient;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Created by B04342A on 6/21/2017.
 */
public class BasicApplicationPersonalDetailsScreen extends BasicApplicationScreen {

    @FindBy(id = "TitleDddl")
    public WebElement titleList;

    @FindBy(id = "SurnameTxt")
    public WebElement surname;

    @FindBy(id = "ForenameTxt")
    public WebElement forename;

    @FindBy(xpath = "//input[contains(@name,'DOBDate')]")
    public WebElement dateOfBirth;

    @FindBy(id = "ExistingCustomerFlagDL")
    public WebElement existingCustomerList;

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

    public BasicApplicationPersonalDetailsScreen(WebClient webClient) {
        super(webClient);
        waitForElement(titleList);
    }

    public void set(Map<String, String> dataTable) {
        for (Map.Entry<String, String> entry : dataTable.entrySet()) {

            if (entry.getKey().equals("Title")) {

                new Select(titleList).selectByVisibleText(entry.getValue());

            } else if (entry.getKey().equals("Surname")) {

                type(surname, entry.getValue());

            } else if (entry.getKey().equals("Forename")) {

                type(forename, entry.getValue());

            } else if (entry.getKey().equals("Date Of Birth")) {

                typeWithClear(dateOfBirth, entry.getValue());

            } else if (entry.getKey().equals("Existing Customer")) {

                new Select(existingCustomerList).selectByVisibleText(entry.getValue());

                if(entry.getValue() == "Yes") waitForElement(existingCustomerNumber);

            } else if (entry.getKey().equals("Existing Customer Number")) {

               // type(existingCustomerNumber, entry.getValue());

            } else if (entry.getKey().equals("Marital Status")) {

                new Select(maritalStatus).selectByVisibleText(entry.getValue());

            } else if (entry.getKey().equals("Home Phone Number")) {

                type(homePhoneNumber, entry.getValue());

            } else if (entry.getKey().equals("Email")) {

                type(emailAddress, entry.getValue());

            } else if (entry.getKey().equals("Residential Status")) {

                new Select(residentialStatus).selectByVisibleText(entry.getValue());

            } else if (entry.getKey().equals("Employment Status")) {

                new Select(employmentStatus).selectByVisibleText(entry.getValue());

            } else if (entry.getKey().equals("Total Annual Income")) {

                typeWithClear(totalAnnualIncome, entry.getValue());

            } else {

                throw new NoSuchElementException("Unable to set value for field " + entry.getKey());
            }
        }
    }


}
