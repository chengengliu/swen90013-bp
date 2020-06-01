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
    private final String dataPath = System.getenv("DATA_STORE");
    private Statement statement;

    /**
     * Add the table in the impala.
     *
     * @param tableName Name of the table to add
     * @param fileName File name
     * @return If the file was added
     */
    public boolean addTable(String tableName, String fileName) {

        String sqlStatementDrop = "DROP TABLE IF EXISTS " + tableName;

        // Build String
        String query = "CREATE EXTERNAL TABLE %s " +
                        "LIKE PARQUET '%s/%s' " +
                        "STORED AS PARQUET " +
                        "LOCATION '%s'";

        query = String.format(query, tableName, dataPath, fileName, dataPath);
        boolean status = false;

        try (
            Connection connection = DriverManager.getConnection(connectionUrl)
        ) {
            // Init connection
            Class.forName(jdbcDriverName);
            statement = connection.createStatement();

            // Import table
            statement.execute(sqlStatementDrop);
            statement.execute(query);

            status = true;
            System.out.println("Table added!!");

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Failed to add Table!!");
            e.printStackTrace();
        }
        return status;
    }

    /**
     * Execute SQL query on the impala tables.
     *
     * @param sqlStatement Sql string
     * @return List of the result rows
     */
    public List<List<String>> executeQuery(String sqlStatement) {

        List<List<String>> resultList = new ArrayList<>();

        try (
            Connection connection = DriverManager.getConnection(connectionUrl)
        ) {
            // Init connection
            Class.forName(jdbcDriverName);
            statement = connection.createStatement();

            // Execute query
            ResultSet resultSet = statement.executeQuery(sqlStatement);
            ResultSetMetaData rsmd = resultSet.getMetaData();

            int columnsNumber = rsmd.getColumnCount();

            List<String> header = new ArrayList<>();

            // Header
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) {
                    System.out.print(" | ");
                }
                header.add(rsmd.getColumnName(i));
                System.out.print(rsmd.getColumnName(i));
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

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return resultList;
    }
}
