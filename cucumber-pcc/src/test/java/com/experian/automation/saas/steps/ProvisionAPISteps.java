package com.experian.automation.saas.steps;

import com.experian.automation.helpers.Config;
import com.experian.automation.helpers.ContextResourceManager;
import com.experian.automation.helpers.Variables;
import com.experian.automation.logger.Logger;
import com.experian.automation.saas.helpers.ProvisionAPIOperations;
import com.jayway.jsonpath.JsonPath;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;

public class ProvisionAPISteps {

  private final Logger logger = Logger.getLogger(this.getClass());

  public ProvisionAPISteps() {
  }

  @Given("^The (.*) environment is provisioned$")
  public void initResources(String environmentType) throws Throwable {

    String serviceGroupNameTemplate = Config.get("openshift.service.group.name");
    // Create stand-by resources
    Boolean enableStandByEnvironments = Config.get("openshift.service.group.standby").equals("true");
    // Get number of service groups
    Integer serviceGroupCount = Integer.valueOf(Config.get("openshift.service.group.count"));
    serviceGroupCount = enableStandByEnvironments ? serviceGroupCount * 3 : serviceGroupCount;

    // Add resources to context
    List<String> serviceGroupNames = new ArrayList<>();
    for (int i = 0; i < serviceGroupCount; i++) {
      String serviceGroupName = String.format(serviceGroupNameTemplate,
                                              StringUtils.leftPad(Integer.toString(i + 1), 2, '0'));
      serviceGroupNames.add(serviceGroupName);
    }
    ContextResourceManager.add("openshift-environment", serviceGroupNames);

    // Set environment type
    Variables.set("openshift.servicegroup.type", environmentType);

    // Get action per service group
    HashMap<String, String> serviceGroupActions = new HashMap<>();

    // Skip stand-by service groups actions
    if (enableStandByEnvironments) {

      String serviceGroupToDelete = ContextResourceManager.allocate("openshift-environment");
      String serviceGroupToCreate = ContextResourceManager.allocate("openshift-environment-deleted");

      serviceGroupActions.put("delete", serviceGroupToDelete);

      if (serviceGroupToCreate == null) {
        serviceGroupActions.put("create", ContextResourceManager.allocate("openshift-environment"));
      } else {
        serviceGroupActions.put("create", serviceGroupToCreate);
      }
    }

    String serviceGroupToVerify = ContextResourceManager.allocate("openshift-environment-created");

    if (serviceGroupToVerify == null) {
      serviceGroupActions.put("initial", ContextResourceManager.allocate("openshift-environment"));
    } else {
      serviceGroupActions.put("verify", serviceGroupToVerify);
    }

    // Thread all service group actions
    ForkJoinPool forkPool = new ForkJoinPool(3);
    List<String> actions = new ArrayList<String>(serviceGroupActions.keySet());

    forkPool.submit(() ->
                        actions.parallelStream().forEach(action -> {

                          String serviceGroupName = serviceGroupActions.get(action);

                          logger.info(String.format("Action: %s on service group: %s", action, serviceGroupName));

                          if (action.equals("delete")) {
                            try {
                              deleteServiceGroupIfExists(serviceGroupName, "TRUE");
                              ContextResourceManager.add("openshift-environment-deleted", serviceGroupName);
                            } catch (Throwable throwable) {
                              throwable.printStackTrace();
                              ContextResourceManager.release("openshift-environment", serviceGroupName);
                            }
                          }

                          if (action.equals("create") || action.equals("initial")) {
                            try {
                              deleteServiceGroupIfExists(serviceGroupName, "TRUE");
                              createEnvironment(environmentType, serviceGroupName);
                              ContextResourceManager.add("openshift-environment-created", serviceGroupName);
                            } catch (Throwable throwable) {
                              throwable.printStackTrace();
                              action = "failed";
                              ContextResourceManager.release("openshift-environment", serviceGroupName);
                            }
                            ContextResourceManager.delete("openshift-environment-deleted", serviceGroupName);

                          }

                          if (action.equals("verify") || action.equals("initial")) {
                            try {
                              waitForEnvironment(environmentType, serviceGroupName);
                              ContextResourceManager.add("openshift-environment-verified", serviceGroupName);
                              ContextResourceManager.delete("openshift-environment-created", serviceGroupName);
                            } catch (Throwable throwable) {
                              throwable.printStackTrace();
                            }
                          }

                        })
    ).get();

    String verifiedServiceGroup = ContextResourceManager.allocate("openshift-environment-verified");
    ContextResourceManager.delete("openshift-environment-verified", verifiedServiceGroup);

    Assert.assertTrue(verifiedServiceGroup != null, "Cannot create environment");

    Variables.set("openshift.servicegroup", verifiedServiceGroup);

  }

  @And("^Destroy provisioned environment$")
  public void destroyEnvironment() throws Throwable {

    String serviceGroupName = Variables.get("openshift.servicegroup");

    if (serviceGroupName != null) {
      // Delete service group
      deleteServiceGroupIfExists(serviceGroupName, "TRUE");
    }
  }

  @And("^I create environment of (.*) type in service group (.*)$")
  public void createEnvironment(String type, String serviceGroupName) throws Throwable {

    List<String> serviceNames = getEnvironmentServices(type);

    // Create services
    createServiceGroupIfDoesntExists(serviceGroupName, "TRUE");

    for (String serviceName : serviceNames) {
      createService(serviceName, serviceGroupName, Config.get("openshift.release." + serviceName));
    }
  }

  @And("^I wait for environment of (.*) type in service group (.*) until it is provisioned$")
  public void waitForEnvironment(String type, String serviceGroupName) throws Throwable {

    logger.info(String.format("Verify services in service group %s ", serviceGroupName));

    List<String> serviceNames = getEnvironmentServices(type);

    // Verify services
    verifyServiceHealth(serviceGroupName, serviceNames);
  }

  @And("^I create environment service group (.*)( if it doesn't exist)?$")
  public void createServiceGroupIfDoesntExists(String serviceGroupName, String checkExistence) throws Throwable {

    logger.info(String.format("+Creating service group %s", serviceGroupName));

    ProvisionAPIOperations apiOps = new ProvisionAPIOperations();

    Boolean skipCreation = false;

    if (checkExistence != null) {
      if (apiOps.getServiceGroupID(serviceGroupName) != 0) {
        skipCreation = true;
      }
    }

    if (!skipCreation) {

      // Create Service Group util creation is successful
      Boolean status = false;

      long timeoutTime = System.currentTimeMillis() + 5 * 60 * 1000;
      do {
        // Try to create
        status = apiOps.createServiceGroup(serviceGroupName);

        // Check for service group existence if creation failed
        if (!status) {
          status = apiOps.getServiceGroupID(serviceGroupName) != 0;
        }

        Thread.sleep(5 * 1000);
      } while (!status && System.currentTimeMillis() < timeoutTime);

      Assert.assertTrue(status, String.format("Cannot create service group %s", serviceGroupName));
    }
  }

  @And("^I delete environment service group (.*)( if it exists)?$")
  public void deleteServiceGroupIfExists(String serviceGroupName, String checkExistence) throws Throwable {

    ProvisionAPIOperations apiOps = new ProvisionAPIOperations();

    Boolean skipDeletion = false;

    int serviceGroupID = apiOps.getServiceGroupID(serviceGroupName);

    if (checkExistence != null && serviceGroupID == 0) {
      skipDeletion = true;
    }

    if (!skipDeletion) {

      logger.info(String.format("-Deleting service group %s", serviceGroupName));

      // Delete Service Group
      Boolean status = apiOps.deleteServiceGroup(serviceGroupName);
      Assert.assertTrue(status, String.format("Cannot delete service group %s", serviceGroupName));

      // Wait until service group is not presented in the list with groups
      long timeoutTime = System.currentTimeMillis() + 10 * 60 * 1000;
      do {
        serviceGroupID = apiOps.getServiceGroupID(serviceGroupName);
        Thread.sleep(5 * 1000);
      } while (serviceGroupID != 0 && System.currentTimeMillis() < timeoutTime);

    }
  }

  @And("^I create environment service (.*) in service group (.*) with (.*|LATEST) release$")
  public void createService(String serviceName, String serviceGroupName, String releaseName) throws Throwable {

    ProvisionAPIOperations apiOps = new ProvisionAPIOperations();

    // Get Release Name
    if (releaseName.equals("LATEST")) {
      releaseName = apiOps.getRelease(serviceName);
    }

    logger.info(String.format("++Creating service %s from release %s in service group %s", serviceName, releaseName,
                              serviceGroupName));

    // Create Service util creation is successful
    Boolean status = false;

    long timeoutTime = System.currentTimeMillis() + 5 * 60 * 1000;
    do {
      // Try to create
      status = apiOps.createService(serviceName, serviceGroupName, releaseName);

      // Check for service existence if creation failed
      if (!status) {
        status = apiOps.getServiceID(serviceName, serviceGroupName) != 0;
      }

      Thread.sleep(5 * 1000);
    } while (!status && System.currentTimeMillis() < timeoutTime);

    Assert.assertTrue(status, String.format("Cannot create service %s from release %s in service group %s", serviceName,
                                            releaseName, serviceGroupName));
  }

  @And("^I create environment services in service group (.*):$")
  public void createServices(String serviceGroupName, List<List<String>> services) throws Throwable {
    for (List<String> service : services) {
      String serviceName = service.get(0);
      String releaseName = service.get(1);
      createService(serviceName, serviceGroupName, releaseName);
    }
  }

  @And("^I delete environment service (.*) in service group (.*)$")
  public void deleteService(String serviceName, String serviceGroupName) throws Throwable {

    logger.info(String.format("--Deleting service %s in service group %s", serviceName, serviceGroupName));

    ProvisionAPIOperations apiOps = new ProvisionAPIOperations();

    apiOps.deleteService(serviceName, serviceGroupName);
    Boolean status = apiOps.waitForServiceStatus(serviceName, serviceGroupName, "DEPROVISIONED", 5 * 60 * 1000);

    Assert.assertTrue(status, String.format("Cannot delete %s service from service group %s failed", serviceName,
                                            serviceGroupName));
  }

  @And("^I delete environment services in service group (.*):$")
  public void deleteServices(String serviceGroupName, List<String> services) throws Throwable {
    for (String serviceName : services) {
      deleteService(serviceName, serviceGroupName);
    }
  }

  @And("^I verify the (.*) environment service health in service group (.*)$")
  public void verifyServiceHealth(String serviceName, String serviceGroupName) throws Throwable {

    ProvisionAPIOperations apiOps = new ProvisionAPIOperations();

    Boolean status = apiOps.waitForServiceStatus(serviceName, serviceGroupName, "PROVISIONED", 1 * 60 * 1000);

    String serviceStatus = apiOps.getServiceStatus(serviceName, serviceGroupName);

    Assert.assertTrue(status,
                      String.format("Health check of %s service from service group %s failed. Service status is: %s",
                                    serviceName, serviceGroupName, serviceStatus));

    // Service specific checks
    if (serviceName.equals("pc-acquirecustomersfaster-us")) {

      // Wait for WebEngine service health check
      String webEngineHost = "web-engine." + serviceGroupName + ".apps.appcanvas.net";
      logger.info("WebEngine OpenShift environment service health check: " + serviceGroupName);
      String webEngineHealthCheckURL = "http://" + webEngineHost + "/WebEngine/health";

      HttpResponse<String> response;
      Boolean healthCheckService = false;

      // Wait for 10 min
      long timeoutTime = System.currentTimeMillis() + 10 * 60 * 1000;
      do {
        response = Unirest.get(webEngineHealthCheckURL).asString();
        if (response.getStatus() == 200) {
          healthCheckService = JsonPath.parse(response.getBody()).read("$.WebEngine.healthy");
        }
        Thread.sleep(10 * 1000);
      } while (!healthCheckService && System.currentTimeMillis() < timeoutTime);

      Assert.assertTrue(healthCheckService, "WebEngine service health check failed");
    }

  }

  @And("^I verify the environment services health in service group (.*):$")
  public void verifyServiceHealth(String serviceGroupName, List<String> services) throws Throwable {
    for (String serviceName : services) {
      verifyServiceHealth(serviceName, serviceGroupName);
    }
  }

  @And("^I save property (.*) from service (.*) as (.*) variable$")
  public void getServiceProperty(String property, String service, String variable)
      throws IOException, UnirestException {

    ProvisionAPIOperations apiOps = new ProvisionAPIOperations();

    int serviceGroupID = apiOps.getServiceGroupID(Config.get("openshift.service.group.name"));
    int serviceID = apiOps.getServiceIDbyTemplateID(service, Config.get("openshift.service.group.name"));

    Map<String, String> properties = apiOps.getServiceProperties(serviceGroupID, serviceID);
    Object propertyValue = properties.get(property);

    Variables.set(variable, propertyValue.toString());
  }

  @After
  public void afterScenario(Scenario scenario) throws Throwable {

    // Release resource
    String serviceGroupName = Variables.get("openshift.servicegroup");

    if (serviceGroupName != null) {
      ContextResourceManager.release("openshift-environment", serviceGroupName);
    }

  }

  private List<String> getEnvironmentServices(String type) {

    List<String> serviceNames = Arrays.asList();

    if (type.equals("ACF")) {

      serviceNames = Arrays.asList(
          "pc-acquirecustomersfaster-us",
          "powercurve-simulation",
          "powercurve-connectivity",
          "admin-portal-server",
          "user-service",
          "originations-facade",
          "token-service",
          "admin-portal-ui");
    }

    if (type.equals("UserJourney")) {

      serviceNames = Arrays.asList(
          "pc-userjourney",
          "powercurve-simulation",
          "powercurve-connectivity",
          "admin-portal-server",
          "user-service",
          "originations-facade",
          "token-service",
          "admin-portal-ui");
    }

    return serviceNames;
  }

}
