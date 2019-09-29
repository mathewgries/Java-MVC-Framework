package com.freedompay.application;
import com.freedompay.services.IRouteListener;
import com.freedompay.services.IRouteService;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JMenuBar;

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
	private JButton homeBtn;
	private JButton filesBtn;
	//private JButton invalidRowsBtn;
	private JButton matchedRowsBtn;
	private JButton exitBtn;
	
	// observer methods
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
	

	
	/**
	 * <p>Build the navigation menu</p>
	 */
	public void build() {
		homeBtn = new JButton("Home");
		homeBtn.setOpaque(true);
		homeBtn.setBackground(Color.WHITE);
		homeBtn.setBorderPainted(false);
		
		filesBtn = new JButton("Files");
		filesBtn.setOpaque(true);
		filesBtn.setBackground(Color.WHITE);
		filesBtn.setBorderPainted(false);
		
//		invalidRowsBtn = new JButton("Invalid Rows");
//		invalidRowsBtn.setOpaque(true);
//		invalidRowsBtn.setBackground(Color.WHITE);
//		invalidRowsBtn.setBorderPainted(false);
		
		matchedRowsBtn = new JButton("Matched Rows");
		matchedRowsBtn.setOpaque(true);
		matchedRowsBtn.setBackground(Color.WHITE);
		matchedRowsBtn.setBorderPainted(false);
		
		exitBtn = new JButton("Exit");
		exitBtn.setOpaque(true);
		exitBtn.setBackground(Color.WHITE);
		exitBtn.setBorderPainted(false);
		
		homeBtn.addActionListener(this);
		filesBtn.addActionListener(this);
//		invalidRowsBtn.addActionListener(this);
		matchedRowsBtn.addActionListener(this);
		exitBtn.addActionListener(this);
		
		add(homeBtn);
		add(filesBtn);
//		add(invalidRowsBtn);
		add(matchedRowsBtn);
		add(exitBtn);
	}
	
	// TODO:
	// This is already set up in the RouteConfig.
	// Figure out how to set the focus on the nav link here.
	// The view is the object being passed in from RouteConfig
	@Override
	public void update(Object obj) {
		if(obj.getClass().getName().equalsIgnoreCase("com.freedompay.views.HomeView")) {
			homeBtn.requestFocus();
		} 
		if(obj.getClass().getName().equalsIgnoreCase("com.freedompay.views.FileView")) {
			filesBtn.requestFocus();	
		} 
//		if(obj.getClass().getName().equalsIgnoreCase("com.freedompay.views.InvalidRowsView")) {
//			invalidRowsBtn.requestFocus();
//		} 
		if(obj.getClass().getName().equalsIgnoreCase("com.freedompay.views.MatchedRowsView")) {
			matchedRowsBtn.requestFocus();
		} 
	}
	
	/**
	 * <p>
	 * Notifies the RouteConfig when a link is clicked
	 * </p>
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == homeBtn) {
			this.notifyObservers("Home");
		}
		if(e.getSource() == filesBtn) {
			this.notifyObservers("Files");
		}
//		if(e.getSource() == invalidRowsBtn) {
//			this.notifyObservers("InvalidRows");
//		}
		if(e.getSource() == matchedRowsBtn) {
			this.notifyObservers("MatchedRows");
		}
		if(e.getSource() == exitBtn) {
			System.exit(0);
		}
	}
}