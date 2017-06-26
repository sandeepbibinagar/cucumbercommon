package com.experian.common.core.runner;

import com.experian.common.RunTest;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class SuiteGenerator
{

	public static void main(String [] args) throws IOException
	{
		// create the command line parser
		CommandLineParser parser = new  DefaultParser();

		// create the Options
		Options options = new Options();
		options.addOption("s", "suite", true, "path to TestNG suite file");
		options.addOption("t", "threads", true, "Number of parallel threads");

		try
		{
			CommandLine line = parser.parse(options, args);

			String suiteXMLFilePath = line.getOptionValue("suite", "target/testng-suite.xml").trim();
			int threads = Integer.parseInt(line.getOptionValue("threads", "1").trim());

			generateSuite(suiteXMLFilePath, threads);

		} catch (ParseException exp)
		{
			System.out.println("Unexpected exception:" + exp.getMessage());
		}

	}

	public static void generateSuite(String suiteXMLFilePath, int threads) throws IOException
	{
		XmlSuite suite = new XmlSuite();
		suite.setName("CucumberSuite");
		suite.setParallel(XmlSuite.ParallelMode.TESTS);
		suite.setThreadCount(threads);

		CustomCucumberOptions customOptions = new CustomCucumberOptions();
		customOptions.setDefaultTags();

		for (String scenarioPath  : new CustomTestNGCucumberRunner(RunTest.class, customOptions).getFeatures() )
		{
			XmlTest test = new XmlTest(suite);
			test.setName("CucumberScenario - " + scenarioPath);
			test.setParameters( ImmutableMap.of("scenario", scenarioPath) );

			List<XmlClass> classes = new ArrayList<XmlClass>();
			classes.add(new XmlClass(RunTest.class));
			test.setXmlClasses(classes);
		}

		List<XmlSuite> suites = new ArrayList<XmlSuite>();
		suites.add(suite);

		FileUtils.writeStringToFile(new File(suiteXMLFilePath), suite.toXml(), Charset.defaultCharset());
	}
}
