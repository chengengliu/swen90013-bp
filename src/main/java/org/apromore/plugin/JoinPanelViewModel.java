package org.apromore.plugin;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apromore.plugin.models.JoinQueryModel;
import org.apromore.plugin.services.FileHandlerService;
import org.apromore.plugin.services.Transaction;
import org.apromore.plugin.utils.TableUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;


/**
 * Model for the join panel.
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class JoinPanelViewModel {

	private List<JoinQueryModel> joinQueryModels = new ArrayList<JoinQueryModel>();
	
	//private String selectedFile;
		
	private List<File> files = new ArrayList<File>();
	
	private List<String> filenames = new ArrayList<String>();
	
	
	private List<String> joins = new ArrayList<String>();

	@WireVariable
    private FileHandlerService fileHandlerService;
	
	@WireVariable
    private Transaction transactionService;

	@Init
    public void init() {
		joins.add("Inner");
		joins.add("Left");
		joins.add("Right");
		//joinQueryModels.add(new JoinQueryModel("a", "b", "c", "d", "e"));
		joinQueryModels.add(new JoinQueryModel());
		//refreshFiles();

    }
	
	//Maybe pass in the JoinQueryModel into here.
	@Command("onTableASelected") @NotifyChange("joinQueryModels")
	public void onTableASelected(@BindingParam("index") int index) {
		//files.add(selectedFile);
		System.out.println("working");
		System.out.println("Table A for JQM0: " + joinQueryModels.get(0).getSelectedTableA());
		//System.out.println("Table A for JQM1: " + joinQueryModels.get(1).getSelectedTableA());
		
		System.out.println("index:" + index);
		
		String selectedTableAName = joinQueryModels.get(index).getSelectedTableA();
		
		List<List<String>> resultsList;
		try {
			resultsList = transactionService.getSnippet(selectedTableAName, 1);
			
			joinQueryModels.get(index).getTableAKeys().clear();
			joinQueryModels.get(index).setTableAKeys(resultsList.get(0));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		List<String> resultsList = new ArrayList<String>();
//		resultsList.add("1");
//		resultsList.add("2");
//		resultsList.add("3");
		
		//joinQueryModels.get(index).getTableAKeys().add("1");
		
		
		
		System.out.println("tableAkeys: "+ joinQueryModels.get(index).getTableAKeys());
		
		
		
	}

	@Command("onTableBSelected")
    public void onTableBSelected() {
        //files.add(selectedFile);
        System.out.println("working");
        System.out.println("Table B for JQM0: " + joinQueryModels.get(0).getSelectedTableB());
        
        updateKeyBList();
        
        //System.out.println("Table B for JQM1: " + joinQueryModels.get(1).getSelectedTableB());
    }
	
	private void updateKeyBList() {
		// TODO Auto-generated method stub
		
	}

	@Command("submitQuery")
    public void submitQuery() {
        //files.add(selectedFile);
        System.out.println("submit query");
        getCurJoinQueryModel().submit(); 
        
    }
	
	@Command("refreshFiles")
    public void refreshFiles() {
		files = fileHandlerService.outputFiles();
		filenames.clear();		
		
		for(File f: files) {
			System.out.println(f);
			//fileNames.add(f.getName());
		}
	}
	

	@GlobalCommand @NotifyChange("filenames")
    public void newFileUpload(@BindingParam("filenames") List<String> filenames) {
        
		System.out.print("newFileUpload filenames: "+ filenames);
		
		this.filenames = filenames;
    }

	
	public List<File> getFiles() {
		return files;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}

	
	public List<JoinQueryModel> getJoinQueryModels() {
		return joinQueryModels;
	}


	public void setJoinQueryModels(List<JoinQueryModel> joinQueryModels) {
		this.joinQueryModels = joinQueryModels;
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
	
	public List<String> getFilenames() {
		return filenames;
	}

	public void setFilenames(List<String> filenames) {
		this.filenames = filenames;
	}
}
