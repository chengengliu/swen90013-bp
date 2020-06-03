package org.apromore.plugin.utils;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Row;

/**
 * A class to test TableUtils.
 */
public class TableUtilsTest {

    /**
     * Returns a return list of lists for a grid.
     * @param rows the number of rows in the grid.
     * @param cols the number of columns in the grid.
     * @return A list of lists to populate the grid with.
     */
    public static List<List<String>> getRandomGridList(int rows, int cols) {
        List<List<String>> exampleList = new ArrayList<>();
        int count = 0;

        //Create a sample list of lists
        for (int i = 0; i < rows; i++) {
            List<String> row = new ArrayList();
            exampleList.add(row);
            for (int j = 0; j < cols; j++) {
                if (i == 0) {
                    row.add("Sample Data");
                } else {
                    row.add(Integer.toString(count++));
                }
            }
        }

        return exampleList;
    }

    /**
     * Tests whether the created grid has the same amount of cells as items in the list.
     */
    @Test
    public void populateGridTest() {
        int rows = 11;
        int cols = 5;
        Grid g = new Grid();
        TableUtils.populateGrid(g, getRandomGridList(rows, cols));

        int gridcells = 0;
        Assert.assertEquals(g.getChildren().size(), 2);

        for (Component child: g.getChildren()) {
            if (child instanceof Columns) {
                Assert.assertEquals(child.getChildren().size(), cols);
                gridcells += child.getChildren().size();
            } else {
                Assert.assertEquals(child.getChildren().size(), rows - 1);
                for (Component row: child.getChildren()) {
                    Assert.assertEquals(row.getClass(), Row.class);
                    Assert.assertEquals(row.getChildren().size(), cols);
                    gridcells += row.getChildren().size();
                }
            }
        }

        Assert.assertEquals(gridcells, rows * cols);
    }

}
