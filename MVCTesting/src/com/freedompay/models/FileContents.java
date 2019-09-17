package com.freedompay.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import com.freedompay.data.ColumnData;
import com.freedompay.data.FileData;

public class FileContents {
	
	private int fileModelId;
	private List<ArrayList<String>> fileRows;
	protected List<String> headerNames;
	private int rowCount;
	private int columnCount;
	protected List<Integer> selectedColumnsIndexes = null;
	private List<ArrayList<Integer>> invalidRowIntegers;
	
	public FileContents(FileModel model) {
		this.fileModelId = model.getId();
		this.setFileRows(model.getFile());
		this.setHeaderNames();
		this.rowCount = this.fileRows.size();
		this.columnCount = this.headerNames.size();
	}
	
	//======================= FILE MODEL ID =========================
	
	public int getFileModelId() {
		return this.fileModelId;
	}
	
	//========================= FILE ROWS ============================
	
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
	
	public List<ArrayList<String>> getFileRows(){
		return this.fileRows;
	}

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
	
	private void setHeaderNames() {
		headerNames = new ArrayList<String>();
		this.headerNames = this.fileRows.get(0);
	}
	
	public List<String> getHeaderNames(){
		return this.headerNames;
	}
	
	//================ FILE COUNTS - ROW and COLUMN ===================
	
	public int getRowCount() {
		return this.rowCount;
	}
	
	public int getColumnCount() {
		return this.columnCount;
	}
	
	//================== COLUMNS SELECTED FOR COMPARE =================
	
	public void setSelectedColumnIndexes() {
		List<String> cellValues = ColumnData.getCellValues(FileData.getFileModel(this.fileModelId).getFileType());
		
		if(cellValues != null) {
			this.selectedColumnsIndexes = new ArrayList<Integer>();
			for(int i = 0; i < this.getColumnCount(); i++) {
				for(int j = 0; j < cellValues.size(); j++) {
					if(this.headerNames.get(i).equalsIgnoreCase(cellValues.get(j))) {
						this.selectedColumnsIndexes.add(i);	
					}
				}
			}
		}
	}
	
	public void setSelectedColumnIndexes(DefaultTableModel tableData) {
		int rowCount = tableData.getRowCount();
		this.selectedColumnsIndexes = new ArrayList<Integer>();
		
		for(int i = 0; i < this.getColumnCount(); i++) {
			for(int j = 0; j < rowCount; j++) {
				if(tableData.getValueAt(j, 1) != null || tableData.getValueAt(j, 2) != null) {
					this.selectedColumnsIndexes.add(i);
				}
			}
		}
	}
	
	public List<Integer> getSelectedColumnIndexes(){
		return this.selectedColumnsIndexes;
	}
	
	//=================================================================
	
	public List<ArrayList<Integer>> getInvalidRowIntegers(){
		return this.invalidRowIntegers;
	}

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
