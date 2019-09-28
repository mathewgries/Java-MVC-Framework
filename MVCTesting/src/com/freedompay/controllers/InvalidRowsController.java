package com.freedompay.controllers;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import com.freedompay.data.ColumnData;
import com.freedompay.data.FileData;
import com.freedompay.models.FileModel;
import com.freedompay.services.IRouteListener;
import com.freedompay.util.ErrorType;
import com.freedompay.views.View;

/**
 * Controller for InvalidRowsView. Sets up the Table Views to display
 * the rows from each file that have invalid data for matching.
 * @author MGries
 *
 */
public class InvalidRowsController extends Controller implements ListSelectionListener, ActionListener {
	
	private View view;
	
	// Observer list and methods
	private List<IRouteListener> observers = new ArrayList<IRouteListener>();
	
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
	
	/**
	 * <p>
	 * Add the view to the controller
	 * </p>
	 */
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
	
	private JButton compareBtn;
	private JButton backBtn;
	
	/**
	 * <p>
	 * Button to change the view to the MatchedRowsView
	 * </p>
	 */
	public JButton getCompareBtn() {
		this.compareBtn = new JButton("Compare Files");
		this.compareBtn.addActionListener(this);
		return this.compareBtn;
	}
	
	/**
	 * <p>
	 * Button to change the view back to the Files view.
	 * </p>
	 */
	public JButton getBackBtn() {
		this.backBtn = new JButton("Back To Files");
		this.backBtn.addActionListener(this);
		return this.backBtn;
	}
	
	// ----------- BUTTON ACTION LISTENER ----------------------------
	
	/**
	 * <p>
	 * Event listener for when the navigation buttons are clicked
	 * to change the view.
	 * </p>
	 */
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == compareBtn) {
			this.notifyObservers("MatchedRows");
		}
		
		if(e.getSource() == backBtn) {
			this.notifyObservers("Files");
		}
	}
	
//===========================================================================
	
							//FILE LISTS

//===========================================================================
	
	//------------ Uploaded Files to Display --------------------------
	
	private DefaultListModel<String> fileNamesListModel = new DefaultListModel<String>();
	private JList<String> displayFileNames;
	private JScrollPane fileNameListContainer;
	
	/**
	 * <p>
	 * Load the file name list to the View.
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
	 * Get the file names to load to the list for the View
	 * </p>
	 */
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
	
	/**
	 * <p>
	 * A table to display the headers from the POS file, and the headers
	 * selected to match against in the unauthed and batched file.
	 * </p>
	 */
	public JScrollPane getMatchedHeadersTable() {
		this.matchedHeadersTable = new JTable(this.loadMatchedHeadersTableModel());
		this.matchedHeadersTable.setEnabled(false);
		this.matchedHeadersScrollPane = new JScrollPane(this.matchedHeadersTable);
		return this.matchedHeadersScrollPane;
	}
	
	/**
	 * <p>
	 * Get the data for the matched headers table.
	 * </p>
	 * @return
	 */
	private DefaultTableModel loadMatchedHeadersTableModel() {
		return ColumnData.getTableData();
	}
	
//===========================================================================

					// TABLE DISPLAY FOR INVALID FILE LINES

//===========================================================================
	
	//------------------- Invalid File lines to display -----------------
	
	private DefaultTableModel invalidModel = new DefaultTableModel();
	private JTable invalidLinesTable = null;
	private JScrollPane tableSP;
	private  List<ArrayList<Integer>> errorIntegers = null;
	
	/**
	 * <p>
	 * Load the invalid rows from the file selected in the FileNames list
	 * </p>
	 */
	public JScrollPane getInvalidRowsList() {
		this.invalidLinesTable = new JTable(this.invalidModel);
		this.invalidLinesTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF );
		this.tableSP = new JScrollPane(this.invalidLinesTable);
		this.tableSP.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.tableSP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		return this.tableSP;
	}
	
	/**
	 * <p>
	 * Event listener for when the file name selection is changed
	 * in the file name list.
	 * </p>
	 */
	public void valueChanged(ListSelectionEvent le) {
		if(!le.getValueIsAdjusting()) {
			@SuppressWarnings("unchecked")
			JList<String> list = (JList<String>) le.getSource();
			String filename = list.getSelectedValue();
			this.updateInvalidModel(filename);
		}
	}
	
	/**
	 * <p>
	 * Update the view when a file is selected in the Filename list.
	 * </p>
	 * @param filename the file selected in the list
	 */
	private void updateInvalidModel(String filename) {
		FileModel model = FileData.getFileModel(filename);
		this.invalidModel.setColumnCount(0);
		this.invalidModel.setRowCount(0);
		if(model.getFileContents().getInvalidRowIntegers() != null) {
			this.errorIntegers = model.getFileContents().getInvalidRowIntegers();
		    this.setTableHeaders(model.getFileContents().getHeaderNames());
		    if(filename != null) {
		        for(ArrayList<Integer> line : errorIntegers) {
		        	if(line.size() > 1) {
		        		this.invalidModel.addRow(this.printErrorLineToArray(model, line));
		        	}
		        }
		    }
		}
	}
	
	/**
	 * <p>
	 * Update the column names for the Invalid Rows table when a new file
	 * is selected.
	 * </p>
	 * @param headers
	 */
	private void setTableHeaders(List<String> headers) {
		this.invalidModel.setColumnCount(0);
		this.invalidModel.addColumn("Line");
		this.invalidModel.addColumn("ErrorCount");
		this.invalidModel.addColumn("ErrorList");
		for(int i=0 ;i<headers.size();i++) {
			this.invalidModel.addColumn(headers.get(i));
		}
	}
	
	/**
	 * <p>
	 * Converts the InvalidRowInteger list from the FileModels FileContents class
	 * to the row from the file for display in the table. Includes the error count
	 * and error type.
	 * </p>
	 * @param model
	 * @param line
	 * @return
	 */
	private Object[] printErrorLineToArray(FileModel model, ArrayList<Integer> line) {
		String[] results = new String[model.getFileContents().getColumnCount() + 3];
		List<ArrayList<String>> lines = model.getFileContents().getFileRows();
		results[0] = Integer.toString(line.get(0)); 
		results[1] = Integer.toString(line.size() - 1); 
		results[2] = "";
		
		for(int i = 1; i < line.size(); i++) {
			results[2] += this.setErrorsFromEnum(line.get(i));
		}
		
		for(int i = 0; i < model.getFileContents().getColumnCount();i++) {
			results[i + 3] = lines.get(line.get(0)).get(i);
		}
		return results;
	}
	
	/**
	 * <p>
	 * Sets the error type to string when printing the errors and rows to the 
	 * invalid rows table.
	 * </p>
	 * @param value The integer for the ErrorType enum
	 * @return The string value for the error type.
	 */
	private String setErrorsFromEnum(int value){
		String errorList = "";
		switch(ErrorType.valueOf(value)) {
			case NULL_OR_EMPTY_VALUE:
				errorList += "Null or Empty Value, ";
				break;
			case INVALID_PAN:
				errorList += "Invalid Card Data, ";
				break;
			case NO_REQUESTID:
				errorList += "No RequestId Present, ";
				break;
			case NON_NUMERIC:
				errorList += "Dollar Value Invalid, ";
				break;
			case DUPLICATED_RECORD:
				errorList += "Match Multiple Records, ";
				break;
			default:
				errorList+= "";
				break;
		}
		return errorList;
	}
}