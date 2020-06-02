package org.apromore.plugin.services;

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
     */
    List<List<String>> addTableGetSnippet(String fileName, int limit);

    /**
     * Get snippet from the impala tables.
     *
     * @param tableName File Name
     * @param limit     Limit the rows
     * @return return the snippet of the table.
     */
    List<List<String>> getSnippet(String tableName, int limit);
}
