package com.experian.common.steps.crb;

import com.experian.common.WebClient;
import com.experian.common.core.logger.Logger;
import com.experian.common.screens.HomeScreen;
import com.experian.common.screens.crb.BasicApplicationPersonalDetailsScreen;
import com.experian.common.screens.crb.BasicApplicationScreen;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.Map;

/**
 * Created by B04342A on 6/24/2017.
 */
public class NewApplicationSteps {

    private final WebClient webClient;
    private final Logger logger = Logger.getLogger(this.getClass());

    public NewApplicationSteps(WebClient webClient) {
        this.webClient = webClient;
    }

    @And("^I start a new application$")
    public void startNewApplication() throws Throwable {
        HomeScreen homeScreen = new HomeScreen(webClient);
        homeScreen.selectSubMenu(homeScreen.menuApply, homeScreen.newApplicationLink);
        BasicApplicationScreen basicApplicationScreen = new BasicApplicationScreen(webClient);
    }

    @And("^I enter the new applicant personal details:$")
    public void populatePersonalDetails(Map<String, String> details) throws Throwable {
        BasicApplicationPersonalDetailsScreen personalDetailsScreen = new BasicApplicationPersonalDetailsScreen(webClient);
        personalDetailsScreen.set(details);
    }

    @And("^I navigate to (.*) tab$")
    public void navigateToTab(String tabName) throws Throwable {
        BasicApplicationScreen basicApplicationScreen = new BasicApplicationPersonalDetailsScreen(webClient);
        basicApplicationScreen.selectDetailsTab(tabName);
    }

    @And("^I select a \"([^\"]*)\" product$")
    public void iSelectAProduct(String arg0) throws Throwable {

    }

    @And("^I proceed to \"([^\"]*)\" page$")
    public void iProceedToPage(String arg0) throws Throwable {

    }

    @And("^I enter identification information:$")
    public void iEnterIdentificationInformation(Map<String, String> details) throws Throwable {

    }

    @And("^I enter applicant's address details:$")
    public void iEnterApplicantSAddressDetails(Map<String, String> details) throws Throwable {

    }

    @And("^I choose Product Type \"([^\"]*)\"$")
    public void iChooseProductType(String arg0) throws Throwable {

    }

    @And("^I enter loan details:$")
    public void iEnterLoanDetails() throws Throwable {

    }

    @And("^I procced to edit the Main Applicant Details$")
    public void iProccedToEditTheMainApplicantDetails() throws Throwable {

    }

    @And("^I enter employment details:$")
    public void iEnterEmploymentDetails(Map<String, String> details) throws Throwable {

    }

    @And("^I enter financial details:$")
    public void iEnterFinancialDetails(Map<String, String> details) throws Throwable {

    }

    @When("^I submit the application$")
    public void iSubmitTheApplication() throws Throwable {

    }

    @Then("^I should see \"([^\"]*)\" page$")
    public void iShouldSeePage(String arg0) throws Throwable {

    }
}
