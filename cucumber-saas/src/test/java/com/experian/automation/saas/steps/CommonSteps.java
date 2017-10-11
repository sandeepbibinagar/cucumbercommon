package com.experian.automation.saas.steps;

import com.experian.automation.converters.XMLtoJSONConverter;
import com.experian.automation.harnesses.TestHarness;
import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.harnesses.TestHarness;
import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.helpers.ArchiversOperations;
import com.experian.automation.helpers.FSOperations;
import com.experian.automation.helpers.FileManipulationOperations;
import com.experian.automation.logger.Logger;
import com.experian.automation.saas.screens.AdminPortal.PortalHomeScreen;
import com.experian.automation.saas.screens.LoginScreen;
import com.experian.automation.saas.screens.AdminPortal.PortalLoginScreen;
import com.experian.automation.saas.screens.HomeScreen;
import com.experian.automation.transformers.XSLTransformer;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import org.apache.commons.io.FilenameUtils;

import java.io.File;

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

    @Given("^Initial setup$")
    public void initialSetup() throws Throwable {
        String filepathRegex = "(?<=file:\\/\\/\\/)(.*)(?=',)";

        new FSOperations().deleteAllFilesExceptOne(testHarness.config.get("deployable.file.path")
                ,testHarness.config.get("deployable.file.wra"));


        new FSOperations().deleteAllFilesAndDirectories(testHarness.config.get("transformation.files.path"));


        new FSOperations().renameFile(testHarness.config.get("deployable.file.path"),
                testHarness.config.get("deployable.file.wra"),
                testHarness.config.get("deployable.file.zip"));


        new ArchiversOperations().unzip(testHarness.config.get("deployable.file.path"),
                testHarness.config.get("deployable.file.zip"),
                testHarness.config.get("deployable.file.path"));

        new FileManipulationOperations().replaceStringInFile(
                getClass().getResource("/XSLT/transformer.xslt").getFile(), filepathRegex
                , testHarness.config.get("delpoyable.file.bundles"));

        new XSLTransformer().transform(
                testHarness.config.get("transformation.file.xml"),
                getClass().getResource("/XSLT/data.xml").getFile(),
                getClass().getResource("/XSLT/transformer.xslt").getFile());

        new XMLtoJSONConverter().convert(testHarness.config.get("transformation.file.xml"), testHarness.config.get("transformation.file.json"));

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
