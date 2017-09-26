package com.experian.automation;

import com.experian.automation.helpers.Config;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.util.HashMap;

public class WebClient
{

	public HashMap<String,String> stepData;

	public Config config;
	public String context;

	private String screenName;
	private String deviceType;

	public WebDriver driver;
	public HashMap<Integer, WebDriver> driversMap;

	public WebClient() throws IOException, ConfigurationException {
		config = new Config();

		stepData = new HashMap<>();
		driversMap = new HashMap<>();
	}

	public void setCurrentScreen(String screenName)
	{
		this.screenName = screenName;
	}

	public String getCurrentScreen()
	{
		return screenName;
	}

	public boolean isCurrentScreen(String screenName)
	{
		return this.screenName.equals(screenName);
	}

	public String getStepData(String key)
	{
		key = key.replaceAll("\\p{Z}", "_");

		return stepData.get(key);
	}

	public String setStepData(String key, String value)
	{
		key = key.replaceAll("\\p{Z}", "_");

		return stepData.put(key, value);
	}

}

