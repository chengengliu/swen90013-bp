package org.apromore.plugin.eventHandlers;

import org.apromore.plugin.utils.TableUtils;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;

import java.util.List;

public class TableIconDiv extends Div {

    private List<List<String>> resultsList = null;

    public TableIconDiv(List<List<String>> list) {
        this.resultsList = list;
    }

    public void onClick(Event event) {
        //Get the grid
        Grid grid = getGridFromEyeIcon(this);

        //Populate the grid with actual data
        TableUtils.populateGrid(grid, resultsList);
    }
}
