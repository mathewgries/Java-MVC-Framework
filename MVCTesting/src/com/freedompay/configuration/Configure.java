package com.freedompay.configuration;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;

/**
 * <p>
 * Global application configurations.
 * Gets the Viewport dimensions from the system
 * Add any global configurations for your app here
 * </p>
 * @author MGries
 *
 */
public class Configure {
	
	private static int viewWidth;
	private static int viewHeight;
	
	/**
	 * <p>
	 * Init method called on application start
	 * Currently sets the application window to fullscreen
	 * </p>
	 */
	public static void setConfigurations() {
		viewWidth = (int) getScreenViewableBounds().getWidth();
		viewHeight = (int) getScreenViewableBounds().getHeight();
	}
	
	/**
	 * <p>Returns the window width</p>
	 * @return
	 */
	public static int getVPWidth() {
		return viewWidth;
	}

	/**
	 * <p>Returns the window height</p>
	 * @return
	 */
	public static int getVPHeight() {
		return viewHeight;	
	}
	
	/**
	 * <p>
	 * The system Viewport dimensions. The dimensions set here
	 * are assigned to the BaseView for Fullscreen.
	 * </p>
	 * @return
	 */
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
