package Chat.Threaded;

// Chatter.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Stores information about a single client:
 the client's IP address, port, and output stream

 The output stream is used to send messages to the client.

 The address and port are used to uniquely identify the
 client (the client has no name).
 */

import java.io.PrintWriter;

public class Chatter {
    private String cliAddr;

    private int port;

    private PrintWriter out;

    public Chatter(String cliAddr, int port, PrintWriter out) {
        this.cliAddr = cliAddr;
        this.port = port;
        this.out = out;
    }

    public boolean matches(String ca, int p)
    // the address and port of a client are used to uniquely identify it
    {
        if (this.cliAddr.equals(ca) && (this.port == p)) {
            return true;
        }
        return false;
    } // end of matches()

    public void sendMessage(String msg) {
        this.out.println(msg);
    }

    @Override
    public String toString() {
        return this.cliAddr + " & " + this.port + " & ";
    }

} // end of Chatter class
