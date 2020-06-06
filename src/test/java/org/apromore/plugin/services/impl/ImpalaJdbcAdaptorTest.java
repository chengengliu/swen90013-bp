package org.apromore.plugin.services.impl;

import java.io.File;
import java.sql.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.*;

/**
 * Test class for ImpalaJdbcAdaptor.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ ImpalaJdbcAdaptor.class })
public class ImpalaJdbcAdaptorTest {
    private ImpalaJdbcAdaptor impalaJdbc;
    private Statement statement;
    private String dataPath = "TEST_DATA_PATH";

    /**
     * Setup for tests.
     * mock the createTable, getStatement and getColumnsFrom methods,
     * inject a fake dataPath into impalaJdbc
     * and mock a statement instance
     */
    @Before
    public void setup() {
        impalaJdbc = createPartialMock(ImpalaJdbcAdaptor.class, "createTable",
            "getStatement", "getColumnsFrom");

        Whitebox.setInternalState(impalaJdbc, "dataPath", dataPath);

        statement = createMock(Statement.class);
    }

    /**
     * Test private method createTable.
     */
    @Test
    public void testCreateTable() throws Exception {
        // Testing the createTable() method,
        // Need the method not to be a mock
        // So create a new impalaJdbc
        ImpalaJdbcAdaptor impalaJdbc = createPartialMock(
            ImpalaJdbcAdaptor.class,
            "getStatement"
        );

        String createStatement = "createStatement";
        String tableName = "testFile";

        expectPrivate(impalaJdbc, "getStatement").andReturn(statement);
        expect(statement.execute("DROP TABLE IF EXISTS " + tableName))
            .andReturn(true);
        expect(statement.execute(createStatement)).andReturn(true);

        replayAll();
        Whitebox.invokeMethod(impalaJdbc, "createTable", createStatement,
            tableName);
        verifyAll();
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
        impalaJdbc.createParquetTable(testTableName, testFileName);
        verifyAll();
    }

    /**
     * Test createCsvTable method.
     */
    @Test
    public void testCreateCsvTable() throws Exception {
        String columns = "`str` STRING, `int` INT, `bool` BOOLEAN, ";
        expectPrivate(impalaJdbc, "getColumnsFrom", anyObject(File.class))
            .andReturn(columns);

        String testTableName = "testTable";
        String testFileName = testTableName + ".csv";
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

        expectPrivate(impalaJdbc, "getStatement").andReturn(statement);

        String statement3 = String.format(
            "INSERT OVERWRITE TABLE `%s` SELECT * FROM `%s`",
            testTableName,
            testTableName + "_csv"
        );
        expect(statement.execute(statement3)).andReturn(true);

        String statement4 = String.format(
            "DROP TABLE IF EXISTS `%s`",
            testTableName + "_csv"
        );
        expect(statement.execute(statement4)).andReturn(true);

        replayAll();
        impalaJdbc.createCsvTable(testTableName, testFileName);
        verifyAll();
    }
}
