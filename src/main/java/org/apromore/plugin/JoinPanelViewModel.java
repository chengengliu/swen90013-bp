package org.apromore.plugin;

import java.util.ArrayList;
import java.util.List;

import org.apromore.plugin.models.joinQueryModel;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.VariableResolver;


/**
 * Model for the join panel.
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class JoinPanelViewModel {

	private List<joinQueryModel> joinQueryModels = new ArrayList<joinQueryModel>();
	
	private String selectedFile;
		
	private List<String> files = new ArrayList<String>();
	

	@Init
    public void init() {
		files.add("A");
		files.add("B");
		files.add("C");
		joinQueryModels.add(new joinQueryModel("a", "b", "c", "d", "e"));
		joinQueryModels.add(new joinQueryModel("1", "2", "3", "4", "5"));

    }
	
	
	@Command("onTableASelected")
	public void onTableASelected() {
		//files.add(selectedFile);
		System.out.println("woprking");
		//System.out.println(selectedFile);
	}
	
	public List<String> getFiles() {
		return files;
	}

	public void setFiles(List<String> files) {
		this.files = files;
	}

	
	public List<joinQueryModel> getJoinQueryModels() {
		return joinQueryModels;
	}


	public void setJoinQueryModels(List<joinQueryModel> joinQueryModels) {
		this.joinQueryModels = joinQueryModels;
	}
	
	public String getSelectedFile() {
		return selectedFile;
	}


	public void setSelectedFile(String selectedFile) {
		this.selectedFile = selectedFile;
	} 
}
