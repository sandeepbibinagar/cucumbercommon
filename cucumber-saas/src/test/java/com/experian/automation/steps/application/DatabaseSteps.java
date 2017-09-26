package com.experian.automation.steps.application;

import com.experian.automation.WebClient;
import com.experian.automation.logger.Logger;
import com.experian.automation.helpers.DBRuntime;
import cucumber.api.java.en.And;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import java.io.File;
import java.util.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Created by b04342a on 9/8/2017.
 */
public class DatabaseSteps {
    private final WebClient webClient;
    private final DBRuntime dbRuntime;
    private final Logger logger = Logger.getLogger(this.getClass());

    public DatabaseSteps(WebClient webClient, DBRuntime dbRuntime){
        this.webClient = webClient;
        this.dbRuntime = dbRuntime;
    }

    /*
    And I execute update sql query DELETE FROM INTERFACEFILES
     */
    @And("^I execute update sql query (.*)$")
    public void updateQuery(String query) throws Throwable {
        logger.info("Executing sql query: " + query);
        int affectedRecords = dbRuntime.update(query, new HashMap<>());
        logger.info("Query affected " + affectedRecords + " record(s)");
    }


    /*
    And I execute select sql query select ID from TEAM where NAME in ('Unassigned','Team1','Team2','Team3') and save results:
      | index | variable   |
      | 1     | Unassigned |
      | 2     | Team1      |
      | 3     | Team2      |
      | 4     | Team3      |
     */
    @And("^I execute select sql query (.*) and save results:$")
    public void selectQuerySaveResults(String query, List<List<String>> dataTable) throws Throwable {

        logger.info("Executing sql query: " + query);
        List<String> result = dbRuntime.queryForObjects(query, new HashMap<>(), String.class);
        logger.info("Query result: " + result.toString());

        for (int i = 1; i < dataTable.size(); i++) {
            List<String> dataTableRow = dataTable.get(i);

            int rowIndex = Integer.parseInt(dataTableRow.get(0)) - 1;
            String value = result.get(rowIndex);
            String varName = dataTableRow.get(1);

            logger.info("Set step data " + varName + " = " + value);
            webClient.setStepData(varName, value);
        }
    }

    /*
    And I execute select sql query select ID from USERS where LOGINNAME='leader1' and save result as leader1 variable
     */
    @And("^I execute select sql query (.*) and save result as (.*) variable$")
    public void selectQuerySaveResult(String query, String varName) throws Throwable {
        selectQuerySaveResultForRow(query, 1, varName);
    }


    /*
    And I execute select sql query select ID from TEAM and save 1st result as Unassigned variable
     */
    @And("^I execute select sql query (.*) and save (\\d+)(?:st|nd|rd|th) result as (.*) variable$")
    public void selectQuerySaveResultForRow(String query, int resIndex, String varName) throws Throwable {

        logger.info("Executing sql query: " + query);
        List<String> result = dbRuntime.queryForObjects(query, new HashMap<>(), String.class);
        logger.info("Query result: " + result.toString());

        String value = result.get(resIndex - 1);

        logger.info("Set step data " + varName + " = " + value);
        webClient.setStepData(varName, value);
    }


    /*
    And I execute select sql queries and save results:
      | query                                             | variable   |
      | select ID from USERS where LOGINNAME='collector3' | collector3 |
      | select ID from TEAM where NAME='Unassigned'       | Unassigned |
      | select ID from TEAM where NAME='Team1'            | Team1      |
      | select ID from TEAM where NAME='Team2'            | Team2      |
      | select ID from TEAM where NAME='Team3'            | Team3      |
    */
    @And("^I execute select sql queries and save results:$")
    public void selectQueriesSaveResults(List<List<String>> dataTable) throws Throwable {

        for (int i = 1; i < dataTable.size(); i++) {
            List<String> dataTableRow = dataTable.get(i);
            selectQuerySaveResult(dataTableRow.get(0), dataTableRow.get(1));
        }
    }

    /*
    And I execute select sql query select USERID, ACTIONID, count(LOGTIME) from AUDITACTIONS where USERID in ('${tallymanCollector}','${leader1}','${collector1}','${leader2}','${collector2}') group by USERID, ACTIONID and verify results for:
      | ${tallymanCollector} | 1 | 1 |
      | ${leader1}           | 1 | 1 |
      | ${collector1}        | 1 | 1 |
      | ${leader2}           | 1 | 1 |
      | ${collector2}        | 1 | 1 |
     */
    @And("^I execute select sql query (.*) and verify results for:$")
    public void selectQueryVerifyResults(String query, List<List<String>> dataTable) throws Throwable {

        //query = DataTransformer.transformSingleValue(query, xfat.getContextVariables());
        //dataTable = DataTransformer.transformTable(dataTable, xfat.getContextVariables());

        logger.info("Executing sql query: " + query);
        List<List<Object>> result = dbRuntime.queryForDataTable(query, new HashMap<>());


        logger.info("Query result: " + result.toString());


        assertEquals(result.size(), dataTable.size(), "Query result row number");

        //Get data type for each result column
        List<Class> dataTypes = new ArrayList<>();
        for (int columnIndex = 0; columnIndex < result.get(0).size(); columnIndex++) {
            boolean notNullValueFound = false;
            for (int rowIndex = 0; !notNullValueFound && rowIndex < result.size(); rowIndex++) {
                if (result.get(rowIndex).get(columnIndex) != null) {
                    dataTypes.add(result.get(rowIndex).get(columnIndex).getClass());
                    notNullValueFound = true;
                }
            }
            if (!notNullValueFound) {
                dataTypes.add(null);
            }
        }
        List<List<Object>> expectedResult = dbRuntime.transformTable(dataTable, dataTypes);

        for (List<Object> expectedRow : expectedResult) {
            Boolean found = false;
            for (int i = 0; !found && i < result.size(); i++) {
                List<Object> actualRow = result.get(i);
                List<Object> formattedActualRow = new ArrayList<>(actualRow.size());
                for(Object element: actualRow) {
                    if (element instanceof String) {
                        formattedActualRow.add(dbRuntime.transformValue((String)element, element.getClass()));
                    } else {
                        formattedActualRow.add(element);
                    }
                }

                if (formattedActualRow.equals(expectedRow)) {
                    result.remove(actualRow);
                    found = true;
                }
            }
            assertTrue(found, "Query result row " + expectedRow + " found");
        }
    }


    /*
    And I execute select sql query SELECT TD.TRANSACTION_MONEY, TD.TRANSACTION_DECIMAL FROM XFAT_TRANSACTION_DETAILS TD order by TRANSACTION_DETAILS_ID DESC and verify first 1 result for:
      | 123.54600 | 123.54556 |
     */
    @And("^I execute select sql query (.*) and verify first (\\d+) result(?:s)? for:$")
    public void selectQueryVerifyResultsForFirstRows(String query,int rowsCount, List<List<String>> dataTable) throws Throwable {
        switch(webClient.config.get("database.platform")){
            case "Oracle": query = "select * from (" + query + ") where rownum <= " + rowsCount;
                break;
            case "Postgre": query = query + " LIMIT " + rowsCount;
                break;
            default:  query = query.replaceAll("(?i)select", "select TOP " + rowsCount);
        }
        /*if(webClient.config.get("database.platform").equals("Oracle")) {
            query = "select * from (" + query + ") where rownum <= " + rowsCount;
        }
        else {
            query = query.replaceAll("(?i)select", "select TOP " + rowsCount);
        }*/
        selectQueryVerifyResults(query, dataTable);
    }



    /*
    And I create records in table CURRENCYRATES:
      | FROMCURRENCYID | TOCURRENCYID | CONVERSIONFACTOR  | STARTDATEEFFECTIVE |
      | 'USD'          | 'CLF'        | '0.025415862241'  | '01-JAN-15'        |
      | 'CLF'          | 'USD'        | '39.345507560974' | '01-JAN-15'        |
      | 'CLF'          | 'CLP'        | '24875.9316201'   | '01-JAN-15'        |
      | 'CLP'          | 'CLF'        | '0.000040436917'  | '01-JAN-15'        |
      | 'USD'          | 'CLP'        | '605.40957215774' | '01-JAN-15'        |
      | 'CLP'          | 'USD'        | '0.0016511774346' | '01-JAN-15'        |
     */
    @And("^I create records in table (.*):$")
    public void createRecords(String tableName, List<List<String>> dataTable) throws Throwable {

        //dataTable = DataTransformer.transformTable(dataTable, xfat.getContextVariables());

        String tableColumns = StringUtils.join(dataTable.get(0), ',');
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO ");
        query.append(tableName);
        query.append(" (");
        query.append(tableColumns);
        query.append(") VALUES (");

        for (int i = 0; i < dataTable.get(0).size(); i++) {
            query.append(":param");
            query.append(i + 1);
            if (i < dataTable.get(0).size() - 1) {
                query.append(", ");
            } else {
                query.append(")");
            }
        }

        for (int i = 1; i < dataTable.size(); i++) {
            Map<String, Object> params = new LinkedHashMap<>();
            for (Integer paramIndex = 0; paramIndex < dataTable.get(i).size(); paramIndex++) {
                params.put("param" + (paramIndex + 1), dbRuntime.transformValue(dataTable.get(i).get(paramIndex)));
            }
            logger.info("Executing sql query: " + query.toString() + " with parameters " + params);

            assertEquals(dbRuntime.update(query.toString(), params), 1, "Created records");
        }
    }

    /*
    And I update records using query from file with path: ${myVariable}/myfolder/myFile.ext
    */
    @And("^I update records using query from file with path: (.*)$")
    public void createRecordsUsingFile(String filePath) throws Throwable {

        //filePath = FilenameUtils.separatorsToUnix(DataTransformer.transformSingleValue(filePath, xfat.getContextVariables()));
        File queryFile = new File(filePath);

        List<String> queries = FileUtils.readLines(queryFile,"UTF-8");

        for(String query : queries)
        {
            logger.info("Executing query from file: " + query);
            dbRuntime.update(query, new HashMap<>());
        }
    }

    /*
     And I update records using query UPDATE CONTACTPHONES SET UUID = :param1 WHERE ID = 1 with data:
      | param1                               |
      | 2f2d3c3f-52ab-46bb-9c8c-dab4f650cbab |
     */
    @And("^I update records using query (.*) with data:$")
    public void updateRecords(String query, List<List<String>> dataTable) throws Throwable {

        //dataTable = DataTransformer.transformTable(dataTable, xfat.getContextVariables());

        for (int i = 1; i < dataTable.size(); i++) {
            Map<String, Object> params = new LinkedHashMap<>();
            for (Integer paramIndex = 0; paramIndex < dataTable.get(i).size(); paramIndex++) {
                params.put(dataTable.get(0).get(paramIndex), dbRuntime.transformValue(dataTable.get(i).get(paramIndex)));
            }
            logger.info("Executing sql query: " + query + " with parameters " + params);
            assertEquals(dbRuntime.update(query, params), 1, "Updated records");
        }
    }

    @And("^I execute query (.*) for (.*) seconds and verify the results number is equal (.*)$")
    public void heartBeatForRecords(String query, Integer timeOut, Integer resultsNum) throws Throwable {
        long endTime = System.currentTimeMillis() + (timeOut * 1000);

        Integer rowCount = (Integer) dbRuntime.queryForObject(query, new HashMap<String, Object>(), Integer.class);

        while(rowCount<resultsNum && System.currentTimeMillis()<=endTime){
            rowCount = (Integer) dbRuntime.queryForObject(query, new HashMap<String, Object>(), Integer.class);
            Thread.sleep(500);
        }
        assertEquals(resultsNum,rowCount,"Expected row number "+resultsNum+ " is euqal to acutal row number"+ rowCount);
    }
}
