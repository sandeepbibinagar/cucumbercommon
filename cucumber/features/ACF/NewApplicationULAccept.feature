Feature: New Application for Unsecured Personal Loans Accepted through the REST api
  In order to obtain a decision for an applicant,
  As a ACF user
  I want to send a POST request with the applicant information to the application and receive a response

  Scenario: NA007 As a User I want to CREATE an application through CLIENT SYSTEM to get final decision Accept when Pre bureau decision is Refer
    When I send POST request to /v1/applications/TENANT1/CreditEvaluation with username admin and password Secret123! and receive status code HTTP 200:
      """
      {
          "Applicant-DV.APP.NME[1].FirstName" : "BERK",
          "Applicant-DV.APP.NME[1].LastName" : "SHARON",
          "Applicant-DV.APP.NME[1].MiddleName" : "B",
          "Applicant-DV.APP.SSN" : "666010038",
          "Applicant-DV.APP.Age" : "41",
          "Applicant-DV.APP.GrossMthlyIncome" : "3000",
          "Applicant-DV.APP.MaritalStatus" : "D",
          "Applicant-DV.APP.EMP[1].EmployerName" : "EXPERIAN",
          "Application Data View.Payment Protection" : "N",
          "Application Data View.Product Details[1].Purpose of Loan" : "N",
          "Application Data View.Applicants[1].Cheque or Debit Card Held" : "Y",
          "Application Data View.Applicants[1].Time with Bank months" : "72",
          "Application Data View.Applicants[1].Time in Employment months" : "72",
          "Application Data View.Applicants[1].Residential Status" : "O"
      }
      """
    Then I verify that the expected decision for Results-DV.RSLT.Final Decision is Approved