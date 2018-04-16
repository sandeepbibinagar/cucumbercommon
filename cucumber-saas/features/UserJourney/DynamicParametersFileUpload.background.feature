# {# Skip this section when used as background
@skip
Feature: DP Upload from file

  Scenario: Dynamic Parameters Upload
    # End of section #}
    #Import Dynamic Parameters
    Given I start the browser
    And I go to login page
    And I login on Admin Portal with username stan.marsh@nabtest.example.com and password Password123
    And I go to WebEngine home page
    And I select menu System/Dynamic Parameter Maintenance on WebEngine home page
    And I import dynamic parameters from files:
      | Country-State | /UserJourney/data/Country-State-DP.csv |
    And I stop the browsers
