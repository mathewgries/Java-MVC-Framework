package com.freedompay.controllers;
import com.freedompay.components.*;
import com.freedompay.views.View;

public abstract class Controller {
	
	public abstract void addView(View v);
	
	public abstract DecisionPanel buildDecisionPanel();
	
	public abstract ScenePanel buildScenePanel();
	
	public abstract StoryPanel buildStoryPanel();
}
