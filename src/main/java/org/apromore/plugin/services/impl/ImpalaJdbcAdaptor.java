package org.apromore.plugin.services.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
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
    private final String dataPath = System.getProperty("java.io.tmpdir") +
        System.getenv("DATA_STORE");

    /**
     * Get the type of a string.
     *
     * @param string string to check
     * @return type of the string
     */
    private String getType(String string) {
        if (isInt(string)) {
            return "INT";
        } else if (isDouble(string)) {
            return "DOUBLE";
        } else if (isBool(string)) {
            return "BOOLEAN";
        } else {
            return "STRING";
        }
    }

    /**
     * Determine whether a string is an integer.
     *
     * @param string string to check
     * @return true if the string is an integer, false otherwise
     */
    private boolean isInt(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Determine whether a string is a double.
     *
     * @param string string to check
     * @return true if the string is a double, false otherwise
     */
    private boolean isDouble(String string) {
        try {
            Double.parseDouble(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Determine whether a string is a boolean.
     *
     * @param string string to check
     * @return true if the string is a boolean, false otherwise
     */
    private boolean isBool(String string) {
        return (string.equals("True") || string.equals("False"));
    }

    /**
     * Add the table in the impala.
     *
     * @param tableName Name of the table to add
     * @param fileName  File name
     * @return If the file was added
     */
    public boolean addTable(String tableName, String fileName) {
        String drop = "DROP TABLE IF EXISTS " + tableName;
        String create;

        if (fileName.endsWith(".parquet") || fileName.endsWith(".dat")) {
            create = "CREATE EXTERNAL TABLE %s " +
                "LIKE PARQUET '%s' " +
                "STORED AS PARQUET " +
                "LOCATION '%s'";

            create = String.format(
                create,
                tableName,
                dataPath + "/" + fileName + "/" + fileName,
                dataPath + "/" + fileName);
        } else if (fileName.endsWith(".csv")) {
            String columns = "";
            File file = new File(dataPath + "/" + fileName + "/" + fileName);

            try (
                FileReader fileReader = new FileReader(file);
                BufferedReader br = new BufferedReader(fileReader);
            ) {
                List<String> headers = Arrays.asList(br.readLine().split(","));
                List<String> firstRow = Arrays.asList(br.readLine().split(","));

                for (int i = 0; i < headers.size(); i++) {
                    columns += String.format(
                        "%s %s, ",
                        headers.get(i),
                        getType(firstRow.get(i)));
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            create = "CREATE EXTERNAL TABLE %s (%s) " +
                "ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' " +
                "LINES TERMINATED BY '\n' " +
                "STORED AS TEXTFILE " +
                "LOCATION '%s' " +
                "TBLPROPERTIES('skip.header.line.count'='1')";

            create = String.format(
                create,
                tableName,
                columns.substring(0, columns.length() - 2),
                dataPath + "/" + fileName);
        } else {
            return false;
        }

        try (
            Connection connection = DriverManager.getConnection(connectionUrl);
        ) {
            // Init connection
            Class.forName(jdbcDriverName);
            Statement statement = connection.createStatement();

            // Import table
            statement.execute(drop);
            statement.execute(create);

            System.out.println("Table added");
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Failed to add Table");
            e.printStackTrace();
        }
        return false;
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
