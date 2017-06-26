package com.experian.common.core.runner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;

public class ReportGenerator
{

    public static void main(String [] args) throws IOException {

        // create the command line parser
        CommandLineParser parser = new DefaultParser();

        // create the Options
        Options options = new Options();
        options.addOption("i", "input", true, "Path to directory where cucumber json files are created");
        options.addOption("o", "output", true, "Path to directory where reports will be stored");

        try {
            CommandLine line = parser.parse(options, args);

            String inputDirPath = FilenameUtils.separatorsToSystem(line.getOptionValue("input", "target/").trim());
            String outputDirPath = FilenameUtils.separatorsToSystem(line.getOptionValue("output", "target/report/").trim());
            File reportDir = new File(outputDirPath);

            if( !reportDir.exists() ){
                FileUtils.forceMkdir(reportDir);
            }

            generateReport(inputDirPath, outputDirPath);

        } catch (ParseException exp) {
            System.out.println("Unexpected exception:" + exp.getMessage());
        }
    }

    public static void generateReport(String inputDirPath, String outputDirPath) throws IOException
    {

        JSONParser parser = new JSONParser();
        JSONArray report = new JSONArray();

        try
        {

            for (File file : new File(inputDirPath).listFiles()) {
                if (file.isFile() && file.getName().startsWith("cucumber") && file.getName().endsWith(".json") )
                {
                    JSONArray jsonArray = new JSONArray();
                    try
                    {
                        jsonArray = (JSONArray) parser.parse(new FileReader(file));
                    } catch (Exception e)
                    {
                    }

                    if (jsonArray.size() > 0)
                    {

                        // Try to add scenario to feature
                        String scenarioURI = (String) ((JSONObject) jsonArray.get(0)).get("uri");
                        JSONArray scenarioElements = (JSONArray) ((JSONObject) jsonArray.get(0)).get("elements");
                        Boolean featureFound = false;

                        for (int i = 0; i < report.size(); i++)
                        {
                            String featureURI = (String) ((JSONObject) report.get(i)).get("uri");

                            if( featureURI.equals(scenarioURI) ){

                                JSONArray featureElements = (JSONArray) ((JSONObject) report.get(i)).get("elements");
                                featureElements.addAll(scenarioElements);

                                featureFound = true;
                            }
                        }

                        // Add new feature
                        if(featureFound == false)
                            report.add(jsonArray.get(0));
                    }
                }
            }

            File jsonFile = new File(outputDirPath + "/cucumber.json");
            Writer jsonWriter = new BufferedWriter(new FileWriter(jsonFile.getAbsoluteFile()));
            PrintWriter jsonOut = new PrintWriter(jsonWriter);

            File xmlFile = new File(outputDirPath + "/cucumber.xml");
            Writer xmlWriter = new BufferedWriter(new FileWriter(xmlFile.getAbsoluteFile()));
            PrintWriter xmlOut = new PrintWriter(xmlWriter);

            try {
                jsonOut.print(report.toString());
                jsonOut.flush();
                jsonWriter.flush();

                ObjectMapper mapper = new ObjectMapper();
                JsonNode actualObj = mapper.readTree("{ \"cucumber\":" + report.toString() + "}");

                XmlMapper xmlMapper = new XmlMapper();
                String cucumberXML = xmlMapper.writeValueAsString(actualObj);

                xmlOut.print(cucumberXML);
                xmlOut.flush();
                xmlWriter.flush();

            } finally {
                jsonWriter.close();
                jsonOut.close();
                xmlWriter.close();
                xmlOut.close();
            }

            System.out.println(
                    "========================================================================\n" +
                    " Cucumber report is ready!  \n" +
                    "========================================================================");

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
