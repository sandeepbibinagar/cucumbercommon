package com.experian.automation.saas.helpers;

import com.experian.automation.harnesses.TestHarness;
import com.experian.automation.logger.Logger;
import com.jayway.jsonpath.Filter;
import com.jayway.jsonpath.JsonPath;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.io.FileUtils;
import net.minidev.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class TacticalParametersOperations {

    private final Logger logger = Logger.getLogger(this.getClass());

    private String apiRequests;
    private final String apiURL = new TestHarness().config.get("bps.url");

    HashMap<String, String> defaultHeaders;

    public TacticalParametersOperations() throws IOException, ConfigurationException {

        File jsonFilePath = new File(getClass().getResource("/steps/tactical-parameters-api/requests.json").getPath());
        apiRequests = FileUtils.readFileToString(jsonFilePath, "UTF-8");

        defaultHeaders = new HashMap<String,String>();
        defaultHeaders.put("Content-Type", "application/json");
        defaultHeaders.put("Accept", "application/json");
        defaultHeaders.put("Authorization", "Basic YWRtaW46U2VjcmV0MTIzIQ==");
    }

   /* Return type java core: List<Map<String,String>> */
    public JSONArray getAllParameters() throws UnirestException {
        String requestURL = getRequestURL("list-all-parameters");
        HttpResponse<String> response = Unirest.get(requestURL).headers(defaultHeaders).asString();
        JSONArray parameters = JsonPath.read(response.getBody(), "$.*");
        return parameters;
    }

    /* Return type java core: List<Map<String,Map<String,String>> */
    public JSONArray getParameter(String id) throws UnirestException {
        String requestURL = String.format(getRequestURL("list-single-parameter"), id);
        HttpResponse<String> response = Unirest.get(requestURL).headers(defaultHeaders).asString();
        JSONArray parameter = JsonPath.read(response.getBody(), "$.*");
        return parameter;
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

    public boolean deployParameter(String id) {
        String requestURL = String.format(getRequestURL("deploy-parameter"),id);
        int statusCode;
        try {
            statusCode = Unirest.post(requestURL).headers(defaultHeaders).asString().getStatus();
        } catch (UnirestException e) {
            statusCode = -1;
        }

        return statusCode == 200;
    }

    private String getRequestURL(String request) {
        return apiURL + JsonPath.parse(apiRequests).read("$." + request + ".uri").toString();
    }

}
