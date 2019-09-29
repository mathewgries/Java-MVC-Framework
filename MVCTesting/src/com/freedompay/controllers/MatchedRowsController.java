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
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import com.freedompay.data.ColumnData;
import com.freedompay.data.ComponentData;
import com.freedompay.data.FileData;
import com.freedompay.models.FileModel;
import com.freedompay.services.IRouteListener;
import com.freedompay.util.Comparison;
import com.freedompay.util.FileType;
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

	private JButton matchedUncapturedBtn;
	private JButton matchedCapturedBtn;
	private JButton duplicateUncapturedBtn;
	private JButton duplicateCapturedBtn;
	private JButton noMatchBtn;
	private JButton backBtn;
	
	public JButton getMatchedUncapturedBtn() {
		this.matchedUncapturedBtn = new JButton("Matched Uncaptured Auth");
		this.matchedUncapturedBtn.addActionListener(this);
		this.matchedUncapturedBtn.setEnabled(ComponentData.getMatchedUncapturedBtnIsEnabled());
		return this.matchedUncapturedBtn;
	}
	
	public JButton getMatchedCapturedBtn() {
		this.matchedCapturedBtn = new JButton("Matched Captured");
		this.matchedCapturedBtn.addActionListener(this);
		this.matchedCapturedBtn.setEnabled(ComponentData.getMatchedCapturedBtnIsEnabled());
		return this.matchedCapturedBtn;
	}
	
	public JButton getDuplicateUncapturedBtn() {
		this.duplicateUncapturedBtn = new JButton("Duplicate Uncaptured Matches");
		this.duplicateUncapturedBtn.addActionListener(this);
		this.duplicateUncapturedBtn.setEnabled(ComponentData.getDuplicateUncapturedBtnIsEnabled());
		return this.duplicateUncapturedBtn;
	}
	
	public JButton getDuplicateCapturedBtn() {
		this.duplicateCapturedBtn = new JButton("Duplicate Captured Matches");
		this.duplicateCapturedBtn.addActionListener(this);
		this.duplicateCapturedBtn.setEnabled(ComponentData.getDuplicateCapturedBtnIsEnabled());
		return this.duplicateCapturedBtn;
	}
	
	public JButton getNoMatchBtn() {
		this.noMatchBtn = new JButton("Not Matched");
		this.noMatchBtn.addActionListener(this);
		return this.noMatchBtn;
	}
	
	public JButton getBackBtn() {
		this.backBtn = new JButton("Back To Files");
		this.backBtn.addActionListener(this);
		return this.backBtn;
	}

	// ----------- BUTTON ACTION LISTENER ----------------------------

	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == backBtn) {
			this.notifyObservers("Files");
		}else {
			this.updateComparisonResultsTableModel(e);
		}
	}

//===========================================================================

					// UPLOADED FILE LIST

//===========================================================================

	//------------ Uploaded Files to Display --------------------------
	
	private DefaultListModel<String> fileNamesListModel = new DefaultListModel<String>();
	private JList<String> displayFileNames;
	private JScrollPane fileNameListContainer;
	
	
	// Display uploaded file names in a JList
	public JScrollPane getFileNameList() {
		this.displayFileNames = new JList<String>(this.loadFileNames());
		this.displayFileNames.setEnabled(false);
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
		this.matchedHeadersTable = new JTable(this.loadMatchedHeadersTableModel()) {
			private static final long serialVersionUID = 5467279387896919490L;
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		this.matchedHeadersScrollPane = new JScrollPane(this.matchedHeadersTable);
		this.matchedHeadersTable.setEnabled(false);
		return this.matchedHeadersScrollPane;
	}
	
	private DefaultTableModel loadMatchedHeadersTableModel() {
		return ColumnData.getTableData();
	}
	
//===========================================================================

				// TABLE VIEW OF FILE COMPARISON RESULTS

//===========================================================================
	
	private DefaultTableModel comparisonResultsTableModel = new DefaultTableModel();
	private JTable comparisonResultsTable = null;
	private JScrollPane tableSP;
	
	/**
	 * <p>
	 * Load the invalid rows from the file selected in the FileNames list
	 * </p>
	 */
	public JScrollPane getComparisonResultsTable() {
		this.comparisonResultsTable = new JTable(this.comparisonResultsTableModel);
//		{
//			private static final long serialVersionUID = 1L;
//			public boolean isCellEditable(int row, int col) {
//				return false;
//			}
//		};
		this.comparisonResultsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF );
		this.tableSP = new JScrollPane(this.comparisonResultsTable);
		this.tableSP.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.tableSP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		return this.tableSP;
	}
	
	private void updateComparisonResultsTableModel(ActionEvent e) {
		this.comparisonResultsTableModel.setColumnCount(0);
		this.comparisonResultsTableModel.setRowCount(0);
		
		if(e.getSource() == matchedUncapturedBtn){
			this.setMatchedRowsToTableModel(Comparison.getMatchedRowsUncapturedIndexes(), FileType.UNCAPTURED_AUTH);
		}
		if(e.getSource() == matchedCapturedBtn){
			this.setMatchedRowsToTableModel(Comparison.getMatchedRowsCapturedIndexes(),  FileType.CAPTURED);
		}
		if(e.getSource() == duplicateUncapturedBtn){
			this.setDuplicateRowsToTableModel(Comparison.getDuplicateRowsUncapturedIndexes(), FileType.UNCAPTURED_AUTH);
		}
		if(e.getSource() == duplicateCapturedBtn){
			this.setDuplicateRowsToTableModel(Comparison.getDuplicateRowsCapturedIndexes(), FileType.CAPTURED);
		}
		if(e.getSource() == noMatchBtn){
			this.setNoMatchesToTableModel(Comparison.getNoMatches());
		}
	}
	
	//----------------------- Matched Rows --------------------------
	
	private void setMatchedRowsToTableModel(List<ArrayList<Integer>> rowIndexes, FileType type){
		List<ArrayList<String>> posRows = FileData.getFileModel(FileType.POS).getFileContents().getFileRows();
		List<String> requestIds = FileData.getFileModel(type).getFileContents().getRequestIds();
		int headerCount = this.setHeadersForMatches();
		int colCount = 0;
		
		for(ArrayList<Integer> row : rowIndexes) {
			String[] rowData = new String[headerCount];
			colCount = 0;
			rowData[0] = Integer.toString(row.get(0) + 1);
			rowData[1] = Integer.toString(row.get(1) + 1);
			rowData[2] = requestIds.get(row.get(1));
			
			for(String posRow : posRows.get(row.get(0))) {
				rowData[colCount + 3] = posRow;
				colCount++;
			}
			this.comparisonResultsTableModel.addRow(rowData);
		}
	}
	
	private int setHeadersForMatches(){
		List<String> posHeaders = FileData.getFileModel(FileType.POS).getFileContents().getHeaderNames();		
		
		this.comparisonResultsTableModel.addColumn("POS Row");
		this.comparisonResultsTableModel.addColumn("Matched Row");
		this.comparisonResultsTableModel.addColumn("RequestId");	
		for(String header : posHeaders) {
			this.comparisonResultsTableModel.addColumn(header);
		}
		return this.comparisonResultsTableModel.getColumnCount();
	}
	
	//----------------------- Duplicated Rows --------------------------
	
	private void setDuplicateRowsToTableModel(List<ArrayList<Integer>> rowIndexes, FileType type) {
		List<ArrayList<String>> posRows = FileData.getFileModel(FileType.POS).getFileContents().getFileRows();
		int headerCount = this.setHeadersForDups();
		int colCount = 0;
		
		for(ArrayList<Integer> row : rowIndexes) {
			String[] rowData = new String[headerCount];
			colCount = 0;
			rowData[0] = Integer.toString(row.get(0) + 1);
			rowData[1] = this.concatDupRowNumbers(row);
			
			for(String posRow : posRows.get(row.get(0))) {
				rowData[colCount + 2] = posRow;
				colCount++;
			}
			this.comparisonResultsTableModel.addRow(rowData);
		}
	}
	
	private String concatDupRowNumbers(ArrayList<Integer> row) {
		String result = "";
		
		for(int i = 1; i < row.size(); i++) {
			if(i < row.size() - 1) {
				result += (row.get(i) + 1) + ", ";
			}else {
				result += (row.get(i) + 1);
			}
		}
		return result;
	}
	
	private int setHeadersForDups() {
		List<String> posHeaders = FileData.getFileModel(FileType.POS).getFileContents().getHeaderNames();
		
		this.comparisonResultsTableModel.addColumn("POS Row");
		this.comparisonResultsTableModel.addColumn("Matched Rows");
		for(String header : posHeaders) {
			this.comparisonResultsTableModel.addColumn(header);
		}
		return this.comparisonResultsTableModel.getColumnCount();
	}
	
	//----------------------- Non Matched Rows --------------------------
	
	private void setNoMatchesToTableModel(List<Integer> rowIndexes) {
		List<ArrayList<String>> posRows = FileData.getFileModel(FileType.POS).getFileContents().getFileRows();
		int headerCount = this.setHeadersForNoMatches();
		int colCount = 0;
		
		for(Integer row : rowIndexes) {
			String[] rowData = new String[headerCount];
			colCount = 0;
			rowData[colCount++] = Integer.toString(row + 1);
			
			for(String col : posRows.get(row)) {
				rowData[colCount++] = col;
			}
			this.comparisonResultsTableModel.addRow(rowData);
		}
	}
	
	private int setHeadersForNoMatches() {
		List<String> posHeaders = FileData.getFileModel(FileType.POS).getFileContents().getHeaderNames();
		
		this.comparisonResultsTableModel.addColumn("Row");
		for(String header : posHeaders) {
			this.comparisonResultsTableModel.addColumn(header);
		}
		return this.comparisonResultsTableModel.getColumnCount();
	}
	
}
