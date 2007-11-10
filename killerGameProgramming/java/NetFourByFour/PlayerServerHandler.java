package NetFourByFour;

// PlayerServerHandler.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Handle messages from the client.

 Upon initial connection:
 response to client is:
 ok <playerID>   or    full
 message to other client if player is accepted:
 added <playerID>

 Other client messages:
 * disconnect
 message to other client:
 removed <playerID>
 
 * try <posn>
 response to client
 tooFewPlayers
 message to other client if turn accepted
 otherTurn <playerID> <posn>
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PlayerServerHandler extends Thread {
    private FBFServer server;

    private Socket clientSock;

    private BufferedReader in;

    private PrintWriter out;

    private int playerID; // this player id is assigned by FBFServer

    public PlayerServerHandler(Socket s, FBFServer serv) {
        this.clientSock = s;
        this.server = serv;
        System.out.println("Player connection request");
        try {
            this.in = new BufferedReader(new InputStreamReader(this.clientSock.getInputStream()));
            this.out = new PrintWriter(this.clientSock.getOutputStream(), true); // autoflush
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void run()
    /*
     * Add this player to FSFServer array, get an ID, then start processing client-side input
     */
    {
        this.playerID = this.server.addPlayer(this);
        if (this.playerID != -1) { // -1 means adding the player was rejected
            sendMessage("ok " + this.playerID);
            System.out.println("ok " + this.playerID); // tell player his/her
            // playerID
            this.server.tellOther(this.playerID, "added " + this.playerID);

            processPlayerInput();

            this.server.removePlayer(this.playerID); // remove player from server data
            this.server.tellOther(this.playerID, "removed " + this.playerID); // tell others
        } else {
            // game is full
            sendMessage("full");
        }

        try { // close socket from player
            this.clientSock.close();
            System.out.println("Player " + this.playerID + " connection closed\n");
        } catch (Exception e) {
            System.out.println(e);
        }

    } // end of run()

    private void processPlayerInput()
    /*
     * Stop when the input stream closes (is null) or "disconnect" is sent. Otherwise pass the input to doRequest().
     */
    {
        String line;
        boolean done = false;
        try {
            while (!done) {
                if ((line = this.in.readLine()) == null) {
                    done = true;
                } else {
                    // System.out.println("Player " + playerID + " msg: " +
                    // line);
                    if (line.trim().equals("disconnect")) {
                        done = true;
                    } else {
                        doRequest(line);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Player " + this.playerID + " closed the connection");
        }
    } // end of processPlayerInput()

    private void doRequest(String line)
    /*
     * The input line can be : try <posn> -- try to occupy position pos (pos == 0-63)
     * 
     * No checking of posn done here; we assume the client has checked it. No checking of turn order here; we assume the client is doing it
     */
    {
        if (line.startsWith("try")) {
            try {
                int posn = Integer.parseInt(line.substring(4).trim());
                // System.out.println("Player " + playerID + " wants to occupy
                // position " + posn);

                if (this.server.enoughPlayers()) {
                    this.server.tellOther(this.playerID, "otherTurn " + this.playerID + " " + posn); // pass turn to
                    // others
                } else {
                    sendMessage("tooFewPlayers");
                }
            } catch (NumberFormatException e) {
                System.out.println(e);
            }
        }
    } // end of doRequest()

    synchronized public void sendMessage(String msg)
    // called by handler and top-level server
    {
        try {
            this.out.println(msg);
        } catch (Exception e) {
            System.out.println("Handler for player " + this.playerID + "\n" + e);
        }
    } // end of sendMessage()

} // end of PlayerServerHandler class
