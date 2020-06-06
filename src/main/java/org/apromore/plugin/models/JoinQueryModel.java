package org.apromore.plugin.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Model for a join query.
 */
public class JoinQueryModel {
	private String selectedTableA;
	private String selectedTableB;
	private String selectedJoin;
	private String selectedKeyA;
	private String selectedKeyB;
		
	private List<String> tableAKeys;
	private List<String> tableBKeys;	
	
	public JoinQueryModel() {	
		tableAKeys = new ArrayList<String>();
		tableBKeys = new ArrayList<String>();
	}
	
	/**
	 * Send the join query to Impala. Not currently implemented.
	 */
	public void submit() {
		System.out.println("Query:"
				+ selectedTableA + " "
				+ selectedTableB + " "
				+ selectedJoin + " "
				+ selectedKeyA + " "
				+ selectedKeyB
				);
	}
	
	public String getSelectedTableA() {
		return selectedTableA;
	}
	public void setSelectedTableA(String selectedTableA) {
		this.selectedTableA = selectedTableA;
	}
	public String getSelectedTableB() {
		return selectedTableB;
	}
	public void setSelectedTableB(String selectedTableB) {
		this.selectedTableB = selectedTableB;
	}
	public String getSelectedJoin() {
		return selectedJoin;
	}
	public void setSelectedJoin(String selectedJoin) {
		this.selectedJoin = selectedJoin;
	}
	public String getSelectedKeyA() {
		return selectedKeyA;
	}
	public void setSelectedKeyA(String selectedKeyA) {
		this.selectedKeyA = selectedKeyA;
	}
	public String getSelectedKeyB() {
		return selectedKeyB;
	}
	public void setSelectedKeyB(String selectedKeyB) {
		this.selectedKeyB = selectedKeyB;
	}
	
	public List<String> getTableAKeys() {
		return tableAKeys;
	}
	
	public void setTableAKeys(List<String> tableAKeys) {
		this.tableAKeys = tableAKeys;
	}
	
	public List<String> getTableBKeys() {
		return tableBKeys;
	}
	public void setTableBKeys(List<String> tableBKeys) {
		this.tableBKeys = tableBKeys;
	}
	
}
