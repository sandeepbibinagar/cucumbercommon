@skip @J004
Feature: J004 Search and verify Applicant Summary, Product Summary and Application Decisions
  As an underwriter (Back-Office User), I want to SEARCH AND UPDATE an application through WEB PAGE
  so that I can review an application and provide the update of decision to the server

  Scenario: I want to review Applicant Summary, Product Summary and Application Decisions
    Given Initial setup
    And I start the browser
    And I go to login page
    And I login with username admin and password Secret123
    And I select sub-menu item All in section General Enquiry in menu Query on page homeStartPage
    And I enter value Rita for field Forename on page Query All Search Page
    And I enter value McIver for field Surname on page Query All Search Page
    And I enter value BK000000000061 for field Application Number on page Query All Search Page
    And I click on button with text Search on page Query All Search Page
    And I select element with title Open in column Action ,where column Application Number contains value BK000000000061 in table with id datagrid
    And I verify data in table with id ApplicantSummaryDGRD:
      | No. | Type           | Forename | Surname | Date of Birth |
      | 1   | Main Applicant | Rita     | McIver  | 02/20/1986    |
    And I verify pairs in section with label Product Summary:
      | Credit Card Product | Accept |
      | Loan Product        | Accept |
    And I verify input-label pairs on page Application Overview:
      | System Decision | Accept   |
      | Date            | 2/1/2017 |
      | Time            | 11:19:03 |
    And I stop the browser








