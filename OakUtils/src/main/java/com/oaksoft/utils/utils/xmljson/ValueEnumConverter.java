package com.oaksoft.utils.utils.xmljson;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;

class ValueEnumConverter implements Converter {

	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		String val = (String) invokeMethod(source, "value");
		writer.setValue(val);
	}

	@SuppressWarnings("unchecked")
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		return getUnmarshallValue(reader, context);
	}

	private Object getUnmarshallValue(HierarchicalStreamReader reader, UnmarshallingContext context) {
		Class type = context.getRequiredType();
		if (type.getSuperclass() != Enum.class) {
			type = type.getSuperclass(); // polymorphic enums
		}
		String name = reader.getValue();
		try {
			return Enum.valueOf(type, name);
		} catch (IllegalArgumentException e) {
			// failed to find it, do a case insensitive match
			for (Enum c : (Enum[]) type.getEnumConstants()) {
				String cVal = (String) invokeMethod(c, "value");
				if (name.equalsIgnoreCase(cVal))
					return c;
			}
			// all else failed
			throw e;
		}
	}

	private Object invokeMethod(Object source, String method) {
		Method valueMethod = null;
		String val = null;
		try {
			valueMethod = source.getClass().getMethod(method, (Class<?>[]) null);
			val = (String) valueMethod.invoke(source, null);
		} catch (NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException
				| IllegalAccessException e) {
			// ignore
		}
		if (val == null) {
			val = source.toString();
		}
		return val;
	}

	@Override
	public boolean canConvert(Class type) {
		return type.isEnum() || Enum.class.isAssignableFrom(type);
	}
}
