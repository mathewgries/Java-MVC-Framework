package com.freedompay.data;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import com.freedompay.util.FileType;

/**
 * <p>
 * Repository for the matched columns table data.
 * Saves the state of the table so the user can change vies
 * without losing the match set of columns.
 * </p>
 * @author MGries
 *
 */
public class ColumnData {
	
	// Headers for the Matched Table
	private static String[] tableHeaders = {"POS","Uncaptured", "Captured"};
	// The data displayed in the table
	private static DefaultTableModel tableDataModel = null;
	
	/**
	 * <p>
	 * Loads the POS file headers to the first row of the table when the 
	 * file is uploaded
	 * </p>
	 * @param cols The headers from the file
	 */
	public static void setPOSHeadersToTableData(List<String> cols) {
		tableDataModel.setRowCount(0);
		for(String col : cols) {
			tableDataModel.addRow(new String[] {col, null, null});
		}
	}
	
	/**
	 * <p>
	 * Returns the current state of the Matched Table
	 * </p>
	 * @return DefaultTableModel for the matched table
	 */
	public static DefaultTableModel getTableData(){
		if(ColumnData.tableDataModel == null && !FileData.getIsPOSLoadedFlag()) {
			ColumnData.tableDataModel = new DefaultTableModel(ColumnData.tableHeaders, 0); 
		}
		return ColumnData.tableDataModel;
	}
	
	/**
	 * <p>
	 * Update the table data when a match is selected. This will also return
	 * the previous value of the cell if the value is being updated. If no current
	 * value exist, the return value is null.
	 * </p>
	 * @param value The value to be displayed in the table cell
	 * @param selectedRow The row to store the value
	 * @param column The column to store the value
	 * @return The old value of the cell, or null
	 */
	public static String updateTableData(String value, int selectedRow, int column) {
		String oldValue = null;
		if(ColumnData.tableDataModel.getValueAt(selectedRow, column) != null) {
			oldValue = (String) ColumnData.tableDataModel.getValueAt(selectedRow, column);
		}
		ColumnData.tableDataModel.setValueAt(value, selectedRow, column);
		return oldValue;
	}
	
	/**
	 * <p>
	 * Updates the table data model when a cell value is cleared.
	 * </p>
	 * @param row The row where the cell is being cleared
	 * @param col The column where the cell is being cleared
	 */
	public static void clearCell(int row, int col) {
		if(ColumnData.tableDataModel.getValueAt(row, col) != null) {
			ColumnData.tableDataModel.setValueAt(null, row, col);	
		}
	}
	
	/**
	 * <p>
	 * Clears the column for the deleted file.
	 * </p>
	 * @param type The FileType of the deleted file
	 */
	public static void clearMatchedCells(FileType type) {
		int removeColumn = -1;
		int checkColumn = -1;
		int rowCount = ColumnData.tableDataModel.getRowCount();
	
		switch(type) {
			case UNCAPTURED_AUTH:
				removeColumn = 1;
				checkColumn = 2;
				break;
			case CAPTURED:
				removeColumn = 2;
				checkColumn = 1;
				break;
			default:
				break;
		}
		for(int i = 0; i < rowCount; i++) {
			ColumnData.tableDataModel.setValueAt(null, i, removeColumn);
			if(ColumnData.getTableData().getValueAt(i, checkColumn) == null) {
				FileData.getFileModel(FileType.POS).getFileContents().updateSelectedHeaders(null, (String)ColumnData.getTableData().getValueAt(i, 0));
			}
		}
	}
	
	/**
	 * <p>Prints the current state of the DefaultTableModel</p>
	 */
	public static void printMatchedTable() {
		int rowCount = ColumnData.tableDataModel.getRowCount();
		for(int i = 0; i < rowCount; i++) {
			for(int j = 0; j < 3; j++) {
				System.out.print(ColumnData.tableDataModel.getValueAt(i, j)+", ");
			}
			System.out.println();
		}
	}
}
