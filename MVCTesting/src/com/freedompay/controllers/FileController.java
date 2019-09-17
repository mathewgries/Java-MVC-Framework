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

//TODO: The Files are loading and deleting.
// Set up the match table and Auth and Batched header lists


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
	private JButton deleteFileBtn;
	private JButton clearBtn;
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
	
	// Deletes the highlighted file in the file list display from the program
	public JButton getDeleteFileBtn() {
		this.deleteFileBtn = new JButton("Delete Selected File");
		this.deleteFileBtn.addActionListener(this);
		return this.deleteFileBtn;
	}
	
	// Clears the value from the selected cell in the Matched Columns Table
	public JButton getClearCellBtn() {
		this.clearBtn = new JButton("Clear Selected Cell");
		this.clearBtn.addActionListener(this);
		return this.clearBtn;
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
			this.openFileChooser(FileType.POS);
		}
		if(e.getSource() == authBtn) {
			this.openFileChooser(FileType.UNCAPTURED_AUTH);
		}
		if(e.getSource() == batchedBtn) {
			this.openFileChooser(FileType.CAPTURED);
		}
		if(e.getSource() == deleteFileBtn) {
			this.deleteSelectedFile();
		}
		if(e.getSource() == clearBtn) {
			int row = this.matchedHeadersTable.getSelectedRow();
			int col = this.matchedHeadersTable.getSelectedColumn();
			if(col > 0 && row >= 0) {
				ColumnData.clearCell(row, col);
			}
		}
		if(e.getSource() == fileLineValidateBtn) {
			if(FileData.getAllFileModels().size() > 1 && FileData.getIsPOSLoadedFlag()){
				List<FileModel> models = FileData.getAllFileModels();
				for(FileModel model : models) {
					if(model.getFileType() == FileType.POS) {
						model.getFileContents().setSelectedColumnIndexes(ColumnData.getTableData());
					}else {
						model.getFileContents().setSelectedColumnIndexes();
					}
				}
				this.notifyObservers("InvalidLines");
			}
		}
	}
	
	private boolean confirmValidationIsReady() {
		boolean authLoaded = ComponentData.getAuthBtnIsEnabled();
		boolean batchedLoaded = ComponentData.getBatchedBtnIsEnabled();
		if((authLoaded || batchedLoaded)) {
			for(FileModel model : FileData.getAllFileModels()) {
				if(model.getFileType() == FileType.POS) {
					model.getFileContents().setSelectedColumnIndexes(ColumnData.getTableData());
				}else {
					model.getFileContents().setSelectedColumnIndexes();
				}
			}
		}
		// TODO: VERIFY THAT THE COLUMNS FOR THE TWO NON POS FILES ARE NOT ALL NULL
		return false;
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
					this.updateColumnSelections(type);
					this.setButtonEnabled(type);
					this.loadFileNames();
				}
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	// Remove selected file on Delete action
	private void deleteSelectedFile() {
		String sel = this.displayFileNames.getSelectedValue();
		if(sel != null) {
			FileModel model = FileData.getFileModel(sel);
			FileType type = model.getFileType();
			FileData.deleteModel(type);
			this.setButtonEnabled(type);
			this.loadFileNames();
			ColumnData.clearMatchedCells(type);
			ColumnData.clearMatchedIndexes(type);
		}
	}
	
	// --------------- FILE COLUMN LIST CONTROLS -------------------
	
	private void updateColumnSelections(FileType type) {
		switch(type) {
			case POS:
				this.setPOSHeadersToTable();
				ColumnData.setPosHeadersSize(FileData.getFileModel(type).getFileContents().getColumnCount());
				ColumnData.setMatchedIndexes();
				break;
			case UNCAPTURED_AUTH:
				this.loadAuthHeaders();
				if(FileData.getIsPOSLoadedFlag()) {
					ColumnData.setMatchedOnAuth();
				}
				break;
			case CAPTURED:
				this.loadBatchedHeaders();
				if(FileData.getIsPOSLoadedFlag()) {
					
				}
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
		this.fileNamesListModel.clear();
		Iterator<FileModel> i = FileData.getAllFileModels().iterator();
		while(i.hasNext()) {
			this.fileNamesListModel.addElement((String)i.next().getName());
		}
		return this.fileNamesListModel;
	}
	
//===========================================================================

	// COLUMN SELECTOR - Display Uncaptured-auth and captured file headers

//===========================================================================
	
	// -------------- UNCAPTURED AUTHORIZATION FILES ----------------
	
	private DefaultListModel<String> authHeaderModel = new DefaultListModel<String>();
	private JList<String> authHeaderList;
	private JScrollPane authHeaderListContainer;
	
	public JPanel getAuthHeaderList() {
		JPanel p = new JPanel();
		JLabel l = new JLabel("Uncaptured Auth Headers");
		
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		// Should take a load method instead of directly assigning the model
		this.authHeaderList = new JList<String>(this.loadAuthHeaders());
		this.authHeaderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.authHeaderList.addListSelectionListener(this);
		this.authHeaderList.setVisibleRowCount(6);
		this.authHeaderListContainer = new JScrollPane(this.authHeaderList);
		
		Border bevel = BorderFactory.createLoweredBevelBorder();
		this.authHeaderListContainer.setBorder(bevel);
		
		l.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.authHeaderListContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		p.add(l);
		p.add(this.authHeaderListContainer);
		
		return p;
	}
	
	private DefaultListModel<String> loadAuthHeaders(){
		if(FileData.getFileModel(FileType.UNCAPTURED_AUTH) != null) {
			FileModel model = FileData.getFileModel(FileType.UNCAPTURED_AUTH);
			Iterator<String> headers = model.getFileContents().getHeaderNames().iterator();
			while(headers.hasNext()) {
				this.authHeaderModel.addElement(headers.next());
			}
		}else {
			this.authHeaderModel.clear();
		}
		return this.authHeaderModel;
	}
	
	// --------------- BATCHED TRANS COLUMN LIST -------------------
	
	private DefaultListModel<String> batchedHeaderModel = new DefaultListModel<String>();
	private JList<String> batchedHeaderList;
	private JScrollPane batchedHeaderListContainer;
	
	public JPanel getBatchedHeaderList() {
		JPanel p = new JPanel();
		JLabel l = new JLabel("Batched Trans Headers");
		
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		
		this.batchedHeaderList = new JList<String>(this.loadBatchedHeaders());
		this.batchedHeaderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.batchedHeaderList.addListSelectionListener(this);
		this.batchedHeaderList.setVisibleRowCount(6);
		this.batchedHeaderListContainer = new JScrollPane(this.batchedHeaderList);

		p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		Border bevel = BorderFactory.createLoweredBevelBorder();
		this.batchedHeaderListContainer.setBorder(bevel);
		
		l.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.batchedHeaderListContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		p.add(Box.createRigidArea(new Dimension(10, 0)));
		p.add(l);
		p.add(Box.createRigidArea(new Dimension(10, 0)));
		p.add(this.batchedHeaderListContainer);
		
		return p;
	}
	
	private DefaultListModel<String> loadBatchedHeaders(){
		if(FileData.getFileModel(FileType.CAPTURED) != null) {
			FileModel model = FileData.getFileModel(FileType.CAPTURED);
			Iterator<String> headers = model.getFileContents().getHeaderNames().iterator();
			while(headers.hasNext()) {
				this.batchedHeaderModel.addElement(headers.next());
			}
		}else {
			this.batchedHeaderModel.clear();
		}
		return this.batchedHeaderModel;
	}

//===========================================================================

	// SELECTED COLUMNS LIST

//===========================================================================

	private JTable matchedHeadersTable = null;
	private JScrollPane matchedHeadersScrollPane;
	private int selectedRow = -1;
	
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

	private void setPOSHeadersToTable() {
		this.selectedRow = -1;
		if(FileData.getFileModel(FileType.POS) != null) {
			FileModel posModel = FileData.getFileModel(FileType.POS);
			ColumnData.setPOSHeadersToTableData(posModel.getFileContents().getHeaderNames());
		}else {
			ColumnData.getTableData().setRowCount(0);
		}
		
	}
	
	private void updateMatchedHeaderModel(String txt, FileType type, int colIndex) {
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
	
	@Override
	public void mousePressed(MouseEvent e) {
		JTable table = (JTable) e.getSource();
		this.selectedRow = table.getSelectedRow();
	}
	
	public void valueChanged(ListSelectionEvent le) {
		if(!le.getValueIsAdjusting() && this.selectedRow > -1) {
			@SuppressWarnings("unchecked")
			JList<String> list = (JList<String>) le.getSource();
			String value = "";
			int index = -1;
			
			if(le.getSource() == authHeaderList) {
				value = (String) list.getSelectedValue();
				index = (int) list.getSelectedIndex();
				this.updateMatchedHeaderModel(value, FileType.UNCAPTURED_AUTH, index);	
			}
			if(le.getSource() == batchedHeaderList) {
				value = (String) list.getSelectedValue();
				index = (int) list.getSelectedIndex();
				this.updateMatchedHeaderModel(value, FileType.CAPTURED, index);
			}
		}
	}
}
