package com.oaksoft.utils.utils.xmljson;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.mapper.Mapper;
import com.thoughtworks.xstream.mapper.MapperWrapper;

public class JsonXmlUtils {

	private static XStream xstream;

	static {
		xstream = new XStream(new StaxDriver()) {
			protected MapperWrapper wrapMapper(MapperWrapper next) {
				return new PackageStrippingMapper(next);
			}
		};
		xstream.registerConverter(new ValueEnumConverter());
	}

	public static <T> String toXml(T obj) {
		String xml = xstream.toXML(obj);
		return xml;
	}
	
	public static <T> Object xmlToJava(String xml) throws InstantiationException, IllegalAccessException {
		return xstream.fromXML(xml);
	}
	
	public static <T> String xmlToJson(String xml, Class<T> c) {
		return JsonConverter.convertObjectToJson(xstream.fromXML(xml, c));
	}

	public static <T> String jsonToXml(String json, Class<T> c) {
		String xml = xstream.toXML(JsonConverter.convertJsonToObject(json, c));
		return xml;
	}

	public static class PackageStrippingMapper extends MapperWrapper {
		public PackageStrippingMapper(Mapper wrapped) {
			super(wrapped);
		}

		public String serializedClass(Class type) {
			return type.getName().replaceFirst(".*\\.", "");
		}
	}

}
