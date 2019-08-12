package com.freedompay.components;
import com.freedompay.configuration.Configure;

import java.awt.*;
import javax.swing.*;

public class DecisionPanel extends JPanel {

	private static final long serialVersionUID = -5334178730174253804L;
	
	private int width;
	private int height;

	
	public void init() {
		this.setSize();
		this.setBackground(Color.WHITE);
	}

	public void setSize() {
		this.width = Configure.getVPWidth();
		this.height = Configure.getVPHeight();
		this.setPreferredSize(new Dimension(this.width, this.height));
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
}
