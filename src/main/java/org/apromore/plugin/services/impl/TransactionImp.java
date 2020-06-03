package org.apromore.plugin.services.impl;

import java.util.List;

import org.apromore.plugin.services.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Transaction service for executing statements for impala.
 */
@Service("transactionService")
public class TransactionImp implements Transaction {

    @Autowired
    private ImpalaJdbcAdaptor impalaJdbc;

    /**
     * Add the file to the Impala and get a snippet.
     *
     * @param fileName File Name
     * @param limit  Limit of the rows
     * @return return the snippet of the table.
     */
    @Override
    public List<List<String>> addTableGetSnippet(String fileName, int limit) {

        List<List<String>> resultsList = null;

        String tableName = fileName.split("\\.")[0];

        // Adding the file into the Impala as a table
        boolean isTableAdded =  impalaJdbc.addTable(tableName, fileName);

        // Get snippet
        if (isTableAdded) {
            resultsList = impalaJdbc.executeQuery("SELECT * FROM " +
                    tableName + " LIMIT " + limit);
        }

        return resultsList;
    }

    /**
     * Get snippet from the impala tables.
     *
     * @param fileName File Name
     * @param limit Limit the rows
     * @return return the snippet of the table.
     */
    @Override
    public List<List<String>> getSnippet(String fileName, int limit) {
        List<List<String>> resultsList;

        String tableName = fileName.split("\\.")[0];

        resultsList = impalaJdbc.executeQuery("SELECT * FROM " +
                tableName + " LIMIT " + limit);

        return resultsList;
    }

    /**
     * Separate add table method to add tables in Impala.
     *
     * @param fileName File to add
     * @return Status if the file was added
     */
    @Override
    public boolean addTable(String fileName) {

        String tableName = fileName.split("\\.")[0];

        // Adding the file into the Impala as a table
        boolean fileAddStatus =  impalaJdbc.addTable(tableName, fileName);

        return fileAddStatus;
    }
}
