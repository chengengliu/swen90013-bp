package org.apromore.plugin.services.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

/**
 * Test class for TransactionImpl.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ ImpalaJdbcAdaptor.class })
public class TransactionImplTest {
    private TransactionImpl transaction;
    private ImpalaJdbcAdaptor impalaJdbc;

    /**
     * Setup for tests.
     * Initialize transaction, mock an impalaJbdc instance
     * and inject mock impalaJbdc into the transaction
     */
    @Before
    public void setup() {
        transaction = new TransactionImpl();
        impalaJdbc = PowerMock.createMock(ImpalaJdbcAdaptor.class);
        Whitebox.setInternalState(transaction, "impalaJdbc", impalaJdbc);
    }

    /**
     * Test addTable method for parquet files.
     * @throws IOException
     * @throws SQLException
     */
    @Test
    public void testAddParquetTable() throws IOException, SQLException {
        String tableName = "testFile";
        String fileName = tableName + ".parquet";

        impalaJdbc.createParquetTable(tableName, fileName);
        EasyMock.expectLastCall();

        EasyMock.replay(impalaJdbc);
        transaction.addTable(fileName);
        EasyMock.verify(impalaJdbc);
    }

    /**
     * Test addTable method for csv files.
     * @throws IOException
     * @throws SQLException
     */
    @Test
    public void testAddTableForCsv() throws IOException, SQLException {
        String tableName = "testFile";
        String fileName = tableName + ".csv";

        impalaJdbc.createCsvTable(tableName, fileName);
        EasyMock.expectLastCall();

        EasyMock.replay(impalaJdbc);
        transaction.addTable(fileName);
        EasyMock.verify(impalaJdbc);
    }

    /**
     * Test getSnippet method.
     * @throws IOException
     * @throws SQLException
     */
    @Test
    public void testGetSnippet() throws IOException, SQLException {
        String tableName = "testFile";
        String fileName = tableName + ".parquet";
        int limit = 2147483647;
        List<List<String>> snippet = null;

        EasyMock.expect(
            impalaJdbc.executeQuery(
                "SELECT * FROM " + tableName + " LIMIT " + limit
            )
        ).andReturn(snippet);

        EasyMock.replay(impalaJdbc);
        transaction.getSnippet(fileName, limit);
        EasyMock.verify(impalaJdbc);
    }

    /**
     * Test addTableGetSnippet method.
     * @throws IOException
     * @throws SQLException
     */
    @Test
    public void testAddTableGetSnippet() throws IOException, SQLException {
        String tableName = "testFile";
        String fileName = tableName + ".parquet";
        int limit = 2147483647;
        List<List<String>> snippet = null;

        impalaJdbc.createParquetTable(tableName, fileName);
        EasyMock.expectLastCall();

        EasyMock.expect(
            impalaJdbc.executeQuery("SELECT * FROM " + tableName + " LIMIT " + limit)
        ).andReturn(snippet);

        EasyMock.replay(impalaJdbc);
        transaction.addTableGetSnippet(fileName, limit);
        EasyMock.verify(impalaJdbc);
    }
}
