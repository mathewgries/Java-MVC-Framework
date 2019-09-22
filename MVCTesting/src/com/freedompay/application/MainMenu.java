package com.freedompay.application;
import com.freedompay.services.IRouteListener;
import com.freedompay.services.IRouteService;
import com.freedompay.views.View;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JMenuBar;

//TODO: Figure out how to set the focus on the nav links based on the 
// current view state.

/**
 * <p>
 * This is the navigation menu. It works like a nav menu on a website
 * </p>
 * @author MGries
 *
 */
public class MainMenu extends JMenuBar implements IRouteService, IRouteListener, ActionListener {
	
	private static final long serialVersionUID = 6911428215410883596L;
	
	private List<IRouteListener> observers = new ArrayList<IRouteListener>();
	
	// Nav buttons that act like site links
	private JButton home;
	private JButton files;
	private JButton invalidRows;
	private JButton matchedRows;
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
	
	// TODO:
	// This is already set up in the RouteConfig.
	// Figure out how to set the focus on the nav link here.
	// The view is the object being passed in from RouteConfig
	@Override
	public void update(Object obj) {
		
	}
	
	/**
	 * <p>Build the navigation menu</p>
	 */
	public void build() {
		home = new JButton("Home");
		home.setOpaque(true);
		home.setBackground(Color.WHITE);
		home.setBorderPainted(false);
		
		files = new JButton("Files");
		files.setOpaque(true);
		files.setBackground(Color.WHITE);
		files.setBorderPainted(false);
		
		invalidRows = new JButton("Invalid Rows");
		invalidRows.setOpaque(true);
		invalidRows.setBackground(Color.WHITE);
		invalidRows.setBorderPainted(false);
		
		matchedRows = new JButton("Matched Rows");
		matchedRows.setOpaque(true);
		matchedRows.setBackground(Color.WHITE);
		matchedRows.setBorderPainted(false);
		
		exit = new JButton("Exit");
		exit.setOpaque(true);
		exit.setBackground(Color.WHITE);
		exit.setBorderPainted(false);
		
		home.addActionListener(this);
		files.addActionListener(this);
		invalidRows.addActionListener(this);
		matchedRows.addActionListener(this);
		exit.addActionListener(this);
		
		add(home);
		add(files);
		add(invalidRows);
		add(matchedRows);
		add(exit);
	}
	
	/**
	 * <p>
	 * Notifies the RouteConfig when a link is clicked
	 * </p>
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == home) {
			this.notifyObservers("Home");
		}
		if(e.getSource() == files) {
			this.notifyObservers("Files");
		}
		if(e.getSource() == invalidRows) {
			
			this.notifyObservers("InvalidRows");
		}
		if(e.getSource() == matchedRows) {
			this.notifyObservers("MatchedRows");
		}
		if(e.getSource() == exit) {
			System.exit(0);
		}
	}
}