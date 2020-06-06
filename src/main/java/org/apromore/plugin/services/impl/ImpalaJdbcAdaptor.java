package org.apromore.plugin.services.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * Impala JDBC Adaptor class to connect and execute queries.
 */
@Component
public class ImpalaJdbcAdaptor {
    // Impala connection info
    private final String connectionUrl = System.getenv("IMPALA_LINK");
    private final String jdbcDriverName = "com.cloudera.impala.jdbc41.Driver";

    /**
     * Execute the raw query commands in the Impala.
     *
     * @param query statement
     * @throws SQLException Sql failure
     */
    public void execute(String query) throws SQLException {
        try {
            Class.forName(jdbcDriverName);
            try (
                    Connection connection = DriverManager
                            .getConnection(connectionUrl);
                    Statement statement = connection.createStatement();
            ) {
                statement.execute(query);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add the table in the impala.
     *
     * @param create    statement used to create the table
     * @param tableName Name of the table to add
     * @throws SQLException if unable to execute statement
     */
    public void createTable(String create, String tableName)
            throws SQLException {
        String drop = "DROP TABLE IF EXISTS " + tableName;

        try {
            Class.forName(jdbcDriverName);
            try (
                Connection connection = DriverManager
                    .getConnection(connectionUrl);
                Statement statement = connection.createStatement();
            ) {
                // Import table
                statement.execute(drop);
                statement.execute(create);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Execute SQL query on the impala tables.
     *
     * @param sqlStatement Sql string
     * @return List of the result rows
     * @throws SQLException if unable to execute query
     */
    public List<List<String>> executeQuery(String sqlStatement)
            throws SQLException {
        List<List<String>> resultList = new ArrayList<>();

        try {
            Class.forName(jdbcDriverName);
            try (
                Connection connection = DriverManager
                    .getConnection(connectionUrl);
                Statement statement = connection.createStatement();
            ) {
                ResultSet resultSet = statement.executeQuery(sqlStatement);
                ResultSetMetaData rsmd = resultSet.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                List<String> header = new ArrayList<>();

                // Header
                for (int i = 1; i <= columnsNumber; i++) {
                    header.add(rsmd.getColumnName(i));
                }

                // Add the header
                resultList.add(header);

                // Parsing the returned result
                while (resultSet.next()) {
                    List<String> rowList = new ArrayList<>();

                    for (int i = 1; i <= columnsNumber; i++) {
                        rowList.add(resultSet.getString(i));
                    }

                    resultList.add(rowList);
                }

                System.out.println("Executed: " + sqlStatement);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return resultList;
    }
}
