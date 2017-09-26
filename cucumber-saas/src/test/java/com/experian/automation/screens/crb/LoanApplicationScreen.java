package com.experian.automation.screens.crb;

import com.experian.automation.WebClient;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Created by B04342A on 6/27/2017.
 */
public class LoanApplicationScreen extends BasicApplicationScreen {

    @FindBy(id = "RequestedLoanAmountTxt")
    public WebElement loanAmount;

    @FindBy(id = "RequestedLoanTermTxt")
    public WebElement loanTerm;

    @FindBy(id = "PurposeofLoanDdl")
    public WebElement loanPurpose;

    public LoanApplicationScreen(WebClient webClient) {
        super(webClient);
    }

    public void set(Map<String, String> dataTable) throws InterruptedException {

        for (Map.Entry<String, String> entry : dataTable.entrySet()) {
            switch (entry.getKey()) {
                case "Requested Loan Amount":
                    typeWithValueReplace(loanAmount,entry.getValue());
                    break;

                case "Requested Loan Term":
                    typeWithValueReplace(loanTerm,entry.getValue());
                    break;

                case "Purpose of Loan":
                    new Select(loanPurpose).selectByVisibleText(entry.getValue());
                    break;


                default:
                    throw new NoSuchElementException("Unable to set value for field " + entry.getKey());
            }
        }
    }
}
