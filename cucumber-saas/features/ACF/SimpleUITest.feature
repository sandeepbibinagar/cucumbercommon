Feature: Simple UI POC

  Scenario: ACF form test
    Given Initial setup
    And I start the browser
    And I go to login page
    And I login with username adm@example.com and password Password123
    And I select solution - PowerCurve Originations
#    And I select solution - Options
#    And I import tactical parameters from files:
#      | ExpCons_TP - TP - ExpCons_TP Search               | ACF_Tactical_Parameters.xml |
#      | Bureau_Enabler_TP - TP - Bureau Enabler           | ACF_Tactical_Parameters.xml |
#      | MiddleName_Mandatory - TP - Middle Name Mandatory | ACF_Tactical_Parameters.xml |
#      | WorkflowTP - TP - Workflow Search                 | ACF_Tactical_Parameters.xml |
#      | EquCons_TP - TP - EquCons_TP Search               | ACF_Tactical_Parameters.xml |
    And I select menu Capture Data on page Home Page
    And I enter values for fields on page P - New Application Screen
      | First Name            | Petar         |
      | Last Name             | Dobrev        |
      | SSN                   | 123123123     |
      | Date of Birth         | 1970-01-16    |
      | Requested Amount      | 100           |
      | Requested Term        | 100           |
      | Address Line 1        | Test Str. 20  |
      | City                  | New York      |
      | State                 | NY            |
      | Zip                   | 123123        |
      | Email                 | test@test.com |
      | Phone (Mobile or Fix) | 2000          |
    And I click on button with text next on page P - New Application Screen
    And I verify values for fields on page P - Summary and Results Screen:
      | System Decision | Decline |


