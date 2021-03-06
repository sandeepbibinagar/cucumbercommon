Feature: User Journey - Update Treatment Tree TP via REST

  Background:
    # {% include 'classpath:../UserJourney/TacticalParametersFileUpload.background.feature' %}

  Scenario: Update Treatment Tree Credit Limit Offer TP via REST to get system decision Accept
    # Test-ID: 3933872
    # Use-Case: UserJourney
    # Priority: P2 - High
    When I update parameter TreatmentTree_CreditLimit_Offer_TP - TP - TreatmentTree_CreditLimit Search description: TreatmentTree CreditLimit ,effective from: 12/01/2017 to
      | Input | CreditLimit Offer |
      |       | 1                 |
    And I deploy tactical parameter TreatmentTree_CreditLimit_Offer_TP - TP - TreatmentTree_CreditLimit Search version LATEST

    Then I set the base webservice url to ${bps.webservices.url}
    And I prepare JWT token with user ${tactical.parameters.api.user} and password ${tactical.parameters.api.password} from service ${token.service.url}/v1/tokens/create
    And I prepare REST request body from file ${features.path}/UserJourney/data/CreateNewApplication.JSON
    And I add the following headers to the REST request:
      | Content-Type | application/json |
      | Accept       | application/json |

    And I send a REST POST request to /v0/applications/CreditEvaluation and receive status code HTTP 200
    Then I verify that the JSON response has fields:
      | $.data.['Application-DV.Product Details[1].Decision Results[1].Post-Bureau Risk Scorecard.Score'] | 684    |
      | $.data.['Results-DV.RSLT.Pst-B-Policy-Decision-Text']                                             | Accept |
      | $.data.['Results-DV.RSLT.CL-Maximum-Limit']                                                       | 8000   |
