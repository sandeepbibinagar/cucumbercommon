package com.experian.automation.saas.steps.WebEngine;

import com.experian.automation.harnesses.TestHarness;
import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.logger.Logger;
import com.experian.automation.saas.screens.HomeScreen;
import com.experian.automation.saas.screens.WebEngine.UserManagementScreen;
import com.experian.automation.saas.screens.WebEngine.WebEngineHome;
import cucumber.api.java.en.And;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserManagementSteps {

    private final Logger logger = Logger.getLogger(this.getClass());
    private final TestHarness testHarness;
    private final WebHarness webHarness;

    public UserManagementSteps(TestHarness testHarness, WebHarness webHarness) {
        this.testHarness = testHarness;
        this.webHarness = webHarness;
    }

    @And("^I select tab item (.*\\/.*)$")
    public void selectTabItem(String tabItem){
        UserManagementScreen umScreen = new UserManagementScreen(testHarness, webHarness);
        Pattern pattern = Pattern.compile("[^\\/]+");
        Matcher matcher = pattern.matcher(tabItem);
        int position = 0;
        while (matcher.find()) {
            if (position != 0) {
                umScreen.selectTabItem(matcher.group().trim());
            } else {
                umScreen.selectTab(matcher.group().trim());
            }
            position++;
        }

    }
    /*
       And I allow all business process rules.
    */
    @And("^I allow all business process rules.$")
    public void setAllPermissions() throws Throwable {
        UserManagementScreen umScreen = new UserManagementScreen(testHarness, webHarness);
        umScreen.waitForElements(umScreen.businessRulesTableRowCells);
        umScreen.clickWithScrollToView(umScreen.editButton);
        if(umScreen.listOfDeniedPermissions.size()!=0){
            umScreen.allowAllButton.click();
            umScreen.clickWithScrollToView(umScreen.okButton);
        }
    }

    /*
       And I set business process rules by feature:
      | Create | Screen New Application    |
      | Create | New Application           |
      | All    | Screen Update Application |
    */
    @And("^I set business process rules by feature:$")
    public void setPermissionsForProfile(List<List<String>> dataTable) throws Throwable {
        UserManagementScreen umScreen = new UserManagementScreen(testHarness, webHarness);
        umScreen.waitForElements(umScreen.businessRulesTableRowCells);
        umScreen.clickWithScrollToView(umScreen.editButton);
        for (List<String> entry : dataTable) {
           umScreen.setProcessRules(entry.get(0), entry.get(1));
        }
        umScreen.clickWithScrollToView(umScreen.okButton);
    }
}
