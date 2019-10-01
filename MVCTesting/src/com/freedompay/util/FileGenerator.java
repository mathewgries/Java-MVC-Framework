package com.freedompay.util;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.freedompay.data.FileData;

public class FileGenerator {
	private static FileWriter fw;
	private static BufferedWriter bw;
	private static File file;
	private static String parentDirectory = "C:\\FPReconFiles";
	private static String matchDirectory = "C:\\FPReconFiles\\MatchedRows";
	private static String duplicateDirectory = "C:\\FPReconFiles\\DuplicateRows";
	private static String noMatchDirectory = "C:\\FPReconFiles\\NotMatched";

	
//===========================================================================

					// DATESTAMP THE FILENAMES

//===========================================================================
	/**
	 * <p>Generate a date for the file name</p>
	 * @return
	 */
	private static String getDateForFileName() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.ENGLISH);
		String date = sdf.format(new Date());
		return date;
	}
	
//===========================================================================

						// EXPORT ROW MATCH FILES

//===========================================================================
	
	/**
	 * <p>Export the Matched Rows file to disk</p>
	 * @param rowIndexes The matched indexes of the POS file, and the given file type - Uncaptured or Captured
	 * @param type The type of file matched against the POS file - Uncaptured or Captured 
	 * @param filename The name for the file being created
	 */
	public static void exportMatchFile(List<ArrayList<Integer>> rowIndexes, FileType type, String filename){
		
		List<ArrayList<String>> posRows = FileData.getFileModel(FileType.POS).getFileContents().getFileRows();
		List<String> requestIds = FileData.getFileModel(type).getFileContents().getRequestIds();
		String[] headers = FileGenerator.setHeadersForMatchFileExport();
		List<ArrayList<String>> forExport = new ArrayList<ArrayList<String>>();
		int colCount = 0;
		
		forExport.add(new ArrayList<String>(Arrays.asList(headers)));
		for(ArrayList<Integer> row : rowIndexes) {
			String[] rowData = new String[headers.length];
			colCount = 0;
			rowData[0] = Integer.toString(row.get(0) + 1);
			rowData[1] = Integer.toString(row.get(1) + 1);
			rowData[2] = requestIds.get(row.get(1));
			
			for(String posRow : posRows.get(row.get(0))) {
				rowData[colCount + 3] = posRow;
				colCount++;
			}
			forExport.add(new ArrayList<String>(Arrays.asList(rowData)));
		}
		FileGenerator.createFile(forExport, filename, FileGenerator.matchDirectory);
	}
	
	/**
	 * <p>Set up the headers for the matched rows file</p>
	 * @return The headers for the file
	 */
	private static String[] setHeadersForMatchFileExport(){
		List<String> posHeaders = FileData.getFileModel(FileType.POS).getFileContents().getHeaderNames();
		String[] headers = new String[posHeaders.size() + 3];
		int count = 0;
		
		headers[0] = "POS Row";
		headers[1] = "Matched Row";
		headers[2] = "RequestId";	
		for(String header : posHeaders) {
			headers[count + 3] = header;
			count++;
		}
		return headers;
	}
	
//===========================================================================

					// EXPORT DUPLICATE ROW FILE

//===========================================================================

	/**
	 * <p>Export the duplicate matched rows file </p>
	 * @param rowIndexes The indexes of the POS file that matched more than once on the given file - Uncaptured or Captured
	 * @param type The file matched on the POS file - Uncaptured or Captured
	 * @param filename The name of the file being created
	 */
	public static void exportDuplicateRowsFiles(List<ArrayList<Integer>> rowIndexes, FileType type, String filename) {
		List<ArrayList<String>> posRows = FileData.getFileModel(FileType.POS).getFileContents().getFileRows();
		List<ArrayList<String>> forExport = new ArrayList<ArrayList<String>>();
		String[] headers = FileGenerator.setHeadersForDupFileExport();
		int colCount = 0;
		
		forExport.add(new ArrayList<String>(Arrays.asList(headers)));
		
		for(ArrayList<Integer> row : rowIndexes) {
			String[] rowData = new String[headers.length];
			colCount = 0;
			rowData[0] = Integer.toString(row.get(0) + 1);
			rowData[1] = FileGenerator.concatDupRowNumbers(row);
			
			for(String posRow : posRows.get(row.get(0))) {
				rowData[colCount + 2] = posRow;
				colCount++;
			}
			forExport.add(new ArrayList<String>(Arrays.asList(rowData)));
		}
		FileGenerator.createFile(forExport, filename, FileGenerator.duplicateDirectory);
	}
	
	/**
	 * <p>Concatenate the indexes from the Captured or Uncaptured file</p>
	 * @param row The index of the list being pulled against
	 * @return The concatenated indexes to String
	 */
	private static String concatDupRowNumbers(ArrayList<Integer> row) {
		String result = "";
		
		for(int i = 1; i < row.size(); i++) {
			if(i < row.size() - 1) {
				result += (row.get(i) + 1) + ", ";
			}else {
				result += (row.get(i) + 1);
			}
		}
		return result;
	}
	
	/**
	 * <p>Set the headers for the duplicate match file</p>
	 * @return The headers for the file
	 */
	private static String[] setHeadersForDupFileExport() {
		List<String> posHeaders = FileData.getFileModel(FileType.POS).getFileContents().getHeaderNames();
		String[] headers = new String[posHeaders.size() + 2];
		int count = 0;
		
		headers[0] = "POS Row";
		headers[1] = "Matched Rows";
		for(String header : posHeaders) {
			headers[count + 2] = header;
			count++;
		}
		return headers;
	}
	
//===========================================================================

						// EXPORT NO MATCH FILE

//==========================================================================

	/**
	 * <p>Export the no matched rows file</p>
	 * @param rowIndexes The indexes of the POS file that weren't matched
	 * @param filename The name of the file being exported 
	 */
	public static void exportNoMatchFile(List<Integer> rowIndexes, String filename) {
		List<ArrayList<String>> posRows = FileData.getFileModel(FileType.POS).getFileContents().getFileRows();
		List<ArrayList<String>> forExport = new ArrayList<ArrayList<String>>();
		String[] headers = FileGenerator.setHeadersForNoMatchesExport();
		int colCount = 0;
		
		forExport.add(new ArrayList<String>(Arrays.asList(headers)));
		
		for(Integer row : rowIndexes) {
			String[] rowData = new String[headers.length];
			colCount = 0;
			rowData[colCount++] = Integer.toString(row + 1);
			
			for(String col : posRows.get(row)) {
				rowData[colCount++] = col;
			}
			forExport.add(new ArrayList<String>(Arrays.asList(rowData)));
		}
		FileGenerator.createFile(forExport, filename, FileGenerator.noMatchDirectory);
	}
	
	/**
	 * <p>Headers for rows with no match table data</p>
	 * @return
	 */
	private static String[] setHeadersForNoMatchesExport() {
		List<String> posHeaders = FileData.getFileModel(FileType.POS).getFileContents().getHeaderNames();
		String[] headers = new String[posHeaders.size() + 1];
		int count = 0;
		
		headers[0] = "Row";
		for(String header : posHeaders) {
			headers[count + 1] = header;
			count++;
		}
		return headers;
	}
	
//===========================================================================

					// EXPORT FILE AND OPEN DIRECTOY

//===========================================================================
	
	/**
	 * <p>The final file export step. Create the file and save to a directory<p>
	 * @param fileRows The list to create the file with
	 * @param fileName The name of the file being created
	 * @param directory The directory on disk to save the file
	 */
	private static void createFile(List<ArrayList<String>> fileRows, String fileName, String directory) {
		try {
			String filePath = directory + "\\" + fileName + "_" + getDateForFileName() + ".csv";
			file = new File(filePath);
			file.getParentFile().mkdirs();
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			
			for(ArrayList<String> row : fileRows) {
				for(String col : row) {
					bw.write(col + ", ");
				}
				bw.newLine();
			}
			bw.close();
			
		}catch(IOException ex) {
			System.out.println(ex);
		}
	}
	
	/**
	 * <p>Once the files are saved, open the directory where they were stored on disk</p>
	 */
	public static void openResultsDirectory() {
		try {
			File file = new File(FileGenerator.parentDirectory);
			Desktop desktop = Desktop.getDesktop();
			desktop.open(file);
		}catch(IOException ex) {
			ex.printStackTrace();
		}
	}
}
