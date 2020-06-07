package org.apromore.plugin.eventHandlers;

import org.apromore.plugin.utils.TableUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpandIconDiv extends Div {
    private List<List<String>> resultsList = null;

    /**
     * Constructor.
     * @param list a list of results.
     */
    public ExpandIconDiv(List<List<String>> list) {
        this.resultsList = list;
    }

    /**
     * This class defines what actions take place when onClick is called.
     * @param event An onClick event.
     */
    public void onClick(Event event) {
        String filename = this.getId().substring(6, this.getId().length() - 7);
        System.out.println("FILENAME: " + filename);

        Map<String,Object> args = new HashMap<String,Object>();
        args.put("resultsList", this.resultsList);
        args.put("filename", filename);
        BindUtils.postGlobalCommand(null, null, "onExpandClick", args);
    }
}
