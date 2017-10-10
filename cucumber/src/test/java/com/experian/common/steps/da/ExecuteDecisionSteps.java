package com.experian.common.steps.da;

import com.experian.common.helpers.GenericComparator;
import com.experian.common.helpers.PropertiesConfigurator;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;

import java.io.*;
import java.util.Properties;


public class ExecuteDecisionSteps {
    @Given("^I execute a bat file")
    public void executeBat() throws Throwable{
        System.out.println("hello world 1");
        GenericComparator.fileCompareWithIgnoreDNC("/da/CS_Results.txt", "C:/automation/tmp/CS_Results.txt");
    }

    @Then("^I see hello world printed in the console")
    public void verifyResult() throws Throwable{
        System.out.println("hello world 2");

        PropertiesConfigurator p = new PropertiesConfigurator("c:/automation/config.properties");
        p.setProperties("database.host", "host3");
        p.save();
        //p.revert();
    }
}
