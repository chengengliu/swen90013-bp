package org.apromore.plugin.models;

public class joinQueryModel {
	private String selectedTableA;
	private String selectedTableB;
	private String selectedJoin;
	private String selectedKeyA;
	private String selectedKeyB;
	
	public joinQueryModel(String selectedTableA, String selectedTableB, String selectedJoin, String selectedKeyA,
			String selectedKeyB) {		
		this.selectedTableA = selectedTableA;
		this.selectedTableB = selectedTableB;
		this.selectedJoin = selectedJoin;
		this.selectedKeyA = selectedKeyA;
		this.selectedKeyB = selectedKeyB;
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
	
}
