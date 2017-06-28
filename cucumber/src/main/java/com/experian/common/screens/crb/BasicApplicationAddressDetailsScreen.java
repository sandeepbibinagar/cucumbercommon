package com.experian.common.screens.crb;

import com.experian.common.WebClient;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Created by B04342A on 6/27/2017.
 */
public class BasicApplicationAddressDetailsScreen extends BasicApplicationScreen{

    @FindBy(id = "currAddressLine1Txt")
    public WebElement number;

    @FindBy(id = "currAddressLine4Txt")
    public WebElement street;

    @FindBy(id = "currAddressLine5Txt")
    public WebElement city;

    @FindBy(id = "currAddressPostCodeTxt")
    public WebElement postCode;

    @FindBy(id = "currAddressCountryDDL")
    public WebElement country;

    @FindBy(id = "timeAtAddressYearsTxt")
    public WebElement timeAtAddressYears;

    @FindBy(id = "timeAtAddressMonthsTxt")
    public WebElement timeAtAddressMonths;

    @FindBy(id = "homePhoneNoTxt")
    public WebElement homePhoneNumber;

    @FindBy(id = "MobilePhoneTxt")
    public WebElement mobilePhoneNumber;

    @FindBy(id = "ResidentialStatusDDL")
    public WebElement recidentialStatus;

    public BasicApplicationAddressDetailsScreen(WebClient webClient) {
        super(webClient);
    }

    public void set(Map<String, String> dataTable) {

        for (Map.Entry<String, String> entry : dataTable.entrySet()) {

            if (entry.getKey().equals("Number")) {

                typeWithClear(number, entry.getValue());

            } else if (entry.getKey().equals("Street")) {

                typeWithClear(street, entry.getValue());

            } else if (entry.getKey().equals("City")) {

                typeWithClear(city, entry.getValue());

            } else if (entry.getKey().equals("Postcode")) {

                typeWithClear(postCode, entry.getValue());

            } else if (entry.getKey().equals("Country")) {

                new Select(country).selectByVisibleText(entry.getValue());

            }
            else if (entry.getKey().equals("Time at Address Years")) {

                typeWithClear(timeAtAddressYears, entry.getValue());

            } else if (entry.getKey().equals("Time at Address Months")) {

                typeWithClear(timeAtAddressMonths, entry.getValue());

            } else if (entry.getKey().equals("Home Phone Number")) {

                typeWithClear(homePhoneNumber, entry.getValue());

            } else if (entry.getKey().equals("Mobile Phone Number")) {

                typeWithClear(mobilePhoneNumber, entry.getValue());

            }
            else if (entry.getKey().equals("Residential Status")) {

                new Select(recidentialStatus).selectByVisibleText(entry.getValue());
            }

            else {

                throw new NoSuchElementException("Unable to set value for field " + entry.getKey());
            }
        }
    }
}
