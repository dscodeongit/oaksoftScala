package com.oaksoft.utils.utils.xmljson;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public class JsonConverter {
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	public static <T> String convertObjectToJson(T object) {
		if (object != null) {
			try {
				return OBJECT_MAPPER.writer().writeValueAsString(object);
			} catch (IOException arg1) {
				throw new IllegalStateException(arg1);
			}
		} else {
			return null;
		}
	}

	public static <T> String convertObjectToPrettyJson(T object) {
		if (object != null) {
			try {
				return OBJECT_MAPPER.writer().withDefaultPrettyPrinter().writeValueAsString(object);
			} catch (IOException arg1) {
				throw new IllegalStateException(arg1);
			}
		} else {
			return null;
		}
	}

	public static <T> T convertJsonToObject(String json, Class<T> tClass) {
		try {
			return OBJECT_MAPPER.readValue(json, tClass);
		} catch (IOException arg2) {
			throw new IllegalStateException(arg2);
		}
	}

	public static String prettyPrint(String json) throws JsonParseException, JsonMappingException, IOException {
		Object jObj = OBJECT_MAPPER.readValue(json, Object.class);
		return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(jObj);
	}

	public static <T> T convertFileToObject(File file, Class<T> tClass) throws Exception {
		return OBJECT_MAPPER.readValue(new FileInputStream(file), tClass);
	}

	public static ObjectMapper getObjectMapper() {
		return OBJECT_MAPPER;
	}

	static {
		SimpleModule simpleModule = new SimpleModule("SimpleModule",
				new Version(1, 0, 0, (String) null, (String) null, (String) null));
		simpleModule.addSerializer(OffsetDateTime.class, new OffsetDateTimeSerializer());
		simpleModule.addDeserializer(OffsetDateTime.class, new OffsetDateTimeDeserializer());
		simpleModule.addSerializer(LocalDate.class, new LocalDateSerializer());
		simpleModule.addDeserializer(LocalDate.class, new LocalDateDeserializer());
		simpleModule.addDeserializer(String.class, new WhiteSpaceRemovalDeserializer(String.class));
		OBJECT_MAPPER.registerModule(simpleModule);
		OBJECT_MAPPER.setSerializationInclusion(Include.NON_NULL);
		OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		OBJECT_MAPPER.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);

	}
}
