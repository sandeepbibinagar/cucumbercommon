@test
Feature: New Application Approval
  In order to obtain a decision for an applicant
  As a CRB user
  I want to create an application through web page

  Scenario: ID_1234 Applicant gets approved for Fixed Term Loan (Unsecured)
    Given I start the browser
    And I go to login page
    And I login with username user and password User123@123
#    And I login with username adm@example.com and password Password123
#    And I select solution - PowerCurve Originations
#    And I check user permissions and parameters for solution - PowerCurve Originations
    And I start a new application
    And I enter the new applicant personal details:
      | Title                    | Mrs            |
      | Surname                  | McIver         |
      | Forename                 | Rita           |
      | Date Of Birth            | 1986 02 20     |
      | Existing Customer        | Yes            |
      | Existing Customer Number | 1008877963     |
      | Marital Status           | Married        |
      | Home Phone Number        | 457875678999   |
      | Email                    | Rita@gmail.com |
      | Residential Status       | Owner occupier |
      | Employment Status        | Full time      |
      | Total Annual Income      | 72500          |
    And I navigate to "Product Details" tab
    And I select a "Fixed Term Loan (Unsecured)" product
    And I proceed to "Previous Applicant Search" page
    And I proceed to "Applicant Details" page
    And I enter identification information:
      | Identification Type | Driving Licence |
      | ID Number           | A455            |
      | Expiry Date         | 2019 06 06      |
    And I navigate to "Address Details" tab
    And I enter applicant's address details:
      | Number                 | 1              |
      | Street                 | Mill Crescent  |
      | City                   | London         |
      | Postcode               | WC10 4MX       |
      | Country                | United Kingdom |
      | Time at Address Years  | 10             |
      | Time at Address Months | 5              |

    And I proceed to "Applicant Summary" page
    And I proceed to "Product Choice Confirmation" page
    And I choose loan type "UL Classic"
    And I proceed to "Loan Application" page
    And I enter requested loan details:
      | Requested Loan Amount | 12000             |
      | Requested Loan Term   | 36                |
      | Purpose of Loan       | Home Improvements |

    And I proceed to "Financial & Employment Details" page
    And I proceed to Main Applicant Details edit

    And I navigate to "Employment Details" tab
    And I enter employment details:
      | Occupation | Senior level professional/admin |
      | Employer   | IT technology Ltd               |
      | Premise    | 8                               |
      | Street     | Oxford Street                   |
      | City       | London                          |
      | Post Code  | WC8 7XX                         |
      | Country    | United Kingdom                  |
      | Start Date | 2007 01 01                      |
      | Work Phone | 87978999                        |

    And I navigate to "Financial Details" tab
    And I enter financial details:
      | Monthly Income After Deductions             | 3700     |
      | Monthly Credit Card and Store Card Payments | 150      |
      | Bank Name                                   | Cashbank |
      | Time with Bank Years                        | 3        |
      | Time with Bank Months                       | 2        |

    And I proceed to "Financial & Employment Details" page

    When I submit the application
    Then I should see that application has been accepted
    And I stop the browser
