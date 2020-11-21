import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.jms.BytesMessage;
import javax.jms.Session;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.*;
import java.util.Arrays;
import java.util.Base64;

@WebServlet("/AddUserNumbers")
public class AddUserNumbers extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /**
         * Add the user numbers from the form and encrypt them into the users txt file on a new line
         */
        String number1 = request.getParameter("number1");
        String number2 = request.getParameter("number2");
        String number3 = request.getParameter("number3");
        String number4 = request.getParameter("number4");
        String number5 = request.getParameter("number5");
        String number6 = request.getParameter("number6");

        String numberCombo =number1+" "+number2+" "+number3+" "+number4+" "+number5+" "+number6;

        //Below encrypts the data passing it to my encrypt method and then making it a string that can be written to a txt file
        byte[] numbersEncrypted = encrypt(numberCombo, request, response);
        //Converts to a string form to write to the txt file
        String numbersEncryptedConversion = Base64.getEncoder().encodeToString(numbersEncrypted);

        HttpSession session = request.getSession();
        String hash = session.getAttribute("password").toString();
        String hashReduced = hash.substring(0,20);
        File directory = new File("Draws");
        if (! directory.exists()){//This makes the folder if it doesn't exist yet in case it was not made in Listener.java
            directory.mkdir();
        }
        File userNumbers = new File("Draws",hashReduced+".txt");
        System.out.println("Encrypted numbers will be written to"+userNumbers.getAbsolutePath());
        session.setAttribute("path",userNumbers.getAbsolutePath());
        if(userNumbers.exists()){

        }
        else
        {userNumbers.createNewFile();}
        FileWriter fileWriter = new FileWriter(userNumbers.getAbsolutePath(), true);

        fileWriter.write(numbersEncryptedConversion +"\n");//Writes the encrypted data to the file and starts a new line so it can be read line by line

        fileWriter.close();


        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/account.jsp");
        dispatcher.forward(request, response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    protected static byte[] encrypt(String text , HttpServletRequest request, HttpServletResponse response) {
        /**
         * @param text Is the string of numbers we want to encrypt using AES
         * @return textEncrypted Is the string in its encrypted form
         */
        try{KeyGenerator keygenerator = null;
        SecretKey myAesKey = null;
        Cipher desCipher = null;


        HttpSession session = request.getSession();
        if(session.getAttribute("key") !=null){ //if the key already exists we retrieve it
        desCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        myAesKey = (SecretKey) session.getAttribute("key");}

        else {//if the key doesn't exist we make one
            // Create the cipher and the key
            keygenerator = KeyGenerator.getInstance("AES");
            myAesKey = keygenerator.generateKey();
            desCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            session.setAttribute("cipher",desCipher);
            session.setAttribute("key",myAesKey);
        }
        //Put the cipher in encryption mode with the key
        desCipher.init(Cipher.ENCRYPT_MODE, myAesKey);
        // The data must be in byte format to encrypt
        byte[] textBytes = text.getBytes();
        // Encrypts the text
        byte[] textEncrypted = desCipher.doFinal(textBytes);


        return textEncrypted;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

}

