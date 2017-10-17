package com.experian.automation.saas.steps.crb;

import com.experian.automation.harnesses.TestHarness;
import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.logger.Logger;
import com.experian.automation.saas.screens.HomeScreen;
import com.experian.automation.saas.screens.crb.UserManagementScreen;
import cucumber.api.java.en.And;

import java.util.Map;

public class UserManagementSteps {

    private final Logger logger = Logger.getLogger(this.getClass());
    private final TestHarness testHarness;
    private final WebHarness webHarness;

    public UserManagementSteps(TestHarness testHarness, WebHarness webHarness) {
        this.testHarness = testHarness;
        this.webHarness = webHarness;
    }

    @And("^I set all permissions for security profile - Administrator$")
    public void setAllPermissions() throws Throwable {
        HomeScreen home = new HomeScreen(testHarness, webHarness);
        home.selectMenu("System", "User Administration");
        UserManagementScreen umScreen = new UserManagementScreen(testHarness, webHarness);
        umScreen.waitForElement(umScreen.securityProfilesButton);
        umScreen.securityProfilesButton.click();
        umScreen.waitForElement(umScreen.administratorProfile);
        umScreen.administratorProfile.click();
        umScreen.waitForElement(umScreen.profilePermissionsEditButton);
        umScreen.profilePermissionsEditButton.click();
        umScreen.waitForElements(umScreen.businessRulesTableRowCells);
        if(umScreen.listOfDeniedPermissions.size()!=0){
            umScreen.allowAllButton.click();
            umScreen.okButton.click();
        }
            umScreen.closeWindow();
    }

/*
     And I set custom permissions for security profile - Administrator:
      | All          | Pending              |
      | Create       | Quotation            |
      | Accepted     | Override Decision    |
      | All          | Query                |
      | Bureau Error | Bureau Re-processing |
*/
    @And("^I set custom permissions for security profile - Administrator:$")
    public void setPermissionsForProfile(Map<String, String> permissionsMatrix) throws Throwable {
        HomeScreen home = new HomeScreen(testHarness, webHarness);
        home.selectMenu("System", "User Administration");
        UserManagementScreen umScreen = new UserManagementScreen(testHarness, webHarness);
        umScreen.securityProfilesButton.click();
        umScreen.waitForElement(umScreen.administratorProfile);
        umScreen.administratorProfile.click();
        umScreen.waitForElement(umScreen.profilePermissionsEditButton);
        umScreen.profilePermissionsEditButton.click();
        umScreen.waitForElements(umScreen.businessRulesTableRowCells);
        for (Map.Entry<String, String> entry : permissionsMatrix.entrySet()) {
            umScreen.selectBusinessProcessRules(entry.getKey(), entry.getValue());
        }
        umScreen.okButton.click();
    }
}
