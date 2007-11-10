package NetFourByFour;

// FBFWatcher.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* FBFWatcher monitors the stream coming from the server
 which will contain messages that must be processed by 
 the client (a NetFourByFour object)

 Incoming Messages:
 ok <playerID>           -- connection accepted; include player ID
 full                    -- connection refused; server has enough players
 tooFewPlayers           -- turn rejected, since not enough players
 otherTurn <player> <posn>  -- turn by other player sent to client
 added <player>          -- other player added to server
 removed <player>        -- other player removed
 */

import java.io.BufferedReader;
import java.util.StringTokenizer;

public class FBFWatcher extends Thread {
    private NetFourByFour fbf; // ref back to client

    private BufferedReader in;

    public FBFWatcher(NetFourByFour fbf, BufferedReader i) {
        this.fbf = fbf;
        this.in = i;
    }

    @Override
    public void run()
    // Read server messages and act on them
    {
        String line;
        try {
            while ((line = this.in.readLine()) != null) {
                if (line.startsWith("ok")) {
                    extractID(line.substring(3));
                } else if (line.startsWith("full")) {
                    this.fbf.disable("full game"); // disable client
                } else if (line.startsWith("tooFewPlayers")) {
                    this.fbf.disable("other player has left"); // disable client
                } else if (line.startsWith("otherTurn")) {
                    extractOther(line.substring(10));
                } else if (line.startsWith("added")) {
                    this.fbf.addPlayer(); // client adds other player
                } else if (line.startsWith("removed")) {
                    this.fbf.removePlayer(); // client removes other player
                } else {
                    // anything else
                    System.out.println("ERR: " + line + "\n");
                }
            }
        } catch (Exception e) // socket closure will cause termination of
        // while
        { // System.out.println("Socket closed");
            this.fbf.disable("server link lost"); // end game as well
        }
    } // end of run()

    private void extractID(String line)
    // line format: <player id>
    {
        StringTokenizer tokens = new StringTokenizer(line);
        try {
            int id = Integer.parseInt(tokens.nextToken());
            this.fbf.setPlayerID(id); // client gets its playerID
        } catch (NumberFormatException e) {
            System.out.println(e);
        }
    } // end of extractID()

    private void extractOther(String line)
    // line format: <player id> <posn>
    {
        StringTokenizer tokens = new StringTokenizer(line);
        try {
            int playerID = Integer.parseInt(tokens.nextToken());
            int posn = Integer.parseInt(tokens.nextToken());
            this.fbf.doMove(posn, playerID); // client executes the other player's
            // move
        } catch (NumberFormatException e) {
            System.out.println(e);
        }
    } // end of extractOther()

} // end of FBFWatcher

