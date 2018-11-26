package utilities;

import java.sql.*

public class Connection {
    static String driver = "com.mysql.jdbc.Driver";
    static String database = "jdbc:mysql://localhost:3306/book";
    static String user = "root";
    static String password = "";

    static Connection conn;
    static Statement stmt;
    static ResultSet rs;

    public 
}