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
        System.out.println("here");
        String number1 = request.getParameter("number1");
        String number2 = request.getParameter("number2");
        String number3 = request.getParameter("number3");
        String number4 = request.getParameter("number4");
        String number5 = request.getParameter("number5");
        String number6 = request.getParameter("number6");

        String numberCombo =number1+" "+number2+" "+number3+" "+number4+" "+number5+" "+number6;
        //Below encrypts the data passing it to my encrypt method and then making it a string that can be writtten to a txt file
        byte[] comboEncrypted = encrypt(numberCombo, request, response);
        String comboEncryptedConversion = Base64.getEncoder().encodeToString(comboEncrypted);

        HttpSession session = request.getSession();
        String hash = session.getAttribute("password").toString();
        String hashReduced = hash.substring(0,20);
        File directory = new File("Draws");
        File userNumbers = new File("Draws",hashReduced+".txt");
        System.out.println("Encrypted numbers will be written to"+userNumbers.getAbsolutePath());
        session.setAttribute("path",userNumbers.getAbsolutePath());
        if(userNumbers.exists()){

        }
        else
        {userNumbers.createNewFile();}
        FileWriter fileWriter = new FileWriter(userNumbers.getAbsolutePath(), true);

        fileWriter.write(comboEncryptedConversion +"\n");//Writes the encrypted data to the file and starts a new line so it can be read line by line

        fileWriter.close();


        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/account.jsp");
        dispatcher.forward(request, response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    protected static byte[] encrypt(String text , HttpServletRequest request, HttpServletResponse response) {
        try{KeyGenerator keygenerator = null;
        SecretKey myDesKey = null;
        Cipher desCipher = null;


        HttpSession session = request.getSession();
        if(session.getAttribute("key") !=null){ //if the key already exists we retrieve it
        desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        myDesKey = (SecretKey) session.getAttribute("key");}

        else {//if the key doesn't exist we make one
            // Create the cipher and the key
            keygenerator = KeyGenerator.getInstance("DES");
            myDesKey = keygenerator.generateKey();
            desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            session.setAttribute("cipher",desCipher);
            session.setAttribute("key",myDesKey);
        }

        desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);
        // It must be in byte format to encrypt
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

