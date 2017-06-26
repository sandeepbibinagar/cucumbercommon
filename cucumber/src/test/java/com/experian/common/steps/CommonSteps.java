package com.experian.common.steps;

import com.experian.common.WebClient;
import com.experian.common.core.logger.Logger;
import com.experian.common.screens.HomeScreen;
import com.experian.common.screens.LoginScreen;
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
        LoginScreen screen = new LoginScreen(webClient);
        screen.goToURL();
    }

    @And("^I login with username (.*) and password (.*)$")
    public void login(String username, String password) throws Throwable {
        LoginScreen screen = new LoginScreen(webClient);

        screen.waitForElement(screen.loginBtn);
        screen.type(screen.usernameText, username);
        screen.type(screen.passwordText, password);
        screen.loginBtn.click();

        HomeScreen homeScreen = new HomeScreen(webClient);
        //homeScreen.waitForScreen(homeScreen.linkExperian);
    }

}
