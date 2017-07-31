@J003
Feature: J003 Search and verify Basic Application Details and Audit Trail Details
  As a Client User I want to SEARCH an application through WEB PAGE
  so that I can review status of an application

  Background:
    Given I start the browser
      And I go to login page
      And I login with username user and password User123@123
      And I start a pending application

  Scenario: I want to review the Personal Details of an Applicant
    When I enter search details:
      |Forename         |Rita                 |
      |Surname          |McIver               |
    And I search for an application
    And I open Basic Application Details with Application Number BK000000051371
    Then I should see Personal Details tab with details:
      |Title                     |Mrs                  |
      |Surname                   |McIver               |
      |Forename                  |Rita                 |
      |Middlename                |                     |
      |Date Of Birth             |1986-02-20           |
      |Existing Customer         |Yes                  |
      |Customer Number           |1008877963           |
      |Marital Status            |Married              |
      |Home Phone Number         |(+45) 787 567        |
      |Mobile Number             |                     |
      |Email Address             |Rita@gmail.com       |
      |Preferred Contact Method  |Choose One           |
      |Residential Status        |Owner occupier       |
      |Employment Status         |Full time            |
      |Total Annual Income       |72,500.00            |
    And I stop the browser

  Scenario: I want to review the Audit Trail for an Application
    When I enter search details:
      |Forename         |Rita                 |
      |Surname          |McIver               |
    And I search for an application
    And I open Audit Trail with Application Number BK000000051371
    Then I should see Audit Trail page with details:
      |Date & Time       |Duration|User ID|Channel|Service ID|BPF Name       |Status |Worklist|
      |07/24/2017 7:07 AM|00:00:49|user   |Web    |1         |New Application|Pending|Pending |
      |07/24/2017 7:07 AM|00:00:39|user   |Web    |1         |New Application|Pending|Pending |
      |07/24/2017 7:07 AM|00:00:23|user   |Web    |1         |New Application|Pending|Pending |
    And I stop the browser