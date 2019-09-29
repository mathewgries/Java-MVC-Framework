package com.freedompay.util;

import javax.swing.JOptionPane;

public class UserMessages {

	public static void fileAlreadyLoaded() {
		JOptionPane.showMessageDialog(
				null, 
				"That file is already loaded. Please select a different file",
				"User Error!",
				JOptionPane.WARNING_MESSAGE
				);
	}
	
	public static void invlalidFileType() {
		JOptionPane.showMessageDialog(
				null, 
				"Please select a a file with a .csv extension",
				"Invalid File Type!",
				JOptionPane.ERROR_MESSAGE
				);
	}
	
	public static void noRequestId() {
		JOptionPane.showMessageDialog(
				null,
				"Match files must contain a RequestId column",
				"Missing RequestId!",
				JOptionPane.WARNING_MESSAGE
				);
	}
	
	public static void fileCountTooLow() {
		JOptionPane.showMessageDialog(
				null,
				"You must upload at least two files",
				"File Count Error",
				JOptionPane.ERROR_MESSAGE
				);
	}
	
	public static void noPOSFilePresent() {
		JOptionPane.showMessageDialog(
				null,
				"You must select a POS file. This file is a report from the POS system",
				"Missing POS File!",
				JOptionPane.ERROR_MESSAGE
				);
	}
	
	public static void invalidColumnSelectionCount() {
		JOptionPane.showMessageDialog(
				null,
				"You must select at least 3 columns to match on",
				"Invalid Column Count!",
				JOptionPane.ERROR_MESSAGE
				);
	}
	
}
