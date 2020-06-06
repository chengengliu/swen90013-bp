package org.apromore.plugin.models;

import org.apache.commons.io.FilenameUtils;

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

    /**
     * Constructor.
     */
    public JoinQueryModel() {
        tableAKeys = new ArrayList<String>();
        tableBKeys = new ArrayList<String>();
    }

    /**
     * Send the join query to Impala. Not currently implemented.
     */
    public List<String> submit() {
        List<String> tableQuery = new ArrayList<>();
        tableQuery.add(FilenameUtils.removeExtension(selectedTableA));
        tableQuery.add(selectedKeyA);
        tableQuery.add(FilenameUtils.removeExtension(selectedTableB));
        tableQuery.add(selectedKeyB);
        tableQuery.add(selectedJoin);

        System.out.println("Query:" +
                selectedTableA + " " +
                selectedTableB + " " +
                selectedJoin + " " +
                selectedKeyA + " " +
                selectedKeyB
        );

        return tableQuery;
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
