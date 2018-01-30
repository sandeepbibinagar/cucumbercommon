package com.experian.automation.saas.steps.WebEngine;

import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.logger.Logger;
import com.experian.automation.saas.screens.WebEngine.WebEngineHome;
import cucumber.api.java.en.And;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MenuSteps {

  private final Logger logger = Logger.getLogger(this.getClass());
  private final WebHarness webHarness;

  public MenuSteps(WebHarness webHarness) {
    this.webHarness = webHarness;
  }

  @And("^I select menu (.*\\/.*) on WebEngine home page$")
  public void selectMenu(String menu) throws Throwable {
    WebEngineHome homePage = new WebEngineHome(webHarness);

    Pattern pattern = Pattern.compile("[^\\/]+");
    Matcher matcher = pattern.matcher(menu);
    int position = 0;
    while (matcher.find()) {
      if (position != 0) {
        homePage.selectSubMenu(matcher.group().trim());
      } else {
        homePage.selectMainMenu(matcher.group().trim());
      }
      position++;
    }
  }

}
