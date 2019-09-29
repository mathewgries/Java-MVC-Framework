package com.freedompay.controllers;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.freedompay.models.Model;
import com.freedompay.services.IRouteListener;
import com.freedompay.services.IRouteService;
import com.freedompay.views.View;

/**
 * <p>
 * The base class for the Controller classes. Not an abstract class.
 * Contains empty method bodies. Allows the view to call anonymously to the controller
 * <p>
 * @author MGries
 *
 */
public class Controller implements IRouteService, ActionListener, ListSelectionListener, MouseListener{
	
	//--------------INTERFACE METHODS -----------------
	
	@Override
	public void addObserver(IRouteListener obj) {}

	@Override
	public void removeObserver(IRouteListener obj) {}

	@Override
	public void notifyObservers(Object obj) {}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {}

	@Override
	public void actionPerformed(ActionEvent e) {}
	
	//------------ MVC METHODS AND HOME METHOD ---------------
	
	public void addView(View v) {}
	
	public void addModel(Model m) {}
	
	public String getInstructions() {return null;}
	
	//------------------------ BUTTONS ------------------------
	
	public JButton getPOSBtn() {return null;}
	
	public JButton getUncapturedBtn() {return null;}
	
	public JButton getCapturedBtn() {return null;}
	
	public JButton getClearCellBtn() {return null;}
	
	public JButton getDeleteFileBtn() {return null;}
	
	public JButton getInvalidRowsBtn() {return null;}

	public JButton getBackBtn() {return null;}
	
	public JButton getMatchedRowsBtn() {return null;}
	
	public JButton getMatchedUncapturedBtn() {return null;}
	
	public JButton getMatchedCapturedBtn() {return null;}
	
	public JButton getNoMatchBtn() {return null;}
	
	public JButton getDuplicateUncapturedBtn() {return null;}
	
	public JButton getDuplicateCapturedBtn() {return null;}
	
	//------------------ JPANEL COMPONENTS ---------------------
	
	public JPanel getPOSColumnList() {return null;}

	public JPanel getUncapturedHeaderList() {return null;}
	
	public JPanel getCapturedHeaderList() {return null;}
	
	//--------------------- SCROLLPANES -------------------------
	
	public JScrollPane getFileNameList() {return null;}

	public JScrollPane getInvalidRowsList() {return null;}
	
	public JScrollPane getMatchedHeadersTable() {return null;}
	
	public JScrollPane getComparisonResultsTable() {return null;}
	
	//-------------------- TEXT COMPONENTS -----------------------
	
	public JLabel getJLabel(String txt) {return null;}
	
	public JLabel getImageLabel(JPanel p) {return null;}
	
	public JTextArea getTextArea(String txt) {return null;}
}
