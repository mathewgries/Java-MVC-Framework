package com.freedompay.util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import com.freedompay.data.ColumnData;
import com.freedompay.data.ComponentData;
import com.freedompay.data.FileData;
import com.freedompay.models.FileModel;

/**
 * @author  MGries
 * @version 1.0
 * @since 	1.0
 */
public class Comparison {
	
	private static List<ArrayList<String>> matchedHeadersList = null;
	
	private static boolean uncapturedLoaded = false;
	private static List<ArrayList<Integer>> matchedHeadersUncapturedIndexes = null;
	private static List<ArrayList<String>> uncapturedRows = null;
	private static List<ArrayList<String>> posForUncaptured = null;
	private static List<ArrayList<Integer>> matchedRowsUncapturedIndexes = null;
	private static List<ArrayList<Integer>> duplicateRowsUncapturedIndexes = null;
	private static List<Integer> noUncapturedMatchIndexes = null;
	
	private static boolean capturedLoaded = false;
	private static List<ArrayList<Integer>> matchedHeadersCapturedIndexes = null;
	private static List<ArrayList<String>> capturedRows = null;
	private static List<ArrayList<String>> posForCaptured = null;
	private static List<ArrayList<Integer>> matchedRowsCapturedIndexes = null;
	private static List<ArrayList<Integer>> duplicateRowsCapturedIndexes = null;
	private static List<Integer> noCapturedMatchIndexes = null;
	
	private static List<Integer> noMatches = null;

//======================================================================================
	
								// SET THE LISTS

//======================================================================================
	
	public static void runComparison() {
		List<FileModel> models = FileData.getAllFileModels();
		Comparison.resetLists();
		Comparison.setLoadedFlags(models);
		Comparison.loadMatchedHeadersList();
		Comparison.initMatchedIndexes();
		Comparison.initFileRows();
		Comparison.intiFileCompare();
		Comparison.finalizeNoMatchList();
	}
	
	private static void resetLists() {
		Comparison.uncapturedLoaded = false;
		ComponentData.setMatchedUncapturedBtnIsEnabled(false);
		ComponentData.setDuplicateUncapturedBtnIsEnabled(false);
		Comparison.capturedLoaded = false;
		ComponentData.setMatchedCapturedBtnIsEnabled(false);
		ComponentData.setDuplicateCapturedBtnIsEnabled(false);
		matchedHeadersList = null;
		uncapturedLoaded = false;
		matchedHeadersUncapturedIndexes = null;
		uncapturedRows = null;
		posForUncaptured = null;
		matchedRowsUncapturedIndexes = null;
		duplicateRowsUncapturedIndexes = null;
		noUncapturedMatchIndexes = null;
		capturedLoaded = false;
		matchedHeadersCapturedIndexes = null;
		capturedRows = null;
		posForCaptured = null;
		matchedRowsCapturedIndexes = null;
		duplicateRowsCapturedIndexes = null;
		noCapturedMatchIndexes = null;
		noMatches = null;
	}
	
//======================================================================================
	
								// GET THE LISTS

//======================================================================================
	
	//--------------------------- UNCAPTURED LISTS -------------------------------
	
	public static List<ArrayList<Integer>> getMatchedRowsUncapturedIndexes(){
		return Comparison.matchedRowsUncapturedIndexes;
	}
	
	public static List<ArrayList<Integer>> getDuplicateRowsUncapturedIndexes(){
		return Comparison.duplicateRowsUncapturedIndexes;
	}
	
	//----------------------------- CAPTURED LISTS ---------------------------------
	
	public static List<ArrayList<Integer>> getMatchedRowsCapturedIndexes(){
		return Comparison.matchedRowsCapturedIndexes;
	}
	
	public static List<ArrayList<Integer>> getDuplicateRowsCapturedIndexes(){
		return Comparison.duplicateRowsCapturedIndexes;
	}
	
	//---------------------------- NO MATCHES LIST --------------------------------------
	
	public static List<Integer> getNoMatches(){
		return Comparison.noMatches;
	}
	
//======================================================================================
	
				// SET UP THE LOADED FLAGS AND MATCHED HEADERS LIST

//======================================================================================
	
	/**
	 * <p>
	 * Check if the unauthed and batched files are uploaded 
	 * and set the flags to true if they are
	 * </p>
	 * @param models
	 */
	private static void setLoadedFlags(List<FileModel> models) {
		for(FileModel model : models) {
			if(model.getFileType() == FileType.UNCAPTURED_AUTH && model.getFileContents().getHeadersIndexes().size() >= 3) {
				Comparison.uncapturedLoaded = true;
				ComponentData.setMatchedUncapturedBtnIsEnabled(true);
				ComponentData.setDuplicateUncapturedBtnIsEnabled(true);
			}
			
			if(model.getFileType() == FileType.CAPTURED && model.getFileContents().getHeadersIndexes().size() >= 3) {
				Comparison.capturedLoaded = true;
				ComponentData.setMatchedCapturedBtnIsEnabled(true);
				ComponentData.setDuplicateCapturedBtnIsEnabled(true);
			}
		}
	}
	
	/**
	 * <p>
	 * Get the matched headers list from the MatchedHeadersTable
	 * All rows will have a size of 3. Column index 1 and 2 will be
	 * null if they were not selected to be compared against.
	 * </p>
	 */
	private static void loadMatchedHeadersList() {
		Comparison.matchedHeadersList = new ArrayList<ArrayList<String>>();
		DefaultTableModel tableData = ColumnData.getTableData();
		
		for(int i = 0; i < tableData.getRowCount(); i++) {
			String[] col = new String[tableData.getColumnCount()];
			for(int j = 0; j < tableData.getColumnCount(); j++) {
				col[j] = (String) tableData.getValueAt(i, j);
			}
			Comparison.matchedHeadersList.add(new ArrayList<String>(Arrays.asList(col)));
		}
		// For testing
		//Comparison.printMatchedList();
	}
	
//======================================================================================
	
				// SET UP THE LIST FOR MATCHED HEADER INDEXES

//======================================================================================
	
	/**
	 * Call the setMatchedIndexes method based on file upload booleans
	 */
	private static void initMatchedIndexes() {
		if(Comparison.uncapturedLoaded) {
			Comparison.matchedHeadersUncapturedIndexes = new ArrayList<ArrayList<Integer>>();
			Comparison.setMatchedIndexes(FileType.UNCAPTURED_AUTH, Comparison.matchedHeadersUncapturedIndexes, 1);
		}
		if(Comparison.capturedLoaded) {
			Comparison.matchedHeadersCapturedIndexes = new ArrayList<ArrayList<Integer>>();
			Comparison.setMatchedIndexes(FileType.CAPTURED, Comparison.matchedHeadersCapturedIndexes, 2);
		}
	}
	
	/**
	 * <p>
	 * Set the matched indexes for the uploaded and ready files
	 * </p>
	 * @param type
	 */
	private static void setMatchedIndexes(FileType type, List<ArrayList<Integer>> matchedHeadersIndexes, int col) {
		for(int i = 0; i < Comparison.matchedHeadersList.size(); i++) {
			if(Comparison.matchedHeadersList.get(i).get(col) != null) {				
				Integer[] headerIndexes = new Integer[2];
				headerIndexes[0] = FileData.getFileModel(FileType.POS).getFileContents().getHeaderIndex(Comparison.matchedHeadersList.get(i).get(0));
				headerIndexes[1] = FileData.getFileModel(type).getFileContents().getHeaderIndex(Comparison.matchedHeadersList.get(i).get(col));
				matchedHeadersIndexes.add(new ArrayList<Integer>(Arrays.asList(headerIndexes)));
			}
		}
		// For Testing
		//Comparison.printIndexes(matchedIndexes);
	}
	
//======================================================================================
	
			// SET UP THE UNAUTHED AND BATCHED FILES ROWS FOR SELECTED COLUMNS
	
//======================================================================================
	

	/**
	 * <p>
	 * Prepare the file row lists with selected columns
	 * </p>
	 */
	private static void initFileRows() {
		if(Comparison.uncapturedLoaded) {
			Comparison.posForUncaptured = new ArrayList<ArrayList<String>>();
			Comparison.uncapturedRows = new ArrayList<ArrayList<String>>();
			Comparison.setRowsBySelectedColumns(FileType.POS, Comparison.posForUncaptured, Comparison.matchedHeadersUncapturedIndexes, 0);
			Comparison.setRowsBySelectedColumns(FileType.UNCAPTURED_AUTH, Comparison.uncapturedRows, Comparison.matchedHeadersUncapturedIndexes, 1);
		}
		if(Comparison.capturedLoaded) {
			Comparison.posForCaptured = new ArrayList<ArrayList<String>>();
			Comparison.capturedRows = new ArrayList<ArrayList<String>>();
			Comparison.setRowsBySelectedColumns(FileType.POS, Comparison.posForCaptured, Comparison.matchedHeadersCapturedIndexes, 0);
			Comparison.setRowsBySelectedColumns(FileType.CAPTURED, Comparison.capturedRows, Comparison.matchedHeadersCapturedIndexes, 1);
		}
	}
	
	/**
	 * <p>
	 * Set the file rows by selected columns
	 * </p>
	 * @param type The file type to be iterated
	 * @param fileRows The rows being iterated
	 * @param indexes The indexes to pick out of each row
	 * @param col The column in the indexes is list to check against
	 */
	private static void setRowsBySelectedColumns(FileType type, List<ArrayList<String>> fileRows, List<ArrayList<Integer>> indexes, int col) {
		List<ArrayList<String>> origFileRows = FileData.getFileModel(type).getFileContents().getFileRows();
		
		for(int i = 0; i < origFileRows.size(); i++) {
			String[] pos = new String[indexes.size()];
			for(int j = 0; j < indexes.size(); j++) {
				for(int k = 0; k < origFileRows.get(i).size(); k++) {
					if(k == indexes.get(j).get(col)) {
						pos[j] = origFileRows.get(i).get(k);
					}
				}
			}
			fileRows.add(new ArrayList<String>(Arrays.asList(pos)));
		}
		// For testing
		//Comparison.printFileRows(fileRows);
	}

//======================================================================================
	
				// SET THE INDEXES FOR THE MATCHING LINES

//======================================================================================

	private static void intiFileCompare() {
		if(Comparison.uncapturedLoaded) {
			Comparison.matchedRowsUncapturedIndexes = new ArrayList<ArrayList<Integer>>();
			Comparison.noUncapturedMatchIndexes = new ArrayList<Integer>();
			Comparison.duplicateRowsUncapturedIndexes = new ArrayList<ArrayList<Integer>>();
			Comparison.setMatchedRows(Comparison.posForUncaptured, Comparison.uncapturedRows, Comparison.matchedRowsUncapturedIndexes, Comparison.duplicateRowsUncapturedIndexes, Comparison.noUncapturedMatchIndexes);
		}
		if(Comparison.capturedLoaded) {
			Comparison.matchedRowsCapturedIndexes = new ArrayList<ArrayList<Integer>>();
			Comparison.noCapturedMatchIndexes = new ArrayList<Integer>();
			Comparison.duplicateRowsCapturedIndexes = new ArrayList<ArrayList<Integer>>();
			Comparison.setMatchedRows(Comparison.posForCaptured, Comparison.capturedRows, Comparison.matchedRowsCapturedIndexes, Comparison.duplicateRowsCapturedIndexes, Comparison.noCapturedMatchIndexes);
		}
	}
	
	private static void setMatchedRows(List<ArrayList<String>> posRows, List<ArrayList<String>> toSearchRows, List<ArrayList<Integer>> indexesToSet, List<ArrayList<Integer>> dups, List<Integer> noMatch) {
		int posRowIndex = -1;
		int searchRowIndex = -1;
		List<Integer> holdIndexes = null;
		
		for(ArrayList<String> posRow : posRows) {
			searchRowIndex = -1;
			posRowIndex++;
			holdIndexes = new ArrayList<Integer>();
			holdIndexes.add(posRowIndex);
			
			for(ArrayList<String> toSearchRow: toSearchRows) {
				searchRowIndex++;
				if(posRow.equals(toSearchRow)) {
					holdIndexes.add(searchRowIndex);
				}
			}
			
			if(holdIndexes.size() == 2) {
				indexesToSet.add(new ArrayList<Integer>(holdIndexes));
			}else if(holdIndexes.size() > 2) {
				dups.add(new ArrayList<Integer>(holdIndexes));
			}else {
				noMatch.add(posRowIndex);
			}
		}
		// For testing
		//Comparison.printIndexes(dups);
//		for(Integer i : noMatch) {
//			System.out.println(i);
//		}
	}

	
//======================================================================================
	
						// FINALIZE NOMATCH LIST

//======================================================================================
	
	private static void finalizeNoMatchList() {
		
		if(Comparison.uncapturedLoaded && Comparison.capturedLoaded) {
			Comparison.noMatches = new ArrayList<Integer>();
			
			for(Integer u : Comparison.noUncapturedMatchIndexes) {
				for(Integer c : Comparison.noCapturedMatchIndexes) {
					if(u == c) {
						noMatches.add(u);
						break;
					}
				}
			}
		}
		
		if(Comparison.uncapturedLoaded && !Comparison.capturedLoaded) {
			Comparison.noMatches = Comparison.noUncapturedMatchIndexes;
		}
		
		if(Comparison.capturedLoaded && !Comparison.uncapturedLoaded) {
			Comparison.noMatches = Comparison.noCapturedMatchIndexes;
		}
		
		Comparison.noMatches.remove(0);
	}
	
//======================================================================================
	
				// METHODS FOR PRINTING LISTS : MAINLY FOR TESTING

//======================================================================================
	
//	/**
//	 * <p>
//	 * Print file rows of passed in file row list
//	 * </p>
//	 * @param rows The file rows to be printed
//	 */
//	private static void printFileRows(List<ArrayList<String>> rows) {
//		for(int i = 0; i < rows.size(); i++) {
//			for(int j = 0; j < rows.get(i).size(); j++) {
//				System.out.print(rows.get(i).get(j) + ", ");
//			}
//			System.out.println();
//		}
//	}
//	
//	/**
//	 * <p>
//	 * Print the matched index list
//	 * </p>
//	 * @param rows
//	 */
//	private static void printIndexes(List<ArrayList<Integer>> rows) {
//		for(int i = 0; i < rows.size(); i++) {
//			for(int j = 0; j < rows.get(i).size(); j++) {
//				System.out.print(rows.get(i).get(j) + ", ");
//			}
//			System.out.println();
//		}
//	}
//	
//	/**
//	 * <p>
//	 * Print the matchedHeadersList
//	 * </p>
//	 */
//	private static void printMatchedList() {
//		for(ArrayList<String> rows : Comparison.matchedHeadersList) {
//			for(String col : rows) {
//				System.out.print(col + ", ");
//			}
//			System.out.println();
//		}
//	}
}