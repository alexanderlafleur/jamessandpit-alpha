package NetBasics.UDP;

// ScoreUDPServer.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* A UDP-based server that stores a client's score (and name) in a
 list of top-10 high scores.

 The server can multiplex between clients because each client
 message is self-contained.

 Understood input messages:
 get					   -- returns the high score list
 score name & score &   -- add the score for name

 no bye message needed since there is no long term
 connection with a client

 The list is maintained in a file SCORFN, and loaded when the
 server starts.
 The server is terminated with a ctrl-C
 */
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ScoreUDPServer {
    private static final int BUFSIZE = 1024; // max size of a message
    private static final int PORT = 1234;

    public static void main(String args[]) {
        new ScoreUDPServer();
    }

    private HighScores hs;
    private DatagramSocket serverSock;

    // ------------------------------
    // processing of client requests
    public ScoreUDPServer() {
        try { // try to create a socket for the server
            serverSock = new DatagramSocket(PORT);
        } catch (SocketException se) {
            System.out.println(se);
            System.exit(1);
        }
        waitForPackets();
    } // end of ScoreUDPServer()

    private void doRequest(InetAddress clientAddr, int clientPort, String clientMesg)
    /*
     * A message from a client can be one of: "score name & score &" or "get"
     */
    {
        if (clientMesg.trim().toLowerCase().equals("get")) {
            System.out.println("Processing 'get'");
            sendMessage(clientAddr, clientPort, hs.toString());
        } else if (clientMesg.length() >= 6 && // "score "
                clientMesg.substring(0, 5).toLowerCase().equals("score")) {
            System.out.println("Processing 'score'");
            hs.addScore(clientMesg.substring(5)); // cut the score keyword
        } else {
            System.out.println("Ignoring input line");
        }
    } // end of doRequest()

    private void processClient(DatagramPacket receivePacket)
    // extract client details from the received packet
    {
        InetAddress clientAddr = receivePacket.getAddress();
        int clientPort = receivePacket.getPort();
        String clientMesg = new String(receivePacket.getData(), 0, receivePacket.getLength());
        System.out.println("Client packet from " + clientAddr + ", " + clientPort);
        System.out.println("Client mesg: " + clientMesg);
        doRequest(clientAddr, clientPort, clientMesg);
    } // end of processClient()

    private void sendMessage(InetAddress clientAddr, int clientPort, String mesg)
    // send message to the socket at the specified address and port
    {
        byte mesgData[] = mesg.getBytes(); // convert message to byte[] form
        try {
            DatagramPacket sendPacket = new DatagramPacket(mesgData, mesgData.length, clientAddr, clientPort);
            serverSock.send(sendPacket);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    } // end of sendMessage()

    // ------------------------------------
    private void waitForPackets()
    /*
     * Repeatedly receive a packet, process it, and perhaps send a response.
     */
    {
        DatagramPacket receivePacket;
        byte data[];
        hs = new HighScores();
        try {
            while (true) {
                // set up an empty packet
                data = new byte[BUFSIZE];
                receivePacket = new DatagramPacket(data, data.length);
                System.out.println("Waiting for a packet...");
                serverSock.receive(receivePacket);
                processClient(receivePacket);
                hs.saveScores(); // backup high scores after each package has
                // finished
            }
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    } // end of waitForPackets()
} // end of ScoreUDPServer class
