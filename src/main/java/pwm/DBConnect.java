package pwm;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static pwm.PasswordManager.decrypt;

public class DBConnect {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:../../../../pwm-db";

    //  Database credentials
    static final String USER = "sa";
    static final String PASS = "";

    public static void createTable(){

        Connection conn = null;
        Statement stmt = null;

        try {
            // STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 2: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            // STEP 3: Execute a query
            System.out.println("Connected database successfully...");
            stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS PASSWORDS (\n" +
                    "    WEBSITE VARCHAR(255) NOT NULL,\n" +
                    "    USERNAME VARCHAR(255) NOT NULL,\n" +
                    "    PASSWORD CHAR(64) NOT NULL,\n" +
                    "    CREATED TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
                    "    ID SERIAL PRIMARY KEY\n" +
                    ");";
            stmt.execute(sql);


            // STEP 5: Clean-up environment
        } catch(SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        } catch(Exception e) {
            // Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            // finally block used to close resources
            try {
                if(stmt!=null) stmt.close();
            } catch(SQLException se2) {
            } // nothing we can do
            try {
                if(conn!=null) conn.close();
            } catch(SQLException se) {
                se.printStackTrace();
            } // end finally try
        } // end try



    }

    public static List<Password> getPasswords() {
        //createTable();
        Connection conn = null;
        Statement stmt = null;
        List<Password> passwords = new ArrayList<>();

        try {
            // STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER);
            URL location = DBConnect.class.getProtectionDomain().getCodeSource().getLocation();
            System.out.println(location.getFile());

            //STEP 2: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            // STEP 3: Execute a query
            System.out.println("Connected database successfully...");
            stmt = conn.createStatement();
            String sql = "SELECT * FROM PASSWORDS";
            ResultSet rs = stmt.executeQuery(sql);


            // STEP 4: Extract data from result set
            while(rs.next()) {
                // Retrieve by column name
                int id  = rs.getInt("id");
                String username = rs.getString("username");
                String website = rs.getString("website");
                String password = rs.getString("password");
                Time time = rs.getTime("created");
                Date date = rs.getDate("created");

                Password item = new Password(website, username, password, date, time, id);
                passwords.add(item);


            }
            // STEP 5: Clean-up environment
            rs.close();
        } catch(SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        } catch(Exception e) {
            // Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            // finally block used to close resources
            try {
                if(stmt!=null) stmt.close();
            } catch(SQLException se2) {
            } // nothing we can do
            try {
                if(conn!=null) conn.close();
            } catch(SQLException se) {
                se.printStackTrace();
            } // end finally try
        } // end try
        return passwords;
    }

    public static void updatePassword(Password password, String newPassword){
        Connection conn = null;
        Statement stmt = null;

        try {
            // STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 2: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            // STEP 3: Execute a query
            System.out.println("Connected database successfully...");
            stmt = conn.createStatement();
            String sql = String.format("UPDATE PASSWORDS " +
                    "SET PASSWORD = '%s'" +
                    "WHERE ID = %d;", newPassword, password.getid());
            stmt.execute(sql);


            // STEP 5: Clean-up environment
        } catch(SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        } catch(Exception e) {
            // Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            // finally block used to close resources
            try {
                if(stmt!=null) stmt.close();
            } catch(SQLException se2) {
            } // nothing we can do
            try {
                if(conn!=null) conn.close();
            } catch(SQLException se) {
                se.printStackTrace();
            } // end finally try
        } // end try
    }

    public static String addPassword(Password password){
        Connection conn = null;
        Statement stmt = null;
        List<Password> passwords = new ArrayList<>();

        //Check if password already exists
        List<Password> pws = getPasswords();
        for (Password pw: pws){
            if (pw.getUsername().equals(password.getUsername())
                    && pw.getWebsite().equals(password.getWebsite())){

                System.out.println("Password already exists... updating");
                updatePassword(pw, password.getPassword_encrypted());
                return "Updated!";
            }
        }



        try {
            // STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 2: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            // STEP 3: Execute a query
            System.out.println("Connected database successfully...");
            stmt = conn.createStatement();
            String sql = String.format(("INSERT INTO PASSWORDS (WEBSITE, USERNAME, PASSWORD)" +
                            "VALUES ('%s', '%s', '%s');"),password.getWebsite(),
                    password.getUsername(),
                    password.getPassword_encrypted());
            stmt.execute(sql);
            System.out.println("Password added successfully!");
        } catch(SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        } catch(Exception e) {
            // Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            // finally block used to close resources
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            } // nothing we can do
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            } // end finally try
        }// end try
        return "Added!";
    }

    public static boolean deletePassword(String website, String username){

        List<Password> passwords = getPasswords();
        for (Password pw : passwords) {

            if (pw.getUsername().equals(username) && pw.getWebsite().equals(website)) {
                Connection conn = null;
                Statement stmt = null;
                try {


                    // STEP 1: Register JDBC driver
                    Class.forName(JDBC_DRIVER);

                    //STEP 2: Open a connection
                    System.out.println("Connecting to database...");
                    conn = DriverManager.getConnection(DB_URL,USER,PASS);
                    // STEP 3: Execute a query
                    System.out.println("Connected database successfully...");
                    stmt = conn.createStatement();
                    String sql = String.format("DELETE FROM PASSWORDS\n" +
                            "WHERE ID = %d;", pw.getid());
                    stmt.execute(sql);
                    System.out.println("Password added successfully!");
                } catch(SQLException se) {
                    // Handle errors for JDBC
                    se.printStackTrace();
                } catch(Exception e) {
                    // Handle errors for Class.forName
                    e.printStackTrace();
                } finally {
                    // finally block used to close resources
                    try {
                        if(stmt!=null) stmt.close();
                    } catch(SQLException se2) {
                    } // nothing we can do
                    try {
                        if(conn!=null) conn.close();
                    } catch(SQLException se) {
                        se.printStackTrace();
                    } // end finally try
                } // end try

                System.out.println("Deleted successfully!");

                return true;
            }

        }
        System.out.println("Not found!");
        return false;

    }



    public static String copyPassword(String website, String username){

        List<Password> passwords = getPasswords();
        for (Password pw : passwords) {

            if (pw.getUsername().equals(username) && pw.getWebsite().equals(website)) {
                String foundPw = decrypt(pw.getPassword_encrypted());
                StringSelection copiedPw = new StringSelection((foundPw));
                //Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                //clipboard.setContents(copiedPw, null);
                System.out.println("Copied!");
                return foundPw;
            }

        }
        System.out.println("Not found!");

        return "NOT FOUND";
    }

    public static void main(String[] args) {
        //createTable();
        //Password pw = new Password("teeb.com", "test", "A");
        //addPassword(pw);
        List<Password> pws = getPasswords();
        System.out.println(pws.get(0).getDate());
        //System.out.println(pws.get(4).getTime());
        //String web = pws.get(4).getWebsite();
        //String user = pws.get(4).getUsername();
        /*for (Password pw_: pws){
            System.out.println(pw_.getWebsite() + " " + pw_.getid() + " " + pw_.getPassword_encrypted());
        }
        deletePassword("teeb.com", "test");
        pws = getPasswords();
        for (Password pw_: pws){
            System.out.println(pw_.getWebsite() + " " + pw_.getid() + " " + pw_.getPassword_encrypted());
        }
        //copyPassword(user, web);*/
    }
}



