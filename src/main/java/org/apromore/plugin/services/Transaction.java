package org.apromore.plugin.services;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Transaction Methods Interface for impala.
 */
public interface Transaction {
    /**
     * Add the file to the Impala and get a snippet.
     *
     * @param fileName File Name
     * @param limit    Limit of the rows
     * @return return the snippet of the table.
     * @throws SQLException if unable to execute statement
     * @throws IOException if unable to read file
     */
    List<List<String>> addTableGetSnippet(String fileName, int limit)
        throws IOException, SQLException;

    /**
     * Separate add table method to add tables in Impala.
     *
     * @param fileName File to add
     * @throws SQLException if unable to execute statement
     * @throws IOException if unable to read file
     */
    void addTable(String fileName) throws IOException, SQLException;

    /**
     * Get snippet from the impala tables.
     *
     * @param fileName File Name
     * @param limit    Limit the rows
     * @return return the snippet of the table.
     * @throws SQLException if unable to execute statement
     */
    List<List<String>> getSnippet(String fileName, int limit)
        throws SQLException;
}
