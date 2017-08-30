package com.experian.common.steps;

import com.experian.common.WebClient;
import com.experian.common.core.logger.Logger;
import com.experian.common.screens.AdminPortal.PortalHomeScreen;
import com.experian.common.screens.AdminPortal.PortalLoginScreen;
import com.experian.common.screens.HomeScreen;
import com.experian.common.screens.LoginScreen;
import com.experian.common.steps.AdminPortal.PortalSolutionSelectionSteps;
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

}
