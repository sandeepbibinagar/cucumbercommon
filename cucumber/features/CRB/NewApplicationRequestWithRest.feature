Feature: New Application approval/denial through the REST api
  In order to obtain a decision for an applicant,
  As a CRB user I want to send a POST request with the applicant information to the application, and receive a response

  Scenario Outline:  J005 - As a Client User I want to CREATE an application through CLIENT SYSTEM so that I obtain a decision for the applicant
    When I send POST request to resource /v1/applications/TENANT1/21 with JSON file body <file> authenticated by username user and password User123@123
    Then I should receive a response from the application with status text OK and status code 200
      And I should see that the expected decision for Application Data View.System Decision is <decision>

    Examples:
      | file                    | decision |
      | rita.json               | Accept   |

  Scenario: J005 - As a Client User I want to CREATE an application through CLIENT SYSTEM so that I obtain a decision for the applicant
    When I send POST request to resource /v1/applications/TENANT1/21 authenticated by username user and password User123@123 with body:
      """
      {
	"Application Data View.Channel" : "01",
	"Application Data View.Country Code" : "GB",
	"Application Data View.Currency" : "EUR",
	"Application Data View.Number of Applicants" : "1",
	"Application Data View.Master Product" : "PL_02",
	"Application Data View.Product Details[1].Requested Balance Transfer" : "0",
	"Application Data View.Product Details[1].Requested Balance Transfer Amount" : "0",
	"Application Data View.Product Details[1].Owners Full Name" : "Rita McIver",
	"Application Data View.Product Details[1].Owners Embossed Name" : "Rita McIver",
	"Application Data View.Product Details[1].Secured Collateral Type" : "Vehicle",
	"Application Data View.Product Details[1].Secured Collateral Value" : "10000",
	"Application Data View.Product Details[1].Secured Collateral Description" : "Description of the Vehicule",
	"Application Data View.Product Details[2].Requested Amount" : "15000",
	"Application Data View.Product Details[2].Requested Term" : "36",
	"Application Data View.Product Details[2].Purpose of Loan" : "HI",
	"Application Data View.Applicant[1].Applicant Type" : "1",
	"Application Data View.Applicant[1].Applicant Identifier" : "1",
	"Application Data View.Applicant[1].Title" : "Mrs",
	"Application Data View.Applicant[1].Forename" : "Rita",
	"Application Data View.Applicant[1].Surname" : "McIver",
	"Application Data View.Applicant[1].Date Of Birth" : "19860220",
	"Application Data View.Applicant[1].Gender" : "1",
	"Application Data View.Applicant[1].Nationality" : "GB",
	"Application Data View.Applicant[1].DocumentId Number" : "A455",
	"Application Data View.Applicant[1].DocumentId Type" : "03",
	"Application Data View.Applicant[1].DocumentId Expiry Date" : "20160606",
	"Application Data View.Applicant[1].Education" : "03",
	"Application Data View.Applicant[1].Marital Status" : "02",
	"Application Data View.Applicant[1].Relationship to Applicant" : "1",
	"Application Data View.Applicant[1].Number of Dependants" : "2",
	"Application Data View.Applicant[1].Special Applicant Flag" : "N",
	"Application Data View.Applicant[1].Home Phone Number" : "457875678999",
	"Application Data View.Applicant[1].Personal Mobile Number" : "9036555555",
	"Application Data View.Applicant[1].Employer Phone Number" : "1234567",
	"Application Data View.Applicant[1].Work Mobile Phone Number" : "087978999",
	"Application Data View.Applicant[1].Email Address" : "Rita@gmail.comcom",
	"Application Data View.Applicant[1].Contact Method" : "3",
	"Application Data View.Applicant[1].Residential Status" : "01",
	"Application Data View.Applicant[1].Employer Name" : "IT technology Ltd",
	"Application Data View.Applicant[1].Employment Status" : "01",
	"Application Data View.Applicant[1].Occupation" : "06",
	"Application Data View.Applicant[1].Years With Employer" : "5",
	"Application Data View.Applicant[1].Months With Employer" : "5",
	"Application Data View.Applicant[1].Monthly Income Before Taxes" : "6000",
	"Application Data View.Applicant[1].Other Income Before Taxes" : "0",
	"Application Data View.Applicant[1].Monthly Income After Taxes" : "4500",
	"Application Data View.Applicant[1].Existing Monthly Outgoings" : "150",
	"Application Data View.Applicant[1].Years With Bank" : "3",
	"Application Data View.Applicant[1].Months With Bank" : "2",
	"Application Data View.Applicant[1].Total Annual Income" : "72500",
	"Application Data View.Applicant[1].IBAN" : "GB57 BIBA 0000 0312 3456 72",
	"Application Data View.Applicant[1].Bank Name" : "CashBank",
	"Application Data View.Applicant[1].Existing Customer Flag" : "1",
	"Application Data View.Applicant[1].Number of Applications L3M" : "0",
	"Application Data View.Applicant[1].Existing Customer.Existing  Customer Number" : "1008877963",
	"Application Data View.Applicant[1].Start date of employment" : "20020101",
	"Application Data View.Applicant[1].Do you pay alimony" : "N",
	"Application Data View.Applicant[1].Do you have a Car" : "N",
	"Application Data View.Applicant[1].Previous Loan Exists" : "N",
	"Application Data View.Applicant[1].Number of Credit Cards" : "0",
	"Application Data View.Applicant[1].Address[1].Address Number 1" : "1",
	"Application Data View.Applicant[1].Address[1].Address Line 1" : "Mill Crescent",
	"Application Data View.Applicant[1].Address[1].Address Line 2" : "London",
	"Application Data View.Applicant[1].Address[1].Address Country" : "GBR",
	"Application Data View.Applicant[1].Address[1].Address Postcode" : "WC10 4MX",
	"Application Data View.Applicant[1].Address[1].Contact Address Flag" : "N",
	"Application Data View.Applicant[1].Address[1].Years At Address" : "10",
	"Working Storage.Master product" : "PL254B3",
	"Working Storage.Product Array for Campaign Code" : "MG_01;PL_01;PL_02;CC_01;CC_02"
}
      """
    Then I should receive a response from the application with status text OK and status code 200
      And I should see that the expected decision for Application Data View.System Decision is Accept