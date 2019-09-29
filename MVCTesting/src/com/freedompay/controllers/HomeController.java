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
	private String instructions = "WELCOME TO THE FREEDOMPAY RECONCILIATION APP\n\n"+
			"App instructions are lsited below. To get started, select the \"Files\" tab in the menu\n"+
			"You can return to the home screen at anytime to review the instructions by clicking\n"+
			"the Home tab. Your data will be saved when you return to the Files tab.\n\n\n"+ 
			"Instructions:\n\n"+
			"1. Click the Add button to open a file dialog\n\n"+
			"2. Choose a file to add it to the list\n\n"+
			"3. Choose no less than two, and no more than three files\n\n"+
			"4. One file must be from the POS system. This file will be matched"+
			" against the other uploaded files";
	
	
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