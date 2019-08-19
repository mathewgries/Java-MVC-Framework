package com.freedompay.application;
import com.freedompay.views.View;
import com.freedompay.components.MainMenu;
import com.freedompay.services.IRouteListener;

import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.*;

public class BaseView extends JFrame implements IRouteListener {
	
	private static final long serialVersionUID = -209583737245413832L;
	
	private MainMenu menu;
	private View view = null;
	
	public void init(MainMenu mm) {
		menu = mm;
	}
	
	public void createAndShowGUI(int w, int h) {
		this.setLayout(new GridBagLayout());
		this.setTitle("MGries Java MVC Framework");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(w, h));
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		menu.build();
		this.setJMenuBar(menu);
		
		this.pack();
		this.setVisible(true);
	}

	@Override
	public void update(Object obj) {
		if(this.view != null) {
			this.getContentPane().remove(this.view);
		}
		this.view = (View) obj;
		this.view.build();
		this.add(this.view);
		this.getContentPane().invalidate();
		this.getContentPane().validate();
	}
}
