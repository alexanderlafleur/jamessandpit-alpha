package NetBasics.Threaded;

// ThreadedScoreHandler.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* A threaded handler, called by ThreadedScoreServer to process 
 a client.

 Understood input messages:
 get						-- returns the high score list
 score name & score &	-- add the score for name
 bye						-- terminates the client link
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ThreadedScoreHandler extends Thread {
    private Socket clientSock;

    private String cliAddr;

    private HighScores hs;

    public ThreadedScoreHandler(Socket s, String cliAddr, HighScores hs) {
        this.clientSock = s;
        this.cliAddr = cliAddr;
        this.hs = hs;
        System.out.println("Client connection from " + cliAddr);
    }

    @Override
    public void run() {
        try {
            // Get I/O streams from the socket
            BufferedReader in = new BufferedReader(new InputStreamReader(this.clientSock.getInputStream()));
            PrintWriter out = new PrintWriter(this.clientSock.getOutputStream(), true); // autoflush

            // interact with a client
            processClient(in, out);

            // Close client connection
            this.clientSock.close();
            System.out.println("Client (" + this.cliAddr + ") connection closed\n");
        } catch (Exception e) {
            System.out.println(e);
        }
    } // end of run()

    private void processClient(BufferedReader in, PrintWriter out)
    // Stop when the input stream closes (is null) or "bye" is sent
    // Otherwise pass the input to doRequest()
    {
        String line;
        boolean done = false;
        try {
            while (!done) {
                if ((line = in.readLine()) == null) {
                    done = true;
                } else {
                    System.out.println("Client msg: " + line);
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
     * The input line can be one of: "score name & score &" or "get"
     */
    {
        if (line.trim().toLowerCase().equals("get")) {
            System.out.println("Processing 'get'");
            out.println(this.hs.toString());
        } else if ((line.length() >= 6) && // "score "
                (line.substring(0, 5).toLowerCase().equals("score"))) {
            System.out.println("Processing 'score'");
            this.hs.addScore(line.substring(5)); // cut the score keyword
        } else {
            System.out.println("Ignoring input line");
        }
    } // end of doRequest()

} // end of ThreadedScoreHandler class
