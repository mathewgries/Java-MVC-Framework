package com.freedompay.util;
import com.freedompay.data.FileData;
import com.freedompay.data.InvalidLinesData;
import com.freedompay.models.FileModel;
import com.freedompay.util.ErrorType;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Validation {
	
	public static void runValidation(FileModel model) {
		List<ArrayList<String>> lines = Validation.loadColumnsForValidation(model);
		List<String> headers = Validation.loadHeadersForValidation(model);
		Validation.validateNullAndEmptyString(model, lines);
		Validation.validateLast4Value(model, lines, headers);
		Validation.validateDollarAmountIsNumeric(model, lines, headers);
	}
	
	private static List<String> loadHeadersForValidation(FileModel model) {
		List<String> results = new ArrayList<String>();
		List<Integer> colPos = null;
		List<String> headers = model.getFileContents().getHeaderNames();
		
		switch(model.getFileType()) {
		case POS:
			colPos = InvalidLinesData.getSelectedColumnIndexes(model.getFileType());
			results = Validation.getFileSelectedHeaders(model, colPos, headers);
			break;
		case UNCAPTURED_AUTH:
			colPos = InvalidLinesData.getSelectedColumnIndexes(model.getFileType());
			results = Validation.getFileSelectedHeaders(model, colPos, headers);
			break;
		case CAPTURED:
			colPos = InvalidLinesData.getSelectedColumnIndexes(model.getFileType());
			results = Validation.getFileSelectedHeaders(model, colPos, headers);
			break;
		}
		return results;
	}
	
	private static List<String> getFileSelectedHeaders(FileModel model, List<Integer> colPos, List<String> headers){
		List<String> results = new ArrayList<String>();
		for(int i = 0; i < headers.size(); i++) {
			for(int j = 0; j < colPos.size(); j++) {
				if(i == colPos.get(j)) {
					results.add(headers.get(i));
				}
			}
		}
		return results;
	}
	//TODO: This is where we left off. We should now have the file lines containing only the selected columns
	// Now we need to run them through the validation above.
	
	private static List<ArrayList<String>> loadColumnsForValidation(FileModel model) {
		System.out.println("Loading Columns For Validation");
		List<ArrayList<String>> lines = new ArrayList<ArrayList<String>>();
		List<ArrayList<String>> filelines = model.getFileContents().getFileRows();
		List<Integer> indexes = null;
		
		switch(model.getFileType()) {
			case POS:
				indexes = InvalidLinesData.getSelectedColumnIndexes(model.getFileType());
				break;
			case UNCAPTURED_AUTH:
				indexes = InvalidLinesData.getSelectedColumnIndexes(model.getFileType());
				break;
			case CAPTURED:
				indexes = InvalidLinesData.getSelectedColumnIndexes(model.getFileType());
				break;
		}
		for(int i = 1; i < filelines.size(); i++) {
			lines.add((i - 1),new ArrayList<String>());
			for(int j = 0; j < filelines.get(i).size();j++) {
				for(int k = 0; k < indexes.size(); k++) {
					if((int)indexes.get(k) == j) {
						lines.get(i - 1).add(filelines.get(i).get(j));
					}
				}
			}
		}
		return lines;
	}
	
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
//		
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
//		
		/**
		 * <p>
		 * Validates file content lines for NULL or EMPTRY STRING values
		 * </p>
		 * @param fileName String name of the file being evaluated
		 * @param compareList ArrayList String[] contents of the file
		 * @param linesWithErrors ArrayList<String[]> List to update with invalid lines and error message
		 * @param parentComponent EntryPanel JPanel for centering the error dialog
		 */
		public static void validateNullAndEmptyString(FileModel model, List<ArrayList<String>> lines) {
			int errorType = ErrorType.NULL_OR_EMPTY_VALUE.getValue();
			// Search file contents for null. Return true if found, false if not
			try {
				
				for(int i = 1; i < lines.size(); i++) {
					for(int j = 0; j < lines.get(i).size(); j++) {
						String value = lines.get(i).get(j);					
						if(value.equalsIgnoreCase("NULL") || value.equalsIgnoreCase("")) {
							System.out.println("Updating Invalid List");
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
	public static void validateLast4Value(FileModel model, List<ArrayList<String>> lines, List<String> headers) {
//		int errorType = ErrorType.INVALID_PAN.getValue();
//		int panPos = Validation.getPanPos(headers);
//		if(panPos >= 0) {
//			try {
//				for(int i = 1; i < lines.size(); i++) {
//					for(int j = 0; j < lines.get(i).size(); j++) {
//						if(j == panPos && lines.get(i).get(j).length() != 4){
//							model.updateInvalidList().get(i).add(errorType);
//							break;
//						}
//					}
//				}
//			}catch(ArrayIndexOutOfBoundsException ex) {
//				System.out.println("Index Out Of Bounds: validateNullAndEmptyString");
//			}
//		}
	}
		
		// Get last 4 header index
	private static int getPanPos(List<String> headers) {
		int panPos = -1;
		for(int i = 0; i < headers.size(); i++) {
			String header = headers.get(i);
			if(
					header.equalsIgnoreCase("LAST4") || 
					header.equalsIgnoreCase("LAST 4")||
					header.equalsIgnoreCase("cardnumber") ||
					header.equalsIgnoreCase("card number") ||
					header.equalsIgnoreCase("account") ||
					header.equalsIgnoreCase("accountnumber") ||
					header.equalsIgnoreCase("account number") ||
					header.equalsIgnoreCase("acct") ||
					header.equalsIgnoreCase("acctNmbr") ||
					header.equalsIgnoreCase("acctNbr") ||
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
	public static void validateDollarAmountIsNumeric(FileModel model, List<ArrayList<String>> lines, List<String> headers) {
//		int errorType = ErrorType.NON_NUMERIC.getValue();
//		ArrayList<Integer> pos = getDollarColumnPostion(headers);
//		if(pos.size() != 0) {
//			Validation.removeDollarSign(pos, lines);
//			try {
//				for(int i = 1; i < lines.size(); i++) {
//					for(int j = 0; j < lines.get(i).size(); j++) {
//						for(int k = 0; k < pos.size(); k++) {
//							if(j == pos.get(k)){
//								if(!isNumeric( lines.get(i).get(j))) {
//									model.getInvalidRowIndexesWithEnumInts().get(i).add(errorType);
//									break;
//								}
//							}
//						}
//					}
//				}
//			}catch(ArrayIndexOutOfBoundsException ex) {
//				System.out.println("Index Out Of Bounds: validateNullAndEmptyString");
//			}
//		}
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
						header.equalsIgnoreCase("TOTALAMOUNT") || 
						header.equalsIgnoreCase("APPROVEDAMOUNT") ||
						header.equalsIgnoreCase("APPROVED AMOUNT") ||
						header.equalsIgnoreCase("TOTAL AMOUNT") ||
						header.equalsIgnoreCase("TOTAL") ||
						header.equalsIgnoreCase("APPROVED") ||
						header.equalsIgnoreCase("authorizedAmount") ||
						header.equalsIgnoreCase("authorized Amount") ||
						header.equalsIgnoreCase("authoAmount") ||
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
