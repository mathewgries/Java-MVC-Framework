package com.freedompay.models;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.freedompay.data.FileData;
import com.freedompay.util.FileType;

/**
 * <p>
 * The file contents for the files uploaded to FileModel.
 * This class is assigned in the FileModel class constructor
 * </p>
 * @author MGries
 *
 */
public class FileContents {
	
	private int fileModelId;
	private List<ArrayList<String>> fileRows;
	private List<String> headerNames;
	private int requestIdPos;
	private int rowCount;
	private int columnCount;
	private List<String> selectedHeaders = new ArrayList<String>();
	private List<ArrayList<Integer>> invalidRowIntegers = null;
	
	public FileContents(FileModel model) {
		this.fileModelId = model.getId();
		this.setFileRows(model.getFile());
		this.setHeaderNames();
		this.rowCount = this.fileRows.size();
		this.columnCount = this.headerNames.size();
		if(model.getFileType() != FileType.POS) {
			this.setRequestId();
		}
	}
	
	//======================= FILE MODEL ID =========================
	
	/**
	 * The FileModel id this is assigned to
	 * @return
	 */
	public int getFileModelId() {
		return this.fileModelId;
	}
	
	//========================= FILE ROWS ============================
	
	/**
	 * <p>Stores the files rows to a List</p>
	 * @param f the file object
	 */
	private void setFileRows(File f) {
		fileRows = new ArrayList<ArrayList<String>>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(f.getPath()));
			String line = "";
			while((line = br.readLine()) != null) {
				this.fileRows.add(new ArrayList<String>(Arrays.asList(line.split(","))));
			}
			br.close();
		}catch(IOException ex) {
			System.out.println("Error loading file contents");
		}
	}
	
	/**
	 * <p>
	 * The rows for the uploaded file
	 * </p>
	 * @return The file rows
	 */
	public List<ArrayList<String>> getFileRows(){
		return this.fileRows;
	}

	/**
	 * <p>
	 * Print the file rows to string
	 * </p>
	 * @return The file rows
	 */
	public String fileRowsToString() {
		String result = "";
		for(int i = 0; i < this.fileRows.size(); i++) {
			for(int j = 0; j < this.fileRows.get(i).size(); j++) {
				result += this.fileRows.get(i).get(j) + ", ";
			}
			result += "\n";
		}
		return result;
	}
	
	//===================== HEADER NAMES ==============================
	
	/*
	 * <p>Set the file headers to a list</p>
	 */
	private void setHeaderNames() {
		headerNames = new ArrayList<String>();
		this.headerNames = this.fileRows.get(0);
	}
	
	/**
	 * <p>Retrieve the full list of file headers</p>
	 * @return The file headers
	 */
	public List<String> getHeaderNames(){
		return this.headerNames;
	}
	
	private void setRequestId() {
		int index = 0;
		for(String header : this.getHeaderNames()) {
			if(header.equalsIgnoreCase("REQUESTID")) {
				this.requestIdPos = index;
			}
			index++;
		}
	}
	
	public int getRequestIdPos() {
		return this.requestIdPos;
	}
	
	//================ FILE COUNTS - ROW and COLUMN ===================
	
	/**
	 * <p>Count of rows in the file</p>
	 * @return The file row count
	 */
	public int getRowCount() {
		return this.rowCount;
	}
	
	/**
	 * <p>Count of columns in the file</p>
	 * @return The file column count
	 */
	public int getColumnCount() {
		return this.columnCount;
	}
	
	//================== COLUMNS SELECTED FOR COMPARE =================
	
	/**
	 * <p>Store selected headers for comparison by value</p>
	 * @param addHeader The header being added to the list
	 * @param removeHeader The header being removed from the list
	 */
	public void updateSelectedHeaders(String addHeader, String removeHeader) {
		if(removeHeader != null && this.selectedHeaders.contains(removeHeader)) {
			this.selectedHeaders.remove(removeHeader);
		}
		if(addHeader != null && !this.selectedHeaders.contains(addHeader)) {
			this.selectedHeaders.add(addHeader);
		}
	}
	
	/**
	 * <p>Return the list of selected headers for comparison</p>
	 * @return the selected header list
	 */
	public List<String> getSelectedHeaders(){
		return this.selectedHeaders;
	}
	
	/**
	 * <p>
	 * Indexes of the headers selected for comparison
	 * Helps with seperating out the columns selected
	 * for comparison when validating and comparing
	 * </p>
	 * @return
	 */
	public List<Integer> getHeadersIndexes(){
		List<Integer> indexes = new ArrayList<Integer>();
		// Loop through all file headers
		for(int i = 0; i < this.headerNames.size(); i++) {
			// Pull out the header value
			String header = this.headerNames.get(i);
			// Loop through the selected header list
			for(int j = 0; j < this.selectedHeaders.size(); j++) {
				// When a match is found, store the index from the main list
				if(header.equalsIgnoreCase(this.selectedHeaders.get(j))) {
					indexes.add(i);
				}
			}
		}
		return indexes;
	}
	
	public Integer getHeaderIndex(String h){
		Integer index = 0;
		for(String header: this.headerNames) {
			if(h.equalsIgnoreCase(header)) {
				break;
			}
			index++;
		}
		return index;
	}
	
	/**
	 * <p>
	 * Print the selected column list to String
	 * </p>
	 */
	public void printSelected() {
		for(String header : this.selectedHeaders) {
			System.out.println(this.getFileModelId()+ ": " + header);
		}
	}
	
	//=================================================================
	
	/**
	 * Initialize the list that holds the integers for storing
	 * invalid row indexes and their Enum ErrorType int values.
	 * Creates one row for each row in the file, intitialized with
	 * the rows index. The Enum ints are added to the rows during
	 * the validation in the static Validation class.
	 * @return Newly intitialized initInvalidRowInteger list
	 */
	public List<ArrayList<Integer>> initInvalidRowIntegers() {
		this.invalidRowIntegers = new ArrayList<ArrayList<Integer>>();
		for(int i = 0; i < this.getRowCount(); i++) {
			this.invalidRowIntegers.add(new ArrayList<Integer>());
			this.invalidRowIntegers.get(i).add(i);
		}
		return this.invalidRowIntegers;
	}
	
	/**
	 * <p>
	 * Return the completed list of invalid rows with Enum ErrorType ints
	 * </p>
	 * @return invalidRowIntegers
	 */
	public List<ArrayList<Integer>> getInvalidRowIntegers(){
		return this.invalidRowIntegers;
	}

	/**
	 * <p>
	 * Print the invalidRowsIntegers to String
	 * </p>
	 * @return invalidRowIntegers in String form
	 */
	public String invalidRowIntegersToString() {
		String result = "";
		for(int i = 0; i < this.invalidRowIntegers.size(); i++) {
			for(int j = 0; j < this.invalidRowIntegers.get(i).size(); j++) {
				result += this.invalidRowIntegers.get(i).get(j) + ", ";
			}
			result += "\n";
		}
		return result;
	}
}
