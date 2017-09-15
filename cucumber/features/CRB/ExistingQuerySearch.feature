@J004
Feature: J004 Search and verify Applicant Summary, Product Summary and Application Decisions
  As an underwriter (Back-Office User), I want to SEARCH AND UPDATE an application through WEB PAGE
  so that I can review an application and provide the update of decision to the server

  Scenario: I want to review Applicant Summary, Product Summary and Application Decisions
    Given I start the browser
      And I go to login page
      And I login with username user and password User123@123
      And I start a general enquiry

    When I enter search details:
      |Forename         |Rita                 |
      |Surname          |McIver               |
     And I search for an application
     And I open Application Overview with Application Number BK000000051371

    Then I should see Applicant Summary section with details:
      |No.  |Type            |Forename  |Surname  |Date of Birth  |
      |1    |Main Applicant  |Rita      |McIver   |02/20/1986     |
     And I should see Product Summary section with details:
      |Credit Card Product      |Box    |
      |Loan Product             |Check  |
      |Mortgage Product         |Cross  |
      |Current Account Product  |Cross  |
      |Savings Account Product  |Cross  |
      |Offer Bundle             |Check  |
     And I should see Application Decisions section with details:
      |System Decision  |Accept     |
      |Date             |2017-07-24 |
      |Time             |07:07:41   |
     And I stop the browser