import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.*;
import javax.servlet.http.HttpServletRequest;

@WebFilter(filterName = "InputFilter")
public class InputFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        /**
         * Checks all request attributes for illegal characters in them
         * If it does have an illegal character the user is sent to the error.jsp page
         */
        boolean illegalInput = false;
        String[] illegalChars = { "<", ">", "!", "{", " }", "insert", "into", "where", "script", "delete", "input" };
        for(int i = 0; i < illegalChars.length; i++){//Iterates through the bad characters
            Enumeration<String> toCheck = request.getParameterNames();
            while (toCheck.hasMoreElements()) {//Iterates through the request object values
                String value = request.getParameter(toCheck.nextElement()); //Set the value of the request to a variable
                    if(value.contains(illegalChars[i])){//Checks if the value contains the illegal characters
                        illegalInput =  true; //Make true so that when it returns it shows theres an illegal character
                }
            }
        }

        if(illegalInput){
            try{
                RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                request.setAttribute("message", "Illegal characters used");
                dispatcher.include(request, response);//Sends the user to the error page for using an illegal character
                return;
            }
            catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
        }
        else{
            request.setAttribute("filteredMessage","Success No Illegal characters used");
            chain.doFilter(request, response);
            return;
        }
        }



    public void init(FilterConfig config) throws ServletException {

    }
}