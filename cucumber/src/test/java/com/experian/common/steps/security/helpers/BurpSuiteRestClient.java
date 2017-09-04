/**
 * Copyright (c) Experian, 2017. All rights reserved.
 */
package com.experian.common.steps.security.helpers;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

/**
 * Utility class for accessing REST API of PortSwigger BurpSuite Enterprise.
 * Used to create dynamic scan for a target application.
 * Created by c01266a on 8/31/2017.
 */
public final class BurpSuiteRestClient {

    // Internal arguments
    private static String burpSuiteEnterpriseUrl;
    private static String authzHeader;
    private static String siteId;
    private static String scheduleItemId;
    private static String scanId;

    // BurpSuite Enterprise URLs
    private static final String JSON_CONTENT_TYPE = "application/json";
    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String BURP_SUITE_LOGIN_URL = "/api/login";
    private static final String BURP_SUITE_CREATE_SITE_URL = "/api/sites/";
    private static final String BURP_SUITE_CREATE_SCAN_URL = "/api/schedule";
    private static final String BURP_SUITE_SCANS_URL = "/api/scans/";
    private static final String BURP_SUITE_ISSUES_URL = "/issues";
    private static final String BURP_SUITE_SCAN_SUMMARIES_URL = "/api/scan_summaries/latest_by_site";
    private static final String BURP_SUITE_SCAN_ISSUE_DEFINITIONS_URL = "/api/issue_definitions";

    // json properties used to call API
    private static final String PARENT_ID_PROPERTY = "parent_id";
    private static final String VERSION_PROPERTY = "version";
    private static final String NAME_PROPERTY = "name";
    private static final String ID_PROPERTY = "id";
    private static final String USERNAME_PROPERTY = "username";
    private static final String PASSWORD_PROPERTY = "password";
    private static final String LABEL_PROPERTY = "label";
    private static final String CREDENTIALS_PROPERTY = "credentials";
    private static final String URLS_PROPERTY = "urls";
    private static final String EXCLUDED_URLS_PROPERTY = "excluded_urls";
    private static final String CUSTOMIZATION_IDS_PROPERTY = "customization_ids";
    private static final String SITE_TREE_NODE_ID_PROPERTY = "site_tree_node_id";
    private static final String RRULE_PROPERTY = "rrule";
    private static final String INITIAL_RUN_TIME_PROPERTY = "initial_run_time";
    private static final String SCAN_SUMMARIES_PROPERTY = "scan_summaries";
    private static final String SCHEDULE_ITEM_ID_PROPERTY = "schedule_item_id";
    private static final String SCAN_ID_PROPERTY = "scan_id";
    private static final String CREDS_ENDING = "creds";

    // properties for report
    private static final String ISSUES = "issues";
    private static final String DEFINITIONS = "definitions";
    private static final String HIGH_SEVERITY = "High";
    private static final String MEDIUM_SEVERITY = "Medium";

    private static final Logger logger = LoggerFactory.getLogger(BurpSuiteRestClient.class);

    // The URL to scan
    public String targetEndpoint;

    /**
     * Method to login to Burp Suite Enterprise
     * @param burpEnterpriseUrl URL pointing where Burp Suite Enterprise is deployed
     * @param burpScannerUsername valid login username for Burp Suite Enterprise
     * @param burpScannerPassword valid login password for Burp Suite Enterprise
     * @return authorization header - required for all subsequent operations with the scanner
     */
    public BurpSuiteRestClient (String burpEnterpriseUrl, String burpScannerUsername, char[] burpScannerPassword) {
        burpSuiteEnterpriseUrl = burpEnterpriseUrl;
        authzHeader = burpScannerLogin (burpEnterpriseUrl, burpScannerUsername, burpScannerPassword);
    }

    /**
     * Burp Suite Enterprise connection parameters:
     * @param burpEnterpriseUrl URL of the Burp Suite Enterprise instance
     * @param burpScannerUsername username for account with permissions to create/delete sites, scans
     * @param burpScannerPassword password for account with permissions to create/delete sites, scans
     * @return authzHeader All operations executed in authenticated context require the authorization header
     */
    private String burpScannerLogin (String burpEnterpriseUrl, String burpScannerUsername, char[] burpScannerPassword) {

        ClientConfig config = new ClientConfig().register(JacksonFeature.class);
        Client client = ClientBuilder.newClient(config);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(USERNAME_PROPERTY, burpScannerUsername);
        jsonObject.addProperty(PASSWORD_PROPERTY, new String (burpScannerPassword));
        String input = jsonObject.toString();

        Response response = client.target(burpEnterpriseUrl + BURP_SUITE_LOGIN_URL).request(JSON_CONTENT_TYPE).post(Entity.json(input));

        List<Object> authz = response.getHeaders().get(AUTHORIZATION_HEADER_NAME);
        authzHeader = authz.get(0).toString();
        logger.debug(authzHeader);

        return authzHeader;
    }

    public static String getAuthzHeader() {
        return authzHeader;
    }

    public static String getBurpSuiteEnterpriseUrl() {
        return burpSuiteEnterpriseUrl;
    }

    /**
     * Create site in Burp Suite Enterprise
     * @param targetEndpoint the URL to be scanned
     * @param applicationName what will be the application name in the Enterprise
     * @return siteId internal representation of the site identification in Enterprise
     */
    public String burpScannerCreateSite (String targetEndpoint, String applicationName, String appUsername, char[] appPassword) {

        ClientConfig config = new ClientConfig().register(JacksonFeature.class);
        Client client = ClientBuilder.newClient(config);

        JsonObject jsonObject = new JsonObject();

        jsonObject.add(PARENT_ID_PROPERTY, null);
        jsonObject.add(VERSION_PROPERTY, null);
        jsonObject.addProperty(NAME_PROPERTY, applicationName);

        JsonArray jEmptyArray = new JsonArray();

        // Create credentials for the target application
        JsonArray jCredsArray = new JsonArray();
        JsonObject creds = new JsonObject();
        JsonPrimitive cpass = new JsonPrimitive(new String(appPassword));
        JsonPrimitive clabel = new JsonPrimitive(applicationName + CREDS_ENDING);
        JsonPrimitive cuser = new JsonPrimitive(appUsername);
        creds.add(PASSWORD_PROPERTY, cpass);
        creds.add(LABEL_PROPERTY, clabel);
        creds.add(USERNAME_PROPERTY, cuser);
        jCredsArray.add(creds);
        jsonObject.add(CREDENTIALS_PROPERTY, jCredsArray);

        JsonArray jEmptyStringArray = new JsonArray();
        JsonPrimitive element = new JsonPrimitive(targetEndpoint);
        jEmptyStringArray.add(element);

        jsonObject.add(URLS_PROPERTY, jEmptyStringArray);
        jsonObject.add(EXCLUDED_URLS_PROPERTY, jEmptyArray);
        jsonObject.add(CUSTOMIZATION_IDS_PROPERTY, jEmptyArray);

        String input = jsonObject.toString();
        logger.debug("burpScannerCreateSite: {}", input);

        Response response = client.target(getBurpSuiteEnterpriseUrl() + BURP_SUITE_CREATE_SITE_URL).request().
                header(HttpHeaders.AUTHORIZATION, authzHeader).
                accept(MediaType.APPLICATION_JSON).post(Entity.json(input));

        String output = response.readEntity(String.class);
        logger.debug("burpScannerCreateSite: {}", output);

        JsonObject jsonResponseObject = new JsonParser().parse(output).getAsJsonObject();
        siteId = jsonResponseObject.get(ID_PROPERTY).getAsString();
        logger.debug("burpScannerCreateSite: For {} accessible on: {}, id: {} is created in Burp Suite Enterprise",
                applicationName, targetEndpoint, siteId);

        return siteId;
    }

    /**
     * Delete site in Burp Suite Enterprise
     * @param siteId internal representation of the site identification in Enterprise
     * @return if site is deleted successfully
     */
    public boolean burpScannerDeleteSite (String siteId) {

        if(siteId == null || siteId.isEmpty()) {
            logger.error("burpScannerDeleteSite: Empty siteId.");
            return false;
        }

        final String BURP_DELETE_SITE_URL = BURP_SUITE_CREATE_SITE_URL + siteId;

        ClientConfig config = new ClientConfig().register(JacksonFeature.class);
        Client client = ClientBuilder.newClient(config);

        Response response = client.target(getBurpSuiteEnterpriseUrl() + BURP_DELETE_SITE_URL).request().
                header(HttpHeaders.AUTHORIZATION, authzHeader).
                accept(MediaType.APPLICATION_JSON).delete();

        int status = response.getStatus();
        if (status != 204) {
            logger.error("burpScannerDeleteSite: Unexpected status code: {}", status);
            return false;
        }

        logger.debug("burpScannerDeleteSite: status code: {}", status);

        return true;
    }

    /**
     * Create scan in Burp Suite Enterprise
     * @param siteId internal representation of the site identification in Enterprise
     * @return internal representation of the scheduled scan
     */
    public String burpScannerCreateScan (String siteId) {

        if(siteId == null || siteId.isEmpty()) {
            logger.error("burpScannerCreateScan: Empty siteId.");
            return null;
        }

        ClientConfig config = new ClientConfig().register(JacksonFeature.class);
        Client client = ClientBuilder.newClient(config);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(SITE_TREE_NODE_ID_PROPERTY, siteId);
        jsonObject.add(RRULE_PROPERTY, null);
        jsonObject.add(INITIAL_RUN_TIME_PROPERTY, null);

        // TODO: Add customization ex. for proxy
        JsonArray jEmptyArray = new JsonArray();
        jsonObject.add(CUSTOMIZATION_IDS_PROPERTY, jEmptyArray);

        String input = jsonObject.toString();

        Response response = client.target(getBurpSuiteEnterpriseUrl() + BURP_SUITE_CREATE_SCAN_URL).request().
                header(HttpHeaders.AUTHORIZATION, authzHeader).
                accept(MediaType.APPLICATION_JSON).post(Entity.json(input));

        String output = response.readEntity(String.class);
        JsonObject jsonResponseObject = new JsonParser().parse(output).getAsJsonObject();

        scheduleItemId = jsonResponseObject.get(ID_PROPERTY).getAsString();

        logger.debug("burpScannerCreateScan: {}", scheduleItemId);

        return scheduleItemId;
    }

    private String burpScanerGetScanId (String scheduleItemId) {

        if(scheduleItemId == null || scheduleItemId.isEmpty()) {
            logger.error("burpScannerCreateScan: Empty scheduleItemId.");
            return null;
        }

        ClientConfig config = new ClientConfig().register(JacksonFeature.class);
        Client client = ClientBuilder.newClient(config);

        Response response = client.target(getBurpSuiteEnterpriseUrl() + BURP_SUITE_SCAN_SUMMARIES_URL).request().
                header(HttpHeaders.AUTHORIZATION, authzHeader).
                accept(MediaType.APPLICATION_JSON).get();

        String output = response.readEntity(String.class);
        JsonObject jsonResponseObject = new JsonParser().parse(output).getAsJsonObject();

        JsonArray scans = jsonResponseObject.get(SCAN_SUMMARIES_PROPERTY).getAsJsonArray();
        for (JsonElement jsonElement : scans) {
            JsonObject jObj = jsonElement.getAsJsonObject();
            if (jObj.get(SCHEDULE_ITEM_ID_PROPERTY).getAsString().equals(scheduleItemId)) {
                scanId = jObj.get(SCAN_ID_PROPERTY).getAsString();
                logger.debug("burpScanerGetScanId: {}", scanId);
            }
        }

        logger.debug("burpScanerGetScanId: {}", scanId);

        return scanId;
    }

    /**
     * Check if scan in Burp Suite Enterprise has finished and report is available
     * @param scheduleItemId internal representation of the scheduled scan
     * @return scanId if scan has finished
     */
    public String checkScanFinished(String scheduleItemId) {

        scanId = burpScanerGetScanId(scheduleItemId);
        final String BURP_SUITE_SCAN_ID_URL = BURP_SUITE_SCANS_URL + scanId;

        if(scanId == null || scanId.isEmpty()) {
            logger.info("checkScanFinished: Scan not finished.");
            return null;
        }

        logger.debug("checkScanFinished: scanId: {}", scanId);

        ClientConfig config = new ClientConfig().register(JacksonFeature.class);
        Client client = ClientBuilder.newClient(config);

        Response response = client.target(getBurpSuiteEnterpriseUrl() + BURP_SUITE_SCAN_ID_URL).request().
                header(HttpHeaders.AUTHORIZATION, authzHeader).
                accept(MediaType.APPLICATION_JSON).get();

        String output = response.readEntity(String.class);
        logger.debug("checkScanFinished: {}", output);

        int status = response.getStatus();
        if (status != 200) {
            logger.error("checkScanFinished: {}", output);
            return null;
        }
        return scanId;
    }

    private Set<IssueDefinition> getBurpScannerIssueDefinitions() {

        Set<IssueDefinition> issuesDefinitions = new HashSet<>();
        ClientConfig config = new ClientConfig().register(JacksonFeature.class);
        Client client = ClientBuilder.newClient(config);

        Response response = client.target(getBurpSuiteEnterpriseUrl() + BURP_SUITE_SCAN_ISSUE_DEFINITIONS_URL).request().
                header(HttpHeaders.AUTHORIZATION, authzHeader).
                accept(MediaType.APPLICATION_JSON).get();

        String burpIssueDefinitions = response.readEntity(String.class);

        JsonParser parser = new JsonParser();
        JsonObject jsonIssueDefinitions = parser.parse(burpIssueDefinitions).getAsJsonObject();
        JsonElement definitions = jsonIssueDefinitions.get(DEFINITIONS);
        JsonArray definitionsArray = definitions.getAsJsonArray();

        for (JsonElement definitionEl : definitionsArray) {
            Gson g = new Gson();
            IssueDefinition issueDefinition = g.fromJson(definitionEl, IssueDefinition.class);
            logger.debug("getBurpScannerIssueDefinitions: ", issueDefinition.toString());
            issuesDefinitions.add(issueDefinition);
        }

        return issuesDefinitions;
    }

    /**
     * Check if high or medium severity vulnerabilities are found in a scan
     * @param scanId internal representation of the scan
     * @return if high or medium severity vulnerabilities are found
     */
    public boolean failBurpScannerReportScanResultsTest (String scanId) {

        boolean highSeverityFound = false;
        boolean mediumSeverityFound = false;
        final String BURP_SCAN_RESULTS_URL = BURP_SUITE_SCANS_URL + scanId + BURP_SUITE_ISSUES_URL;

        if(scanId == null || scanId.isEmpty()) {
            logger.error("burpScannerReportScanResults: Empty scanId.");
            return true;
        }

        ClientConfig config = new ClientConfig().register(JacksonFeature.class);
        Client client = ClientBuilder.newClient(config);

        Response response = client.target(getBurpSuiteEnterpriseUrl() + BURP_SCAN_RESULTS_URL).request().
                header(HttpHeaders.AUTHORIZATION, authzHeader).
                accept(MediaType.APPLICATION_JSON).get();

        String burpReport = response.readEntity(String.class);

        JsonParser parser = new JsonParser();
        JsonObject jsonReport = parser.parse(burpReport).getAsJsonObject();

        JsonElement issues = jsonReport.get(ISSUES);
        JsonArray issuesArray = issues.getAsJsonArray();

        for (JsonElement issueElement : issuesArray) {
            Gson g = new Gson();
            Issue issue = g.fromJson(issueElement, Issue.class);
            issue.setIssueDefinition(issue.getTypeIndex(), getBurpScannerIssueDefinitions());

            if (issue.getSeverity().equalsIgnoreCase(HIGH_SEVERITY)) {
                highSeverityFound = true;
                logger.debug("High severity issues found: {}", highSeverityFound);
            }

            if (issue.getSeverity().equalsIgnoreCase(MEDIUM_SEVERITY)) {
                mediumSeverityFound = true;
                logger.debug("Medium severity issues found: {}", mediumSeverityFound);
            }

            logger.debug(issue.toString());
        }

        return highSeverityFound || mediumSeverityFound;
    }
}
