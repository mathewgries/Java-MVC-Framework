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
import com.freedompay.util.FileGenerator;
import com.freedompay.util.FileType;
import com.freedompay.views.View;


/**
 * <p>
 * This view is where the user can view the rows where matches were found
 * They can also see rows where multiple matches were found, and rows
 * where no matches were found.
 * </p>
 * @author MGries
 *
 */
public class MatchedRowsController extends Controller{

	private View view;
	
	// ------------ OBSERVER METHODS ----------------------------
	
	// The list of observers for the controller -- RouteConfig
	private List<IRouteListener> observers = new ArrayList<IRouteListener>();
	
	/**
	 * <p>Add the observers</p>
	 */
	public void addObserver(IRouteListener obj) {
		this.observers.add(obj);
	}
	
	/**
	 * <p>Remove the observers</p>
	 */
	public void removeObserver(IRouteListener obj) {
		this.observers.remove(obj);
	}
	
	/**
	 * <p>Notify the observers</p>
	 */
	@Override
	public void notifyObservers(Object obj) {
		for(IRouteListener rs : observers) {
			rs.update(obj);
		}
	}
	
	// --------- ADD THE VIEW --------------------------
	
	/**
	 * <p>Add the MatchedRowsView</p>
	 */
	@Override
	public void addView(View v) {
		view = v;
	}
	
	/**
	 * <p>Basic label for the view</p>
	 */
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
	private JButton exportBtn;
	
	/**
	 * <p>Button to view matched rows from Uncaptured file</p>
	 */
	public JButton getMatchedUncapturedBtn() {
		this.matchedUncapturedBtn = new JButton("Matched Uncaptured Auth");
		this.matchedUncapturedBtn.addActionListener(this);
		this.matchedUncapturedBtn.setEnabled(ComponentData.getMatchedUncapturedBtnIsEnabled());
		return this.matchedUncapturedBtn;
	}
	
	/**
	 * <p>Button to view matched rows from Captured file</p>
	 */
	public JButton getMatchedCapturedBtn() {
		this.matchedCapturedBtn = new JButton("Matched Captured");
		this.matchedCapturedBtn.addActionListener(this);
		this.matchedCapturedBtn.setEnabled(ComponentData.getMatchedCapturedBtnIsEnabled());
		return this.matchedCapturedBtn;
	}
	
	/**
	 * <p>Button to view duplicate matches from Uncaptured file</p>
	 */
	public JButton getDuplicateUncapturedBtn() {
		this.duplicateUncapturedBtn = new JButton("Duplicate Uncaptured Matches");
		this.duplicateUncapturedBtn.addActionListener(this);
		this.duplicateUncapturedBtn.setEnabled(ComponentData.getDuplicateUncapturedBtnIsEnabled());
		return this.duplicateUncapturedBtn;
	}
	
	/**
	 * <p>Button to view duplicate matches from Captured file</p>
	 */
	public JButton getDuplicateCapturedBtn() {
		this.duplicateCapturedBtn = new JButton("Duplicate Captured Matches");
		this.duplicateCapturedBtn.addActionListener(this);
		this.duplicateCapturedBtn.setEnabled(ComponentData.getDuplicateCapturedBtnIsEnabled());
		return this.duplicateCapturedBtn;
	}
	
	/**
	 * <p>Button to view rows with now matches</p>
	 */
	public JButton getNoMatchBtn() {
		this.noMatchBtn = new JButton("Not Matched");
		this.noMatchBtn.addActionListener(this);
		return this.noMatchBtn;
	}
	
	/**
	 * <p>Button to return to the FilesView</p>
	 */
	public JButton getBackBtn() {
		this.backBtn = new JButton("Back To Files");
		this.backBtn.addActionListener(this);
		return this.backBtn;
	}
	
	public JButton getExportBtn() {
		this.exportBtn = new JButton("Export Results");
		this.exportBtn.addActionListener(this);
		return this.exportBtn;
	}

	// ----------- BUTTON ACTION LISTENER ----------------------------

	/**
	 * <p>Action event handler for the buttons</p>
	 */
	public void actionPerformed(ActionEvent e) {
		
		// Go back to FilesView
		if(e.getSource() == backBtn) {
			this.notifyObservers("Files");
		}else if(e.getSource() == exportBtn) {
			this.exportResults();
		}else {
			// Update the table view
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
	
	
	/**
	 * <p>List of names for uploaded files</p>
	 */
	public JScrollPane getFileNameList() {
		this.displayFileNames = new JList<String>(this.loadFileNames());
		this.displayFileNames.setEnabled(false);
		this.displayFileNames.setVisibleRowCount(6);
		this.fileNameListContainer = new JScrollPane(this.displayFileNames);
		return this.fileNameListContainer;
	}
	
	/**
	 * <p>Load file names to the list</p>
	 * @return File names list
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
	 * <p>The matched headers table</p>
	 */
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
	
	/**
	 * <p>Load the matched headers data to the table</p>
	 * @return MatchedHeadersTable data
	 */
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
	 * Load the selected file data. Matched rows, duplicated rows, and unmatched rows.
	 * This table is updated depending on the button selected by the user.
	 * </p>
	 */
	public JScrollPane getComparisonResultsTable() {
		this.comparisonResultsTable = new JTable(this.comparisonResultsTableModel);
		this.comparisonResultsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF );
		this.tableSP = new JScrollPane(this.comparisonResultsTable);
		this.tableSP.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.tableSP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		return this.tableSP;
	}
	
	/**
	 * <p>
	 * Called from the button listener method. Updates the Table data
	 * based on the selected button
	 * </p>
	 * @param e
	 */
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
	
	/**
	 * <p>
	 * Loads matched row data to the table. Uncaptured and Captured, depending on
	 * selected button
	 * </p>
	 * @param rowIndexes Indexes from comparison for POS and selected file type
	 * @param type The selected file type, Uncaptured or Captured
	 */
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
	
	/**
	 * <p>Loads the headers for the match selection</p>
	 * @return Column count for table
	 */
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
	
	/**
	 * <p>Loads the duplicated row data for selected file, Uncaptured or Captured</p>
	 * @param rowIndexes Indexes for POS file, and the matched rows from the selected file
	 * @param type The chosen file type, Uncaptured or Captured
	 */
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
	
	/**
	 * <p>Concatenates the indexes of found duplicates to a single string</p>
	 * @param row The row from the index list where the duplicated indexes are stored
	 * @return The duplicated indexes in a single string
	 */
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
	
	/**
	 * <p>Set the headers for the duplicated table data</p>
	 * @return
	 */
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
	
	/**
	 * <p>Sets the row data for POS rows with no found match</p>
	 * @param rowIndexes
	 */
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
	
	/**
	 * <p>Headers for rows with no match table data</p>
	 * @return
	 */
	private int setHeadersForNoMatches() {
		List<String> posHeaders = FileData.getFileModel(FileType.POS).getFileContents().getHeaderNames();
		
		this.comparisonResultsTableModel.addColumn("Row");
		for(String header : posHeaders) {
			this.comparisonResultsTableModel.addColumn(header);
		}
		return this.comparisonResultsTableModel.getColumnCount();
	}
	
//===========================================================================

				// EXPORT RESULTS TO FILE

//===========================================================================
	
	private void exportResults() {
		if(Comparison.getUncapturedLoadedFlag()) {
			if(Comparison.getMatchedRowsUncapturedIndexes() != null && Comparison.getMatchedRowsUncapturedIndexes().size() > 0) {
				FileGenerator.exportMatchFile(Comparison.getMatchedRowsUncapturedIndexes(), FileType.UNCAPTURED_AUTH, "MatchedUncapturedAuthorizations");
			}
			if(Comparison.getDuplicateRowsUncapturedIndexes() != null && Comparison.getDuplicateRowsUncapturedIndexes().size() > 0) {
				FileGenerator.exportDuplicateRowsFiles(Comparison.getDuplicateRowsUncapturedIndexes(), FileType.UNCAPTURED_AUTH, "DuplicateMatchesUncapturedAuthorization");
			}
		}
		
		if(Comparison.getCapturedLoadedFlag()) {
			if(Comparison.getMatchedRowsCapturedIndexes() != null && Comparison.getMatchedRowsCapturedIndexes().size() > 0) {
				FileGenerator.exportMatchFile(Comparison.getMatchedRowsCapturedIndexes(), FileType.CAPTURED, "MatchedCapturedSettlements");
			}
			if(Comparison.getDuplicateRowsCapturedIndexes() != null && Comparison.getDuplicateRowsCapturedIndexes().size() > 0) {
				FileGenerator.exportDuplicateRowsFiles(Comparison.getDuplicateRowsCapturedIndexes(), FileType.CAPTURED, "DuplicateMatchesCapturedSettlements");
			}
		}
		
		if(Comparison.getNoMatches() != null && Comparison.getNoMatches().size() > 0) {
			FileGenerator.exportNoMatchFile(Comparison.getNoMatches(), "NoMatchesFound");
		}
		
		FileGenerator.openResultsDirectory();
	}
}
