@skip @J004
Feature: J004 Search and verify Applicant Summary, Product Summary and Application Decisions
  As an underwriter (Back-Office User), I want to SEARCH AND UPDATE an application through WEB PAGE
  so that I can review an application and provide the update of decision to cthe server

  Scenario: I want to review Applicant Summary, Product Summary and Application Decisions
    Given Initial setup
    And I start the browser
    And I go to login page
    And I login with username admin and password Secret123
    And I select item All in section General Enquiry in menu Query on page homeStartPage
    And I enter values for fields on page Query All Search Page
      | Forename           | Rita           |
      | Surname            | McIver         |
      | Application Number | BK000000000061 |
    And I click on button with text Search on page Query All Search Page
    And I select element with title Open in column Action ,where column Application Number contains value BK000000000061 in table with id datagrid
    And I verify data in table with id ApplicantSummaryDGRD:
      | No. | Type           | Forename | Surname | Date of Birth |
      | 1   | Main Applicant | Rita     | McIver  | 02/20/1986    |
    And I verify pairs in section with label Product Summary:
      | Credit Card Product | Accept |
      | Loan Product        | Accept |
    And I verify values for fields on page Application Overview:
      | System Decision | Accept     |
      | Date            | 2017-02-01 |
      | Time            | 11:19:03   |
    And I stop the browser








