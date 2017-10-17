package com.experian.automation.saas.steps.crb;

import com.experian.automation.harnesses.TestHarness;
import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.logger.Logger;
import com.experian.automation.saas.screens.crb.*;
import com.experian.automation.saas.screens.HomeScreen;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.Map;
import static org.testng.Assert.assertTrue;

/**
 * Created by B04342A on 6/24/2017.
 */
public class NewApplicationSteps {

    private final TestHarness testHarness;
    private final WebHarness webHarness;
    private final Logger logger = Logger.getLogger(this.getClass());

    public NewApplicationSteps(TestHarness testHarness, WebHarness webHarness) {
        this.testHarness = testHarness;
        this.webHarness = webHarness;
    }

    @And("^I start a new application$")
    public void startNewApplication() throws Throwable {
        HomeScreen homeScreen = new HomeScreen(testHarness, webHarness);
        homeScreen.selectMenu("Apply", "New Application");
        BasicApplicationScreen basicApplicationScreen = new BasicApplicationScreen(testHarness, webHarness);
        basicApplicationScreen.waitForWindowWithTitle(basicApplicationScreen.windowTitle);
    }

    @And("^I enter the new applicant personal details:$")
    public void enterPersonalDetails(Map<String, String> details) throws Throwable {
        PersonalDetailsScreen personalDetailsScreen = new PersonalDetailsScreen(testHarness, webHarness);
        personalDetailsScreen.set(details);
    }

    @And("^I navigate to \"([^\"]*)\" tab$")
    public void navigateToTab(String tabName) throws Throwable {
        BasicApplicationScreen basicApplicationScreen = new BasicApplicationScreen(testHarness, webHarness);
        basicApplicationScreen.waitForElementToDisappear(basicApplicationScreen.ajaxLoader);
        basicApplicationScreen.selectDetailsTab(tabName);
    }

    @And("^I select a \"([^\"]*)\" product$")
    public void selectProduct(String productName) throws Throwable {
        ProductDetailsScreen productDetailsScreen = new ProductDetailsScreen(testHarness, webHarness);
        productDetailsScreen.selectProduct(productName);

    }

    @And("^I proceed to \"([^\"]*)\" page$")
    public void proceed(String pageName) throws Throwable {
        BasicApplicationScreen basicApplicationScreen = new BasicApplicationScreen(testHarness, webHarness);
        String labelOldContent = basicApplicationScreen.pageTitleLabel.getText();
        basicApplicationScreen.proceedNext();
        basicApplicationScreen.waitForElementContentChange(basicApplicationScreen.pageTitleLabel,labelOldContent);
        assertTrue((basicApplicationScreen.getPageTitleLabel()).equals(pageName),"Page "+pageName+" loaded");
    }

    @And("^I enter identification information:$")
    public void enterIdentificationDetails(Map<String, String> details) throws Throwable {
        enterPersonalDetails(details);
    }

    @And("^I enter applicant's address details:$")
    public void enterAddressDetails(Map<String, String> details) throws Throwable {
        AddressDetailsScreen addressDetailsScreen = new AddressDetailsScreen(testHarness, webHarness);
        addressDetailsScreen.set(details);
    }

    @And("^I choose loan type \"([^\"]*)\"$")
    public void chooseLoanType(String loanType) throws Throwable {
        ProductChoiceScreen productChoiceScreen = new ProductChoiceScreen(testHarness, webHarness);
        productChoiceScreen.waitForElements(productChoiceScreen.radioButtons);
        productChoiceScreen.selectProductByType(loanType);
        productChoiceScreen.refreshScreen();
    }

    @And("^I enter loan details:$")
    public void enterLoanDetails(Map<String, String> details) throws Throwable {
        ProductChoiceScreen productChoiceScreen = new ProductChoiceScreen(testHarness, webHarness);
        // The below refresh is a workaround for non-appearing Next button
        productChoiceScreen.set(details);
    }

    @And("^I enter requested loan details:$")
    public void enterRequestedLoanDetails(Map<String, String> details) throws Throwable {
        LoanApplicationScreen loanApplicationScreen = new LoanApplicationScreen(testHarness, webHarness);
        loanApplicationScreen.set(details);
    }

    @And("^I proceed to Main Applicant Details edit$")
    public void goToMainApplicantDetais() throws Throwable {
        BasicApplicationScreen basicApplicationScreen = new BasicApplicationScreen(testHarness, webHarness);
        String labelOldContent = basicApplicationScreen.pageTitleLabel.getText();
        basicApplicationScreen.edit();
        basicApplicationScreen.waitForElementContentChange(basicApplicationScreen.pageTitleLabel,labelOldContent);
        assertTrue((basicApplicationScreen.getPageTitleLabel()).equals("Employment & Financial Details"));
    }

    @And("^I enter employment details:$")
    public void enterEmploymentDetails(Map<String, String> details) throws Throwable {
        EmploymentDetailsScreen employmentDetailsScreen = new EmploymentDetailsScreen(testHarness, webHarness);
        employmentDetailsScreen.set(details);
    }

    @And("^I enter financial details:$")
    public void enterFinancialDetails(Map<String, String> details) throws Throwable {
        FinancialDetailsScreen financialDetailsScreen = new FinancialDetailsScreen(testHarness, webHarness);
        financialDetailsScreen.waitForElementToDisappear(financialDetailsScreen.ajaxLoader);
        financialDetailsScreen.set(details);
    }

    @When("^I submit the application$")
    public void submitApplication() throws Throwable {
        proceed("Decision Summary");
    }

    @Then("^I should see that application has been accepted$")
    public void checkAcceptedApplication() throws Throwable {
        DecisionSummaryScreen decisionSummaryScreen = new DecisionSummaryScreen(testHarness, webHarness);
        decisionSummaryScreen.waitForElement(decisionSummaryScreen.acceptApplicationImage);
    }
}
