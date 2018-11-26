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
    
    public static ResultSet getFromDB(PreparedStatement preparedStatement) throws SQLException, ClassNotFoundException {
        
        Connection con = getConnection();
        
//        String query = "SELECT * FROM USER WHERE ID= (?) AND ADDRESS = (?);"
//        PreparedStatement p = con.prepareStatement(query);
//        p.setInt(1,20);
//        p.setString(2, "Shinta");
        
        ResultSet resultSet = preparedStatement.executeQuery();
        
        // p.executeUpdate();
        
        closeConnection(con);
        
        return resultSet;
        
    }
    
}