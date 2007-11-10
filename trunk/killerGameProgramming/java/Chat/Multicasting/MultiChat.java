package Chat.Multicasting;

// MultiChat.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* A client for a multicast chat group which uses
 a NameServer for certain tasks.

 The NameServer does a very simplistic check of the
 user before the multicast group address is revealed.

 The NameServer answer's "who" questions.

 There are three possible types of message sent to the
 NameServer:
 hi <name>   // add the name to the NameServer
 Get back the multicast address, or "no"

 bye <name>  // delete this name from the NameServer

 who         // send back a list of all the current members


 Types of message sent to the multicast group:

 (name): msg [ / toName ]

 A "/ toName" extension means that the message will only
 appear in the sender's text window and toName's window.

 ---- Changes 30 August 2004 ---

 Modified showMsg() to move the caret position, and use invokeLater()
 to avoid Swing+threads problem. For details see:
 http://java.sun.com/products/jfc/tsc/articles/threads/threads1.html
 - thanks to Rachel Struthers (rmstruthers@mn.rr.com)

 */

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class MultiChat extends JFrame implements ActionListener {
    /**
     * 
     */
    private static final long serialVersionUID = -419371595660379398L;

    private static final int TIME_OUT = 5000; // 5 secs

    // timeout used when waiting in receive()
    private static final int PACKET_SIZE = 1024; // max size of a message

    // NameServer address and port constants
    private static final String SERVER_HOST = "localhost";

    private static final int SERVER_PORT = 1234;

    /*
     * The multicast port. The multicast group address is obtained from the NameServer object.
     */
    private static final int GROUP_PORT = 5555;

    // for communication with the NameServer
    private DatagramSocket clientSock;

    private InetAddress serverAddr;

    // for communication with the multicast group
    private MulticastSocket groupSock;

    private InetAddress groupAddr;

    private String userName; // name of this client

    private JTextArea jtaMesgs;

    private JTextField jtfMsg;

    private JButton jbWho;

    public MultiChat(String nm) {
        super("Multicasting Chat Client for " + nm);
        this.userName = nm;
        initializeGUI();

        /*
         * Attempt to register name and get multicast group address from the NameServer
         */
        makeClientSock();
        sendServerMessage("hi " + this.userName); // say "hi" to NameServer
        checkHiResponse();

        // Connect to the multicast group; say hi to everyone
        joinChatGroup();
        sendPacket("hi");

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                sayBye();
            } // say bye at termination time
        });

        setSize(300, 450);
        setVisible(true);

        waitForPackets();
    } // end of MultiChat();

    // ----------- GUI related -------------------------

    private void initializeGUI()
    /*
     * Text area in center, and controls below. Controls: - textfield for entering messages - a "Who" button
     */
    {
        Container c = getContentPane();
        c.setLayout(new BorderLayout());

        this.jtaMesgs = new JTextArea(7, 7);
        this.jtaMesgs.setEditable(false);
        JScrollPane jsp = new JScrollPane(this.jtaMesgs);
        c.add(jsp, "Center");

        JLabel jlMsg = new JLabel("Message: ");
        this.jtfMsg = new JTextField(15);
        this.jtfMsg.addActionListener(this); // pressing enter triggers sending of
        // userName/score

        this.jbWho = new JButton("Who");
        this.jbWho.addActionListener(this);

        JPanel p1 = new JPanel(new FlowLayout());
        p1.add(jlMsg);
        p1.add(this.jtfMsg);

        JPanel p2 = new JPanel(new FlowLayout());
        p2.add(this.jbWho);

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(p1);
        p.add(p2);

        c.add(p, "South");
    } // end of initializeGUI()

    public void actionPerformed(ActionEvent e)
    /*
     * Either a message is to be sent or the "Who" button has been pressed
     */
    {
        if (e.getSource() == this.jbWho) {
            doWho();
        } else if (e.getSource() == this.jtfMsg) {
            sendMessage();
        }
    }

    /*
     * synchronized private void showMsg(String msg) // Synchronized since this method can be called by the GUI // and application threads. { jtaMesgs.append(msg); }
     */

    private void showMsg(final String msg)
    /*
     * We're updating the messages text area, so the code should be carried out by Swing's event dispatching thread, which is achieved by calling invokeLater().
     * 
     * msg must be final to be used inside the inner class for Runnable.
     * 
     * Thanks to Rachel Struthers (rmstruthers@mn.rr.com)
     */
    {
        // System.out.println("showMsg(): " + msg);
        Runnable updateMsgsText = new Runnable() {
            public void run() {
                MultiChat.this.jtaMesgs.append(msg); // append message to text area
                MultiChat.this.jtaMesgs.setCaretPosition(MultiChat.this.jtaMesgs.getText().length());
                // move insertion point to the end of the text
            }
        };
        SwingUtilities.invokeLater(updateMsgsText);
    } // end of showMsg()

    // ----------- NameServer communication methods --------------

    private void makeClientSock() {
        try { // try to create the client's socket
            this.clientSock = new DatagramSocket();
            this.clientSock.setSoTimeout(TIME_OUT); // include a time-out
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }

        try { // NameServer address string --> IP no.
            this.serverAddr = InetAddress.getByName(SERVER_HOST);
        } catch (UnknownHostException uhe) {
            uhe.printStackTrace();
            System.exit(1);
        }
    } // end of makeClientSock()

    private void checkHiResponse()
    /*
     * The response to "hi <name>" can be the multicast group address (in string form), or "no".
     */
    {
        String hiResponse = readServerMessage();

        if (hiResponse.equals("no")) {
            System.out.println("Login rejected; exiting...");
            System.exit(0);
        } else {
            try { // multicast address string --> IP no.
                this.groupAddr = InetAddress.getByName(hiResponse);
            } catch (Exception e) {
                System.out.println(e);
                System.exit(0);
            }
        }
    } // end of checkHiResponse()

    private void doWho()
    /*
     * Ask the NameServer who is currently registered with it. Wait for the response and display it.
     */
    {
        sendServerMessage("who");
        String whoResponse = readServerMessage();
        if (whoResponse == null) {
            showMsg("NameServer problem: no who info available\n");
        } else {
            showMsg(whoResponse);
        }
    } // end of doWho()

    private void sayBye()
    // Say bye to the multicast group *and* the NameServer
    {
        try {
            sendPacket("bye"); // "bye" to group
            sendServerMessage("bye " + this.userName); // "bye" to server
        } catch (Exception e) {
            System.out.println(e);
        }

        System.exit(0);
    } // end of sayBye()

    private void sendServerMessage(String msg)
    // Send message to NameServer
    {
        try {
            DatagramPacket sendPacket = new DatagramPacket(msg.getBytes(), msg.length(), this.serverAddr, SERVER_PORT);
            this.clientSock.send(sendPacket);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    } // end of sendServerMessage()

    private String readServerMessage()
    /*
     * Read a message sent from the NameServer (when it arrives). There is a time-out associated with receive()
     */
    {
        String msg = null;
        try {
            byte[] data = new byte[PACKET_SIZE];
            DatagramPacket receivePacket = new DatagramPacket(data, data.length);

            this.clientSock.receive(receivePacket); // wait for a packet
            msg = new String(receivePacket.getData(), 0, receivePacket.getLength());
        } catch (IOException ioe) {
            System.out.println(ioe);
        }

        return msg;
    } // end of readServerMessage()

    // ----------------------- multicast group methods -------------

    private void joinChatGroup() {
        try {
            this.groupSock = new MulticastSocket(GROUP_PORT);
            this.groupSock.joinGroup(this.groupAddr);
        } catch (Exception e) {
            System.out.println(e);
        }
    } // end of joinChatGroup()

    private void sendMessage()
    /*
     * Check if the user has supplied a message, then send it to the multicast group.
     */
    {
        String msg = this.jtfMsg.getText().trim();
        // System.out.println("'"+msg+"'");

        if (msg.equals("")) {
            JOptionPane.showMessageDialog(null, "No message entered", "Send Message Error", JOptionPane.ERROR_MESSAGE);
        } else {
            sendPacket(msg);
        }
    } // end of sendMessage()

    private void sendPacket(String msg)
    /*
     * Send a packet to the multicast group. Messages have the form (name): msg
     */
    {
        String labelledMsg = "(" + this.userName + "): " + msg;
        try {
            DatagramPacket sendPacket = new DatagramPacket(labelledMsg.getBytes(), labelledMsg.length(), this.groupAddr, GROUP_PORT);
            this.groupSock.send(sendPacket);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    } // end of sendPacket()

    // --------------------------------------------------------
    // Processing of group messages by the application thread

    private void waitForPackets()
    /*
     * Repeatedly receive a packet from the group, process it. No messages are sent from here. Output is done from the GUI thread by calling sendPacket().
     */
    {
        DatagramPacket packet;
        byte data[];

        try {
            while (true) {
                data = new byte[PACKET_SIZE]; // set up an empty packet
                packet = new DatagramPacket(data, data.length);
                this.groupSock.receive(packet); // wait for a packet
                processPacket(packet);
            }
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    } // end of waitForPackets()

    private void processPacket(DatagramPacket dp)
    /*
     * Extract details from the received packet. Format: (fromName): msg [ / toName ]
     * 
     * A "/ toName" extension means that the message will only appear in the sender's text window and toName's window.
     */
    {
        String msg = new String(dp.getData(), 0, dp.getLength());
        if (isVisibleMsg(msg, this.userName)) {
            showMsg(msg + "\n");
        }
    } // end of processPacket()

    private boolean isVisibleMsg(String msg, String name)
    /*
     * A message is visible if it has no "/ name" part, or "/ name" is the user, or the message is _from_ the user.
     */
    {
        int index = msg.indexOf("/");
        if (index == -1) {
            return true;
        }

        // does have a "/ name" part
        String toName = msg.substring(index + 1).trim();
        if (toName.equals(name)) {
            return true;
        } else { // for another user
            if (msg.startsWith("(" + name)) {
                return true;
            } else {
                // from someone else
                return false;
            }
        }
    } // end of isVisibleMsg()

    // ------------------------------------

    public static void main(String args[]) {
        if (args.length != 1) {
            System.out.println("usage:  java MultiChat <your userName>");
            System.exit(0);
        }
        new MultiChat(args[0]);
    } // end of main()

} // end of MultiChat class

