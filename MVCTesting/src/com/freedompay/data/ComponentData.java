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
	private static boolean uncapturedBtnIsEnabled = true;
	private static boolean capturedBtnIsEnabled = true;
	private static boolean matchedUncapturedBtnIsEnabled = false;
	private static boolean matchedCapturedBtnIsEnabled = false;
	private static boolean duplicateUncapturedBtnIsEnabled = false;
	private static boolean duplicateCapturedBtnIsEnabled = false;
	
	//==============================================================================
	
	public static boolean setPosBtnIsEnabled() {
		ComponentData.posBtnIsEnabled = ComponentData.posBtnIsEnabled ? false : true;
		return ComponentData.posBtnIsEnabled;
	}
	
	public static boolean getPosBtnIsEnabled() {
		return ComponentData.posBtnIsEnabled;
	}
	
	//==============================================================================
	
	public static boolean setUncapturedBtnIsEnabled() {
		ComponentData.uncapturedBtnIsEnabled = ComponentData.uncapturedBtnIsEnabled ? false : true;
		return ComponentData.uncapturedBtnIsEnabled;
	}
	
	public static boolean getUncapturedBtnIsEnabled() {
		return ComponentData.uncapturedBtnIsEnabled;
	}
	
	//==============================================================================
	
	public static boolean setCapturedBtnIsEnabled() {
		ComponentData.capturedBtnIsEnabled = ComponentData.capturedBtnIsEnabled ? false : true;
		return ComponentData.capturedBtnIsEnabled;
	}
	
	public static boolean getCapturedBtnIsEnabled() {
		return ComponentData.capturedBtnIsEnabled;
	}
	
	//==============================================================================
	
	public static boolean setMatchedUncapturedBtnIsEnabled(boolean isEnabled) {
		ComponentData.matchedUncapturedBtnIsEnabled = isEnabled;
		return ComponentData.matchedUncapturedBtnIsEnabled;
	}
	
	public static boolean getMatchedUncapturedBtnIsEnabled() {
		return ComponentData.matchedUncapturedBtnIsEnabled;
	}
	
	//==============================================================================
	
	public static boolean setMatchedCapturedBtnIsEnabled(boolean isEnabled) {
		ComponentData.matchedCapturedBtnIsEnabled = isEnabled;
		return ComponentData.matchedCapturedBtnIsEnabled;
	}
	
	public static boolean getMatchedCapturedBtnIsEnabled() {
		return ComponentData.matchedCapturedBtnIsEnabled;
	}
	
	//==============================================================================
	
	public static boolean setDuplicateUncapturedBtnIsEnabled(boolean isEnabled) {
		ComponentData.duplicateUncapturedBtnIsEnabled = isEnabled;
		return ComponentData.duplicateUncapturedBtnIsEnabled;
	}
	
	public static boolean getDuplicateUncapturedBtnIsEnabled() {
		return ComponentData.duplicateUncapturedBtnIsEnabled;
	}
	
	//==============================================================================
	
	public static boolean setDuplicateCapturedBtnIsEnabled(boolean isEnabled) {
		ComponentData.duplicateCapturedBtnIsEnabled = isEnabled;
		return ComponentData.duplicateCapturedBtnIsEnabled;
	}
	
	public static boolean getDuplicateCapturedBtnIsEnabled() {
		return ComponentData.duplicateCapturedBtnIsEnabled;
	}
}
