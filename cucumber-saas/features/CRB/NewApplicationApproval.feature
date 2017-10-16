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
    And I select menu item Apply and sub-menu item New Application on page homeStartPage
    And I enter enter values for dropdowns on page Basic Application Details Page
      | Title                    | Mrs            |
      | Existing Customer?       | Yes            |
      | Marital Status           | Married        |
      | Residential Status       | Owner occupier |
      | Employment Status        | Full time      |
      | Preferred Contact Method | Email          |
    And I enter enter values for textfields on page Basic Application Details Page
      | Surname             | McIver         |
      | Forename            | Rita           |
      | Home Phone Number   | 457875678999   |
      | Customer Number     | 1008877963     |
      | Email Address       | Rita@gmail.com |
      | Total Annual Income | 72500          |
    And I enter value 1986-02-20 for datepicker Date Of Birth on page Basic Application Details Page
    And I select tab with text Product Details on page Basic Application Details Page
    And I enter enter values for dropdowns on page Basic Application Details Page
      | Master Product | Fixed Term Loan (Unsecured) |
    And I click on button with text Next on page Basic Application Details Page
    And I click on button with text Next on page Search Applicants on previous Applications
    And I select value Driving Licence from select with ID DocumentTypeDDL
    And I enter value 2019-06-06 for datepicker Expiry Date on page Applicant and Address Details Page
    And I enter value A455 for field ID Number on page Applicant and Address Details Page
    And I click on button with text Next on page Applicant and Address Details Page
    And I enter enter values for textfields on page Applicant and Address Details Page
      | Number / Name | 1             |
      | Street        | Mill Crescent |
      | Postcode      | WC10 4MX      |
      | City          | London        |
    And I enter enter values for dropdowns on page Applicant and Address Details Page
      | Country | United Kingdom |
    And I fill consecutive inputs with label Time at Address on page Applicant and Address Details Page:
      | 10 |
      | 5  |
    #    Defect in the solution - Next button has to be clicked twice to switch page
    And I click on button with text Next on page Applicant and Address Details Page
    And I click on button with text Next on page Applicant and Address Details Page
    And I click on button with text Next on page Existing Customer Matches
    And I click on button with text Next on page Applicant Summary Page
    And I click on button with text Next on page Loan - Product Choice Confirmation
    And I enter enter values for textfields on page Loan - Details Page
      | Requested Loan Amount | 12000 |
      | Requested Loan Term   | 36    |
    And I select value Home Improvements from dropdown field Purpose of Loan on page Loan - Details Page
    And I click on button with text Next on page Loan - Details Page
    And I click on button with value yes
    And I click on button with text Edit on page Applicant Selection for Financial & Employment details Page
    And I select tab with text Employment Details on page Applicant Employment and Affordability Page
    And I enter enter values for dropdowns on page Applicant Employment and Affordability Page
      | Occupation | Senior level professional/admin |
      | Country    | United Kingdom                  |
    And I enter enter values for textfields on page Applicant Employment and Affordability Page
      | Employer   | IT technology Ltd |
      | Premise    | 8                 |
      | Street     | Oxford Street     |
      | City       | London            |
      | Post Code  | WC8 7XX           |
      | Work Phone | 87978999          |
    And I select tab with text Financial Details on page Applicant Employment and Affordability Page
    And I enter enter values for textfields on page Applicant Employment and Affordability Page
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