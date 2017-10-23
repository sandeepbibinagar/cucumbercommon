package com.experian.automation.saas.steps;

import com.experian.automation.harnesses.TestHarness;
import com.experian.automation.helpers.NetworkOperations;
import com.experian.automation.logger.Logger;
import com.jayway.jsonpath.Filter;
import com.jayway.jsonpath.JsonPath;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import cucumber.api.java.en.And;
import net.minidev.json.JSONArray;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.jayway.jsonpath.Criteria.where;
import static com.jayway.jsonpath.Filter.filter;

public class OpenShiftProvisionSteps {


    private final TestHarness testHarness;
    private final Logger logger = Logger.getLogger(this.getClass());

    private final String apiURL = "http://provisioning-service-non-prod.dyn.dl-non-prod.genesaas.io/api/v1";
    private String apiRequests;

    public OpenShiftProvisionSteps(TestHarness testHarness) {
        this.testHarness = testHarness;

        Unirest.setDefaultHeader("Content-Type", "application/json");
        Unirest.setDefaultHeader("Accept", "application/json");

        File jsonFilePath = new File(getClass().getResource("/steps/openshift-provision/api.json").getPath());
        try {
            apiRequests = FileUtils.readFileToString(jsonFilePath, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @And("^Initialize provisioned environments$")
    public void initResources() throws Throwable {

        String serviceGroupNameTemplate = testHarness.config.get("openshift.service.group.name");
        Integer serviceGroupCount =  Integer.valueOf(testHarness.config.get("openshift.service.group.count"));

        String serviceGroupName;
        List<String> serviceGroupNames = new ArrayList<>();

        for (int i=0; i < serviceGroupCount; i++) {
            serviceGroupName = String.format(serviceGroupNameTemplate, StringUtils.leftPad(Integer.toString(i+1), 2, '0'));
            serviceGroupNames.add(serviceGroupName);
        }

        testHarness.contextResources.add("openshift-environment", serviceGroupNames);

        serviceGroupName = testHarness.contextResources.allocate("openshift-environment");
        testHarness.setStepData("openshift.servicegroup", serviceGroupName);

        System.out.print("openshift-environment--" + serviceGroupName + " > allocating......" + Thread.currentThread().getId() + "\n");

        // Delete use case env
        deleteServiceGroupIfDoesntExists(serviceGroupName, "TRUE");

        // Create use case env
        createUseCaseEnvironment("ACF", serviceGroupName);

        // Wait for BPS port health check
        String bpsHost = "bps-" + serviceGroupName + ".dyn.dl-non-prod.genesaas.io";
        Boolean healthCheckPort = new NetworkOperations(bpsHost, 80).checkPortAvailability(true);
        Assert.assertTrue(healthCheckPort, "Port: 80 on " + bpsHost + " is not accessible");

        // Wait for BPS service health chech
        String webServicesBaseURL = "http://" +  bpsHost + "/services";

        HttpResponse<String> response;
        Boolean healthCheckService = false;

        // Wait for 5 min
        long timeoutTime = System.currentTimeMillis() + 5 * 60 * 1000;
        do {
            response = Unirest.get(webServicesBaseURL + "/health").asString();
            if ( response.getStatus() == 200 ){
                healthCheckService = JsonPath.parse(response.getBody()).read("$.BPS.healthy");
            }
            Thread.sleep(500);
        } while (!healthCheckService && System.currentTimeMillis() < timeoutTime);

        Assert.assertTrue(healthCheckPort, "BPS service health check failed");

        testHarness.setStepData("base.webservices.url", webServicesBaseURL);
    }

    @And("^Destroy provisioned environments$")
    public void releaseResources() throws Throwable {

        String serviceGroupName = testHarness.getStepData("openshift.servicegroup");

        // Delete use case env
        deleteServiceGroupIfDoesntExists(serviceGroupName, "TRUE");

        // Release resource
        testHarness.contextResources.release("openshift-environment", serviceGroupName);
    }

    @And("^I create environment for use case (.*) in service group (.*)?$")
    public void createUseCaseEnvironment(String ucName, String serviceGroupName) throws Throwable {

        createServiceGroupIfDoesntExists(serviceGroupName, "TRUE");

        if ( ucName.equals("ACF") ){
            createService("powercurve-simulation", serviceGroupName, "LATEST");
            createService("powercurve-connectivity", serviceGroupName, "LATEST");
            createService("pc-acquirecustomersfaster-us", serviceGroupName, "LATEST");
            createService("admin-portal-server", serviceGroupName, "LATEST");
            createService("user-service", serviceGroupName, "LATEST");
            createService("originations-facade", serviceGroupName, "LATEST");
            createService("token-service", serviceGroupName, "LATEST");
            createService("admin-portal-ui", serviceGroupName, "LATEST");
        }

    }

    @And("^I create environment service group (.*)( if it doesn't exist)?$")
    public void createServiceGroupIfDoesntExists(String serviceGroupName, String checkExistence) throws Throwable {

        Boolean skipCreation = false;

        if ( checkExistence != null ){
            HttpResponse<String> response = Unirest.get(getRequestURL("list-service-group")).asString();
            Filter filter = filter(where("name").eq(serviceGroupName));
            List<Integer> serviceGroupIDs = JsonPath.parse(response.getBody()).read("$.serviceGroups[?].id", filter);

            if( serviceGroupIDs.size() == 1 ){
                skipCreation = true;
            }
        }

        if( !skipCreation ){
            // Create Service Group
            String requestBodyData = String.format(getRequestBody("create-service-group"), serviceGroupName);
            HttpResponse<String> response = Unirest.post(getRequestURL("create-service-group")).body(requestBodyData).asString();

            Assert.assertEquals(response.getStatus(), 201, "Cannot create service group." + response.getBody());
        }
    }

    @And("^I delete environment service group (.*)( if it doesn't exist)?$")
    public void deleteServiceGroupIfDoesntExists(String serviceGroupName, String checkExistence) throws Throwable {

        Boolean skipDeletion = false;

        HttpResponse<String> response = Unirest.get(getRequestURL("list-service-group")).asString();
        Filter filter = filter(where("name").eq(serviceGroupName));
        List<Integer> serviceGroupIDs = JsonPath.parse(response.getBody()).read("$.serviceGroups[?].id", filter);

        if( checkExistence != null && serviceGroupIDs.size() == 0 ){
            skipDeletion = true;
        }

        if( !skipDeletion ){
            // Create Service Group
            String requestURL = String.format(getRequestURL("delete-service-group"), serviceGroupIDs.get(0).toString());
            response = Unirest.delete(requestURL).asString();

            // Skip because of ESSP-6069
            // Assert.assertEquals(response.getStatus(), 504, "Cannot delete service group.");
        }
    }

    @And("^I create environment service (.*) in service group (.*) with (.*|LATEST) release$")
    public void createService(String serviceName, String serviceGroupName, String releaseName) throws Throwable {

        // Get Release Name
        if ( releaseName.equals("LATEST") ){
            HttpResponse<String> response = Unirest.get(getRequestURL("list-release")).asString();
            JSONArray releases = JsonPath.read(response.getBody(), "$.releases[?(@.releaseGroup == '" + serviceName + "')].name");
            releaseName = releases.get(releases.size()-1).toString();
        }

        // Get Service Group ID
        HttpResponse<String> response = Unirest.get(getRequestURL("list-service-group")).asString();
        Filter filter = filter(where("name").eq(serviceGroupName));
        List<Integer> serviceGroupIDs = JsonPath.parse(response.getBody()).read("$.serviceGroups[?].id", filter);

        Assert.assertEquals(serviceGroupIDs.size(), 1, "Could not found the Service Group ID for " + serviceGroupName);

        String serviceGroupID = serviceGroupIDs.get(0).toString();

        // Create service
        String requestURL = String.format(getRequestURL("create-service-" + serviceName), serviceGroupID);
        String requestBodyData = String.format(getRequestBody("create-service-" + serviceName), serviceGroupName, releaseName);
        response = Unirest.post(requestURL).body(requestBodyData).asString();

        Assert.assertEquals(response.getStatus(), 202);

    }

    @And("^I create environment services in service group (.*):$")
    public void createServices(String serviceGroupName, List<List<String>> services) throws Throwable {

        for (List<String> service : services) {
            String serviceName = service.get(0);
            String releaseName = service.get(1);
            createService(serviceName, serviceGroupName, releaseName);
        }

    }

    private String getRequestURL(String request){
        return apiURL + JsonPath.parse(apiRequests).read("$." + request + ".uri").toString();
    }

    private String getRequestBody(String request){
        return JsonPath.parse((Object) JsonPath.parse(apiRequests).read("$." + request + ".body")).jsonString();
    }


}
