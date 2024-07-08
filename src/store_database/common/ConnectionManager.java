package store_database.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private static final String URL = "jdbc:mysql://localhost:3306/store_java";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() {

        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            if (conn == null) System.out.println("there is an error in the connetion");
            else System.out.println("This is so right ");
            return conn;
        } catch (SQLException |
                 ClassNotFoundException e) {
            e.getMessage();
        }
        return conn;
    }


    /*
     * This is the main Class used to connect to database
     * And this class will provide objects for any repo that needs to
     * connect to database to its CRUD operations
     * */
    class OldConnectionManager {
        final String URL = "jdbc:mysql://localhost:3306/store_java";
        final String USERNAME = "root";
        final String PASSWORD = "";

        public Connection connect() {
            Connection connection = null;
            // This is used to make sure // For making sure that the class is in libraries
            // You must not use Try with resource here uaAmer
            // because you need to return the connection Not close it before returning the connection
            try {
                Class.forName("com.mysql.cj.jdbc.Driver"); // For making sure that the class is in libraries
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                return connection;
            } catch (SQLException e) {
                System.out.println("WE failed to open connection to our database Thanks very much : " + e.getMessage());
            } catch (ClassNotFoundException e) {
                System.out.println("This is an error loading Driver manager");
            }

            // This is used to make connection

            // method should return connection
            return connection;
        }

        public Connection getConnection() {
            Connection conn = null;
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Connected to MySQL database!");
            } catch (SQLException | ClassNotFoundException e) {
                System.err.println("Error connecting to MySQL database: " + e.getMessage());
            }
            return conn;
        }
    }
}
