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
    private final String connectionUrl = "jdbc:impala://apacheimpala:21050";
    private final String jdbcDriverName = "com.cloudera.impala.jdbc41.Driver";
    private Connection con = null;
    private Statement stmt;

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
                        "LIKE PARQUET '/preprocess_data/%s' " +
                        "STORED AS PARQUET " +
                        "LOCATION '/preprocess_data'";

        query = String.format(query, tableName, fileName);
        boolean status = false;

        try {

            initConnection();

            // Import table
            stmt.execute(sqlStatementDrop);
            stmt.execute(query);

            status = true;
            System.out.println("Table added!!");

            exitConnection();

        } catch (SQLException e) {
            System.out.println("Failed to add Table!!");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Failed to add Table!!");
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
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

        try {

            initConnection();

            // Execute query
            ResultSet resultSet = stmt.executeQuery(sqlStatement);
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
            exitConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return resultList;
    }

    /**
     * Constructing the JDBC connection.
     */
    private void initConnection() {
        try {
            //
            Class.forName(jdbcDriverName);
            con = DriverManager.getConnection(connectionUrl);
            stmt = con.createStatement();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Exit the JDBC connection.
     * @throws Throwable Close connection error.
     */
    private void exitConnection() throws Throwable {
        this.con.close();
    }
}
