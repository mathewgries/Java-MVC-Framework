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

public class MatchedRowsView extends View {

	private static final long serialVersionUID = 1629215632273169120L;
	
	private Controller controller;
	
	/**
	 * <p>Add the MatchedRowsController</p>
	 */
	@Override
	public void addController(Controller c) {
		controller = c;
	}

	
//==========================================================================================

	//BUILD THE MAIN PANEL - Stretches over entire JFrame View

//==========================================================================================
		
	/**
	 * <p>Build the view</p>
	 */
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
	
	/**
	 * <p>
	 * The left panel holds the decision buttons for switching table views,
	 * as well as the File name list, and matched headers table
	 * </p>
	 */
	private void buildLeftDivider() {
		this.leftPanel = new JPanel();		
		
		Border bevel = BorderFactory.createLoweredBevelBorder();
		this.leftPanel.setBorder(bevel);
		this.leftPanel.setLayout(new BoxLayout(this.leftPanel, BoxLayout.PAGE_AXIS));
		
		JLabel l = this.controller.getJLabel("Matched Rows");
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

	/**
	 * <p>
	 * The scene panel holds the file name list, and the matched headers table
	 * It is added to the left panel
	 * </p>
	 * @return scene panel
	 */
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
	
	/**
	 * <p>The buttons for switching the table view</p>
	 * @return The button panel
	 */
	private JPanel buildBtnPanel() {
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(4,2));
		
		JButton matchedUncapturedBtn = this.controller.getMatchedUncapturedBtn();
		JButton matchedCapturedBtn = this.controller.getMatchedCapturedBtn();
		JButton duplicateUncapturedBtn = this.controller.getDuplicateUncapturedBtn();
		JButton duplicateCapturedBtn = this.controller.getDuplicateCapturedBtn();
		JButton noMatchBtn = this.controller.getNoMatchBtn();
		JButton exportBtn = this.controller.getExportBtn();
		JButton backBtn = this.controller.getBackBtn();
		
		p.add(matchedUncapturedBtn);
		p.add(matchedCapturedBtn);
		p.add(duplicateUncapturedBtn);
		p.add(duplicateCapturedBtn);
		p.add(noMatchBtn);
		p.add(exportBtn);
		p.add(backBtn);
		return p;
	}
	
//==========================================================================================

	//FILE DISPLAY - Show uploaded files and invalid lines for each file

//==========================================================================================
		
	private JPanel rightPanel;	
	
	//-------------- Main panel for scene and story panel ---------
	
	/**
	 * <p>
	 * The right panel to hold the JPanel for the matched rows table
	 * </p>
	 */
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
	
	/**
	 * <p>
	 * The JPanel to hold the matched rows, duplicated rows, and unmatched rows table data
	 * </p>
	 * @return
	 */
	private JPanel buildStoryPanel() {
		JPanel story = new JPanel();
		JLabel label = this.controller.getJLabel("Comparison Tables");
		JScrollPane compareResults = this.controller.getComparisonResultsTable();
		story.setLayout(new BoxLayout(story, BoxLayout.PAGE_AXIS));
		
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		compareResults.setAlignmentX(Component.CENTER_ALIGNMENT);

		story.add(label);
		story.add(compareResults);
		return story;
	}
}
