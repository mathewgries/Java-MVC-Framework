package com.freedompay.views;
import com.freedompay.configuration.Configure;
import com.freedompay.components.*;
import com.freedompay.controllers.*;

import java.awt.*;
import javax.swing.*;

public class HomeView extends View {

	private static final long serialVersionUID = -2450001680426173348L;
	
	private Controller controller;
	private int width;
	private int height;
	private DecisionPanel decisionPanel;
	private ScenePanel scenePanel;
	private StoryPanel storyPanel;
	private JPanel leftPanel;
	private JPanel rightPanel;
	
	public void addController(Controller c) {
		controller = c;
	}
	
	public void setSize() {
		width = Configure.getVPWidth();
		height = Configure.getVPHeight();
	}
	
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
		this.decisionPanel.add(controller.getJLabel("Decision Panel - Home"));
	}
	
	public void buildScenePanel() {
		this.scenePanel = new ScenePanel();
		this.scenePanel.init();
		this.scenePanel.add(this.controller.getJLabel("ScenePanel - Home"));
		this.scenePanel.add(controller.getImageLabel(this.scenePanel));
	}
	
	public void buildStoryPanel() {
		this.storyPanel = new StoryPanel();
		this.storyPanel.init();
		this.storyPanel.add(controller.getJLabel("StoryPanel - Home"));
	}
}
