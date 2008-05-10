package NetBasics.Firewalls;

// TimeClient.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* Contact the TimeServlet running locally at 
 http://localhost:8080/servlet/TimeServlet
 and print its response.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class TimeClient {
    public static void main(String args[]) throws IOException {
        URL url = new URL("http://localhost:8100/servlet/TimeServlet");
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
        br.close();
    }
} // end of TimeClient class
