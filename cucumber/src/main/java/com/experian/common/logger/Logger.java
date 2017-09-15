package com.experian.common.logger;

import com.experian.common.steps.CucumberSteps;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriverException;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Logger {

    private static org.slf4j.Logger logger;

    private Logger(String name) {
        logger = LoggerFactory.getLogger(name);
    }

    public static Logger getLogger(String name) {
        return new Logger(name);
    }

    public static Logger getLogger(Class clazz) {
        return new Logger(clazz.getName());
    }

    public void info(String message) {

        if (logger.isInfoEnabled()) {
            logToReport(message);
        }
    }

    public void debug(String message) {
        if (logger.isDebugEnabled()) {
            logToReport(message);
        }
    }

    public void warn(String message) {
        if (logger.isWarnEnabled()) {
            logToReport(message);
        }
    }

    public void error(String message) {
        if (logger.isErrorEnabled()) {
            logToReport(message);
        }
    }

    private void logToReport(String message) {
        if (CucumberSteps.currentScenario != null) {
            CucumberSteps.currentScenario.write(message);
        } else {
            logger.warn("Cucumber scenario is not initialized, cannot write message to the report");
        }
    }

    public void embedFileToReport(String filePath) {
        embedFileToReport(new File(filePath), null);
    }

    public void embedFileToReport(String filePath, String mimeType) {
        embedFileToReport(new File(filePath), mimeType);
    }

    public void embedFileToReport(File file) {
        embedFileToReport(file, null);
    }

    public void embedFileToReport(File file, String mimeType) {

        if (!file.exists()) {
            logger.error("Cannot embed file " + file.getAbsolutePath() + " to report, it does not exist!");
            return;
        }

        if (CucumberSteps.currentScenario != null) {
            try {
                byte[] fileData = Files.readAllBytes(file.toPath());
                if (StringUtils.isEmpty(mimeType)) {
                    mimeType = Files.probeContentType(file.toPath());
                }
                CucumberSteps.currentScenario.embed(fileData, mimeType);
            } catch (WebDriverException wde) {
                logger.error(wde.getMessage());
            } catch (IOException | ClassCastException ex) {
                ex.printStackTrace();
            }
        } else {
            logger.warn("Cucumber scenario is not initialized, cannot embed file to the report");
        }
    }
}
