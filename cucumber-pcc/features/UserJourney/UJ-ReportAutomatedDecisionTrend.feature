@skip
Feature: Automated Decision Trend Report

  Scenario: UJ-Report-Automated Decision Trend Report
  # Test-ID: 5250512
  # Use-Case: UserJourney
  # Priority: P2

    When I set the base webservice url to ${bps.webservices.url}

    And I prepare REST request body:
    """

      {% include 'classpath:../UserJourney/data/CreateNewApplication.JSON' %}

    """
    And I add the following headers to the REST request:
      | Content-Type | application/json |
      | Accept       | application/json |

    And I prepare REST authentcation username admin and password Secret123!
    And I send a REST POST request to /v1/applications/TENANT1/CreditEvaluation and receive status code HTTP 200
    And I save JSON response matching expression $['data']['Results-DV.RSLT.Decision Date'] as variable appCreationDate

    And I start the browser
    And I go to login page
    And I login on Admin Portal with username adm@example.com and password Password123
    And I select solution - BI
    And I select test environment testenv11
    And I select report Automated Decision Trend
    And I change the format of date ${appCreationDate} from yyyyMMdd to MM/dd/yyyy and save it to variable filterDate
    And I set time interval to Date
    And I set From date to 3/1/2018
    And I set To date to ${filterDate}
    And I change the format of date ${appCreationDate} from yyyyMMdd to dd-MMM-yy and save it to variable tableDate
    Then I verify that the following data is displayed in the table:
      | Date      | DECLINE | ERROR | Total | DECLINE | ERROR  | Total  |
      | 16-Mar-18 |         | 4     | 4     |         | 100.0% | 100.0% |
    And I stop the browser