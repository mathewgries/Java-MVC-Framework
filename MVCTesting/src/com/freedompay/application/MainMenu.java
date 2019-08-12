package com.freedompay.application;
import com.freedompay.configuration.RouteConfig;
import com.freedompay.controllers.*;
import com.freedompay.views.*;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class MainMenu extends JMenuBar {
	
	private static final long serialVersionUID = 6911428215410883596L;
	
	private RouteConfig router;
	
	public void init(RouteConfig r) {
		router = r;
	}
	
	public void build() {
		JMenu fileMenu = new JMenu("File");
		JMenuItem homeRoute = new JMenuItem("Home");
		
		homeRoute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RouteConfig.setRoute(new HomeController(), new HomeView());
			}
		});
		
		fileMenu.add(homeRoute);
		add(fileMenu);
	}
}