package com.hxy.shortmessage;

public class ShortMessageFactory {

	public static <T> T createInstance(Class<T> cls) {
		T obj = null;
		try {
			obj = cls.newInstance();
		} catch (Exception e) {
			obj = null;
		}
		return obj;
	}
}
