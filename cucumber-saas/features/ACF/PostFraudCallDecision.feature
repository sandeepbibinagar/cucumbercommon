Feature: Post Fraud Decision  through the REST api
  In order to obtain decision for an application based on Fraud and ID services calls,
  As an ACF user,
  I want to send a POST request with  information for one applicant and receive a valid response with Post Fraud scores and decision for the application

 Background:
    Given I update tactical parameters from file ${features.path}/ACF/data/tactical_parameter_exported_data_v0.4.xml
    And I deploy tactical parameter BureauEnabler_TP - TP - BureauEnabler version LATEST
    And I deploy tactical parameter Product_TP - TP - Product_TP Search version LATEST
    And I deploy tactical parameter OtherCalls_TP - TP - OtherCalls Search version LATEST
    And I deploy tactical parameter TPID - TP - Master Search version LATEST
    And I deploy tactical parameter External Call Priority - TP - External Call Priority version LATEST
    And I deploy tactical parameter DA Post CrossCore TP - TP - DA Post CrossCore version LATEST
    And I deploy tactical parameter DA Pre Bureau TP - TP - DA Pre Bureau version LATEST
    And I deploy tactical parameter DA Post Bureau TP - TP - DA Post Bureau version LATEST

  Scenario: ID:PCCD01 As a User I want to CREATE an application through CLIENT SYSTEM to get PreciseID score and decision APPROVE for the application
    # Test-ID: 5020112
    # Use-Case: ACF
    # Priority: P3
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
            "DV-Applicant.APP[1].CreditRole" : "Borrower",
            "DV-Applicant.APP[1].IndividualJoint" : "I",
            "DV-Applicant.APP[1].IDENT[1].IDType" : "DL",
            "DV-Applicant.APP[1].IDENT[1].IDNumber" : "12345678",
            "DV-Applicant.APP[1].IDENT[1].IDCountry" : "US",
            "DV-Applicant.APP[1].IDENT[1].IDState" : "AL",
            "DV-Applicant.APP[1].IDENT[1].IDIssueDate" : "20101010",
            "DV-Applicant.APP[1].IDENT[1].IDExpiration": "20201010",
            "DV-Applicant.APP[1].ADDRESS[1].AddressLine1 " : "6402 S. Malaya Street",
            "DV-Applicant.APP[1].ADDRESS[1].AddressLine2 " : "Apt. 2012",
            "DV-Applicant.APP[1].ADDRESS[1].City " : "Centennial",
            "DV-Applicant.APP[1].ADDRESS[1].State " : "AL",
            "DV-Applicant.APP[1].ADDRESS[1].ZipCode " : "80016",
            "DV-Applicant.APP[1].ADDRESS[1].ResidentialStatus " : "Own",
            "DV-Applicant.APP[1].ADDRESS[1].MonthlyPayment" : 2000,
            "DV-Applicant.APP[1].ADDRESS[1].YearsAtAddress " : 3,
            "DV-Applicant.APP[1].PHONE[1].Number" : "987987456",
            "DV-Applicant.APP[1].PHONE[1].Type" : "Cell",
            "DV-Applicant.APP[1].GrantPermissionToContactCell" : "Y",
            "DV-Applicant.APP[1].EmailAddress" : "acb@asd",
            "DV-Applicant.APP[1].GrossMthlyIncome" : 5890,
            "DV-Applicant.APP[1].EMPL[1].Name" : "Jacks Fish Cleaning",
            "DV-Applicant.APP[1].EMPL[1].AddressLine1" : "101 S. Main",
            "DV-Applicant.APP[1].EMPL[1].AddressLine2" : "Suite 101",
            "DV-Applicant.APP[1].EMPL[1].City" : "Aurora",
            "DV-Applicant.APP[1].EMPL[1].State" : "Al",
            "DV-Applicant.APP[1].EMPL[1].YearsWithEmployer" : 3,
            "DV-Applicant.APP[1].AlimonyChildSupport" : 1200,
            "DV-Applicant.APP[1].OtherMthlyIncome" : 500,
            "DV-Applicant.APP[1].OtherIncomeSource" : "Stock dividend and interest",
            "DV-Product.PRODUCT[1].AmountRequested " : 10000,
            "DV-Product.PRODUCT[1].TermRequested " : 3
          }
      """
    And I add the following headers to the REST request:
      | Content-Type | application/json |
      | Accept       | application/json |
    And I prepare REST authentcation username admin and password Secret123!
    When I send a REST POST request to /v1/applications/TENANT1/NewApp and receive status code HTTP 200
    Then I verify that the JSON response has fields:
      | $.data.['DV-CrossCore.Applicant[1].PreciseID.ServiceName']                                          | PreciseId |
      | $.data.['DV-CrossCore.Applicant[1].PreciseID.Decision']                                             | APPROVE   |
      | $.data.['DV-CrossCore.Applicant[1].PreciseID.Score']                                                | 675       |
      | $.data.['DV-CrossCore.Applicant[1].FraudNet.ServiceName']                                           | FraudNet  |
      | $.data.['DV-CrossCore.Applicant[1].FraudNet.Decision']                                              | APPROVE   |
      | $.data.['DV-CrossCore.Applicant[1].FraudNet.Score']                                                 | 1400      |
      | $.data.['DV-Results.Result Calls.C4 Policy Rules-Decision Setter Typical Result.Decision Category'] | APPROVE   |

  Scenario: ID:PCCD03 As a User I want to CREATE an application through CLIENT SYSTEM to get Post Fraud decision is DECLINE for the application when the  FraudNet score lower than the minimum
  # Test-ID: 5020114
  # Use-Case: ACF
  # Priority: P3
    And I update parameter DA Post CrossCore TP - TP - DA Post CrossCore description: Test ,effective from: 01/01/2018
      | DA Post CrossCore ID | MinimumPreciseIDScore | MinimumFraudnetScore |
      | 1                    | 500                   | 2000                 |
    And I deploy tactical parameter DA Post CrossCore TP - TP - DA Post CrossCore version LATEST
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
                  "DV-Applicant.APP[1].CreditRole" : "Borrower",
                  "DV-Applicant.APP[1].IndividualJoint" : "I",
                  "DV-Applicant.APP[1].IDENT[1].IDType" : "DL",
                  "DV-Applicant.APP[1].IDENT[1].IDNumber" : "12345678",
                  "DV-Applicant.APP[1].IDENT[1].IDCountry" : "US",
                  "DV-Applicant.APP[1].IDENT[1].IDState" : "AL",
                  "DV-Applicant.APP[1].IDENT[1].IDIssueDate" : "20101010",
                  "DV-Applicant.APP[1].IDENT[1].IDExpiration": "20201010",
                  "DV-Applicant.APP[1].ADDRESS[1].AddressLine1 " : "6402 S. Malaya Street",
                  "DV-Applicant.APP[1].ADDRESS[1].AddressLine2 " : "Apt. 2012",
                  "DV-Applicant.APP[1].ADDRESS[1].City " : "Centennial",
                  "DV-Applicant.APP[1].ADDRESS[1].State " : "AL",
                  "DV-Applicant.APP[1].ADDRESS[1].ZipCode " : "80016",
                  "DV-Applicant.APP[1].ADDRESS[1].ResidentialStatus " : "Own",
                  "DV-Applicant.APP[1].ADDRESS[1].MonthlyPayment" : 2000,
                  "DV-Applicant.APP[1].ADDRESS[1].YearsAtAddress " : 3,
                  "DV-Applicant.APP[1].PHONE[1].Number" : "987987456",
                  "DV-Applicant.APP[1].PHONE[1].Type" : "Cell",
                  "DV-Applicant.APP[1].GrantPermissionToContactCell" : "Y",
                  "DV-Applicant.APP[1].EmailAddress" : "acb@asd",
                  "DV-Applicant.APP[1].GrossMthlyIncome" : 5890,
                  "DV-Applicant.APP[1].EMPL[1].Name" : "Jacks Fish Cleaning",
                  "DV-Applicant.APP[1].EMPL[1].AddressLine1" : "101 S. Main",
                  "DV-Applicant.APP[1].EMPL[1].AddressLine2" : "Suite 101",
                  "DV-Applicant.APP[1].EMPL[1].City" : "Aurora",
                  "DV-Applicant.APP[1].EMPL[1].State" : "Al",
                  "DV-Applicant.APP[1].EMPL[1].YearsWithEmployer" : 3,
                  "DV-Applicant.APP[1].AlimonyChildSupport" : 1200,
                  "DV-Applicant.APP[1].OtherMthlyIncome" : 500,
                  "DV-Applicant.APP[1].OtherIncomeSource" : "Stock dividend and interest",
                  "DV-Product.PRODUCT[1].AmountRequested " : 10000,
                  "DV-Product.PRODUCT[1].TermRequested " : 3
                }
            """
    And I add the following headers to the REST request:
      | Content-Type | application/json |
      | Accept       | application/json |
    And I prepare REST authentcation username admin and password Secret123!
    When I send a REST POST request to /v1/applications/TENANT1/NewApp and receive status code HTTP 200
    Then I verify that the JSON response has fields:
      | $.data.['DV-Results.Result Calls.C4 Policy Rules-Decision Setter Typical Result.Decision Category'] | DECLINE   |
      | $.data.['DV-CrossCore.Applicant[1].FraudNet.ServiceName']                                           | FraudNet  |
      | $.data.['DV-CrossCore.Applicant[1].FraudNet.Score']                                                 | 1400      |

  Scenario: ID:PCCD02 As a User I want to CREATE an application through CLIENT SYSTEM to get Post Fraud decision is DECLINE for the application when the  PreciseID score lower than the minimum
 # Test-ID: 5020113
 # Use-Case: ACF
 # Priority: P3
    And I update parameter DA Post CrossCore TP - TP - DA Post CrossCore description: Test ,effective from: 01/01/218
      | DA Post CrossCore ID | MinimumPreciseIDScore | MinimumFraudnetScore |
      | 1                    | 2000                  | 500                  |
    And I deploy tactical parameter DA Post CrossCore TP - TP - DA Post CrossCore version LATEST
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
                  "DV-Applicant.APP[1].CreditRole" : "Borrower",
                  "DV-Applicant.APP[1].IndividualJoint" : "I",
                  "DV-Applicant.APP[1].IDENT[1].IDType" : "DL",
                  "DV-Applicant.APP[1].IDENT[1].IDNumber" : "12345678",
                  "DV-Applicant.APP[1].IDENT[1].IDCountry" : "US",
                  "DV-Applicant.APP[1].IDENT[1].IDState" : "AL",
                  "DV-Applicant.APP[1].IDENT[1].IDIssueDate" : "20101010",
                  "DV-Applicant.APP[1].IDENT[1].IDExpiration": "20201010",
                  "DV-Applicant.APP[1].ADDRESS[1].AddressLine1 " : "6402 S. Malaya Street",
                  "DV-Applicant.APP[1].ADDRESS[1].AddressLine2 " : "Apt. 2012",
                  "DV-Applicant.APP[1].ADDRESS[1].City " : "Centennial",
                  "DV-Applicant.APP[1].ADDRESS[1].State " : "AL",
                  "DV-Applicant.APP[1].ADDRESS[1].ZipCode " : "80016",
                  "DV-Applicant.APP[1].ADDRESS[1].ResidentialStatus " : "Own",
                  "DV-Applicant.APP[1].ADDRESS[1].MonthlyPayment" : 2000,
                  "DV-Applicant.APP[1].ADDRESS[1].YearsAtAddress " : 3,
                  "DV-Applicant.APP[1].PHONE[1].Number" : "987987456",
                  "DV-Applicant.APP[1].PHONE[1].Type" : "Cell",
                  "DV-Applicant.APP[1].GrantPermissionToContactCell" : "Y",
                  "DV-Applicant.APP[1].EmailAddress" : "acb@asd",
                  "DV-Applicant.APP[1].GrossMthlyIncome" : 5890,
                  "DV-Applicant.APP[1].EMPL[1].Name" : "Jacks Fish Cleaning",
                  "DV-Applicant.APP[1].EMPL[1].AddressLine1" : "101 S. Main",
                  "DV-Applicant.APP[1].EMPL[1].AddressLine2" : "Suite 101",
                  "DV-Applicant.APP[1].EMPL[1].City" : "Aurora",
                  "DV-Applicant.APP[1].EMPL[1].State" : "Al",
                  "DV-Applicant.APP[1].EMPL[1].YearsWithEmployer" : 3,
                  "DV-Applicant.APP[1].AlimonyChildSupport" : 1200,
                  "DV-Applicant.APP[1].OtherMthlyIncome" : 500,
                  "DV-Applicant.APP[1].OtherIncomeSource" : "Stock dividend and interest",
                  "DV-Product.PRODUCT[1].AmountRequested " : 10000,
                  "DV-Product.PRODUCT[1].TermRequested " : 3
                }
            """
    And I add the following headers to the REST request:
      | Content-Type | application/json |
      | Accept       | application/json |
    And I prepare REST authentcation username admin and password Secret123!
    When I send a REST POST request to /v1/applications/TENANT1/NewApp and receive status code HTTP 200
    Then I verify that the JSON response has fields:
      | $.data.['DV-Results.Result Calls.C4 Policy Rules-Decision Setter Typical Result.Decision Category'] | DECLINE   |
      | $.data.['DV-CrossCore.Applicant[1].PreciseID.ServiceName']                                          | PreciseId |
      | $.data.['DV-CrossCore.Applicant[1].PreciseID.Score']                                                | 675       |

