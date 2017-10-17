package com.experian.automation.saas.steps.crb;

import com.experian.automation.harnesses.TestHarness;
import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.logger.Logger;
import com.experian.automation.saas.screens.HomeScreen;
import com.experian.automation.saas.screens.crb.DynamicParameterMaintenanceScreen;
import cucumber.api.java.en.And;

import java.util.Map;

import static org.testng.Assert.assertEquals;

public class DynamicalParametersSteps {

    private final Logger logger = Logger.getLogger(this.getClass());
    private final TestHarness testHarness;
    private final WebHarness webHarness;

    public DynamicalParametersSteps(TestHarness testHarness, WebHarness webHarness) {
        this.testHarness = testHarness;
        this.webHarness = webHarness;
    }

/*
    And I import dynamic parameters from files:
       | Purpose of Purchase                         | Purpose of Purchase.csv                             |
       | Education                                   | Education.csv                                       |
       | Occupation                                  | Occupation.csv                                      |
       | Document Type                               | Document Type.csv                                   |
       | Override Reason                             | Override Reason.csv                                 |
       | Contact method                              | Contact method.csv                                  |
       | Gender                                      | Gender.csv                                          |
       | Country                                     | Country.csv                                         |
       | Marital Status                              | Marital Status.csv                                  |
       | Purpose of Loan                             | Purpose of Loan.csv                                 |
       | Residential Status                          | Residential Status.csv                              |
       | Title                                       | Title.csv                                           |
       | Employment Status                           | Employment Status.csv                               |
       | Current Lender                              | Current Lender.csv                                  |
       | Relationship to applicant                   | Relationship to applicant.csv                       |
       | Master Product-Second Product-Third Product | ALL-Master Product-Second Product-Third Product.csv |
*/
    @And("^I import dynamic parameters from files:$")
    public void importDynamicParameters(Map<String, String> parameters) throws Throwable {
        HomeScreen home = new HomeScreen(testHarness, webHarness);
        home.selectMenu("System", "Dynamic Parameter Maintenance");
        DynamicParameterMaintenanceScreen screen = new DynamicParameterMaintenanceScreen(testHarness, webHarness);
        screen.waitForElements(screen.dynamicParameters);
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            screen.uploadDynamicalParameter(entry.getKey(), entry.getValue());
        }
        screen.closeWindow();
    }
}
