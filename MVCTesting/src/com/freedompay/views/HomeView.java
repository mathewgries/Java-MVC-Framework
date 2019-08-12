package com.freedompay.views;
import com.freedompay.components.*;
import com.freedompay.configuration.Configure;
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
		
		//this.setBackground(new Color(68, 62, 69));
		
		this.decisionPanel = controller.buildDecisionPanel();
		this.scenePanel = controller.buildScenePanel();
		this.storyPanel = controller.buildStoryPanel();
		
		buildPanels();
		
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
	
	private void buildPanels() {
		leftPanel = new JPanel();
		rightPanel = new JPanel();
		
		leftPanel.setBackground(new Color(0,0,0,0));
		rightPanel.setBackground(new Color(0,0,0,0));
		
		leftPanel.setPreferredSize(new Dimension((this.width/4), this.height));
		leftPanel.setMinimumSize(new Dimension((this.width/4), this.height));
		leftPanel.setMaximumSize(new Dimension((this.width/4), this.height));
		
		rightPanel.setPreferredSize(new Dimension(((this.width/4)*3), this.height));
		rightPanel.setMinimumSize(new Dimension(((this.width/4)*3), this.height));
		rightPanel.setMaximumSize(new Dimension(((this.width/4)*3), this.height));
	}
}
