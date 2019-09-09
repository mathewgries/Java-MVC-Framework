package com.freedompay.configuration;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;

public class Configure {
	
	private static int viewWidth;
	private static int viewHeight;
	
	public static void setConfigurations() {
		viewWidth = (int) getScreenViewableBounds().getWidth();
		viewHeight = (int) getScreenViewableBounds().getHeight();
	}
	
	public static int getVPWidth() {
		return viewWidth;
	}

	public static int getVPHeight() {
		return viewHeight;	
	}
	
	public static Rectangle getScreenViewableBounds() {

	    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    GraphicsDevice gd = ge.getDefaultScreenDevice();

	    Rectangle bounds = new Rectangle(0, 0, 0, 0);

	    if (gd != null) {

	        GraphicsConfiguration gc = gd.getDefaultConfiguration();
	        bounds = gc.getBounds();

	        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(gc);

	        bounds.x += insets.left;
	        bounds.y += insets.top;
	        bounds.width -= (insets.left + insets.right);
	        bounds.height -= (insets.top + insets.bottom);

	    }

	    return bounds;

	}
}
