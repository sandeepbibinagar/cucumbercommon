Feature: User Journey - Update Policy Rule TP via REST

  Background:
    # {% include 'classpath:../UserJourney/TacticalParametersFileUpload.background.feature' %}

  Scenario: Turn off Policy Rules TP via REST to get System Decision Empty
    # Test-ID: 3933869
    # Use-Case: UserJourney
    # Priority: P2 - High
    When I update parameter PolicyRules_TP - TP - PolicyRules Search description: PolicyRules ,effective from: 12/01/2017 to
      | Input | Execute Policy Rules |
      |       | N                    |
    And I deploy tactical parameter PolicyRules_TP - TP - PolicyRules Search version LATEST

    Then I set the base webservice url to ${bps.webservices.url}
    And I prepare JWT token with user ${tactical.parameters.api.user} and password Password123 from service ${token.service.url}/v1/tokens/create
    And I prepare REST request body from file ${features.path}/UserJourney/data/CreateNewApplication.JSON
    And I add the following headers to the REST request:
      | Content-Type | application/json |
      | Accept       | application/json |

    And I send a REST POST request to /v0/applications/CreditEvaluation and receive status code HTTP 200
    Then I verify that the JSON response has fields:
      | $.data.['Application-DV.Product Details[1].Decision Results[1].Post-Bureau Risk Scorecard.Score'] | 684  |
      | $.data.['Results-DV.RSLT.Pst-B-Policy-Decision-Text']                                             | null |
