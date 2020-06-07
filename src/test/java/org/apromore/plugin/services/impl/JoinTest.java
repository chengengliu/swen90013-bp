package org.apromore.plugin.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.apromore.plugin.PluginConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Unit Test for the Join Tables.
 */
@ContextConfiguration(classes = PluginConfig.class)
@RunWith(SpringRunner.class)
public class JoinTest {
    @Autowired
    Join joinTable;

    /**
     * Create the input parameter for the test case.
     *
     * @param tablePairs List of join table inputs
     * @return List of List for JoinTable input
     */
    private List<List<String>> createInput(String[] tablePairs) {
        List<List<String>> input = new ArrayList<>();

        for (int i = 0; i < tablePairs.length; i++) {

            List<String> inputRow = new ArrayList<>();
            String [] columns = tablePairs[i].split(",");

            for (int j = 0; j < columns.length; j++) {
                inputRow.add(columns[j]);
            }
            input.add(inputRow);
        }

        return input;
    }

    /**
     * Join 2 tables with different types of join.
     */
    @Test
    public void joinTwoTables() {
        String [] testInput1 =  {"Table1,key1,Table2,key2,INNER JOIN"};
        String [] testInput2 =  {"Table1,key1,Table2,key2,LEFT JOIN"};
        String [] testInput3 =  {"Table1,key1,Table2,key2,RIGHT JOIN"};
        String [] testInput4 =  {"Table1,key1,Table2,key2,FULL OUTER JOIN"};
        String expectedTest1 = "`Table1` INNER JOIN `Table2` ON " +
                "`Table1`.`key1`=`Table2`.`key2` ";
        String expectedTest2 = "`Table1` LEFT JOIN `Table2` ON " +
                "`Table1`.`key1`=`Table2`.`key2` ";
        String expectedTest3 = "`Table1` RIGHT JOIN `Table2` ON " +
                "`Table1`.`key1`=`Table2`.`key2` ";
        String expectedTest4 = "`Table1` FULL OUTER JOIN `Table2` ON " +
                "`Table1`.`key1`=`Table2`.`key2` ";

        Assert.assertEquals(
                expectedTest1,
                joinTable.getJoinString(createInput(testInput1))
        );

        Assert.assertEquals(
                expectedTest2,
                joinTable.getJoinString(createInput(testInput2))
        );

        Assert.assertEquals(
                expectedTest3,
                joinTable.getJoinString(createInput(testInput3))
        );

        Assert.assertEquals(
                expectedTest4,
                joinTable.getJoinString(createInput(testInput4))
        );
    }

    /**
     * Join 3 Tables with different types of joins.
     */
    @Test
    public void joinThreeTables() {
        String [] testInput1 = {
            "Table1,key1,Table2,key2,INNER JOIN",
            "Table2,key22,Table3,key3,INNER JOIN"
        };
        String [] testInput2 = {
            "Table1,key1,Table2,key2,LEFT JOIN",
            "Table2,key22,Table3,key3,RIGHT JOIN"
        };
        String [] testInput3 = {
            "Table1,key1,Table2,key2,FULL OUTER JOIN",
            "Table2,key22,Table3,key3,LEFT JOIN"
        };
        String [] testInput4 = {
            "Table1,key1,Table2,key2,INNER JOIN",
            "Table2,key22,Table3,key3,LEFT JOIN"
        };
        String expectedTest1 = "`Table1` INNER JOIN `Table2` ON " +
            "`Table1`.`key1`=`Table2`.`key2` " +
            "INNER JOIN `Table3` ON `Table2`.`key22`=`Table3`.`key3` ";
        String expectedTest2 = "`Table1` LEFT JOIN `Table2` ON " +
            "`Table1`.`key1`=`Table2`.`key2` " +
            "RIGHT JOIN `Table3` ON `Table2`.`key22`=`Table3`.`key3` ";
        String expectedTest3 = "`Table1` FULL OUTER JOIN `Table2` ON " +
            "`Table1`.`key1`=`Table2`.`key2` " +
            "LEFT JOIN `Table3` ON `Table2`.`key22`=`Table3`.`key3` ";
        String expectedTest4 = "`Table1` INNER JOIN `Table2` ON " +
            "`Table1`.`key1`=`Table2`.`key2` "+
            "LEFT JOIN `Table3` ON `Table2`.`key22`=`Table3`.`key3` ";

        Assert.assertEquals(
                expectedTest1,
                joinTable.getJoinString(createInput(testInput1))
        );

        Assert.assertEquals(
                expectedTest2,
                joinTable.getJoinString(createInput(testInput2))
        );

        Assert.assertEquals(
                expectedTest3,
                joinTable.getJoinString(createInput(testInput3))
        );

        Assert.assertEquals(
                expectedTest4,
                joinTable.getJoinString(createInput(testInput4))
        );
    }

    /**
     * Join 4 tables for different types of joins.
     */
    @Test
    public void joinFourTables() {
        String [] testInput1 = {
            "Table1,key1,Table2,key2,LEFT JOIN",
            "Table2,key22,Table3,key3,INNER JOIN",
            "Table3,key32,Table4,key4,RIGHT JOIN"
        };
        String expectedTest1 = "`Table1` LEFT JOIN `Table2` ON " +
            "`Table1`.`key1`=`Table2`.`key2` " +
            "INNER JOIN `Table3` ON `Table2`.`key22`=`Table3`.`key3` " +
            "RIGHT JOIN `Table4` ON `Table3`.`key32`=`Table4`.`key4` ";

        Assert.assertEquals(
                expectedTest1,
                joinTable.getJoinString(createInput(testInput1))
        );
    }
}
