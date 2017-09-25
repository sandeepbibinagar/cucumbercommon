Feature: New Application for Unsecured Personal Loans Declined through the REST api
  In order to obtain a decision for an applicant,
  As a ACF user
  I want to send a POST request with the applicant information to the application and receive a response

  Scenario: NA006 As a User I want to CREATE an application through CLIENT SYSTEM to get final decision Decline when Pre bureau decision is Decline
    When I send POST request to /v1/applications/TENANT1/CreditEvaluation with username admin and password Secret123! and receive status code HTTP 200:
      """
      {
      "Applicant-DV.APP.NME[1].FirstName" : "GLORIA",
      "Applicant-DV.APP.NME[1].LastName" : "LEBRATO",
      "Applicant-DV.APP.NME[1].MiddleName" : "M",
      "Applicant-DV.APP.SSN" : "666010020",
      "Applicant-DV.APP.Age" : "17",
      "Applicant-DV.APP.GrossMthlyIncome" : "4000",
      "Applicant-DV.APP.MaritalStatus" : "S",
      "Applicant-DV.APP.EMP[1].EmployerName" : "EXPERIAN",
      "Application Data View.Payment Protection" : "Y",
      "Application Data View.Product Details[1].Purpose of Loan" : "D",
      "Application Data View.Applicants[1].Cheque or Debit Card Held" : "N",
      "Application Data View.Applicants[1].Time with Bank months" : "11",
      "Application Data View.Applicants[1].Time in Employment months" : "11",
      "Application Data View.Applicants[1].Residential Status" : "U"
      }
      """
    Then I verify that the expected decision for Results-DV.RSLT.Pre-B-Policy-Decision-Text is Decline