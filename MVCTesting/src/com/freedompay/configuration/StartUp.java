package com.freedompay.configuration;
import com.freedompay.application.*;

import javax.swing.SwingUtilities;

public class StartUp {
	
	private static BaseView baseView;
	private static MainMenu menu;
	
	
	public static void runApplication() {
		Configure.setConfigurations();
		
		baseView = new BaseView();
		menu = new MainMenu();
		
		RouteConfig.init(baseView);
		baseView.init(menu);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				baseView.createAndShowGUI(Configure.getVPWidth(), Configure.getVPHeight());
			}
		});
	}
}
