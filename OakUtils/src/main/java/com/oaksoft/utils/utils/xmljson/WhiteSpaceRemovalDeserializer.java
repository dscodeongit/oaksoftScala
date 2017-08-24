package com.oaksoft.utils.utils.xmljson;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;

public class WhiteSpaceRemovalDeserializer extends StdScalarDeserializer<String> {
	protected WhiteSpaceRemovalDeserializer(Class<String> vc) {
		super(vc);
	}

	@Override
	public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException  {
		return StringUtils.trim(jp.getValueAsString());
	}
}
