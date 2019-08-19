package com.freedompay.controllers;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.freedompay.components.*;
import com.freedompay.models.Model;
import com.freedompay.views.View;

public class Controller {
	
	public void addView(View v) {}
	
	public void addModel(Model m) {}
	
	public JLabel getJLabel(String txt) {return null;}
	
	public JLabel getImageLabel(JPanel p) {return null;}
	
	public JTextField getTextField(int length) {return null;}
	
	public JButton getButton(String txt) {return null;}
	
	public DecisionPanel getDecisionPanel() {return null;}
	
	public ScenePanel getScenePanel() {return null;}
	
	public StoryPanel getStoryPanel() {return null;}
	
	public JPanel buildFileInput(JPanel panel, String labeltxt) {return null;}
}
