package NetFourByFour;

// NetFourByFour.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* A network version of FourByFour.java

 Message sent by client:
 -- initial opening of connetion
 * receive back     ok <id>   or   full
 -- try <posn>
 * may receive back tooFewPlayers
 -- disconnect

 Changes:
 - playerID is 1 or 2
 - altered Board so does not change playerID

 - removed GUI button (jbDisconnect) and two text fields
 for reporting ID and status
 - text fields replaced by overlays on the canvas
 - disconnect button idea not used
 - replaced Board's gameOver with isDisabled in NetFourByFour

 When a player (e.g. p1) wants to make a move:
 1. PickDragBehaviour calls tryMove() in this class.
 2. The PlayerServerHandler is informed with a "try <posn>" message
 - p1 may get back a "tooFewPlayers" message, or
 - the handler sends an "otherTurn <playerID><posn>" message 
 to the other player (p2)
 3. Without waiting for any response, tryMove calls doMove(), which
 calls tryPosn() in WrapNetFBF, and then tryPosn() in Board.
 4. Board updates the data structures, and calls set() in Positions
 to update the 3D representation.

 For the other player (p2):
 1. The FBFServer sends a "otherTurn <playerID><posn>"
 message to p2's FBFWatcher.
 2. FBFWatcher calls doMove() in its NetFourByFour object.
 3 & 4. Same as for p1 above

 When Board detects that a player has won:
 1. Board calls its reportWinner(), which then calls disable()
 in this object.
 2. disable() breaks the network connection, updates the status
 text , and sets isDiabled to true. isDisabled will prevent
 any more calls to tryMove() in NetFourByFour 
 */

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;

public class NetFourByFour extends JFrame {
    /**
     * 
     */
    private static final long serialVersionUID = -2007783802540979848L;

    private static final int PORT = 1234; // server details

    private static final String HOST = "localhost";

    private static final int MAX_PLAYERS = 2;

    private final static int PLAYER1 = 1;

    private final static int PLAYER2 = 2; // use to be MACHINE

    private WrapNetFBF wrapFBF;

    private Socket sock;

    private PrintWriter out;

    // game-related
    private int playerID;

    private String status; // used to place info into the 3D canvas

    private int numPlayers;

    private int currPlayer; // which player can take a turn now?

    private boolean isDisabled; // to indicate that the game has ended

    public NetFourByFour() {
        super("Net Four By Four");

        this.playerID = 0; // no id as yet
        this.status = null; // no status to report
        this.numPlayers = 0;
        this.currPlayer = 1; // player 1 starts first
        this.isDisabled = false;

        makeContact();

        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        this.wrapFBF = new WrapNetFBF(this);
        c.add(this.wrapFBF, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disable("exiting");
                System.exit(0);
            }
        });

        pack();
        setResizable(false); // fixed size display
        setVisible(true);
    } // end of NetFourByFour()

    private void makeContact()
    // contact the FBFServer, and then one of its player handlers
    {
        try {
            this.sock = new Socket(HOST, PORT);
            BufferedReader in = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
            this.out = new PrintWriter(this.sock.getOutputStream(), true); // autoflush

            new FBFWatcher(this, in).start(); // start watching for server
            // msgs
        } catch (Exception e) { // System.out.println(e);
            System.out.println("Cannot contact the NetFourByFour Server");
            System.exit(0);
        }
    } // end of makeContact()

    public void tryMove(int posn)
    // called by PickDragBehaviour to initiate a new move
    {
        if (!this.isDisabled) {
            if (this.numPlayers < MAX_PLAYERS) {
                setStatus("Waiting for player " + otherPlayer(this.playerID));
            } else if (this.playerID != this.currPlayer) {
                setStatus("Sorry, it is Player " + this.currPlayer + "'s turn");
            } else if (this.numPlayers == MAX_PLAYERS) {
                this.out.println("try " + posn); // tell the server
                doMove(posn, this.playerID); // do it, don't wait for any response
            } else {
                System.out.println("Error on processing position");
            }
        }
    } // end of tryMove()

    synchronized public void doMove(int posn, int pid)
    /*
     * Called by this object to execute this player's move, and by FBFWatcher to execute the other player's move. But the synchronized keyword means never at the same time.
     */
    {
        this.wrapFBF.tryPosn(posn, pid); // and so to Board
        if (!this.isDisabled) {
            this.currPlayer = otherPlayer(this.currPlayer); // player's turn is finished
            if (this.currPlayer == this.playerID) {
                setStatus("It's your turn now");
            } else {
                // the other player's turn
                setStatus("Player " + this.currPlayer + "'s turn");
            }
        }
    } // end of doMove()

    private int otherPlayer(int id)
    // we assume there are only two players
    {
        int otherID = ((id == PLAYER1) ? PLAYER2 : PLAYER1);
        return otherID;
    }

    synchronized public void disable(String msg)
    /*
     * Disconnect from network but do not terminate. No more moves will be allowed but the 3D image will be rotatable.
     * 
     * Called by: the close box, FBFWatcher, and Board (via gameWon()).
     */
    {
        if (!this.isDisabled) { // the client can only be disabled once
            try {
                this.isDisabled = true;
                this.out.println("disconnect"); // tell server
                this.sock.close();
                setStatus("Game Over: " + msg);
                // System.out.println("Disabled: " + msg);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    } // end of disable()

    public void gameWon(int pid, int score)
    /*
     * Called from reportWinner() in Board to announce a win by player pid with score.
     */
    {
        if (pid == this.playerID) {
            disable("You've won with score " + score);
        } else {
            disable("Player " + pid + " has won with score " + score);
        }
    } // end of gameWon()

    synchronized public void setStatus(String msg) { // System.out.println("Status:
        // " + msg);
        this.status = msg;
    }

    synchronized public String getStatus()
    // regularly called from OverlayCanvas, to update its display
    {
        return this.status;
    }

    // -------------------------------------------------
    // the following methods are only called from FBFWatcher

    public void addPlayer()
    // add the other player
    {
        this.numPlayers++;
        if (this.numPlayers == MAX_PLAYERS) { // time to start the game
            if (this.currPlayer == this.playerID) {
                setStatus("Please start");
            } else {
                // the other player starts
                setStatus("Player " + this.currPlayer + " starts the game");
            }
        }
    } // end of addPlayer()

    public void removePlayer()
    // remove the other player
    {
        this.numPlayers--;
        if (this.numPlayers < MAX_PLAYERS) {
            disable("Player " + otherPlayer(this.playerID) + " has left"); // disable
            // client
        }
    } // end of removePlayer()

    public void setPlayerID(int id)
    /*
     * This player is given a playerID. Update the window's title bar. The id is also the number of current players, so can be used to check whether the game can start.
     */
    {
        System.out.println("My player ID: " + id);
        this.playerID = id;
        if (this.playerID == PLAYER1) {
            setTitle("Net Four By Four. Player 1 (red balls)");
        } else {
            // PLAYER2
            setTitle("Net Four By Four. Player 2 (blue cubes)");
        }

        this.numPlayers = id;
        if (this.numPlayers == MAX_PLAYERS) {
            setStatus("Player 1 starts the game");
        } else if (this.numPlayers < MAX_PLAYERS) {
            setStatus("Waiting for player " + otherPlayer(this.playerID));
        }
    } // end of setPlayerID()

    // -----------------------------------------

    public static void main(String[] args) {
        new NetFourByFour();
    }

} // end of NetFourByFour class
