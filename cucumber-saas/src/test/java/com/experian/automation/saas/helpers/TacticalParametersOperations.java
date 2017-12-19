package com.experian.automation.saas.helpers;

import com.experian.automation.helpers.XMLOperations;
import com.experian.automation.logger.Logger;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jayway.jsonpath.JsonPath;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minidev.json.JSONArray;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;


public class TacticalParametersOperations {

    private final Logger logger = Logger.getLogger(this.getClass());

    private String apiRequests;
    private final String apiURL;

    HashMap<String, String> defaultHeaders;

    public TacticalParametersOperations(String apiURL) throws IOException, ConfigurationException {

        File jsonFilePath = new File(getClass().getResource("/steps/tactical-parameters-api/requests.json").getPath());
        apiRequests = FileUtils.readFileToString(jsonFilePath, "UTF-8");

        Unirest.clearDefaultHeaders();

        defaultHeaders = new HashMap<String, String>();
        defaultHeaders.put("Content-Type", "application/json");
        defaultHeaders.put("Accept", "application/json");
        defaultHeaders.put("Authorization", "Basic YWRtaW46U2VjcmV0MTIzIQ==");

        this.apiURL = apiURL;
    }

    /* Return type java core: List<Map<String,String>> */
    public JSONArray getAllParameters() throws UnirestException {
        String requestURL = getRequestURL("list-all-parameters");

        int statusCode;
        try {
            statusCode = Unirest.get(requestURL).headers(defaultHeaders).asString().getStatus();
        } catch (UnirestException e) {
            statusCode = -1;
        }

        if (statusCode != 200) {
            throw new IllegalArgumentException("Cannot fetch all Tactical Parameters. Status code HTTP "+statusCode);
        }

        HttpResponse<String> response = Unirest.get(requestURL).headers(defaultHeaders).asString();

        JSONArray parameters = JsonPath.read(response.getBody(), "$.*");

        return parameters;
    }

    /* Return type java core: List<Map<String,Map<String,String>> */
    public JSONArray getParameter(String id) throws UnirestException, IllegalArgumentException {
        String requestURL = String.format(getRequestURL("list-single-parameter"), id);

        int statusCode;
        try {
            statusCode = Unirest.get(requestURL).headers(defaultHeaders).asString().getStatus();
        } catch (UnirestException e) {
            statusCode = -1;
        }

        if (statusCode != 200) {
            throw new IllegalArgumentException("Cannot get parameter with ID: " + id);
        }

        HttpResponse<String> response = Unirest.get(requestURL).headers(defaultHeaders).asString();
        JSONArray parameter = JsonPath.read(response.getBody(), "$.*");

        return parameter;
    }

    public JSONArray getParameterVersions(String componentId) throws UnirestException {
        String requestURL = String.format(getRequestURL("list-parameter-versions"), componentId);
        HttpResponse<String> response = Unirest.get(requestURL).headers(defaultHeaders).asString();
        JSONArray parameters = JsonPath.read(response.getBody(), "$.*");
        return parameters;
    }

    public String getLatestParameterVersion(String parameterName) throws UnirestException {
        JSONArray parameterProperties = JsonPath.read(getAllParameters(), "$.*[?(@.name=='" + parameterName + "')]]");
        if (parameterProperties.size() == 0) {
            throw new IllegalArgumentException("Cannot find parameter with name " + parameterName + " List of available parameters: " +
                    JsonPath.read(getAllParameters(), "$.*.name"));
        }
        String version = JsonPath.read(parameterProperties.get(0), "$.version").toString();
        return version;
    }

    public String getLatestParameterVersionId(String parameterName) throws UnirestException {
        JSONArray parameterProperties = JsonPath.read(getAllParameters(), "$.*[?(@.name=='" + parameterName + "')]]");
        if (parameterProperties.size() == 0) {
            throw new IllegalArgumentException("Cannot find parameter with name " + parameterName + " List of available parameters: " +
                    JsonPath.read(getAllParameters(), "$.*.name"));
        }
        String latestVersionId = JsonPath.read(parameterProperties.get(0), "$.id").toString();
        return latestVersionId;
    }

    public boolean updateParameter(String data) throws UnirestException {
        String requestURL = String.format(getRequestURL("update-parameter"));
        int statusCode;
        try {
            statusCode = Unirest.post(requestURL).headers(defaultHeaders).body(data).asString().getStatus();
        } catch (UnirestException e) {
            statusCode = -1;
        }

        return statusCode == 200;
    }

    public boolean updateParameter(String name, String description, String fromDate, String toDate,
                                   List<List<String>> data) throws UnirestException {
        JSONArray parameterBody = getParameter(getLatestParameterVersionId(name));

        LinkedHashMap<String, String> metadata = (LinkedHashMap<String, String>) parameterBody.get(0);

        if (metadata.get("label") != null) {
            metadata.replace("label", description);
        } else {
            metadata.put("label", description);
        }

        if (metadata.get("from") != null) {
            metadata.replace("from", fromDate);
        } else {
            metadata.put("from", fromDate);
        }

        if (toDate != null) {
            if (metadata.get("to") != null) {
                metadata.replace("to", toDate);
            } else {
                metadata.put("to", toDate);
            }
        } else {
            if (metadata.get("to") != null) {
                metadata.replace("to", "");
            }
        }

        metadata.remove("deployed");
        metadata.remove("state");
        metadata.remove("deployed_user");
        metadata.remove("client_guid");
        metadata.remove("deployed_date");
        metadata.remove("componentId");

        List<HashMap<String, String>> attributesList = new ArrayList<>();

        for (Integer k = 1; k < data.size(); k++) {
            HashMap<String, String> values = new HashMap<>();
            for (Integer j = 0; j < data.get(k).size(); j++) {
                values.put("param_weight", k.toString());
                JSONArray jsonAttribute = JsonPath.read(parameterBody.get(1), "$.*[?(@.name=='" + data.get(0).get(j) + "')].field");
                if (jsonAttribute.size() == 0) {
                    throw new IllegalArgumentException(
                            "Cannot find attribute with name: " + data.get(0).get(j));
                }
                values.put(jsonAttribute.get(0).toString(), data.get(k).get(j));
            }
            attributesList.add(k - 1, values);
        }

        Gson gsonObj = new Gson();
        JsonElement attributesValues = gsonObj.toJsonTree(attributesList);
        JsonElement metaDataValues = gsonObj.toJsonTree(metadata);

        JsonObject childNodes = new JsonObject();
        childNodes.add("meta_datas", metaDataValues);
        childNodes.add("attributesValues", attributesValues);

        JsonObject parentNode = new JsonObject();
        parentNode.add("parameter_data", childNodes);

        return updateParameter(parentNode.toString());
    }


    public boolean deployParameter(String name, String version) throws UnirestException {
        if (version.equals("LATEST")) {
            version = getLatestParameterVersionId(name);
        } else {
            JSONArray parameterProperties = JsonPath.read(getAllParameters(), "$.*[?(@.name=='" + name + "')]]");
            String componentId = JsonPath.read(parameterProperties.get(0), "$.componentId").toString();
            JSONArray versionProperties = JsonPath.read(getParameterVersions(componentId), "$.*[?(@.version=='" + version + "')]]");

            if (JsonPath.read(versionProperties.get(0), "$.deployed").toString().equals("deployed")) {
                throw new IllegalArgumentException("Version:" + version + " is already deployed.");
            }
            version = JsonPath.read(versionProperties.get(0), "$.id").toString();
        }

        logger.info("Deploying parameter: " + name + " Version ID:" + version);

        String requestURL = String.format(getRequestURL("deploy-parameter"), version);
        int statusCode;
        try {
            statusCode = Unirest.post(requestURL).headers(defaultHeaders).asString().getStatus();
        } catch (UnirestException e) {
            statusCode = -1;
        }

        return statusCode == 200;
    }

    public List<String> getParametersListFromFile(String file) throws IOException {
        JSONObject fileParameters = new XMLOperations().convertXMLToJSON(file);
        List<String> allParametersList = JsonPath.read(fileParameters.toString(), "$.TacticalParameterGroupsData.e.*.meta_datas.name");
        return allParametersList;
    }

    public void updateFromFile(String parameterName, String parameterVersion, String parameterVersionId, String file) throws IOException, UnirestException {

        /* Fetching the selected parameter object and checking whether the version is already deployed */
        JSONArray solutionParameters = JsonPath.read(getAllParameters(), "$.*.[?(@.name=='" + parameterName + "')]]");
        String nonDeployedParameterVersion = JsonPath.read(solutionParameters.get(0), "$.version").toString();

        if (!nonDeployedParameterVersion.equals(parameterVersion)) {
            throw new IllegalArgumentException("Wrong version: " + parameterVersion + " for parameter " + parameterName + ". Currently deploy-able version: " + nonDeployedParameterVersion);
        }

        JSONArray parameter = getParameter(parameterVersionId);
        String deployed = JsonPath.read(parameter, "$.*.deployed").toString();
        Boolean isDeployed = Boolean.parseBoolean(deployed.substring(1, deployed.length() - 1));

        if (isDeployed) {
            throw new IllegalArgumentException("Parameter with ID " + parameterVersionId + " is already deployed.");
        }

        /* Converting the parameter XML to JSON and checking if the selected parameter exists in the JSON object */
        JSONObject fileParameters = new XMLOperations().convertXMLToJSON(file);
        JSONArray targetParameterGroup = JsonPath.read(fileParameters.toString(), "$.TacticalParameterGroupsData.e.*[?(@.meta_datas.name=='" + parameterName + "')]]");

        if (targetParameterGroup.size() != 1) {
            throw new IllegalArgumentException("Parameters with name: " + parameterName + " found: " + targetParameterGroup.size());
        }


        JSONArray paramWeightsNumber = JsonPath.read(targetParameterGroup.toString(), "$.*.attributesValues.e[?(@._param_weight)]");

        List<HashMap<String, Object>> parametersList = new ArrayList<>();
        List<HashMap<String, String>> normalizedParametersList = new ArrayList<>();

        /* Creating Map which holds the <K,V> pairs for the parameters attributes from the JSON object */
        String paramFindExpresion =
                (paramWeightsNumber.size() > 1) ? "$.*.attributesValues.e.*" : "$.*.attributesValues.e";
        parametersList = JsonPath.read(targetParameterGroup.toString(), paramFindExpresion);
        for (int j = 0; j < parametersList.size(); j++) {
            HashMap<String, String> normalizedKeysMap = new HashMap<>();
            for (Map.Entry<String, Object> entry : parametersList.get(j).entrySet()) {
                if (!(entry.getKey().equals("_param_id") || entry.getKey().endsWith("_id"))) {
                    normalizedKeysMap.put(entry.getKey().substring(1), entry.getValue().toString());
                }
            }
            normalizedParametersList.add(j, normalizedKeysMap);
        }

        /* Creating map which is based on name,id,version and label <K,V> pairs from the JSON object */
        List<Map<String, Object>> metaData = JsonPath.read(targetParameterGroup.toString(), "$.*.meta_datas");
        HashMap<String, String> normalizedMetaData = new HashMap<>();

        for (Map.Entry<String, Object> entry : metaData.get(0).entrySet()) {
            if (entry.getKey().equals("name")) {
                normalizedMetaData.put(entry.getKey(), parameterName);
            }
            if (entry.getKey().equals("id")) {
                normalizedMetaData.put(entry.getKey(), parameterVersionId);
            }
            if (entry.getKey().equals("version")) {
                normalizedMetaData.put(entry.getKey(), parameterVersion);
            }
            if (entry.getKey().equals("label") || entry.getKey().equals("from")) {
                normalizedMetaData.put(entry.getKey(), entry.getValue().toString());
            }
        }

        /* Assembling JSON body which has of two objects: meta_datas and attributes_values
         * meta_datas - Consists of the data from the Map which holds name,id,version,etc...
         * attribute_values - Consists of the data from the Map which holds the parameters attributes <K,V> pairs
         */
        Gson gsonObj = new Gson();
        JsonElement attributesValues = gsonObj.toJsonTree(normalizedParametersList);
        JsonElement metaDataValues = gsonObj.toJsonTree(normalizedMetaData);

        JsonObject data = new JsonObject();
        data.add("meta_datas", metaDataValues);
        data.add("attributesValues", attributesValues);

        JsonObject parameterBody = new JsonObject();
        parameterBody.add("parameter_data", data);

        updateParameter(parameterBody.toString());
    }


    private String getRequestURL(String request) {
        return apiURL + JsonPath.parse(apiRequests).read("$." + request + ".uri").toString();
    }

}
