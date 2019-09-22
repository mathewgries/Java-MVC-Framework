package com.freedompay.util;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Enums of the different validation errors that can be found
 * in the files. Can also be represented by an Integer.
 * </p>
 * @author MGries
 *
 */
public enum ErrorType {
	NULL_OR_EMPTY_VALUE(1),
	INVALID_PAN(2),
	NO_REQUESTID(3),
	NON_NUMERIC(4),
	DUPLICATED_RECORD(5);
	
	private int value;
	private static Map<Object, Object> map = new HashMap<>();
	
	private ErrorType(int value) {
		this.value = value;
	}
	
	static {
		for(ErrorType errorType : ErrorType.values()) {
			map.put(errorType.value, errorType);
		}
	}
	
	public static ErrorType valueOf(int errorType) {
		return (ErrorType) map.get(errorType);
	}
	
	public int getValue() {
		return value;
	}
}
