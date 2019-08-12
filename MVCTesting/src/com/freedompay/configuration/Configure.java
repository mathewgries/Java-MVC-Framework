package com.freedompay.configuration;

import java.awt.Dimension;
import java.awt.Toolkit;

public class Configure {
	
	private static int viewWidth;
	private static int viewHeight;
	
	public static void setConfigurations() {
		Dimension viewport = Toolkit.getDefaultToolkit().getScreenSize();
		viewWidth = (int) viewport.getWidth();
		viewHeight = (int) viewport.getHeight();
	}
	
	public static int getVPWidth() {
		return viewWidth;
	}

	public static int getVPHeight() {
		return viewHeight;
	}
}
