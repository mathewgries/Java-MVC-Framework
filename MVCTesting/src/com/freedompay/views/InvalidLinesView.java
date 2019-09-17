package com.freedompay.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import com.freedompay.controllers.Controller;

public class InvalidLinesView extends View {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8610858178420512857L;

	private Controller controller;
	
	@Override
	public void addController(Controller c) {
		controller = c;
	}
	
//==========================================================================================
	
	//BUILD THE MAIN PANEL - Stretches over entire JFrame View

//==========================================================================================
	
	@Override
	public void build() {
		Border bevel = BorderFactory.createLoweredBevelBorder();
		this.setBorder(bevel);
		this.setLayout(new BorderLayout());
		this.buildLeftDivider();
		this.buildRightDivider();
		this.add(leftPanel, BorderLayout.WEST);
		this.add(rightPanel, BorderLayout.CENTER);
	}
	
//==========================================================================================
	
	//DECISION PANEL DISPLAY - Buttons for add and remove

//==========================================================================================
	
	private JPanel leftPanel;
	
	private void buildLeftDivider() {
		this.leftPanel = new JPanel();		
		
		Border bevel = BorderFactory.createLoweredBevelBorder();
		this.leftPanel.setBorder(bevel);
		this.leftPanel.setLayout(new BoxLayout(this.leftPanel, BoxLayout.PAGE_AXIS));
		
		JLabel l = this.controller.getJLabel("FreedomPay File Compare");
		JPanel btnPanel = this.buildBtnPanel();
		JPanel scene = this.buildScenePanel();
		
		l.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		scene.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		this.leftPanel.add(l);
		this.leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		this.leftPanel.add(btnPanel);
		this.leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		this.leftPanel.add(scene);
	}
	
	//------------------ Uploaded File List --------------------------

	private JPanel buildScenePanel() {
		JPanel scene = new JPanel();
		JLabel fileLabel = this.controller.getJLabel("Uploaded Files"); 
		JLabel columnLabel = this.controller.getJLabel("Matched Columns");
		JScrollPane fileListScrollPane = this.controller.getFileNameList();
		JScrollPane matchedColumnsScrollPane = this.controller.getMatchedHeadersTable();
		
		scene.setLayout(new BoxLayout(scene, BoxLayout.PAGE_AXIS));
		fileLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		fileListScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		columnLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		matchedColumnsScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		scene.add(fileLabel);
		scene.add(fileListScrollPane);
		scene.add(columnLabel);
		scene.add(matchedColumnsScrollPane);
		
		return scene;
	}
	
	private JPanel buildBtnPanel() {
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(3,2));
		JButton backBtn = this.controller.getBackBtn();
		p.add(backBtn);
		return p;
	}
	
//==========================================================================================
	
	//FILE DISPLAY - Show uploaded files and invalid lines for each file

//==========================================================================================
	
	private JPanel rightPanel;	
	
	//-------------- Main panel for scene and story panel ---------
	
	private void buildRightDivider() {
		this.rightPanel = new JPanel();
		this.rightPanel.setLayout(new BoxLayout(this.rightPanel, BoxLayout.PAGE_AXIS));
		this.rightPanel.setBackground(Color.WHITE);
		Border bevel = BorderFactory.createLoweredBevelBorder();
		this.rightPanel.setBorder(bevel);
		
		JPanel story = this.buildStoryPanel();
		
		story.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		this.rightPanel.add(story);
	}

	//--------------- Show invalid lines for files -------------------
	
	private JPanel buildStoryPanel() {
		JPanel story = new JPanel();
		JLabel label = this.controller.getJLabel("Invalid File Lines");
		JScrollPane invalidLines = this.controller.getInvalidLineItemsList();
		story.setLayout(new BoxLayout(story, BoxLayout.PAGE_AXIS));
		
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		invalidLines.setAlignmentX(Component.CENTER_ALIGNMENT);

		story.add(label);
		story.add(invalidLines);
		return story;
	}
	
}
