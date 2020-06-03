package org.apromore.plugin.services.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apromore.plugin.services.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Transaction service for executing statements for impala.
 */
@Service("transactionService")
public class TransactionImpl implements Transaction {

    @Autowired
    private ImpalaJdbcAdaptor impalaJdbc;

    /**
     * Add the file to the Impala and get a snippet.
     *
     * @param fileName File Name
     * @param limit    Limit of the rows
     * @return return the snippet of the table.
     * @throws SQLException if unable to execute statement
     * @throws IOException  if unable to read file
     */
    @Override
    public List<List<String>> addTableGetSnippet(String fileName, int limit)
            throws IOException, SQLException {
        addTable(fileName);
        return getSnippet(fileName, limit);
    }

    /**
     * Get snippet from the impala tables.
     *
     * @param fileName File Name
     * @param limit    Limit the rows
     * @return return the snippet of the table.
     * @throws SQLException if unable to execute statement
     */
    @Override
    public List<List<String>> getSnippet(String fileName, int limit)
            throws SQLException {
        List<List<String>> resultsList;
        String tableName = FilenameUtils.removeExtension(fileName);

        resultsList = impalaJdbc
                .executeQuery("SELECT * FROM " + tableName + " LIMIT " + limit);

        return resultsList;
    }

    /**
     * Separate add table method to add tables in Impala.
     *
     * @param fileName File to add
     * @throws SQLException if unable to execute statement
     * @throws IOException  if unable to read file
     */
    @Override
    public void addTable(String fileName) throws IOException, SQLException {
        String tableName = FilenameUtils.removeExtension(fileName);

        // Adding the file into the Impala as a table
        if (fileName.endsWith(".csv")) {
            impalaJdbc.createCsvTable(tableName, fileName);
        } else if (
            fileName.endsWith(".parq") ||
            fileName.endsWith(".parquet") ||
            fileName.endsWith(".dat")
        ) {
            impalaJdbc.createParquetTable(tableName, fileName);
        }
    }
}
