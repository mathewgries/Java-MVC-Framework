package com.freedompay.controllers;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.freedompay.data.ColumnData;
import com.freedompay.data.FileData;
import com.freedompay.models.FileModel;
import com.freedompay.services.IRouteListener;
import com.freedompay.views.View;

public class MatchedRowsController extends Controller{

	private View view;
	
	// ------------ OBSERVER METHODS ----------------------------
	
	private List<IRouteListener> observers = new ArrayList<IRouteListener>();
	
	// The RouteConfig is the observer
	public void addObserver(IRouteListener obj) {
		this.observers.add(obj);
	}
	
	public void removeObserver(IRouteListener obj) {
		this.observers.remove(obj);
	}
	
	@Override
	public void notifyObservers(Object obj) {
		for(IRouteListener rs : observers) {
			rs.update(obj);
		}
	}
	
	// --------- ADD THE VIEW --------------------------
	
	@Override
	public void addView(View v) {
		view = v;
	}
	
	public JLabel getJLabel(String txt) {
		return new JLabel(txt);
	}
	
	
//===========================================================================

	//DECISION PANEL DISPLAY - Action Buttons

//===========================================================================

	//-------------- ADD and REMOVE FILES BUTTONS ----------------

	private JButton matcheAuthBtn;
	private JButton matchedBatchBtn;
	private JButton noMatchBtn;
	private JButton duplicateMatchBtn;
	private JButton backBtn;
	
	public JButton getMatchedAuthBtn() {
		this.matcheAuthBtn = new JButton("Matched Uncaptured Auth");
		this.matcheAuthBtn.addActionListener(this);
		return this.matcheAuthBtn;
	}
	
	public JButton getMatchedBatchedBtn() {
		this.matchedBatchBtn = new JButton("Matched Captured");
		this.matchedBatchBtn.addActionListener(this);
		return this.matchedBatchBtn;
	}
	
	public JButton getNoMatchBtn() {
		this.noMatchBtn = new JButton("Not Matched");
		this.noMatchBtn.addActionListener(this);
		return this.noMatchBtn;
	}
	
	public JButton getDuplicateMatchBtn() {
		this.duplicateMatchBtn = new JButton("Duplicate Matches");
		this.duplicateMatchBtn.addActionListener(this);
		return this.duplicateMatchBtn;
	}
	
	public JButton getBackBtn() {
		this.backBtn = new JButton("Back To Invalid Rows");
		this.backBtn.addActionListener(this);
		return this.backBtn;
	}

	// ----------- BUTTON ACTION LISTENER ----------------------------

	public void actionPerformed(ActionEvent e) {
		JButton btn = (JButton) e.getSource();

		if(e.getSource() == backBtn) {
			this.notifyObservers("InvalidRows");
		}
	}

//===========================================================================

					//FILE LISTS

//===========================================================================

	//------------ Uploaded Files to Display --------------------------
	
	private DefaultListModel<String> fileNamesListModel = new DefaultListModel<String>();
	private JList<String> displayFileNames;
	private JScrollPane fileNameListContainer;
	
	
	// Display uploaded file names in a JList
	public JScrollPane getFileNameList() {
		this.displayFileNames = new JList<String>(this.loadFileNames());
		this.displayFileNames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.displayFileNames.addListSelectionListener(this);
		this.displayFileNames.setVisibleRowCount(6);
		this.fileNameListContainer = new JScrollPane(this.displayFileNames);
		return this.fileNameListContainer;
	}
	
	// Load existing files when navigating back to the page
	private DefaultListModel<String> loadFileNames(){
		this.fileNamesListModel.clear();
		Iterator<FileModel> i = FileData.getAllFileModels().iterator();
		while(i.hasNext()) {
			this.fileNamesListModel.addElement((String)i.next().getName());
		}
		return this.fileNamesListModel;
	}

	//------------ Matched Headers Table --------------------------

	private JTable matchedHeadersTable = null;
	private JScrollPane matchedHeadersScrollPane;
	
	public JScrollPane getMatchedHeadersTable() {
		this.matchedHeadersTable = new JTable(this.loadMatchedHeadersTableModel());
		this.matchedHeadersTable.addMouseListener(this);
		this.matchedHeadersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.matchedHeadersScrollPane = new JScrollPane(this.matchedHeadersTable);
		return this.matchedHeadersScrollPane;
	}
	
	private DefaultTableModel loadMatchedHeadersTableModel() {
		return ColumnData.getTableData();
	}
}
