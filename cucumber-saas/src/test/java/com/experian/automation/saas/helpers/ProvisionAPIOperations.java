package com.experian.automation.saas.helpers;

import com.experian.automation.logger.Logger;
import com.jayway.jsonpath.Filter;
import com.jayway.jsonpath.JsonPath;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import net.minidev.json.JSONArray;
import net.sf.saxon.expr.TryCatch;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jayway.jsonpath.Criteria.where;
import static com.jayway.jsonpath.Filter.filter;

public class ProvisionAPIOperations {

    private final Logger logger = Logger.getLogger(this.getClass());

    private String apiRequests;
    private final String apiURL = "http://provisioning-service-non-prod.dyn.dl-non-prod.genesaas.io/api/v1";

    HashMap<String, String> defaultHeaders;

    public ProvisionAPIOperations() throws IOException {

        File jsonFilePath = new File(getClass().getResource("/steps/provision-api/requests.json").getPath());
        apiRequests = FileUtils.readFileToString(jsonFilePath, "UTF-8");

        defaultHeaders = new HashMap<String,String>();
        defaultHeaders.put("Content-Type", "application/json");
        defaultHeaders.put("Accept", "application/json");
    }

    public int getServiceGroupID(String serviceGroupName) throws UnirestException {

        String requestURL = getRequestURL("list-service-group");
        HttpResponse<String> response = Unirest.get(requestURL).headers(defaultHeaders).asString();

        Filter filter = filter(where("name").eq(serviceGroupName));
        List<Integer> serviceGroupIDs = JsonPath.parse(response.getBody()).read("$.serviceGroups[?].id", filter);

        int serviceGroupID = 0;

        if (serviceGroupIDs.size() == 1) {
            serviceGroupID = serviceGroupIDs.get(0);
        }

        return serviceGroupID;
    }

    public int getServiceID(String serviceName, int serviceGroupID) throws UnirestException {

        String requestURL = String.format(getRequestURL("list-services"), serviceGroupID);
        HttpResponse<String> response = Unirest.get(requestURL).headers(defaultHeaders).asString();

        Filter filter = filter(where("release.group").eq(serviceName));
        List<Integer> serviceIDs = JsonPath.read(response.getBody(), "$.services[?].id", filter);

        int serviceID = 0;

        if (serviceIDs.size() == 1) {
            serviceID = serviceIDs.get(0);
        }

        return serviceID;
    }

    public int getServiceID(String serviceName, String serviceGroupName) throws UnirestException {

        int serviceGroupID = getServiceGroupID(serviceGroupName);

        return getServiceID(serviceName, serviceGroupID);
    }

    public List<String> getServices(String serviceGroupName) throws UnirestException {

        int serviceGroupID = getServiceGroupID(serviceGroupName);

        String requestURL = String.format(getRequestURL("list-services"), serviceGroupID);
        HttpResponse<String> responseServices = Unirest.get(requestURL).headers(defaultHeaders).asString();
        List<String> serviceNames = JsonPath.read(responseServices.getBody(), "$.services[*].release.group");

        return serviceNames;
    }

    public String getServiceStatus(String serviceName, String serviceGroupName) throws UnirestException {

        int serviceGroupID = getServiceGroupID(serviceGroupName);

        String requestURL = String.format(getRequestURL("list-services"), serviceGroupID);

        String serviceStatus = "NOTSET";

        try {
            HttpResponse<String> response = Unirest.get(requestURL).headers(defaultHeaders).asString();

            Filter filter = filter(where("release.group").eq(serviceName));
            List<String> serviceStatuses = JsonPath.read(response.getBody(), "$.services[?].status", filter);

            if (serviceStatuses.size() == 1) {
                serviceStatus = serviceStatuses.get(0);
            }

        } catch (UnirestException e) {
            serviceStatus = "UNKNOWN";
        }

        return serviceStatus;
    }

    public String getRelease(String serviceName) throws UnirestException {

        String requestURL = String.format(getRequestURL("list-release"), serviceName);
        HttpResponse<String> response = Unirest.get(requestURL).headers(defaultHeaders).asString();

        Filter filter = filter(where("releaseGroup").eq(serviceName)).and(where("tags.release-approved").eq("true"));
        JSONArray releases = JsonPath.read(response.getBody(), "$.releases[?].name", filter);

        String releaseName = "";

        if (releases.size() > 0) {
            releaseName = releases.get(releases.size() - 1).toString();
        }

        return releaseName;
    }

    public Map<String,String> getServiceProperties(int serviceGroupID,int serviceID) throws UnirestException {

        String requestURL = String.format(getRequestURL("list-service"), serviceGroupID, serviceID);

        HttpResponse<String> responseServices = Unirest.get(requestURL).headers(defaultHeaders).asString();

        Map<String,String> serviceNames = JsonPath.read(responseServices.getBody(), "$.properties");

        return serviceNames;
    }

    public boolean createServiceGroup(String serviceGroupName) throws UnirestException {

        String requestURL = getRequestURL("create-service-group");
        String requestBodyData = String.format(getRequestBody("create-service-group"), serviceGroupName);

        int statusCode;

        try {
            statusCode = Unirest.post(requestURL).headers(defaultHeaders).body(requestBodyData).asString().getStatus();
        } catch (UnirestException e) {
            statusCode = -1;
        }

        return statusCode == 201;
    }

    public boolean createService(String serviceName, String serviceGroupName, String releaseName) throws UnirestException {

        int serviceGroupID = getServiceGroupID(serviceGroupName);

        String requestURL = String.format(getRequestURL("create-service-" + serviceName), serviceGroupID);
        String requestBodyData = String.format(getRequestBody("create-service-" + serviceName), serviceGroupName, releaseName);

        int statusCode;

        try {
            statusCode = Unirest.post(requestURL).headers(defaultHeaders).body(requestBodyData).asString().getStatus();
        } catch (UnirestException e) {
            statusCode = -1;
        }

        return statusCode == 202;

    }

    public boolean deleteServiceGroup(String serviceGroupName) throws UnirestException {

        int serviceGroupID = getServiceGroupID(serviceGroupName);

        String requestURL = String.format(getRequestURL("delete-service-group"), serviceGroupID);

        int statusCode;

        try {
            statusCode = Unirest.delete(requestURL).headers(defaultHeaders).asString().getStatus();
        } catch (UnirestException e) {
            statusCode = -1;
        }

        // 504 will be returned because of ENP-8849
        return statusCode == 202 || statusCode == 504;
    }

    public boolean deleteService(String serviceName, String serviceGroupName) throws UnirestException {

        int serviceGroupID = getServiceGroupID(serviceGroupName);
        int serviceID = getServiceID(serviceName, serviceGroupID);

        String requestURL = String.format(getRequestURL("delete-service"), serviceGroupID, serviceID);

        int statusCode;

        try {
            statusCode = Unirest.delete(requestURL).headers(defaultHeaders).asString().getStatus();
        } catch (UnirestException e) {
            statusCode = -1;
        }

        return statusCode == 202;
    }

    public boolean waitForServiceStatus(String serviceName, String serviceGroupName, String status, long timeout) throws InterruptedException, UnirestException {

        boolean condition = false;
        long timeoutTime = System.currentTimeMillis() + timeout;

        do {
            condition = getServiceStatus(serviceName, serviceGroupName).equals(status);
            Thread.sleep(2 * 1000);
        } while (!condition && System.currentTimeMillis() < timeoutTime);

        return condition;
    }

    private String getRequestURL(String request) {
        return apiURL + JsonPath.parse(apiRequests).read("$." + request + ".uri").toString();
    }

    private String getRequestBody(String request) {
        return JsonPath.parse((Object) JsonPath.parse(apiRequests).read("$." + request + ".body")).jsonString();
    }

}
