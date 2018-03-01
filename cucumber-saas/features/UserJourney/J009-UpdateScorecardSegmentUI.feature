Feature: User Journey - Update Scorecard TP via UI

  Background:
    # {% include 'classpath:../UserJourney/TacticalParametersFileUpload.background.feature' %}

  Scenario: Update Age gt 36 Score of Scorecard Generic UL TP via UI to get system decision Accept
   # Test-ID: 4039617
   # Use-Case: UserJourney
   # Priority: P1 - Critical
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

#   Populating Tactical Parameters
    And I update parameter Scorecard_Generic_UL_TP - TP_Scorecard_Generic_UL Search description: Scorecard Generic UL ,effective from: 12/01/2017 to
      | Input | Age lt 20 Score | Age gt 21 lt 25 Score | Age gt 26 lt 30 Score | Age gt 31 lt 35 Score | Age gt 36 Score | Initial Score | Transformation Score |
      |       | -115            | -20                   | 0                     | 30                    | 100             | 600           | 1                    |

    And I deploy tactical parameter Scorecard_Generic_UL_TP - TP_Scorecard_Generic_UL Search version LATEST

#    Filling the new application form in the ACF Originations UI
    And I start the browser
    And I go to login page
    And I login on Admin Portal with username adm@example.com and password Password123
    And I select solution - PowerCurve Originations
    And I select menu Capture Data on page Home Page
    And I enter values for fields on page P - New Application Screen
      | Suffix                              | Mrs                |
      | First Name                          | GLORIA             |
      | Last Name                           | LEBRATO            |
      | SSN                                 | 666010020          |
      | Date of Birth                       | 1980-10-20         |
      #| Is this a single or joint application? | Single        |
      | Requested Amount                    | 10000              |
      | Requested Term                      | 12                 |
      | Payment Protection                  | N                  |
      | Bank Current Account Holder?        | N                  |
      | Number of years with same bank      | 11                 |
      | Online Banking Account Holder?      | N                  |
      | Bank Account Number                 | 123                |
      | Cheque or Debit Card Held           | N                  |
      | Credit Card Number                  | 123                |
      | Membership Number                   | 123                |
      | Address Line 1                      | 1148 SMITH ST      |
      | House Number                        | 12                 |
      | City                                | MAXWELL AFB        |
      | State                               | AL                 |
      | Zip                                 | 361131510          |
      | Number of years at same address?    | 11                 |
      | Residential Status                  | Owner              |
      | Email                               | a@b.com            |
      | Phone (Mobile or Fix)               | 9725551212         |
      | Marital status                      | Single             |
      | Purpose of Loan                     | Debt Consolidation |
      | Gross monthly income                | 1000               |
      | Other Monthly Expenses              | 0                  |
      | Employer Name                       | EXPERIAN           |
      | Number of years with same employer? | 11                 |
      | Privacy Agreement                   | N                  |

    And I click on button with text next on page P - New Application Screen
    And I verify values for fields on page P - Summary and Results Screen:
      | System Score    | 734    |
      | System Decision | Accept |
    And I stop the browsers