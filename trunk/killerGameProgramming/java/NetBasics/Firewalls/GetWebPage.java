package NetBasics.Firewalls;

// GetWebPage.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Retrieve a Web page using a URL object.

 For use through a firewall, use something like:
 java -DproxySet=true -DproxyHost=SOMEHOST -DproxyPort=SOMENUM GetWebPage <url>

 This is packaged up in GetWebPage.bat
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class GetWebPage {

    public static void main(String args[]) throws IOException {
        if (args.length != 1) {
            System.out.println("usage:  java GetWebPage <url> ");
            System.exit(0);
        }

        URL url = new URL(args[0]);
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

        // print first ten lines of contents
        int numLine = 0;
        String line;
        while (((line = br.readLine()) != null) && (numLine <= 10)) {
            System.out.println(line);
            numLine++;
        }
        if (line != null) {
            System.out.println(". . .");
        }

        br.close();
    }

} // end of GetWebPage class
