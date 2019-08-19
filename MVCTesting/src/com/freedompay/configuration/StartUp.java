package com.freedompay.configuration;
import com.freedompay.application.BaseView;
import com.freedompay.components.MainMenu;
import javax.swing.SwingUtilities;

public class StartUp {
	
	private static BaseView baseView;
	private static MainMenu menu;
	private static RouteConfig router;
	
	
	public static void runApplication() {
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
