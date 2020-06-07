package org.apromore.plugin.services.impl;

import org.apromore.plugin.PluginConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

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
        String[] TestInput1 =  {"Table1,key1,Table2,key2,INNER JOIN"};
        String[] TestInput2 =  {"Table1,key1,Table2,key2,LEFT JOIN"};
        String[] TestInput3 =  {"Table1,key1,Table2,key2,RIGHT JOIN"};
        String[] TestInput4 =  {"Table1,key1,Table2,key2,FULL OUTER JOIN"};
        String ExpectedTest1 = "`Table1` INNER JOIN `Table2` ON " +
                "`Table1`.`key1`=`Table2`.`key2` ";
        String ExpectedTest2 = "`Table1` LEFT JOIN `Table2` ON " +
                "`Table1`.`key1`=`Table2`.`key2` ";
        String ExpectedTest3 = "`Table1` RIGHT JOIN `Table2` ON " +
                "`Table1`.`key1`=`Table2`.`key2` ";
        String ExpectedTest4 = "`Table1` FULL OUTER JOIN `Table2` ON " +
                "`Table1`.`key1`=`Table2`.`key2` ";

        Assert.assertEquals(
                ExpectedTest1,
                joinTable.getJoinString(createInput(TestInput1))
        );

        Assert.assertEquals(
                ExpectedTest2,
                joinTable.getJoinString(createInput(TestInput2))
        );

        Assert.assertEquals(
                ExpectedTest3,
                joinTable.getJoinString(createInput(TestInput3))
        );

        Assert.assertEquals(
                ExpectedTest4,
                joinTable.getJoinString(createInput(TestInput4))
        );
    }

    /**
     * Join 3 Tables with different types of joins.
     */
    @Test
    public void joinThreeTables() {
        String[] TestInput1 = {
                "Table1,key1,Table2,key2,INNER JOIN",
                "Table2,key22,Table3,key3,INNER JOIN"};
        String[] TestInput2 = {
                "Table1,key1,Table2,key2,LEFT JOIN",
                "Table2,key22,Table3,key3,RIGHT JOIN"};

        String[] TestInput3 = {
                "Table1,key1,Table2,key2,FULL OUTER JOIN",
                "Table2,key22,Table3,key3,LEFT JOIN"};

        String[] TestInput4 = {
                "Table1,key1,Table2,key2,INNER JOIN",
                "Table2,key22,Table3,key3,LEFT JOIN"};

        String ExpectedTest1 = "`Table1` INNER JOIN `Table2` ON " +
                "`Table1`.`key1`=`Table2`.`key2` " +
                "INNER JOIN `Table3` ON `Table2`.`key22`=`Table3`.`key3` ";

        String ExpectedTest2 = "`Table1` LEFT JOIN `Table2` ON " +
                "`Table1`.`key1`=`Table2`.`key2` "+
                "RIGHT JOIN `Table3` ON `Table2`.`key22`=`Table3`.`key3` ";

        String ExpectedTest3 = "`Table1` FULL OUTER JOIN `Table2` ON " +
                "`Table1`.`key1`=`Table2`.`key2` "+
                "LEFT JOIN `Table3` ON `Table2`.`key22`=`Table3`.`key3` ";

        String ExpectedTest4 = "`Table1` INNER JOIN `Table2` ON " +
                "`Table1`.`key1`=`Table2`.`key2` "+
                "LEFT JOIN `Table3` ON `Table2`.`key22`=`Table3`.`key3` ";

        Assert.assertEquals(
                ExpectedTest1,
                joinTable.getJoinString(createInput(TestInput1))
        );

        Assert.assertEquals(
                ExpectedTest2,
                joinTable.getJoinString(createInput(TestInput2))
        );

        Assert.assertEquals(
                ExpectedTest3,
                joinTable.getJoinString(createInput(TestInput3))
        );

        Assert.assertEquals(
                ExpectedTest4,
                joinTable.getJoinString(createInput(TestInput4))
        );
    }


    /**
     * Join 4 tables for different types of joins.
     */
    @Test
    public void joinFourTables() {
        String[] TestInput1 = {
                "Table1,key1,Table2,key2,LEFT JOIN",
                "Table2,key22,Table3,key3,INNER JOIN",
                "Table3,key32,Table4,key4,RIGHT JOIN"
        };

        String ExpectedTest1 = "`Table1` LEFT JOIN `Table2` ON " +
                "`Table1`.`key1`=`Table2`.`key2` " +
                "INNER JOIN `Table3` ON `Table2`.`key22`=`Table3`.`key3` " +
                "RIGHT JOIN `Table4` ON `Table3`.`key32`=`Table4`.`key4` ";

        Assert.assertEquals(
                ExpectedTest1,
                joinTable.getJoinString(createInput(TestInput1))
        );
    }
}
