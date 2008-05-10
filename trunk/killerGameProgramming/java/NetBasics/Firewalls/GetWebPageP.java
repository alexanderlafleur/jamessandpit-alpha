package NetBasics.Firewalls;

// GetWebPageP.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* Retrieve a Web page using URL and URLConnection objects.

 Sets hardwired proxy and authorization details.
 Uses David W. Croft's Base64Converter class.

 Usage:
 java GetWebPageP <password> <url> 
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

public class GetWebPageP {
    private static final String LOGIN = "ad"; // modify this

    public static void main(String args[]) throws IOException {
        if (args.length != 2) {
            System.out.println("usage:  java GetWebPageP <password> <url>");
            System.exit(0);
        }
        // set the properties used for proxy support
        Properties props = System.getProperties();
        props.put("proxySet", "true");
        props.put("proxyHost", "cache.psu.ac.th");
        props.put("proxyPort", "8080");
        System.setProperties(props);
        // create a URL and URLConnection
        URL url = new URL(args[1]); // URL string
        URLConnection conn = url.openConnection();
        // encode the "login:password" string
        Base64Converter bc = new Base64Converter();
        String encoding = Base64Converter.encode(LOGIN + ":" + args[0]);
        // send the authorization
        conn.setRequestProperty("Proxy-Authorization", "Basic " + encoding);
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        // print first ten lines of contents
        int numLine = 0;
        String line;
        while ((line = br.readLine()) != null && numLine <= 10) {
            System.out.println(line);
            numLine++;
        }
        if (line != null) {
            System.out.println(". . .");
        }
        br.close();
        System.exit(0);
    } // end of main()
} // end GetWebPageP class
