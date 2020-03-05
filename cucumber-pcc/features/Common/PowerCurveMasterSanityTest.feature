Feature: PowerCurve master branch - sanity test

  Scenario: Ping Web Engine REST API (menu API) to ensure Web Engine is up and running
    # Test-ID: 6794677
    # Type: Sanity
    # Use-Case: Sanity
    # Priority: P1 - Critical
    When I prepare JWT token with user ${tactical.parameters.api.user} and password ${tactical.parameters.api.password} from service ${token.service.url}/v1/tokens/create
    And I set the base webservice url to ${webengine.base.url}
    And I send a REST GET request to /api/menu and receive status code HTTP 200
    Then I verify that the JSON response has fields:
      | $.content_type | menu |
