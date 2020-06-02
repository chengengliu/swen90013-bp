package org.apromore.plugin.eventHandlers;

import java.util.List;

import org.apromore.plugin.utils.TableUtils;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;

/**
 * This class provides additional methods to handle
 * events for an EyeIconDiv.
 */
public class EyeIconDiv extends Div {
    private List<List<String>> resultsList = null;

    /**
     * Constructor.
     * @param list a list of results.
     */
    public EyeIconDiv(List<List<String>> list) {
        this.resultsList = list;
    }

    /**
     * This class defines what actions take place when onClick is called.
     * @param event An onClick event.
     */
    public void onClick(Event event) {
        //Get the grid
        Grid grid = TableUtils.getGridFromButton(this);
        //System.out.println("Grid: "+grid.getId());

        //Populate the grid with actual data
        TableUtils.populateGrid(grid, resultsList);
        //Populate the grid with sample data
        //TableUtils.populateGrid(grid, TableUtils.getRandomGridList(10, 10));
    }
}
