package org.apromore.plugin.services.impl;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import static org.easymock.EasyMock.anyObject;
import static org.powermock.api.easymock.PowerMock.*;

/**
 * Test class for ImpalaJdbcAdaptor.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ ImpalaJdbcAdaptor.class, ImpalaTable.class })
public class ImpalaTableTest {
    private ImpalaJdbcAdaptor impalaJdbc;
    private ImpalaTable impalaTable;
    private String dataPath = "TEST_DATA_PATH";

    /**
     * Setup for tests.
     * mock ImpalaJdbcAdaptor and ImpalaTable instances,
     * inject a fake dataPath into impalaTable
     */
    @Before
    public void setup() {
        impalaTable = createPartialMock(ImpalaTable.class, "getColumnsFrom");
        impalaJdbc = createMock(ImpalaJdbcAdaptor.class);

        Whitebox.setInternalState(impalaTable, "impalaJdbcAdaptor", impalaJdbc);
        Whitebox.setInternalState(impalaTable, "dataPath", dataPath);
    }

    /**
     * Test createParquetTable method.
    */
    @Test
    public void testCreateParquetTable() throws Exception {
        String testTableName = "testTable";
        String testFileName = testTableName + ".parquet";
        String dir = dataPath + "/" + testTableName;
        String createStatement = String.format(
            "CREATE EXTERNAL TABLE `%s` " +
            "LIKE PARQUET '%s' " +
            "STORED AS PARQUET " +
            "LOCATION '%s'",
            testTableName,
            dir + "/" + testFileName,
            dir
        );

        expectPrivate(impalaJdbc, "createTable", createStatement,
            testTableName);

        replayAll();
        impalaTable.createParquetTable(testTableName, testFileName);
        verifyAll();
    }

    /**
     * Test createCsvTable method.
    */
    @Test
    public void testCreateCsvTable() throws Exception {
        String columns = "`str` STRING, `int` INT, `bool` BOOLEAN, ";
        expectPrivate(impalaTable, "getColumnsFrom", anyObject(File.class))
            .andReturn(columns);

        String testTableName = "testTable";
        String dir = dataPath + "/" + testTableName + "_csv";

        String statement1 = String.format(
            "CREATE EXTERNAL TABLE `%s` (%s) " +
            "ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' " +
            "LINES TERMINATED BY '\n' " +
            "STORED AS TEXTFILE " +
            "LOCATION '%s' " +
            "TBLPROPERTIES('skip.header.line.count'='1')",

            testTableName + "_csv",
            columns.substring(0, columns.length() - 2),
            dir
        );
        expectPrivate(impalaJdbc, "createTable", statement1,
            testTableName + "_csv");

        String statement2 = String.format(
            "CREATE EXTERNAL TABLE `%s` " +
            "LIKE `%s` " +
            "STORED AS PARQUET " +
            "LOCATION '%s'",

            testTableName,
            testTableName + "_csv",
            dataPath + "/" + testTableName
        );
        expectPrivate(
            impalaJdbc,
            "createTable",
            statement2,
            testTableName
        );

        String statement3 = String.format(
            "INSERT OVERWRITE TABLE `%s` SELECT * FROM `%s`",
            testTableName,
            testTableName + "_csv"
        );
        impalaJdbc.execute(statement3);
        expectLastCall();

        String statement4 = String.format(
            "DROP TABLE IF EXISTS `%s`",
            testTableName + "_csv"
        );
        impalaJdbc.execute(statement4);
        expectLastCall();

        String testFileName = testTableName + ".csv";
        replayAll();
        impalaTable.createCsvTable(testTableName, testFileName);
        verifyAll();
    }
}
