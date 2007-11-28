/*
 * Created on Jan 30, 2005
 *
 */

package com.james.test;
import java.sql.SQLException;

/**
 * @author James
 */
public class DBTest {
    public DBTest() throws SQLException, ClassNotFoundException {
        // Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
        //        String cxnURL = "jdbc:microsoft:sqlserver://lolly:1433;user=sa;password=password";
//        Connection cxn = DriverManager.getConnection(cxnURL);

        // PreparedStatement stmt = cxn.prepareStatement("delete from disk");

        //stmt.execute();

    }

    public static void main(String args[]) throws SQLException, ClassNotFoundException {

        new DBTest();

        //        lolly
        //        db:faye
        //        user:sa
        //        password:password
        //        1433
        //        table: outlet
        //        column: description
    }

}