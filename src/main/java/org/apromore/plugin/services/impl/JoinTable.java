package org.apromore.plugin.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Join table is the Join table structure to store join information
 * such as keys, join type Table child Graph.
 */
public class JoinTable {
    private String tableName;
    private String joinType = null;
    private HashMap<String, String> joinKeys  = null;
    private List<JoinTable> children = null;

    /**
     * Constructor to create the Join table.
     *
     * @param tableName Name of the table
     * @param joinKey The key to join to other table
     * @param forwardTable The forward joining table
     */
    public JoinTable(String tableName, String joinKey, String forwardTable) {
        joinKeys = new HashMap<>();
        this.tableName = tableName;
        addKey(forwardTable, joinKey);
    }

    /**
     * Add Keys for the tables that are connected to this table.
     *
     * @param forwardTable The table the key is connected to
     * @param key The column field that is connected to the forward table
     */
    public void addKey(String forwardTable, String key) {
        String joinKey = String.format(
                            "`%s`.`%s`",
                            tableName,
                            key
        );

        joinKeys.put(forwardTable, joinKey);
    }

    /**
     * Get the Join key associated with the Join Table.
     *
     * @param table join table key to look for
     * @return Join key
     */
    public String getJoinKey(JoinTable table) {
        return joinKeys.get(table.getTableName());
    }

    /**
     * Add child Join Table in the graph.
     *
     * @param table Join table to add as child.
     */
    public void addChild(JoinTable table) {

        if (children == null) {
            children = new ArrayList<>();
        }

        children.add(table);
    }

    /**
     * Get the child Tables.
     *
     * @return List of children Join Tables
     */
    public List<JoinTable> getChildTables() {
        return children;
    }

    /**
     * Get the table Name.
     *
     * @return Table name
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Set the Join type.
     *
     * @param joinType Join Type that joins the preceding table
     */
    public void setJoinType(String joinType) {
        this.joinType = joinType;
    }

    /**
     * Get the join Type.
     *
     * @return Join type
     */
    public String getJoinType() {
        return joinType;
    }

    /**
     * Equals overriding method to see equality with the object.
     *
     * @param obj Other object to compare with
     * @return True if equal; false if not equal
     */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /**
     * Hash Code to compare objects.
     *
     * @return Hash code
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
