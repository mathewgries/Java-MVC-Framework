package com.freedompay.util;
import com.freedompay.data.FileData;
import com.freedompay.data.InvalidLinesData;
import com.freedompay.models.FileModel;
import com.freedompay.util.ErrorType;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * Static validation class for validating files and field values
 * </p>
 * @author MGries
 *
 */
public class Validation {
	/**
	 * <p>
	 * Main validation method that calls the other validation methods
	 * </p>
	 * @param model the file model to validate
	 */
	public static void runValidation(FileModel model) {
		List<ArrayList<Integer>> 	invalidRows 	= model.getFileContents().initInvalidRowIntegers();
		List<ArrayList<String>> 	fileRows 		= model.getFileContents().getFileRows();
		List<Integer> 				headerByIndexes = model.getFileContents().getHeaderIndexes();
		List<ArrayList<String>> 	selectedColumns = Validation.getSelectedColumnsFromRows(fileRows, headerByIndexes);
		
		Validation.validateNullAndEmptyString(selectedColumns, invalidRows);
		Validation.validateLast4Value(selectedColumns, invalidRows);
		Validation.validateDollarAmountIsNumeric(selectedColumns, invalidRows);
	}
	
	/**
	 * <p>
	 * Break out the columns from the rows that, for only the selected
	 * columns to match against.
	 * </p>
	 * @param rows the complete file rows
	 * @param col the indexes of the columns to pick out
	 * @return rows with only selected colums
	 */
	private static List<ArrayList<String>> getSelectedColumnsFromRows(List<ArrayList<String>> rows, List<Integer> col) {
		List<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		for(int i = 0; i < rows.size(); i++) {
			String[] selected = new String[col.size()];
			for(int j = 0; j < col.size(); j++) {
				selected[j] = rows.get(i).get(col.get(j));
			}
			list.add(new ArrayList<String>(Arrays.asList(selected)));
		}
		return list;
	}
	
	/**
	 * <p>Validate the uploaded file before saving the model</p>
	 * @param model
	 * @param type
	 * @return
	 */
	public static boolean isModelValid(File model, FileType type) {
		if(!Validation.validateCSVFile(model.getName())) {
			return false;
		}
		if(!Validation.validateFileNotUploaded(model)) {
			return false;
		}
		return true;
	}
		
	/**
	 * <p>
	 * Validates the file type being uploaded. Must be .CSV
	 * </p>
	 * @param filename String the name of the file being evaluated
	 * @param parentComponent JPanel the component where the action is created
	 * @return boolean Return true if file extension is csv
	 */
	public static boolean validateCSVFile(String filename) {
		String extension = "";
		int i = filename.lastIndexOf(".");
		if(i > 0) { extension = filename.substring(i+1); }
		if(extension.equals("csv")) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * <p>Validates if the file being uploaded has already been uploaded</p>
	 * @param file File The file selected by the user
	 * @param parentComponent JPanel The JPanel the file was uploaded to
	 * @return boolean True if the file is already uploaded
	 */
	public static boolean validateFileNotUploaded(File model) {
		try {
			for(int i = 0; i < FileData.getFileCount(); i++) {
				if(FileData.getAllFileModels().get(i).getFile().equals(model)) {
					return false;
				}
			}
		}catch(ArrayIndexOutOfBoundsException ex) {
			
		}
		return true;
	}
		
//		/**
//		 * <p>
//		 * Check the file for a requestId column. If it exists, return the
//		 * position in the header list. If not, return -1
//		 * </p>
//		 * @param headers String[] Array to search for requestId value
//		 * @param parentComponent	EntryPanel Japanle to attach error message
//		 * @return	-1 if not found, or index of position if found
//		 */
//		public static int validateRequestIdExists(String[] headers) {
//			int pos = -1;
//			
//			try {
//			for(int i = 0; i < headers.length; i++) {
//				if(headers[i].toUpperCase().equals("REQUESTID")) {
//					pos = i;
//					break;
//				}
//			}
//			}catch(ArrayIndexOutOfBoundsException ex) {
//				
//			}
//			return pos;
//		}
	
	/**
	 * <p>
	 * Validates file content lines for NULL or EMPTRY STRING values
	 * </p>
	 * @param fileName String name of the file being evaluated
	 * @param compareList ArrayList String[] contents of the file
	 * @param linesWithErrors ArrayList<String[]> List to update with invalid lines and error message
	 * @param parentComponent EntryPanel JPanel for centering the error dialog
	 */
	public static void validateNullAndEmptyString(
			List<ArrayList<String>> fileRows, List<ArrayList<Integer>> invalidRows) {
		int errorType = ErrorType.NULL_OR_EMPTY_VALUE.getValue();
		try {
			
			for(int i = 1; i < fileRows.size(); i++) {
				for(int j = 0; j < fileRows.get(i).size(); j++) {
					String value = fileRows.get(i).get(j);
					if(value.equalsIgnoreCase("NULL") || value.equalsIgnoreCase("")) {
						invalidRows.get(i).add(errorType);
						break;
					}
				}
			}
			
		}catch(ArrayIndexOutOfBoundsException ex) {
			System.out.println("Index Out Of Bounds: validateNullAndEmptyString");
		}
	}
	
	/**
	 * <p>Validates the Last4 columns length</p>
	 * @param fileName String name of file
	 * @param compareList ArrayList<String[]> list containing values to check
	 * @param linesWithErrors ArrayList<String[]> List to update with invalid lines and error message
	 */
	public static void validateLast4Value(
			List<ArrayList<String>> fileRows, List<ArrayList<Integer>> invalidRows) {
		int errorType = ErrorType.INVALID_PAN.getValue();
		int panPos = Validation.getPanPos(fileRows.get(0));
		if(panPos >= 0) {
			try {
				for(int i = 1; i < fileRows.size(); i++) {
					for(int j = 0; j < fileRows.get(i).size(); j++) {
						if(j == panPos && fileRows.get(i).get(j).length() != 4){
							invalidRows.get(i).add(errorType);
							break;
						}
					}
				}
			}catch(ArrayIndexOutOfBoundsException ex) {
				System.out.println("Index Out Of Bounds: validateNullAndEmptyString");
			}
		}
	}
		
		// Get last 4 header index
	private static int getPanPos(List<String> headers) {
		int panPos = -1;
		for(int i = 0; i < headers.size(); i++) {
			String header = headers.get(i);
			if(
					header.equalsIgnoreCase("LAST4") 			|| 
					header.equalsIgnoreCase("LAST 4")			||
					header.equalsIgnoreCase("cardnumber") 		||
					header.equalsIgnoreCase("card number") 		||
					header.equalsIgnoreCase("account") 			||
					header.equalsIgnoreCase("accountnumber") 	||
					header.equalsIgnoreCase("account number") 	||
					header.equalsIgnoreCase("acct") 			||
					header.equalsIgnoreCase("acctNmbr") 		||
					header.equalsIgnoreCase("acctNbr") 			||
					header.equalsIgnoreCase("acctNumber")
			) 
			{
				panPos = i;
				break;
			}
		}
		return panPos;
	}
		
	/**
	 * <p>Validate money fields are valid numerics</p>
	 * @param fileName String name of the file being evaluated
	 * @param headers ArrayList String header list for the file
	 * @param compareList ArrayList String[] contents of the file
	 * @param linesWithErrors ArrayList String[] List to add the invalid lines to
	 */
	public static void validateDollarAmountIsNumeric(List<ArrayList<String>> fileRows, List<ArrayList<Integer>> invalidRows) {
		int errorType = ErrorType.NON_NUMERIC.getValue();
		ArrayList<Integer> pos = getDollarColumnPostion(fileRows.get(0));
		if(pos.size() != 0) {
			Validation.removeDollarSign(pos, fileRows);
			try {
				for(int i = 1; i < fileRows.size(); i++) {
					for(int j = 0; j < fileRows.get(i).size(); j++) {
						for(int k = 0; k < pos.size(); k++) {
							if(j == pos.get(k)){
								if(!isNumeric(fileRows.get(i).get(j))) {
									invalidRows.get(i).add(errorType);
									break;
								}
							}
						}
					}
				}
			}catch(ArrayIndexOutOfBoundsException ex) {
				System.out.println("Index Out Of Bounds: validateNullAndEmptyString");
			}
		}
	}
	
	/**
	 * <p>Identifies which dollar columns are being used. Then locates the position for validating format method.</p>
	 * @param headers ArrayList String The selected headers array
	 * @return ArrayList Integer position of the dollar amount columns
	 */
	private static ArrayList<Integer> getDollarColumnPostion(List<String> headers) {
		ArrayList<Integer> pos = new ArrayList<Integer>();
		try {
			for(int i = 0; i < headers.size(); i++) {
				String header = headers.get(i);
				if(
						header.equalsIgnoreCase("TOTALAMOUNT") 			|| 
						header.equalsIgnoreCase("APPROVEDAMOUNT") 		||
						header.equalsIgnoreCase("APPROVED AMOUNT") 		||
						header.equalsIgnoreCase("TOTAL AMOUNT") 		||
						header.equalsIgnoreCase("TOTAL") 				||
						header.equalsIgnoreCase("APPROVED") 			||
						header.equalsIgnoreCase("authorizedAmount") 	||
						header.equalsIgnoreCase("authorized Amount") 	||
						header.equalsIgnoreCase("authoAmount") 			||
						header.equalsIgnoreCase("authAmount")
				) 
				{
					pos.add(i);
				}
			}
		}catch(ArrayIndexOutOfBoundsException ex) {
			System.out.println("Index Out Of Bounds: validateNullAndEmptyString");
		}
		return pos;
	}
	
	/**
	 * <p>Remove dollar sign from dollar amount field if applied</p>
	 * @param pos ArrayList Integer The position of the dollar amount columns
	 * @param compareList ArrayList String[] The list containing the file data
	 */
	private static void removeDollarSign(ArrayList<Integer> pos, List<ArrayList<String>> lines) {
		try {
			for(int i = 1; i < lines.size(); i++) {
				for(int j = 0; j < lines.get(i).size(); j++) {
					for(int k = 0; k < pos.size(); k++) {
						if(j == pos.get(k)){
							if(lines.get(i).get(j).charAt(0) == '$'){
								lines.get(i).set(j, lines.get(i).get(j).substring(1));
							}
						}
					}
				}
			}
		}catch(ArrayIndexOutOfBoundsException ex) {
			System.out.println("Index Out Of Bounds: validateNullAndEmptyString");
		}
	}
	
	/**
	 * <p>Validates if dollar amount field is a numeric value</p>
	 * @param str String the field value to validate
	 * @return boolean True if field value is numeric
	 */
	private static boolean isNumeric(String str) {
		try {  
		    Double.parseDouble(str);  
		    return true;
		} catch(NumberFormatException e){  
		    return false;  
		}  
	}

//		
//		/**
//		 * <p>
//		 * This function is not currently in use.
//		 * This will check a file for duplicate records within itself
//		 * </p>
//		 * @param fileName		 	Name of file we are checking again
//		 * @param compareList		The file contents to check against
//		 * @param linesWithErrors	The error list to add the invalid lines too
//		 */
//		public static void validateSameFileForDuplicates(String fileName, ArrayList<String[]> compareList, ArrayList<String[]> linesWithErrors, EntryPanel parentComponent) {
//			String message = "Error: File row is a duplicate of row: ";
//			boolean match = true;
//			
//			try {
//				for(int i = 0; i < compareList.size(); i++) {
//					for(int j = (i + 1); j < compareList.size(); j++) {
//						for(int k = 0; k < compareList.get(i).length; k++) {
//							if(!compareList.get(i)[k].equals(compareList.get(j)[k])){
//								match = false;
//								break;
//							}
//						}
//						if(match){
//							linesWithErrors.add(new String[]{fileName, "Line: " + (j + 1), message + "" + Integer.toString(i + 1)});
//						}
//						match = true;
//					}
//				}
//			}catch(ArrayIndexOutOfBoundsException ex) {
//				logger.logError("CalledFrom: FileValidation.validateSameFileForDuplicates()\n", ex);
//				GenericMessages.userErrorMessage(ex.getMessage(), parentComponent);
//			}
//		}
}
