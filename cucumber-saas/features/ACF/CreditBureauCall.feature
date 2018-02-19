Feature: Credit Bureau Calls through the REST api
  In order to obtain a credit bureau score for an application from one of the Credit Bureaus available ,
  As an ACF user,
  I want to send a POST request with  information for one applicant with appropriate setting of tactical parameters and receive a valid response with the risk model score for the application.

  Background:
    Given I update tactical parameters from file ${features.path}/ACF/data/tactical_parameter_exported_data_v0.7.xml
    And I deploy tactical parameter Product_TP - TP - Product_TP Search version LATEST
    And I deploy tactical parameter OtherCalls_TP - TP - OtherCalls Search version LATEST
    And I deploy tactical parameter TPID - TP - Master Search version LATEST
    And I deploy tactical parameter DA Pre Bureau TP - TP - DA Pre Bureau version LATEST
    And I deploy tactical parameter DA Post Bureau TP - TP - DA Post Bureau version LATEST
    And I deploy tactical parameter FraudandIDProducts_TP - TP - FraudandIDProducts version LATEST
    And I deploy tactical parameter DA Post CrossCore TP - TP - DA Post CrossCore version LATEST
    And I deploy tactical parameter External Call Priority - TP - External Call Priority version LATEST

  Scenario: ID:EXCB01 As a User I want to CREATE an application through CLIENT SYSTEM to get experian credit bureau score for the application
    # Test-ID: 4817940
    # Type: Functional
    # Use-Case: ACF
    # Priority: P3 - Medium
    And I update parameter BureauEnabler_TP - TP - BureauEnabler description: Test ,effective from: 01/01/2018
      | Bureau En Out EXP | Bureau En Out EQX | Bureau En Out TUC | Experian FACTA Enabled | Experian Red Flag Enabled |
      | Y                 |  N                | N                 | N                      | N                         |
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
                "DV-Applicant.APP[1].ADDRESS[2].AddressLine1 " : "993 WESTWAY",
                "DV-Applicant.APP[1].ADDRESS[2].AddressLine2 " : "SOUTH",
                "DV-Applicant.APP[1].ADDRESS[2].City " : "ANNAPOLIS",
                "DV-Applicant.APP[1].ADDRESS[2].State " : "MD",
                "DV-Applicant.APP[1].ADDRESS[2].ZipCode " : "214015136",
                "DV-Applicant.APP[1].ADDRESS[2].ResidentialStatus " : "O",
                "DV-Applicant.APP[1].ADDRESS[2].MonthlyPayment" : 1500,
                "DV-Applicant.APP[1].ADDRESS[2].YearsAtAddress " : 5,
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
                "DV-Applicant.APP[1].EMPL[2].Name" : "Fish Cleaning",
                "DV-Applicant.APP[1].EMPL[2].AddressLine1" : "111 S. Main",
                "DV-Applicant.APP[1].EMPL[2].AddressLine2" : "Suite 111",
                "DV-Applicant.APP[1].EMPL[2].City" : "Aurora",
                "DV-Applicant.APP[1].EMPL[2].State" : "AL",
                "DV-Applicant.APP[1].EMPL[2].YearsWithEmployer" : 1,
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
      | $.data.['TP-EA.BureauEnabler.Bureau En Out EXP']           | Y   |
      | $.data.['DV-CreditReport.APP[1].CBR[2].RiskModelScore[1]'] | 771 |
      | $.data.['DV-CreditReport.APP[1].CBR[2].RiskModelScore[2]'] | 808 |
      | $.data.['DV-CreditReport.APP[1].CBR[2].RiskModelScore[3]'] | 779 |
      | $.data.['DV-CreditReport.APP[1].CBR[2].RiskModelScore[4]'] | 799 |

  Scenario: ID:EXCB03 As a User I want to CREATE an application through CLIENT SYSTEM to get message for experian credit bureau being swiched off.
  # Test-ID: 4817941
  # Type: Functional
  # Use-Case: ACF
  # Priority: P3 - Medium
  # The date format should be mm/dd/yyyy
    Given I update parameter BureauEnabler_TP - TP - BureauEnabler description: Test ,effective from: 01/01/2018
      | Bureau En Out EXP | Bureau En Out EQX | Bureau En Out TUC | Experian FACTA Enabled | Experian Red Flag Enabled |
      | N                 | N                 | N                 | N                      | N                         |

    And I deploy tactical parameter BureauEnabler_TP - TP - BureauEnabler version LATEST
    And I set the base webservice url to ${bps.webservices.url}
    And I prepare REST request body:
        """
            {
              "DV-Application.Channel" : "Branch",
              "DV-Application.Channel" : "BRA",
              "DV-Application.InternalContactName" : "Mount Pilot Bank",
              "DV-Application.ApplicationSource" : "Frederick,JoAnne",
              "DV-Application.TPID.Master" : "1",
              "DV-Applicant.APP[1].NAME[1].FirstName " : "Edwin",
              "DV-Applicant.APP[1].NAME[1].MiddleName " : "B",
              "DV-Applicant.APP[1].NAME[1].LastName " : "Cliff",
              "DV-Applicant.APP[1].NAME[1].Suffix " : "MR",
              "DV-Applicant.APP[1].DateofBirth " : "19651003",
              "DV-Applicant.APP[1].SSN " : "666453252",
              "DV-Product.PRODUCT[1].ProductNamePrefix" : "Unsecured",
              "DV-Product.PRODUCT[1].ProductName" : "CreditCard",
              "DV-Applicant.APP[1].CreditRole" : "Borrower",
              "DV-Applicant.APP[1].IndividualJoint" : "Individual",
              "DV-Applicant.APP[1].IDENT[1].IDType" : "Driver License",
              "DV-Applicant.APP[1].IndividualJoint" : "I",
              "DV-Applicant.APP[1].IDENT[1].IDType" : "DL",
              "DV-Applicant.APP[1].IDENT[1].IDNumber" : "12345678",
              "DV-Applicant.APP[1].IDENT[1].IDCountry" : "United States",
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
              "DV-Applicant.APP[1].ADDRESS[1].MonthlyPayment" : "2000",
              "DV-Applicant.APP[1].ADDRESS[1].MonthlyPayment" : 2000,
              "DV-Applicant.APP[1].ADDRESS[1].YearsAtAddress " : 3,
              "DV-Applicant.APP[1].ADDRESS[2].AddressLine1 " : "993 WESTWAY",
              "DV-Applicant.APP[1].ADDRESS[2].AddressLine2 " : "SOUTH",
              "DV-Applicant.APP[1].ADDRESS[2].City " : "ANNAPOLIS",
              "DV-Applicant.APP[1].ADDRESS[2].State " : "MD",
              "DV-Applicant.APP[1].ADDRESS[2].ZipCode " : "214015136",
              "DV-Applicant.APP[1].ADDRESS[2].ResidentialStatus " : "O",
              "DV-Applicant.APP[1].ADDRESS[2].MonthlyPayment" : 1500,
              "DV-Applicant.APP[1].ADDRESS[2].YearsAtAddress " : 5,
              "DV-Applicant.APP[1].PHONE[1].Number" : "987987456",
              "DV-Applicant.APP[1].PHONE[1].Type" : "Cell",
              "DV-Applicant.APP[1].GrantPermissionToContactCell" : "Yes",
              "DV-Applicant.APP[1].GrantPermissionToContactCell" : "Y",
              "DV-Applicant.APP[1].EmailAddress" : "acb@asd",
              "DV-Applicant.APP[1].GrossMthlyIncome" : 5890,
              "DV-Applicant.APP[1].EMPL[1].Name" : "Jacks Fish Cleaning",
              "DV-Applicant.APP[1].EMPL[1].AddressLine1" : "101 S. Main",
              "DV-Applicant.APP[1].EMPL[1].AddressLine2" : "Suite 101",
              "DV-Applicant.APP[1].EMPL[1].City" : "Aurora",
              "DV-Applicant.APP[1].EMPL[1].State" : "AL",
              "DV-Applicant.APP[1].EMPL[1].State" : "Al",
              "DV-Applicant.APP[1].EMPL[1].YearsWithEmployer" : 3,
              "DV-Applicant.APP[1].EMPL[2].Name" : "Fish Cleaning",
              "DV-Applicant.APP[1].EMPL[2].AddressLine1" : "111 S. Main",
              "DV-Applicant.APP[1].EMPL[2].AddressLine2" : "Suite 111",
              "DV-Applicant.APP[1].EMPL[2].City" : "Aurora",
              "DV-Applicant.APP[1].EMPL[2].State" : "AL",
              "DV-Applicant.APP[1].EMPL[2].YearsWithEmployer" : 1,
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
      | $.data.['TP-EA.BureauEnabler.Bureau En Out EXP'] | N |
#
  Scenario: ID:EQCB01 As a User I want to CREATE an application through CLIENT SYSTEM to get equifax credit bureau score for the application
    # Test-ID: 5010784
    # Type: Functional
    # Use-Case: ACF
    # Priority: P3 - Medium
    And I update parameter BureauEnabler_TP - TP - BureauEnabler description: Test ,effective from: 01/01/2018
      | Bureau En Out EXP | Bureau En Out EQX | Bureau En Out TUC | Experian FACTA Enabled | Experian Red Flag Enabled |
      | N                 |  Y                | N                 | N                      | N                         |
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
                "DV-Applicant.APP[1].ADDRESS[2].AddressLine1 " : "993 WESTWAY",
                "DV-Applicant.APP[1].ADDRESS[2].AddressLine2 " : "SOUTH",
                "DV-Applicant.APP[1].ADDRESS[2].City " : "ANNAPOLIS",
                "DV-Applicant.APP[1].ADDRESS[2].State " : "MD",
                "DV-Applicant.APP[1].ADDRESS[2].ZipCode " : "214015136",
                "DV-Applicant.APP[1].ADDRESS[2].ResidentialStatus " : "O",
                "DV-Applicant.APP[1].ADDRESS[2].MonthlyPayment" : 1500,
                "DV-Applicant.APP[1].ADDRESS[2].YearsAtAddress " : 5,
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
                "DV-Applicant.APP[1].EMPL[2].Name" : "Fish Cleaning",
                "DV-Applicant.APP[1].EMPL[2].AddressLine1" : "111 S. Main",
                "DV-Applicant.APP[1].EMPL[2].AddressLine2" : "Suite 111",
                "DV-Applicant.APP[1].EMPL[2].City" : "Aurora",
                "DV-Applicant.APP[1].EMPL[2].State" : "AL",
                "DV-Applicant.APP[1].EMPL[2].YearsWithEmployer" : 1,
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
      | $.data.['TP-EA.BureauEnabler.Bureau En Out EQX']           | Y   |
      | $.data.['DV-CreditReport.APP[1].CBR[2].RiskModelScore[1]'] | 771 |
      | $.data.['DV-CreditReport.APP[1].CBR[2].RiskModelScore[2]'] | 808 |
      | $.data.['DV-CreditReport.APP[1].CBR[2].RiskModelScore[3]'] | 779 |
      | $.data.['DV-CreditReport.APP[1].CBR[2].RiskModelScore[4]'] | 799 |

  Scenario: ID:EQCB02 As a User I want to CREATE an application through CLIENT SYSTEM to get message for equifax credit bureau being swiched off.
   # Test-ID: 5010785
   # Type: Functional
   # Use-Case: ACF
   # Priority: P3 - Medium
   # The date format should be mm/dd/yyyy
    Given I update parameter BureauEnabler_TP - TP - BureauEnabler description: Test ,effective from: 01/01/2018
      | Bureau En Out EXP | Bureau En Out EQX | Bureau En Out TUC | Experian FACTA Enabled | Experian Red Flag Enabled |
      | N                 | N                 | N                 | N                      | N                         |

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
              "DV-Product.PRODUCT[1].ProductNamePrefix" : "Unsecure",
              "DV-Product.PRODUCT[1].ProductName" : "CreditCard",
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
              "DV-Applicant.APP[1].ADDRESS[2].AddressLine1 " : "993 WESTWAY",
              "DV-Applicant.APP[1].ADDRESS[2].AddressLine2 " : "SOUTH",
              "DV-Applicant.APP[1].ADDRESS[2].City " : "ANNAPOLIS",
              "DV-Applicant.APP[1].ADDRESS[2].State " : "MD",
              "DV-Applicant.APP[1].ADDRESS[2].ZipCode " : "214015136",
              "DV-Applicant.APP[1].ADDRESS[2].ResidentialStatus " : "O",
              "DV-Applicant.APP[1].ADDRESS[2].MonthlyPayment" : 1500,
              "DV-Applicant.APP[1].ADDRESS[2].YearsAtAddress " : 5,
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
              "DV-Applicant.APP[1].EMPL[2].Name" : "Fish Cleaning",
              "DV-Applicant.APP[1].EMPL[2].AddressLine1" : "111 S. Main",
              "DV-Applicant.APP[1].EMPL[2].AddressLine2" : "Suite 111",
              "DV-Applicant.APP[1].EMPL[2].City" : "Aurora",
              "DV-Applicant.APP[1].EMPL[2].State" : "AL",
              "DV-Applicant.APP[1].EMPL[2].YearsWithEmployer" : 1,
              "DV-Applicant.APP[1].AlimonyChildSupport" : 1200,
              "DV-Applicant.APP[1].OtherMthlyIncome" : 500,
              "DV-Applicant.APP[1].OtherIncomeSource" : "Stock dividend and interest",
              "DV-Product.PRODUCT[1].AmountRequested " : 10000
            }

        """
    And I add the following headers to the REST request:
      | Content-Type | application/json |
      | Accept       | application/json |
    And I prepare REST authentcation username admin and password Secret123!
    When I send a REST POST request to /v1/applications/TENANT1/NewApp and receive status code HTTP 200
    Then I verify that the JSON response has fields:
      | $.data.['TP-EA.BureauEnabler.Bureau En Out EQX'] | N |

  Scenario: ID:TUCB01 As a User I want to CREATE an application through CLIENT SYSTEM to get transunion credit bureau score for the application
    # Test-ID: 5010786
    # Type: Functional
    # Use-Case: ACF
    # Priority: P3 - Medium
    And I update parameter BureauEnabler_TP - TP - BureauEnabler description: Test ,effective from: 01/01/2018
      | Bureau En Out EXP | Bureau En Out EQX | Bureau En Out TUC | Experian FACTA Enabled | Experian Red Flag Enabled |
      | N                 |  N                | Y                 | N                      | N                         |
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
                "DV-Applicant.APP[1].ADDRESS[2].AddressLine1 " : "993 WESTWAY",
                "DV-Applicant.APP[1].ADDRESS[2].AddressLine2 " : "SOUTH",
                "DV-Applicant.APP[1].ADDRESS[2].City " : "ANNAPOLIS",
                "DV-Applicant.APP[1].ADDRESS[2].State " : "MD",
                "DV-Applicant.APP[1].ADDRESS[2].ZipCode " : "214015136",
                "DV-Applicant.APP[1].ADDRESS[2].ResidentialStatus " : "O",
                "DV-Applicant.APP[1].ADDRESS[2].MonthlyPayment" : 1500,
                "DV-Applicant.APP[1].ADDRESS[2].YearsAtAddress " : 5,
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
                "DV-Applicant.APP[1].EMPL[2].Name" : "Fish Cleaning",
                "DV-Applicant.APP[1].EMPL[2].AddressLine1" : "111 S. Main",
                "DV-Applicant.APP[1].EMPL[2].AddressLine2" : "Suite 111",
                "DV-Applicant.APP[1].EMPL[2].City" : "Aurora",
                "DV-Applicant.APP[1].EMPL[2].State" : "AL",
                "DV-Applicant.APP[1].EMPL[2].YearsWithEmployer" : 1,
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
      | $.data.['TP-EA.BureauEnabler.Bureau En Out TUC']           | Y   |

  Scenario: ID:TUCB02 As a User I want to CREATE an application through CLIENT SYSTEM to get message for experian credit bureau being swiched off.
    # Test-ID: 5010787
    # Type: Functional
    # Use-Case: ACF
    # Priority: P3 - Medium
    # The date format should be mm/dd/yyyy
    Given I update parameter BureauEnabler_TP - TP - BureauEnabler description: Test ,effective from: 01/01/2018
      | Bureau En Out EXP | Bureau En Out EQX | Bureau En Out TUC | Experian FACTA Enabled | Experian Red Flag Enabled |
      | N                 | N                 | N                 | N                      | N                         |

    And I deploy tactical parameter BureauEnabler_TP - TP - BureauEnabler version LATEST
    And I set the base webservice url to ${bps.webservices.url}
    And I prepare REST request body:
        """
            {
              "DV-Application.Channel" : "Branch",
              "DV-Application.Channel" : "BRA",
              "DV-Application.InternalContactName" : "Mount Pilot Bank",
              "DV-Application.ApplicationSource" : "Frederick,JoAnne",
              "DV-Application.TPID.Master" : "1",
              "DV-Applicant.APP[1].NAME[1].FirstName " : "Edwin",
              "DV-Applicant.APP[1].NAME[1].MiddleName " : "B",
              "DV-Applicant.APP[1].NAME[1].LastName " : "Cliff",
              "DV-Applicant.APP[1].NAME[1].Suffix " : "MR",
              "DV-Applicant.APP[1].DateofBirth " : "19651003",
              "DV-Applicant.APP[1].SSN " : "666453252",
              "DV-Product.PRODUCT[1].ProductNamePrefix" : "Unsecured",
              "DV-Product.PRODUCT[1].ProductName" : "CreditCard",
              "DV-Applicant.APP[1].CreditRole" : "Borrower",
              "DV-Applicant.APP[1].IndividualJoint" : "Individual",
              "DV-Applicant.APP[1].IDENT[1].IDType" : "Driver License",
              "DV-Applicant.APP[1].IndividualJoint" : "I",
              "DV-Applicant.APP[1].IDENT[1].IDType" : "DL",
              "DV-Applicant.APP[1].IDENT[1].IDNumber" : "12345678",
              "DV-Applicant.APP[1].IDENT[1].IDCountry" : "United States",
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
              "DV-Applicant.APP[1].ADDRESS[1].MonthlyPayment" : "2000",
              "DV-Applicant.APP[1].ADDRESS[1].MonthlyPayment" : 2000,
              "DV-Applicant.APP[1].ADDRESS[1].YearsAtAddress " : 3,
              "DV-Applicant.APP[1].ADDRESS[2].AddressLine1 " : "993 WESTWAY",
              "DV-Applicant.APP[1].ADDRESS[2].AddressLine2 " : "SOUTH",
              "DV-Applicant.APP[1].ADDRESS[2].City " : "ANNAPOLIS",
              "DV-Applicant.APP[1].ADDRESS[2].State " : "MD",
              "DV-Applicant.APP[1].ADDRESS[2].ZipCode " : "214015136",
              "DV-Applicant.APP[1].ADDRESS[2].ResidentialStatus " : "O",
              "DV-Applicant.APP[1].ADDRESS[2].MonthlyPayment" : 1500,
              "DV-Applicant.APP[1].ADDRESS[2].YearsAtAddress " : 5,
              "DV-Applicant.APP[1].PHONE[1].Number" : "987987456",
              "DV-Applicant.APP[1].PHONE[1].Type" : "Cell",
              "DV-Applicant.APP[1].GrantPermissionToContactCell" : "Yes",
              "DV-Applicant.APP[1].GrantPermissionToContactCell" : "Y",
              "DV-Applicant.APP[1].EmailAddress" : "acb@asd",
              "DV-Applicant.APP[1].GrossMthlyIncome" : 5890,
              "DV-Applicant.APP[1].EMPL[1].Name" : "Jacks Fish Cleaning",
              "DV-Applicant.APP[1].EMPL[1].AddressLine1" : "101 S. Main",
              "DV-Applicant.APP[1].EMPL[1].AddressLine2" : "Suite 101",
              "DV-Applicant.APP[1].EMPL[1].City" : "Aurora",
              "DV-Applicant.APP[1].EMPL[1].State" : "AL",
              "DV-Applicant.APP[1].EMPL[1].State" : "Al",
              "DV-Applicant.APP[1].EMPL[1].YearsWithEmployer" : 3,
              "DV-Applicant.APP[1].EMPL[2].Name" : "Fish Cleaning",
              "DV-Applicant.APP[1].EMPL[2].AddressLine1" : "111 S. Main",
              "DV-Applicant.APP[1].EMPL[2].AddressLine2" : "Suite 111",
              "DV-Applicant.APP[1].EMPL[2].City" : "Aurora",
              "DV-Applicant.APP[1].EMPL[2].State" : "AL",
              "DV-Applicant.APP[1].EMPL[2].YearsWithEmployer" : 1,
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
      | $.data.['TP-EA.BureauEnabler.Bureau En Out TUC']    | N               |