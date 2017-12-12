Feature: New Application for Credit Card Referred through the REST api
	In order to obtain a decision for an applicant,
	As an User
	I want to send a POST request with the applicant information to the application and receive a response

	Scenario: As a User I want to CREATE an application through CLIENT SYSTEM to get final decision Accept when bureau decision is Refer
	  # Test-ID: 4570749
	  # Use-Case: UserJourney
	  # Priority: P1
		And I update tactical parameters from file ${features.path}/UserJourney/data/UserJourney_Tactical_Parameters_Export.xml

		And I deploy tactical parameter Bureau Parameter - Get Bureau Parameter version LATEST
		And I deploy tactical parameter Application Parameter - Get Application Parameter version LATEST
		And I deploy tactical parameter Product Reference Parameter - Get Product Reference Parameter version LATEST
		And I deploy tactical parameter Policy Config Parameter - Get Policy Conf Parameter version LATEST
		And I deploy tactical parameter Credit Card Parameters - Get Credit Card Parameters version LATEST
		And I deploy tactical parameter Scorecard Config Parameter - Get Scorecard Conf Parameter version LATEST
		And I deploy tactical parameter Treatment Conf Parameter - Get Treatment Config Parameter version LATEST

		When I set the base webservice url to ${bps.webservices.url}
		And I prepare REST request body:
        """
        {
         "Application Data View.Applicants[1].Surname" : "Adams",
         "Application Data View.Applicants[1].Forename" : "Cindy",
         "Application Data View.Applicants[1].Passport Or Identification No" : "A001",
         "Application Data View.Applicants[1].Title" : "Ms",
         "Application Data View.Applicants[1].Date Of Birth" : "19900107",
         "Application Data View.Proof Of Address Supplied" : "N" ,
         "Application Data View.Proof Of Income Supplied" : "N",
         "Application Data View.Payment Protection" : "N",
         "Application Data View.Joint Applicant Flag" : "N",
         "Application Data View.Staff Indicator" : "N",
         "Application Data View.VIP Indicator" : "N",
         "Application Data View.Product Details.Product" : "C"
        }
        """
		And I add the following headers to the REST request:
			| Content-Type | application/json |
			| Accept       | application/json |

		And I prepare REST authentcation username admin and password Secret123!
		And I send a REST POST request to /v1/applications/TENANT1/1 and receive status code HTTP 200
		And I verify that the JSON response has fields:
			| $.data.['Application Data View.Application Status'] | 4|
