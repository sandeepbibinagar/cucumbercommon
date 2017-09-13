package com.experian.common.helpers;

import com.experian.common.config.CustomConversionHandler;
import com.experian.common.config.CustomLookup;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.interpol.ConfigurationInterpolator;
import org.apache.commons.configuration2.io.FileHandler;
import org.apache.commons.configuration2.io.FileLocator;
import org.apache.commons.configuration2.io.FileLocatorUtils;
import org.apache.commons.io.FilenameUtils;
import java.io.IOException;

public class Config {
	private final PropertiesConfiguration data;

	public Config() throws IOException, ConfigurationException {
		String basePath = getClass().getResource("/config/").getPath();
		data = new PropertiesConfiguration();
		data.setConversionHandler(new CustomConversionHandler());

		ConfigurationInterpolator ci = new ConfigurationInterpolator();
		ci.addDefaultLookup(new CustomLookup(data));
		data.setInterpolator(ci);
		FileLocator locator = FileLocatorUtils.fileLocator().basePath(basePath).fileName("config.properties").create();
		FileHandler handler = new FileHandler(data);
		handler.setFileLocator(locator);
		handler.load();
	}

	public String get(String property) {
		if (data.containsKey(property)) {
			return data.getString(property);
		} else {
			return null;
		}
	}

	public String getAsUnixPath(String property) {
		return FilenameUtils.separatorsToUnix(get(property));
	}

	public String getAsWindowsPath(String property) {
		return FilenameUtils.separatorsToWindows(get(property));
	}

	public String getAsSystemPath(String property) {
		return FilenameUtils.separatorsToSystem(get(property));
	}

	public PropertiesConfiguration getPropertiesConfiguration() {
		return data;
	}
}
