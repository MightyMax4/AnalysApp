package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Max on 01.12.2014.
 */

public class DBConnector {
    private static Connection conn;

    public static Connection getConn() throws ClassNotFoundException, SQLException  {
        if (conn != null) {
            return conn;
        } else {
            createConnection();
            return conn;
        }
    }

    public static void createConnection() throws ClassNotFoundException  {
        try {
            //Loading Driver for MySql
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:analysappbd.s3db");
            System.out.println("******* Connection created successfully........");
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }


    public static void closeConnection() throws ClassNotFoundException, SQLException {
        try {
            conn.close();
            System.out.println("******* Connection closed Successfully.........");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}