import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

@WebFilter(filterName = "InputFilter")
public class InputFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        boolean invalid = false;
        Map params = request.getParameterMap();
        if(params != null){
            Iterator iter = params.keySet().iterator();
            while(iter.hasNext()){
                String key = (String) iter.next();
                String[] values = (String[]) params.get(key);

                for(int i=0; i < values.length; i++) {
                    if (checkChars(values[i])) {
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
                    chain.doFilter(request, response);
                    return;
                }
        }}
        chain.doFilter(request, response);}



    public void init(FilterConfig config) throws ServletException {

    }

    public static boolean checkChars(String value) {
        boolean invalid = false;
        String[] badChars = { "<", ">", "!", "{", " }", "insert", "into", "where", "script", "delete",
                "input" };

        for(int i = 0; i < badChars.length; i++){
            if(value.contains(badChars[i])){
                invalid = true;
                break;
            }
        }
        return invalid;
    }

}