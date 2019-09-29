package com.freedompay.application;
import javax.swing.SwingUtilities;
import com.freedompay.configuration.*;

public class Program {
	
	private static BaseView baseView;
	private static MainMenu menu;
	private static RouteConfig router;
	
	public static void main(String[] args) {	
		
		/**
		 * THIS IS WHERE THE PROGRAM STARTS
		 * 
		 * For instructions on how to control routing,
		 * please see the com.freedompay.configuration.RouteConfig's update method.
		 */
		
		// Sets up the size values for the JFrame
		// Configure is global. Add global configs to that
		// class file, and you can initialize them here.
		Configure.setConfigurations();
		
		// RouteConfig sets up the routing for the app
		// Defaults at HomeController/HomeView
		router = new RouteConfig();
		
		// The JFrame for the application
		// Shows the View class for the given route 
		baseView = new BaseView();
		
		// The navigation menu for the application
		menu = new MainMenu();
		
		router.addObserver(baseView);
		menu.addObserver(router);
		baseView.init(menu);
		// Default the start up View to HomeView
		router.update("Home");
		router.addObserver(menu);
		
		// Run the BaseView in a EDT thread
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				baseView.createAndShowGUI(Configure.getVPWidth(), Configure.getVPHeight());
			}
		});
	}
}