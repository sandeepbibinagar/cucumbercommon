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

import com.experian.automation.saas.screens.WebEngine.WebEngineHome;
import com.experian.automation.steps.FileOperationsSteps;
import com.mashape.unirest.http.exceptions.UnirestException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

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

  @And("^I download solution (.*)$")
  public void getSolutionDeployables(String solutionName) throws IOException, UnirestException {

        /* Have to check where EPP solutions deployables will be taken from */
    ProvisionAPISteps apiSteps = new ProvisionAPISteps(testHarness);
    apiSteps.getServiceProperty("WEB_ENGINE_SOLUTION_FILES", solutionName, "SOLUTION_FILES_PATH_VAR");

    File solutionFilesZip = new File(testHarness.config.get("temp.dir") + File.separator + solutionName + ".zip");
    FileUtils.copyURLToFile(new URL(testHarness.getStepData("SOLUTION_FILES_PATH_VAR")), solutionFilesZip);

    new ArchiversOperations().unzip(solutionFilesZip.getAbsolutePath(), testHarness.config.get("temp.dir"));

  }

  @And("^I create the solution page object model from file (.*)$")
  public void createPageObject(String deployablePath) throws Throwable {

    if (testHarness.config.get("portal.login").equals("true")) {
      getSolutionDeployables("powercurve-core");
    }

    File deployableFile = new File(deployablePath);
    String solutionPageObjectFile = getClass().getResource("/XSLT/solution-page-object.xslt").getFile();
    String tempDeployablesFolder = testHarness.config.get("temp.dir") + "/deployables";
    String dataFile = tempDeployablesFolder + "/data.xml";
    String transformedXMLfile = testHarness.config.get("temp.dir") + "/output.xml";
    String pageObjectsJSONfile =
        testHarness.config.get("temp.dir") + "page-object-" + FilenameUtils.removeExtension(deployableFile.getName())
            + ".json";

    String filepathRegex = "(?<=file:\\/\\/\\/)(.*)(?=',)";
    String dataFileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><pages></pages>";

    new ArchiversOperations().unzip(deployableFile.getAbsolutePath(), tempDeployablesFolder);

    new TextFileOperations().replaceStringInFile(solutionPageObjectFile, filepathRegex,
                                                 testHarness.config.get("temp.dir") + "/deployables/bundles");

    new FileOperationsSteps(testHarness).createFile(dataFile, dataFileContent);

    new XMLOperations().XSLtransform(transformedXMLfile, dataFile, solutionPageObjectFile);

    new FSOperations().delete(tempDeployablesFolder);

    new XMLOperations().convertXMLToJSON(transformedXMLfile, pageObjectsJSONfile);
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

  @And("^I go to WebEngine home page$")
  public void goToWebEngine() throws Throwable {
    WebEngineHome webEngine = new WebEngineHome(testHarness, webHarness);
    webEngine.waitForElements(webEngine.mainMenuItems);
  }


  @And("^I login on Admin Portal with username (.*) and password (.*)$")
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
