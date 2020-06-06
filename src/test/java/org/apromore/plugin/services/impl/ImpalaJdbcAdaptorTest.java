package org.apromore.plugin.services.impl;

import java.io.File;
import java.sql.*;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

/**
 * Test class for ImpalaJdbcAdaptor
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ ImpalaJdbcAdaptor.class })
public class ImpalaJdbcAdaptorTest {
    private ImpalaJdbcAdaptor impalaJdbc;
    private Statement statement;
    private String dataPath = "TEST_DATA_PATH";

    /**
     * Setup for tests
     * mock the createTable, getStatement and getColumnsFrom methods,
     * inject a fake dataPath into impalaJdbc
     * and mock a statement instance
     */
    @Before
    public void setup() {
        impalaJdbc = PowerMock.createPartialMock(
            ImpalaJdbcAdaptor.class,
            "createTable",
            "getStatement", 
            "getColumnsFrom"
        );

        Whitebox.setInternalState(impalaJdbc, "dataPath", dataPath);

        statement = PowerMock.createMock(Statement.class);
    }

    /**
     * Test private method createTable
     * @throws Exception
     */
    @Test
    public void testCreateTable() throws Exception {
        // Testing the createTable() method,
        // Need the method not to be a mock
        // So create a new impalaJdbc 
        ImpalaJdbcAdaptor impalaJdbc = PowerMock.createPartialMock(
            ImpalaJdbcAdaptor.class,
            "getStatement"
        );

        String createStatement = "createStatement";
        String tableName = "testFile";
        
        PowerMock.expectPrivate(impalaJdbc, "getStatement").andReturn(statement);
        EasyMock.expect(statement.execute(
            "DROP TABLE IF EXISTS " + tableName
        )).andReturn(true);
        EasyMock.expect(statement.execute(createStatement)).andReturn(true);
    
        EasyMock.replay(impalaJdbc, statement);
        Whitebox.invokeMethod(impalaJdbc, "createTable", createStatement, tableName);
        EasyMock.verify(impalaJdbc, statement);
    }

    /**
     * Test createParquetTable method
     * @throws Exception
     */
    @Test
    public void testCreateParquetTable() throws Exception {
        String testTableName = "testTable";
        String testFileName = testTableName + ".parquet";
        String dir = dataPath + "/" + testTableName;
        
        String createStatement = "CREATE EXTERNAL TABLE `%s` " +
            "LIKE PARQUET '%s' " +
            "STORED AS PARQUET " +
            "LOCATION '%s'";

        createStatement = String.format(
            createStatement,
            testTableName,
            dir + "/" + testFileName,
            dir
        );

        PowerMock.expectPrivate(
            impalaJdbc,
            "createTable",
            createStatement,
            testTableName
        );

        EasyMock.replay(impalaJdbc);
        impalaJdbc.createParquetTable(testTableName, testFileName);
        EasyMock.verify(impalaJdbc);
    }

    /**
     * Test createCsvTable method
     * @throws Exception
     */
    @Test
    public void testCreateCsvTable() throws Exception {
        String testTableName = "testTable";
        String testFileName = testTableName + ".csv";
        String dir = dataPath + "/" + testTableName + "_csv";
        String columns = "`str` STRING, `int` INT, `bool` BOOLEAN, ";

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

        String statement2 = String.format(
            "CREATE EXTERNAL TABLE `%s` " +
            "LIKE `%s` " +
            "STORED AS PARQUET " +
            "LOCATION '%s'",
        
            testTableName,
            testTableName + "_csv",
            dataPath + "/" + testTableName
        );

        String statement3 = String.format(
            "INSERT OVERWRITE TABLE `%s` SELECT * FROM `%s`",
            testTableName,
            testTableName + "_csv"
        );
        
        String statement4 = String.format(
            "DROP TABLE IF EXISTS `%s`",
            testTableName + "_csv"
        );

        PowerMock.expectPrivate(
            impalaJdbc,
            "getColumnsFrom",
            EasyMock.anyObject(File.class)
        ).andReturn(columns);

        PowerMock.expectPrivate(
            impalaJdbc,
            "createTable",
            statement1,
            testTableName + "_csv"
        );

        PowerMock.expectPrivate(
            impalaJdbc,
            "createTable",
            statement2,
            testTableName
        );

        PowerMock.expectPrivate(impalaJdbc, "getStatement").andReturn(statement);
        EasyMock.expect(statement.execute(statement3)).andReturn(true);
        EasyMock.expect(statement.execute(statement4)).andReturn(true);

        replayAll();
        impalaJdbc.createCsvTable(testTableName, testFileName);
        verifyAll();
    }

}