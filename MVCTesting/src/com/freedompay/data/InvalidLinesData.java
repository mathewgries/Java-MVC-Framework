package com.freedompay.data;
import java.util.List;
import com.freedompay.models.FileModel;
import com.freedompay.util.FileType;
import java.util.ArrayList;
import java.util.Collections;

public class InvalidLinesData {
	
	private static List<ArrayList<String>> invalidPOSLines = new ArrayList<ArrayList<String>>();
	private static List<ArrayList<String>> invalidAuthLines = new ArrayList<ArrayList<String>>();
	private static List<ArrayList<String>> invalidBatchedLines = new ArrayList<ArrayList<String>>();
	
	private static List<Integer> selectedPOSColumns = new ArrayList<Integer>();
	private static List<Integer> selectedAuthColumns = new ArrayList<Integer>();
	private static List<Integer> selectedBatchedColumns = new ArrayList<Integer>();
	
	
	public static void setInvalidLinesData() {
		List<ArrayList<Integer>> auth = null;
		List<ArrayList<Integer>> batched = null;
		
		if(ColumnData.getMatchOnAuth() != null && ColumnData.getMatchedOnBatched() != null) {
			System.out.println("Called 1");
			//auth = ColumnData.getMatchOnAuth();
			//batched = ColumnData.getMatchedOnBatched();
			//InvalidLinesData.posMatchedToBoth(auth, batched);
		}else if(ColumnData.getMatchOnAuth().size() > 0 && ColumnData.getMatchedOnBatched() == null) {
			System.out.println("Called 1");
			//auth = ColumnData.getMatchOnAuth();
			//InvalidLinesData.posMatchedToOne(auth, FileType.UNCAPTURED_AUTH);
		}else if(ColumnData.getMatchedOnBatched().size() > 0 && ColumnData.getMatchOnAuth() == null) {
			System.out.println("Called 1");
			//batched = ColumnData.getMatchedOnBatched();
			//InvalidLinesData.posMatchedToOne(batched, FileType.CAPTURED);
		}
		Collections.sort(InvalidLinesData.selectedPOSColumns);
		if(InvalidLinesData.selectedAuthColumns.size() > 0) {
			Collections.sort(InvalidLinesData.selectedAuthColumns);
		}
		if(InvalidLinesData.selectedBatchedColumns.size() > 0) {
			Collections.sort(InvalidLinesData.selectedBatchedColumns);
		}
	}
	
	private static void posMatchedToOne(List<ArrayList<Integer>> matchedIndexes, FileType type) {
		for(int i = 0; i < matchedIndexes.size(); i++) {
			if(matchedIndexes.get(i).size() > 1) {
				InvalidLinesData.selectedPOSColumns.add(matchedIndexes.get(i).get(0));
				if(type == FileType.UNCAPTURED_AUTH) {
					InvalidLinesData.selectedAuthColumns.add(matchedIndexes.get(i).get(1));
				}
				if(type == FileType.CAPTURED) {
					InvalidLinesData.selectedBatchedColumns.add(matchedIndexes.get(i).get(1));
				}
			}
		}
		Collections.sort(InvalidLinesData.selectedPOSColumns);
	}
	
	private static void posMatchedToBoth(List<ArrayList<Integer>> matchedIndexesOne, List<ArrayList<Integer>> matchedIndexesTwo) {
		InvalidLinesData.posMatchedToOne(matchedIndexesOne, FileType.UNCAPTURED_AUTH);
		boolean alreadyAdded = false;
		for(int i = 0; i < matchedIndexesTwo.size(); i++) {
			if(matchedIndexesTwo.get(i).size() > 1) {
				for(int j = 0; j < InvalidLinesData.selectedPOSColumns.size(); j++) {
					if(InvalidLinesData.selectedPOSColumns.get(i) == matchedIndexesTwo.get(i).get(0)) {
						alreadyAdded = true;
						break;
					}
				}
				if(!alreadyAdded) {
					InvalidLinesData.selectedPOSColumns.add(matchedIndexesTwo.get(i).get(0));
					InvalidLinesData.selectedBatchedColumns.add(matchedIndexesTwo.get(i).get(1));
					alreadyAdded = false;
				}
			}
		}
		Collections.sort(InvalidLinesData.selectedPOSColumns);
	}
	
	public static List<Integer> getSelectedColumnIndexes(FileType type){
		System.out.println("Getting Selected Column Indexes");
		List<Integer> l = null;
		switch(type) {
			case POS:
				l = InvalidLinesData.selectedPOSColumns;
			case UNCAPTURED_AUTH:
				l = InvalidLinesData.selectedAuthColumns;
			case CAPTURED:
				l = InvalidLinesData.selectedBatchedColumns;
			default:
				break;
		}
		System.out.println("Printing Selected Column Indexes");
		for(Integer i : l) {
			System.out.print(i + ", ");
		}
		return l;
	}
	
	public static void clearPOSselectedPOSColumns() {
		InvalidLinesData.selectedPOSColumns.clear();
	}
	
	public static void clearAuthSelectedColumns() {
		InvalidLinesData.selectedAuthColumns.clear();
	}
	
	public static void clearBatchedSelectedColumns() {
		InvalidLinesData.selectedBatchedColumns.clear();
	}
	
	public static void clearAllSelectedLinesColumns() {
		InvalidLinesData.selectedPOSColumns.clear();
		InvalidLinesData.selectedAuthColumns.clear();
		InvalidLinesData.selectedBatchedColumns.clear();
	}
	
	
	
}