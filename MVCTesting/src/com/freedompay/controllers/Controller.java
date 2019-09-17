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

public class Controller implements IRouteService, ActionListener, ListSelectionListener, MouseListener{
	
	@Override
	public void addObserver(IRouteListener obj) {}

	@Override
	public void removeObserver(IRouteListener obj) {}

	@Override
	public void notifyObservers(Object obj) {}
	
	public void addView(View v) {}
	
	public void addModel(Model m) {}
	
	public void addFileInput(JPanel p) {}
	
	public String getInstructions() {return null;}
	
	public JButton getPOSBtn() {return null;}
	
	public JButton getAuthBtn() {return null;}
	
	public JButton getBatchedBtn() {return null;}
	
	public JButton getClearCellBtn() {return null;}
	
	public JButton getDeleteFileBtn() {return null;}
	
	public JButton getFileLineValidateBtn() {return null;}
	
	public JButton getBackBtn() {return null;}
	
	public JButton getCompareBtn() {return null;}
	
	public JLabel getJLabel(String txt) {return null;}
	
	public JLabel getImageLabel(JPanel p) {return null;}
	
	public JTextArea getTextArea(String txt) {return null;}
	
	public JButton getButton(String txt) {return null;}
	
	public JScrollPane getFileNameList() {return null;}

	public JScrollPane getInvalidLineItemsList() {return null;}
	
	public JPanel getPOSColumnList() {return null;}

	public JPanel getAuthHeaderList() {return null;}
	
	public JPanel getBatchedHeaderList() {return null;}
	
	public JScrollPane getMatchedHeadersTable() {return null;}

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

}
