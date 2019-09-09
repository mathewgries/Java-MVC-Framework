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
	private long size;
	private int columnCount = 0;
	private int rowCount = 0;	
	
	private List<ArrayList<String>> fileRows = new ArrayList<ArrayList<String>>();
	private List<String> headerNames = new ArrayList<String>();
	private List<ArrayList<Integer>> invalidRowIndexesWithEnumInts = new ArrayList<ArrayList<Integer>>();
	private boolean isValidated = false;
	
	public FileModel(File f, FileType type) {
		this.id = lastId++;							//Set the Id and increment lastId for next file
		this.file = f;								// The file being uploaded
		this.type = type;							// The enum value for file type: POS, UNCAPTURED_AUTH, CAPTURED
		this.name = f.getName();					// The name of the file with extension
		this.absolutePath = f.getAbsolutePath();	// The path of the file
		this.setSize(f);							// KB Size of file
		this.setFileRows(f);						// The file rows
		this.setHeaderNames();							// The headers in the file
		this.setColumnCount();						// How many columns are in the file
	}
	
//	public void toConsole() {
//		System.out.println("Cols: " + this.getColumnCount());
//		System.out.println("Rows: " + this.getRowCount());
//		System.out.println("Rows: " + this.getHeaders().size());
//		System.out.println("Lines: " + this.filelines.size());
//		System.out.println("inavlid: " + this.invalidIndexes.size());;
//		System.out.println("invalid list: " + this.getInvalidLines().size());
//	}
	
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
	
	private void setSize(File f) {
		this.size = f.length();
	}
	
	public long getSize() {
		return this.size;
	}
	
	private void setRowCount() {
		this.rowCount++;
	}
	
	public int getRowCount() {
		return this.rowCount;
	}
	
	private void setColumnCount() {
		this.columnCount = this.getHeaderNames().size();
	}
	
	public int getColumnCount() {
		return this.columnCount;
	}
	
	private void setFileRows(File f) {
		try {
			int row = 0;
			FileReader fr = new FileReader(f.getPath());
			BufferedReader br = new BufferedReader(fr);
			
			String line = "";
			while((line = br.readLine()) != null) {
				row = this.getRowCount();
				String[] columns = line.split(",");
				this.fileRows.add(row, new ArrayList<String>());
				this.invalidRowIndexesWithEnumInts.add(row, new ArrayList<Integer>());
				this.invalidRowIndexesWithEnumInts.get(row).add(row);
				for(int i = 0; i < columns.length; i++) {
					this.fileRows.get(row).add(columns[i]);
				}
				this.setRowCount();
			}
			br.close();
		}catch(FileNotFoundException ex) {
			System.out.println("Error loading file contents");
		}catch(IOException ex) {
			System.out.println("Error loading file contents");
	    }
	}
	
	public List<ArrayList<String>> getFileRows() {
		return this.fileRows;
	}
	
	public void setIsValidated() {
		this.isValidated = this.isValidated ? false : true;
	}
	
	public boolean getIsValidated() {
		return this.isValidated;
	}
	
	public List<ArrayList<Integer>> getInvalidRowIndexesWithEnumInts(){
		List<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
		for(ArrayList<Integer> line : this.invalidRowIndexesWithEnumInts) {
			if(line.size() > 1) {result.add(line);}
		}
		return result;
	}
	
	public List<ArrayList<Integer>> updateInvalidList() {
		System.out.println("Update Invalid From Model");
		return this.invalidRowIndexesWithEnumInts;
	}
	
	public void validateFileRows() {
		Validation.runValidation(this);
	}
	
	private void setHeaderNames() {
		this.headerNames = this.getFileRows().get(0);
	}
	
	public List<String> getHeaderNames(){
		return this.headerNames;
	}
	
}
