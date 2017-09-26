package com.experian.automation.steps;

import com.experian.automation.WebClient;
import com.experian.automation.logger.Logger;
import com.experian.automation.screens.AdminPortal.PortalHomeScreen;
import com.experian.automation.screens.AdminPortal.PortalLoginScreen;
import com.experian.automation.screens.HomeScreen;
import com.experian.automation.screens.LoginScreen;
import cucumber.api.java.en.And;

/**
 * Created by B04342A on 6/24/2017.
 */
public class CommonSteps {

    private final WebClient webClient;
    private final Logger logger = Logger.getLogger(this.getClass());

    public CommonSteps(WebClient webClient) {
        this.webClient = webClient;
    }

    @And("^I go to login page?$")
    public void goToLogin() throws Throwable {
        if (webClient.config.get("portal.login").equals("true")) {
            PortalLoginScreen portalScreen = new PortalLoginScreen(webClient);
            portalScreen.goToURL();
        } else {
            LoginScreen screen = new LoginScreen(webClient);
            screen.goToURL();
        }
    }

    @And("^I login with username (.*) and password (.*)$")
    public void login(String username, String password) throws Throwable {

        if (webClient.config.get("portal.login").equals("true")) {
            PortalLoginScreen portalScreen = new PortalLoginScreen(webClient);
            portalScreen.type(portalScreen.usernameInput, username);
            portalScreen.type(portalScreen.passwordInput, password);
            portalScreen.loginButton.click();
            PortalHomeScreen ps = new PortalHomeScreen(webClient);

        } else {
            LoginScreen screen = new LoginScreen(webClient);
            screen.waitForElement(screen.loginBtn);
            screen.type(screen.usernameText, username);
            screen.type(screen.passwordText, password);
            screen.loginBtn.click();

            HomeScreen homeScreen = new HomeScreen(webClient);
        }

    }

    @And("^I logout from the solution$")
    public void solutionLogout() throws Throwable {
        HomeScreen home = new HomeScreen(webClient);
        home.selectMenu("System", "Logout");
    }
}
