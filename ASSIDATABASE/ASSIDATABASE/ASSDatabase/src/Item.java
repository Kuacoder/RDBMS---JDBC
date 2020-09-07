import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Item {
    PreparedStatement pstmt, pstmt1, pstmt5, pstmt3, pstmt4, pstmt6, pstmt7, pstmt8, pstmt9, pstmt10 = null;
    Statement stmt, stmt1, stmt2, stmt3, stmt4 = null;
    ResultSet rs, rs1, rs2, rs3, rs4 = null;
    private String ItemID = "";
    private String I_name;
    private int CategoryID = 0;
    private String I_details;
    private int I_amount = 0;
    private String I_brand;
    private String I_warehouse;
    private String I_status;

    int i_Cost = 0;
    int discount = 0;
    String from_date;
    String to_date;

    public Item() {
    }

    public String getItemID() {
        return ItemID;
    }

    public void setItemID(String itemID) {
        ItemID = itemID;
    }

    public String getI_name() {
        return I_name;
    }

    public void setI_name(String i_name) {
        I_name = i_name;
    }

    public int getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(int categoryID) {
        CategoryID = categoryID;
    }

    public String getI_details() {
        return I_details;
    }

    public void setI_details(String i_details) {
        I_details = i_details;
    }

    public int getI_amount() {
        return I_amount;
    }

    public void setI_amount(int i_amount) {
        I_amount = i_amount;
    }

    public String getI_brand() {
        return I_brand;
    }

    public void setI_brand(String i_brand) {
        I_brand = i_brand;
    }

    public String getI_warehouse() {
        return I_warehouse;
    }

    public void setI_warehouse(String i_warehouse) {
        I_warehouse = i_warehouse;
    }

    public String getI_status() {
        return I_status;
    }

    public void setI_status(String i_status) {
        I_status = i_status;
    }

    public void input() throws SQLException {
        Connection conn = JDBCconection.getJDBCconnection();
        String sql = "INSERT INTO Item VALUES(?,?,?,?,?,?,?,?)";
        String sql1 = "INSERT INTO Pictures VALUES(?,?)";//

        String sql3 = "INSERT INTO Pictures_item VALUES(?,?)";//
        String sql4 = "SELECT max(CategoryID) FROM Category";//
        String sql5 = "SELECT max(PictureID) FROM Pictures";//
        String sql6 = "INSERT INTO Category VALUES(?,?)";//
        String sql7 = "INSERT INTO Colors VALUES(?,?)";//
        String sql8 = "SELECT max(ColorsID) FROM Colors";//
        String sql9 = "INSERT INTO Item_corlors VALUES(?,?)";//
        String sql10 = "SELECT max(SizeID) FROM Sizes";//
        String sql11 = "INSERT INTO Sizes VALUES(?,?)";//
        String sql12 = "INSERT INTO Item_sizes VALUES(?,?)";//
        String sql13 = "SELECT max(PriceID) FROM Price";//
        String sql14 = "INSERT INTO Price VALUES(?,?,?,?,?)";//
        String sql15 = "INSERT INTO Current_price VALUES(?,?)";//
        int CategoryID = 0;
        int PictureID = 0;
        int ColorsID = 0;
        int SizeID = 0;
        int PriceID = 0;
        stmt = conn.createStatement();
        stmt1 = conn.createStatement();
        stmt2 = conn.createStatement();
        stmt3 = conn.createStatement();
        stmt4 = conn.createStatement();
        rs = stmt.executeQuery(sql4);
        rs1 = stmt1.executeQuery(sql5);
        rs2 = stmt2.executeQuery(sql8);
        rs3 = stmt3.executeQuery(sql10);
        rs4 = stmt4.executeQuery(sql13);

        pstmt = conn.prepareStatement(sql);

        try {
            while (rs.next()) {
                if (rs == null) {
                    CategoryID = 0;
                } else {
                    CategoryID = rs.getInt("max(CategoryID)");
                }
            }
            while (rs1.next()) {
                if (rs1 == null) {
                    PictureID = 0;
                } else {
                    PictureID = rs1.getInt("max(PictureID)");
                }
            }
            while (rs2.next()) {
                if (rs2 == null) {
                    ColorsID = 0;
                } else {
                    ColorsID = rs2.getInt("max(ColorsID)");
                }
            }
            while (rs3.next()) {
                if (rs3 == null) {
                    SizeID = 0;
                } else {
                    SizeID = rs3.getInt("max(SizeID)");
                }
            }

            while (rs4.next()) {
                if (rs4 == null) {
                    PriceID = 0;
                } else {
                    PriceID = rs4.getInt("max(PriceID)");
                }
            }
            System.out.println("                  ITEM INFORMATION             ");
       
            while (true) {
                System.out.println("Enter Item ID(VD: I000): ");
                Boolean validateItemID = Item.validateItemID(this.ItemID);
                while (validateItemID==false){
                    this.ItemID= getscanner().nextLine();
                    validateItemID = Item.validateItemID(this.ItemID);
                    if(validateItemID == false){
                        System.out.println("Please enter the correct format!");
                    }
                }
                    pstmt.setString(1, this.ItemID);
                    break;
            }
            while (true) {
                System.out.println("Enter Product's Name: ");
                this.I_name = getscanner().nextLine();
                if (this.I_name.isEmpty()) {
                    System.out.println("You must enter your name!");
                } else {
                    pstmt.setString(2, this.I_name);
                    break;
                }
            }
            while (true) {
                String C_type;
                String choice;
                CategoryID = (CategoryID + 1);
                while (true) {
                    System.out.println("Enter Product's Category: ");
                    C_type = getscanner().nextLine();
                    if (C_type.isEmpty()) {
                        System.out.println("Category is not empty!");
                    } else {
                        break;
                    }
                }
                pstmt4 = conn.prepareStatement(sql6);
                pstmt4.setInt(1, CategoryID);
                pstmt4.setString(2, C_type);
                pstmt4.executeUpdate();
                pstmt.setInt(3, CategoryID);
                while (true) {
                    System.out.println("Do you want enter extra Category(Y/N): ");
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

            System.out.println("Enter Product's Detail:");
            this.I_details = getscanner().nextLine();
            pstmt.setString(4, this.I_details);
            while (true) {
                System.out.println("Enter Amount: ");
                this.I_amount = getscanner().nextInt();
                if (this.I_amount <= 0) {
                    System.out.println("Product's amount must be different 0 !");
                } else {
                    pstmt.setInt(5, this.I_amount);
                    break;
                }
            }
            while (true) {
                System.out.println("Enter Brand: ");
                this.I_brand = getscanner().nextLine();
                if (this.I_brand.isEmpty()) {
                    System.out.println("You must enter Product's Brand!");
                } else {
                    pstmt.setString(6, this.I_brand);
                    break;
                }
            }
            System.out.println("Enter Product's Warehouse: ");
            this.I_warehouse = getscanner().nextLine();
            pstmt.setString(7, this.I_warehouse);
            while (true) {
                System.out.println("Enter Item's Status: ");
                this.I_status = getscanner().nextLine();
                if (this.I_status.isEmpty()) {
                    System.out.println("You must enter Product's Status!");
                } else {
                    pstmt.setString(8, this.I_status);
                    break;
                }
            }
            pstmt.executeUpdate();
            while (true) {
                String choice;
                String I_image;
                PictureID = (PictureID + 1);
                while (true) {
                    System.out.println("Enter your Image's name: ");
                    I_image = getscanner().nextLine();
                    if (I_image.isEmpty()) {
                        System.out.println("You must enter Image's name!");
                    } else {
                        break;
                    }
                }
                pstmt1 = conn.prepareStatement(sql1);
                pstmt1.setInt(1, PictureID);
                pstmt1.setString(2, I_image);
                pstmt1.executeUpdate();
                pstmt3 = conn.prepareStatement(sql3);
                pstmt3.setInt(1, PictureID);
                pstmt3.setString(2, this.ItemID);
                pstmt3.executeUpdate();
                while (true) {
                    System.out.println("Do you want enter extra Image's names(Y/N): ");
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
                String choice;
                String C_type;
                ColorsID = (ColorsID + 1);
                while (true) {
                    System.out.println("Enter Product's Color: ");
                    C_type = getscanner().nextLine();
                    if (C_type.isEmpty()) {
                        System.out.println("You must enter Product's Color!");
                    } else {
                        break;
                    }
                }
                pstmt5 = conn.prepareStatement(sql7);
                pstmt5.setInt(1, ColorsID);
                pstmt5.setString(2, C_type);
                pstmt5.executeUpdate();
                pstmt6 = conn.prepareStatement(sql9);
                pstmt6.setString(1, this.ItemID);
                pstmt6.setInt(2, ColorsID);
                pstmt6.executeUpdate();
                while (true) {
                    System.out.println("Do you want enter extra Color(Y/N): ");
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
                String choice;
                String S_type;
                SizeID = (SizeID + 1);
                while (true) {
                    System.out.println("Enter Product's Size: ");
                    S_type = getscanner().nextLine();
                    if (S_type.isEmpty()) {
                        System.out.println("You must enter Product's Size!");
                    } else {
                        break;
                    }
                }
                pstmt7 = conn.prepareStatement(sql11);
                pstmt7.setInt(1, SizeID);
                pstmt7.setString(2, S_type);
                pstmt7.executeUpdate();
                pstmt8 = conn.prepareStatement(sql12);
                pstmt8.setString(1, this.ItemID);
                pstmt8.setInt(2, SizeID);
                pstmt8.executeUpdate();
                while (true) {
                    System.out.println("Do you want enter extra Sizes(Y/N): ");
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
                String choice;

                PriceID = (PriceID + 1);
                while (true) {
                    System.out.println("Enter Product's Cost: ");
                    i_Cost = getscanner().nextInt();
                    if (i_Cost <= 0) {
                        System.out.println("You must enter Product's Cost!");
                    } else {
                        break;
                    }
                }
                while (true) {
                    System.out.println("Enter Product's Discount: ");
                    this.discount = getscanner().nextInt();
                    if (discount > 0) {
                        System.out.println("Enter Product's Discount from date(yy/mm/dd): ");
                        this.from_date = getscanner().nextLine();
                        System.out.println("Enter Product's Discount to date(yy/mm/dd): ");
                        this.to_date = getscanner().nextLine();
                        break;
                    } else if (this.discount == 0){
                        break;    
                    }
                    else{
                        System.out.println("Please enter the value");
                    }
                }

                pstmt9 = conn.prepareStatement(sql14);
                pstmt9.setInt(1, PriceID);
                pstmt9.setInt(2, this.i_Cost);
                pstmt9.setInt(3, this.discount);
                pstmt9.setString(4, this.from_date);
                pstmt9.setString(5, this.to_date);
                pstmt9.executeUpdate();
                pstmt10 = conn.prepareStatement(sql15);
                pstmt10.setInt(1, PriceID);
                pstmt10.setString(2, this.ItemID);
                pstmt10.executeUpdate();
                break;
            }
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
    public static Boolean validateItemID(String ItemID){
        Pattern pattern = Pattern.compile("^I[0-9]{1,2}$", Pattern.CASE_INSENSITIVE);
        return pattern.matcher(ItemID).matches();
     }
     
}