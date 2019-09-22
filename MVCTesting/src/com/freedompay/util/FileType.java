package com.freedompay.util;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Enum for the different file types to keep track of
 * which file is which when making selections in the app
 * </p>
 * @author MGries
 *
 */
public enum FileType {
	POS(1),
	UNCAPTURED_AUTH(2),
	CAPTURED(3);
	
	private int value;
	private static Map<Object, Object> map = new HashMap<>();
	
	private FileType(int value) {
		this.value = value;
	}
	
	static {
		for(FileType fileType : FileType.values()) {
			map.put(fileType.value, fileType);
		}
	}
	
	public static FileType valueOf(int fileType) {
		return (FileType) map.get(fileType);
	}
	
	public int getValue() {
		return value;
	}
}