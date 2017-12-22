Feature: Simple UI POC

  Scenario: ACF solution simple form test
    # Use-Case: ACF

    Given Initial setup

#   Populating Tactical Parameters
    And I update parameter ExpCons_TP - TP - ExpCons_TP Search description: Bureau Test3 ,effective from: 03/01/2017 to 03/03/2018
      | Runtime Environment | DBHost | EAI      | ARF Version | Op Initials | Preamble | Sub Code | Vendor Number | NCUsername            | NCPassword    | Version |
      | PRD                 | STAR   | JYE85WXO | 07          | MM          | TEST     | 5991476  | C03           | edanetconnectconsumer | EDAConsumer07 |         |
      | PRD                 | STAR   | JYE85WXO | 07          | MM          | TEST     | 5991476  | C03           | edanetconnectconsumer | EDAConsumer07 | N       |
      | PRD                 | STAR   | JYE85WXO | 07          | MM          | TEST     | 5991476  | C03           | edanetconnectconsumer | EDAConsumer07 | Y       |

    And I deploy tactical parameter ExpCons_TP - TP - ExpCons_TP Search version LATEST

    And I update tactical parameters from file ${features.path}/ACF/data/ACF_Tactical_Parameters_Export.xml

#   And I deploy tactical parameter ExpCons_TP - TP - ExpCons_TP Search version LATEST
    And I deploy tactical parameter Product_TP - TP - Product_TP Search version LATEST
    And I deploy tactical parameter Bureau_Enabler_TP - TP - Bureau Enabler version LATEST
    And I deploy tactical parameter MiddleName_Mandatory - TP - Middle Name Mandatory version LATEST
    And I deploy tactical parameter WorkflowTP - TP - Workflow Search version LATEST
    And I deploy tactical parameter EquCons_TP - TP - EquCons_TP Search version LATEST

#    Giving User Permissions to access the Originations UI
    And I start the browser
    And I go to login page
    And I login on Admin Portal with username adm@example.com and password Password123
    And I go to WebEngine home page
    And I select menu System/User Administration on WebEngine home page
    And I select tab item Security Profiles/Administrator on Web Engine user administration panel
    And I set business process rules by feature:
      | Create | Screen New Application    |
      | Create | New Application           |
      | All    | Screen Update Application |
    And I select menu System/Logout on WebEngine home page
    And I stop the browser

#    Filling the new application form in the ACF Origionations UI
    And I start the browser
    And I go to login page
    And I login on Admin Portal with username adm@example.com and password Password123
    And I select solution - PowerCurve Originations

    And I verify that field Capture Data is located in the TOP-LEFT part of the Home Page screen

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


