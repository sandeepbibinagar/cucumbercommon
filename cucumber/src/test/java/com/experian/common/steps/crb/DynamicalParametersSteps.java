package com.experian.common.steps.crb;

import com.experian.common.WebClient;
import com.experian.common.logger.Logger;
import com.experian.common.screens.HomeScreen;
import com.experian.common.screens.crb.DynamicParameterMaintenanceScreen;
import cucumber.api.java.en.And;

import java.util.Map;

import static org.testng.Assert.assertEquals;

public class DynamicalParametersSteps {

    private final Logger logger = Logger.getLogger(this.getClass());
    private final WebClient webClient;

    public DynamicalParametersSteps(WebClient webClient) {
        this.webClient = webClient;
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
        HomeScreen home = new HomeScreen(webClient);
        home.selectMenu("System", "Dynamic Parameter Maintenance");
        DynamicParameterMaintenanceScreen screen = new DynamicParameterMaintenanceScreen(webClient);
        screen.waitForElements(screen.dynamicParameters);
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            screen.uploadDynamicalParameter(entry.getKey(), entry.getValue());
        }
        screen.closeWindow();
    }
}
