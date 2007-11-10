package NetBasics.Firewalls;

// TimeServlet.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Return a text page to the client containing the 
 current date and time.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TimeServlet extends HttpServlet {
    /**
     * 
     */
    private static final long serialVersionUID = -4999953753568611156L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // response.setContentType( "text/txt" ); // content type

        SimpleDateFormat formatter = new SimpleDateFormat("d M yyyy HH:mm:ss");
        Date today = new Date();
        String todayStr = formatter.format(today);
        System.out.println("Today is: " + todayStr);

        PrintWriter output = response.getWriter();
        output.println(todayStr); // send date & time
        output.close();
    }

} // end of TimeServlet class

