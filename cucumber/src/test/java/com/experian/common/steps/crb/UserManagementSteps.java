package com.experian.common.steps.crb;

import com.experian.common.WebClient;
import com.experian.common.core.logger.Logger;
import com.experian.common.screens.HomeScreen;
import com.experian.common.screens.crb.TacticalParametersMaintananceScreen;
import com.experian.common.screens.crb.UserManagementScreen;
import cucumber.api.java.en.And;

import java.util.Map;

public class UserManagementSteps {

    private final Logger logger = Logger.getLogger(this.getClass());
    private final WebClient webClient;

    public UserManagementSteps(WebClient webClient) {
        this.webClient = webClient;
    }

    @And("^I set all permissions for security profile - Administrator$")
    public void setAllPermissions() throws Throwable {
        HomeScreen home = new HomeScreen(webClient);
        home.selectMenu("System", "User Administration");
        UserManagementScreen umScreen = new UserManagementScreen(webClient);
        umScreen.securityProfilesButton.click();
        umScreen.waitForElement(umScreen.administratorProfile);
        umScreen.administratorProfile.click();
        umScreen.waitForElement(umScreen.profilePermissionsEditButton);
        umScreen.profilePermissionsEditButton.click();
        umScreen.waitForElements(umScreen.businessRulesTableRowCells);
        umScreen.allowAllButton.click();
        umScreen.okButton.click();
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
        HomeScreen home = new HomeScreen(webClient);
        home.selectMenu("System", "User Administration");
        UserManagementScreen umScreen = new UserManagementScreen(webClient);
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
