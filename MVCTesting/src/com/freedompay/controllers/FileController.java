package com.freedompay.controllers;
import com.freedompay.data.*;
import com.freedompay.models.FileModel;
import com.freedompay.services.IRouteListener;
import com.freedompay.views.View;
import com.freedompay.util.FileType;
import com.freedompay.util.UserMessages;
import com.freedompay.util.Validation;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
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

/**
 * <p>
 * The controller for the FileView class.
 * </p>
 * @author MGries
 *
 */
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
	
	/**
	 * <p>
	 * Adds a reference of the view. Not always needed
	 * unless you are calling on the view for some reason.
	 * In this case, we are using it to set the parent component
	 * of the FileOpenDialog box.
	 * </p>
	 */
	@Override
	public void addView(View v) {
		view = v;
	}
	
	// Label for page sections
	public JLabel getJLabel(String txt) {
		return new JLabel(txt);
	}
	
//==========================================================================================================
	
							//DECISION PANEL DISPLAY - Action Buttons
	
//==========================================================================================================
	
	//-------------- ADD and REMOVE FILES BUTTONS ----------------
	
	private JButton posBtn;
	private JButton uncapturedBtn;
	private JButton capturedBtn;
	private JButton deleteFileBtn;
	private JButton clearBtn;
	private JButton matchedRowsBtn;
	
	//When a file is picked..., 
	
	/**
	 * <p>Load POS add button to View</p>
	 */
	public JButton getPOSBtn() {
		this.posBtn = new JButton("POS File");
		this.posBtn.addActionListener(this);
		this.posBtn.setEnabled(ComponentData.getPosBtnIsEnabled());
		return this.posBtn;
	}
	
	/**
	 * <p>Load uncapturedAuth button to view</p>
	 */
	public JButton getUncapturedBtn() {
		this.uncapturedBtn = new JButton("Uncaptured File");
		this.uncapturedBtn.addActionListener(this);
		this.uncapturedBtn.setEnabled(ComponentData.getUncapturedBtnIsEnabled());
		return this.uncapturedBtn; 
	}
	
	/**
	 * <p>Load Captured button to View</p>
	 */
	public JButton getCapturedBtn() {
		this.capturedBtn = new JButton("Captured File");
		this.capturedBtn.addActionListener(this);
		this.capturedBtn.setEnabled(ComponentData.getCapturedBtnIsEnabled());
		return this.capturedBtn;
	}
	
	/**
	 * <p>Load file delete button to View</p>
	 */
	public JButton getDeleteFileBtn() {
		this.deleteFileBtn = new JButton("Delete Selected File");
		this.deleteFileBtn.addActionListener(this);
		return this.deleteFileBtn;
	}
	
	/**
	 * <p>Load Clear Cell button to View</p>
	 */
	public JButton getClearCellBtn() {
		this.clearBtn = new JButton("Clear Selected Cell");
		this.clearBtn.addActionListener(this);
		return this.clearBtn;
	}
	
	/**
	 * <p>Load File Validation Button to View</p>
	 */
	public JButton getMatchedRowsBtn() {
		this.matchedRowsBtn = new JButton("Matched Rows");
		this.matchedRowsBtn.addActionListener(this);
		return this.matchedRowsBtn;
	}
	
	// ----------- BUTTON ACTION LISTENER ----------------------------
	
	/**
	 * <p>
	 * Event Listener for the button clicks
	 * Mostly we are calling the FileChooser, and setting
	 * the file type from the button used to open it.
	 * We are also clearing cells from the match table, as well
	 * as changing the route for matched rows view.
	 * </p>
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == posBtn) {
			this.openFileChooser(FileType.POS);
		}
		
		if(e.getSource() == uncapturedBtn) {
			this.openFileChooser(FileType.UNCAPTURED_AUTH);
		}
		
		if(e.getSource() == capturedBtn) {
			this.openFileChooser(FileType.CAPTURED);
		}
		
		if(e.getSource() == deleteFileBtn) {
			this.deleteSelectedFile();
		}
		
		if(e.getSource() == clearBtn) {
			int row = this.matchedHeadersTable.getSelectedRow();
			int col = this.matchedHeadersTable.getSelectedColumn();
			String value = (String) this.matchedHeadersTable.getValueAt(row, col);
			FileType type = null;
			boolean posUpdate = true;
			if(col == 1) {type = FileType.UNCAPTURED_AUTH;}
			if(col == 2) {type = FileType.CAPTURED;}
			if(col > 0 && row >= 0) {
				ColumnData.clearCell(row, col);
				FileData.getFileModel(type).getFileContents().updateSelectedHeaders(null, value);
				for(int i = 1; i < 3; i++) {
					if(ColumnData.getTableData().getValueAt(this.selectedRow, i) != null) {
						posUpdate = false;
					}
				}
				if(posUpdate) {
					FileData.getFileModel(FileType.POS).getFileContents().updateSelectedHeaders(null, (String)ColumnData.getTableData().getValueAt(this.selectedRow, 0));
				}
			}
		}
		
		if(e.getSource() == matchedRowsBtn) {
			if(!FileData.getIsPOSLoadedFlag()) {
				UserMessages.noPOSFilePresent();
			}else if(FileData.getFileCount() < 2) {
				UserMessages.fileCountTooLow();
			}else if(!validateHeaderCount(FileType.UNCAPTURED_AUTH) && !validateHeaderCount(FileType.CAPTURED)) {
				UserMessages.invalidColumnSelectionCount();
			}else{
				this.notifyObservers("MatchedRows");	
			}
		}
	}
	
	/**
	 * <p>
	 * Validating that enough header matches have been selected
	 * from at least one of the uploaded files.
	 * </p>
	 * @param type The file type to check against
	 * @return boolean
	 */
	private boolean validateHeaderCount(FileType type) {
		int count = 0;
		int col = 0;
		if(type == FileType.UNCAPTURED_AUTH) {
			col = 1;
		}
		if(type == FileType.CAPTURED) {
			col = 2;
		}
		
		for(int i = 0; i < ColumnData.getTableData().getRowCount(); i++) {
			if(ColumnData.getTableData().getValueAt(i, col) != null) {
				count++;
			}
		}
		
		if(count >= 3) {
			return true;
		}else {
			return false;
		}
	}

	
	// --------------- BUTTON STATE TOGGLE ---------------------
	
	
	/**
	 * <p>Sets the state of the file load buttons on upload and delete</p>
	 * @param type The file type being uploaded or deleted
	 */
	private void setButtonEnabled(FileType type) {
		switch(type) {
			case POS:
				this.posBtn.setEnabled(ComponentData.setPosBtnIsEnabled());
				break;
			case UNCAPTURED_AUTH:
				this.uncapturedBtn.setEnabled(ComponentData.setUncapturedBtnIsEnabled());
				break;
			case CAPTURED:
				this.capturedBtn.setEnabled(ComponentData.setCapturedBtnIsEnabled());
				break;
		}
	}
	
//==========================================================================================================

								// ADDING A FILE TO THE PROGRAM

//==========================================================================================================
	
	/**
	 * <p>
	 * Opens the file dialog window and lets the user select a file.
	 * Saves the FileModel, and sets the JLists with the appropriate data
	 * for headers. Disables appropriate file add button.
	 * </p>
	 * @param type
	 */
	private void openFileChooser(FileType type) {
		try {
			JFileChooser fc = new JFileChooser("C:\\");
			int choice = fc.showDialog(this.view, "Add File");
			if(choice == JFileChooser.APPROVE_OPTION) {
				FileModel model = new FileModel(fc.getSelectedFile(), type);
				if(Validation.isModelValid(model)) {
					FileData.saveFileModel(model);
					this.updateColumnSelections(type);
					this.setButtonEnabled(type);
					this.loadFileNames();
				}
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * <p>
	 * Delete the selected file. Reset the appropriate JList
	 * and MatchedTable fields. Enables approprate file add button
	 * </p>
	 */
	private void deleteSelectedFile() {
		String sel = this.displayFileNames.getSelectedValue();
		if(sel != null) {
			FileModel model = FileData.getFileModel(sel);
			FileType type = model.getFileType();
			FileData.deleteFileModel(type);
			this.setButtonEnabled(type);
			this.loadFileNames();
			this.updateColumnSelections(type);
			ColumnData.clearMatchedCells(type);
		}
	}
	
	// --------------------------- FILE COLUMN LIST CONTROLS ---------------------------------
	
	// When a file is loaded or deleted, call the proper method that
	// sets the Match Table for POS, or list for auth and captured.
	// Adds or clears depending on upload or delete.
	private void updateColumnSelections(FileType type) {
		switch(type) {
			case POS:
				this.setPOSHeadersToTable();
				break;
			case UNCAPTURED_AUTH:
				this.loadUncapturedHeaders();
				break;
			case CAPTURED:
				this.loadCapturedHeaders();
				break;
		}
	}
	
//==========================================================================================================
	
											//FILE LISTS

//==========================================================================================================
	
	//---------------------------- Uploaded Files to Display --------------------------------------
	
	private DefaultListModel<String> fileNamesListModel = new DefaultListModel<String>();
	private JList<String> displayFileNames;
	private JScrollPane fileNameListContainer;
	
	
	/**
	 * <p>
	 * This display and select list for uploaded file names
	 * </p>
	 */
	public JScrollPane getFileNameList() {
		this.displayFileNames = new JList<String>(this.loadFileNames());
		this.displayFileNames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.displayFileNames.addListSelectionListener(this);
		this.displayFileNames.setVisibleRowCount(6);
		this.fileNameListContainer = new JScrollPane(this.displayFileNames);
		return this.fileNameListContainer;
	}
		
	/**
	 * <p>
	 * Loads the list of file names from the stored FileModels in 
	 * FileData static class file
	 * </p>
	 * @return
	 */
	private DefaultListModel<String> loadFileNames(){
		this.fileNamesListModel.clear();
		for(FileModel model : FileData.getAllFileModels()) {
			this.fileNamesListModel.addElement(model.getName());
		}
		return this.fileNamesListModel;
	}
	
//==========================================================================================================

			// COLUMN SELECTOR - Display Uncaptured-auth and captured file headers

//==========================================================================================================
	
	// ---------------------------- UNCAPTURED AUTHORIZATION FILES ----------------------------
	
	private DefaultListModel<String> uncapturedHeaderModel = new DefaultListModel<String>();
	private JList<String> uncapturedHeaderList;
	private JScrollPane uncapturedHeaderListContainer;
	
	/**
	 * <p>
	 * Panel that holds the JList for the uncapturedAuth headers.
	 * Loaded to FileView
	 * </p>
	 */
	public JPanel getUncapturedHeaderList() {
		JPanel p = new JPanel();
		JLabel l = new JLabel("Uncaptured Auth Headers");
		
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		// Should take a load method instead of directly assigning the model
		this.uncapturedHeaderList = new JList<String>(this.loadUncapturedHeaders());
		this.uncapturedHeaderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.uncapturedHeaderList.addListSelectionListener(this);
		this.uncapturedHeaderList.setVisibleRowCount(6);
		this.uncapturedHeaderListContainer = new JScrollPane(this.uncapturedHeaderList);
		
		Border bevel = BorderFactory.createLoweredBevelBorder();
		this.uncapturedHeaderListContainer.setBorder(bevel);
		
		l.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.uncapturedHeaderListContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		p.add(l);
		p.add(this.uncapturedHeaderListContainer);
		
		return p;
	}
	
	/**
	 * <p>
	 * When adding file, adds file headers to JList selection scrollpane.
	 * When removing file, clears the JList selection scrollpane
	 * </p>
	 * @return DefaultListModel for uncaptureAuth header list
	 */
	private DefaultListModel<String> loadUncapturedHeaders(){
		if(FileData.getFileModel(FileType.UNCAPTURED_AUTH) != null) {
			for(String header : FileData.getFileModel(FileType.UNCAPTURED_AUTH).getFileContents().getHeaderNames()) {
				this.uncapturedHeaderModel.addElement(header);
			}
		}else {
			this.uncapturedHeaderModel.clear();
		}
		return this.uncapturedHeaderModel;
	}
	
	// --------------------------- captured TRANS COLUMN LIST ---------------------------------------------------
	
	private DefaultListModel<String> capturedHeaderModel = new DefaultListModel<String>();
	private JList<String> capturedHeaderList;
	private JScrollPane capturedHeaderListContainer;
	
	/**
	 * <p>
	 * Panel that holds the JList for the captured headers.
	 * Loaded to FileView
	 * </p>
	 */
	public JPanel getCapturedHeaderList() {
		JPanel p = new JPanel();
		JLabel l = new JLabel("Captured Trans Headers");
		
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		
		this.capturedHeaderList = new JList<String>(this.loadCapturedHeaders());
		this.capturedHeaderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.capturedHeaderList.addListSelectionListener(this);
		this.capturedHeaderList.setVisibleRowCount(6);
		this.capturedHeaderListContainer = new JScrollPane(this.capturedHeaderList);

		p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		Border bevel = BorderFactory.createLoweredBevelBorder();
		this.capturedHeaderListContainer.setBorder(bevel);
		
		l.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.capturedHeaderListContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		p.add(Box.createRigidArea(new Dimension(10, 0)));
		p.add(l);
		p.add(Box.createRigidArea(new Dimension(10, 0)));
		p.add(this.capturedHeaderListContainer);
		
		return p;
	}
	
	/**
	 * <p>
	 * When adding file, adds file headers to JList selection scrollpane.
	 * When removing file, clears the JList selection scrollpane
	 * </p>
	 * @return DefaultListModel for captured header list
	 */
	private DefaultListModel<String> loadCapturedHeaders(){
		if(FileData.getFileModel(FileType.CAPTURED) != null) {
			for(String header :  FileData.getFileModel(FileType.CAPTURED).getFileContents().getHeaderNames()) {
				this.capturedHeaderModel.addElement(header);
			}
		}else {
			this.capturedHeaderModel.clear();
		}
		return this.capturedHeaderModel;
	}

//==========================================================================================================

								// SELECTED COLUMNS LIST

//==========================================================================================================

	private JTable matchedHeadersTable = null;
	private JScrollPane matchedHeadersScrollPane;
	private int selectedRow = -1;
	
	/**
	 * <p>
	 * Loads the Matched Headers Table to the View.
	 * <p>
	 */
	public JScrollPane getMatchedHeadersTable() {
		this.matchedHeadersTable = new JTable(this.loadMatchedHeadersTableModel()) {
			private static final long serialVersionUID = 7245257179142963426L;
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		this.matchedHeadersTable.addMouseListener(this);
		this.matchedHeadersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.matchedHeadersScrollPane = new JScrollPane(this.matchedHeadersTable);
		return this.matchedHeadersScrollPane;
	}
	
	/**
	 * <p>
	 * Loads the DefaultTableModel for the Matched Headers table
	 * from ColumnData static class file.
	 * </p>
	 * @return
	 */
	private DefaultTableModel loadMatchedHeadersTableModel() {
		return ColumnData.getTableData();
	}
	
	/**
	 * <p>
	 * Load the headers from the POS table to the Matched Header Table
	 * </p>
	 */
	private void setPOSHeadersToTable() {
		this.selectedRow = -1;
		if(FileData.getFileModel(FileType.POS) != null) {
			ColumnData.setPOSHeadersToTableData(FileData.getFileModel(FileType.POS).getFileContents().getHeaderNames());
		}else {
			ColumnData.getTableData().setRowCount(0);
		}
	}
	
	/**
	 * <p>
	 * Listens for selected rows in the Matched Table Headers
	 * and set the selected index to a variable so the program
	 * knows which row in the table is being updated.
	 * </p>
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		JTable table = (JTable) e.getSource();
		this.selectedRow = table.getSelectedRow();
	}
	
	/**
	 * <p>
	 * List selection listener for when unAuthed or captured List items
	 * are selected. Controls whether an items is added to the Matched Header Table,
	 * when an item is removed from the matched header table. This will also
	 * update the selected header list in the selected file model.
	 * </p>
	 */
	public void valueChanged(ListSelectionEvent le) {
		
		if(!le.getValueIsAdjusting() && this.selectedRow > -1) {
			@SuppressWarnings("unchecked")
			JList<String> list = (JList<String>) le.getSource();
			FileType type = null;
			int colPos = -1;
			String value = "";
			String oldValue = null;
			String posValue = (String)ColumnData.getTableData().getValueAt(this.selectedRow, 0);
			int index = list.getSelectedIndex();
			
			// Do nothing if list is not captured or unAuthed list (avoid null pointer on delete file)
			if(le.getSource() == displayFileNames || index < 0) {return;}

			if(le.getSource() == uncapturedHeaderList) {
				type = FileType.UNCAPTURED_AUTH;
				colPos = 1;
			}
			if(le.getSource() == capturedHeaderList) {
				type = FileType.CAPTURED;
				colPos = 2;
			}
			
			// Get new value and old value if it exists. Controls whether we are just adding
			// or replacing a value in the selected list
			value = (String) list.getSelectedValue();
			// Value to replace, or null if no value currently exists at indexes
			oldValue = ColumnData.updateTableData(value, selectedRow, colPos);
			FileData.getFileModel(type).getFileContents().updateSelectedHeaders(value, oldValue);
			FileData.getFileModel(FileType.POS).getFileContents().updateSelectedHeaders(posValue, null);
		}
	}
}
