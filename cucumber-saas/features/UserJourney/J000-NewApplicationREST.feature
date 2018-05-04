Feature: User Journey - New Application via REST

  Background:
    # {% include 'classpath:../UserJourney/TacticalParametersFileUpload.background.feature' %}

  Scenario: Create Application via REST to get system decision Accept
    # Test-ID: 4570749
    # Use-Case: UserJourney
    # Priority: P1 - Critical
    When I update tactical parameters from file ${features.path}/UserJourney/data/UserJourney_ACF_TP_Export.xml

    And I deploy tactical parameter TreatmentTree_CreditLimit_Offer_TP - TP - TreatmentTree_CreditLimit Search version LATEST
    And I deploy tactical parameter Bureau_Enabler_TP - TP - Bureau Enabler version LATEST
    And I deploy tactical parameter Scorecard_Generic_UL_TP - TP_Scorecard_Generic_UL Search version LATEST
    And I deploy tactical parameter MiddleName_Mandatory - TP - Middle Name Mandatory version LATEST
    And I deploy tactical parameter PolicyRules_TP - TP - PolicyRules Search version LATEST
    And I deploy tactical parameter Product_TP - TP - Product_TP Search version LATEST
    And I deploy tactical parameter WorkflowTP - TP - Workflow Search version LATEST
    And I deploy tactical parameter ExpCons_TP - TP - ExpCons_TP Search version LATEST
    And I deploy tactical parameter EquCons_TP - TP - EquCons_TP Search version LATEST

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
