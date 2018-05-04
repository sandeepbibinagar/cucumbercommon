Feature: User Journey - Create Credit Card TP via REST

  Background:
    # {% include 'classpath:../UserJourney/TacticalParametersFileUpload.background.feature' %}

  Scenario: Create Credit Card TP via REST to get system decision Accept
    # Test-ID: 3933888
    # Use-Case: UserJourney
    # Priority: P2 - High
    When I update parameter Product_TP - TP - Product_TP Search description: Product Type ,effective from: 12/01/2017 to
      | Product Type | Interest Rate | Min Age | Max Age | Min Credit Limit | Max Credit Limit |
      |              | 5.0           | 18      | 60      | 100              | 10000            |
      |              | 4.12          | 18      | 60      | 100              | 10000            |
    And I deploy tactical parameter Product_TP - TP - Product_TP Search version LATEST

    Then I set the base webservice url to ${bps.webservices.url}
    And I prepare JWT token with user ${tactical.parameters.api.user} and password ${tactical.parameters.api.password} from service ${token.service.url}/v1/tokens/create
    And I prepare REST request body from file ${features.path}/UserJourney/data/CreateNewApplication.JSON
    And I add the following headers to the REST request:
      | Content-Type | application/json |
      | Accept       | application/json |

    And I send a REST POST request to /v0/applications/CreditEvaluation and receive status code HTTP 200
    Then I verify that the JSON response has fields:
      | $.data.['Results-DV.RSLT.Pst-B-Policy-Decision-Text'] | Accept |
