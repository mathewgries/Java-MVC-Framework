package com.freedompay.models;

import java.io.*;

public class FileModel extends Model {
	
	private static long lastId = 0;
	
	private long id;
	private File file;
	private String name;
	private int size;
	private int columnCount;
	private int rowCount;
	
	public FileModel() {
		this.id = ++lastId;
	}
	
	public void setFile(File f) {
		this.file = f;
	}
	
	public File getFile() {
		return this.file;
	}
	
	public void setName(String n) {
		this.name = n;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setSize(int s) {
		this.size = s;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public void setRowCount(int rc) {
		this.rowCount = rc;
	}
	
	public int getRowCount() {
		return this.rowCount;
	}
	
	public void setColumnCount(int cc) {
		this.columnCount = cc;
	}
	
	public int getColumnCount() {
		return this.columnCount;
	}
	
}
