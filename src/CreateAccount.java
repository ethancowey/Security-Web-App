import com.mysql.cj.Session;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

@WebServlet("/CreateAccount")
public class CreateAccount extends HttpServlet {
    /**
     * Takes the input from the registration form and adds it into the database if it doesn't already exist
     * If successful an admin is sent to the admin_home.jsp page with and a user to the account.jsp page
     * If an account exists with the same credentials they are sent to the error.jsp page
     */


    private static Connection conn;
    private static PreparedStatement stmt;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // MySql database connection info
        String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        String USER = "user";
        String PASS = "password";

        // URLs to connect to database depending on your development approach
        // (NOTE: please change to option 1 when submitting)

        // 1. use this when running everything in Docker using docker-compose
        String DB_URL = "jdbc:mysql://db:3306/lottery";

        // 2. use this when running tomcat server locally on your machine and mysql database server in Docker
        //String DB_URL = "jdbc:mysql://localhost:33333/lottery";

        // 3. use this when running tomcat and mysql database servers on your machine
        //String DB_URL = "jdbc:mysql://localhost:3306/lottery";

        // get parameter data that was submitted in HTML form (use form attributes 'name')
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String username = request.getParameter("username");
        String admin = request.getParameter("admin");
        String hash = getHash(request.getParameter("password").toString());

        HttpSession session = request.getSession();
        session.setAttribute("dburl",DB_URL);
        session.setAttribute("winningDraws",winningDraw(DB_URL));

        try {
          //addAdminToDB(DB_URL);
          Class.forName(JDBC_DRIVER);
          conn = DriverManager.getConnection(DB_URL, USER, PASS);

          String query = "INSERT INTO userAccounts (Firstname, Lastname, Email, Phone, Username, Pwd, ADMIN)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?)";


          // set values into SQL query statement
          stmt = conn.prepareStatement(query);
          stmt.setString(1, firstname);
          stmt.setString(2, lastname);
          stmt.setString(3, email);
          stmt.setString(4, phone);
          stmt.setString(5, username);
          stmt.setString(6, hash);
          stmt.setString(7, admin);

          // execute query and close connection
          stmt.execute();
          conn.close();
          session.setAttribute("firstname", firstname);
          session.setAttribute("lastname", lastname);
          session.setAttribute("email", email);
          session.setAttribute("phone", phone);
          session.setAttribute("username", username);
          session.setAttribute("password", hash);
          session.setAttribute("admin",admin);



          if (request.getParameter("admin") != null) {
                // display admin.jsp page with given message if successful admin account
                RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/admin_home.jsp");
                session.setAttribute("message", firstname + ", you have successfully made an admin account");
                dispatcher.forward(request, response);
          } else {
                // display account.jsp page with given message if successful non admin account
                RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
                session.setAttribute("message", firstname + ", you have successfully created an account");
                session.setAttribute("message2", "Your details Firstname: "+firstname +" Lastname: "+lastname+" " +
                        " Email: "+email+" Number: "+phone+" Username: "+username);
                dispatcher.forward(request, response);
            }

        } catch (Exception se) {
            se.printStackTrace();
            // display error.jsp page with given message if unsuccessful
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            request.setAttribute("message", firstname + ", this username/password combination already exists. Please try again");
            dispatcher.forward(request, response);
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected static String getHash(String input) {
        /**
        * @param input Is the passworrd we want to hash
         * @return hash Is the hashed password using SHA-256
         */
        try {
            //Call the hashing algorithm SHA-256
            MessageDigest msgDigest = MessageDigest.getInstance("SHA-256");
            //Hash the input in byte form
            byte[] inpDigest = msgDigest.digest(input.getBytes());
            BigInteger inpDigestBigInt = new BigInteger(1, inpDigest);
            // Converts the bytes into a string hex value
            String hash = inpDigestBigInt.toString(16);
            //The hash will be 64 digits long
            return hash;
        }
        // Catch block to handle if SHA-256 doesn't exist
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    protected static String winningDraw(String url){
        /**
         * @param url the database url to connect to
         * @return winningDraw returns the winning draw 10 11 12 13 14 15 which is written and read from the database
         */
        String DB_URL = url;
        String winningDraw = null; //this will be the variable where the winning numbers from the database is saved to
        String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        String USER = "user";
        String PASS = "password";
        try {
            // create database connection and statement
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            //Add new data into the winning draws table I added to data.sql
            String sqlInsert = "INSERT INTO winningDraws " +
                    "VALUE('10 11 12 13 14 15')";//Winning draw is 10 11 12 13 14 15
            stmt = conn.prepareStatement(sqlInsert);
            stmt.execute();
            //Select the winning draws from the database
            String getWinning = "SELECT * FROM winningDraws";
            stmt = conn.prepareStatement(getWinning);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
            winningDraw =rs.getString("winningNumber");
            return winningDraw; //return the winning draws so it can be used to compare to user draws
            }
        }catch (Exception se){}


    return winningDraw;
    }


}
