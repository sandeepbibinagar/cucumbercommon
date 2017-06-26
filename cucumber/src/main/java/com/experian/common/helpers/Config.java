package com.experian.common.helpers;

import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.util.Properties;

public class Config
{
	private final Properties data;

	public Config() throws IOException
	{
		data = new Properties();
		data.load(getClass().getResourceAsStream("/config/config.properties"));
	}

	public String get(String property)
	{
		return data.getProperty(property);
	}

	public String getAsUnixPath(String property)
	{
		return FilenameUtils.separatorsToUnix(get(property));
	}

	public String getAsWindowsPath(String property)
	{
		return FilenameUtils.separatorsToWindows(get(property));
	}

	public String getAsSystemPath(String property)
	{
		return FilenameUtils.separatorsToSystem(get(property));
	}

	public Properties getProperties()
	{
		return data;
	}
}
