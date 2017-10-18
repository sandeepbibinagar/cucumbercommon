@execute
Feature: New Application for Unsecured Personal Loans Accepted through the REST api
  In order to obtain a decision for an applicant,
  As a ACF user
  I want to send a POST request with the applicant information to the application and receive a response

  Scenario: NA002 As a User I want to CREATE an application through CLIENT SYSTEM to get final decision Accept when Pre bureau decision is Refer
    # Test-ID: 4056743
    When I set the base webservice url to ${base.webservices.url}
    And I prepare REST request body:
      """
      {
          "Applicant-DV.APP.NME[1].FirstName" : "BERK",
          "Applicant-DV.APP.NME[1].LastName" : "SHARON",
          "Applicant-DV.APP.NME[1].MiddleName" : "B",
          "Applicant-DV.APP.NME[1].Suffix" : "DR",
          "Applicant-DV.APP.SSN" : "666010038",
          "Applicant-DV.APP.Age" : 41,
          "Applicant-DV.APP.GrossMthlyIncome" : 3400,
          "Applicant-DV.APP.MaritalStatus" : "D",
          "Application-DV.Payment Protection" : "N",
          "Application-DV.Product Details[1].Purpose of Loan" : "N",
          "Application-DV.Applicant.Cheque or Debit Card Held" : "Y",
          "Application-DV.Applicant.Time with Bank Years" : 6,
          "Applicant-DV.APP.EMP[1].YearsWithEmployer" : 6,
          "Application-DV.Applicant.Residential Status" : "O",
          "Application-DV.Source" : "Branch",
          "Application-DV.IndividualJoint" : "N",
	      "Applicant-DV.APP.EMP[1].EmployerName" : "EXPERIAN",
          "Applicant-DV.APP.ADD[1].Address Line 1" : "601 Experian Parkway",
	      "Applicant-DV.APP.ADD[1].City" : "ANNAPOLIS",
	      "Applicant-DV.APP.ADD[1].State" : "MD",
	      "Applicant-DV.APP.ADD[1].Postcode" : "2010",
	      "Applicant-DV.APP.ADD[1].YearsAtAddress" : 5,
	      "Applicant-DV.APP.CONT[1].PhoneNumber" : "123456",
	      "Applicant-DV.APP.CONT[1].EmailAddress" : "berk.sharon@com",
	      "Applicant-DV.APP.ADD[1].HouseNumber" : "993",
	      "Application-DV.Product Details[1].Requested Amount":15000,
          "Application-DV.Product Details[1].Requested Term":100
      }
      """
    And I add the following headers to the REST request:
      | Content-Type | application/json |
      | Accept       | application/json |
    And I prepare REST authentcation username admin and password Secret123!
    And I send a REST POST request to /v1/applications/TENANT1/CreditEvaluation and receive status code HTTP 200
    And I verify that the JSON response has fields:
      | $.data.['Results-DV.RSLT.Pst-B-Policy-Decision-Text'] | Accept |