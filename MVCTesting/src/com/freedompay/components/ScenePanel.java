package com.freedompay.components;
import com.freedompay.configuration.Configure;

import java.awt.*;
import javax.swing.*;



public class ScenePanel extends JPanel {

	private static final long serialVersionUID = 7648240312002662737L;
	
	private int width;
	private int height;
	
	public void init() {
		this.setSize();
		this.setBackground(Color.WHITE);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setSize() {
		this.width = (Configure.getVPWidth()/3)*2;
		this.height = Configure.getVPHeight()/2;
		this.setPreferredSize(new Dimension(this.width, this.height));
	}
}
