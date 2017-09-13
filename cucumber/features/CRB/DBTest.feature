@db
Feature: DBRuntime integration test feature

  Scenario: DBRuntime integration test scenario - SELECT
    And I execute select sql query SELECT label ,paramkey from eda_tenant1.eda_parameter order by label ASC and verify first 3 result for:
      | OV02 | Adverse data found after application was processed |
      | AF   | Afghanistan                                        |
      | AL   | Albania                                            |
    And I execute select sql query SELECT forename, surname FROM eda_tenant1.applicants WHERE ids_applicants=1 and verify results for:
      | Richard | Hendges |
    And I execute select sql query SELECT paramkey from eda_tenant1.eda_parameter where label = 'Albania' and save result as saved_param variable
    And I execute select sql query SELECT label from eda_tenant1.eda_parameter order by label ASC LIMIT 3 and save results:
      | index | variable   |
      | 1     | paramKey1  |
      | 2     | paramKey2  |
      | 3     | paramKey3  |
    And I execute select sql query SELECT label from eda_tenant1.eda_parameter order by label ASC LIMIT 3 and save 1st result as paramKey1 variable
    And I execute select sql queries and save results:
      | query                                                                 | variable       |
      | SELECT enabled from eda_tenant1.eda_parameter WHERE label = 'Title'   | titleEnabled   |
      | SELECT enabled from eda_tenant1.eda_parameter WHERE label = 'Country' | countryEnabled |
      | SELECT enabled from eda_tenant1.eda_parameter WHERE label = 'Gender'  | genderEnabled  |

  Scenario: DBRuntime integration test scenario - UPDATE
    And I execute update sql query UPDATE eda_tenant1.applicants SET (forename, surname) = ('Rita', 'Skeeter') WHERE ids_applicants=2
    And I create records in table eda_tenant1.films:
      | code  | title    | did  | date_prod        | kind       |
      | bx34  | Die easy | 1    | DATE%15/05/2005% | 'comedy'   |
      | bx24  | Gossip   | 1    | DATE%15/05/2005% | 'thriller' |
    And I update records using query UPDATE eda_tenant1.applicants SET (forename, surname) = (:forename, :surname) WHERE ids_applicants=2 with data:
      | forename | surname |
      | Richard  | Grill   |
    And I execute query SELECT age_of_applicant from eda_tenant1.applicants where ids_applicants=2 for 2 seconds and verify the results number is equal 31
    And I update records using query from file with path: C://tmp/query.sql