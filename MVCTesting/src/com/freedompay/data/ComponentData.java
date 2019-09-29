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
	
	/**
	 * <p>Toggle for enabling and disabling posBtn in FileController</p>
	 * @return State of posBtn after toggle
	 */
	public static boolean setPosBtnIsEnabled() {
		ComponentData.posBtnIsEnabled = ComponentData.posBtnIsEnabled ? false : true;
		return ComponentData.posBtnIsEnabled;
	}
	
	/**
	 * @return boolean value for posBtn enabled
	 */
	public static boolean getPosBtnIsEnabled() {
		return ComponentData.posBtnIsEnabled;
	}
	
	//==============================================================================
	
	/**
	 * <p>Toggle for enabling and disabling uncapturedBtn in FileController</p>
	 * @return State of uncapturedBtn after toggle
	 */
	public static boolean setUncapturedBtnIsEnabled() {
		ComponentData.uncapturedBtnIsEnabled = ComponentData.uncapturedBtnIsEnabled ? false : true;
		return ComponentData.uncapturedBtnIsEnabled;
	}
	
	/**
	 * @return boolean for state of uncapturedBtn 
	 */
	public static boolean getUncapturedBtnIsEnabled() {
		return ComponentData.uncapturedBtnIsEnabled;
	}
	
	//==============================================================================
	
	/**
	 * <p>Toggle for enabling and disabling capturedBtn in FileController</p>
	 * @return
	 */
	public static boolean setCapturedBtnIsEnabled() {
		ComponentData.capturedBtnIsEnabled = ComponentData.capturedBtnIsEnabled ? false : true;
		return ComponentData.capturedBtnIsEnabled;
	}
	
	/**
	 * @return boolean value for state of capturedBtn
	 */
	public static boolean getCapturedBtnIsEnabled() {
		return ComponentData.capturedBtnIsEnabled;
	}
	
	//==============================================================================
	
	/**
	 * <p>Set value of matchedUncapturedBtn in MatchedRowsController</p>
	 * @param isEnabled boolean for new value
	 * @return state after value is updated
	 */
	public static boolean setMatchedUncapturedBtnIsEnabled(boolean isEnabled) {
		ComponentData.matchedUncapturedBtnIsEnabled = isEnabled;
		return ComponentData.matchedUncapturedBtnIsEnabled;
	}
	
	/**
	 * @return Get current state of matchedUncapturedBtn
	 */
	public static boolean getMatchedUncapturedBtnIsEnabled() {
		return ComponentData.matchedUncapturedBtnIsEnabled;
	}
	
	//==============================================================================
	
	/**
	 * <p>Set value of matchedCapturedBtn in MatchedRowsController</p>
	 * @param isEnabled boolean for new value
	 * @return state after value is updated
	 */
	public static boolean setMatchedCapturedBtnIsEnabled(boolean isEnabled) {
		ComponentData.matchedCapturedBtnIsEnabled = isEnabled;
		return ComponentData.matchedCapturedBtnIsEnabled;
	}
	
	/**
	 * @return Get current state of matchedCapturedBtn
	 */
	public static boolean getMatchedCapturedBtnIsEnabled() {
		return ComponentData.matchedCapturedBtnIsEnabled;
	}
	
	//==============================================================================
	
	/**
	 * <p>Set value of duplicatedUncapturedBtn in MatchedRowsController</p>
	 * @param isEnabled boolean for new value
	 * @return state after value is updated
	 */
	public static boolean setDuplicateUncapturedBtnIsEnabled(boolean isEnabled) {
		ComponentData.duplicateUncapturedBtnIsEnabled = isEnabled;
		return ComponentData.duplicateUncapturedBtnIsEnabled;
	}
	
	/**
	 * @return Get current state of duplicatedUncapturedBtn
	 */
	public static boolean getDuplicateUncapturedBtnIsEnabled() {
		return ComponentData.duplicateUncapturedBtnIsEnabled;
	}
	
	//==============================================================================
	
	/**
	 * <p>Set value of duplicatedCapturedBtn in MatchedRowsController</p>
	 * @param isEnabled boolean for new value
	 * @return state after value is updated
	 */
	public static boolean setDuplicateCapturedBtnIsEnabled(boolean isEnabled) {
		ComponentData.duplicateCapturedBtnIsEnabled = isEnabled;
		return ComponentData.duplicateCapturedBtnIsEnabled;
	}
	
	/**
	 * @return Get current state of duplicatedCapturedBtn
	 */
	public static boolean getDuplicateCapturedBtnIsEnabled() {
		return ComponentData.duplicateCapturedBtnIsEnabled;
	}
}
