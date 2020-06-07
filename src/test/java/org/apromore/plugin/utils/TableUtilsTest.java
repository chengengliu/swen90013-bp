package org.apromore.plugin.utils;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Popup;
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
            List<String> row = new ArrayList<>();
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
     * Tests whether the created grid has the same amount of
     * cells as items in the list.
     */
    @Test
    public void populateGridNoNullParamsTest() {
        int rows = 11;
        int cols = 5;
        Grid grid = new Grid();
        //Add +1 to row for header
        TableUtils.populateGrid(grid, getRandomGridList(rows + 1, cols));

        int gridcells = 0;
        Assert.assertEquals(grid.getChildren().size(), 2);

        for (Component child: grid.getChildren()) {
            if (child instanceof Columns) {
                //Headers
                Assert.assertEquals(child.getChildren().size(), cols);
            } else {
                //Cell contents
                Assert.assertEquals(child.getChildren().size(), rows);
                for (Component row: child.getChildren()) {
                    Assert.assertEquals(row.getClass(), Row.class);
                    Assert.assertEquals(row.getChildren().size(), cols);
                    gridcells += row.getChildren().size();
                }
            }
        }

        Assert.assertEquals(gridcells, rows * cols);
    }

    /**
     * Tests grid contains only a single cell when list is null.
     */
    @Test
    public void populateGridNullListTest() {
        Grid grid = new Grid();
        TableUtils.populateGrid(grid, null);
        Assert.assertEquals(grid.getChildren().size(), 2);

        Component cols = grid.getChildren().get(0);
        Component rows = grid.getChildren().get(1);
        Assert.assertEquals(cols.getChildren().size(), 1);
        Assert.assertEquals(rows.getChildren().size(), 0);
    }

    /**
     * Tests the label value is returned when the UI components
     * have the correct layout.
     */
    @Test
    public void getFilenameFromGridCorrectFormatTest() {
        Grid grid = new Grid();
        Div parent = new Div();
        parent.appendChild(grid);

        Popup grandParent = new Popup();
        grandParent.appendChild(parent);

        Hlayout greatGrandParent = new Hlayout();
        Label fileLabel = new Label("getFilenameFromGridCorrectFormatTest");

        greatGrandParent.appendChild(grandParent);
        greatGrandParent.appendChild(fileLabel);

        Assert.assertEquals(TableUtils.getFilenameFromGrid(grid),
                "getFilenameFromGridCorrectFormatTest");
    }

    /**
     * Tests null is returned when no label is found.
     */
    @Test
    public void getFilenameFromGridNoLabelTest() {
        Grid grid = new Grid();
        Div parent = new Div();
        Popup grandParent = new Popup();
        Hlayout greatGrandParent = new Hlayout();

        greatGrandParent.appendChild(grandParent);
        grandParent.appendChild(parent);
        parent.appendChild(grid);

        Assert.assertEquals(TableUtils.getFilenameFromGrid(grid),
                null);
    }

    /**
     * Tests null is returned when not enough parents are present.
     */
    @Test
    public void getFilenameFromGridNotEnoughParentsTest() {
        Grid grid = new Grid();
        Div parent = new Div();
        Popup grandParent = new Popup();

        grandParent.appendChild(parent);
        parent.appendChild(grid);

        Assert.assertEquals(TableUtils.getFilenameFromGrid(grid),
                null);
    }
}
