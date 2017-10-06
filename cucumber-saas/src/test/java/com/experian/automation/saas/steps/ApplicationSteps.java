package com.experian.automation.saas.steps;

import com.experian.automation.harnesses.TestHarness;
import com.experian.automation.harnesses.WebHarness;
import com.experian.automation.logger.Logger;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import java.io.File;

public class ApplicationSteps {

    private final TestHarness testHarness;
    private final WebHarness webHarness;
    private final Logger logger = Logger.getLogger(this.getClass());
    public static Scenario currentScenario;

    public ApplicationSteps(TestHarness testHarness, WebHarness webHarness) {
        this.testHarness = testHarness;
        this.webHarness = webHarness;
    }

    @Before
    public void beforeScenario(Scenario scenario) throws Throwable {
        currentScenario = scenario;
    }

    @After
    public void afterScenario(Scenario scenario) throws Throwable {
        if (scenario.isFailed()) {
            if (webHarness.driver != null) {
                try {

                    logger.error("Url on failure: " + webHarness.driver.getCurrentUrl());

                    File ImageFile = ((TakesScreenshot) webHarness.driver).getScreenshotAs(OutputType.FILE);
                    FileUtils.copyFile(ImageFile,
                            new File(testHarness.config.getAsUnixPath("reports.dir") + "/data-" + scenario.getId().replace(";", "-") + ".png"));
                    logger.embedFileToReport(ImageFile, "image/png");

                    //Quit all started web drivers
                    for (WebDriver driver : webHarness.driversMap.values()) {
                        driver.quit();
                    }
                    webHarness.driversMap.clear();
                } catch (WebDriverException wde) {
                    logger.error(wde.getMessage());
                } catch (ClassCastException cce) {
                    cce.printStackTrace();
                }
            }

        }
    }
}