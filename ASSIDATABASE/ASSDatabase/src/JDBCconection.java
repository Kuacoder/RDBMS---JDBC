import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCconection {
    public static Connection getJDBCconnection() throws SQLException{
    Connection conn = null;
    String strConn = null;
    try {
    strConn = "jdbc:mysql://localhost:3306/Shopee";
    conn = DriverManager.getConnection(strConn,"root","nhulin12");
    } catch (SQLException e) {
    System.out.println("SQLException: "+e.getMessage());
    System.out.println("SQLState: "+e.getSQLState());
    System.out.println("VendorError: "+e.getErrorCode());
    }
    return conn;
    }
}