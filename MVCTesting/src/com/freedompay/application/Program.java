package com.freedompay.application;

import javax.swing.SwingUtilities;
import com.freedompay.configuration.*;

public class Program {
	
	private static BaseView baseView;
	private static MainMenu menu;
	private static RouteConfig router;
	
	public static void main(String[] args) {	
		Configure.setConfigurations();
		
		router = new RouteConfig();
		baseView = new BaseView();
		menu = new MainMenu();
		
		router.addObserver(baseView);
		menu.addObserver(router);
		baseView.init(menu);
		router.update("Home");
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				baseView.createAndShowGUI(Configure.getVPWidth(), Configure.getVPHeight());
			}
		});
	}
}