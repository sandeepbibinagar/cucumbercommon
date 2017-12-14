package com.experian.automation.saas.steps.TacticalParameters;

import com.experian.automation.harnesses.TestHarness;
import com.experian.automation.logger.Logger;
import com.experian.automation.saas.helpers.TacticalParametersOperations;
import com.experian.automation.transformers.DataTransformer;
import com.mashape.unirest.http.exceptions.UnirestException;
import cucumber.api.java.en.And;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.IOException;
import java.util.List;


public class TacticalParamsAPISteps {
    private final TestHarness testHarness;
    private final Logger logger = Logger.getLogger(this.getClass());

    public TacticalParamsAPISteps(TestHarness testHarness) {
        this.testHarness = testHarness;
    }

    @And("^I list all tactical parameters$")
    public void listAllParameters() throws IOException, ConfigurationException, UnirestException {
        TacticalParametersOperations tpo = new TacticalParametersOperations(testHarness.config.get("bps.webservices.url"));
        tpo.getAllParameters();
    }

    @And("^I list tactical parameter with id - (.*)$")
    public void listParameterByID(String id) throws IOException, ConfigurationException, UnirestException {
        TacticalParametersOperations tpo = new TacticalParametersOperations(testHarness.config.get("bps.webservices.url"));
        System.out.println(tpo.getParameter(id));
    }

    @And("^I update tactical parameter:$")
    public void updateParameter(String data) throws IOException, ConfigurationException, UnirestException {
        TacticalParametersOperations tpo = new TacticalParametersOperations(testHarness.config.get("bps.webservices.url"));
        tpo.updateParameter(data);
    }


    @And("^I deploy tactical parameter (.*) version (.*)$")
    public void deployParameter(String name, String version) throws IOException, ConfigurationException, UnirestException {
        TacticalParametersOperations tpo = new TacticalParametersOperations(testHarness.config.get("bps.webservices.url"));
        tpo.deployParameter(name, version);
    }

    /*
    And I update tactical parameters from file ${features.path}/ACF/data/ACF_Tactical_Parameters_Export.xml
    */
    @And("^I update tactical parameters from file (.*)$")
    public void updateParameterFromFile(String filePath) throws IOException, ConfigurationException, UnirestException {

        String parametersFile = DataTransformer.transformSingleValue(filePath, testHarness.stepData);
        TacticalParametersOperations tpo = new TacticalParametersOperations(testHarness.config.get("bps.webservices.url"));
        List<String> parametersList = tpo.getParametersListFromFile(parametersFile);

        for (int j = 0; j < parametersList.size(); j++) {
            logger.info("Updating parameter: " + parametersList.get(j));
            tpo.updateFromFile(parametersList.get(j), tpo.getLatestParameterVersion(parametersList.get(j)), tpo.getLatestParameterVersionId(parametersList.get(j)), parametersFile);
        }
    }
}
