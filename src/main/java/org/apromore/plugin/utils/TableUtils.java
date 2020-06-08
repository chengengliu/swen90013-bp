package org.apromore.plugin.utils;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.*;

/**
 * The purpose of this class is to populate tables from the UI.
 */
public class TableUtils {

    /**
     * Populates a grid with a list of lists.
     * @param grid the grid to populate.
     * @param gridData the data to populate the grid with.
     */
    public static void populateGrid(Grid grid, List<List<String>> gridData) {
        boolean firstRow = true;

        if (grid == null) {
            System.out.println("Grid is null");
            return;
        }

        grid.getChildren().clear();
        Columns cols = new Columns();
        Rows rows = new Rows();
        grid.appendChild(cols);
        grid.appendChild(rows);

        if (gridData == null) {
            System.out.println("List is null");
            Column col = new Column("No Data Found");
            cols.appendChild(col);
            return;
        }

        for (List<String> row:gridData) {
            if (firstRow) {

                // populate columns with headers
                for (String cell: row) {
                    Column col = new Column(cell);
                    col.setSclass("columns-class");
                    col.setAlign("center");
                    cols.appendChild(col);
                }

                firstRow = false;

            } else {
                // create new row
                Row tableRow = new Row();
                rows.appendChild(tableRow);

                // populate row cells
                for (String cell: row) {

                    Cell rowCell = new Cell();
                    rowCell.setSclass("row-cell");
                    rowCell.appendChild(new Label(cell));
                    tableRow.appendChild(rowCell);

                }
            }
        }
        grid.setSizedByContent(true);
    }

    /**
     * Get the filename from the label in UI based on the position of the grid.
     * @param g A grid that shows input snippets.
     * @return the filename of the data shown in the grid.
     */
    public static String getFilenameFromGrid(Grid g) {
        //Return null if not enough parents
        if (g.getParent() == null || g.getParent().getParent() == null ||
                g.getParent().getParent().getParent() == null) {
            return null;
        }

        for (Component child: g.getParent().getParent().getParent()
                .getChildren()) {
            if (child instanceof Label) {
                Label label = (Label)child;
                return label.getValue();
            }
        }
        return null;
    }

}
