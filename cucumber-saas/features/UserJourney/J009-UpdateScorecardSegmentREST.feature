Feature: User Journey - Update Scorecard TP via REST

  Background:
    # {% include 'classpath:../UserJourney/TacticalParametersFileUpload.background.feature' %}

  Scenario: Update Age gt 36 Score of Scorecard Generic UL TP via REST to get system decision Accept
    # Test-ID: 3887940
    # Use-Case: UserJourney
    # Priority: P2 - High
    When I update parameter Scorecard_Generic_UL_TP - TP_Scorecard_Generic_UL Search description: Scorecard Generic UL ,effective from: 12/01/2017 to
      | Input | Age lt 20 Score | Age gt 21 lt 25 Score | Age gt 26 lt 30 Score | Age gt 31 lt 35 Score | Age gt 36 Score | Initial Score | Transformation Score |
      |       | -115            | -20                   | 0                     | 30                    | 100             | 600           | 1                    |
    And I deploy tactical parameter Scorecard_Generic_UL_TP - TP_Scorecard_Generic_UL Search version LATEST

    Then I set the base webservice url to ${bps.webservices.url}
    And I prepare JWT token with user ${tactical.parameters.api.user} and password ${tactical.parameters.api.password} from service ${token.service.url}/v1/tokens/create
    And I prepare REST request body from file ${features.path}/UserJourney/data/CreateNewApplication.JSON
    And I add the following headers to the REST request:
      | Content-Type | application/json |
      | Accept       | application/json |

    And I send a REST POST request to /v0/applications/CreditEvaluation and receive status code HTTP 200
    Then I verify that the JSON response has fields:
      | $.data.['Application-DV.Product Details[1].Decision Results[1].Post-Bureau Risk Scorecard.Score'] | 734    |
      | $.data.['Results-DV.RSLT.Pst-B-Policy-Decision-Text']                                             | Accept |
