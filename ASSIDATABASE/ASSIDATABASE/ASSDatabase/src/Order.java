import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Order {
    PreparedStatement pstmt, pstmt5, pstmt7 = null;
    Statement stmt, stmt1, stmt2, stmt3, stmt4, stmt6, stmt8 = null;
    ResultSet rs, rs1, rs2, rs3, rs4, rs6, rs8 = null;
    private String OrderID;
    private String CustomerID;
    private String ItemID;
    private int O_amount;
    private int DeliveryID;
    private int Total_price;
    private int PaymentID;
    private String O_date;
    private String O_status;
    private int Fee_shipping = 0;
    private String P_type;
    private String search;

    public Order() {
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOderID(String orderID) {
        OrderID = orderID;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public String getItemID() {
        return ItemID;
    }

    public void setItemID(String itemID) {
        ItemID = itemID;
    }

    public int getO_amount() {
        return O_amount;
    }

    public void setO_amount(int o_amount) {
        O_amount = o_amount;
    }

    public int getDeliveryID() {
        return DeliveryID;
    }

    public void setDeliveryID(int deliveryID) {
        DeliveryID = deliveryID;
    }

    public int getTotal_price() {
        return Total_price;
    }

    public void setTotal_price(int total_price) {
        Total_price = total_price;
    }

    public int getPaymentID() {
        return PaymentID;
    }

    public void setPaymentID(int paymentID) {
        PaymentID = paymentID;
    }

    public String getO_date() {
        return O_date;
    }

    public void setO_date(String o_date) {
        O_date = o_date;
    }

    public String getO_status() {
        return O_status;
    }

    public void setO_status(String o_status) {
        O_status = o_status;
    }

    public void input() throws SQLException {
        Connection conn = JDBCconection.getJDBCconnection();
        String sql = "INSERT INTO Item_order VALUES(?,?,?,?,?,?,?,?,?)";
        String sql1 = "SELECT CustomerID FROM Customer";
        String sql2 = "SELECT ItemID FROM Item";
        String sql3 = "SELECT max(DeliveryID) FROM Delivery";
        String sql4 = "SELECT max(PaymentID) FROM Payment";
        String sql5 = "INSERT INTO Delivery VALUES(?,?,?)";
        String sql6 = "SELECT ItemID,Cost,Discount FROM getprice_view";
        String sql7 = "INSERT INTO Payment VALUES(?,?,?)";

        int DeliveryID = 0;
        int PaymentID = 0;

        stmt1 = conn.createStatement();
        stmt2 = conn.createStatement();
        stmt3 = conn.createStatement();
        stmt4 = conn.createStatement();
        stmt6 = conn.createStatement();

        rs1 = stmt1.executeQuery(sql1);
        rs2 = stmt2.executeQuery(sql2);
        rs3 = stmt3.executeQuery(sql3);
        rs4 = stmt4.executeQuery(sql4);
        rs6 = stmt6.executeQuery(sql6);

        try {
            pstmt = conn.prepareStatement(sql);
            while (rs3.next()) {
                if (rs3 == null) {
                    this.DeliveryID = 0;
                } else {
                    this.DeliveryID = rs3.getInt("max(DeliveryID)");
                }
            }

            while (rs4.next()) {
                if (rs4 == null) {
                    this.PaymentID = 0;
                } else {
                    this.PaymentID = rs4.getInt("max(PaymentID)");
                }
            }
            System.out.println("                      ORDER                 ");
            while (true) {
                System.out.println("Enter Order ID: ");
                this.OrderID = getscanner().nextLine();
                if (this.OrderID.isEmpty()) {
                    System.out.println("You must enter Order ID!");
                } else {
                    pstmt.setString(1, this.OrderID);
                    break;
                }
            }
            while (true) {
                System.out.println("Enter Customer ID: ");
                this.CustomerID = getscanner().nextLine();
                while (rs1.next()) {
                    if (rs1 == null) {
                        System.out.println("Not have Customer ID");
                    } else if (rs1.getString("CustomerID").equalsIgnoreCase(this.CustomerID)) {
                        pstmt.setString(2, this.CustomerID);
                        break;
                    }
                }
                if (this.CustomerID.isEmpty()) {
                    System.out.println("Retype!");
                } else {
                    break;
                }
            }
            while (true) {
                System.out.println("Enter Item ID: ");
                this.ItemID = getscanner().nextLine();
                while (rs2.next()) {
                    if (rs2 == null) {
                        System.out.println("Not have Item ID");
                    } else if (rs2.getString("ItemID").equalsIgnoreCase(this.ItemID)) {
                        pstmt.setString(3, this.ItemID);
                        break;
                    }
                }
                if (this.ItemID.isEmpty()) {
                    System.out.println("Retype!");
                } else {
                    break;
                }
            }
            while (true) {
                System.out.println("Enter Amount: ");
                this.O_amount = getscanner().nextInt();
                if (this.O_amount <= 0) {
                    System.out.println("Product's amount must be different 0 !");
                } else {
                    pstmt.setInt(4, this.O_amount);
                    break;
                }
            }
            while (true) {
                String D_type;

                String choice;
                this.DeliveryID = (this.DeliveryID + 1);
                while (true) {
                    System.out.println("Enter Delivery type: ");
                    D_type = getscanner().nextLine();
                    if (D_type.isEmpty()) {
                        System.out.println("Delivery is not empty!");
                    } else {
                        break;
                    }
                }
                while (true) {
                    System.out.println("Enter Fee shipping: ");
                    this.Fee_shipping = getscanner().nextInt();
                    if (this.Fee_shipping <= 0) {
                        System.out.println("Fee shipping must be different 0 !");
                    } else {
                        break;
                    }
                }
                pstmt5 = conn.prepareStatement(sql5);
                pstmt5.setInt(1, this.DeliveryID);
                pstmt5.setString(2, D_type);
                pstmt5.setInt(3, this.Fee_shipping);
                pstmt5.executeUpdate();
                pstmt.setInt(5, this.DeliveryID);
                while (true) {
                    System.out.println("Do you want enter extra Delivery type(Y/N): ");
                    choice = getscanner().nextLine();
                    if (choice.equalsIgnoreCase("n")) {
                        break;
                    } else if (choice.equalsIgnoreCase("y")) {
                        break;
                    } else {
                        System.out.println("Retype the selection");
                    }
                }
                if (choice.equalsIgnoreCase("n")) {
                    break;
                }
            }
            while (rs6.next()) {
                if (rs6.getString("ItemID").equalsIgnoreCase(this.ItemID)) {
                    this.Total_price = this.O_amount * (rs6.getInt("Cost") - rs6.getInt("Discount"))
                            + this.Fee_shipping;
                    pstmt.setInt(6, this.Total_price);
                    break;
                }
            }
            while (true) {
                String choice;
                this.PaymentID = (this.PaymentID + 1);
                while (true) {
                    System.out.println("Enter Payment type: ");
                    this.P_type = getscanner().nextLine();
                    if (this.P_type.isEmpty()) {
                        System.out.println("Please enter Payment type!");
                    } else {
                        break;
                    }
                }
                pstmt7 = conn.prepareStatement(sql7);
                pstmt7.setInt(1, this.PaymentID);
                pstmt7.setString(2, this.CustomerID);
                pstmt7.setString(3, this.P_type);
                pstmt7.executeUpdate();
                pstmt.setInt(7, this.PaymentID);
                while (true) {
                    System.out.println("Do you want enter extra Payment type(Y/N): ");
                    choice = getscanner().nextLine();
                    if (choice.equalsIgnoreCase("n")) {
                        break;
                    } else if (choice.equalsIgnoreCase("y")) {
                        break;
                    } else {
                        System.out.println("Retype the selection");
                    }
                }
                if (choice.equalsIgnoreCase("n")) {
                    break;
                }
            }
            while (true) {
                System.out.println("Enter date create order:");
                this.O_date = getscanner().nextLine();
                if (this.O_date.isEmpty()) {
                    System.out.println("Please enter Order date!");
                } else {
                    pstmt.setString(8, this.O_date);
                    break;
                }
            }
            while (true) {
                System.out.println("Enter Order status:");
                this.O_status = getscanner().nextLine();
                if (this.O_status.isEmpty()) {
                    System.out.println("Please enter Order status!");
                } else {
                    pstmt.setString(9, this.O_status);
                    break;
                }
            }
            pstmt.executeUpdate();
            System.out.println("ENTER SUCCESSFUL! COME BACK THE MAIN MENU!");
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }

    public Scanner getscanner() {
        return new Scanner(System.in);
    }

    public void search() throws SQLException {
        Connection conn = JDBCconection.getJDBCconnection();
        String sql8 = "SELECT ItemID,I_name,CategoryID,I_details,I_amount,I_brand,I_warehouse,I_status FROM search_view";
        stmt8 = conn.createStatement();
        rs8 = stmt8.executeQuery(sql8);
        try {
            System.out.println("Enter ItemID you want to search: ");
            this.search = getscanner().nextLine();
            while (rs8.next()) {
                if (rs8.getString("ItemID").equalsIgnoreCase(this.search)) {
                    System.out.println(
                            "+-----------------------------------------------------------------------------------------------------------------+");
                    System.out.printf("| %-7s| %-20s| %-11s| %-20s| %-10s| %-10s| %-10s| %-10s|\n", "Item ID", "Name",
                            "Category ID", "Details", "Amount", "Brand", "Warehouse", "Status");
                    System.out.println(
                            "+-----------------------------------------------------------------------------------------------------------------+");
                    System.out.printf("| %-7s| %-20s| %-11d| %-20s| %-10d| %-10s| %-10s| %-10s|",
                            rs8.getString("ItemID"), rs8.getString("I_name"), rs8.getInt("CategoryID"),
                            rs8.getString("I_details"), rs8.getInt("I_amount"), rs8.getString("I_brand"),
                            rs8.getString("I_warehouse"), rs8.getString("I_status"));
                    System.out.printf("\n");
                    System.out.println(
                            "+-----------------------------------------------------------------------------------------------------------------+");
                }
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }
}