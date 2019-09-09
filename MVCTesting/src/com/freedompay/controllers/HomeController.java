package com.freedompay.controllers;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.freedompay.configuration.Configure;


public class HomeController extends Controller {
	
	//private Image banner;
	//private Image scaled;
	
	//--------------- PAGE INSTRUCTIONS ---------------------
	
	// Page instruction area
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
	
	
	// Load instructions
	public String getInstructions() {
		return this.instructions;
	}
	
	// Instructions container
	public JTextArea getTextArea(String txt) {
		JTextArea t = new JTextArea(txt);
		t.setLineWrap(true);
		t.setWrapStyleWord(true);
		return t;
	}

	
	public JLabel getJLabel(String txt) {
		return new JLabel(txt);
	}
}