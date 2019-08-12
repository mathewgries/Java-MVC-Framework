package com.freedompay.controllers;
import com.freedompay.configuration.RouteConfig;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.imageio.*;
import javax.swing.*;
import com.freedompay.components.*;
import com.freedompay.views.*;


public class HomeController extends Controller {
	
	private View view;
	private DecisionPanel decisionPanel;
	private ScenePanel scenePanel;
	private StoryPanel storyPanel;
	private JLabel decisionLabel;
	private JLabel sceneLabel;
	private JLabel storyLabel;
	
	private Image banner;
	private Image scaled;
	private JLabel image;
	
	public void addView(View v) {
		view = v;
	}
	
	public JLabel resizeImage() {
		JLabel picLabel = null;
		try {
			this.banner = ImageIO.read(new File("src/com/freedompay/images/Banner.png"));
			this.scaled = this.banner.getScaledInstance(
					this.scenePanel.getWidth(),
					this.scenePanel.getHeight(),
					Image.SCALE_SMOOTH
			);
			
	    	picLabel = new JLabel(new ImageIcon(this.scaled));
		}catch(IOException ex) {
			System.out.println("Cannot find image");
			return null;
		}
		return picLabel;
	}
	
	public DecisionPanel buildDecisionPanel() {
		this.decisionPanel = new DecisionPanel();
		this.decisionLabel = new JLabel("Decision Panel - Home");		
		this.decisionPanel.init();
		this.decisionPanel.add(decisionLabel);
		this.decisionPanel.add(this.startButton());
		return this.decisionPanel;
	}
	
	public ScenePanel buildScenePanel() {
		this.scenePanel = new ScenePanel();
		this.sceneLabel = new JLabel("ScenePanel - Home");
		this.scenePanel.init();
		//this.scenePanel.add(sceneLabel);
		image = resizeImage();
		this.scenePanel.add(image);
		return this.scenePanel;
	}
	
	public StoryPanel buildStoryPanel() {
		this.storyPanel = new StoryPanel();
		this.storyLabel = new JLabel("StoryPanel - Home");
		this.storyPanel.init();
		this.storyPanel.add(storyLabel);
		return this.storyPanel;
	}
	
	private JButton startButton() {
		JButton btn = new JButton("Start");
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RouteConfig.setRoute(new FileController(), new FileView());
			}
		});
		
		return btn;
	}
}
