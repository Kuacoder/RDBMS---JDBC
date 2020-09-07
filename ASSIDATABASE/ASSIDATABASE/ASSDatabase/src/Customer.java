import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Customer {
    PreparedStatement pstmt, pstmt1, pstmt2, pstmt3, pstmt4 = null;
    Statement stmt, stmt1 = null;
    ResultSet rs, rs1 = null;
    private String customerID ="";
    private String c_name;
    private String dateofbirth;
    private String address;
    private String c_gender;
    private String c_email;
    private String c_phonenumber;
    private String c_bank;

    public Customer() {
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getC_gender() {
        return c_gender;
    }

    public void setC_gender(String c_gender) {
        this.c_gender = c_gender;
    }

    public String getC_email() {
        return c_email;
    }

    public void setC_email(String c_email) {
        this.c_email = c_email;
    }

    public String getC_phonenumber() {
        return c_phonenumber;
    }

    public void setC_phonenumber(String c_phonenumber) {
        this.c_phonenumber = c_phonenumber;
    }

    public String getC_bank() {
        return c_bank;
    }

    public void setC_bank(String c_bank) {
        this.c_bank = c_bank;
    }

    public void input() throws SQLException {
        Connection conn = JDBCconection.getJDBCconnection();
        String sql = "INSERT INTO Customer VALUES(?,?,?,?,?,?)";
        String sql1 = "INSERT INTO Address VALUES(?,?)";
        String sql2 = "INSERT INTO Bank_Account VALUES(?,?)";
        String sql3 = "INSERT INTO Customer_address VALUES(?,?)";
        String sql4 = "SELECT max(AddressID) FROM Address";
        String sql5 = "SELECT max(BankID) FROM Bank_Account";
        String sql6 = "INSERT INTO Current_bank VALUES(?,?)";

        int addressID = 0;
        int bankID = 0;
        stmt = conn.createStatement();
        stmt1 = conn.createStatement();
        rs = stmt.executeQuery(sql4);
        rs1 = stmt1.executeQuery(sql5);

        pstmt = conn.prepareStatement(sql);
        pstmt4 = conn.prepareStatement(sql6);
        try {
            while (rs.next()) {
                if (rs == null) {
                    addressID = 0;
                } else {
                    addressID = rs.getInt("max(AddressID)");
                }
            }
            while (rs1.next()) {
                if (rs1 == null) {
                    bankID = 0;
                } else {
                    bankID = rs1.getInt("max(BankID)");
                }
            }
            System.out.println("                 CUSTOMER INFORMATION               ");
            while (true) {
                System.out.println("Enter your Customer ID(VD: C00): ");
                Boolean validateCustomerID = Customer.validateCustomerID(this.customerID);
                while (validateCustomerID==false){
                    this.customerID= getscanner().nextLine();
                    validateCustomerID = Customer.validateCustomerID(this.customerID);
                    if(validateCustomerID == false){
                        System.out.println("Please enter the correct format!");
                    }
                }
                    pstmt.setString(1, this.customerID);
                    break;
            }
            while (true) {
                System.out.println("Enter your Name: ");
                this.c_name = getscanner().nextLine();
                if (this.c_name.isEmpty()) {
                    System.out.println("You must enter your name!");
                } else {
                    pstmt.setString(2, this.c_name);
                    break;
                }
            }
            System.out.println("Enter your Date of Birth (yy/mm/dd): ");
            this.dateofbirth = getscanner().nextLine();
            pstmt.setString(3, this.dateofbirth);
            while (true) {
                System.out.println("Enter your Gender(M/F): ");
                this.c_gender = getscanner().nextLine();
                if (this.c_gender.isEmpty()) {
                    System.out.println("You must enter your Gender!");
                } else if (this.c_gender.equalsIgnoreCase("m") || this.c_gender.equalsIgnoreCase("f")) {
                    pstmt.setString(4, this.c_gender);
                    break;
                } else {
                    System.out.println("Please retype(M/F)!");
                }
            }
            while (true) {
                System.out.println("Enter your Email: ");
                this.c_email = getscanner().nextLine();
                if (this.c_email.isEmpty()) {
                    System.out.println("You must enter your Email!");
                }else{
                    pstmt.setString(5, this.c_email);
                    break;
                }
            }     
            while (true) {
                System.out.println("Enter your Phone Number: ");
                this.c_phonenumber = getscanner().nextLine();
                if (this.c_phonenumber.isEmpty()) {
                    System.out.println("You must enter your Phone Number!");
                } else {
                    pstmt.setString(6, this.c_phonenumber);
                    break;
                }
            }

            pstmt.executeUpdate();
            while (true) {
                String choice;
                addressID = (addressID + 1);
                while (true) {
                    System.out.println("Enter your Address: ");
                    this.address = getscanner().nextLine();
                    if (this.address.isEmpty()) {
                        System.out.println("You must enter your address!");
                    } else {
                        break;
                    }
                }
                pstmt1 = conn.prepareStatement(sql1);
                pstmt1.setInt(1, addressID);
                pstmt1.setString(2, this.address);
                pstmt1.executeUpdate();
                pstmt3 = conn.prepareStatement(sql3);
                pstmt3.setString(1, this.customerID);
                pstmt3.setInt(2, addressID);
                pstmt3.executeUpdate();
                while (true) {
                    System.out.println("Do you want enter extra address(Y/N): ");
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
                bankID = (bankID + 1);
                while (true) {
                    System.out.println("Enter your Bank Account: ");
                    this.c_bank = getscanner().nextLine();
                    if (this.c_bank.isEmpty()) {
                        System.out.println("You must enter your Bank Account!");
                    } else {
                        break;
                    }
                }
                pstmt2 = conn.prepareStatement(sql2);
                pstmt2.setInt(1, bankID);
                pstmt2.setString(2, this.c_bank);
                pstmt2.executeUpdate();
                pstmt4.setInt(1, bankID);
                pstmt4.setString(2, this.customerID);
                pstmt4.executeUpdate();
                while (true) {
                    System.out.println("Do you want enter extra Bank Account(Y/N): ");
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
            System.out.println("ENTER SUCCESSFUL! COME BACK THE MAIN MENU!");
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }

    public void hien() throws SQLException{
        Connection conn = JDBCconection.getJDBCconnection();
        String sql7 = "SELECT * FROM Customer";
        stmt = conn.createStatement();
        try {
            rs = stmt.executeQuery(sql7);
            while (rs.next()) {
                System.out.println(rs.getString("CustomerID")+" "+rs.getString("C_name")+" "+rs.getString("Dateofbirth")+" "+rs.getString("C_gender")+" "+rs.getString("C_email")+" "+rs.getString("C_phonenumber"));
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }

    public Scanner getscanner() {
        return new Scanner(System.in);
    }
    public static Boolean validateCustomerID(String customerID){
        Pattern pattern = Pattern.compile("^C[0-9]{1,2}$", Pattern.CASE_INSENSITIVE);
        return pattern.matcher(customerID).matches();
     }
 
}