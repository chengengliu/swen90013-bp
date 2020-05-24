package org.apromore.plugin.services.impl;

import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//@Service("impalaJdbc")
public class ImpalaJdbc {
    private static final String connectionUrl = "jdbc:impala://localhost:21050";
    private static final String jdbcDriverName = "com.cloudera.impala.jdbc41.Driver";
    private static final String sqlStatementInvalidate = "INVALIDATE METADATA";
    private Connection con = null;
    private Statement stmt;

    public ImpalaJdbc(){
        try {

            Class.forName(jdbcDriverName);
            con = DriverManager.getConnection(connectionUrl);
            stmt = con.createStatement();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.con.close();
    }

    public boolean addTable(String tableName, String fileName){

        String sqlStatementDrop = "DROP TABLE IF EXISTS " + tableName;

        String query = "CREATE EXTERNAL TABLE %s " +
                        "LIKE PARQUET '/home/impala/preprocess_data/%s' " +
                        "STORED AS PARQUET " +
                        "LOCATION '/home/impala/preprocess_data'";

        query = String.format(query, tableName, fileName);
        boolean status = true;

        try {

            System.out.println("\n== Begin Adding table== ");
            System.out.println(query);

            // Import files
            stmt.execute(sqlStatementInvalidate);
//            stmt.execute(sqlStatementDrop);
            stmt.execute(query);

            if (status){
                System.out.println("Table added!!");
            }else {
                System.out.println("Failed to add Table!!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    public List<String> executeQuery(String sqlStatement) {

        System.out.println("\n=============================================");
        System.out.println("Using Connection URL: " + connectionUrl);
        System.out.println("Running Query: " + sqlStatement);


        List<String> resultList = new ArrayList<>();

        try {

            System.out.println("\n== Begin Query Results ======================");
            stmt.execute(sqlStatementInvalidate);
            ResultSet resultSet = stmt.executeQuery(sqlStatement);
            ResultSetMetaData rsmd = resultSet.getMetaData();

            int columnsNumber = rsmd.getColumnCount();

            // Header
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) System.out.print(" | ");
                System.out.print(rsmd.getColumnName(i));
            }

            // Rows and columns
            System.out.println("");
            while (resultSet.next()) {
                String row = "";
                for (int i = 1; i <= columnsNumber; i++) {

                    if (i > 1 && i < columnsNumber) {
                        System.out.print(", ");
                        row += ",";
                    }

                    String columnValue = resultSet.getString(i);
                    row += columnValue;
                    System.out.print(columnValue);
                }

                resultList.add(row);
                System.out.println("");
            }

            System.out.println("== End Query Results =======================\n\n");

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        return resultList;
    }
}