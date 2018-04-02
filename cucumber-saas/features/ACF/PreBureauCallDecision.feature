Feature: Pre Bureau Call Decision through the REST api
  In order to obtain a pre-bureau decision for an application,
  As an ACF user,
  I want to send a POST request with  information for one applicant and receive a valid response with pre-bureau decision for the application.

  Background:

    Given I update tactical parameters from file ${features.path}/ACF/data/tactical_parameter_exported_data_v0.7.xml
    And I deploy tactical parameter BureauEnabler_TP - TP - BureauEnabler version LATEST
    And I deploy tactical parameter OtherCalls_TP - TP - OtherCalls Search version LATEST
    And I deploy tactical parameter TPID - TP - Master Search version LATEST
    And I deploy tactical parameter External Call Priority - TP - External Call Priority version LATEST
    And I deploy tactical parameter DA Post CrossCore TP - TP - DA Post CrossCore version LATEST
    And I deploy tactical parameter DA Pre Bureau TP - TP - DA Pre Bureau version LATEST
    And I deploy tactical parameter DA Post Bureau TP - TP - DA Post Bureau version LATEST
    And I deploy tactical parameter FraudandIDProducts_TP - TP - FraudandIDProducts version LATEST
    And I update parameter Product_TP - TP - Product_TP Search description: Test ,effective from: 01/01/2018
      | Product ID | Min Age | Max Age | Min Credit Limit | Max Credit Limit | Product Name                   | Primary Purpose | Unique Product ID | Repayment Method | Payment Frequency | Accessed by Credit Card | Minimum Term | Maximum Term | Minimum Amount | Maximum Amount | Rate ID    | Calculation ID  | Secured | Collateral Purchased with Proceeds | Decision Rules Parameter ID | Bureaus and Scores Parameter Set | Product in Production |
      | UPL0234234 | 18      | 60      | 100              | 1000             | Personal Loan                  | Consumer        | UPL0234234        | Amortized        | Monthly           | No                      | 12           | 36           | 100            | 100000         | AUT8128992 | AUTOLOAN9023923 | Yes     | Yes                                | USEDAUTO098080              | AUTOParamv4                      | Yes                   |
      | UCC02VISA1 | 18      | 60      | 100              | 1000             | Credit Card - Visa             | Consumer        | UCC0234233        | Amortized        | Monthly           | No                      | 11           | 35           | 100            | 100000         | AUT8128991 | AUTOLOAN9023922 | Yes     | Yes                                | USEDAUTO098080              | AUTOParamv4                      | Yes                   |
      | UCCMASTER3 | 18      | 60      | 100              | 1000             | Credit Card - Master Card      | Consumer        | UCC0234232        | Amortized        | Monthly           | No                      | 15           | 40           | 100            | 100000         | AUT8128990 | AUTOLOAN9023921 | Yes     | Yes                                | USEDAUTO098080              | AUTOParamv4                      | Yes                   |
      | SPL0234234 | 18      | 60      | 100              | 1000             | Personal Loan                  | Consumer        | SPL0234231        | Amortized        | Monthly           | No                      | 9            | 33           | 100            | 100000         | AUT8128999 | AUTOLOAN9023920 | Yes     | Yes                                | USEDAUTO098080              | AUTOParamv4                      | Yes                   |
      | SCC02VISA1 | 18      | 60      | 100              | 1000             | Credit Card - Visa Gold        | Consumer        | SCC0234230        | Amortized        | Monthly           | No                      | 8            | 32           | 100            | 100000         | AUT8128998 | AUTOLOAN9023929 | Yes     | Yes                                | USEDAUTO098080              | AUTOParamv4                      | Yes                   |
      | SCCMASTER4 | 18      | 60      | 100              | 1000             | Credit Card - Master Card Gold | Consumer        | SCC0234229        | Amortized        | Monthly           | No                      | 7            | 31           | 100            | 100000         | AUT8128997 | AUTOLOAN9023928 | Yes     | Yes                                | USEDAUTO098080              | AUTOParamv4                      | Yes                   |
    And I deploy tactical parameter Product_TP - TP - Product_TP Search version LATEST
    And I prepare JWT token with user adm@example.com and password Password123 from service ${token.service.url}/v1/tokens/create

  Scenario: ACF-US Pre-Bureau DA call - APPROVE
  # Test-ID: 5015358
  # Type: Functional
  # Use-Case: ACF
  # Priority: P3 - Medium
    And I update parameter BureauEnabler_TP - TP - BureauEnabler description: Test ,effective from: 01/01/2018
      | Bureau En Out EXP | Bureau En Out EQX | Bureau En Out TUC | Experian FACTA Enabled | Experian Red Flag Enabled |
      | N                 | Y                 | N                 | N                      | N                         |

    And I deploy tactical parameter BureauEnabler_TP - TP - BureauEnabler version LATEST
    And I set the base webservice url to ${bps.webservices.url}
    And I prepare REST request body:
      """
        {
          "DV-Application.Channel" : "BRA",
          "DV-Application.InternalContactName" : "Mount Pilot Bank",
          "DV-Application.ApplicationSource" : "Frederick,JoAnne",
          "DV-Applicant.APP[1].NAME[1].FirstName " : "Edwin",
          "DV-Applicant.APP[1].NAME[1].MiddleName " : "B",
          "DV-Applicant.APP[1].NAME[1].LastName " : "Cliff",
          "DV-Applicant.APP[1].NAME[1].Suffix " : "MR",
          "DV-Applicant.APP[1].DateofBirth " : "19651003",
          "DV-Applicant.APP[1].SSN " : "666453252",
          "DV-Product.PRODUCT[1].ProductNamePrefix" : "Unsecured",
          "DV-Product.PRODUCT[1].ProductName" : "Credit Card",
          "DV-Product.PRODUCT[1].ProductID" : "UCC02VISA1",
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
          "DV-Applicant.APP[1].ADDRESS[1].City " : "Centennial",
          "DV-Applicant.APP[1].ADDRESS[1].State " : "AL",
          "DV-Applicant.APP[1].ADDRESS[1].ZipCode " : "80016",
          "DV-Applicant.APP[1].ADDRESS[1].MonthlyPayment" : 2000,
          "DV-Applicant.APP[1].ADDRESS[1].YearsAtAddress " : 3,
          "DV-Applicant.APP[1].PHONE[1].PhoneNumber" : "987987456",
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
          "DV-Product.PRODUCT[1].TermRequested " : 15
        }
      """
    And I add the following headers to the REST request:
      | Content-Type | application/json |
      | Accept       | application/json |
    When I send a REST POST request to /v0/applications/NewApp and receive status code HTTP 200
    Then I verify that the JSON response has fields:
      | $.data.['DV-Results.Result Calls.C1 Policy Rules-Decision Setter Typical Result.Decision Category'] | APPROVE    |
      | $.data.['DV-Results.Result Calls.C1 Scorecard 1-Scorecard Minimum Results.Score']                   | 950.000000 |


  Scenario: ACF-US Pre-Bureau DA call - INCOMPLETE
    # Test-ID: 5015359
    # Type: Functional
    # Use-Case: ACF
    # Priority: P3 - Medium
    And I update parameter BureauEnabler_TP - TP - BureauEnabler description: Test ,effective from: 01/01/2018
      | Bureau En Out EXP | Bureau En Out EQX | Bureau En Out TUC | Experian FACTA Enabled | Experian Red Flag Enabled |
      | N                 | Y                 | N                 | N                      | N                         |

    And I deploy tactical parameter BureauEnabler_TP - TP - BureauEnabler version LATEST
    And I update parameter DA Pre Bureau TP - TP - DA Pre Bureau description: Test ,effective from: 01/01/2018
      | DA Pre Bureau ID | MinimumAcceptScore |
      | 1                | 700                |
    And I deploy tactical parameter DA Pre Bureau TP - TP - DA Pre Bureau version LATEST

    And I set the base webservice url to ${bps.webservices.url}
    And I prepare REST request body:
    """
      {
        "DV-Application.Channel" : "BRA",
        "DV-Application.InternalContactName" : "Mount Pilot Bank",
        "DV-Application.ApplicationSource" : "Frederick,JoAnne",
        "DV-Applicant.APP[1].NAME[1].FirstName " : "Edwin",
        "DV-Applicant.APP[1].NAME[1].MiddleName " : "B",
        "DV-Applicant.APP[1].NAME[1].LastName " : "Cliff",
        "DV-Applicant.APP[1].NAME[1].Suffix " : "MR",
        "DV-Applicant.APP[1].DateofBirth " : "19851003",
        "DV-Applicant.APP[1].SSN " : "666453252",
        "DV-Product.PRODUCT[1].ProductNamePrefix" : "Unsecured",
        "DV-Product.PRODUCT[1].ProductName" : "Credit Card",
        "DV-Product.PRODUCT[1].ProductID" : "UCC02VISA1",
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
        "DV-Applicant.APP[1].ADDRESS[1].City " : "Centennial",
        "DV-Applicant.APP[1].ADDRESS[1].State " : "AL",
        "DV-Applicant.APP[1].ADDRESS[1].ZipCode " : "80016",
        "DV-Applicant.APP[1].ADDRESS[1].MonthlyPayment" : 2000,
        "DV-Applicant.APP[1].ADDRESS[1].YearsAtAddress " : 3,
        "DV-Applicant.APP[1].PHONE[1].PhoneNumber" : "987987456",
        "DV-Applicant.APP[1].PHONE[1].PhoneType" : "C",
        "DV-Applicant.APP[1].GrantPermissionToContactCell" : "Y",
        "DV-Applicant.APP[1].EmailAddress" : "acb@asd",
        "DV-Applicant.APP[1].GrossMthlyIncome" : 0,
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
        "DV-Product.PRODUCT[1].TermRequested " : 15
      }
          """
    And I add the following headers to the REST request:
      | Content-Type | application/json |
      | Accept       | application/json |
    When I send a REST POST request to /v0/applications/NewApp and receive status code HTTP 200
    Then I verify that the JSON response has fields:
      | $.data.['DV-Results.Result Calls.C1 Policy Rules-Decision Setter Typical Result.Decision Category'] | INCOMPLETE |


  Scenario: ACF-US Pre-Bureau DA call - DECLINE - Applicant's age lower than the minimum.
    # Test-ID: 5015360
    # Type: Functional
    # Use-Case: ACF
    # Priority: P3 - Medium
    And I update parameter BureauEnabler_TP - TP - BureauEnabler description: Test ,effective from: 01/01/2018
      | Bureau En Out EXP | Bureau En Out EQX | Bureau En Out TUC | Experian FACTA Enabled | Experian Red Flag Enabled |
      | N                 | Y                 | N                 | N                      | N                         |

    And I deploy tactical parameter BureauEnabler_TP - TP - BureauEnabler version LATEST
    And I set the base webservice url to ${bps.webservices.url}
    And I prepare REST request body:
      """
        {
          "DV-Application.Channel" : "BRA",
          "DV-Application.InternalContactName" : "Mount Pilot Bank",
          "DV-Application.ApplicationSource" : "Frederick,JoAnne",
          "DV-Applicant.APP[1].NAME[1].FirstName " : "Edwin",
          "DV-Applicant.APP[1].NAME[1].MiddleName " : "B",
          "DV-Applicant.APP[1].NAME[1].LastName " : "Cliff",
          "DV-Applicant.APP[1].NAME[1].Suffix " : "MR",
          "DV-Applicant.APP[1].DateofBirth " : "20021003",
          "DV-Applicant.APP[1].SSN " : "666453252",
          "DV-Product.PRODUCT[1].ProductNamePrefix" : "Unsecured",
          "DV-Product.PRODUCT[1].ProductName" : "Credit Card",
          "DV-Product.PRODUCT[1].ProductID" : "UCC02VISA1",
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
          "DV-Applicant.APP[1].ADDRESS[1].City " : "Centennial",
          "DV-Applicant.APP[1].ADDRESS[1].State " : "AL",
          "DV-Applicant.APP[1].ADDRESS[1].ZipCode " : "80016",
          "DV-Applicant.APP[1].ADDRESS[1].MonthlyPayment" : 2000,
          "DV-Applicant.APP[1].ADDRESS[1].YearsAtAddress " : 3,
          "DV-Applicant.APP[1].PHONE[1].PhoneNumber" : "987987456",
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
          "DV-Product.PRODUCT[1].TermRequested " : 15
        }
      """
    And I add the following headers to the REST request:
      | Content-Type | application/json |
      | Accept       | application/json |
    When I send a REST POST request to /v0/applications/NewApp and receive status code HTTP 200
    Then I verify that the JSON response has fields:
      | $.data.['DV-Results.Result Calls.C1 Policy Rules-Decision Setter Typical Result.Decision Category'] | DECLINE |

  Scenario: ACF-US Pre-Bureau DA call - DECLINE - Pre-bureau score lower than the minimum
    # Test-ID: 5015361
    # Type: Functional
    # Use-Case: ACF
    # Priority: P3 - Medium
    And I update parameter BureauEnabler_TP - TP - BureauEnabler description: Test ,effective from: 01/01/2018
      | Bureau En Out EXP | Bureau En Out EQX | Bureau En Out TUC | Experian FACTA Enabled | Experian Red Flag Enabled |
      | N                 | Y                 | N                 | N                      | N                         |

    And I deploy tactical parameter BureauEnabler_TP - TP - BureauEnabler version LATEST
    And I set the base webservice url to ${bps.webservices.url}
    And I prepare REST request body:
      """
        {
          "DV-Application.Channel" : "BRA",
          "DV-Application.InternalContactName" : "Mount Pilot Bank",
          "DV-Application.ApplicationSource" : "Frederick,JoAnne",
          "DV-Applicant.APP[1].NAME[1].FirstName " : "Edwin",
          "DV-Applicant.APP[1].NAME[1].MiddleName " : "B",
          "DV-Applicant.APP[1].NAME[1].LastName " : "Cliff",
          "DV-Applicant.APP[1].NAME[1].Suffix " : "MR",
          "DV-Applicant.APP[1].DateofBirth " : "19951003",
          "DV-Applicant.APP[1].SSN " : "666453252",
          "DV-Product.PRODUCT[1].ProductNamePrefix" : "Unsecured",
          "DV-Product.PRODUCT[1].ProductName" : "Credit Card",
          "DV-Product.PRODUCT[1].ProductID" : "UCC02VISA1",
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
          "DV-Applicant.APP[1].ADDRESS[1].City " : "Centennial",
          "DV-Applicant.APP[1].ADDRESS[1].State " : "AL",
          "DV-Applicant.APP[1].ADDRESS[1].ZipCode " : "80016",
          "DV-Applicant.APP[1].ADDRESS[1].MonthlyPayment" : 2000,
          "DV-Applicant.APP[1].ADDRESS[1].YearsAtAddress " : 3,
          "DV-Applicant.APP[1].PHONE[1].PhoneNumber" : "987987456",
          "DV-Applicant.APP[1].PHONE[1].PhoneType" : "C",
          "DV-Applicant.APP[1].GrantPermissionToContactCell" : "Y",
          "DV-Applicant.APP[1].EmailAddress" : "acb@asd",
          "DV-Applicant.APP[1].GrossMthlyIncome" : 70,
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
          "DV-Product.PRODUCT[1].TermRequested " : 15
        }
          """
    And I add the following headers to the REST request:
      | Content-Type | application/json |
      | Accept       | application/json |
    When I send a REST POST request to /v0/applications/NewApp and receive status code HTTP 200
    Then I verify that the JSON response has fields:
      | $.data.['DV-Results.Result Calls.C1 Policy Rules-Decision Setter Typical Result.Decision Category'] | DECLINE    |
      | $.data.['DV-Results.Result Calls.C1 Scorecard 1-Scorecard Minimum Results.Score']                   | 600.000000 |

  Scenario: ACF-US Pre-Bureau DA call - DECLINE - Applicant's age higher than the maximum.
    # Test-ID: 5015362
    # Type: Functional
    # Use-Case: ACF
    # Priority: P3 - Medium
    And I update parameter BureauEnabler_TP - TP - BureauEnabler description: Test ,effective from: 01/01/2018
      | Bureau En Out EXP | Bureau En Out EQX | Bureau En Out TUC | Experian FACTA Enabled | Experian Red Flag Enabled |
      | N                 | Y                 | N                 | N                      | N                         |

    And I deploy tactical parameter BureauEnabler_TP - TP - BureauEnabler version LATEST
    And I set the base webservice url to ${bps.webservices.url}
    And I prepare REST request body:
      """
        {
          "DV-Application.Channel" : "BRA",
          "DV-Application.InternalContactName" : "Mount Pilot Bank",
          "DV-Application.ApplicationSource" : "Frederick,JoAnne",
          "DV-Applicant.APP[1].NAME[1].FirstName " : "Edwin",
          "DV-Applicant.APP[1].NAME[1].MiddleName " : "B",
          "DV-Applicant.APP[1].NAME[1].LastName " : "Cliff",
          "DV-Applicant.APP[1].NAME[1].Suffix " : "MR",
          "DV-Applicant.APP[1].DateofBirth " : "19451003",
          "DV-Applicant.APP[1].SSN " : "666453252",
          "DV-Product.PRODUCT[1].ProductNamePrefix" : "Unsecured",
          "DV-Product.PRODUCT[1].ProductName" : "Credit Card",
          "DV-Product.PRODUCT[1].ProductID" : "UCC02VISA1",
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
          "DV-Applicant.APP[1].ADDRESS[1].City " : "Centennial",
          "DV-Applicant.APP[1].ADDRESS[1].State " : "AL",
          "DV-Applicant.APP[1].ADDRESS[1].ZipCode " : "80016",
          "DV-Applicant.APP[1].ADDRESS[1].MonthlyPayment" : 2000,
          "DV-Applicant.APP[1].ADDRESS[1].YearsAtAddress " : 3,
          "DV-Applicant.APP[1].PHONE[1].PhoneNumber" : "987987456",
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
          "DV-Product.PRODUCT[1].TermRequested " : 15
        }
                  """
    And I add the following headers to the REST request:
      | Content-Type | application/json |
      | Accept       | application/json |
    When I send a REST POST request to /v0/applications/NewApp and receive status code HTTP 200
    Then I verify that the JSON response has fields:
      | $.data.['DV-Results.Result Calls.C1 Policy Rules-Decision Setter Typical Result.Decision Category'] | DECLINE |

  Scenario: ACF-US Pre-Bureau DA call - DECLINE - Requested term lower than the minimum.
    # Test-ID: 5015363
    # Type: Functional
    # Use-Case: ACF
    # Priority: P3 - Medium
    And I update parameter BureauEnabler_TP - TP - BureauEnabler description: Test ,effective from: 01/01/2018
      | Bureau En Out EXP | Bureau En Out EQX | Bureau En Out TUC | Experian FACTA Enabled | Experian Red Flag Enabled |
      | N                 | Y                 | N                 | N                      | N                         |

    And I deploy tactical parameter BureauEnabler_TP - TP - BureauEnabler version LATEST
    And I set the base webservice url to ${bps.webservices.url}
    And I prepare REST request body:
      """
        {
          "DV-Application.Channel" : "BRA",
          "DV-Application.InternalContactName" : "Mount Pilot Bank",
          "DV-Application.ApplicationSource" : "Frederick,JoAnne",
          "DV-Applicant.APP[1].NAME[1].FirstName " : "Edwin",
          "DV-Applicant.APP[1].NAME[1].MiddleName " : "B",
          "DV-Applicant.APP[1].NAME[1].LastName " : "Cliff",
          "DV-Applicant.APP[1].NAME[1].Suffix " : "MR",
          "DV-Applicant.APP[1].DateofBirth " : "19851003",
          "DV-Applicant.APP[1].SSN " : "666453252",
          "DV-Product.PRODUCT[1].ProductNamePrefix" : "Unsecured",
          "DV-Product.PRODUCT[1].ProductName" : "Credit Card",
          "DV-Product.PRODUCT[1].ProductID" : "UCC02VISA1",
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
          "DV-Applicant.APP[1].ADDRESS[1].City " : "Centennial",
          "DV-Applicant.APP[1].ADDRESS[1].State " : "AL",
          "DV-Applicant.APP[1].ADDRESS[1].ZipCode " : "80016",
          "DV-Applicant.APP[1].ADDRESS[1].MonthlyPayment" : 2000,
          "DV-Applicant.APP[1].ADDRESS[1].YearsAtAddress " : 3,
          "DV-Applicant.APP[1].PHONE[1].PhoneNumber" : "987987456",
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
          "DV-Product.PRODUCT[1].TermRequested " : 1
        }
                  """
    And I add the following headers to the REST request:
      | Content-Type | application/json |
      | Accept       | application/json |
    When I send a REST POST request to /v0/applications/NewApp and receive status code HTTP 200
    Then I verify that the JSON response has fields:
      | $.data.['DV-Results.Result Calls.C1 Policy Rules-Decision Setter Typical Result.Decision Category'] | DECLINE |

  Scenario:  ACF-US Pre-Bureau DA call - DECLINE - Requested term higher than the maximum.
    # Test-ID: 5015364
    # Type: Functional
    # Use-Case: ACF
    # Priority: P3 - Medium
    And I update parameter BureauEnabler_TP - TP - BureauEnabler description: Test ,effective from: 01/01/2018
      | Bureau En Out EXP | Bureau En Out EQX | Bureau En Out TUC | Experian FACTA Enabled | Experian Red Flag Enabled |
      | N                 | Y                 | N                 | N                      | N                         |

    And I deploy tactical parameter BureauEnabler_TP - TP - BureauEnabler version LATEST
    And I set the base webservice url to ${bps.webservices.url}
    And I prepare REST request body:
      """
        {
          "DV-Application.Channel" : "BRA",
          "DV-Application.InternalContactName" : "Mount Pilot Bank",
          "DV-Application.ApplicationSource" : "Frederick,JoAnne",
          "DV-Applicant.APP[1].NAME[1].FirstName " : "Edwin",
          "DV-Applicant.APP[1].NAME[1].MiddleName " : "B",
          "DV-Applicant.APP[1].NAME[1].LastName " : "Cliff",
          "DV-Applicant.APP[1].NAME[1].Suffix " : "MR",
          "DV-Applicant.APP[1].DateofBirth " : "19851003",
          "DV-Applicant.APP[1].SSN " : "666453252",
          "DV-Product.PRODUCT[1].ProductNamePrefix" : "Unsecured",
          "DV-Product.PRODUCT[1].ProductName" : "Credit Card",
          "DV-Product.PRODUCT[1].ProductID" : "UCC02VISA1",
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
          "DV-Applicant.APP[1].ADDRESS[1].City " : "Centennial",
          "DV-Applicant.APP[1].ADDRESS[1].State " : "AL",
          "DV-Applicant.APP[1].ADDRESS[1].ZipCode " : "80016",
          "DV-Applicant.APP[1].ADDRESS[1].MonthlyPayment" : 2000,
          "DV-Applicant.APP[1].ADDRESS[1].YearsAtAddress " : 3,
          "DV-Applicant.APP[1].PHONE[1].PhoneNumber" : "987987456",
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
          "DV-Product.PRODUCT[1].TermRequested " : 41
        }
                  """
    And I add the following headers to the REST request:
      | Content-Type | application/json |
      | Accept       | application/json |
    When I send a REST POST request to /v0/applications/NewApp and receive status code HTTP 200
    Then I verify that the JSON response has fields:
      | $.data.['DV-Results.Result Calls.C1 Policy Rules-Decision Setter Typical Result.Decision Category'] | DECLINE |

  Scenario:  ACF-US Pre-Bureau DA call - DECLINE - Requested amount lower than the minimum.
    # Test-ID: 5015365
    # Type: Functional
    # Use-Case: ACF
    # Priority: P3 - Medium
    And I update parameter BureauEnabler_TP - TP - BureauEnabler description: Test ,effective from: 01/01/2018
      | Bureau En Out EXP | Bureau En Out EQX | Bureau En Out TUC | Experian FACTA Enabled | Experian Red Flag Enabled |
      | N                 | Y                 | N                 | N                      | N                         |

    And I deploy tactical parameter BureauEnabler_TP - TP - BureauEnabler version LATEST
    And I set the base webservice url to ${bps.webservices.url}
    And I prepare REST request body:
      """
        {
          "DV-Application.Channel" : "BRA",
          "DV-Application.InternalContactName" : "Mount Pilot Bank",
          "DV-Application.ApplicationSource" : "Frederick,JoAnne",
          "DV-Applicant.APP[1].NAME[1].FirstName " : "Edwin",
          "DV-Applicant.APP[1].NAME[1].MiddleName " : "B",
          "DV-Applicant.APP[1].NAME[1].LastName " : "Cliff",
          "DV-Applicant.APP[1].NAME[1].Suffix " : "MR",
          "DV-Applicant.APP[1].DateofBirth " : "19851003",
          "DV-Applicant.APP[1].SSN " : "666453252",
          "DV-Product.PRODUCT[1].ProductNamePrefix" : "Unsecured",
          "DV-Product.PRODUCT[1].ProductName" : "Credit Card",
          "DV-Product.PRODUCT[1].ProductID" : "UCC02VISA1",
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
          "DV-Applicant.APP[1].ADDRESS[1].City " : "Centennial",
          "DV-Applicant.APP[1].ADDRESS[1].State " : "AL",
          "DV-Applicant.APP[1].ADDRESS[1].ZipCode " : "80016",
          "DV-Applicant.APP[1].ADDRESS[1].MonthlyPayment" : 2000,
          "DV-Applicant.APP[1].ADDRESS[1].YearsAtAddress " : 3,
          "DV-Applicant.APP[1].PHONE[1].PhoneNumber" : "987987456",
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
          "DV-Product.PRODUCT[1].AmountRequested " : 99,
          "DV-Product.PRODUCT[1].TermRequested " : 3
        }
                  """
    And I add the following headers to the REST request:
      | Content-Type | application/json |
      | Accept       | application/json |
    When I send a REST POST request to /v0/applications/NewApp and receive status code HTTP 200
    Then I verify that the JSON response has fields:
      | $.data.['DV-Results.Result Calls.C1 Policy Rules-Decision Setter Typical Result.Decision Category'] | DECLINE |

  Scenario: ACF-US Pre-Bureau DA call - DECLINE - Requested amount higher than the maximum.
    # Test-ID: 5015366
    # Type: Functional
    # Use-Case: ACF
    # Priority: P3 - Medium
    And I update parameter BureauEnabler_TP - TP - BureauEnabler description: Test ,effective from: 01/01/2018
      | Bureau En Out EXP | Bureau En Out EQX | Bureau En Out TUC | Experian FACTA Enabled | Experian Red Flag Enabled |
      | N                 | Y                 | N                 | N                      | N                         |

    And I deploy tactical parameter BureauEnabler_TP - TP - BureauEnabler version LATEST
    And I set the base webservice url to ${bps.webservices.url}
    And I prepare REST request body:
      """
        {
          "DV-Application.Channel" : "BRA",
          "DV-Application.InternalContactName" : "Mount Pilot Bank",
          "DV-Application.ApplicationSource" : "Frederick,JoAnne",
          "DV-Applicant.APP[1].NAME[1].FirstName " : "Edwin",
          "DV-Applicant.APP[1].NAME[1].MiddleName " : "B",
          "DV-Applicant.APP[1].NAME[1].LastName " : "Cliff",
          "DV-Applicant.APP[1].NAME[1].Suffix " : "MR",
          "DV-Applicant.APP[1].DateofBirth " : "19851003",
          "DV-Applicant.APP[1].SSN " : "666453252",
          "DV-Product.PRODUCT[1].ProductNamePrefix" : "Unsecured",
          "DV-Product.PRODUCT[1].ProductName" : "Credit Card",
          "DV-Product.PRODUCT[1].ProductID" : "UCC02VISA1",
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
          "DV-Applicant.APP[1].ADDRESS[1].City " : "Centennial",
          "DV-Applicant.APP[1].ADDRESS[1].State " : "AL",
          "DV-Applicant.APP[1].ADDRESS[1].ZipCode " : "80016",
          "DV-Applicant.APP[1].ADDRESS[1].MonthlyPayment" : 2000,
          "DV-Applicant.APP[1].ADDRESS[1].YearsAtAddress " : 3,
          "DV-Applicant.APP[1].PHONE[1].PhoneNumber" : "987987456",
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
          "DV-Product.PRODUCT[1].AmountRequested " : 100001,
          "DV-Product.PRODUCT[1].TermRequested " : 3
        }
                  """
    And I add the following headers to the REST request:
      | Content-Type | application/json |
      | Accept       | application/json |
    When I send a REST POST request to /v0/applications/NewApp and receive status code HTTP 200
    Then I verify that the JSON response has fields:
      | $.data.['DV-Results.Result Calls.C1 Policy Rules-Decision Setter Typical Result.Decision Category'] | DECLINE |