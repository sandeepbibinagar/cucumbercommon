@skip @J003
Feature: J003 Search and verify Basic Application Details and Audit Trail Details
  As a Client User I want to SEARCH an application through WEB PAGE
  so that I can review status of an application

  Scenario: I want to review the Personal Details of an Applicant
    Given Initial setup
    And I start the browser
    And I go to login page
    And I login with username admin and password Secret123
    And I select sub-menu item Pending in section Pending Applications in menu Apply on page homeStartPage
    And I enter enter values for textfields on page Query All Search Page
      | Surname  | McIver |
      | Forename | Rita   |
    And I enter value BK000000002210 for field Application Number on page Query All Search Page
    And I click on button with text Search on page Query All Search Page
    And I select element with title Open in column Action ,where column Application Number contains value BK000000002210 in table with id datagrid
    And I verify input-label pairs on page Basic Application Details Page:
      | Surname             | McIver             |
      | Forename            | Rita               |
      | Home Phone Number   | (+45) 787 567 8999 |
      | Email Address       | Rita@gmail.com     |
      | Total Annual Income | 72,500.00          |
    And I verify select-label pairs on page Basic Application Details Page:
      | Title              | Mrs            |
      | Existing Customer? | Yes            |
      | Marital Status     | Married        |
      | Residential Status | Owner occupier |
      | Employment Status  | Full time      |
    And I stop the browser

  Scenario: I want to review the Personal Details of an Applicant
    And I start the browser
    And I go to login page
    And I login with username admin and password Secret123
    And I select sub-menu item Pending in section Pending Applications in menu Apply on page homeStartPage
    And I enter enter values for textfields on page Query All Search Page
      | Surname  | McIver |
      | Forename | Rita   |
    And I enter value BK000000002210 for field Application Number on page Query All Search Page
    And I search for an application
    And I select element with title Audit Trail in column Action ,where column Application Number contains value BK000000002210 in table with id datagrid
    And I switch to page with title: Audit Trail Page
    And I verify data in table with class audit-trail-table:
      | Date & Time        | Duration | User ID | Channel | Service ID | BPF Name        | Status  | Worklist |
      | 10/10/2017 1:42 PM | 00:01:29 | admin   | Web     | 1          | New Application | Pending | Pending  |
      | 10/10/2017 1:42 PM | 00:01:16 | admin   | Web     | 1          | New Application | Pending | Pending  |
    And I stop the browser


