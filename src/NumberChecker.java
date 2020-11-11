import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet("/NumberChecker")
public class NumberChecker extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        ArrayList<String> arr = (ArrayList<String>) session.getAttribute("draws");//This is set after being read and decrypted from a txt in GetUserNumbers.java
        String winningDraw = (String) session.getAttribute("winningDraws");//This is set from the DB in the winningDraw() function in CreateAccount.java
        int j = 0; //The winning draw is 10 11 12 13 14 15
        if(arr != null){
        if(!arr.isEmpty())
            {
                for(int i = 0; i < arr.size(); i++) //Iterates through the arraylist of user draws
                {
                    if(arr.get(i).equals(winningDraw)){
                        //If the current item in the array list matches the winning ticket it alerts the user
                        // alert("Congratulations we have a winner");

                        if(session.getAttribute("path")!=null){
                            String filePath = (String) session.getAttribute("path");
                            PrintWriter writer = new PrintWriter(filePath);//Make a printwriter for the file as they will overwrite the file
                            writer.print("");//This will remove all draws in the file and replace it with nothing
                            writer.close();

                            RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
                            session.setAttribute("winCheck", "Congratulations you have WON!");
                            session.setAttribute("draws", null);//Sets the session for draws back to null also as it can outlive the txt file
                            dispatcher.forward(request, response);
                        }
                        }
                }
            }
        }
        if(j == 0){//j is changed to 1 if theres a match
            //No match so they have not won this time

            if(session.getAttribute("path")!=null){
                String filePath = (String) session.getAttribute("path");
                PrintWriter writer = new PrintWriter(filePath);//Make a printwriter for the file as they will overwrite the file
                writer.print("");//This will remove all draws in the file and replace it with nothing
                writer.close();}
            RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
            session.setAttribute("winCheck", "No win this time Unlucky");
            session.setAttribute("draws", null);//Sets the session for draws back to null also as it can outlive the txt file
            dispatcher.forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
