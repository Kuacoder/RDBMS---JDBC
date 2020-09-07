import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class RunASSDatabase {

    public static void main(String[] args) throws SQLException {
        // Connection conn = JDBCconection.getJDBCconnection();
        // ResultSet rs = null;
        // Statement stmt = null;
        // Scanner getScanner = new Scanner(System.in);
        // Customer c = new Customer();
        // Item i = new Item();
        // Order o = new Order();
        // int choice = -1;
    //     while (choice != 0) {
    //         System.out.println("+---------------------------------------------------+");
    //         System.out.println("|                      SHOPEE                       |");
    //         System.out.println("+---------------------------------------------------+");
    //         System.out.println("| 1. Enter Your information                         |");
    //         System.out.println("| 2. Enter Product's information                    |");
    //         System.out.println("| 3. Update Product's information                   |");
    //         System.out.println("| 4. Purchase                                       |");
    //         System.out.println("| 5. Search Product's information                   |");
    //         System.out.println("| 0. Exit                                           |");
    //         System.out.println("+---------------------------------------------------+");
    //         System.out.println("Please choice: ");
    //         choice = getScanner.nextInt();
    //         switch (choice) {
    //             case 1:
    //                 c.input();
    //                 break;
    //             case 2:
    //                 i.input();
    //                 break;
    //             case 3:
    //                 i.update();
    //                 break;
    //             case 4:
    //                 o.input();
    //                 break;
    //             case 5:
    //                 o.search();
    //                 break;
    //             default:
    //                 if (choice != 0) {
    //                     System.out.println("Please rechoice!");
    //                 }
    //                 break;
    //         }
    //     }
    //     System.out.println("The end!");
    // }
    Scanner sc = new Scanner(System.in);
        Connection conn = null;
        Statement stmt = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String strConn = null;
        try {
            strConn = "jdbc:mysql://localhost:3306/shopee";//ket noi database
            conn = DriverManager.getConnection(strConn, "root", "nhulin12");
            System.out.println("Connection OK");
            stmt = conn.createStatement();
            String sql = "SELECT CustomerID, C_name  FROM Customer LIMIT 10";
            rs = stmt.executeQuery(sql);
            while (rs.next()){
                System.out.print(rs.getString("CustomerID"));
                System.out.print("\t");
                System.out.print(rs.getString("C_name"));
                System.out.print("\n");
            } } catch (SQLException e) {
                System.out.println("SQLException: "+e.getMessage());
                System.out.println("SQLState: "+e.getSQLState());
                System.out.println("VendorError: "+e.getErrorCode());
            }
        }  
    }

