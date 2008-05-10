package NetTour3D;

// TourServer.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/*
 The top-level Tour server, which waits for client connections
 and creates TourServerHandler threads to handle them.

 Details about each client are maintained in a TourGroup object
 which is referenced by each thread.

 Very similar to the multithreaded Chat server.
 */
import java.net.ServerSocket;
import java.net.Socket;

public class TourServer {
    static final int PORT = 5555;

    public static void main(String args[]) {
        new TourServer();
    }

    private TourGroup tg;

    // -----------------------------------
    public TourServer()
    // wait for a client connection, spawn a thread, repeat
    {
        tg = new TourGroup();
        try {
            ServerSocket serverSock = new ServerSocket(PORT);
            Socket clientSock;
            while (true) {
                System.out.println("Waiting for a client...");
                clientSock = serverSock.accept();
                new TourServerHandler(clientSock, tg).start();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    } // end of TourServer()
} // end of TourServer class
