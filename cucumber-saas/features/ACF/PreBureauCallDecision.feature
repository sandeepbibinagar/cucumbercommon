Feature: Pre Bureau Call Decision through the REST api
  In order to obtain a pre-bureau decision for an application,
  As an ACF user,
  I want to send a POST request with  information for one applicant and receive a valid response with pre-bureau decision for the application.

  Background:
    Given I update tactical parameters from file ${features.path}/ACF/data/tactical_parameters_PreB.xml
    And I deploy tactical parameter Pre-Bureau Decisioning_TP - TP - Pre-Bureau Decisioning version LATEST
    And I deploy tactical parameter Lending Area_TP - TP - Lending Area version LATEST
    And I deploy tactical parameter Unsecured Credit Card Product Definition_TP - TP - Unsecured Credit Card Product Definition version LATEST
    And I prepare JWT token with user stan.marsh@nabtest.example.com and password Password123 from service ${token.service.url}/v1/tokens/create

  Scenario: ACF-US Pre-Bureau DA call - APPROVE
    # Test-ID: 5015358
    # Type: Functional
    # Use-Case: ACF
    # Priority: P3 - Medium
    And I set the base webservice url to ${bps.webservices.url}
    And I prepare REST request body:
      """
        {
            "DV-Application.Channel" : "HVC",
            "DV-Application.ApplicationSource" : "Terry's HVAC",
            "DV-Application.InternalContactName" : "Lottle, Nancy",
            "DV-Applicant.APP[1].NAME[1].FirstName " : "KENNETH",
            "DV-Applicant.APP[1].NAME[1].MiddleName " : "B",
            "DV-Applicant.APP[1].NAME[1].LastName " : "CARISON",
            "DV-Applicant.APP[1].NAME[1].Suffix " : "MRS",
            "DV-Applicant.APP[1].DateofBirth " : "19610627",
            "DV-Applicant.APP[1].SSN " : "666559236",
            "DV-Product.PRODUCT[1].ProductType" : "UCC",
            "DV-Product.PRODUCT[1].ProductID" : "UNSECCC",
            "DV-Applicant.APP[1].CreditRole" : "Borrower",
            "DV-Applicant.APP[1].IndividualJoint" : "I",
            "DV-Applicant.APP[1].IDENT[1].IDType" : "DL",
            "DV-Applicant.APP[1].IDENT[1].IDNumber" : "12345678",
            "DV-Applicant.APP[1].IDENT[1].IDCountry" : "US",
            "DV-Applicant.APP[1].IDENT[1].IDState" : "AL",
            "DV-Applicant.APP[1].IDENT[1].IDIssueDate" : "20101010",
            "DV-Applicant.APP[1].IDENT[1].IDExpiration": "20201010",
            "DV-Applicant.APP[1].ADDRESS[1].AddressLine1 " : "6402 S.234",
            "DV-Applicant.APP[1].ADDRESS[1].AddressLine2 " : "Apt. 2012",
            "DV-Applicant.APP[1].ADDRESS[1].City " : "VIDOR",
            "DV-Applicant.APP[1].ADDRESS[1].State " : "AL",
            "DV-Applicant.APP[1].ADDRESS[1].ZipCode " : "776623108",
            "DV-Applicant.APP[1].ADDRESS[1].ResidentialStatus " : "Own",
            "DV-Applicant.APP[1].ADDRESS[1].MonthlyPayment" : 2000,
            "DV-Applicant.APP[1].ADDRESS[1].YearsAtAddress " : 3,
            "DV-Applicant.APP[1].PHONE[1].PhoneNumber" : "4097691742",
            "DV-Applicant.APP[1].PHONE[1].PhoneType" : "C",
            "DV-Applicant.APP[1].GrantPermissionToContactCell" : "Y",
            "DV-Applicant.APP[1].EmailAddress" : "acb@asd",
            "DV-Applicant.APP[1].GrossMthlyIncome" : 5890,
            "DV-Applicant.APP[1].EMPL[1].ContactName" : "Jacks Fish Cleaning",
            "DV-Applicant.APP[1].EMPL[1].EmployerAddressLine1" : "101 S. Main",
            "DV-Applicant.APP[1].EMPL[1].EmployerAddressLine2" : "Suite 101",
            "DV-Applicant.APP[1].EMPL[1].EmployerAddressCity" : "Aurora",
            "DV-Applicant.APP[1].EMPL[1].EmployerAddressState" : "Al",
            "DV-Applicant.APP[1].EMPL[1].YearsWithEmployer" : 3,
            "DV-Applicant.APP[1].AlimonyChildSupport" : 1200,
            "DV-Applicant.APP[1].OtherMthlyIncome" : 500,
            "DV-Applicant.APP[1].OtherIncomeSource" : "Stock dividend and interest",
            "DV-Product.PRODUCT[1].AmountRequested " : 10000,
            "DV-Product.PRODUCT[1].TermRequested " : 12
        }
      """
    And I add the following headers to the REST request:
      | Content-Type | application/json |
      | Accept       | application/json |
    When I send a REST POST request to /v0/applications/NewApp and receive status code HTTP 200
    Then I verify that the JSON response has fields:
      | $.data.['DV-Results.Result Calls.C1 Policy Rules-Decision Setter Results.Decision Category'] | APPROVE |

  Scenario: ACF-US Pre-Bureau DA call - DECLINE - Applicant's age lower than the minimum.
    # Test-ID: 5015360
    # Type: Functional
    # Use-Case: ACF
    # Priority: P3 - Medium
    And I set the base webservice url to ${bps.webservices.url}
    And I prepare REST request body:
      """
         {
            "DV-Application.Channel" : "Web",
            "DV-Application.ApplicationSource" : "Home Banking",
            "DV-Application.InternalContactName" : "Porter,Nancy",
            "DV-Applicant.APP[1].NAME[1].FirstName " : "EMILIONO",
            "DV-Applicant.APP[1].NAME[1].MiddleName " : "B",
            "DV-Applicant.APP[1].NAME[1].LastName " : "BROWN",
            "DV-Applicant.APP[1].NAME[1].Suffix " : "MR",
            "DV-Applicant.APP[1].DateofBirth " : "19991003",
            "DV-Applicant.APP[1].SSN " : "666535944",
            "DV-Product.PRODUCT[1].ProductType" : "UCC",
            "DV-Product.PRODUCT[1].ProductID" : "UNSECCC",
            "DV-Applicant.APP[1].CreditRole" : "Borrower",
            "DV-Applicant.APP[1].IndividualJoint" : "I",
            "DV-Applicant.APP[1].IDENT[1].IDType" : "DL",
            "DV-Applicant.APP[1].IDENT[1].IDNumber" : "12345678",
            "DV-Applicant.APP[1].IDENT[1].IDCountry" : "US",
            "DV-Applicant.APP[1].IDENT[1].IDState" : "AL",
            "DV-Applicant.APP[1].IDENT[1].IDIssueDate" : "20101010",
            "DV-Applicant.APP[1].IDENT[1].IDExpiration": "20201010",
            "DV-Applicant.APP[1].ADDRESS[1].AddressLine1 " : "6402 S.234",
            "DV-Applicant.APP[1].ADDRESS[1].AddressLine2 " : "Apt. 2012",
            "DV-Applicant.APP[1].ADDRESS[1].City " : "PORTLAND",
            "DV-Applicant.APP[1].ADDRESS[1].State " : "OR",
            "DV-Applicant.APP[1].ADDRESS[1].ZipCode " : "972093482",
            "DV-Applicant.APP[1].ADDRESS[1].ResidentialStatus " : "Own",
            "DV-Applicant.APP[1].ADDRESS[1].MonthlyPayment" : 2000,
            "DV-Applicant.APP[1].ADDRESS[1].YearsAtAddress " : 3,
            "DV-Applicant.APP[1].PHONE[1].PhoneNumber" : "5032219597",
            "DV-Applicant.APP[1].PHONE[1].PhoneType" : "C",
            "DV-Applicant.APP[1].GrantPermissionToContactCell" : "Y",
            "DV-Applicant.APP[1].EmailAddress" : "eb@asd",
            "DV-Applicant.APP[1].GrossMthlyIncome" : 5890,
            "DV-Applicant.APP[1].EMPL[1].ContactName" : "Jacks Fish Cleaning",
            "DV-Applicant.APP[1].EMPL[1].EmployerAddressLine1" : "101 S. Main",
            "DV-Applicant.APP[1].EMPL[1].EmployerAddressLine2" : "Suite 101",
            "DV-Applicant.APP[1].EMPL[1].EmployerAddressCity" : "Aurora",
            "DV-Applicant.APP[1].EMPL[1].EmployerAddressState" : "Al",
            "DV-Applicant.APP[1].EMPL[1].YearsWithEmployer" : 3,
            "DV-Applicant.APP[1].AlimonyChildSupport" : 1200,
            "DV-Applicant.APP[1].OtherMthlyIncome" : 500,
            "DV-Applicant.APP[1].OtherIncomeSource" : "Stock dividend and interest",
            "DV-Product.PRODUCT[1].AmountRequested " : 10000,
            "DV-Product.PRODUCT[1].TermRequested " : 12
          }
      """
    And I add the following headers to the REST request:
      | Content-Type | application/json |
      | Accept       | application/json |
    When I send a REST POST request to /v0/applications/NewApp and receive status code HTTP 200
    Then I verify that the JSON response has fields:
      | $.data.['DV-Results.Result Calls.C1 Policy Rules-Decision Setter Results.Decision Category'] | DECLINE |

  Scenario: ACF-US Pre-Bureau DA call - DECLINE - All Applicants are outside lending territory
    # Test-ID: 5015359
    # Type: Functional
    # Use-Case: ACF
    # Priority: P3 - Medium
    And I update parameter Pre-Bureau Decisioning_TP - TP - Pre-Bureau Decisioning description: Test ,effective from: 01/01/2018
      | Lending Area Ind | Lending Area Rule Flag | Applicant Age | Minimum Gross Mthly Income | Min Applicant Age Rule Type | Minimum Gross Mthly Income Rule Type |
      | 1                | Y                      | 21            | 3000                       | D                           | D                                    |
    And I update parameter Lending Area_TP - TP - Lending Area description: Test ,effective from: 01/01/2018
      | Lending Area ID | All States | State 1 | State 2 | State 3 | State 4 | State 5 | State 6 | State 7 | State 8 | State 9 | State 10 | State 11 | State 12 | State 13 | State 14 | State 15 | State 16 | State 17 | State 18 | State 19 | State 20 | State 21 | State 22 | State 23 | State 24 | State 25 | State 26 | State 27 | State 28 | State 29 | State 30 | State 31 | State 32 | State 33 | State 34 | State 35 | State 36 | State 37 | State 38 | State 39 | State 40 | State 41 | State 42 | State 43 | State 44 | State 45 | State 46 | State 47 | State 48 | State 49 | State 50 |
      | LAID1           | N          | AL      | AK      |         |         |         |         |         |         |         |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |
    And I deploy tactical parameter Pre-Bureau Decisioning_TP - TP - Pre-Bureau Decisioning version LATEST
    And I deploy tactical parameter Lending Area_TP - TP - Lending Area version LATEST
    And I set the base webservice url to ${bps.webservices.url}
    And I prepare REST request body:
      """
         {
            "DV-Application.Channel" : "Str",
            "DV-Application.ApplicationSource" : "Post Modern Furniture Nbr1",
            "DV-Application.InternalContactName" : "Jones,Kerry",
            "DV-Applicant.APP[1].NAME[1].FirstName " : "NANCY",
            "DV-Applicant.APP[1].NAME[1].MiddleName " : "L",
            "DV-Applicant.APP[1].NAME[1].LastName " : "BIRKHEAD",
            "DV-Applicant.APP[1].NAME[1].Suffix " : "MRS",
            "DV-Applicant.APP[1].DateofBirth " : "19381014",
            "DV-Applicant.APP[1].SSN " : "666701451",
            "DV-Product.PRODUCT[1].ProductType" : "UCC",
            "DV-Product.PRODUCT[1].ProductID" : "UNSECCC",
            "DV-Applicant.APP[1].CreditRole" : "Borrower",
            "DV-Applicant.APP[1].IndividualJoint" : "I",
            "DV-Applicant.APP[1].IDENT[1].IDType" : "DL",
            "DV-Applicant.APP[1].IDENT[1].IDNumber" : "12345678",
            "DV-Applicant.APP[1].IDENT[1].IDCountry" : "US",
            "DV-Applicant.APP[1].IDENT[1].IDState" : "AL",
            "DV-Applicant.APP[1].IDENT[1].IDIssueDate" : "20101010",
            "DV-Applicant.APP[1].IDENT[1].IDExpiration": "20201010",
            "DV-Applicant.APP[1].ADDRESS[1].AddressLine1 " : "6402 S.234",
            "DV-Applicant.APP[1].ADDRESS[1].AddressLine2 " : "Apt. 2012",
            "DV-Applicant.APP[1].ADDRESS[1].City " : "BLOOMSBURG",
            "DV-Applicant.APP[1].ADDRESS[1].State " : "FL",
            "DV-Applicant.APP[1].ADDRESS[1].ZipCode " : "178151847",
            "DV-Applicant.APP[1].ADDRESS[1].ResidentialStatus " : "Own",
            "DV-Applicant.APP[1].ADDRESS[1].MonthlyPayment" : 2000,
            "DV-Applicant.APP[1].ADDRESS[1].YearsAtAddress " : 3,
            "DV-Applicant.APP[1].PHONE[1].PhoneNumber" : "5704414264",
            "DV-Applicant.APP[1].PHONE[1].PhoneType" : "C",
            "DV-Applicant.APP[1].GrantPermissionToContactCell" : "Y",
            "DV-Applicant.APP[1].EmailAddress" : "nb@asd",
            "DV-Applicant.APP[1].GrossMthlyIncome" : 5890,
            "DV-Applicant.APP[1].EMPL[1].ContactName" : "Jacks Fish Cleaning",
            "DV-Applicant.APP[1].EMPL[1].EmployerAddressLine1" : "101 S. Main",
            "DV-Applicant.APP[1].EMPL[1].EmployerAddressLine2" : "Suite 101",
            "DV-Applicant.APP[1].EMPL[1].EmployerAddressCity" : "Aurora",
            "DV-Applicant.APP[1].EMPL[1].EmployerAddressState" : "Al",
            "DV-Applicant.APP[1].EMPL[1].YearsWithEmployer" : 3,
            "DV-Applicant.APP[1].AlimonyChildSupport" : 1200,
            "DV-Applicant.APP[1].OtherMthlyIncome" : 500,
            "DV-Applicant.APP[1].OtherIncomeSource" : "Stock dividend and interest",
            "DV-Product.PRODUCT[1].AmountRequested " : 10000,
            "DV-Product.PRODUCT[1].TermRequested " : 12,
            "DV-Application.Channel" : "Str",
            "DV-Application.ApplicationSource" : "Post Modern Furniture Nbr1",
            "DV-Application.InternalContactName" : "Jones,Kerry",
            "DV-Applicant.APP[2].NAME[1].FirstName " : "NANCY",
            "DV-Applicant.APP[2].NAME[1].MiddleName " : "L",
            "DV-Applicant.APP[2].NAME[1].LastName " : "BIRKHEAD",
            "DV-Applicant.APP[2].NAME[1].Suffix " : "MRS",
            "DV-Applicant.APP[2].DateofBirth " : "19381014",
            "DV-Applicant.APP[2].SSN " : "666701451",
            "DV-Product.PRODUCT[1].ProductType" : "UCC",
            "DV-Product.PRODUCT[1].ProductID" : "UNSECCC",
            "DV-Applicant.APP[2].CreditRole" : "Borrower",
            "DV-Applicant.APP[2].IndividualJoint" : "I",
            "DV-Applicant.APP[2].IDENT[1].IDType" : "DL",
            "DV-Applicant.APP[2].IDENT[1].IDNumber" : "12345678",
            "DV-Applicant.APP[2].IDENT[1].IDCountry" : "US",
            "DV-Applicant.APP[2].IDENT[1].IDState" : "AL",
            "DV-Applicant.APP[2].IDENT[1].IDIssueDate" : "20101010",
            "DV-Applicant.APP[2].IDENT[1].IDExpiration": "20201010",
            "DV-Applicant.APP[2].ADDRESS[1].AddressLine1 " : "6402 S.234",
            "DV-Applicant.APP[2].ADDRESS[1].AddressLine2 " : "Apt. 2012",
            "DV-Applicant.APP[2].ADDRESS[1].City " : "BLOOMSBURG",
            "DV-Applicant.APP[2].ADDRESS[1].State " : "FL",
            "DV-Applicant.APP[2].ADDRESS[1].ZipCode " : "178151847",
            "DV-Applicant.APP[2].ADDRESS[1].ResidentialStatus " : "Own",
            "DV-Applicant.APP[2].ADDRESS[1].MonthlyPayment" : 2000,
            "DV-Applicant.APP[2].ADDRESS[1].YearsAtAddress " : 3,
            "DV-Applicant.APP[2].PHONE[1].PhoneNumber" : "5704414264",
            "DV-Applicant.APP[2].PHONE[1].PhoneType" : "C",
            "DV-Applicant.APP[2].GrantPermissionToContactCell" : "Y",
            "DV-Applicant.APP[2].EmailAddress" : "nb@asd",
            "DV-Applicant.APP[2].GrossMthlyIncome" : 5890,
            "DV-Applicant.APP[2].EMPL[1].ContactName" : "Jacks Fish Cleaning",
            "DV-Applicant.APP[2].EMPL[1].EmployerAddressLine1" : "101 S. Main",
            "DV-Applicant.APP[2].EMPL[1].EmployerAddressLine2" : "Suite 101",
            "DV-Applicant.APP[2].EMPL[1].EmployerAddressCity" : "Aurora",
            "DV-Applicant.APP[2].EMPL[1].EmployerAddressState" : "Al",
            "DV-Applicant.APP[2].EMPL[1].YearsWithEmployer" : 3,
            "DV-Applicant.APP[2].AlimonyChildSupport" : 1200,
            "DV-Applicant.APP[2].OtherMthlyIncome" : 500,
            "DV-Applicant.APP[2].OtherIncomeSource" : "Stock dividend and interest",
            "DV-Product.PRODUCT[1].AmountRequested " : 10000,
            "DV-Product.PRODUCT[1].TermRequested " : 12
          }
          """
    And I add the following headers to the REST request:
      | Content-Type | application/json |
      | Accept       | application/json |
    When I send a REST POST request to /v0/applications/NewApp and receive status code HTTP 200
    Then I verify that the JSON response has fields:
      | $.data.['DV-Results.Result Calls.C1 Policy Rules-Decision Setter Results.Decision Category'] | DECLINE |

  Scenario: ACF-US Pre-Bureau DA call - DECLINE - At least 1 Applicant is outside lending territory
    # Test-ID: 5015361
    # Type: Functional
    # Use-Case: ACF
    # Priority: P3 - Medium
    And I update parameter Pre-Bureau Decisioning_TP - TP - Pre-Bureau Decisioning description: Test ,effective from: 01/01/2018
      | Lending Area Ind | Lending Area Rule Flag | Applicant Age | Minimum Gross Mthly Income | Min Applicant Age Rule Type | Minimum Gross Mthly Income Rule Type |
      | 2                | Y                      | 21            | 3000                       | D                           | D                                    |
    And I update parameter Lending Area_TP - TP - Lending Area description: Test ,effective from: 01/01/2018
      | Lending Area ID | All States | State 1 | State 2 | State 3 | State 4 | State 5 | State 6 | State 7 | State 8 | State 9 | State 10 | State 11 | State 12 | State 13 | State 14 | State 15 | State 16 | State 17 | State 18 | State 19 | State 20 | State 21 | State 22 | State 23 | State 24 | State 25 | State 26 | State 27 | State 28 | State 29 | State 30 | State 31 | State 32 | State 33 | State 34 | State 35 | State 36 | State 37 | State 38 | State 39 | State 40 | State 41 | State 42 | State 43 | State 44 | State 45 | State 46 | State 47 | State 48 | State 49 | State 50 |
      | LAID1           | N          | AL      | AK      |         |         |         |         |         |         |         |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |
    And I deploy tactical parameter Pre-Bureau Decisioning_TP - TP - Pre-Bureau Decisioning version LATEST
    And I deploy tactical parameter Lending Area_TP - TP - Lending Area version LATEST
    And I set the base webservice url to ${bps.webservices.url}
    And I prepare REST request body:
      """
         {
            "DV-Application.Channel" : "Str",
            "DV-Application.ApplicationSource" : "Post Modern Furniture Nbr1",
            "DV-Application.InternalContactName" : "Jones,Kerry",
            "DV-Applicant.APP[1].NAME[1].FirstName " : "NANCY",
            "DV-Applicant.APP[1].NAME[1].MiddleName " : "L",
            "DV-Applicant.APP[1].NAME[1].LastName " : "BIRKHEAD",
            "DV-Applicant.APP[1].NAME[1].Suffix " : "MRS",
            "DV-Applicant.APP[1].DateofBirth " : "19381014",
            "DV-Applicant.APP[1].SSN " : "666701451",
            "DV-Product.PRODUCT[1].ProductType" : "UCC",
            "DV-Product.PRODUCT[1].ProductID" : "UNSECCC",
            "DV-Applicant.APP[1].CreditRole" : "Borrower",
            "DV-Applicant.APP[1].IndividualJoint" : "I",
            "DV-Applicant.APP[1].IDENT[1].IDType" : "DL",
            "DV-Applicant.APP[1].IDENT[1].IDNumber" : "12345678",
            "DV-Applicant.APP[1].IDENT[1].IDCountry" : "US",
            "DV-Applicant.APP[1].IDENT[1].IDState" : "AL",
            "DV-Applicant.APP[1].IDENT[1].IDIssueDate" : "20101010",
            "DV-Applicant.APP[1].IDENT[1].IDExpiration": "20201010",
            "DV-Applicant.APP[1].ADDRESS[1].AddressLine1 " : "6402 S.234",
            "DV-Applicant.APP[1].ADDRESS[1].AddressLine2 " : "Apt. 2012",
            "DV-Applicant.APP[1].ADDRESS[1].City " : "BLOOMSBURG",
            "DV-Applicant.APP[1].ADDRESS[1].State " : "FL",
            "DV-Applicant.APP[1].ADDRESS[1].ZipCode " : "178151847",
            "DV-Applicant.APP[1].ADDRESS[1].ResidentialStatus " : "Own",
            "DV-Applicant.APP[1].ADDRESS[1].MonthlyPayment" : 2000,
            "DV-Applicant.APP[1].ADDRESS[1].YearsAtAddress " : 3,
            "DV-Applicant.APP[1].PHONE[1].PhoneNumber" : "5704414264",
            "DV-Applicant.APP[1].PHONE[1].PhoneType" : "C",
            "DV-Applicant.APP[1].GrantPermissionToContactCell" : "Y",
            "DV-Applicant.APP[1].EmailAddress" : "nb@asd",
            "DV-Applicant.APP[1].GrossMthlyIncome" : 5890,
            "DV-Applicant.APP[1].EMPL[1].ContactName" : "Jacks Fish Cleaning",
            "DV-Applicant.APP[1].EMPL[1].EmployerAddressLine1" : "101 S. Main",
            "DV-Applicant.APP[1].EMPL[1].EmployerAddressLine2" : "Suite 101",
            "DV-Applicant.APP[1].EMPL[1].EmployerAddressCity" : "Aurora",
            "DV-Applicant.APP[1].EMPL[1].EmployerAddressState" : "Al",
            "DV-Applicant.APP[1].EMPL[1].YearsWithEmployer" : 3,
            "DV-Applicant.APP[1].AlimonyChildSupport" : 1200,
            "DV-Applicant.APP[1].OtherMthlyIncome" : 500,
            "DV-Applicant.APP[1].OtherIncomeSource" : "Stock dividend and interest",
            "DV-Product.PRODUCT[1].AmountRequested " : 10000,
            "DV-Product.PRODUCT[1].TermRequested " : 12,
            "DV-Application.Channel" : "Str",
            "DV-Application.ApplicationSource" : "Post Modern Furniture Nbr1",
            "DV-Application.InternalContactName" : "Jones,Kerry",
            "DV-Applicant.APP[2].NAME[1].FirstName " : "NANCY",
            "DV-Applicant.APP[2].NAME[1].MiddleName " : "L",
            "DV-Applicant.APP[2].NAME[1].LastName " : "BIRKHEAD",
            "DV-Applicant.APP[2].NAME[1].Suffix " : "MRS",
            "DV-Applicant.APP[2].DateofBirth " : "19381014",
            "DV-Applicant.APP[2].SSN " : "666701451",
            "DV-Product.PRODUCT[1].ProductType" : "UCC",
            "DV-Product.PRODUCT[1].ProductID" : "UNSECCC",
            "DV-Applicant.APP[2].CreditRole" : "Borrower",
            "DV-Applicant.APP[2].IndividualJoint" : "I",
            "DV-Applicant.APP[2].IDENT[1].IDType" : "DL",
            "DV-Applicant.APP[2].IDENT[1].IDNumber" : "12345678",
            "DV-Applicant.APP[2].IDENT[1].IDCountry" : "US",
            "DV-Applicant.APP[2].IDENT[1].IDState" : "TX",
            "DV-Applicant.APP[2].IDENT[1].IDIssueDate" : "20101010",
            "DV-Applicant.APP[2].IDENT[1].IDExpiration": "20201010",
            "DV-Applicant.APP[2].ADDRESS[1].AddressLine1 " : "6402 S.234",
            "DV-Applicant.APP[2].ADDRESS[1].AddressLine2 " : "Apt. 2012",
            "DV-Applicant.APP[2].ADDRESS[1].City " : "BLOOMSBURG",
            "DV-Applicant.APP[2].ADDRESS[1].State " : "TX",
            "DV-Applicant.APP[2].ADDRESS[1].ZipCode " : "178151847",
            "DV-Applicant.APP[2].ADDRESS[1].ResidentialStatus " : "Own",
            "DV-Applicant.APP[2].ADDRESS[1].MonthlyPayment" : 2000,
            "DV-Applicant.APP[2].ADDRESS[1].YearsAtAddress " : 3,
            "DV-Applicant.APP[2].PHONE[1].PhoneNumber" : "5704414264",
            "DV-Applicant.APP[2].PHONE[1].PhoneType" : "C",
            "DV-Applicant.APP[2].GrantPermissionToContactCell" : "Y",
            "DV-Applicant.APP[2].EmailAddress" : "nb@asd",
            "DV-Applicant.APP[2].GrossMthlyIncome" : 5890,
            "DV-Applicant.APP[2].EMPL[1].ContactName" : "Jacks Fish Cleaning",
            "DV-Applicant.APP[2].EMPL[1].EmployerAddressLine1" : "101 S. Main",
            "DV-Applicant.APP[2].EMPL[1].EmployerAddressLine2" : "Suite 101",
            "DV-Applicant.APP[2].EMPL[1].EmployerAddressCity" : "Aurora",
            "DV-Applicant.APP[2].EMPL[1].EmployerAddressState" : "Al",
            "DV-Applicant.APP[2].EMPL[1].YearsWithEmployer" : 3,
            "DV-Applicant.APP[2].AlimonyChildSupport" : 1200,
            "DV-Applicant.APP[2].OtherMthlyIncome" : 500,
            "DV-Applicant.APP[2].OtherIncomeSource" : "Stock dividend and interest",
            "DV-Product.PRODUCT[1].AmountRequested " : 10000,
            "DV-Product.PRODUCT[1].TermRequested " : 12
         }
      """
    And I add the following headers to the REST request:
      | Content-Type | application/json |
      | Accept       | application/json |
    When I send a REST POST request to /v0/applications/NewApp and receive status code HTTP 200
    Then I verify that the JSON response has fields:
      | $.data.['DV-Results.Result Calls.C1 Policy Rules-Decision Setter Results.Decision Category'] | DECLINE |

  Scenario: ACF-US Pre-Bureau DA call - DECLINE - Primary Applicant is outside lending territory
    # Test-ID: 5015362
    # Type: Functional
    # Use-Case: ACF
    # Priority: P3 - Medium
    And I update parameter Pre-Bureau Decisioning_TP - TP - Pre-Bureau Decisioning description: Test ,effective from: 01/01/2018
      | Lending Area Ind | Lending Area Rule Flag | Applicant Age | Minimum Gross Mthly Income | Min Applicant Age Rule Type | Minimum Gross Mthly Income Rule Type |
      | 3                | Y                      | 21            | 3000                       | D                           | D                                    |
    And I update parameter Lending Area_TP - TP - Lending Area description: Test ,effective from: 01/01/2018
      | Lending Area ID | All States | State 1 | State 2 | State 3 | State 4 | State 5 | State 6 | State 7 | State 8 | State 9 | State 10 | State 11 | State 12 | State 13 | State 14 | State 15 | State 16 | State 17 | State 18 | State 19 | State 20 | State 21 | State 22 | State 23 | State 24 | State 25 | State 26 | State 27 | State 28 | State 29 | State 30 | State 31 | State 32 | State 33 | State 34 | State 35 | State 36 | State 37 | State 38 | State 39 | State 40 | State 41 | State 42 | State 43 | State 44 | State 45 | State 46 | State 47 | State 48 | State 49 | State 50 |
      | LAID1           | N          | AL      | AK      |         |         |         |         |         |         |         |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |          |
    And I deploy tactical parameter Pre-Bureau Decisioning_TP - TP - Pre-Bureau Decisioning version LATEST
    And I deploy tactical parameter Lending Area_TP - TP - Lending Area version LATEST
    And I set the base webservice url to ${bps.webservices.url}
    And I prepare REST request body:
      """
         {
            "DV-Application.Channel" : "Str",
            "DV-Application.ApplicationSource" : "Post Modern Furniture Nbr1",
            "DV-Application.InternalContactName" : "Jones,Kerry",
            "DV-Applicant.APP[1].NAME[1].FirstName " : "NANCY",
            "DV-Applicant.APP[1].NAME[1].MiddleName " : "L",
            "DV-Applicant.APP[1].NAME[1].LastName " : "BIRKHEAD",
            "DV-Applicant.APP[1].NAME[1].Suffix " : "MRS",
            "DV-Applicant.APP[1].DateofBirth " : "19381014",
            "DV-Applicant.APP[1].SSN " : "666701451",
            "DV-Product.PRODUCT[1].ProductType" : "UCC",
            "DV-Product.PRODUCT[1].ProductID" : "UNSECCC",
            "DV-Applicant.APP[1].CreditRole" : "Borrower",
            "DV-Applicant.APP[1].IndividualJoint" : "I",
            "DV-Applicant.APP[1].IDENT[1].IDType" : "DL",
            "DV-Applicant.APP[1].IDENT[1].IDNumber" : "12345678",
            "DV-Applicant.APP[1].IDENT[1].IDCountry" : "US",
            "DV-Applicant.APP[1].IDENT[1].IDState" : "AL",
            "DV-Applicant.APP[1].IDENT[1].IDIssueDate" : "20101010",
            "DV-Applicant.APP[1].IDENT[1].IDExpiration": "20201010",
            "DV-Applicant.APP[1].ADDRESS[1].AddressLine1 " : "6402 S.234",
            "DV-Applicant.APP[1].ADDRESS[1].AddressLine2 " : "Apt. 2012",
            "DV-Applicant.APP[1].ADDRESS[1].City " : "BLOOMSBURG",
            "DV-Applicant.APP[1].ADDRESS[1].State " : "TX",
            "DV-Applicant.APP[1].ADDRESS[1].ZipCode " : "178151847",
            "DV-Applicant.APP[1].ADDRESS[1].ResidentialStatus " : "Own",
            "DV-Applicant.APP[1].ADDRESS[1].MonthlyPayment" : 2000,
            "DV-Applicant.APP[1].ADDRESS[1].YearsAtAddress " : 3,
            "DV-Applicant.APP[1].PHONE[1].PhoneNumber" : "5704414264",
            "DV-Applicant.APP[1].PHONE[1].PhoneType" : "C",
            "DV-Applicant.APP[1].GrantPermissionToContactCell" : "Y",
            "DV-Applicant.APP[1].EmailAddress" : "nb@asd",
            "DV-Applicant.APP[1].GrossMthlyIncome" : 5890,
            "DV-Applicant.APP[1].EMPL[1].ContactName" : "Jacks Fish Cleaning",
            "DV-Applicant.APP[1].EMPL[1].EmployerAddressLine1" : "101 S. Main",
            "DV-Applicant.APP[1].EMPL[1].EmployerAddressLine2" : "Suite 101",
            "DV-Applicant.APP[1].EMPL[1].EmployerAddressCity" : "Aurora",
            "DV-Applicant.APP[1].EMPL[1].EmployerAddressState" : "Al",
            "DV-Applicant.APP[1].EMPL[1].YearsWithEmployer" : 3,
            "DV-Applicant.APP[1].AlimonyChildSupport" : 1200,
            "DV-Applicant.APP[1].OtherMthlyIncome" : 500,
            "DV-Applicant.APP[1].OtherIncomeSource" : "Stock dividend and interest",
            "DV-Product.PRODUCT[1].AmountRequested " : 10000,
            "DV-Product.PRODUCT[1].TermRequested " : 12,
            "DV-Application.Channel" : "Str",
            "DV-Application.ApplicationSource" : "Post Modern Furniture Nbr1",
            "DV-Application.InternalContactName" : "Jones,Kerry",
            "DV-Applicant.APP[2].NAME[1].FirstName " : "NANCY",
            "DV-Applicant.APP[2].NAME[1].MiddleName " : "L",
            "DV-Applicant.APP[2].NAME[1].LastName " : "BIRKHEAD",
            "DV-Applicant.APP[2].NAME[1].Suffix " : "MRS",
            "DV-Applicant.APP[2].DateofBirth " : "19381014",
            "DV-Applicant.APP[2].SSN " : "666701451",
            "DV-Product.PRODUCT[1].ProductType" : "UCC",
            "DV-Product.PRODUCT[1].ProductID" : "UNSECCC",
            "DV-Applicant.APP[2].CreditRole" : "Borrower",
            "DV-Applicant.APP[2].IndividualJoint" : "I",
            "DV-Applicant.APP[2].IDENT[1].IDType" : "DL",
            "DV-Applicant.APP[2].IDENT[1].IDNumber" : "12345678",
            "DV-Applicant.APP[2].IDENT[1].IDCountry" : "US",
            "DV-Applicant.APP[2].IDENT[1].IDState" : "AL",
            "DV-Applicant.APP[2].IDENT[1].IDIssueDate" : "20101010",
            "DV-Applicant.APP[2].IDENT[1].IDExpiration": "20201010",
            "DV-Applicant.APP[2].ADDRESS[1].AddressLine1 " : "6402 S.234",
            "DV-Applicant.APP[2].ADDRESS[1].AddressLine2 " : "Apt. 2012",
            "DV-Applicant.APP[2].ADDRESS[1].City " : "BLOOMSBURG",
            "DV-Applicant.APP[2].ADDRESS[1].State " : "AL",
            "DV-Applicant.APP[2].ADDRESS[1].ZipCode " : "178151847",
            "DV-Applicant.APP[2].ADDRESS[1].ResidentialStatus " : "Own",
            "DV-Applicant.APP[2].ADDRESS[1].MonthlyPayment" : 2000,
            "DV-Applicant.APP[2].ADDRESS[1].YearsAtAddress " : 3,
            "DV-Applicant.APP[2].PHONE[1].PhoneNumber" : "5704414264",
            "DV-Applicant.APP[2].PHONE[1].PhoneType" : "C",
            "DV-Applicant.APP[2].GrantPermissionToContactCell" : "Y",
            "DV-Applicant.APP[2].EmailAddress" : "nb@asd",
            "DV-Applicant.APP[2].GrossMthlyIncome" : 5890,
            "DV-Applicant.APP[2].EMPL[1].ContactName" : "Jacks Fish Cleaning",
            "DV-Applicant.APP[2].EMPL[1].EmployerAddressLine1" : "101 S. Main",
            "DV-Applicant.APP[2].EMPL[1].EmployerAddressLine2" : "Suite 101",
            "DV-Applicant.APP[2].EMPL[1].EmployerAddressCity" : "Aurora",
            "DV-Applicant.APP[2].EMPL[1].EmployerAddressState" : "Al",
            "DV-Applicant.APP[2].EMPL[1].YearsWithEmployer" : 3,
            "DV-Applicant.APP[2].AlimonyChildSupport" : 1200,
            "DV-Applicant.APP[2].OtherMthlyIncome" : 500,
            "DV-Applicant.APP[2].OtherIncomeSource" : "Stock dividend and interest",
            "DV-Product.PRODUCT[1].AmountRequested " : 10000,
            "DV-Product.PRODUCT[1].TermRequested " : 12
          }
          """
    And I add the following headers to the REST request:
      | Content-Type | application/json |
      | Accept       | application/json |
    When I send a REST POST request to /v0/applications/NewApp and receive status code HTTP 200
    Then I verify that the JSON response has fields:
      | $.data.['DV-Results.Result Calls.C1 Policy Rules-Decision Setter Results.Decision Category'] | DECLINE |

  Scenario: ACF-US Pre-Bureau DA call - DECLINE - Gross Monthly Income below Min cut-off
    # Test-ID: 5015363
    # Type: Functional
    # Use-Case: ACF
    # Priority: P3 - Medium
    And I set the base webservice url to ${bps.webservices.url}
    And I prepare REST request body:
      """
         {
            "DV-Application.Channel" : "BRA",
            "DV-Application.ApplicationSource" : "Telluride",
            "DV-Application.InternalContactName" : "Jones, Jane",
            "DV-Applicant.APP[1].NAME[1].FirstName " : "PAUL",
            "DV-Applicant.APP[1].NAME[1].MiddleName " : "MARIE",
            "DV-Applicant.APP[1].NAME[1].LastName " : "BURNIA",
            "DV-Applicant.APP[1].NAME[1].Suffix " : "MR",
            "DV-Applicant.APP[1].DateofBirth " : "19610810",
            "DV-Applicant.APP[1].SSN " : "666390426",
            "DV-Product.PRODUCT[1].ProductType" : "UCC",
            "DV-Product.PRODUCT[1].ProductID" : "UNSECCC",
            "DV-Applicant.APP[1].CreditRole" : "Borrower",
            "DV-Applicant.APP[1].IndividualJoint" : "I",
            "DV-Applicant.APP[1].IDENT[1].IDType" : "DL",
            "DV-Applicant.APP[1].IDENT[1].IDNumber" : "12345678",
            "DV-Applicant.APP[1].IDENT[1].IDCountry" : "US",
            "DV-Applicant.APP[1].IDENT[1].IDState" : "AL",
            "DV-Applicant.APP[1].IDENT[1].IDIssueDate" : "20101010",
            "DV-Applicant.APP[1].IDENT[1].IDExpiration": "20201010",
            "DV-Applicant.APP[1].ADDRESS[1].AddressLine1 " : "6402 S.234",
            "DV-Applicant.APP[1].ADDRESS[1].AddressLine2 " : "Apt. 2012",
            "DV-Applicant.APP[1].ADDRESS[1].City " : "CARSON",
            "DV-Applicant.APP[1].ADDRESS[1].State " : "CA",
            "DV-Applicant.APP[1].ADDRESS[1].ZipCode " : "907462742",
            "DV-Applicant.APP[1].ADDRESS[1].ResidentialStatus " : "Own",
            "DV-Applicant.APP[1].ADDRESS[1].MonthlyPayment" : 2000,
            "DV-Applicant.APP[1].ADDRESS[1].YearsAtAddress " : 3,
            "DV-Applicant.APP[1].PHONE[1].PhoneNumber" : "3107649465",
            "DV-Applicant.APP[1].PHONE[1].PhoneType" : "C",
            "DV-Applicant.APP[1].GrantPermissionToContactCell" : "Y",
            "DV-Applicant.APP[1].EmailAddress" : "pb@asd",
            "DV-Applicant.APP[1].GrossMthlyIncome" : 100,
            "DV-Applicant.APP[1].EMPL[1].ContactName" : "Jacks Fish Cleaning",
            "DV-Applicant.APP[1].EMPL[1].EmployerAddressLine1" : "101 S. Main",
            "DV-Applicant.APP[1].EMPL[1].EmployerAddressLine2" : "Suite 101",
            "DV-Applicant.APP[1].EMPL[1].EmployerAddressCity" : "Aurora",
            "DV-Applicant.APP[1].EMPL[1].EmployerAddressState" : "Al",
            "DV-Applicant.APP[1].EMPL[1].YearsWithEmployer" : 3,
            "DV-Applicant.APP[1].AlimonyChildSupport" : 120,
            "DV-Applicant.APP[1].OtherMthlyIncome" : 500,
            "DV-Applicant.APP[1].OtherIncomeSource" : "Stock dividend and interest",
            "DV-Product.PRODUCT[1].AmountRequested " : 10000,
            "DV-Product.PRODUCT[1].TermRequested " : 12
          }
                  """
    And I add the following headers to the REST request:
      | Content-Type | application/json |
      | Accept       | application/json |
    When I send a REST POST request to /v0/applications/NewApp and receive status code HTTP 200
    Then I verify that the JSON response has fields:
      | $.data.['DV-Results.Result Calls.C1 Policy Rules-Decision Setter Results.Decision Category'] | DECLINE |