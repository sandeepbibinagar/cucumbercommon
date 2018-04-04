# {# Skip this section when used as background
@skip
Feature: TP & DP Upload from file

  Scenario: Tactical Parameters Upload and Deploy
    # End of section #}
    Given I update tactical parameters from file ${features.path}/UserJourney/data/UserJourney_ACF_TP_Export.xml
    And I deploy tactical parameter TreatmentTree_CreditLimit_Offer_TP - TP - TreatmentTree_CreditLimit Search version LATEST
    And I deploy tactical parameter Bureau_Enabler_TP - TP - Bureau Enabler version LATEST
    And I deploy tactical parameter Scorecard_Generic_UL_TP - TP_Scorecard_Generic_UL Search version LATEST
    And I deploy tactical parameter MiddleName_Mandatory - TP - Middle Name Mandatory version LATEST
    And I deploy tactical parameter PolicyRules_TP - TP - PolicyRules Search version LATEST
    And I deploy tactical parameter Product_TP - TP - Product_TP Search version LATEST
    And I deploy tactical parameter WorkflowTP - TP - Workflow Search version LATEST
    And I deploy tactical parameter ExpCons_TP - TP - ExpCons_TP Search version LATEST
    And I deploy tactical parameter EquCons_TP - TP - EquCons_TP Search version LATEST

    #Import Dynamic Parameters
    And I start the browser
    And I go to login page
    And I login on Admin Portal with username adm@example.com and password Password123
    And I go to WebEngine home page
    And I select menu System/Dynamic Parameter Maintenance on WebEngine home page
    And I import dynamic parameters from files:
      | Country-State | /UserJourney/data/Country-State-DP.csv |
    And I stop the browsers





