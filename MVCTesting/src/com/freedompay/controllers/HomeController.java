package com.freedompay.controllers;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class HomeController extends Controller {
	
	private Image banner;
	private Image scaled;
	
	public JLabel getImageLabel(JPanel panel) {
		JLabel picLabel = null;
		try {
			this.banner = ImageIO.read(new File("src/com/freedompay/images/Banner.png"));
			this.scaled = this.banner.getScaledInstance(
					panel.getWidth(),
					panel.getHeight(),
					Image.SCALE_SMOOTH
			);
			
	    	picLabel = new JLabel(new ImageIcon(this.scaled));
		}catch(IOException ex) {
			System.out.println("Cannot find image");
			return null;
		}
		return picLabel;
	}
	
	public JLabel getJLabel(String txt) {
		return new JLabel(txt);
	}
}