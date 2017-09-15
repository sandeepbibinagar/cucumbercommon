package com.experian.common.screens.crb;

import com.experian.common.WebClient;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Created by b04342a on 6/29/2017.
 */
public class FinancialDetailsScreen extends BasicApplicationScreen {

    @FindBy(id = "AnnualSalaryTxt")
    public WebElement annualSalary;

    @FindBy(id = "MonthlyIncomeAfterDeductionsTxt")
    public WebElement monthlyIncomeAfter;

    @FindBy(id = "OtherPersonalMonthlyIncomeAfterDeductionsTxt")
    public WebElement otherIncome;

    @FindBy(id = "MonthlyCreditCardAndStoreCardPaymentsTxt")
    public WebElement monthlyCardPayments;

    @FindBy(id = "MonthlyLoanPaymentsTxt")
    public WebElement monthlyLoanPayments;

    @FindBy(id = "OtherRetailMonthlyPaymentsTxt")
    public WebElement retailMonthlyPayments;

    @FindBy(id = "MonthlyMortgageRentPaymentsTXT")
    public WebElement monthlyMotgageRentPayments;

    @FindBy(id = "BankNameTxt")
    public WebElement bankName;

    @FindBy(id = "TimeWithBankYYTxt")
    public WebElement timeBankYears;

    @FindBy(id = "TimeWithBankMMTxt")
    public WebElement timeBankMonths;

    @FindBy(id = "IBANTxt")
    public WebElement iban;

    public FinancialDetailsScreen(WebClient webClient) {
        super(webClient);
    }

    public void set(Map<String, String> dataTable) {

        for (Map.Entry<String, String> entry : dataTable.entrySet()) {

            switch (entry.getKey()) {
                case "Annual Salary":
                    typeWithValueReplace(annualSalary, entry.getValue());
                    break;

                case "Monthly Income After Deductions":
                    waitForElementToDisappear(ajaxLoader);
                    typeWithValueReplace(monthlyIncomeAfter, entry.getValue());
                    break;

                case "Other Personal Monthly Income After Deductions":
                    typeWithValueReplace(otherIncome, entry.getValue());
                    break;

                case "Monthly Credit Card and Store Card Payments":
                    typeWithValueReplace(monthlyCardPayments, entry.getValue());
                    break;

                case "Monthly Loan Payments":
                    typeWithValueReplace(monthlyLoanPayments, entry.getValue());
                    break;

                case "Other Retail Monthly Payments":
                    typeWithValueReplace(retailMonthlyPayments, entry.getValue());
                    break;

                case "Monthly Mortgage / Rent Payments":
                    typeWithValueReplace(monthlyMotgageRentPayments, entry.getValue());
                    break;

                case "Bank Name":
                    waitForElementToDisappear(ajaxLoader);
                    typeWithClear(bankName, entry.getValue());
                    break;

                case "Time with Bank Years":
                    typeWithClear(timeBankYears, entry.getValue());
                    break;

                case "Time with Bank Months":
                    typeWithClear(timeBankMonths, entry.getValue());
                    break;

                case "IBAN":
                    typeWithClear(iban, entry.getValue());
                    break;

                default:
                    throw new NoSuchElementException("Unable to set value for field " + entry.getKey());
            }
        }
    }

}
