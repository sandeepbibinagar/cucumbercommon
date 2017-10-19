@skip
Feature: New Application Approval
  In order to obtain a decision for an applicant
  As a CRB user
  I want to create an application through web page

  Scenario: ID_1234 Applicant gets approved for Fixed Term Loan (Unsecured)
    Given Initial setup
    And I start the browser
    And I go to login page
    And I login with username admin and password Secret123
#    And I login with username adm@example.com and password Password123
#    And I select solution - PowerCurve Originations
#    And I check user permissions and parameters for solution - PowerCurve Originations
    And I select menu Apply/New Application on page homeStartPage
    And I enter values for fields on page Basic Application Details Page
      | Title                    | Mrs            |
      | Existing Customer?       | Yes            |
      | Marital Status           | Married        |
      | Residential Status       | Owner occupier |
      | Employment Status        | Full time      |
      | Preferred Contact Method | Email          |
      | Surname                  | McIver         |
      | Forename                 | Rita           |
      | Home Phone Number        | 457875678999   |
      | Customer Number          | 1008877963     |
      | Email Address            | Rita@gmail.com |
      | Total Annual Income      | 72500          |
      | Date Of Birth            | 1986-02-20     |
    And I select tab with text Product Details on page Basic Application Details Page
    And I enter values for fields on page Basic Application Details Page
      | Master Product | Fixed Term Loan (Unsecured) |
    And I click on button with text Next on page Basic Application Details Page
    And I click on button with text Next on page Search Applicants on previous Applications
    And I enter value Driving Licence for dropdown with id DocumentTypeDDL
    And I enter value A455 for textfield with id DocumentIdNumberTxt
    And I enter values for fields on page Applicant and Address Details Page
      | Expiry Date         | 2019-06-06      |
    And I click on button with text Next on page Applicant and Address Details Page
    And I enter values for fields on page Applicant and Address Details Page
      | Number / Name | 1             |
      | Street        | Mill Crescent |
      | Postcode      | WC10 4MX      |
    And I enter value London for textfield with id currAddressLine5Txt
    And I enter value United Kingdom for dropdown with id currAddressCountryDDL
    And I fill consecutive inputs with label Time at Address on page Applicant and Address Details Page:
      | 10 |
      | 5  |
    #    Defect in the solution - Next button has to be clicked twice to switch page
    And I click on button with text Next on page Applicant and Address Details Page
    And I click on button with text Next on page Applicant and Address Details Page
    And I click on button with text Next on page Existing Customer Matches
    And I click on button with text Next on page Applicant Summary Page
    And I click on button with text Next on page Loan - Product Choice Confirmation
    And I enter values for fields on page Loan - Details Page
      | Requested Loan Amount | 12000             |
      | Requested Loan Term   | 36                |
      | Purpose of Loan       | Home Improvements |
    And I click on button with text Next on page Loan - Details Page
    And I click on button with value yes
    And I click on button with text Edit on page Applicant Selection for Financial & Employment details Page
    And I select tab with text Employment Details on page Applicant Employment and Affordability Page
    And I enter values for fields on page Applicant Employment and Affordability Page
      | Occupation | Senior level professional/admin |
      | Employer   | IT technology Ltd               |
      | Premise    | 8                               |
      | Post Code  | WC8 7XX                         |
      | Work Phone | 87978999                        |
    And I enter value United Kingdom for dropdown with id CountryDDL
    And I enter value Oxford Street for textfield with id workAddressLine2Txt
    And I enter value London for textfield with id workAddressLine5Txt
    And I select tab with text Financial Details on page Applicant Employment and Affordability Page
    And I enter values for fields on page Applicant Employment and Affordability Page
      | Monthly Income After Deductions             | 3700     |
      | Monthly Credit Card and Store Card Payments | 150      |
      | Bank Name                                   | Cashbank |
    And I fill consecutive inputs with label Time with Bank on page Applicant Employment and Affordability Page:
      | 10 |
      | 5  |
    And I click on button with text Next on page Applicant Employment and Affordability Page
    And I click on button with text Next on page Applicant Selection for Financial & Employment details Page
    And I click on button with text Continue on page Decision Page
    And I stop the browser