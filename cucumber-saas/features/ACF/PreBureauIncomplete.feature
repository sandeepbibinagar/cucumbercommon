@run
Feature: Pre Bureau Decision through the REST api
  In order to obtain an Incomplete status for an application,
  As an ACF user,
  I want to send a POST request with  information for one applicant and receive a valid response with staus Incomplete for the application.

  Scenario: ID:PRBD02 As a User I want to CREATE an application through CLIENT SYSTEM to get pre-bureau decision INCOMPLETE for the application
     # Test-ID: 5015359
    # Use-Case: ACF
    # Priority: P3
    Given I update parameter BureauEnabler_TP - TP - BureauEnabler description: Test ,effective from: 01/01/218
      | Bureau En Out EXP | Bureau En Out EQX | Bureau En Out TUC | Experian FACTA Enabled | Experian Red Flag Enabled |
      | Y                 | N                 | N                 | N                      | N                         |

    And I deploy tactical parameter BureauEnabler_TP - TP - BureauEnabler version LATEST

    And I update parameter Product_TP - TP - Product_TP Search description: Test ,effective from: 01/01/218
      | Interest Rate | Min Age | Max Age | Min Credit Limit | Max Credit Limit | Min Amount Requested | Max Amount Requested | Min Term Requested | Max Term Requested |
      |               | 16      | 60      | 100              | 100000           | 100                  | 100000               | 2                  | 4                  |

    And I deploy tactical parameter Product_TP - TP - Product_TP Search version LATEST

    And I update parameter OtherCalls_TP - TP - OtherCalls Search description: Test ,effective from: 01/01/218
      | OtherCallsID | Kelly Blue Book | Black Book | Auto Check | Experian Motor Vehicle | EDQ Address Parser | ATB | Carleton Smartcalcs | Search Client Data | SearchBasedPrepopulation |
      | 1            | N               | N          | N          | N                      | N                  | N   | N                   | N                  | Y                        |

    And I deploy tactical parameter OtherCalls_TP - TP - OtherCalls Search version LATEST

    And I update parameter TPID - TP - Master Search description: Test ,effective from: 01/01/218
      | PreBurID | PostBurID | FraudID | FinalID | ExperianID | EquifaxID | TransUnionID | OtherCallsID | ExternalCallsPriorityID | PostCrossCoreID |
      | 1        | 1         | 1       | 1       | PRD        | 1         | 1            | 1            | 1                       | 1               |

    And I deploy tactical parameter TPID - TP - Master Search version LATEST

    And I update parameter External Call Priority - TP - External Call Priority description: Test ,effective from: 01/01/218
      | Priority ID | Priority 1    | Priority 2 | Priority 3                 | Priority 4 | Priority 5 |
      | 1           | Credit Bureau | FraudNet   | Other External Data Source | PreciseID  | PreciseID  |

    And I deploy tactical parameter External Call Priority - TP - External Call Priority version LATEST

    And I update parameter DA Post CrossCore TP - TP - DA Post CrossCore description: Test ,effective from: 01/01/218
      | DA Post CrossCore ID | MinimumPreciseIDScore | MinimumFraudnetScore |
      | 1                    | 500                   | 500                  |
    And I deploy tactical parameter DA Post CrossCore TP - TP - DA Post CrossCore version LATEST

    And I update parameter DA Pre Bureau TP - TP - DA Pre Bureau description: Test ,effective from: 01/01/2018
      | DA Pre Bureau ID | MinimumAcceptScore |
      | 1                | 700                |
    And I deploy tactical parameter DA Pre Bureau TP - TP - DA Pre Bureau version LATEST

    And I update parameter DA Post Bureau TP - TP - DA Post Bureau description: Test ,effective from: 01/01/2018
      | DA Post Bureau ID | MinimumReferScore | MinimumAcceptScore |
      | 1                 | 901               | 951                |
    And I deploy tactical parameter DA Post Bureau TP - TP - DA Post Bureau version LATEST

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
                "DV-Applicant.APP[1].GrossMthlyIncome" : 0,
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
      | $.data.['DV-Results.Result Calls.C1 Policy Rules-Decision Setter Typical Result.Decision Category'] | INCOMPLETE |
