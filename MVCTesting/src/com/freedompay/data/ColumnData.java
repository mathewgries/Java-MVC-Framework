package com.freedompay.data;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import com.freedompay.models.FileModel;
import com.freedompay.util.FileType;

public class ColumnData {

	private static String[] tableHeaders = {"POS","Uncaptured", "Captured"};
	private static DefaultTableModel tableDataModel = null;
	
	public static void setPOSHeadersToTableData(List<String> cols) {
		tableDataModel.setRowCount(0);
		for(String col : cols) {
			tableDataModel.addRow(new String[] {col, null, null});
		}
	}
	
	public static DefaultTableModel getTableData(){
		if(ColumnData.tableDataModel == null && !FileData.getIsPOSLoadedFlag()) {
			ColumnData.tableDataModel = new DefaultTableModel(ColumnData.tableHeaders, 0); 
		}
		return ColumnData.tableDataModel;
	}
	
	public static void updateTableData(String value, int selectedRow, int column) {
		ColumnData.tableDataModel.setValueAt(value, selectedRow, column);
	}
	
	public static void clearCell(int row, int col) {
		if(ColumnData.tableDataModel.getValueAt(row, col) != null) {
			ColumnData.tableDataModel.setValueAt(null, row, col);	
		}
	}
	
	public static void clearMatchedCells(FileType type) {
		int removeColumn = -1;
		int rowCount = ColumnData.tableDataModel.getRowCount();
		switch(type) {
			case UNCAPTURED_AUTH:
				removeColumn = 1;
				break;
			case CAPTURED:
				removeColumn = 2;
				break;
			default:
				break;
		}
		for(int i = 0; i < rowCount; i++) {
			ColumnData.tableDataModel.setValueAt(null, i, removeColumn);
		}
	}
	
	public static List<String> getCellValues(FileType type){
		List<String> result = new ArrayList<String>();
		int rowCount = ColumnData.tableDataModel.getRowCount();
		int column = -1;
		
		switch(type) {
			case UNCAPTURED_AUTH:
				column = 1;
				break;
			case CAPTURED:
				column = 2;
				break;
			default:
				result = null;
				break;
		}
		
		for(int i = 0; i < rowCount; i++) {
			if(ColumnData.tableDataModel.getValueAt(i, column) != null) {
				result.add((String)ColumnData.tableDataModel.getValueAt(i, column));
			}
		}
		
		if(result.size() <= 0) {
			System.out.println("results is Null");
			result = null;
		}
		
		return result;
	}
	
	//-----------------------------------------------------------------------------------
	
	private static int posHeadersSize = 0;
	
	// Contains one row for each header in POS file
	// Each row contains the index from the correpsonding file's selected header to match
	// with the header from the POS file.
	private static List<ArrayList<Integer>> matchedOnAuth = null;
	private static List<ArrayList<Integer>> matchedOnBatched = null;
	
	public static void printList(List<ArrayList<Integer>> list) {
		for(int i = 0; i < list.size(); i++) {
			for(int j = 0; j < list.get(i).size(); j++) {
				System.out.print(list.get(i).get(j) + ",");
			}
			System.out.println();
		}
	}
	
	public static void setPosHeadersSize(int size) {
		ColumnData.posHeadersSize = size;
	}
	
	public static int getPosHeadersSize() {
		return ColumnData.posHeadersSize;
	}
	
	public static void setMatchedIndexes() {
		List<FileModel> models = FileData.getAllFileModels();
		for(FileModel model : models) {
			if(model.getFileType() == FileType.POS) {
				ColumnData.setMatchedOnAuth();
				ColumnData.setMatchedOnBatched();
			}
			if(model.getFileType() == FileType.UNCAPTURED_AUTH && ColumnData.matchedOnAuth == null) {
				ColumnData.setMatchedOnAuth();
			}
			if(model.getFileType() == FileType.CAPTURED && ColumnData.matchedOnBatched == null) {
				ColumnData.setMatchedOnBatched();
			}
		}
	}
	
	public static void setMatchedOnAuth() {
		ColumnData.matchedOnAuth = new ArrayList<ArrayList<Integer>>();
		int length = ColumnData.posHeadersSize;
		
		for(int i = 0; i < length; i++) {
			ColumnData.matchedOnAuth.add(i, new ArrayList<Integer>());
			ColumnData.matchedOnAuth.get(i).add(i);
		}
	}
	
	public static List<ArrayList<Integer>> getMatchOnAuth(){
		return ColumnData.matchedOnAuth;
	}
	
	public static void updateMatchedOnAuth(int row, int matchedIndex) {
		int currentRowLength = ColumnData.matchedOnAuth.get(row).size();
		
		if(currentRowLength > 1) {
			ColumnData.matchedOnAuth.get(row).set(1, matchedIndex);
		}else {
			ColumnData.matchedOnAuth.get(row).add(1, matchedIndex);
		}
	}
	
	public static void clearMatchedOnAuth() {
		ColumnData.matchedOnAuth = null;
	}
	
	public static void setMatchedOnBatched() {
		ColumnData.matchedOnBatched = new ArrayList<ArrayList<Integer>>();
		int length = ColumnData.posHeadersSize;
		
		for(int i = 0; i < length; i++) {
			ColumnData.matchedOnBatched.add(i, new ArrayList<Integer>());
			ColumnData.matchedOnBatched.get(i).add(i);
		}
	}
	
	public static List<ArrayList<Integer>> getMatchedOnBatched(){
		return ColumnData.matchedOnBatched;
	}
	
	public static void updateMatchedOnBatched(int row, int matchedIndex) {
		int currentRowLength = ColumnData.matchedOnBatched.get(row).size();
		
		if(currentRowLength > 1) {
			ColumnData.matchedOnBatched.get(row).set(1, matchedIndex);
		}else {
			ColumnData.matchedOnBatched.get(row).add(1, matchedIndex);
		}
	}
	
	public static void clearMatchedOnBatched() {
		ColumnData.matchedOnBatched = null;
	}
	
	public static void updateMatchedIndexes(FileType type, int selectedRow, int colIndex){
		switch(type) {
			case UNCAPTURED_AUTH:
				ColumnData.updateMatchedOnAuth(selectedRow, colIndex);
				break;
			case CAPTURED:
				ColumnData.updateMatchedOnBatched(selectedRow, colIndex);
				break;
			default:
				break;
		}
	}
	
	public static void clearMatchedIndexes(FileType type) {
		switch(type) {
			case POS:
				ColumnData.clearMatchedOnAuth();
				ColumnData.clearMatchedOnBatched();
				break;
			case UNCAPTURED_AUTH:
				ColumnData.clearMatchedOnAuth();
				break;
			case CAPTURED:
				ColumnData.clearMatchedOnBatched();
				break;
			default:
				break;
		}
	}
}
