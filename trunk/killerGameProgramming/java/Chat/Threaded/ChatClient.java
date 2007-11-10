package Chat.Threaded;

// ChatClient.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* The ChatClient interacts with the ChatServer.
 It can send the following messages:

 who -- a list of users is returned, with the format
 "WHO$$ cliAddr1 & port1 & ... cliAddrN & portN & "

 bye -- client is disconnecting

 any text  -- which is broadcast to all clients with the format
 "(cliAddr,port): msg".
 The address and port uniquely identify the client.

 The first & third messages are sent via a GUI interface:
 * a "Who" button
 * a "Message:" text field

 Clicking the close box of the window, causes the client to
 send the "bye" message before terminating. 

 There is a separate threaded ChatWatcher object for processing 
 messages coming from the ChatServer object.

 ---- Changes 30 August 2004 ---

 Modified showMsg() to move the caret position, and use invokeLater()
 to avoid Swing+threads problem. For details see:
 http://java.sun.com/products/jfc/tsc/articles/threads/threads1.html
 - thanks to Rachel Struthers (rmstruthers@mn.rr.com)

 Moved makeContact() call to after window made visible.
 */

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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

public class ChatClient extends JFrame implements ActionListener {
    /**
     * 
     */
    private static final long serialVersionUID = 369838802284096226L;

    private static final int PORT = 1234; // server details

    private static final String HOST = "localhost";

    private Socket sock;

    private PrintWriter out; // output to the server

    private JTextArea jtaMesgs; // output text area

    private JTextField jtfMsg; // for sending messages

    private JButton jbWho; // for sending a "who" message

    public ChatClient() {
        super("Chat Client");
        initializeGUI();
        // makeContact();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeLink();
            }
        });

        setSize(300, 450);
        setVisible(true);

        makeContact(); // change: moved so window visible before contact
    } // end of ChatClient();

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
        // name/score

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

    private void closeLink() {
        try {
            this.out.println("bye"); // tell server that client is disconnecting
            this.sock.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        System.exit(0);
    } // end of closeLink()

    private void makeContact()
    // contact the server and start a ChatWatcher thread
    {
        try {
            this.sock = new Socket(HOST, PORT);
            BufferedReader in = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
            this.out = new PrintWriter(this.sock.getOutputStream(), true); // autoflush

            new ChatWatcher(this, in).start(); // start watching for server
            // msgs
        } catch (Exception e) {
            System.out.println(e);
        }
    } // end of makeContact()

    public void actionPerformed(ActionEvent e)
    /*
     * Either a message is to be sent or the "Who" button has been pressed.
     */
    {
        if (e.getSource() == this.jbWho) {
            this.out.println("who"); // the response is read by ChatWatcher
        } else if (e.getSource() == this.jtfMsg) {
            sendMessage();
        }
    }

    private void sendMessage()
    /*
     * Check if the user has supplied a message, then send it to the server.
     */
    {
        String msg = this.jtfMsg.getText().trim();
        // System.out.println("'"+msg+"'");

        if (msg.equals("")) {
            JOptionPane.showMessageDialog(null, "No message entered", "Send Message Error", JOptionPane.ERROR_MESSAGE);
        } else {
            this.out.println(msg);
            // showMsg("Sent: " + msg + "\n");
        }
    } // end of sendMessage()

    /*
     * synchronized public void showMsgFoo(String msg) // Synchronized since this method can be called by this // object and the URLChatWatcher thread. { jtaMesgs.append(msg); }
     */

    public void showMsg(final String msg)
    /*
     * We're updating the messages text area, so the code should be carried out by Swing's event dispatching thread, which is achieved by calling invokeLater().
     * 
     * msg must be final to be used inside the inner class for Runnable.
     * 
     * showMsg() may be called by this object and the ChatWatcher thread, but the updates are serialised by being placed in the queue associated with the event dispatcher. This
     * means that there's no need to synchronize this method.
     * 
     * Thanks to Rachel Struthers (rmstruthers@mn.rr.com)
     */
    {
        // System.out.println("showMsg(): " + msg);
        Runnable updateMsgsText = new Runnable() {
            public void run() {
                ChatClient.this.jtaMesgs.append(msg); // append message to text area
                ChatClient.this.jtaMesgs.setCaretPosition(ChatClient.this.jtaMesgs.getText().length());
                // move insertion point to the end of the text
            }
        };
        SwingUtilities.invokeLater(updateMsgsText);
    } // end of showMsg()

    // ------------------------------------

    public static void main(String args[]) {
        new ChatClient();
    }

} // end of ChatClient class

