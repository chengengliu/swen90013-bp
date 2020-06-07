package org.apromore.plugin.services.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Add Table to Impala Handler.
 */
@Component
public class ImpalaTable {
    @Autowired
    ImpalaJdbcAdaptor impalaJdbcAdaptor;

    // Impala connection info
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

        create = String.format(create, tableName, dir + "/" + fileName, dir);

        impalaJdbcAdaptor.createTable(create, tableName);
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
            throws IOException, SQLException {
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

        // Create Table in CSV/Textfile format
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

        impalaJdbcAdaptor.createTable(create, tableName + "_csv");

        // Create File in Parquet format
        String query = "CREATE EXTERNAL TABLE `%s` " +
                        "LIKE `%s` " +
                        "STORED AS PARQUET " +
                        "LOCATION '%s'";

        query = String.format(
                        query,
                tableName,
                        tableName + "_csv",
                        dataPath + "/" + tableName);

        impalaJdbcAdaptor.createTable(query, tableName);

        impalaJdbcAdaptor.execute(String.format(
                    "INSERT OVERWRITE TABLE `%s` SELECT * FROM `%s`",
                tableName,
                        tableName + "_csv")
        );

        impalaJdbcAdaptor.execute(
                String.format(
                        "DROP TABLE IF EXISTS `%s`",
                        tableName + "_csv"));

    }
}
