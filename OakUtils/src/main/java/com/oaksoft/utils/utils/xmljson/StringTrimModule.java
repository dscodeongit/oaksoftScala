package com.oaksoft.utils.utils.xmljson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;

public class StringTrimModule extends SimpleModule {

	public StringTrimModule() {
		addDeserializer(String.class, new StdScalarDeserializer<String>(String.class) {
			@Override
			public String deserialize(JsonParser jsonParser, DeserializationContext ctx)
					throws IOException, JsonProcessingException {
				final String stringValue = jsonParser.getValueAsString();
				return stringValue.trim();

			}
		});
	}
}
