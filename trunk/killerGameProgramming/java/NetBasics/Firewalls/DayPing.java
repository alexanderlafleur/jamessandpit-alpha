package NetBasics.Firewalls;

// DayPing.java
// Andrew Davison, June 2003, dandrew@ratree.psu.ac.th

/* Check if a server is alive by contacting (pinging) its daytime server.
 One problem is that daytime servers are not always switched on.

 A host which is on:
 time-A.timefreq.bldrdoc.gov

 For a long list, see:
 http://www.boulder.nist.gov/timefreq/service/time-servers.html

 For use through a firewall, use something like:
 java -DproxySet=true -DproxyHost=SOMEHOST -DproxyPort=SOMENUM DayPing <host>

 This is packaged up in DayPing.bat
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class DayPing {
    public static void main(String args[]) throws IOException {
        if (args.length != 1) {
            System.out.println("usage:  java DayPing <host> ");
            System.exit(0);
        }

        Socket sock = new Socket(args[0], 13); // host and daytime port
        BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));

        System.out.println(args[0] + " is alive at ");
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }

        sock.close();
    }

} // end of DayPing class
