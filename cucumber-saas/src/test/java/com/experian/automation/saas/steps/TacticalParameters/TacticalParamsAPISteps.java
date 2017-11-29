package com.experian.automation.saas.steps.TacticalParameters;

import com.experian.automation.harnesses.TestHarness;
import com.experian.automation.logger.Logger;
import com.experian.automation.saas.helpers.TacticalParametersOperations;
import com.mashape.unirest.http.exceptions.UnirestException;
import cucumber.api.java.en.And;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.IOException;

public class TacticalParamsAPISteps {
    private final TestHarness testHarness;
    private final Logger logger = Logger.getLogger(this.getClass());

    public TacticalParamsAPISteps(TestHarness testHarness) {
        this.testHarness = testHarness;
    }

    @And("^I list all tactical parameters$")
    public void listAllParameters() throws IOException, ConfigurationException, UnirestException {
        TacticalParametersOperations tpo = new TacticalParametersOperations();
        tpo.getAllParameters();
    }

    @And("^I list tactical parameter with id - (.*)$")
    public void listParameterByID(String id) throws IOException, ConfigurationException, UnirestException {
        TacticalParametersOperations tpo = new TacticalParametersOperations();
        System.out.println(tpo.getParameter(id));
    }

    @And("^I update tactical parameter:$")
    public void updateParameter(String data) throws IOException, ConfigurationException, UnirestException {
        TacticalParametersOperations tpo = new TacticalParametersOperations();
        tpo.updateParameter(data);
    }

    @And("^I deploy tactical parameter with id - (.*)$")
    public void deployParameter(String id) throws IOException, ConfigurationException, UnirestException {
        TacticalParametersOperations tpo = new TacticalParametersOperations();
        tpo.deployParameter(id);
    }
}
