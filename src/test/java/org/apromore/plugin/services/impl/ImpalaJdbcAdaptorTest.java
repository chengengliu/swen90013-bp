package org.apromore.plugin.services.impl;

import java.sql.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
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

    /**
     * Setup for tests.
     * mock the getStatement method, and mock a statement instance
     */
    @Before
    public void setup() {
        impalaJdbc = createPartialMock(ImpalaJdbcAdaptor.class, "getStatement");
        statement = createMock(Statement.class);
    }

    /**
     * Test private method createTable.
     */
    @Test
    public void testCreateTable() throws Exception {
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
}
