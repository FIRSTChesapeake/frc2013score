package main.java.FRC_Score_Sys;

import javax.swing.table.DefaultTableModel;

public class customTableModel extends DefaultTableModel {
	private static final long	serialVersionUID	= 1L;

	public boolean isCellEditable(int row, int column){
		return false;
	}
	
}
