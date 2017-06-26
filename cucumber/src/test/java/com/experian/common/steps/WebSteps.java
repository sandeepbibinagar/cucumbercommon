package com.experian.common.steps;

import com.experian.common.WebClient;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import org.apache.commons.io.FilenameUtils;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;

public class WebSteps {

    private final WebClient webClient;

    public WebSteps(WebClient webClient) {
        this.webClient = webClient;
    }

    @Given("^I start (the|another) browser$")
    public void startBrowser(String browserName) throws Throwable {

        String downloadDir = FilenameUtils.separatorsToSystem(webClient.config.get("temp.dir"));
        String webDriverDir = FilenameUtils.separatorsToSystem(webClient.config.get("webdriver.dir"));

        DesiredCapabilities capabilities;
        Boolean localBrowser = webClient.config.get("selenium.hub.url").isEmpty();

        switch (webClient.config.get("browser")) {
            case "firefox":

                FirefoxProfile firefoxProfile = new FirefoxProfile();
                firefoxProfile.setPreference("browser.download.folderList", 2);
                firefoxProfile.setPreference("browser.helperApps.alwaysAsk.force", false);
                firefoxProfile.setPreference("browser.download.manager.showWhenStarting", false);
                firefoxProfile.setPreference("browser.download.dir", downloadDir);
                firefoxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk", "text/plain,application/rtf");

                capabilities = DesiredCapabilities.firefox();
                capabilities.setCapability(FirefoxDriver.PROFILE, firefoxProfile);

                if (localBrowser) {
                    // Set driver location
                    System.setProperty("webdriver.gecko.driver", webDriverDir + "/" + webClient.config.get("webdriver.gecko.driver"));

                    webClient.driver = new FirefoxDriver(capabilities);
                }

                break;

            case "chrome":

                HashMap<String, Object> chromePrefs = new HashMap<>();
                chromePrefs.put("profile.default_content_settings.popups", 0);
                chromePrefs.put("download.default_directory", downloadDir);
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("prefs", chromePrefs);

                capabilities = DesiredCapabilities.chrome();
                capabilities.setCapability(ChromeOptions.CAPABILITY, options);

                if (localBrowser) {
                    // Set driver location
                    System.setProperty("webdriver.chrome.driver", webDriverDir + "/" + webClient.config.get("webdriver.chrome.driver"));

                    webClient.driver = new ChromeDriver(capabilities);
                }

                break;

            case "ie":

                capabilities = DesiredCapabilities.internetExplorer();
                capabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);

                if (localBrowser) {
                    // Set driver location
                    System.setProperty("webdriver.ie.driver", webDriverDir + "/" + webClient.config.get("webdriver.ie.driver"));
                    webClient.driver = new InternetExplorerDriver(capabilities);
                }

                break;

            default:
                throw new RuntimeException("Invalid value set to 'browser' config property: " + webClient.config.get("browser"));
        }

        // Selenium grid will be used
        if (!localBrowser) {
            String hubURL = webClient.config.get("selenium.hub.url");
            webClient.driver = new RemoteWebDriver(new URL(hubURL), capabilities);
        }

        Integer browserIndex = 1;
        if (browserName.equals("another")) {
            browserIndex = Collections.max(webClient.driversMap.keySet()) + 1;
        }

        //Add the driver to the map
        if (!webClient.driversMap.containsKey(browserIndex)) {
            webClient.driversMap.put(browserIndex, webClient.driver);
        } else {
            throw new RuntimeException("Browser with index '" + browserIndex + "' is already started");
        }


    }

    @And("^I stop the browser$")
    public void stopAllBrowsers() throws Throwable {
        webClient.driver.quit();
    }
}