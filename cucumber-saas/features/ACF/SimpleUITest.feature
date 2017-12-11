Feature: Simple UI POC

  Scenario: ACF solution simple form test
    # Use-Case: ACF
    Given Initial setup

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

    And I start the browser
    And I go to login page
    And I login on Admin Portal with username adm@example.com and password Password123
    And I select solution - PowerCurve Originations
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

