package com.freedompay.models;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.freedompay.util.FileType;
import com.freedompay.util.Validation;

public class FileModel extends Model {
	
	private static int lastId = 0;
	
	private int id;
	private FileType type;
	private File file;
	private String name;
	private String absolutePath;	
	private FileContents fileContents;
	
	public FileModel(File f, FileType type) {
		this.id = lastId++;							//Set the Id and increment lastId for next file
		this.file = f;								// The file being uploaded
		this.type = type;							// The enum value for file type: POS, UNCAPTURED_AUTH, CAPTURED
		this.name = f.getName();					// The name of the file with extension
		this.absolutePath = f.getAbsolutePath();	// The path of the file
		this.fileContents = new FileContents(this);
	}
	
	public int getId() {
		return id;
	}
	
	public FileType getFileType() {
		return this.type;
	}
	
	public File getFile() {
		return this.file;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getAbsolutePath() {
		return this.absolutePath;
	}
	
	public FileContents getFileContents() {
		return this.fileContents;
	}
}