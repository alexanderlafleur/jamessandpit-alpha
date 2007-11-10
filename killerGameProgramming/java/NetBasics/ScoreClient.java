package NetBasics;

// ScoreClient.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/*
 A test rig for ScoreServer with a GUI interface
 The client can send a name/score, and ask for the 
 current high scores. Clicking the close box of the
 window breaks the network link.

 Or we could just use:
 telnet localhost 1234
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
import java.util.StringTokenizer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ScoreClient extends JFrame implements ActionListener {
    /**
     * 
     */
    private static final long serialVersionUID = -7496531017894635451L;

    private static final int PORT = 1234; // server details

    private static final String HOST = "localhost";

    private Socket sock;

    private BufferedReader in; // i/o for the client

    private PrintWriter out;

    private JTextArea jtaMesgs;

    private JTextField jtfName, jtfScore;

    private JButton jbGetScores;

    public ScoreClient() {
        super("High Score Client");

        initializeGUI();
        makeContact();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeLink();
            }
        });

        setSize(300, 450);
        setVisible(true);
    } // end of ScoreClient();

    private void initializeGUI()
    // text area in center, and controls in south
    {
        Container c = getContentPane();
        c.setLayout(new BorderLayout());

        this.jtaMesgs = new JTextArea(7, 7);
        this.jtaMesgs.setEditable(false);
        JScrollPane jsp = new JScrollPane(this.jtaMesgs);
        c.add(jsp, "Center");

        JLabel jlName = new JLabel("Name: ");
        this.jtfName = new JTextField(10);

        JLabel jlScore = new JLabel("Score: ");
        this.jtfScore = new JTextField(5);
        this.jtfScore.addActionListener(this); // pressing enter triggers sending
        // of name/score

        this.jbGetScores = new JButton("Get Scores");
        this.jbGetScores.addActionListener(this);

        JPanel p1 = new JPanel(new FlowLayout());
        p1.add(jlName);
        p1.add(this.jtfName);
        p1.add(jlScore);
        p1.add(this.jtfScore);

        JPanel p2 = new JPanel(new FlowLayout());
        p2.add(this.jbGetScores);

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
    }

    private void makeContact() {
        try {
            this.sock = new Socket(HOST, PORT);
            this.in = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
            this.out = new PrintWriter(this.sock.getOutputStream(), true); // autoflush
        } catch (Exception e) {
            System.out.println(e);
        }
    } // end of makeContact()

    public void actionPerformed(ActionEvent e)
    // Either a name/score is to be sent or the "Get Scores"
    // button has been pressed
    {
        if (e.getSource() == this.jbGetScores) {
            sendGet();
        } else if (e.getSource() == this.jtfScore) {
            sendScore();
        }
    }

    private void sendGet() {
        // Send "get" command, read response and display it
        // Response should be "HIGH$$ n1 & s1 & .... nN & sN & "
        try {
            this.out.println("get");
            String line = this.in.readLine();
            System.out.println(line);
            if ((line.length() >= 7) && // "HIGH$$ "
                    (line.substring(0, 6).equals("HIGH$$"))) {
                showHigh(line.substring(6).trim());
                // remove HIGH$$ keyword and surrounding spaces
            } else {
                // should not happen
                this.jtaMesgs.append(line + "\n");
            }
        } catch (Exception ex) {
            this.jtaMesgs.append("Problem obtaining high scores\n");
            System.out.println(ex);
        }
    } // end of sendGet()

    private void showHigh(String line)
    // Parse the high scores and display in a nicer way
    {
        StringTokenizer st = new StringTokenizer(line, "&");
        String name;
        int i, score;
        i = 1;
        try {
            while (st.hasMoreTokens()) {
                name = st.nextToken().trim();
                score = Integer.parseInt(st.nextToken().trim());
                this.jtaMesgs.append("" + i + ". " + name + " : " + score + "\n");
                i++;
            }
            this.jtaMesgs.append("\n");
        } catch (Exception e) {
            this.jtaMesgs.append("Problem parsing high scores\n");
            System.out.println("Parsing error with high scores: \n" + e);
        }
    } // end of showHigh()

    private void sendScore()
    // Check if the user has supplied a name and score, then
    // send "score name & score &" to server
    // NOte: we should check that score is an integer, but we don't
    {
        String name = this.jtfName.getText().trim();
        String score = this.jtfScore.getText().trim();
        // System.out.println("'"+name+"' '"+score+"'");

        if ((name.equals("")) && (score.equals(""))) {
            JOptionPane.showMessageDialog(null, "No name and score entered", "Send Score Error", JOptionPane.ERROR_MESSAGE);
        } else if (name.equals("")) {
            JOptionPane.showMessageDialog(null, "No name entered", "Send Score Error", JOptionPane.ERROR_MESSAGE);
        } else if (score.equals("")) {
            JOptionPane.showMessageDialog(null, "No score entered", "Send Score Error", JOptionPane.ERROR_MESSAGE);
        } else {
            this.out.println("score " + name + " & " + score + " &");
            this.jtaMesgs.append("Sent " + name + " & " + score + "\n");
        }
    } // end of sendScore()

    // ------------------------------------

    public static void main(String args[]) {
        new ScoreClient();
    }

} // end of ScoreClient class

