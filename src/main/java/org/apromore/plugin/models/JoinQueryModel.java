package org.apromore.plugin.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

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

    /**
     * Constructor.
     */
    public JoinQueryModel() {
        tableAKeys = new ArrayList<String>();
        tableBKeys = new ArrayList<String>();
    }

    /**
     * Send the join query to Impala. Not currently implemented.
     *
     * @return Get the join table Info
     */
    public List<String> submit() {
        List<String> tableQuery = new ArrayList<>();
        tableQuery.add(FilenameUtils.removeExtension(selectedTableA));
        tableQuery.add(selectedKeyA);
        tableQuery.add(FilenameUtils.removeExtension(selectedTableB));
        tableQuery.add(selectedKeyB);
        tableQuery.add(selectedJoin);

        return tableQuery;
    }
    
    /**
     * Have all fields been assigned?
     * 
     * @return True if all fields have been assigned and false if not.
     */
    public Boolean isComplete() {
    	Boolean isComplete;  	
    	if(selectedTableA == null || selectedTableB == null ||
    			selectedJoin == null || selectedKeyA == null ||
    			selectedKeyB == null) {
    		isComplete = false;  		
    	} else {
    		isComplete = true;
    	}	
    	return isComplete;
    }

    /**
     * Get selectedTableA.
     *
     * @return selectedTableA
     */
    public String getSelectedTableA() {
        return selectedTableA;
    }

    /**
     * Set selectedTableA.
     *
     * @param selectedTableA selectedTableA
     */
    public void setSelectedTableA(String selectedTableA) {
        this.selectedTableA = selectedTableA;
    }

    /**
     * Get selectedTableB.
     *
     * @return selectedTableB
     */
    public String getSelectedTableB() {
        return selectedTableB;
    }

    /**
     * Set selectedTableB.
     *
     * @param selectedTableB selectedTableB
     */
    public void setSelectedTableB(String selectedTableB) {
        this.selectedTableB = selectedTableB;
    }

    /**
     * Get selectedJoin.
     *
     * @return selectedJoin
     */
    public String getSelectedJoin() {
        return selectedJoin;
    }

    /**
     * Set selectedJoin.
     *
     * @param selectedJoin selectedJoin
     */
    public void setSelectedJoin(String selectedJoin) {
        this.selectedJoin = selectedJoin;
    }

    /**
     * Get selectedKeyA.
     *
     * @return selectedKeyA
     */
    public String getSelectedKeyA() {
        return selectedKeyA;
    }

    /**
     * Set selectedKeyA.
     *
     * @param selectedKeyA selectedKeyA
     */
    public void setSelectedKeyA(String selectedKeyA) {
        this.selectedKeyA = selectedKeyA;
    }

    /**
     * Get selectedKeyB.
     *
     * @return selectedKeyB
     */
    public String getSelectedKeyB() {
        return selectedKeyB;
    }

    /**
     * Set selectedKeyB.
     *
     * @param selectedKeyB selectedKeyB
     */
    public void setSelectedKeyB(String selectedKeyB) {
        this.selectedKeyB = selectedKeyB;
    }

    /**
     * Get tableAKeys.
     *
     * @return tableAKeys
     */
    public List<String> getTableAKeys() {
        return tableAKeys;
    }

    /**
     * Set tableAKeys.
     *
     * @param tableAKeys tableAKeys
     */
    public void setTableAKeys(List<String> tableAKeys) {
        this.tableAKeys = tableAKeys;
    }

    /**
     * Get tableBKeys.
     *
     * @return tableBKeys
     */
    public List<String> getTableBKeys() {
        return tableBKeys;
    }

    /**
     * Set tableBKeys.
     *
     * @param tableBKeys tableBKeys
     */
    public void setTableBKeys(List<String> tableBKeys) {
        this.tableBKeys = tableBKeys;
    }

}
