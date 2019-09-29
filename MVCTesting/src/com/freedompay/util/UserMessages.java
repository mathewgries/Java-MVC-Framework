package com.freedompay.util;

import javax.swing.JOptionPane;

/**
 * <p>
 * This file holds all warning, error, and general messages to show to the user.
 * This helps keep clutter out of the other class files by putting the bulk of the
 * message data here, and one line calling the proper message where needed.
 * </p>
 * @author MGries
 *
 */
public class UserMessages {

	/**
	 * <p>Error response when trying to upload a file that is already loaded</p>
	 */
	public static void fileAlreadyLoaded() {
		JOptionPane.showMessageDialog(
				null, 
				"That file is already loaded. Please select a different file",
				"User Error!",
				JOptionPane.ERROR_MESSAGE
				);
	}
	
	/**
	 * <p>Error response when file extension is incorrect</p>
	 */
	public static void invlalidFileType() {
		JOptionPane.showMessageDialog(
				null, 
				"Please select a a file with a .csv extension",
				"Invalid File Type!",
				JOptionPane.ERROR_MESSAGE
				);
	}
	
	/**
	 * <p>
	 * Error response when chosen file does not have requestId
	 * POS file does not require requestId column
	 * </p>
	 */
	public static void noRequestId() {
		JOptionPane.showMessageDialog(
				null,
				"Match files must contain a RequestId column",
				"Missing RequestId!",
				JOptionPane.ERROR_MESSAGE
				);
	}
	
	/**
	 * <p>Error message when trying to run comparison when not enough files are loaded</p>
	 */
	public static void fileCountTooLow() {
		JOptionPane.showMessageDialog(
				null,
				"You must upload at least two files",
				"File Count Error",
				JOptionPane.ERROR_MESSAGE
				);
	}
	
	/**
	 * <p>Error message when trying to run comparison when no POS file is loaded</p>
	 */
	public static void noPOSFilePresent() {
		JOptionPane.showMessageDialog(
				null,
				"You must select a POS file. This file is a report from the POS system",
				"Missing POS File!",
				JOptionPane.ERROR_MESSAGE
				);
	}
	
	/**
	 * <p>Error message when trying to run comparison when not enough header matches are selected</p>
	 */
	public static void invalidColumnSelectionCount() {
		JOptionPane.showMessageDialog(
				null,
				"You must select at least 3 columns to match on",
				"Invalid Column Count!",
				JOptionPane.ERROR_MESSAGE
				);
	}
	
}
