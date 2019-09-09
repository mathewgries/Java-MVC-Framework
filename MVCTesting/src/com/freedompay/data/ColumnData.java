package com.freedompay.data;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import com.freedompay.models.FileModel;
import com.freedompay.util.FileType;

public class ColumnData {

	private static boolean posHeadersSetFlag = false;
	private static String[] matchedTableHeaders = {"POS","Uncaptured", "Captured"};
	private static DefaultTableModel tableData = new DefaultTableModel();
	
	public static void setPOSHeadersSetFlag() {
		ColumnData.posHeadersSetFlag = ColumnData.posHeadersSetFlag ? false : true;
	}
	
	public static boolean getPOSHeadersSetFlag() {
		return ColumnData.posHeadersSetFlag;
	}
	
	public static String[] getMatchedTableHeaders() {
		return ColumnData.matchedTableHeaders;
	}
	
	public static DefaultTableModel getTableData(){
		return ColumnData.tableData;
	}
	
	public static void updateTableData(String value, int selectedRow, int column) {
		ColumnData.tableData.setValueAt(value, selectedRow, column);
	}
	
	public static void setPOSColumnsToTableData(List<String> cols) {
		tableData.setRowCount(0);
		for(String col : cols) {
			tableData.addRow(new String[] {col, null, null});
		}
	}
	
	public static void clearCell(int row, int col) {
		if(ColumnData.tableData.getValueAt(row, col) != null) {
			ColumnData.tableData.setValueAt(null, row, col);
			if(col == 1) {
				ColumnData.matchedOnAuth.get(row).remove(1);
			}
			if(col == 2) {
				ColumnData.matchedOnBatched.get(row).remove(1);
			}	
		}
	}
	
	public static void clearMatchedCells(FileType type) {
		int removeColumn = -1;
		List<ArrayList<Integer>> list = null;
		switch(type) {
			case UNCAPTURED_AUTH:
				removeColumn = 1;
				list = ColumnData.matchedOnAuth;
				break;
			case CAPTURED:
				removeColumn = 2;
				list = ColumnData.matchedOnBatched;
				break;
			default:
				break;
		}
		for(int i = 0; i < list.size(); i++) {
			ColumnData.tableData.setValueAt(null, i, removeColumn);
		}
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
		System.out.println("Setting Matched Indexes");
		List<FileModel> models = FileData.getAllFiles();
		for(FileModel model : models) {
			if(model.getFileType() == FileType.UNCAPTURED_AUTH && ColumnData.matchedOnAuth == null) {
				ColumnData.setMatchedOnAuth();
			}
			if(model.getFileType() == FileType.CAPTURED && ColumnData.matchedOnBatched == null) {
				ColumnData.setMatchedOnBatched();
			}
		}
	}
	
	public static void setMatchedOnAuth() {
		System.out.println("Setting Auth Indexes");
		ColumnData.matchedOnAuth = new ArrayList<ArrayList<Integer>>();
		int length = ColumnData.posHeadersSize;
		for(int i = 0; i < length; i++) {
			ColumnData.matchedOnAuth.add(i, new ArrayList<Integer>());
			ColumnData.matchedOnAuth.get(i).add(i);
		}
	}
	
	public static List<ArrayList<Integer>> getMatchOnAuth(){
		//System.out.println("Called");
		
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
		System.out.println("Clearing Auth Indexes");
		ColumnData.matchedOnAuth = null;
		
	}
	
	public static void setMatchedOnBatched() {
		System.out.println("Setting Batched Indexes");
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
		System.out.println("Clearing Batched Indexes");
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
