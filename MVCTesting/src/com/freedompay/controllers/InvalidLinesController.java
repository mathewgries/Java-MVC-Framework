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
import com.freedompay.util.FileType;
import com.freedompay.views.View;

public class InvalidLinesController extends Controller implements ListSelectionListener, ActionListener {
	
	private View view;
	
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
	
	private JButton backBtn;
	
	public JButton getBackBtn() {
		this.backBtn = new JButton("Back To Files");
		this.backBtn.addActionListener(this);
		return this.backBtn;
	}
	
	// ----------- BUTTON ACTION LISTENER ----------------------------
	
	public void actionPerformed(ActionEvent e) {
		JButton btn = (JButton) e.getSource();
		
		if(btn.getText().equalsIgnoreCase("Back To Files")) {
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
			if(i.next().getFileContents().getSelectedColumnIndexes() != null) {
				this.fileNamesListModel.addElement((String)i.next().getName());
			}
		}
		return this.fileNamesListModel;
	}
	
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

	private void setPOSHeadersToTable() {
		if(FileData.getFileModel(FileType.POS) != null) {
			FileModel posModel = FileData.getFileModel(FileType.POS);
			ColumnData.setPOSHeadersToTableData(posModel.getFileContents().getHeaderNames());
		}else {
			ColumnData.getTableData().setRowCount(0);
		}
		
	}
	
//===========================================================================

	// TABLE DISPLAY FOR INVALID FILE LINES

//===========================================================================
	
	//------------------- Invalid File lines to display -----------------
	
	private DefaultTableModel invalidModel = new DefaultTableModel();
	private JTable invalidLinesTable = null;
	private JScrollPane tableSP;
	private List<String> columnNames;
	private  List<ArrayList<Integer>> errorIntegers = null;
	//Object[][] rowData = new Object[0][0];
	
	public JScrollPane getInvalidLineItemsList() {
		this.invalidLinesTable = new JTable(this.invalidModel);
		this.invalidLinesTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF );
		this.tableSP = new JScrollPane(this.invalidLinesTable);
		this.tableSP.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.tableSP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		return this.tableSP;
	}
	
	public void valueChanged(ListSelectionEvent le) {
		if(!le.getValueIsAdjusting()) {
			@SuppressWarnings("unchecked")
			JList<String> list = (JList<String>) le.getSource();
			String filename = list.getSelectedValue();
			this.updateInvalidModel(filename);
		}
	}
	
	private void updateInvalidModel(String filename) {
		FileModel model = FileData.getFileModel(filename);
		this.invalidModel.setColumnCount(0);
		this.invalidModel.setRowCount(0);
		//this.errorIntegers = model.getInvalidRowIndexesWithEnumInts();
	    //this.setTableHeaders(model.getHeaderNames());
	    if(filename != null) {
	        for(ArrayList<Integer> line : errorIntegers) {
	        	if(line.size() > 1) {
	        		this.invalidModel.addRow(this.printErrorLineToArray(model, line));
	        	}
	        }
	    }
	}
	
	private void setTableHeaders(List<String> headers) {
		this.invalidModel.setColumnCount(0);
		this.invalidModel.addColumn("Line");
		this.invalidModel.addColumn("ErrorCount");
		this.invalidModel.addColumn("ErrorList");
		for(int i=0 ;i<headers.size();i++) {
			this.invalidModel.addColumn(headers.get(i));
		}
	}
	
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