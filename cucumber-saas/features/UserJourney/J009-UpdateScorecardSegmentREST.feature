Feature: Update Scorecard Tactical Parameters

  Background:
    # {% include 'classpath:../UserJourney/TacticalParametersFileUpload.background.feature' %}

  Scenario: As a User I want to update the "Age gt 36 Score" of Scorecard_Generic_UL  from 50 to 100, to get System Decision Accept
    # Test-ID: 3887940
    # Use-Case: UserJourney
    # Priority: P2
    When I update parameter Scorecard_Generic_UL_TP - TP_Scorecard_Generic_UL Search description: Scorecard Generic UL ,effective from: 12/01/2017 to
      | Input | Age lt 20 Score | Age gt 21 lt 25 Score | Age gt 26 lt 30 Score | Age gt 31 lt 35 Score | Age gt 36 Score | Initial Score | Transformation Score |
      |       | -115            | -20                   | 0                     | 30                    | 100             | 600           | 1                    |
    And I deploy tactical parameter Scorecard_Generic_UL_TP - TP_Scorecard_Generic_UL Search version LATEST

    Then I set the base webservice url to ${bps.webservices.url}
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

    And I prepare REST authentcation username admin and password Secret123!
    And I send a REST POST request to /v1/applications/TENANT1/CreditEvaluation and receive status code HTTP 200
    Then I verify that the JSON response has fields:
      | $.data.['Application-DV.Product Details[1].Decision Results[1].Post-Bureau Risk Scorecard.Score'] | 734    |
      | $.data.['Results-DV.RSLT.Pst-B-Policy-Decision-Text']                                             | Accept |
