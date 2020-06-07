package org.apromore.plugin.eventHandlers;

import java.util.List;

import org.apromore.plugin.utils.TableUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Popup;

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
        Grid grid = getGridFromEyeIcon(this);

        //Populate the grid with actual data
        TableUtils.populateGrid(grid, resultsList);
    }

    /**
     * Get the grid in UI based on the position of the eye icon div.
     * @param eyeIcon The button used to show snippets.
     * @return a grid which shows snippets.
     */
    private Grid getGridFromEyeIcon(EyeIconDiv eyeIcon) {

        String filename = eyeIcon.getId().substring(4,
                eyeIcon.getId().length() - 7);

        for (Component child: eyeIcon.getParent().getChildren()) {
            if (child instanceof Popup) {
                for (Component popupChild: child.getChildren()) {
                    if (popupChild.getId().equals("scrollArea" + filename)) {
                        return (Grid)popupChild.getFirstChild();
                    }
                }
            }
        }
        return null;
    }
}
