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
    And I click on table cell link with text Open in column Action on row identified by cell text BK000000000061 in column Application Number on table with id datagrid
    And I verify data in table with id ApplicantSummaryDGRD:
      | No. | Type           | Forename | Surname | Date of Birth |
      | 1   | Main Applicant | Rita     | McIver  | 02/20/1986    |
    And I compare web image with label to local image:
      | Credit Card Product     | Bundled.png  |
      | Loan Product            | Accept.png   |
      | Offer Bundle            | Accept.png   |
      | Mortgage Product        | Declined.png |
      | Current Account Product | Declined.png |
      | Savings Account Product | Declined.png |
    And I verify values for fields on page Application Overview:
      | System Decision | Accept     |
      | Date            | 2017-02-01 |
      | Time            | 11:19:03   |
    And I stop the browser








