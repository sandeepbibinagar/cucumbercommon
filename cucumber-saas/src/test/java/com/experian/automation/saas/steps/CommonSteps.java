package com.experian.automation.saas.steps;

import com.experian.automation.harnesses.TestHarness;
import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.helpers.ArchiversOperations;
import com.experian.automation.helpers.FSOperations;
import com.experian.automation.helpers.TextFileOperations;
import com.experian.automation.helpers.XMLOperations;
import com.experian.automation.logger.Logger;
import com.experian.automation.saas.screens.AdminPortal.PortalHomeScreen;
import com.experian.automation.saas.screens.LoginScreen;
import com.experian.automation.saas.screens.AdminPortal.PortalLoginScreen;
import com.experian.automation.saas.screens.HomeScreen;

import com.experian.automation.steps.FileOperationsSteps;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;

import java.io.File;
import java.io.IOException;

/**
 * Created by B04342A on 6/24/2017.
 */
public class CommonSteps {

    private final TestHarness testHarness;
    private final WebHarness webHarness;
    private final Logger logger = Logger.getLogger(this.getClass());

    public CommonSteps(TestHarness testHarness, WebHarness webHarness) {
        this.testHarness = testHarness;
        this.webHarness = webHarness;
    }

    @And("^I crate page object from file (.*)$")
    public void createPageObject(String deployablePath) throws Throwable {

        String transformedXMLfile = testHarness.config.get("temp.dir")+"/output.xml";
        String pageObjectsJSONfile = testHarness.config.get("temp.dir")+"/pageObject.json";

        String filepathRegex = "(?<=file:\\/\\/\\/)(.*)(?=',)";
        String dataFile = testHarness.config.get("temp.dir")+"/data.xml";
        String dataFileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><pages></pages>";

        new ArchiversOperations().unzip(deployablePath, testHarness.config.get("temp.dir")+"/deployables");

        new TextFileOperations().replaceStringInFile(getClass().getResource("/XSLT/transformer.xslt").getFile(), filepathRegex, testHarness.config.get("temp.dir")+"/deployables/bundles");

        new FileOperationsSteps(testHarness).createFile(dataFile,dataFileContent);

        new XMLOperations().XSLtransform(transformedXMLfile,dataFile,getClass().getResource("/XSLT/transformer.xslt").getFile());

        new FSOperations().delete(testHarness.config.get("temp.dir")+"/deployables");

        new XMLOperations().convertXMLToJSON(transformedXMLfile,pageObjectsJSONfile);
    }

    @Given("^Initial setup$")
    public void initialSetup() throws Throwable {
        createPageObject(testHarness.config.get("deployable.file"));
    }

    @And("^I go to login page?$")
    public void goToLogin() throws Throwable {
        if (testHarness.config.get("portal.login").equals("true")) {
            PortalLoginScreen portalScreen = new PortalLoginScreen(testHarness, webHarness);
            portalScreen.goToURL();
        } else {
            LoginScreen screen = new LoginScreen(testHarness, webHarness);
            screen.goToURL();
        }
    }

    @And("^I login with username (.*) and password (.*)$")
    public void login(String username, String password) throws Throwable {

        if (testHarness.config.get("portal.login").equals("true")) {
            PortalLoginScreen portalScreen = new PortalLoginScreen(testHarness, webHarness);
            portalScreen.type(portalScreen.usernameInput, username);
            portalScreen.type(portalScreen.passwordInput, password);
            portalScreen.loginButton.click();
            PortalHomeScreen ps = new PortalHomeScreen(testHarness, webHarness);

        } else {
            LoginScreen screen = new LoginScreen(testHarness, webHarness);
            screen.waitForElement(screen.loginBtn);
            screen.type(screen.usernameText, username);
            screen.type(screen.passwordText, password);
            screen.loginBtn.click();

            HomeScreen homeScreen = new HomeScreen(testHarness, webHarness);
        }

    }

    @And("^I logout from the solution$")
    public void solutionLogout() throws Throwable {
        HomeScreen home = new HomeScreen(testHarness, webHarness);
        home.selectMenu("System", "Logout");
    }
}
