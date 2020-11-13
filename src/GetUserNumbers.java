import javax.crypto.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.*;

@WebServlet("/GetUserNumbers")
public class GetUserNumbers extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String hash = session.getAttribute("password").toString();
        String hashReduced = hash.substring(0,20);//The files name is the first 20 chars of the hashed password

        File userNumbers = new File("Draws", hashReduced+".txt");

        if(!userNumbers.exists()){
            //This will happen when a user logs in but hasn't made any number draws yet so is sent to the account page as they have no file with numbers in yet
            //So as no file exists yet they are sent to the account page as theres no numbers to get
            RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
            request.setAttribute("password",session.getAttribute("password"));
            request.setAttribute("username",session.getAttribute("username"));
            dispatcher.forward(request, response);
            return;
        }
        System.out.println("Encrypted numbers will be read from"+userNumbers.getAbsolutePath());
        session.setAttribute("path",userNumbers.getAbsolutePath());
        FileReader myReader = new FileReader(userNumbers);
        BufferedReader br = new BufferedReader(myReader);
        ArrayList<String> draws = new ArrayList<String>();//This will be converted to an array later we are using it for arrayList functions
        String data = null;
        String line = br.readLine();
        //The while loop will read one line at a time and decrypt that line and add it to our arrayList draws
        while (line != null) {//reads line by line as we have written each encrypted string on a new line
        data = line;//Set the line to be decoded to the string data
        //Puts the line read back into byte form as we made it a string to write it here
        byte[] databytes = Base64.getDecoder().decode(data);
        line = br.readLine();

        try {

        Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        SecretKey myDesKey = (SecretKey) session.getAttribute("key");//Get the previously used key

        // Initialize the same cipher as used in AddUserNumbers for decryption
        desCipher.init(Cipher.DECRYPT_MODE, myDesKey);

        // Decrypt the text
        byte[] textDecrypted = desCipher.doFinal(databytes);

        //add it to the arrayList draws
        draws.add(new String(textDecrypted));
        //Set the normal array to the array list

        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    //Pass the information back to the accounts page
    String[] drawsArray = draws.toArray(draws.toArray(new String[0]));//Convert the ArrayList to a regular Array
    RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
    session.setAttribute("draws", drawsArray);//Set the session version of draws to be used in other functions
    request.setAttribute("draws", drawsArray);//Set the request version to be outputted on account.jsp
    request.setAttribute("password",session.getAttribute("password"));
    request.setAttribute("username",session.getAttribute("username"));
    dispatcher.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

}
