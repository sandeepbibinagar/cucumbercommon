Feature: User Journey - Update Credit Card TP via REST

  Background:
    # {% include 'classpath:../UserJourney/TacticalParametersFileUpload.background.feature' %}

  Scenario: Update Credit Card TP via REST to get system decision Accept
    # Test-ID: 3933890
    # Use-Case: UserJourney
    # Priority: P2 - High
    When I update parameter Product_TP - TP - Product_TP Search description: Product Type ,effective from: 12/01/2017 to
      | Product Type | Interest Rate | Min Age | Max Age | Min Credit Limit | Max Credit Limit |
      |              | 4.12          | 18      | 60      | 100              | 10000            |
    And I deploy tactical parameter Product_TP - TP - Product_TP Search version LATEST

    Then I set the base webservice url to ${bps.webservices.url}
    And I prepare JWT token with user adm@example.com and password Password123 from service ${token.service.url}/v1/tokens/create
    And I prepare REST request body:
        """
        {
			"Applicant-DV.APP.NME[1].Suffix" : "Mrs",
			"Applicant-DV.APP.NME[1].FirstName" : "GLORIA",
			"Applicant-DV.APP.NME[1].LastName" : "LEBRATO",
			"Applicant-DV.APP.SSN" : "666010020",
			"Applicant-DV.APP.DateofBirth" : "19801020",
			"Application-DV.IndividualJoint" : "Single",
			"Application-DV.Product Details[1].Requested Amount" : "10000",
			"Application-DV.Product Details[1].Requested Term" : "12",
			"Application-DV.Payment Protection" : "N",
			"Application-DV.Product Details[1].Bank Current Account Holder" : "N",
			"Application-DV.Applicant.Time with Bank Years" : "11",
			"Application-DV.Product Details[1].Online Banking Account Holder" : "N",
			"Application-DV.AccountNumber" : "123",
			"Application-DV.Applicant.Cheque or Debit Card Held" : "N",
			"Application-DV.Credit Card Number" : "123",
			"Application-DV.Product Details[1].Membership Number" : "123",
			"Applicant-DV.APP.ADD[1].Address Line 1" : "1148 SMITH ST",
			"Applicant-DV.APP.ADD[1].HouseNumber" : "12",
			"Applicant-DV.APP.ADD[1].City" : "MAXWELL AFB",
			"Applicant-DV.APP.ADD[1].State" : "AL",
			"Applicant-DV.APP.ADD[1].Postcode" : "361131510",
			"Applicant-DV.APP.ADD[1].YearsAtAddress" : "11",
			"Application-DV.Applicant.Residential Status" : "O",
			"Applicant-DV.APP.CONT[1].EmailAddress" : "a@b.com",
			"Applicant-DV.APP.CONT[1].PhoneNumber" : "9725551212",
			"Applicant-DV.APP.MaritalStatus" : "S",
			"Application-DV.Product Details[1].Purpose of Loan" : "D",
			"Applicant-DV.APP.GrossMthlyIncome" : "1000",
			"Application-DV.Product Details[1].Monthly Installment Amount" : "0",
			"Application-DV.Product Details[1].Other Monthly Expenses" : "0",
			"Applicant-DV.APP.EMP[1].EmployerName" : "EXPERIAN",
			"Applicant-DV.APP.EMP[1].YearsWithEmployer" : "11",
			"Application-DV.Product Details[1].Privacy Agreement Flag" : "N"
		}
        """
    And I add the following headers to the REST request:
      | Content-Type | application/json |
      | Accept       | application/json |

    And I send a REST POST request to /v0/applications/CreditEvaluation and receive status code HTTP 200
    Then I verify that the JSON response has fields:
      | $.data.['Results-DV.RSLT.Pst-B-Policy-Decision-Text'] | Accept |
      | $.data.['Application-DV.APR']                         | 4.120  |
