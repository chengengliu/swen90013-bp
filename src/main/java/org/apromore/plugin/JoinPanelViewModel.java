package org.apromore.plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apromore.plugin.models.JoinQueryModel;
import org.apromore.plugin.services.FileHandlerService;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;


/**
 * Model for the join panel.
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class JoinPanelViewModel {

	private List<JoinQueryModel> joinQueryModels = new ArrayList<JoinQueryModel>();
	
	private String selectedFile;
		
	private List<String> files = new ArrayList<String>();
	private List<String> joins = new ArrayList<String>();

	@WireVariable
    private FileHandlerService fileHandlerService;

	@Init
    public void init() {
		files.add("A");
		files.add("B");
		files.add("C");
		joins.add("Inner");
		joins.add("Left");
		joins.add("Right");
		//joinQueryModels.add(new JoinQueryModel("a", "b", "c", "d", "e"));
		joinQueryModels.add(new JoinQueryModel());
		
		
    }
	
	//Maybe pass in the JoinQueryModel into here.
	@Command("onTableASelected")
	public void onTableASelected() {
		//files.add(selectedFile);
		System.out.println("working");
		System.out.println("Table A for JQM0: " + joinQueryModels.get(0).getSelectedTableA());
		//System.out.println("Table A for JQM1: " + joinQueryModels.get(1).getSelectedTableA());
	}

	@Command("onTableBSelected")
    public void onTableBSelected() {
        //files.add(selectedFile);
        System.out.println("working");
        System.out.println("Table B for JQM0: " + joinQueryModels.get(0).getSelectedTableB());
        //System.out.println("Table B for JQM1: " + joinQueryModels.get(1).getSelectedTableB());
    }
	
	@Command("submitQuery")
    public void submitQuery() {
        //files.add(selectedFile);
        System.out.println("submit query");
        getCurJoinQueryModel().submit();
        
        ArrayList<File> files = fileHandlerService.outputFiles();
		
		System.out.println(files);
    }
	
	public List<String> getFiles() {
		return files;
	}

	public void setFiles(List<String> files) {
		this.files = files;
	}

	
	public List<JoinQueryModel> getJoinQueryModels() {
		return joinQueryModels;
	}


	public void setJoinQueryModels(List<JoinQueryModel> joinQueryModels) {
		this.joinQueryModels = joinQueryModels;
	}
	
	public String getSelectedFile() {
		return selectedFile;
	}


	public void setSelectedFile(String selectedFile) {
		this.selectedFile = selectedFile;
	} 
	
	public List<String> getJoins() {
		return joins;
	}

	public void setJoins(List<String> joins) {
		this.joins = joins;
	}
	
	public JoinQueryModel getCurJoinQueryModel() {
		return this.joinQueryModels.get(joinQueryModels.size() - 1);
	}
}
