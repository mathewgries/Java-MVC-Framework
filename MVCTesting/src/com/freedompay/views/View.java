package com.freedompay.views;

import com.freedompay.controllers.*;
import javax.swing.JPanel;

public abstract class View extends JPanel {
	
	private static final long serialVersionUID = 8386386706268216201L;
	
	public abstract void addController(Controller c);
	
	public abstract void setSize();
	
	public abstract void build();
}