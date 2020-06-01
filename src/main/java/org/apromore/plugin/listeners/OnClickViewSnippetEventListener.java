package org.apromore.plugin.listeners;

import java.util.List;

import org.apromore.plugin.utils.TableUtils;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;

/**
 * This class implements a listener which populates an input snippet
 * table when view input snippet is clicked.
 */
public class OnClickViewSnippetEventListener implements EventListener<Event> {

    List<List<String>> resultsList = null;

    /**
     * Constructor.
     * @param list the resultsList to populate a grid with
     */
    public OnClickViewSnippetEventListener(List<List<String>> list){
        this.resultsList = list;
    }

    /**
     * Defines what happens when the event occurs.
     * @param event the event which occurs.
     */
    @Override
    public void onEvent(Event event) throws Exception {

        if (event.getTarget() instanceof Div) {
            Div iconButton = (Div)event.getTarget();
            //System.out.println("Clicked "+iconButton.getId());
            //Get the grid
            Grid grid = TableUtils.getGridFromButton(iconButton);
            //System.out.println("Grid: "+grid.getId());

            //Populate the grid
            TableUtils.populateGrid(grid, resultsList);

        } else {
            //System.out.println("Event Target: "
            //+ event.getTarget().getClass().toString());
        }
    }
}
