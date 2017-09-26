package com.experian.automation.helpers;


import com.experian.automation.logger.Logger;
import com.google.common.collect.HashMultimap;
import oracle.jdbc.internal.OracleTypes;
import org.apache.commons.lang.StringUtils;
import org.apache.wink.json4j.OrderedJSONObject;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("unchecked")
public class DBOperations {

    private final Logger logger = Logger.getLogger(this.getClass());

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private SingleConnectionDataSource dataSource;

    public final static String dateOnlyFormat = "dd/MM/yyyy";
    public final static String dateOnlyFormatRegEx = "^\\d{2}\\/\\d{2}\\/\\d{4}$";

    public final static String dateTimeFormat = "dd/MM/yyyy HH:mm:ss.SSS";
    public final static String dateTimeFormatRegEx = "^\\d{2}\\/\\d{2}\\/\\d{4} [0-2][0-9]:[0-5][0-9]:[0-5][0-9].\\d{1,3}$";

    protected String driverClassName;
    protected String url;
    protected String userName;
    protected String password;

    public DBOperations() {
        driverClassName = "";
        url = "";
        userName = "";
        password = "";
    }

    private void initConnection() {

        if (!isConnectionActive()) {
            dataSource = new SingleConnectionDataSource();
            dataSource.setDriverClassName(driverClassName);
            dataSource.setUrl(url);
            dataSource.setUsername(userName);
            dataSource.setPassword(password);
            namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        }
    }

    private boolean isConnectionActive() {

        if (dataSource == null) {
            return false;
        } else {
            try {
                return !dataSource.getConnection().isClosed();
            } catch (SQLException ex) {
                return false;
            }
        }
    }

    public <T> Object queryForObject(String sql, Map<String, Object> params, Class<T> requiredType) {

        initConnection();

        try {
            return namedParameterJdbcTemplate.queryForObject(sql, params, requiredType);
        } catch (Exception ex) {

            logger.error("request failed with following properties: ");
            for (String parameter : params.keySet()) {
                logger.error(parameter + " = " + params.get(parameter));
            }
            throw ex;
        }
    }

    public <T> List<T> queryForObjects(String sql, Map<String, Object> params, Class<T> requiredType) throws Exception {

        initConnection();

        try {
            return namedParameterJdbcTemplate.queryForList(sql, params, requiredType);
        } catch (Exception ex) {
            logger.error("request failed with following properties: ");
            for (String parameter : params.keySet()) {
                logger.error(parameter + " = " + params.get(parameter));
            }
            throw ex;
        }
    }

    public Map<String, Object> queryForDataSet(String sql, Map<String, Object> params) throws Exception {

        initConnection();

        try {
            return namedParameterJdbcTemplate.queryForMap(sql, params);
        } catch (Exception ex) {
            logger.error("request failed with following properties: ");
            for (String parameter : params.keySet()) {
                logger.error(parameter + " = " + params.get(parameter));
            }
            throw ex;
        }
    }

    public List<Map<String, Object>> queryForDataSets(String sql, Map<String, Object> params) throws Exception {

        initConnection();

        try {
            return namedParameterJdbcTemplate.queryForList(sql, params);
        } catch (Exception ex) {
            logger.error("request failed with following properties: ");
            for (String parameter : params.keySet()) {
                logger.error(parameter + " = " + params.get(parameter));
            }
            throw ex;
        }
    }

    public List<List<Object>> queryForDataTable(String sql, Map<String, Object> params) throws Exception {

        initConnection();

        List<List<Object>> result = new ArrayList<>();
        try {
            for (Map<String, Object> row : (List<Map<String, Object>>) namedParameterJdbcTemplate.queryForList(sql, params)) {
                result.add(new ArrayList<>(row.values()));
            }
            return result;
        } catch (Exception ex) {
            logger.error("request failed with following properties: ");
            for (String parameter : params.keySet()) {
                logger.error(parameter + " = " + params.get(parameter));
            }
            throw ex;
        }
    }

    public <K, V> Map<K, V> queryForDataPairs(String sql, Map<String, Object> params, String keyName, String valueName) throws Exception {

        initConnection();

        Map<K, V> result = new HashMap<K, V>();
        try {
            for (Map<String, Object> row : (List<Map<String, Object>>) namedParameterJdbcTemplate.queryForList(sql, params)) {
                if (result.containsKey(row.get(keyName))) {
                    throw new IncorrectResultSizeDataAccessException("Multiple row with same key value found!", 1, 2);
                }
                result.put((K) row.get(keyName), (V) row.get(valueName));
            }
            return result;
        } catch (Exception ex) {
            logger.error("request failed with following properties: ");
            for (String parameter : params.keySet()) {
                logger.error(parameter + " = " + params.get(parameter));
            }
            throw ex;
        }
    }

    public <K, V> HashMultimap<K, V> queryForDataMultiPairs(String sql, Map<String, Object> params, String keyName, String valueName) throws Exception {

        initConnection();

        try {
            HashMultimap<K, V> result = HashMultimap.create();
            for (Map<String, Object> row : (List<Map<String, Object>>) namedParameterJdbcTemplate.queryForList(sql, params)) {
                result.put((K) row.get(keyName), (V) row.get(valueName));
            }
            return result;
        } catch (Exception ex) {
            logger.error("request failed with following properties: ");
            for (String parameter : params.keySet()) {
                logger.error(parameter + " = " + params.get(parameter));
            }
            throw ex;
        }
    }


    public List<Object> transformList(List<String> input, List<Class> dataTypes) throws Exception {

        List<Object> result = new ArrayList<>();

        for (int i = 0; i < dataTypes.size(); i++) {
            result.add(transformValue(input.get(i), dataTypes.get(i)));
        }

        return result;
    }

    public List<List<Object>> transformTable(List<List<String>> input, List<Class> dataTypes) throws Exception {

        List<List<Object>> result = new ArrayList<>();

        for (List<String> row : input) {
            result.add(transformList(row, dataTypes));
        }
        return result;
    }


    public Object transformValue(String input, Class clazz) throws Exception {
        //string null is value null
        if (input.equalsIgnoreCase("null")) {
            return null;
            //numeric string is converted to numeric class value, default is BigDecimal
        } else if (input.matches("-?\\d+(\\.\\d+)?")) {
            //Remove trailing zeros
            if (input.contains(".") && input.endsWith("0")) {
                input = new DecimalFormat("0.########################").format(new BigDecimal(input));
            }
            if (clazz != null) {
                return clazz.getConstructor(String.class).newInstance(input);
            } else {
                return new BigDecimal(input);
            }
            //Date string converted to date value based on 2 formats, default date value is from class java.sql.Date
            //Retrieved date value from spring is Timestamp
            //If string cannot be converted to date exception is thrown
        } else if (input.toUpperCase().startsWith("DATE%") && input.endsWith("%")) {
            input = input.substring(input.indexOf("%") + 1, input.lastIndexOf("%"));
            long date = -1;
            if (input.matches(dateOnlyFormatRegEx)) {
                date = new SimpleDateFormat(dateOnlyFormat).parse(input).getTime();
            } else if (input.matches(dateTimeFormatRegEx)) {
                date = new SimpleDateFormat(dateTimeFormat).parse(input).getTime();
            }
            if (date != -1) {
                if (clazz != null) {
                    return clazz.getConstructor(Long.TYPE).newInstance(date);
                } else {
                    return new java.sql.Date(date);
                }
            } else {
                throw new IllegalArgumentException("Invalid date string " + input +
                        ", should be one of " + dateOnlyFormat + ", " + dateTimeFormat);
            }

        } else {
            //Remove string formatting
            return input.trim().replaceAll("\\s{1,}", " ");
        }
    }

    public Object transformValue(String input) throws Exception {
        return transformValue(input, null);
    }

    public int update(String sql, Map<String, Object> params) throws Exception {

        initConnection();

        try {
            return namedParameterJdbcTemplate.update(sql, params);
        } catch (Exception ex) {
            logger.error("request failed with following properties: ");
            for (String parameter : params.keySet()) {
                logger.error(parameter + " = " + params.get(parameter));
            }
            throw ex;
        }
    }

    public List<List<String>> executeStoredProcedure(String procedureName, String parameters) throws Exception {

        initConnection();

        try {
            Map<String, String> expectedComponents = jsonStringToMap(parameters);
            List<List<String>> queryData = new LinkedList<>();
            SimpleJdbcCall procedureParametersCall = new SimpleJdbcCall(dataSource);
            procedureParametersCall
                    .withProcedureName(procedureName);
            procedureParametersCall.declareParameters(new SqlOutParameter("p_result", OracleTypes.CURSOR, new ParameterizedRowMapper<List<List<String>>>() {
                public List<List<String>> mapRow(ResultSet rs, int i) throws SQLException {
                    ResultSetMetaData metadata = rs.getMetaData();
                    int columnCount = metadata.getColumnCount();
                    do {
                        List<String> a = new LinkedList<>();
                        for (int q = 1; q <= columnCount; q++) {
                            if (StringUtils.isEmpty(rs.getString(q))) {
                                a.add("");
                            } else {
                                a.add(rs.getString(q));
                            }
                        }
                        queryData.add(a);

                    } while (rs.next());
                    return queryData;
                }
            }
            ));

            for (Map.Entry<String, String> paramEntry : expectedComponents.entrySet()
                    ) {
                Map<String, String> paramMap = jsonStringToMap(paramEntry.getValue());
                switch (paramMap.get("type")) {
                    case "INTEGER":
                        procedureParametersCall.declareParameters(new SqlParameter(paramEntry.getKey(), Types.INTEGER));
                        break;
                    case "DATE":
                        procedureParametersCall.declareParameters(new SqlParameter(paramEntry.getKey(), Types.TIMESTAMP));
                        break;
                    case "DATEF":
                        procedureParametersCall.declareParameters(new SqlParameter(paramEntry.getKey(), Types.TIMESTAMP));
                        break;
                    case "STRING":
                        procedureParametersCall.declareParameters(new SqlParameter(paramEntry.getKey(), Types.VARCHAR));
                        break;
                }
            }

            MapSqlParameterSource valueMap = new MapSqlParameterSource();
            for (Map.Entry<String, String> valueEntry : expectedComponents.entrySet()
                    ) {
                Map<String, String> paramMap = jsonStringToMap(valueEntry.getValue());
                if (paramMap.get("value").equals("null")) {
                    valueMap.addValue(valueEntry.getKey(), null);
                } else {
                    switch (paramMap.get("type")) {
                        case "DATE":
                            valueMap.addValue(valueEntry.getKey(), new java.sql.Date(new Date().getTime() - 60000 * 60 * 24 * Math.abs(Long.parseLong(paramMap.get("value")))));
                            break;
                        case "DATEF":
                            valueMap.addValue(valueEntry.getKey(), new java.sql.Date(new Date().getTime() + 60000 * 60 * 24 * Math.abs(Long.parseLong(paramMap.get("value")))));
                            break;
                        default:
                            valueMap.addValue(valueEntry.getKey(), paramMap.get("value"));
                            break;
                    }
                }
            }
            List l = procedureParametersCall.executeFunction(List.class, valueMap);
            return (List<List<String>>) l.get(0);
        } catch (Exception ex) {
            logger.error("request failed with following properties: ");
            logger.error(procedureName + " = " + parameters);

            throw ex;
        }
    }

    public static Map<String, String> jsonStringToMap(String jsonString) throws Exception {

        Map<String, String> result = new LinkedHashMap<>();

        OrderedJSONObject object = new OrderedJSONObject(jsonString);

        Iterator keys = object.getOrder();

        while (keys.hasNext()) {
            String key = keys.next().toString();
            result.put(key, object.get(key).toString());
        }
        return result;
    }
}