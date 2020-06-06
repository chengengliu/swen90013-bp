package org.apromore.plugin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apromore.plugin.models.JoinQueryModel;
import org.apromore.plugin.services.FileHandlerService;
import org.apromore.plugin.services.Transaction;
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

	private List<JoinQueryModel> joinQueryModels;
	private List<String> filenames;	
	private List<String> joins;

	@WireVariable
    private FileHandlerService fileHandlerService;
	
	@WireVariable
    private Transaction transactionService;

	/**
     * Initialise.
     */
	@Init
    public void init() {
		filenames = new ArrayList<String>(); 
		joinQueryModels = new ArrayList<JoinQueryModel>();
		joins = new ArrayList<String>();
		joins.add("Inner");
		joins.add("Left");
		joins.add("Right");
		joinQueryModels.add(new JoinQueryModel());
    }	
	
	/**
	 * onSelect command for table A being selected.
	 * @param index
	 */
	@Command("onTableASelected") @NotifyChange("joinQueryModels")
	public void onTableASelected(@BindingParam("index") int index) {
		String selectedTableAName = joinQueryModels.get(index).getSelectedTableA();	
		List<List<String>> resultsList;
		
		try {
			resultsList = transactionService.getSnippet(selectedTableAName, 1);			
			joinQueryModels.get(index).getTableAKeys().clear();
			joinQueryModels.get(index).setTableAKeys(resultsList.get(0));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}

	/**
	 * onSelect command for table B being selected.
	 * @param index
	 */
	@Command("onTableBSelected") @NotifyChange("joinQueryModels")
    public void onTableBSelected(@BindingParam("index") int index) {
		String selectedTableBName = joinQueryModels.get(index).getSelectedTableB();
		List<List<String>> resultsList;
		
		try {
			resultsList = transactionService.getSnippet(selectedTableBName, 1);			
			joinQueryModels.get(index).getTableBKeys().clear();
			joinQueryModels.get(index).setTableBKeys(resultsList.get(0));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
    }
	
	
	/**
	 * Submit the join query for given index to Impala.
	 * 
	 */
	@Command("submitQuery")
    public void submitQuery() {
        getCurJoinQueryModel().submit();        
    }
	
	
	/**
	 * Update list of file names. Can be called globally.
	 * 
	 * @param filenames list of uploaded file names
	 */
	@GlobalCommand @NotifyChange("filenames")
    public void newFileUpload(@BindingParam("filenames") List<String> filenames) {		
		this.filenames = filenames;
    }
	
	/**
	 * Get the last joinQueryModel in the list.
	 * 
	 * @return the last joinQueryModel in the list.
	 */
	public JoinQueryModel getCurJoinQueryModel() {
		return this.joinQueryModels.get(joinQueryModels.size() - 1);
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
	
	public List<String> getFilenames() {
		return filenames;
	}

	public void setFilenames(List<String> filenames) {
		this.filenames = filenames;
	}
}
