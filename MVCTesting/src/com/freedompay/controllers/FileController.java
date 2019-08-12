package com.freedompay.controllers;
import com.freedompay.components.*;
import com.freedompay.views.View;

import java.awt.Image;
import javax.swing.JLabel;

public class FileController extends Controller {

	private View view;
	private DecisionPanel decisionPanel;
	private ScenePanel scenePanel;
	private StoryPanel storyPanel;
	private Image banner;
	private JLabel decisionLabel;
	private JLabel sceneLabel;
	private JLabel storyLabel;
	
	@Override
	public void addView(View v) {
		view = v;
	}

	@Override
	public DecisionPanel buildDecisionPanel() {
		this.decisionPanel = new DecisionPanel();
		this.decisionLabel = new JLabel("Decision Panel - File");
		this.decisionPanel.init();
		this.decisionPanel.add(decisionLabel);
		return this.decisionPanel;
	}

	@Override
	public ScenePanel buildScenePanel() {
		this.scenePanel = new ScenePanel();
		this.sceneLabel = new JLabel("ScenePanel - File");
		this.scenePanel.init();
		this.scenePanel.add(sceneLabel);
		return this.scenePanel;
	}

	@Override
	public StoryPanel buildStoryPanel() {
		this.storyPanel = new StoryPanel();
		this.storyLabel = new JLabel("StoryPanel - File");
		this.storyPanel.init();
		this.storyPanel.add(storyLabel);
		return this.storyPanel;
	}
}
