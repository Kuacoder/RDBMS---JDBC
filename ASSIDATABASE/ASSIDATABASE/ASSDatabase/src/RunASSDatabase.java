import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class RunASSDatabase {

    public static void main(String[] args) throws Exception {
        Scanner getScanner = new Scanner(System.in);
        Connection conn = JDBCconection.getJDBCconnection();
        Customer c = new Customer();
        Item i = new Item();
        Order o = new Order();
        int choice = -1;
        while (choice != 0) {
            System.out.println("+---------------------------------------------------+");
            System.out.println("|                      SHOPEE                       |");
            System.out.println("+---------------------------------------------------+");
            System.out.println("| 1. Enter Your information                         |");
            System.out.println("| 2. Enter Product's information                    |");
            System.out.println("| 3. Purchase                                       |");
            System.out.println("| 4. Search Product's information                   |");
            System.out.println("| 0. Exit                                           |");
            System.out.println("+---------------------------------------------------+");
            System.out.println("Please choice: ");
            choice = getScanner.nextInt();
            switch (choice) {
                case 1:
                    c.input();
                    break;
                case 2:
                    i.input();
                    break;
                case 3:
                    o.input();
                    break;
                case 4:
                    o.search();
                    break;
                default:
                    if (choice != 0) {
                        System.out.println("Please rechoice!");
                    }
                    break;
            }
        }
        System.out.println("The end!");
    }
}
