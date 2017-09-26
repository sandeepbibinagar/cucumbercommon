package com.experian.automation.transformers;
import com.experian.automation.helpers.Config;
import cucumber.api.Transformer;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataTransformer extends Transformer<String> {

	@Override
	public String transform(String parameter) {
		return transformSingleValue(parameter, null, true);
	}

	public static Config config;

	public static String transformSingleValue(String parameter, Map<String, String> replacements, boolean decodeSpecialSymbols) {

		if (decodeSpecialSymbols){
			parameter = decodeSpecialSymbols(parameter);
		}

		Pattern pattern = Pattern.compile("(\\$)?\\$\\{(.+?)\\}");
		Matcher matcher = pattern.matcher(parameter);
		StringBuilder result = new StringBuilder();
		int i = 0;
		while (matcher.find()) {
			String replacement = "";
			if (matcher.group(1) != null) {
				replacement = "${" + matcher.group(2) + "}";
			} else {
				replacement = getReplacementValue(matcher.group(2), replacements);
			}
			result.append(parameter.substring(i, matcher.start()));
			if (replacement == null)
				result.append(matcher.group(0));
			else
				result.append(replacement);
			i = matcher.end();
		}
		result.append(parameter.substring(i, parameter.length()));
		return result.toString();
	}

	public static String transformSingleValue(String parameter, Map<String, String> replacements) {
		return transformSingleValue(parameter, replacements, true);
	}


	public static String decodeSpecialSymbols(String parameter) {
		String[] findWhat = new String[]{"\\n", "\\t", "\\r"};
		String[] replaceWith = new String[]{"\n", "\t", "\r"};

		for (int i = 0; i < findWhat.length; i++) {
			parameter = parameter.replace(findWhat[i], replaceWith[i]);
		}
		return parameter;
	}

	private static String getReplacementValue(String key, Map<String, String> replacements) {
		if (config == null) {
			try {
				config = new Config();
			} catch (IOException|ConfigurationException ex) {
				throw new IllegalArgumentException(ex);
			}
		}
		if (System.getProperty(key) != null) {
			return System.getProperty(key);
		} else if (replacements != null && replacements.get(key) != null) {
			return replacements.get(key);
		} else if (config.get(key) != null) {
			return config.get(key);
		}
		throw new IllegalArgumentException("Transformation:  missing value for key " + key);
	}

	public static Map<String, String> transformMap(Map<String, String> input, Map<String, String> replacements) {
		Map<String, String> result = new LinkedHashMap<>();
		for (Map.Entry<String, String> entry : input.entrySet()) {
			result.put(transformSingleValue(entry.getKey(), replacements), transformSingleValue(entry.getValue(), replacements));
		}
		return result;
	}

	public static List<String> transformList(List<String> input, Map<String, String> replacements) {
		List<String> result = new ArrayList<>();
		for (String value : input) {
			result.add(transformSingleValue(value, replacements));
		}
		return result;
	}

	public static List<List<String>> transformTable(List<List<String>> input, Map<String, String> replacements) {
		List<List<String>> result = new ArrayList<>();
		for (List<String> row : input) {
			result.add(transformList(row, replacements));
		}
		return result;
	}
}