package utilities;

import java.sql.*;

public class ConnectionMySQL {
    static String driver = "com.mysql.jdbc.Driver";
    static String database = "jdbc:mysql://localhost:3306/book";
    static String user = "root";
    static String password = "";
    
    
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        return DriverManager.getConnection(database,user,password);
    }
    
    public static void closeConnection(Connection con) throws SQLException {
        con.close();
    }
    
}