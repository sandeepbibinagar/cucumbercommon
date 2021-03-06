package com.experian.automation.saas.steps.TacticalParameters;

import static org.testng.Assert.assertTrue;

import com.experian.automation.helpers.Config;
import com.experian.automation.logger.Logger;
import com.experian.automation.saas.helpers.TacticalParametersOperations;
import com.experian.automation.transformers.VariablesTransformer;
import cucumber.api.java.en.And;
import java.util.List;

public class TacticalParamsAPISteps {

  private final Logger logger = Logger.getLogger(this.getClass());

  public TacticalParamsAPISteps() {
  }

   /*
   * Usage example(s):
   *  And I update parameter ExpCons_TP - TP - ExpCons_TP Search description: Bureau Test ,effective from: 03/01/2017 to 03/03/2018
   *    | Runtime Environment | DBHost | EAI      | ARF Version | Op Initials | Preamble | Sub Code | Vendor Number | NCUsername            | NCPassword    | Version |
   *    | PRD                 | STAR   | JYE85WXO | 07          | MM          | TEST     | 5991476  | C03           | edanetconnectconsumer | EDAConsumer07 |         |
   *    | PRD                 | STAR   | JYE85WXO | 07          | MM          | TEST     | 5991476  | C03           | edanetconnectconsumer | EDAConsumer07 | N       |
   */

  @And("^I update parameter (.*) description: (.*) ,effective from: (.*?)(?: to (.*))?$")
  public void updateParameterAttributes(String name, String description, String fromDate, String toDate,
      List<List<String>> data) throws Exception {
    TacticalParametersOperations tpo = new TacticalParametersOperations(Config.get("bps.webservices.url"));
    assertTrue(tpo.updateParameter(name, description, fromDate, toDate, data),
               "Successfully updated parameter: " + name);
  }

   /*
   * Usage example(s):
   *   And I deploy tactical parameter ExpCons_TP - TP - ExpCons_TP Search version LATEST
   */

  @And("^I deploy tactical parameter (.*) version (.*)$")
  public void deployParameter(String name, String version)
      throws Exception {
    TacticalParametersOperations tpo = new TacticalParametersOperations(
        Config.get("bps.webservices.url"));
    tpo.deployParameter(name, version);
  }

    /*
    * Usage example(s):
    *   And I update tactical parameters from file ${features.path}/ACF/data/ACF_Tactical_Parameters_Export.xml
    */

  @And("^I update tactical parameters from file (.*)$")
  public void updateParameterFromFile(String filePath) throws Exception {

    String parametersFile = VariablesTransformer.transformSingleValue(filePath);
    TacticalParametersOperations tpo = new TacticalParametersOperations(
        Config.get("bps.webservices.url"));
    List<String> parametersList = tpo.getParametersListFromFile(parametersFile);

    for (int j = 0; j < parametersList.size(); j++) {
      logger.info("Updating parameter: " + parametersList.get(j));
      tpo.updateFromFile(parametersList.get(j),
                         tpo.getLatestParameterVersion(parametersList.get(j)),
                         tpo.getLatestParameterVersionId(parametersList.get(j)), parametersFile);
    }
  }
}
