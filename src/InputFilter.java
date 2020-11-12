import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

@WebFilter(filterName = "InputFilter")
public class InputFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        boolean invalid = false;
        Map params = request.getParameterMap();//Gets all the user inputs via the requests
        Enumeration<String> attributes = request.getAttributeNames();
        if(params != null){
            Iterator iter = params.keySet().iterator();
            while(iter.hasNext()){
                String key = (String) iter.next();
                String[] values = (String[]) params.get(key);//Makes an array of the values of the requests

                for(int i=0; i < values.length; i++) {
                    if (checkChars(values[i])) { //Checks if the value has an illegal character
                        invalid = true;
                        break;
                    }
                }
                if(invalid){
                    try{
                        RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                        request.setAttribute("message", "Illegal characters used");
                        dispatcher.include(request, response);
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
        }}
        request.setAttribute("filteredMessage","Success No Illegal characters used");
        chain.doFilter(request, response);}



    public void init(FilterConfig config) throws ServletException {

    }

    public static boolean checkChars(String value) {
        boolean illegalInput = false;
        String[] illegalChars = { "<", ">", "!", "{", " }", "insert", "into", "where", "script", "delete", "input" };

        for(int i = 0; i < illegalChars.length; i++){//Iterates through the bad characters seeing if any are a part of the value
            if(value.contains(illegalChars[i])){
                illegalInput = true;
                break;
            }
        }
        return illegalInput;
    }

}