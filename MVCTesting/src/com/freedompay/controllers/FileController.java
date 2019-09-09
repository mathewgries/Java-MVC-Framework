package com.freedompay.controllers;
import com.freedompay.data.*;
import com.freedompay.models.FileModel;
import com.freedompay.services.IRouteListener;
import com.freedompay.views.View;
import com.freedompay.util.FileType;
import com.freedompay.util.Validation;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Iterator;


public class FileController extends Controller  {

	private View view;
	
	
	// ------------ OBSERVER METHODS ----------------------------
	
	private List<IRouteListener> observers = new ArrayList<IRouteListener>();
	
	// The RouteConfig is the observer
	// We notify if the user clicks the Check Error Buttons
	// The view and controller will be set to InvalidLines...
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
	
	// Label for page sections
	public JLabel getJLabel(String txt) {
		return new JLabel(txt);
	}
	
//===========================================================================
	
			//DECISION PANEL DISPLAY - Action Buttons
	
//===========================================================================
	
	//-------------- ADD and REMOVE FILES BUTTONS ----------------
	
	private JButton posBtn;
	private JButton authBtn;
	private JButton batchedBtn;
	private JButton clearBtn;
	private JButton removeBtn;
	private JButton fileLineValidateBtn;
	
	//When a file is picked..., 
	
	// ...the file type is set to POS
	public JButton getPOSBtn() {
		this.posBtn = new JButton("POS File");
		this.posBtn.addActionListener(this);
		this.posBtn.setEnabled(ComponentData.getPosBtnIsEnabled());
		return this.posBtn;
	}
	
	// ...the file type is set to UNCAPTURED_AUTH
	public JButton getAuthBtn() {
		this.authBtn = new JButton("UnCaptured Auth File");
		this.authBtn.addActionListener(this);
		this.authBtn.setEnabled(ComponentData.getAuthBtnIsEnabled());
		return this.authBtn; 
	}
	
	// ...the file type is set to CAPTURED
	public JButton getBatchedBtn() {
		this.batchedBtn = new JButton("Captured File");
		this.batchedBtn.addActionListener(this);
		this.batchedBtn.setEnabled(ComponentData.getBatchedBtnIsEnabled());
		return this.batchedBtn;
	}
	
	// Clears the value from the selected cell in the Matched Columns Table
	public JButton getClearCellBtn() {
		this.clearBtn = new JButton("Clear Selected Cell");
		this.clearBtn.addActionListener(this);
		return this.clearBtn;
	}
	
	// Deletes the highlighted file in the file list display from the program
	public JButton getRemoveBtn() {
		this.removeBtn = new JButton("Delete Selected File");
		this.removeBtn.addActionListener(this);
		return this.removeBtn;
	}
	
	// Updates the current view to the Invalid Lines View.
	// Begins validation on selected columns
	public JButton getFileLineValidateBtn() {
		this.fileLineValidateBtn = new JButton("Check Files For Errors");
		this.fileLineValidateBtn.addActionListener(this);
		return this.fileLineValidateBtn;
	}
	
	// ----------- BUTTON ACTION LISTENER ----------------------------
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == posBtn) {
			if(FileData.getFileCount() < 3) {
				this.openFileChooser(FileType.POS);
			}
		}
		if(e.getSource() == authBtn) {
			if(Validation.validateFileUpload(FileType.UNCAPTURED_AUTH)) {
				this.openFileChooser(FileType.UNCAPTURED_AUTH);
			}
		}
		if(e.getSource() == batchedBtn) {
			if(Validation.validateFileUpload(FileType.CAPTURED)) {
				this.openFileChooser(FileType.CAPTURED);
			}
		}
		if(e.getSource() == clearBtn) {
			int row = this.matchedColumnsTable.getSelectedRow();
			int col = this.matchedColumnsTable.getSelectedColumn();
			if(col > 0 && row >= 0) {
				ColumnData.clearCell(row, col);
			}
		}
		if(e.getSource() == fileLineValidateBtn) {
			if(FileData.getAllFiles().size() > 1  ){
				InvalidLinesData.setInvalidLinesData();
				for(FileModel model : FileData.getAllFiles()) {
					//model.validateFileRows();
					
//					if(!model.getIsValidated()) {
//						model.validateFileRows();
//						model.setIsValidated();
//					}
				}
				this.notifyObservers("InvalidLines");
			}
		}
		if(e.getSource() == removeBtn) {
			this.deleteSelectedItem();
		}
	}
	
	// --------------- BUTTON STATE TOGGLE ---------------------
	
	
	// Sets button state. Stored in ComponentData file.
	private void setButtonEnabled(FileType type) {
		switch(type) {
			case POS:
				this.posBtn.setEnabled(ComponentData.setPosBtnIsEnabled());
				break;
			case UNCAPTURED_AUTH:
				this.authBtn.setEnabled(ComponentData.setAuthBtnIsEnabled());
				break;
			case CAPTURED:
				this.batchedBtn.setEnabled(ComponentData.setBatchedBtnIsEnabled());
				break;
		}
	}
	
//===========================================================================

		// ADDING A FILE TO THE PROGRAM

//===========================================================================
	
	private void openFileChooser(FileType type) {
		try {
			JFileChooser fc = new JFileChooser("C:\\users\\mgries\\eclipse-workspace\\testfiles\\containsnullorempty");
			int choice = fc.showDialog(this.view, "Open");
			if(choice == JFileChooser.APPROVE_OPTION) {
				File f = fc.getSelectedFile();
				if(Validation.isModelValid(f, type)) {
					FileData.saveModel(new FileModel(f, type));
					this.updateFileNameList();
					this.updateColumnSelections(type);
					this.setButtonEnabled(type);
				}
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	// Add a new file to the list
	private void updateFileNameList(){
		this.clearFileList();
		this.loadFileNames();
	}
	
//===========================================================================

		// REMOVING A FILE FROM THE PROGRAM

//===========================================================================
	
	// Remove selected file on Delete action
	private void deleteSelectedItem() {
		String sel = this.displayFileNames.getSelectedValue();
		if(sel != null) {
			FileModel model = FileData.getFile(sel);
			FileType type = model.getFileType();
			this.setButtonEnabled(type);
			this.clearFileList();
			this.clearColumns(type);
			FileData.deleteModel(model);
			this.loadFileNames();
		}
	}
	
	private void clearFileList() {
		this.displayFileNames.clearSelection();
		this.fileNamesListModel.clear();
	}
	
	private void clearColumns(FileType type) {
		switch(type) {
			case POS:
				this.matchedColumnsTable.clearSelection();
				ColumnData.getTableData().setRowCount(0);
				ColumnData.clearMatchedIndexes(type);
				InvalidLinesData.clearAllSelectedLinesColumns();
				FileData.setIsPOSLoadedFlag();
				this.selectedRow = -1;
				break;
			case UNCAPTURED_AUTH:
				this.authColumnList.clearSelection();
				this.authColumnModel.clear();
				ColumnData.clearMatchedCells(type);
				ColumnData.clearMatchedIndexes(type);
				InvalidLinesData.clearAuthSelectedColumns();
				break;
			case CAPTURED:
				this.batchedColumnList.clearSelection();
				this.batchedColumnModel.clear();
				ColumnData.clearMatchedCells(type);
				ColumnData.clearMatchedIndexes(type);
				InvalidLinesData.clearBatchedSelectedColumns();
				break;
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
		Iterator<FileModel> i = FileData.getAllFiles().iterator();
		while(i.hasNext()) {
			this.fileNamesListModel.addElement((String)i.next().getName());
		}
		return this.fileNamesListModel;
	}
	
//===========================================================================

	// COLUMN SELECTOR - Display Uncaptured-auth and captured file headers

//===========================================================================
	
	// -------------- UNCAPTURED AUTHORIZATION FILES ----------------
	
	private DefaultListModel<String> authColumnModel = new DefaultListModel<String>();
	private JList<String> authColumnList;
	private JScrollPane authColumnListContainer;
	
	public JPanel getAuthColumnList() {
		JPanel p = new JPanel();
		JLabel l = new JLabel("Uncaptured Auth Columns");
		
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		// Should take a load method instead of directly assigning the model
		this.authColumnList = new JList<String>(this.loadAuthColumns());
		this.authColumnList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.authColumnList.addListSelectionListener(this);
		this.authColumnList.setVisibleRowCount(6);
		this.authColumnListContainer = new JScrollPane(this.authColumnList);
		
		Border bevel = BorderFactory.createLoweredBevelBorder();
		this.authColumnListContainer.setBorder(bevel);
		
		l.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.authColumnListContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		p.add(l);
		p.add(this.authColumnListContainer);
		
		return p;
	}
	
	private DefaultListModel<String> loadAuthColumns(){
		if(FileData.getFile(FileType.UNCAPTURED_AUTH) != null) {
			Iterator<String> i = FileData.getFile(FileType.UNCAPTURED_AUTH).getHeaderNames().iterator();
			while(i.hasNext()) {
				this.authColumnModel.addElement(i.next());
			}
		}
		return this.authColumnModel;
	}
	
	// --------------- BATCHED TRANS COLUMN LIST -------------------
	
	private DefaultListModel<String> batchedColumnModel = new DefaultListModel<String>();
	private JList<String> batchedColumnList;
	private JScrollPane batchedColumnListContainer;
	
	public JPanel getBatchedColumnList() {
		JPanel p = new JPanel();
		JLabel l = new JLabel("Batched Trans Columns");
		
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		
		this.batchedColumnList = new JList<String>(this.loadBatchedColumns());
		this.batchedColumnList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.batchedColumnList.addListSelectionListener(this);
		this.batchedColumnList.setVisibleRowCount(6);
		this.batchedColumnListContainer = new JScrollPane(this.batchedColumnList);

		p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		Border bevel = BorderFactory.createLoweredBevelBorder();
		this.batchedColumnListContainer.setBorder(bevel);
		
		l.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.batchedColumnListContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		p.add(Box.createRigidArea(new Dimension(10, 0)));
		p.add(l);
		p.add(Box.createRigidArea(new Dimension(10, 0)));
		p.add(this.batchedColumnListContainer);
		
		return p;
	}
	
	private DefaultListModel<String> loadBatchedColumns(){
		if(FileData.getFile(FileType.CAPTURED) != null) {
			Iterator<String> i = FileData.getFile(FileType.CAPTURED).getHeaderNames().iterator();
			while(i.hasNext()) {
				this.batchedColumnModel.addElement(i.next());
			}
		}
		return this.batchedColumnModel;
	}
	
	// --------------- FILE COLUMN LIST CONTROLS -------------------
	
	private void updateColumnSelections(FileType type) {
		switch(type) {
			case POS:
				this.setPOSColumnsToTable();
				ColumnData.setPosHeadersSize(FileData.getFile(type).getColumnCount());
				ColumnData.setMatchedIndexes();
				break;
			case UNCAPTURED_AUTH:
				this.loadAuthColumns();
				if(FileData.getIsPOSLoadedFlag()) {
					ColumnData.setMatchedOnAuth();
				}
				break;
			case CAPTURED:
				this.loadBatchedColumns();
				if(FileData.getIsPOSLoadedFlag()) {
					ColumnData.setMatchedOnBatched();
				}
				break;
		}
	}
	
	public void valueChanged(ListSelectionEvent le) {
		if(!le.getValueIsAdjusting() && this.selectedRow > -1) {
			@SuppressWarnings("unchecked")
			JList<String> list = (JList<String>) le.getSource();
			String value = "";
			int index = -1;
			
			if(le.getSource() == authColumnList) {
				value = (String) list.getSelectedValue();
				index = (int) list.getSelectedIndex();
				this.updateMatchedColumnModel(value, FileType.UNCAPTURED_AUTH, index);	
			}
			if(le.getSource() == batchedColumnList) {
				value = (String) list.getSelectedValue();
				index = (int) list.getSelectedIndex();
				this.updateMatchedColumnModel(value, FileType.CAPTURED, index);
			}
		}
	}
		
	@Override
	public void mousePressed(MouseEvent e) {
		JTable table = (JTable) e.getSource();
		this.selectedRow = table.getSelectedRow();
	}

//===========================================================================

	// SELECTED COLUMNS LIST

//===========================================================================

	private JTable matchedColumnsTable = null;
	private JScrollPane matchedColumnsScrollPane;
	private int selectedRow = -1;
	
	public JScrollPane getMatchedColumnsTable() {
		this.matchedColumnsTable = new JTable(this.loadMatchedColumnTableModel());
		this.matchedColumnsTable.addMouseListener(this);
		this.matchedColumnsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.matchedColumnsScrollPane = new JScrollPane(this.matchedColumnsTable);
		return this.matchedColumnsScrollPane;
	}
	
	private DefaultTableModel loadMatchedColumnTableModel() {
		if(!ColumnData.getPOSHeadersSetFlag()) {
			this.setHeadersToMatchTable();
		}
		return ColumnData.getTableData();
	}
	
	private void setHeadersToMatchTable() {
		for(String header : ColumnData.getMatchedTableHeaders()) {
			ColumnData.getTableData().addColumn(header);
		}
		ColumnData.setPOSHeadersSetFlag();
	}
	
	private void setPOSColumnsToTable() {
		this.selectedRow = -1;
		ColumnData.setPOSColumnsToTableData(FileData.getFile(FileType.POS).getHeaderNames());
	}
	
	private void updateMatchedColumnModel(String txt, FileType type, int colIndex) {
		int column = -1;
		switch(type) {
		case UNCAPTURED_AUTH:
			column = 1;
			break;
		case CAPTURED:
			column = 2;
			break;
		default:
			System.out.println("Something went wrong");
			break;
		}
		ColumnData.updateTableData(txt, selectedRow, column);
		ColumnData.updateMatchedIndexes(type, selectedRow, colIndex);
	}
}
