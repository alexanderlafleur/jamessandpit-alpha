package Chat.Threaded;

// ChatServerHandler.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/*
 A threaded ChatServerHandler deals with a client.

 Details about a client are maintained in a ChatGroup 
 object, which is referenced by all the threads.

 The ChatGroup object handles message broadcasting
 via its broadcast() method.

 Possible client messages:
 who				-- a list of users is returned
 bye             -- client is disconnecting
 any text		-- which is broadcast with
 (cliAddr,port): at its front 
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatServerHandler extends Thread {
    private Socket clientSock; // client details

    private String cliAddr;

    private int port;

    private ChatGroup cg; // shared by all threads

    public ChatServerHandler(Socket s, ChatGroup cg) {
        this.cg = cg;
        this.clientSock = s;
        this.cliAddr = this.clientSock.getInetAddress().getHostAddress();
        this.port = this.clientSock.getPort();
        System.out.println("Client connection from (" + this.cliAddr + ", " + this.port + ")");
    } // end of ChatServerHandler()

    @Override
    public void run() {
        try {
            // Get I/O streams from the socket
            BufferedReader in = new BufferedReader(new InputStreamReader(this.clientSock.getInputStream()));
            PrintWriter out = new PrintWriter(this.clientSock.getOutputStream(), true); // autoflush

            this.cg.addPerson(this.cliAddr, this.port, out); // add client details to
            // ChatGroup

            processClient(in, out); // interact with client

            // the client has finished when execution reaches here
            this.cg.delPerson(this.cliAddr, this.port); // remove client details
            this.clientSock.close();
            System.out.println("Client (" + this.cliAddr + ", " + this.port + ") connection closed\n");
        } catch (Exception e) {
            System.out.println(e);
        }
    } // end of run()

    private void processClient(BufferedReader in, PrintWriter out)
    /*
     * Stop when the input stream closes (is null) or "bye" is sent Otherwise pass the input to doRequest().
     */
    {
        String line;
        boolean done = false;
        try {
            while (!done) {
                if ((line = in.readLine()) == null) {
                    done = true;
                } else {
                    System.out.println("Client (" + this.cliAddr + ", " + this.port + "): " + line);
                    if (line.trim().equals("bye")) {
                        done = true;
                    } else {
                        doRequest(line, out);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    } // end of processClient()

    private void doRequest(String line, PrintWriter out)
    /*
     * The input line (client message) can be : who -- a list of users is returned or any text -- which is broadcast with (cliAddr,port) at its front
     */
    {
        if (line.trim().toLowerCase().equals("who")) {
            System.out.println("Processing 'who'");
            out.println(this.cg.who());
        } else {
            // use ChatGroup object to broadcast the message
            this.cg.broadcast("(" + this.cliAddr + ", " + this.port + "): " + line);
        }
    } // end of doRequest()

} // end of ChatServerHandler class

