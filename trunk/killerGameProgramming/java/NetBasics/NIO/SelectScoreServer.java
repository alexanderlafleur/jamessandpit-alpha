package NetBasics.NIO;

// SelectScoreServer.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* A non-blocking  sequential server that stores a client's score
 (and name) in a list of top-10 high scores.

 Understood input messages:
 get					   -- returns the high score list
 score name & score &   -- add the score for name
 bye					   -- terminates the client link

 The list is maintained in a file SCORFN, and loaded when the
 server starts.
 The server is terminated with a ctrl-C

 Derived from SelectSockets.java 

 We use non-blocking detection of new socket channel
 connections from clients _and_ use non-blocking reading
 of those channels.

 */

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;

public class SelectScoreServer {
    private static final int PORT_NUMBER = 1234;

    private HighScores hs;

    private HashMap clients; // map of channels to ClientInfo objects

    public SelectScoreServer() {
        this.hs = new HighScores();
        this.clients = new HashMap();
        try {
            System.out.println("Listening on port " + PORT_NUMBER);

            // set up server channel and socket
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false); // use non-blocking mode

            ServerSocket serverSocket = serverChannel.socket();
            serverSocket.bind(new InetSocketAddress(PORT_NUMBER)); // set port
            // for
            // listening

            Selector selector = Selector.open();
            serverChannel.register(selector, SelectionKey.OP_ACCEPT); // register
            // channel
            // with
            // selector

            while (true) {
                // System.out.println("No. of clients: " + clients.size());
                selector.select(); // wait for ready channels
                Iterator it = selector.selectedKeys().iterator(); // get
                // iterator
                // for keys
                SelectionKey key;
                while (it.hasNext()) { // look at each key
                    key = (SelectionKey) it.next(); // get a key
                    it.remove(); // remove it
                    if (key.isAcceptable()) {
                        newChannel(key, selector);
                    } else if (key.isReadable()) {
                        readFromChannel(key);
                    } else {
                        System.out.println("Did not process key: " + key);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    } // end of SelectScoreServer()

    private void newChannel(SelectionKey key, Selector selector)
    // Add the socket channel for a new client to the selector
    {
        try {
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            SocketChannel channel = server.accept(); // get the channel
            channel.configureBlocking(false); // use non-blocking
            channel.register(selector, SelectionKey.OP_READ); // register it
            // with selector

            this.clients.put(channel, new ClientInfo(channel, this)); // store
            // info
        } catch (IOException e) {
            System.out.println(e);
        }
    } // end of newChannel()

    private void readFromChannel(SelectionKey key)
    // process input that is waiting on a channel
    {
        SocketChannel channel = (SocketChannel) key.channel();
        ClientInfo ci = (ClientInfo) this.clients.get(channel);
        if (ci == null) {
            System.out.println("No client info for channel " + channel);
        } else {
            String msg = ci.readMessage();
            if (msg != null) {
                System.out.println("Read message: " + msg);
                if (msg.trim().equals("bye")) {
                    ci.closeDown();
                    this.clients.remove(channel); // delete ci from hash map
                } else {
                    doRequest(msg, ci);
                }
            }
        }
    } // end of readFromChannel()

    private void doRequest(String line, ClientInfo ci)
    /*
     * The input line can be one of: "score name & score &" or "get"
     */
    {
        if (line.trim().toLowerCase().equals("get")) {
            System.out.println("Processing 'get'");
            ci.sendMessage(this.hs.toString());
        } else if ((line.length() >= 6) && // "score "
                (line.substring(0, 5).toLowerCase().equals("score"))) {
            System.out.println("Processing 'score'");
            this.hs.addScore(line.substring(5)); // cut the score keyword
        } else {
            System.out.println("Ignoring input line");
        }
    } // end of doRequest()

    public void removeChannel(SocketChannel chan)
    // called by ClientInfo object when channel is closed
    {
        this.clients.remove(chan);
    }

    // --------------------------------------------

    public static void main(String[] argv) {
        new SelectScoreServer();
    }

} // end of SelectScoreServer class

