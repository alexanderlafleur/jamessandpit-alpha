package NetFourByFour;

// FBFServer.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* A threaded server specialised to deal with at most two
 concurrent links from the two players involved in the
 FourByFour game.

 This is a thin server -- all the game logic is located
 on the client-side.

 The shared data is an array of references to the handlers
 and the current number of players. Since this is so little,
 a shared object is not used to maintain it. 
 */

import java.net.ServerSocket;
import java.net.Socket;

public class FBFServer {
    private static final int PORT = 1234;

    private static final int MAX_PLAYERS = 2; // two-person game

    private final static int PLAYER1 = 1;

    private final static int PLAYER2 = 2; // use to be MACHINE

    // data structures shared by the handlers
    private PlayerServerHandler[] handlers; // handlers for players

    private int numPlayers;

    public FBFServer()
    // Concurrently process players
    {
        this.handlers = new PlayerServerHandler[MAX_PLAYERS];
        this.handlers[0] = null;
        this.handlers[1] = null;
        this.numPlayers = 0;

        try {
            ServerSocket serverSock = new ServerSocket(PORT);
            Socket clientSock;
            while (true) {
                System.out.println("Waiting for a client...");
                clientSock = serverSock.accept();
                new PlayerServerHandler(clientSock, this).start();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    } // end of ChatServer()

    // methods for child threads to access shared data structures

    synchronized public boolean enoughPlayers() {
        return (this.numPlayers == MAX_PLAYERS);
    }

    synchronized public int addPlayer(PlayerServerHandler h)
    /*
     * Store a reference to the handler, and return a player ID to the handler. The ID is the array index where the handler is stored + 1.
     */
    {
        for (int i = 0; i < MAX_PLAYERS; i++) {
            if (this.handlers[i] == null) {
                this.handlers[i] = h;
                this.numPlayers++;
                return i + 1; // playerID is 1 or 2 (array index + 1)
            }
        }
        return -1; // means we have enough players already
    } // end of addPlayer()

    synchronized public void removePlayer(int playerID) {
        this.handlers[playerID - 1] = null; // no checking done of player value
        this.numPlayers--;
    } // end of removePlayer()

    synchronized public void tellOther(int playerID, String msg)
    // send mesg to the other player
    {
        int otherID = ((playerID == PLAYER1) ? PLAYER2 : PLAYER1);
        if (this.handlers[otherID - 1] != null) {
            this.handlers[otherID - 1].sendMessage(msg);
        }
    } // end of tellOther()

    // -----------------------------------

    public static void main(String args[]) {
        new FBFServer();
    }

} // end of FBFServer class

