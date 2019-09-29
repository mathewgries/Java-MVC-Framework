package com.freedompay.views;

/**
 * The base class for the view. Not abstract.
 * Allows for assigning views anonymously from the BaseView,
 * and build the selected view.
 */
import com.freedompay.controllers.*;
import com.freedompay.models.FileModel;

import java.util.List;

import javax.swing.JPanel;

public class View extends JPanel {
	
	private static final long serialVersionUID = 8386386706268216201L;
	
	public void addController(Controller c) {}
	
	public void setSize() {}
	
	public void build() {}
	
	public void addFileNameToList(List<FileModel> files) {}
}