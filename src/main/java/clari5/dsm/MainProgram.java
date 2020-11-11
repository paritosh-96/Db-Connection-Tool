package clari5.dsm;

import java.sql.*;
import java.util.Scanner;

public class MainProgram {
    private static final DbDetails details = new DbDetails();
    private static Scanner sc;
    private static int noOfInserts;

    private static final String insertQuery = "INSERT INTO DSM_SEED_TEST VALUES(?,?)";
    private static final String dropTableQuery = "DROP TABLE DSM_SEED_TEST";

    public static void main(String[] args) throws SQLException {
        sc = new Scanner(System.in);
        getDbDetails();
        Connection con = null;
        Statement st = null;

        try {
            String url = "";
            switch (details.dbType) {
                case ORACLE:
                    DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                    url = "jdbc:oracle:thin:@" + details.ip + ":" + details.port + ":" + details.sId;
                    break;
                case SQLSERVER:
                    DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
                    url = "jdbc:sqlserver://" + details.ip + ":" + details.port + ";database=" + details.sId + "";
                    break;
                case MYSQL:
                    DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
                    url = "jdbc:mysql://" + details.ip + ":" + details.port + "/" + details.sId + "";
            }
            con = DriverManager.getConnection(url, details.user, details.password);
            if (con == null) {
                throw new SQLException("Connection could not be created");
            }
            System.out.println("DB connection test successfully!!");

            // test db operations
            if (details.dbOperation) {
                st = con.createStatement();
                con.setAutoCommit(false);
                st.executeUpdate(details.getCreateQuery());
                insertData(noOfInserts, con);
                st = con.createStatement();
                st.executeUpdate(dropTableQuery);
                System.out.println("Tests done:\n1) Table Creation\n2) Data insertion\n3) Table dropped");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                con.close();
            if (st != null)
                st.close();
        }

    }

    private static void insertData(int n, Connection con) {
        try {
            for (int i = 0; i < n; i++) {
                PreparedStatement ps = con.prepareStatement(insertQuery);
                ps.setInt(1, i + 1);
                ps.setString(2, "abc" + (i + 1));
                ps.execute();
            }
            con.commit();
            System.out.println("Inserted [" + n + "]  data in DSM_SEED_TEST table");
        } catch (SQLException ex) {
            System.out.println("Exception while inserting data");
            ex.printStackTrace();
        }
    }

    public static void getDbDetails() {
        System.out.println("Enter db details");
        System.out.print("Db Type:");
        details.setDbType(sc.nextLine());
        System.out.print("Ip: ");
        details.ip = sc.nextLine();
        System.out.print("Port: ");
        details.port = sc.nextLine();
        System.out.print("SID: ");
        details.sId = sc.nextLine();
        System.out.print("User: ");
        details.user = sc.nextLine();
        System.out.print("Password: ");
        details.password = sc.nextLine();
        System.out.println("Test Db Operation [true|false]: ");
        details.dbOperation = ("true").equals(sc.nextLine().trim().toLowerCase());
        if (details.dbOperation) {
            System.out.print("No of inserts [default:2]: ");
            String number = sc.nextLine();
            if (number == null || number.isEmpty()) noOfInserts = 2;
            else noOfInserts = Integer.parseInt(number);
        }
    }
}
