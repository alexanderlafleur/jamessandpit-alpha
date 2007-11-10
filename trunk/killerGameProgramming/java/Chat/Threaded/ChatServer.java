package Chat.Threaded;

// ChatServer.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/*
 The top-level chat server, which waits for client connections
 and creates ChatServerHandler threads to handle them.

 Details about each client are maintained in a ChatGroup object
 which is referenced by each thread.
 */

import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
    static final int PORT = 1234; // port for this server

    private ChatGroup cg;

    public ChatServer()
    // wait for a client connection, spawn a thread, repeat
    {
        this.cg = new ChatGroup();
        try {
            ServerSocket serverSock = new ServerSocket(PORT);
            Socket clientSock;

            while (true) {
                System.out.println("Waiting for a client...");
                clientSock = serverSock.accept();
                new ChatServerHandler(clientSock, this.cg).start();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    } // end of ChatServer()

    // -----------------------------------

    public static void main(String args[]) {
        new ChatServer();
    }

} // end of ChatServer class
