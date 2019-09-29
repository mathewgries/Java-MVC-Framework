package com.freedompay.application;
import com.freedompay.views.View;
import com.freedompay.services.IRouteListener;
import java.awt.Dimension;
import javax.swing.*;

/*
 * This class is the JFrame for the whole application.
 * All JPanels/Views are added or removed to this JFrame
 * during route updates.
 */
public class BaseView extends JFrame implements IRouteListener {
	
	private static final long serialVersionUID = -209583737245413832L;
	
	// MainMenu is where the navigation buttons are placed
	private MainMenu menu;
	// The view for the current route
	private View view = null;
	
	/**
	 * <p>Initialize the MainMenu to the JFrame on start up</p>
	 * @param mm MainMenu object
	 */
	public void init(MainMenu mm) {
		menu = mm;
	}
	
	/**
	 * Set up the JFrame on start up with basic configurations
	 * @param w Width of the JFrame
	 * @param h Height of the JFrame
	 */
	public void createAndShowGUI(int w, int h) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		this.setTitle("MGries Java MVC Framework");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(w, h));
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		menu.build();
		
		this.setJMenuBar(menu);
		
		this.pack();
		this.setVisible(true);
	}

	/**
	 * <p>
	 * Remove previous view, if exists, and add new view
	 * </p>
	 */
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
