package org.apromore.plugin.composers;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.Init;
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
     * Retrieves table data when the eye icon is clicked.
     */
    @Listen("onClick = div#view-input-snippet")
    public void getTableData() {
        List<List<String>> exampleList = new ArrayList<List<String>>();
        int count = 0;

        //Use a sample list of lists for now
        for (int i = 0; i < 5; i++) {
            List<String> row = new ArrayList<String>();
            exampleList.add(row);
            for (int j = 0; j < 5; j++) {
                row.add(Integer.toString(count++));
            }
        }

        if (inputGrid.getChildren().size() == 0) {
            populateGrid(inputGrid, exampleList);
        }
    }

    /**
     * Populates a grid with a list of lists.
     * @param g the grid to populate.
     * @param list the data to populate the grid with.
     */
    public static void populateGrid(Grid g, List<List<String>> list) {
        boolean firstRow = true;

        Columns cols = new Columns();
        Rows rows = new Rows();
        g.appendChild(cols);
        g.appendChild(rows);

        for (List<String> row:list) {
            if (firstRow) {

                // populate columns with headers
                for (String cell: row) {
                    Column col = new Column(cell);
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
}
