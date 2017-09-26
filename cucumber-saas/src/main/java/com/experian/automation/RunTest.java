package com.experian.automation;

import com.experian.automation.runner.CustomCucumberOptions;
import com.experian.automation.runner.CustomTestNGCucumberRunner;
import cucumber.api.CucumberOptions;
import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestResult;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


import java.io.IOException;
import java.util.Arrays;

//@RunWith(Cucumber.class)
@CucumberOptions(
		plugin = {"pretty"},
		format = {"rerun:target/rerun.txt"}
)
public class RunTest implements IHookable
{
	public RunTest() {
	}

	@Test(
			groups = {"cucumber"},
			description = "Runs Cucumber Features"

	)
	@Parameters("scenario")
	public void run_cukes(@Optional("") String scenario) throws IOException
	{
		CustomCucumberOptions customOptions = new CustomCucumberOptions();
		customOptions.setFeatures(Arrays.asList(scenario));
		customOptions.setPlugins(Arrays.asList("json:target/cucumber" + scenario.replaceAll("[:|/]","-") + ".json"));
		customOptions.setDefaultTags();

		(new CustomTestNGCucumberRunner(this.getClass(), customOptions)).runCukes();
	}

	public void run(IHookCallBack iHookCallBack, ITestResult iTestResult) {
		iHookCallBack.runTestMethod(iTestResult);
	}

}