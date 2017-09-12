@db
Feature: DBRuntime integration test feature

  Scenario: DBRuntime integration test scenario
    And I execute select sql query SELECT forename, surname FROM eda_tenant1.applicants WHERE ids_applicants=1 and verify results for:
      | Richard | Hendges |