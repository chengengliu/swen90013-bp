package org.apromore.plugin.services.impl;

import java.sql.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.*;

/**
 * Test class for ImpalaJdbcAdaptor.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ DriverManager.class, ImpalaJdbcAdaptor.class })
public class ImpalaJdbcAdaptorTest {
    private ImpalaJdbcAdaptor impalaJdbc;
    private Connection connection;
    private Statement statement;

    /**
     * Setup for tests.
     * mock the getStatement method, and mock a statement instance
     */
    @Before
    public void setup() {
        mockStatic(DriverManager.class);
        impalaJdbc = createPartialMock(ImpalaJdbcAdaptor.class, "executeQuery");
        connection = createNiceMock(Connection.class);
        statement = createNiceMock(Statement.class);
    }

    /**
     * Test method execute.
     */
    @Test
    public void testExecute() throws Exception {
        String queryStatement = "queryStatement";

        expect(DriverManager.getConnection(anyString())).andReturn(connection);
        expect(connection.createStatement()).andReturn(statement);

        expect(statement.execute(queryStatement)).andReturn(true);

        replayAll();
        impalaJdbc.execute(queryStatement);
        verifyAll();
    }

    /**
     * Test method createTable.
     */
    @Test
    public void testCreateTable() throws Exception {
        String createStatement = "createStatement";
        String tableName = "testFile";

        expect(DriverManager.getConnection(anyString())).andReturn(connection);
        expect(connection.createStatement()).andReturn(statement);

        expect(statement.execute("DROP TABLE IF EXISTS " + tableName))
            .andReturn(true);
        expect(statement.execute(createStatement)).andReturn(true);

        replayAll();
        impalaJdbc.createTable(createStatement, tableName);
        verifyAll();
    }
}
