package com.experian.common.steps;

import com.experian.common.WebClient;
import com.experian.common.core.logger.Logger;
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

    private final WebClient webClient;
    private final Logger logger = Logger.getLogger(this.getClass());
    public static Scenario currentScenario;

    public ApplicationSteps(WebClient webClient) {
        this.webClient = webClient;
    }

    @Before
    public void beforeScenario(Scenario scenario) throws Throwable {
        currentScenario = scenario;
    }

    @After
    public void afterScenario(Scenario scenario) throws Throwable {
        if (scenario.isFailed()) {
            if (webClient.driver != null) {
                try {

                    logger.error("Url on failure: " + webClient.driver.getCurrentUrl());

                    File ImageFile = ((TakesScreenshot) webClient.driver).getScreenshotAs(OutputType.FILE);
                    FileUtils.copyFile(ImageFile,
                            new File(webClient.config.getAsUnixPath("reports.dir") + "/data-" + scenario.getId().replace(";", "-") + ".png"));
                    logger.embedFileToReport(ImageFile, "image/png");

                    //Quit all started web drivers
                    for (WebDriver driver : webClient.driversMap.values()) {
                        driver.quit();
                    }
                    webClient.driversMap.clear();
                } catch (WebDriverException wde) {
                    logger.error(wde.getMessage());
                } catch (ClassCastException cce) {
                    cce.printStackTrace();
                }
            }

        }
    }
}