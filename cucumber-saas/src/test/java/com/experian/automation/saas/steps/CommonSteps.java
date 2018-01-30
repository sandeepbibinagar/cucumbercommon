package com.experian.automation.saas.steps;

import static org.testng.Assert.assertTrue;
import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.helpers.ArchiversOperations;
import com.experian.automation.helpers.Config;
import com.experian.automation.helpers.FSOperations;
import com.experian.automation.helpers.TextFileOperations;
import com.experian.automation.helpers.Variables;
import com.experian.automation.helpers.XMLOperations;
import com.experian.automation.logger.Logger;
import com.experian.automation.saas.screens.AdminPortal.PortalHomeScreen;
import com.experian.automation.saas.screens.AdminPortal.PortalLoginScreen;
import com.experian.automation.saas.screens.HomeScreen;
import com.experian.automation.saas.screens.LoginScreen;
import com.experian.automation.saas.screens.WebEngine.WebEngineHome;
import com.experian.automation.steps.FileOperationsSteps;
import com.mashape.unirest.http.exceptions.UnirestException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 * Created by B04342A on 6/24/2017.
 */
public class CommonSteps {

  private final WebHarness webHarness;
  private final Logger logger = Logger.getLogger(this.getClass());

  public CommonSteps(WebHarness webHarness) {
    this.webHarness = webHarness;
  }

  @And("^I download solution (.*)$")
  public void getSolutionDeployables(String solutionName) throws IOException, UnirestException {

        /* Have to check where EPP solutions deployables will be taken from */
    ProvisionAPISteps apiSteps = new ProvisionAPISteps();
    apiSteps.getServiceProperty("WEB_ENGINE_SOLUTION_FILES", solutionName, "SOLUTION_FILES_PATH_VAR");

    File solutionFilesZip = new File(Config.get("temp.dir") + File.separator + solutionName + ".zip");

    FileUtils.copyURLToFile(new URL(Variables.get("SOLUTION_FILES_PATH_VAR")), solutionFilesZip);

    logger.info("Downloading: " + Variables.get("SOLUTION_FILES_PATH_VAR"));
    assertTrue(solutionFilesZip.exists(), "File " + solutionFilesZip + "is successfully created.");

    new ArchiversOperations().unzip(solutionFilesZip.getAbsolutePath(), Config.get("temp.dir"));

  }

  @And("^I create the solution page object model from file (.*)$")
  public void createPageObject(String deployablePath) throws Throwable {

    if (Config.get("portal.login").equals("true")) {
      getSolutionDeployables("powercurve-core");
    }

    File deployableFile = new File(deployablePath);
    String solutionPageObjectFile = getClass().getResource("/XSLT/solution-page-object.xslt").getFile();
    String tempDeployablesFolder = Config.get("temp.dir") + "/deployables";
    String dataFile = tempDeployablesFolder + "/data.xml";
    String transformedXMLfile = Config.get("temp.dir") + "/output.xml";
    String pageObjectsJSONfile =
        Config.get("temp.dir") + "page-object-" + FilenameUtils.removeExtension(deployableFile.getName())
            + ".json";

    String filepathRegex = "(?<=file:\\/\\/\\/)(.*)(?=',)";
    String dataFileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><pages></pages>";

    assertTrue(deployableFile.exists(), "File " + deployablePath + " exists");

    new ArchiversOperations().unzip(deployableFile.getAbsolutePath(), tempDeployablesFolder);

    new TextFileOperations().replaceStringInFile(solutionPageObjectFile, filepathRegex,
                                                 Config.get("temp.dir") + "/deployables/bundles");

    new FileOperationsSteps().createFile(dataFile, dataFileContent);

    new XMLOperations().XSLtransform(transformedXMLfile, dataFile, solutionPageObjectFile);

    new FSOperations().delete(tempDeployablesFolder);

    new XMLOperations().convertXMLToJSON(transformedXMLfile, pageObjectsJSONfile);
  }

  @Given("^Initial setup$")
  public void initialSetup() throws Throwable {
    createPageObject(Config.get("deployable.file"));
  }

  @And("^I go to login page?$")
  public void goToLogin() throws Throwable {
    if (Config.get("portal.login").equals("true")) {
      PortalLoginScreen portalScreen = new PortalLoginScreen(webHarness);
      portalScreen.goToURL();
    } else {
      LoginScreen screen = new LoginScreen(webHarness);
      screen.goToURL();
    }
  }

  @And("^I go to WebEngine home page$")
  public void goToWebEngine() throws Throwable {
    WebEngineHome webEngine = new WebEngineHome(webHarness);
    webEngine.waitForElements(webEngine.mainMenuItems);
  }


  @And("^I login on Admin Portal with username (.*) and password (.*)$")
  public void login(String username, String password) throws Throwable {

    if (Config.get("portal.login").equals("true")) {
      PortalLoginScreen portalScreen = new PortalLoginScreen(webHarness);
      portalScreen.type(portalScreen.usernameInput, username);
      portalScreen.type(portalScreen.passwordInput, password);
      portalScreen.loginButton.click();
      PortalHomeScreen ps = new PortalHomeScreen(webHarness);

    } else {
      LoginScreen screen = new LoginScreen(webHarness);
      screen.waitForElement(screen.loginBtn);
      screen.type(screen.usernameText, username);
      screen.type(screen.passwordText, password);
      screen.loginBtn.click();

      HomeScreen homeScreen = new HomeScreen(webHarness);
    }

  }

  @And("^I logout from the solution$")
  public void solutionLogout() throws Throwable {
    HomeScreen home = new HomeScreen(webHarness);
    home.selectMenu("System", "Logout");
  }
}
