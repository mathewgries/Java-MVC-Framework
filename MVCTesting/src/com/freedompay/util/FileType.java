package com.freedompay.util;

import java.util.HashMap;
import java.util.Map;

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
