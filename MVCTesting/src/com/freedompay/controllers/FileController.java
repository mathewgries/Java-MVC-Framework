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
	private JButton authBtn;
	private JButton batchedBtn;
	private JButton deleteFileBtn;
	private JButton clearBtn;
	private JButton invalidRowsBtn;
	
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
	public JButton getAuthBtn() {
		this.authBtn = new JButton("UnCaptured Auth File");
		this.authBtn.addActionListener(this);
		this.authBtn.setEnabled(ComponentData.getAuthBtnIsEnabled());
		return this.authBtn; 
	}
	
	/**
	 * <p>Load Captured button to View</p>
	 */
	public JButton getBatchedBtn() {
		this.batchedBtn = new JButton("Captured File");
		this.batchedBtn.addActionListener(this);
		this.batchedBtn.setEnabled(ComponentData.getBatchedBtnIsEnabled());
		return this.batchedBtn;
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
	public JButton getInvalidRowsBtn() {
		this.invalidRowsBtn = new JButton("Invalid Rows");
		this.invalidRowsBtn.addActionListener(this);
		return this.invalidRowsBtn;
	}
	
	// ----------- BUTTON ACTION LISTENER ----------------------------
	
	/**
	 * <p>Event Listener for the button clicks</p>
	 */
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
		if(e.getSource() == invalidRowsBtn) {
			this.notifyObservers("InvalidRows");
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
				this.authBtn.setEnabled(ComponentData.setAuthBtnIsEnabled());
				break;
			case CAPTURED:
				this.batchedBtn.setEnabled(ComponentData.setBatchedBtnIsEnabled());
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
			JFileChooser fc = new JFileChooser("C:\\users\\mgries\\eclipse-workspace\\testfiles\\containsnullorempty");
			int choice = fc.showDialog(this.view, "Add File");
			if(choice == JFileChooser.APPROVE_OPTION) {
				File f = fc.getSelectedFile();
				if(Validation.isModelValid(f, type)) {
					FileData.saveFileModel(new FileModel(f, type));
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
	
	private void updateColumnSelections(FileType type) {
		switch(type) {
			case POS:
				this.setPOSHeadersToTable();
				break;
			case UNCAPTURED_AUTH:
				this.loadAuthHeaders();
				break;
			case CAPTURED:
				this.loadBatchedHeaders();
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
		Iterator<FileModel> i = FileData.getAllFileModels().iterator();
		while(i.hasNext()) {
			this.fileNamesListModel.addElement((String)i.next().getName());
		}
		return this.fileNamesListModel;
	}
	
//==========================================================================================================

			// COLUMN SELECTOR - Display Uncaptured-auth and captured file headers

//==========================================================================================================
	
	// ---------------------------- UNCAPTURED AUTHORIZATION FILES ----------------------------
	
	private DefaultListModel<String> authHeaderModel = new DefaultListModel<String>();
	private JList<String> authHeaderList;
	private JScrollPane authHeaderListContainer;
	
	/**
	 * <p>
	 * Panel that holds the JList for the uncapturedAuth headers.
	 * Loaded to FileView
	 * </p>
	 */
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
	
	/**
	 * <p>
	 * When adding file, adds file headers to JList selection scrollpane.
	 * When removing file, clears the JList selection scrollpane
	 * </p>
	 * @return DefaultListModel for uncaptureAuth header list
	 */
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
	
	// --------------------------- BATCHED TRANS COLUMN LIST ---------------------------------------------------
	
	private DefaultListModel<String> batchedHeaderModel = new DefaultListModel<String>();
	private JList<String> batchedHeaderList;
	private JScrollPane batchedHeaderListContainer;
	
	/**
	 * <p>
	 * Panel that holds the JList for the captured headers.
	 * Loaded to FileView
	 * </p>
	 */
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
	
	/**
	 * <p>
	 * When adding file, adds file headers to JList selection scrollpane.
	 * When removing file, clears the JList selection scrollpane
	 * </p>
	 * @return DefaultListModel for captured header list
	 */
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
		this.matchedHeadersTable = new JTable(this.loadMatchedHeadersTableModel());
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
			FileModel posModel = FileData.getFileModel(FileType.POS);
			ColumnData.setPOSHeadersToTableData(posModel.getFileContents().getHeaderNames());
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
	 * List selection listener for when unAuthed or Batched List items
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
			
			// Do nothing if list is not batched or unAuthed list (avoid null pointer on delete file)
			if(le.getSource() == displayFileNames || index < 0) {return;}

			if(le.getSource() == authHeaderList) {
				type = FileType.UNCAPTURED_AUTH;
				colPos = 1;
			}
			if(le.getSource() == batchedHeaderList) {
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
