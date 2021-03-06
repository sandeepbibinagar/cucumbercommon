package com.experian.automation.saas.steps.TacticalParameters;

import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.logger.Logger;
import com.experian.automation.saas.screens.HomeScreen;
import com.experian.automation.saas.screens.WebEngine.DynamicParameterMaintenanceScreen;
import cucumber.api.java.en.And;

import java.util.Map;

import static org.testng.Assert.assertEquals;

public class DynamicalParametersScreenSteps {

  private final Logger logger = Logger.getLogger(this.getClass());
  private final WebHarness webHarness;

  public DynamicalParametersScreenSteps(WebHarness webHarness) {
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
    DynamicParameterMaintenanceScreen screen = new DynamicParameterMaintenanceScreen(webHarness);
    screen.waitForElements(screen.dynamicParameters);
    for (Map.Entry<String, String> entry : parameters.entrySet()) {
      screen.uploadDynamicalParameter(entry.getKey(), entry.getValue());
    }
    screen.closeWindow();
  }
}
