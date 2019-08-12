package com.freedompay.application;
import com.freedompay.views.*;
import com.freedompay.configuration.RouteConfig;
import com.freedompay.controllers.*;

import java.awt.Dimension;
import javax.swing.*;

public class BaseView extends JFrame {
	
	private static final long serialVersionUID = -209583737245413832L;
	
	private MainMenu menu;
	
	public void init(MainMenu mm) {
		menu = mm;
	}
	
	public void createAndShowGUI(int w, int h) {
		this.setTitle("MGries Java MVC Framework");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(w, h));
		//this.setMaximumSize(new Dimension(w,h));
		//this.setMinimumSize(new Dimension(w, h));
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		menu = new MainMenu();
		menu.build();
		this.setJMenuBar(menu);
		
		RouteConfig.setRoute(new HomeController(), new HomeView());
		
		this.pack();
		this.setVisible(true);
	}
}
