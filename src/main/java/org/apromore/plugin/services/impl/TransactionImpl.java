package org.apromore.plugin.services.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apromore.plugin.services.Transaction;
import org.jooq.Record1;
import org.jooq.SelectJoinStep;
import org.jooq.SelectOnStep;
import org.jooq.conf.ParamType;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

/**
 * Transaction service for executing statements for impala.
 */
@Service("transactionService")
public class TransactionImpl implements Transaction {

    @Autowired
    private ImpalaJdbcAdaptor impalaJdbc;

    @Autowired
    private ImpalaTable impalaTable;

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

        resultsList = impalaJdbc.executeQuery(
                "SELECT * FROM " + tableName + " LIMIT " + limit
        );

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
            impalaTable.createCsvTable(tableName, fileName);
        } else if (
            fileName.endsWith(".parq") ||
            fileName.endsWith(".parquet") ||
            fileName.endsWith(".dat")
        ) {
            impalaTable.createParquetTable(tableName, fileName);
        }
    }

    /**
    * Join multiple tables with different keys.
    *
    * @param joinTables List of table pairs to join
    * @param limit number of rows to return after join
    * @throws SQLException If unable to execute sql query
     * @return result List
    */
    @Override
    public List<List<String>> join(List<List<String>> joinTables, int limit)
            throws SQLException {
        SelectJoinStep<Record1<Object>> myQuery = DSL
            .select(field("*"))
            .from(table(String.format("`%s`", joinTables.get(0).get(0))));

        for (List<String> join : joinTables) {
            String table1 = String.format("`%s`", join.get(0));
            String key1 = String.format("`%s`", join.get(1));
            String table2 = String.format("`%s`", join.get(2));
            String key2 = String.format("`%s`", join.get(3));
            String joinType = join.get(4);

            SelectOnStep<Record1<Object>> onStep = null;

            if (joinType.equals("INNER JOIN")) {
                onStep = myQuery.innerJoin(table(table2));
            } else if (joinType.equals("RIGHT JOIN")) {
                onStep = myQuery.rightJoin(table(table2));
            } else if (joinType.equals("LEFT JOIN")) {
                onStep = myQuery.leftJoin(table(table2));
            } else if (joinType.equals("FULL OUTER JOIN")) {
                onStep = myQuery.fullOuterJoin(table(table2));
            }

            if (onStep != null) {
                myQuery = onStep
                    .on(field(String.format("%s.%s", table1, key1))
                        .eq(field(String.format("%s.%s", table2, key2))));
            }
        }

        String query = myQuery.limit(limit).getSQL(ParamType.INLINED);

        return impalaJdbc.executeQuery(query);
    }
}
