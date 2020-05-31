package org.apromore.plugin.services.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Impala JDBC Adaptor class to connect and execute queries.
 */
public class ImpalaJdbcAdaptor {
    // Impala connection info
    private final String connectionUrl = "jdbc:impala://apacheimpala:21050";
    private final String jdbcDriverName = "com.cloudera.impala.jdbc41.Driver";
    private Connection con = null;
    private Statement stmt;

    /**
     * Constructing the JDBC connection.
     */
    public ImpalaJdbcAdaptor() {
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

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.con.close();
    }

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

            // Import table
            stmt.execute(sqlStatementDrop);
            stmt.execute(query);

            status = true;
            System.out.println("Table added!!");

        } catch (SQLException e) {
            System.out.println("Failed to add Table!!");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Failed to add Table!!");
            e.printStackTrace();
        }
        return status;
    }

    /**
     * Create query to get snippet from impala.
     *
     * @param tableName Name of the table to execute query on
     * @param limit Limit of the rows results
     * @return List of the result rows
     */
    public List<List<String>> getSnippet(String tableName, int limit) {
        return executeQuery("SELECT * FROM "
                            + tableName + " LIMIT " + limit);
    }

    /**
     * Execute SQL query on the impala tables.
     *
     * @param sqlStatement Sql string
     * @return List of the result rows
     */
    private List<List<String>> executeQuery(String sqlStatement) {

        List<List<String>> resultList = new ArrayList<>();

        try {

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

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }
}
