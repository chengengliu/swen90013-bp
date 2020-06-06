package org.apromore.plugin.services.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.powermock.api.easymock.PowerMock.*;

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
        impalaJdbc = createMock(ImpalaJdbcAdaptor.class);
        Whitebox.setInternalState(transaction, "impalaJdbc", impalaJdbc);
    }

    /**
     * Test addTable method for parquet files.
     */
    @Test
    public void testAddParquetTable() throws IOException, SQLException {
        String tableName = "testFile";
        String fileName = tableName + ".parquet";

        impalaJdbc.createParquetTable(tableName, fileName);
        expectLastCall();

        replayAll();
        transaction.addTable(fileName);
        verifyAll();
    }

    /**
     * Test addTable method for csv files.
     */
    @Test
    public void testAddTableForCsv() throws IOException, SQLException {
        String tableName = "testFile";
        String fileName = tableName + ".csv";

        impalaJdbc.createCsvTable(tableName, fileName);
        expectLastCall();

        replayAll();
        transaction.addTable(fileName);
        verifyAll();
    }

    /**
     * Test getSnippet method.
     */
    @Test
    public void testGetSnippet() throws IOException, SQLException {
        String tableName = "testFile";
        String fileName = tableName + ".parquet";
        int limit = 2147483647;
        List<List<String>> snippet = null;

        expect(impalaJdbc.executeQuery(
            "SELECT * FROM " + tableName + " LIMIT " + limit
        )).andReturn(snippet);

        replayAll();
        transaction.getSnippet(fileName, limit);
        verifyAll();
    }

    /**
     * Test addTableGetSnippet method.
     */
    @Test
    public void testAddTableGetSnippet() throws IOException, SQLException {
        String tableName = "testFile";
        String fileName = tableName + ".parquet";
        int limit = 2147483647;
        List<List<String>> snippet = null;

        impalaJdbc.createParquetTable(tableName, fileName);
        expectLastCall();

        expect(impalaJdbc.executeQuery(
            "SELECT * FROM " + tableName + " LIMIT " + limit
        )).andReturn(snippet);

        replayAll();
        transaction.addTableGetSnippet(fileName, limit);
        verifyAll();
    }
}
