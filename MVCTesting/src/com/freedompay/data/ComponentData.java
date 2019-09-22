package com.freedompay.data;

/**
 * <p>
 * Saves the state of the file upload buttons.
 * Enabled if file not uploaded. Disabled if
 * file uploaded. This allows to switch views without
 * losing state data.
 * </p>
 * @author MGries
 *
 */
public class ComponentData {
	
	// Booleans for button state
	private static boolean posBtnIsEnabled = true;
	private static boolean authBtnIsEnabled = true;
	private static boolean batchedBtnIsEnabled = true;
	
	//==============================================================================
	
	public static boolean setPosBtnIsEnabled() {
		ComponentData.posBtnIsEnabled = ComponentData.posBtnIsEnabled ? false : true;
		return ComponentData.posBtnIsEnabled;
	}
	
	public static boolean getPosBtnIsEnabled() {
		return ComponentData.posBtnIsEnabled;
	}
	
	//==============================================================================
	
	public static boolean setAuthBtnIsEnabled() {
		ComponentData.authBtnIsEnabled = ComponentData.authBtnIsEnabled ? false : true;
		return ComponentData.authBtnIsEnabled;
	}
	
	public static boolean getAuthBtnIsEnabled() {
		return ComponentData.authBtnIsEnabled;
	}
	
	//==============================================================================
	
	public static boolean setBatchedBtnIsEnabled() {
		ComponentData.batchedBtnIsEnabled = ComponentData.batchedBtnIsEnabled ? false : true;
		return ComponentData.batchedBtnIsEnabled;
	}
	
	public static boolean getBatchedBtnIsEnabled() {
		return ComponentData.batchedBtnIsEnabled;
	}
}
