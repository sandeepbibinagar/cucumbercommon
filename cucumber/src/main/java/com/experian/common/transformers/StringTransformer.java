package com.experian.common.transformers;

import cucumber.api.Transformer;

public class StringTransformer extends Transformer<String>
{
	@Override
	public String transform(String parameter) {

		return parameter.replace("\"", "").replace("'","");
	}
}