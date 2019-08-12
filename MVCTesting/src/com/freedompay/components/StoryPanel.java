package com.freedompay.components;
import com.freedompay.configuration.Configure;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;



public class StoryPanel extends JPanel {

	private static final long serialVersionUID = -5009575111284102257L;
	
	private int width;
	private int height;
	
	public void init() {
		this.setSize();
		this.setBackground(Color.WHITE);
	}
	
	public void setSize() {
		this.width = (Configure.getVPWidth()/3)*2;
		this.height = Configure.getVPHeight()/2;
		
		this.setPreferredSize(new Dimension(this.width, this.height));
	}
}
