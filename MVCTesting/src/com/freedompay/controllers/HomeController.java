package com.freedompay.controllers;
import javax.swing.JLabel;
import javax.swing.JTextArea;

/**
 * <p>
 * Controller for the HomeView class
 * This will set the instructions for the application,
 * and the welcome message.
 * </p>
 * @author MGries
 *
 */
public class HomeController extends Controller {
	
	//--------------- PAGE INSTRUCTIONS ---------------------
	
	/**
	 * <p>Instructions for using the application</p>
	 */
	private String instructions = 
			"WELCOME TO THE FREEDOMPAY RECONCILIATION APP\n\n"+
			"App instructions are listed below. To get started, select the \"Files\" tab in the menu\n"+
			"You can return to the home screen at anytime to review the instructions by clicking\n"+
			"the Home tab. Your data will be saved when switching views.\n\n\n"+ 
			"Instructions:\n\n"+
			"1.  A file must be uploaded from the POS system. Load this file using the \"POS File\" button\n" +
			"2.  The \"Uncaptured File\" button is to a load a FreedomPay file for authorization transactions that have not been "+
			"settled in the FreedomPay system.\n"+
			"3.  The \"Captured File\" button is to load a FreedomPay file for transactions that have been settled in the FreedomPay system\n"+
			"4.  You can load one of, or both of these files to run against the uploaded POS file\n"+
			"5.  In order to compare the POS file to either file, you must select at least 3 columns to match against for each file\n"+
			"6.  To match columns, select the row from the table on the \"Matched Headers\" table, then select the headers from the Uncaptured and Captured Headers list on the right\n"+
			"7.  The Uncaptured and Captured files must contain a requestId column named as such\n"+
			"8.  To view the file comparison results, select the \"Matched Rows\" button\n"+
			"9.  When viewing the matches, you will be presented with the option to view the POS row, with row number"+
			", and the row number as well as requestId from the selected file\n"+
			"10. You will also be presented with a view for POS rows that were matched more than once. These will be considered invalid match ups\n"+
			"11. Lastly, you can also view the rows from the POS file that did not match on either file.";
	
	
	/**
	 * <p>Loads the instructions to the view</p>
	 */
	public String getInstructions() {
		return this.instructions;
	}
	
	/**
	 * <p>The container where the isntructions will appear</p>
	 */
	public JTextArea getTextArea(String txt) {
		JTextArea t = new JTextArea(txt);
		t.setLineWrap(true);
		t.setWrapStyleWord(true);
		return t;
	}

	/**
	 * <p>Basic page label</p>
	 */
	public JLabel getJLabel(String txt) {
		return new JLabel(txt);
	}
}