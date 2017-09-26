package com.experian.automation.runner;

import com.experian.automation.helpers.Config;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomCucumberOptions
{
	private List<String> features = new ArrayList<String>();
	private List<String> tags = new ArrayList<String>();
	private List<String> plugins = new ArrayList<String>();
	

	public List<String> getFeatures()
	{
		return features;
	}

	public void setFeatures(List<String> features)
	{
		for (String feature : features)
		{
			if ( !feature.trim().isEmpty())
				{
				this.features.add(feature);
			}
		}
	}

	public List<String> getTags()
	{
		return tags;
	}

	public void setTags(List<String> tags)
	{
		this.tags = tags;
	}

	public List<String> getPlugins()
	{
		return plugins;
	}

	public void setPlugins(List<String> plugins)
	{
		this.plugins = plugins;
	}

	public void setDefaultTags()
	{
		if (features.size() == 0)
		{
			try
			{
				Config config = new Config();

				for (String tag : config.get("execution.tags").split(" "))
				{
					tags.add(tag);
				}
			} catch (IOException e)
			{
				e.printStackTrace();
			} catch (ConfigurationException e) {
				e.printStackTrace();
			}
		}
	}

}
