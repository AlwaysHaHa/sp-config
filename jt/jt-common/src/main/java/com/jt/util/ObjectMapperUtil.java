package com.jt.util;


import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperUtil {
	
	private static final ObjectMapper MAPPER= new ObjectMapper();
	/**
	 * 对象转化为json
	 */
	public static String toJSON(Object target) {
		String result =null;
		try {
			result = MAPPER.writeValueAsString(target);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return result;
	}
	
	public static <T> T toObject(String json,Class<T> target) {
		T object = null;
		try {
			object = MAPPER.readValue(json, target);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return object;
	}
}
