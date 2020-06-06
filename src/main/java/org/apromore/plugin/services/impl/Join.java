package org.apromore.plugin.services.impl;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Join is used to create Join String using TableJoin Graph and
 * running Breath first search on it.
 */
@Component
public class Join {
    private List<JoinTable> tables;
    private String joinString;

    /**
     * Get Joining string for all the table inputs.
     *
     * @param joinTables List of all the join table pairs
     * @return The join string for all the tables
     */
    public String getJoinString(List<List<String>> joinTables) {
        createJoinTables(joinTables);
        for (JoinTable t: tables) {
            t.printTableInfo();
        }

        buildJoinString();
        return joinString;
    }

    /**
     * Create the Join Table Graph.
     *
     * @param joinTables List of the tables to join
     */
    private void createJoinTables(List<List<String>> joinTables) {
        tables = new ArrayList<>();

        // Init table list
        for (List<String> row: joinTables) {
            String table1 = row.get(0);
            String key1 = row.get(1);
            String table2 = row.get(2);
            String key2 = row.get(3);
            String joinType = row.get(4);

            addTablePair(table1, key1, table2, key2, joinType);
        }
    }

    /**
     * Create the Join Table or add parameters to the existing ones.
     *
     * @param tableName Table name
     * @param key Key name
     * @param forwardTableName Next table to join name
     * @return Join Table node for graph
     */
    private JoinTable getJoinTable(
            String tableName,
            String key,
            String forwardTableName
    ) {

        JoinTable joinTable = null;

        for (JoinTable table: tables) {
            if (table.getTableName().equals(tableName)) {
                joinTable = table;
                break;
            }
        }

        if (joinTable == null) {
            joinTable = new JoinTable(tableName, key, forwardTableName);
            tables.add(joinTable);
        } else {
            joinTable.addKey(forwardTableName, key);
        }

        return joinTable;
    }

    /**
     *
     * @param table1
     * @param key1
     * @param table2
     * @param key2
     * @param joinType
     */
    private void addTablePair(
            String table1,
            String key1,
            String table2,
            String key2,
            String joinType
    ) {
        // Create tables graph
        JoinTable joinTable1 = getJoinTable(table1, key1, table2);
        JoinTable joinTable2 = getJoinTable(table2, key2, table1);

        // Add child and joinType
        joinTable1.addChild(joinTable2);
        joinTable2.setJoinType(joinType);
    }

    /**
     *
     * @return
     */
    private JoinTable findStartingTable() {

        for (JoinTable table: tables) {
            if (table.getJoinType() == null) {
                return table;
            }
        }

        return null;
    }

    private JoinTable getPrecedingTable(JoinTable table) {

        for (JoinTable jTable : tables) {
            List<JoinTable> childTables = jTable.getChildTables();
            if (childTables != null) {
                for (JoinTable jt : childTables) {
                    if (table.equals(jt)) {
                        return jTable;
                    }
                }
            }
        }
        return null;
    }

    private ArrayList<JoinTable> CalculateJoinSequenceBFS() {
        Queue<JoinTable> queue = new LinkedList<>();
        ArrayList<JoinTable> joinSequence = new ArrayList<>();
        JoinTable startTable = findStartingTable();

        queue.add(startTable);

        while (!queue.isEmpty()) {
            JoinTable current = queue.remove();
            List<JoinTable> childTables = current.getChildTables();

            if (childTables != null) {
                queue.addAll(childTables);
            }

            joinSequence.add(current);
        }

        return joinSequence;
    }

    private void buildJoinString() {

        joinString = "";
        ArrayList<JoinTable> joinSequence = CalculateJoinSequenceBFS();

        // Get the first table to join with
        joinString += joinSequence.get(0).getTableName() + " ";

        // Create all the tables to join
        for (int i = 1; i < joinSequence.size() ; i ++) {
            JoinTable rightTable = joinSequence.get(i);
            JoinTable leftTable = getPrecedingTable(rightTable);

            joinString += String.format(
                    "%s %s ON %s=%s ",
                    rightTable.getJoinType(),
                    rightTable.getTableName(),
                    leftTable.getJoinKey(rightTable),
                    rightTable.getJoinKey(leftTable)
            );
        }
    }

}
