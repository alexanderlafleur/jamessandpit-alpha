package NetTour3D;

// TouristInfo.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* Stores information about a single client:
 the client's IP address, port, and output stream

 The output stream is used to send messages to the client.

 The address and port are used to uniquely identify the
 client (the client has no name).

 Very similar to Chatter in the multithreaded Chat server.
 */
import java.io.PrintWriter;

public class TouristInfo {
    String cliAddr;
    PrintWriter out;
    int port;

    public TouristInfo(String cliAddr, int port, PrintWriter out) {
        this.cliAddr = cliAddr;
        this.port = port;
        this.out = out;
    }

    public boolean matches(String ca, int p) {
        if (cliAddr.equals(ca) && port == p) {
            return true;
        }
        return false;
    }

    public void sendMessage(String msg) { // System.out.println("sendMessage
        // to (" + cliAddr + "," +
        // port + ") : " + msg);
        out.println(msg);
    }
    // public String toString()
    // { return cliAddr + " & " + port + " & "; }
} // end of TouristInfo class
