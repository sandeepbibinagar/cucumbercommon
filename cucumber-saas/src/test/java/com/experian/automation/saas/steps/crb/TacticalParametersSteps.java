package com.experian.automation.saas.steps.crb;

import com.experian.automation.harnesses.TestHarness;
import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.logger.Logger;
import com.experian.automation.saas.screens.HomeScreen;
import com.experian.automation.saas.screens.crb.TacticalParametersMaintananceScreen;
import cucumber.api.java.en.And;

import java.util.Map;

public class TacticalParametersSteps {

    private final Logger logger = Logger.getLogger(this.getClass());
    private final TestHarness testHarness;
    private final WebHarness webHarness;

    public TacticalParametersSteps(TestHarness testHarness, WebHarness webHarness) {
        this.testHarness = testHarness;
        this.webHarness = webHarness;
    }


/*
     And I import tactical parameters from files:
     | ProveID Parameters - Get ProveID Parameters                         | ALL-CRBPT-TacticalParam.xml |
     | Experian Bureau Parameters - Get Experian Bureau Parameters         | ALL-CRBPT-TacticalParam.xml |
     | Experian Product Parameters - Get Experian Product Parameters       | ALL-CRBPT-TacticalParam.xml |
     | Client Parameters - Get Client Parameters                           | ALL-CRBPT-TacticalParam.xml |
     | Experian Interfaces Parameters - Get Experian Interfaces Parameters | ALL-CRBPT-TacticalParam.xml |
     | Quotation Parameters - Get Quotation Parameters                     | ALL-CRBPT-TacticalParam.xml |
     | Loan Parameters - Get Loan Parameters                               | ALL-CRBPT-TacticalParam.xml |
     | Current Account Parameters - Get Current Account Parameters         | ALL-CRBPT-TacticalParam.xml |
     | Saving Account Parameters - Get Saving Account Parameters           | ALL-CRBPT-TacticalParam.xml |
     | Mortgage Parameters - Get Mortage Parameters                        | ALL-CRBPT-TacticalParam.xml |
     | Credit Card Parameters - Get Credit Card Parameters                 | ALL-CRBPT-TacticalParam.xml |
     | Experian Solution Parameters - Get Experian Solution Parameters     | ALL-CRBPT-TacticalParam.xml |
*/
    @And("^I import tactical parameters from files:$")
    public void importTacticalParameters(Map<String, String> data) throws Throwable {
        HomeScreen home = new HomeScreen(testHarness, webHarness);
        home.selectMenu("System", "Tactical Parameters Maintenance");
        TacticalParametersMaintananceScreen screen = new TacticalParametersMaintananceScreen(testHarness, webHarness);
        for (Map.Entry<String, String> params : data.entrySet()) {
            screen.uploadTacticalParameter(params.getKey(), params.getValue());
        }
        screen.closeWindow();
    }
}

