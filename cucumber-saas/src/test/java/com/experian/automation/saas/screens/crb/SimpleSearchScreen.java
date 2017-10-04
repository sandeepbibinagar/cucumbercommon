package com.experian.automation.saas.screens.crb;

import com.experian.automation.WebClient;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.Map;
import java.util.NoSuchElementException;
/**
 * Created by c08358a on 7/19/2017.
 */
public class SimpleSearchScreen extends ApplicationSearchScreen {

    //OK
    @FindBy(id = "textfieldApplicationNumber")
    public WebElement applicationNumber;

    //OK
    @FindBy(id = "textfieldForename")
    public WebElement forename;

    //OK
    @FindBy(id = "textfieldSurname")
    public WebElement surname;

    //OK
    @FindBy(id = "TitleDDL")
    public WebElement title;

    //OK
    @FindBy(xpath = "//input[contains(@name,'datepickerDateOfBirth')]")
    public WebElement dateOfBirth;

    //OK
    @FindBy(xpath = "//div[@id='datepickerDateOfBirth']//img[contains(@src,'calendar')]")
    public WebElement dateOfBirthCalendarBtn;

    //OK
    @FindBy(id = "textfieldEmailAddress")
    public WebElement emailAddress;

    public SimpleSearchScreen(WebClient webClient) {
        super(webClient);


    }

    public void set(Map<String, String> dataTable) {

        for (Map.Entry<String, String> entry : dataTable.entrySet()) {

            switch (entry.getKey()) {
                case "Application Number":
                    typeWithClear(applicationNumber, entry.getValue());
                    break;

                case "Forename":
                    typeWithClear(forename, entry.getValue());
                    break;

                case "Surname":
                    typeWithClear(surname, entry.getValue());
                    break;

                case "Title":
                    new Select(title).selectByVisibleText(entry.getValue());
                    break;

                case "Date Of Birth":
                    typeWithClear(dateOfBirth, entry.getValue());
                    break;

                case "Email":
                    typeWithClear(emailAddress, entry.getValue());
                    break;

                default:
                    throw new NoSuchElementException("Unable to set value for field " + entry.getKey());
            }
        }
    }
}
