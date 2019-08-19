package com.freedompay.components;
import com.freedompay.services.IRouteListener;
import com.freedompay.services.IRouteService;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JMenuBar;



public class MainMenu extends JMenuBar implements IRouteService, ActionListener {
	
	private static final long serialVersionUID = 6911428215410883596L;
	
	private List<IRouteListener> observers = new ArrayList<IRouteListener>();
	
	private JButton home;
	private JButton files;
	private JButton exit;
	
	public void addObserver(IRouteListener obj) {
		this.observers.add(obj);
	}
	
	public void removeObserver(IRouteListener obj) {
		this.observers.remove(obj);
	}
	
	@Override
	public void notifyObservers(Object obj) {
		for(IRouteListener rs : observers) {
			rs.update(obj);
		}
	}
	
	public void build() {
		home = new JButton("Home");
		home.setOpaque(true);
		home.setBackground(Color.WHITE);
		home.setBorderPainted(false);
		
		files = new JButton("Files");
		files.setOpaque(true);
		files.setBackground(Color.WHITE);
		files.setBorderPainted(false);
		
		exit = new JButton("Exit");
		exit.setOpaque(true);
		exit.setBackground(Color.WHITE);
		exit.setBorderPainted(false);
		
		home.addActionListener(this);
		files.addActionListener(this);
		exit.addActionListener(this);
		
		add(home);
		add(files);
		add(exit);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == home) {
			this.notifyObservers("home");
		}
		if(e.getSource() == files) {
			this.notifyObservers("files");
		}
		if(e.getSource() == exit) {
			System.exit(0);
		}
	}
}