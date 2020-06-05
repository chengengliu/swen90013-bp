package org.apromore.plugin.eventHandlers;

import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Div;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableIconDiv extends Div {

    private List<List<String>> resultsList = null;
    private String textList;

    public TableIconDiv(List<List<String>> list) {
        this.resultsList = list;
    }

    public void onClick(Event event) {

        Map<String,Object> args = new HashMap<String,Object>();
        args.put("resultsList", this.resultsList);

        BindUtils.postGlobalCommand(null, null, "onTableClick", args);

    }

    public List<List<String>> getResultsList() {
        return resultsList;
    }
}
