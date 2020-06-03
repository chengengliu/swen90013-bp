package org.apromore.plugin.services.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;

/**
 * Impala JDBC Adaptor class to connect and execute queries.
 */
@Component
public class ImpalaJdbcAdaptor {

    // Impala connection info
    private final String connectionUrl = System.getenv("IMPALA_LINK");
    private final String jdbcDriverName = "com.cloudera.impala.jdbc41.Driver";
    private final String dataPath = System.getProperty("java.io.tmpdir") +
        System.getenv("DATA_STORE");

    /**
     * Create a table from a parquet file.
     *
     * @param tableName name of the table to create
     * @param fileName  name of the file
     * @throws SQLException if unable to execute statement
     */
    public void createParquetTable(String tableName, String fileName)
            throws SQLException {
        String dir = dataPath + "/" + FilenameUtils.removeExtension(fileName);

        String create = "CREATE EXTERNAL TABLE `%s` " +
            "LIKE PARQUET '%s' " +
            "STORED AS PARQUET " +
            "LOCATION '%s'";

        create = String.format(
            create,
            tableName,
            dir + "/" + fileName,
            dir);

        createTable(create, tableName);
    }

    /**
     * Create a table from a csv file.
     *
     * @param tableName name of the table to create
     * @param fileName  name of the file
     * @throws IOException  if unable to read file
     * @throws SQLException if unable to execute statement
     */
    public void createCsvTable(String tableName, String fileName)
            throws IOException,
            SQLException {
        String columns = "";

        String dir = dataPath + "/" + FilenameUtils.removeExtension(fileName) +
            "_csv";
        File file = new File(dir + "/" + fileName);

        try (
            FileReader fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader);
        ) {
            List<String> headers = Arrays.asList(br.readLine().split(","));
            List<String> firstRow = Arrays.asList(br.readLine().split(","));

            for (int i = 0; i < headers.size(); i++) {
                columns += String.format(
                    "`%s` %s, ",
                    headers.get(i),
                    StringUtils.getColumnType(firstRow.get(i)));
            }
        }

        String create = "CREATE EXTERNAL TABLE `%s` (%s) " +
            "ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' " +
            "LINES TERMINATED BY '\n' " +
            "STORED AS TEXTFILE " +
            "LOCATION '%s' " +
            "TBLPROPERTIES('skip.header.line.count'='1')";

        create = String.format(
            create,
            tableName + "_csv",
            columns.substring(0, columns.length() - 2),
            dir);

        createTable(create, tableName + "_csv");

        try {
            Class.forName(jdbcDriverName);
            try (
                Connection connection = DriverManager
                    .getConnection(connectionUrl);
                Statement statement = connection.createStatement();
            ) {
                String query = "CREATE EXTERNAL TABLE `%s` " +
                    "LIKE `%s` " +
                    "STORED AS PARQUET " +
                    "LOCATION '%s'";

                query = String.format(
                    query,
                    tableName,
                    tableName + "_csv",
                    dataPath + "/" + tableName);

                createTable(query, tableName);

                statement.execute(
                    String.format(
                        "INSERT OVERWRITE TABLE `%s` SELECT * FROM `%s`",
                        tableName,
                        tableName + "_csv"));

                statement.execute(
                    String.format(
                        "DROP TABLE IF EXISTS `%s`",
                        tableName + "_csv"));
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
    private void createTable(String create, String tableName)
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

        try (
            Connection connection = DriverManager.getConnection(connectionUrl);
        ) {
            // Init connection
            Class.forName(jdbcDriverName);
            Statement statement = connection.createStatement();

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

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return resultList;
    }
}
