import com.mysql.cj.Session;

import javax.crypto.SecretKey;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Enumeration;


@WebServlet("/UserLogin")
public class UserLogin extends HttpServlet {

    private Connection conn;
    private Statement stmt;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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



        //Set the username and password thats been hashed to variables to be used in SQL statements
        String username = request.getParameter("username");
        String hash = CreateAccount.getHash(request.getParameter("password"));//Calls hash function I made in CreateAccount.java
        HttpSession session = request.getSession();
        session.setAttribute("winningDraws",CreateAccount.winningDraw(DB_URL));//Get  the wining draws from the db using this function

        //Set the login attempts counter up by 1
        if(session.getAttribute("attempts")==null){
            session.setAttribute("attempts", 1);
        }
        else{
            session.setAttribute("attempts",(int)session.getAttribute("attempts")+1);
        }
        try {
            // create database connection and statement
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            // query database and get results for matching credentials
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM userAccounts WHERE username=? AND pwd=?");
            ps.setString(1, username);
            ps.setString(2, hash);
            ResultSet rs = ps.executeQuery();


            Boolean found = false;

            while (rs.next() && found == false) {
                if (rs.getString("Username").equals(username) && rs.getString("Pwd").equals(hash)) {
                    //Sets all the user data in the file to session attributes to be used later
                    session.setAttribute("firstname", rs.getString("Firstname"));
                    session.setAttribute("lastname", rs.getString("Lastname"));
                    session.setAttribute("email", rs.getString("Email"));
                    session.setAttribute("phone", rs.getString("Phone"));
                    session.setAttribute("admin", rs.getString("ADMIN"));
                    session.setAttribute("username", username);
                    session.setAttribute("password",hash);
                    found = true;
                }
            }
            // close connection
            conn.close();
            if (found == true) {

                // display a different page with given content above if successful admin or regular user
                String firstname = session.getAttribute("firstname").toString();
                String lastname = session.getAttribute("lastname").toString();
                String email = session.getAttribute("email").toString();
                String phone = session.getAttribute("phone").toString();

                //If its an admin logging in they will be directed to admin_home.jsp
                if(session.getAttribute("admin") != null){
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/admin_home.jsp");
                    session.setAttribute("message", "Login Successful Admin");
                    dispatcher.forward(request, response);
                }
                //If its a regular user they are sent to account.jsp page
                else{
                RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
                session.setAttribute("message", "Login Successful");
                session.setAttribute("message2", "Your details: "+firstname +" "+lastname+" "+email+" "+phone+" "+username);
                dispatcher.forward(request, response);}

            } else { //If theres no match in the database they are sent here as the login failed
                RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                request.setAttribute("message", "Login Unsuccessful you have "+(3 - (int)session.getAttribute("attempts"))+" attempts remaining");
                dispatcher.forward(request, response);
            }


        } catch (Exception se) {
            se.printStackTrace();
            // display error.jsp page with given message if database error
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            request.setAttribute("message", "Database Error, Please try again");
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
        //below removes all session attributes and returns the user to the home page acting as a logout
        HttpSession session = request.getSession();
        Enumeration<String> attributes = session.getAttributeNames();
        SecretKey myDesKey = (SecretKey) session.getAttribute("key");//We need to save this key to read the users draws txt file if they login again
        while (attributes.hasMoreElements()) { //this iterates through and removes all session attributes
            String attributeName = attributes.nextElement();
            session.removeAttribute(attributeName);
            }
        session.setAttribute("key",myDesKey); //We need to save this key to read the users draws txt file if they login again
        System.out.println("Logged Out");
        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");//sends the user back to the home page
        dispatcher.forward(request, response);
    }
}