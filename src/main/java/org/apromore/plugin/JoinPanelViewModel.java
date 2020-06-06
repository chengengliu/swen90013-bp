package org.apromore.plugin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apromore.plugin.models.JoinQueryModel;
import org.apromore.plugin.services.FileHandlerService;
import org.apromore.plugin.services.Transaction;
import org.zkoss.bind.BindUtils;
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
        joins.add("INNER JOIN");
        joins.add("LEFT JOIN");
        joins.add("RIGHT JOIN");
        joins.add("FULL OUTER JOIN");
        joinQueryModels.add(new JoinQueryModel());
    }

    /**
     * onSelect command for table A being selected.
     * @param index Index of join query model.
     */
    @Command("onTableASelected")
    @NotifyChange("joinQueryModels")
    public void onTableASelected(@BindingParam("index") int index) {
        String selectedTableAName =
                joinQueryModels.get(index).getSelectedTableA();
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
     * @param index Index of join query model.
     */
    @Command("onTableBSelected")
    @NotifyChange("joinQueryModels")
    public void onTableBSelected(@BindingParam("index") int index) {
        String selectedTableBName =
                joinQueryModels.get(index).getSelectedTableB();
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
     * Submit join query.
     *
     * @param index Index of join query model
     */
    @Command("submitQuery")
    public void submitQuery(@BindingParam("index") int index) {

        List<String> joinTableInfo = joinQueryModels.get(index).submit();
        List<List<String>> joinInfo = new ArrayList<>();

        joinInfo.add(joinTableInfo);

        List<List<String>> resultsList = null;
        try {

            resultsList = transactionService.join(joinInfo, 10);
            Map<String,Object> args = new HashMap<String,Object>();
            args.put("resultsList", resultsList);
            BindUtils.postGlobalCommand(null, null, "onTableClick", args);

        } catch (SQLException e) {

            e.printStackTrace();
        }

        String text = "";

        for (List<String> rowList : resultsList) {
            for (String rowVal : rowList) {
                text += rowVal + ", ";
            }
            text += "\n";
        }

        System.out.println(text);
    }

    /**
     * Update list of file names. Can be called globally.
     *
     * @param filenames list of uploaded file names
     */
    @GlobalCommand
    @NotifyChange("filenames")
    public void newFileUpload(@BindingParam("filenames")
        List<String> filenames) {
        this.filenames = filenames;
    }

    /**
     * Get joinQueryModels.
     *
     * @return joinQueryModels joinQueryModels
     */
    public List<JoinQueryModel> getJoinQueryModels() {
        return joinQueryModels;
    }

    /**
     * Set joinQueryModels.
     *
     * @param joinQueryModels joinQueryModels
     */
    public void setJoinQueryModels(List<JoinQueryModel> joinQueryModels) {
        this.joinQueryModels = joinQueryModels;
    }

    /**
     * Get joins.
     *
     * @return joins joins
     */
    public List<String> getJoins() {
        return joins;
    }

    /**
     * Set joins.
     *
     * @param joins joins
     */
    public void setJoins(List<String> joins) {
        this.joins = joins;
    }

    /**
     * Get filenames.
     *
     * @return filenames filenames
     */
    public List<String> getFilenames() {
        return filenames;
    }

    /**
     * Set filenames.
     *
     * @param filenames filenames
     */
    public void setFilenames(List<String> filenames) {
        this.filenames = filenames;
    }
}
