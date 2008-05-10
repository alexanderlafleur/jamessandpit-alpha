package NetBasics.Threaded;

// ThreadedScoreServer.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* A threaded server that stores a client's score (and name) in a
 list of top-10 high scores.

 The list is maintained in a file SCORFN, and loaded when the
 server starts.
 The server is terminated with a ctrl-C

 The server processes a client by creating a 
 ThreadedScoreHandler object.

 Derived from ThreadedEchoServer.java (Listing 3-5, p.161) in
 Core Java 2, Volume II -- Advanced Features
 Horstmann and Cornell
 Sun Microsystems Press, 2000, 4th Edition
 */
// import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadedScoreServer {
    private static final int PORT = 1234;

    public static void main(String args[]) {
        new ThreadedScoreServer();
    }

    private HighScores hs;

    // -----------------------------------
    public ThreadedScoreServer()
    // Concurrently process clients forever
    {
        hs = new HighScores();
        try {
            ServerSocket serverSock = new ServerSocket(PORT);
            Socket clientSock;
            String cliAddr;
            while (true) {
                System.out.println("Waiting for a client...");
                clientSock = serverSock.accept();
                cliAddr = clientSock.getInetAddress().getHostAddress();
                new ThreadedScoreHandler(clientSock, cliAddr, hs).start();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    } // end of ThreadedScoreServer()
} // end of ThreadedScoreServer class
