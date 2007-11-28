package com.james.dao.driver;

import java.sql.SQLException;

public class DriverManagerTest {
    private static String driver = "org.hsqldb.jdbcDriver";

    // private static String password = "";

    public static final void main(String args[]) throws SQLException {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        DriverManagerTest test = new DriverManagerTest();

        test.connect();
    }

    public void connect() throws SQLException {

        // DriverManagerDataSourceFactory connectionFactory = new
        // DriverManagerDataSourceFactory(
        // url, null, null);
        //
        // Connection cxn = connectionFactory.createConnection();
        //
        // Statement stmt = cxn.createStatement();
        //
        // stmt.execute("select * from dir;");
        //
        // ResultSet rs = stmt.getResultSet();
        //
        // while (rs.next()) {
        // System.out.println(rs.getString(1));
        // }
    }
}
