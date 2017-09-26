package com.experian.automation.helpers;

import org.apache.commons.configuration2.ex.ConfigurationException;
import java.io.IOException;

public class DBRuntime extends DBOperations {
    public final Config config;

    public DBRuntime() throws IOException, ConfigurationException {
        config = new Config();
        driverClassName = config.get("database.driver");
        url = config.get("database.url");
        userName = config.get("database.user");
        password = config.get("database.password");
    }
}
