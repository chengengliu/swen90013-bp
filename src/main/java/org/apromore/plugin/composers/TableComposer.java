package org.apromore.plugin.composers;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;

/**
 * The purpose of this class is to populate tables from the UI.
 */
public class TableComposer extends SelectorComposer<Hlayout> {

    private static final long serialVersionUID = 1L;
    @Wire
    Grid inputGrid;

    /**
     * Constructor.
     */
    public TableComposer() {

    }

    /**
     * Populates a grid with a list of lists.
     * @param g the grid to populate.
     * @param list the data to populate the grid with.
     */
    public static void populateGrid(Grid g, List<List<String>> list) {
        boolean firstRow = true;

        g.getChildren().clear();
        Columns cols = new Columns();
        Rows rows = new Rows();
        g.appendChild(cols);
        g.appendChild(rows);

        for (List<String> row:list) {
            if (firstRow) {

                // populate columns with headers
                for (String cell: row) {
                    Column col = new Column(cell);
                    col.setWidth("100px");
                    cols.appendChild(col);
                }

                firstRow = false;

            } else {
                // create new row
                Row r = new Row();
                rows.appendChild(r);

                // populate row cells
                for (String cell: row) {

                    Cell rowCell = new Cell();
                    rowCell.appendChild(new Label(cell));
                    r.appendChild(rowCell);

                }
            }
        }
    }

    /**
     * Get the filename from the label in UI based on the position of the grid.
     * @param g A grid that shows input snippets.
     * @return the filename of the data shown in the grid.
     */
    public String getFilenameFromGrid(Grid g) {
        for (Component child: g.getParent().getParent().getParent()
                .getChildren()) {
            if (child instanceof Label) {
                Label label = (Label)child;
                return label.getValue();
            }
        }
        return null;
    }

    /**
     * Get the grid in UI based on the position of the eye icon div.
     * @param button The button used to show snippets.
     * @return a grid which shows snippets.
     */
    public Grid getGridFromButton(Div button) {
        for (Component child: button.getParent().getChildren()) {
            if (child instanceof Popup) {
                Component component = child.getFirstChild().getFirstChild();
                if (component instanceof Grid) {
                    return (Grid)component;
                }
            }
        }
        return null;
    }

    /**
     * Returns a return list of lists for a grid.
     * @param rows the number of rows in the grid.
     * @param cols the number of columns in the grid.
     * @return A list of lists to populate the grid with.
     */
    public List<List<String>> getRandomGridList(int rows, int cols) {
        List<List<String>> exampleList = new ArrayList<List<String>>();
        int count = 0;

        //Use a sample list of lists for now
        for (int i = 0; i < rows; i++) {
            List<String> row = new ArrayList<String>();
            exampleList.add(row);
            for (int j = 0; j < cols; j++) {
                row.add(Integer.toString(count++));
            }
        }

        return exampleList;
    }
}
