package com.freedompay.views;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.freedompay.components.DecisionPanel;
import com.freedompay.components.ScenePanel;
import com.freedompay.components.StoryPanel;
import com.freedompay.configuration.Configure;
import com.freedompay.controllers.Controller;

public class FileView extends View {

	private static final long serialVersionUID = -794968485917811795L;
		
	private int width;
	private int height;
	private Controller controller;
	private JPanel leftPanel;
	private JPanel rightPanel;
	private DecisionPanel decisionPanel;
	private ScenePanel scenePanel;
	private StoryPanel storyPanel;
	private JPanel input1;
	private JPanel input2;
	private JPanel input3;
	
	@Override
	public void addController(Controller c) {
		controller = c;
	}

	@Override
	public void setSize() {
		width = Configure.getVPWidth();
		height = Configure.getVPHeight();
	}

	@Override
	public void build() {
		this.setSize();
		this.setPreferredSize(new Dimension(this.width, this.height));
		this.setMaximumSize(new Dimension(this.width, this.height));
		this.setMinimumSize(new Dimension(this.width, this.height));
		this.buildLeftDivider();
		this.buildRightDivider();
		this.buildDecisionPanel();
		this.buildScenePanel();
		this.buildStoryPanel();
		leftPanel.add(decisionPanel);
		rightPanel.add(scenePanel);
		rightPanel.add(storyPanel);
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(layout.createSequentialGroup()
			.addComponent(leftPanel)
			.addComponent(rightPanel)
		);
		layout.setVerticalGroup(layout.createParallelGroup()
			.addComponent(leftPanel)
			.addComponent(rightPanel)
		);
	}
	
	private void buildLeftDivider() {
		leftPanel = new JPanel();
		leftPanel.setBackground(new Color(0,0,0,0));
		leftPanel.setPreferredSize(new Dimension((this.width/4), this.height));
		leftPanel.setMinimumSize(new Dimension((this.width/4), this.height));
		leftPanel.setMaximumSize(new Dimension((this.width/4), this.height));
	}
	
	private void buildRightDivider() {
		rightPanel = new JPanel();
		rightPanel.setBackground(new Color(0,0,0,0));
		rightPanel.setPreferredSize(new Dimension(((this.width/4)*3), this.height));
		rightPanel.setMinimumSize(new Dimension(((this.width/4)*3), this.height));
		rightPanel.setMaximumSize(new Dimension(((this.width/4)*3), this.height));
	}
	
	public void buildDecisionPanel() {
		this.decisionPanel = new DecisionPanel();
		this.decisionPanel.init();
		this.decisionPanel.add(this.controller.getJLabel("Decision Panel - File"));
	}

	public void buildScenePanel() {
		this.scenePanel = new ScenePanel();
		this.scenePanel.init();
		scenePanel.setLayout(new BoxLayout(scenePanel, BoxLayout.PAGE_AXIS));
		scenePanel.add(this.controller.getJLabel("Upload Files"));
		scenePanel.add(this.buildFileInput(this.input1, "File 1"));
		scenePanel.add(this.buildFileInput(this.input2, "File 2"));
		scenePanel.add(this.buildFileInput(this.input3, "File 3"));
		scenePanel.setBackground(Color.WHITE);
	}

	public void buildStoryPanel() {
		this.storyPanel = new StoryPanel();
		this.storyPanel.init();
		this.storyPanel.add(this.controller.getJLabel("StoryPanel - File"));
	}
	
	public JPanel buildFileInput(JPanel panel, String labeltxt) {
		panel = new JPanel();
		panel.setBackground(new Color(0,0,0,0));
		JLabel label = this.controller.getJLabel(labeltxt);
		JButton btnBrowse = this.controller.getButton("Browse");
		JButton btnRemove = this.controller.getButton("Remove");
		JTextField tf = this.controller.getTextField(20);
		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(layout.createParallelGroup()
			.addComponent(label)
			.addGroup(layout.createSequentialGroup()
				.addComponent(btnBrowse)
				.addComponent(btnRemove)
				.addComponent(tf)
			)
		);
		layout.setVerticalGroup(layout.createSequentialGroup()
			.addComponent(label)
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(btnBrowse)
				.addComponent(btnRemove)
				.addComponent(tf))
		);
		
		return panel;
	}
}
