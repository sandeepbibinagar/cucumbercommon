package com.experian.common.core.runner;

import cucumber.api.testng.TestNgReporter;
import cucumber.runtime.CucumberException;
import cucumber.runtime.Runtime;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoaderClassFinder;
import cucumber.runtime.model.CucumberFeature;
import cucumber.runtime.model.CucumberTagStatement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomTestNGCucumberRunner
{
	private final Runtime runtime;

	private MultiLoader resourceLoader;
	private RuntimeOptions runtimeOptions;

	public CustomTestNGCucumberRunner(Class clazz, CustomCucumberOptions customOptions)
	{
		ClassLoader classLoader = clazz.getClassLoader();
		resourceLoader = new MultiLoader(classLoader);
		CustomRuntimeOptionsFactory runtimeOptionsFactory = new CustomRuntimeOptionsFactory(clazz, customOptions);
		runtimeOptions = runtimeOptionsFactory.create();
		TestNgReporter reporter = new TestNgReporter(System.out);
		runtimeOptions.addPlugin(reporter);
		ResourceLoaderClassFinder classFinder = new ResourceLoaderClassFinder(resourceLoader, classLoader);
		this.runtime = new Runtime(resourceLoader, classFinder, classLoader, runtimeOptions);
	}

	public void runCukes()
	{
		try
		{
			this.runtime.run();
		} catch (IOException var2)
		{
			throw new RuntimeException(var2);
		}

		if (!this.runtime.getErrors().isEmpty())
		{
			throw new CucumberException((Throwable) this.runtime.getErrors().get(0));
		} else if (this.runtime.exitStatus() != 0)
		{
			throw new CucumberException("There are pending or undefined steps.");
		}
	}


	public List<String> getScenarios()
	{
		String path;
		List<String> scenarioPaths = new ArrayList<String>();

		List<CucumberFeature> features = runtimeOptions.cucumberFeatures(resourceLoader);

		for (CucumberFeature cucumberFeature : features)
		{
			for (CucumberTagStatement scenario : cucumberFeature.getFeatureElements())
			{
				path = "classpath:" + cucumberFeature.getPath() + ":" + scenario.getGherkinModel().getLine();
				scenarioPaths.add(path);
			}
		}

		return scenarioPaths;
	}

	//
	public List<String> getFeatures()
	{
		String path;
		List<String> featuresPaths = new ArrayList<String>();

		List<CucumberFeature> features = runtimeOptions.cucumberFeatures(resourceLoader);

		for (CucumberFeature cucumberFeature : features)
		{
			path = "classpath:" + cucumberFeature.getPath();
			featuresPaths.add(path);
		}

		return featuresPaths;
	}

}
