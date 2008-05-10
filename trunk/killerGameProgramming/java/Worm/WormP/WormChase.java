package Worm.WormP;

// WormChase.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* A worm moves around the screen and the user must
 click (press) on its head to complete the game.

 If the user misses the worm's head then a blue box
 will be added to the screen (if the worm's body was
 not clicked upon).

 A worm cannot move over a box, so the added obstacles
 *may* make it easier to catch the worm.

 A worm starts at 0 length and increases to a maximum
 length which it keeps from then on.

 A score is displayed on screen at the end, calculated
 from the number of boxes used and the time taken. Less
 boxes and less time mean a higher score.

 -------------

 The display includes two textfields for showing the current time
 and number of boxes. The average FPS/UPS values are drawn in
 the game's JPanel.

 Pausing/Resuming/Quiting are controlled via the frame's window
 listener methods.

 Uses active rendering to update the JPanel.

 Using Java 3D's timer: J3DTimer.getValue()
 *  nanosecs rather than millisecs for the period

 Average FPS / UPS
 20			50			80			100
 Win 98:    20/20       48/50       81/83       96/100
 Win 2000:  20/20       43/50       59/83       58/100    // slow machine
 Win XP:    20/20       50/50       83/83      100/100
 */
import java.awt.Container;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class WormChase extends JFrame implements WindowListener {
    private static int DEFAULT_FPS = 80;
    /**
     * 
     */
    private static final long serialVersionUID = 651478815822461889L;

    public static void main(String args[]) {
        int fps = DEFAULT_FPS;
        if (args.length != 0) {
            fps = Integer.parseInt(args[0]);
        }
        long period = (long) 1000.0 / fps;
        System.out.println("fps: " + fps + "; period: " + period + " ms");
        new WormChase(period * 1000000L); // ms --> nanosecs
    }

    private JTextField jtfBox; // displays no.of boxes used
    private JTextField jtfTime; // displays time spent in game
    private WormPanel wp; // where the worm is drawn

    public WormChase(long period) {
        super("The Worm Chase");
        makeGUI(period);
        addWindowListener(this);
        pack();
        setResizable(false);
        setVisible(true);
    } // end of WormChase() constructor

    private void makeGUI(long period) {
        Container c = getContentPane(); // default BorderLayout used
        wp = new WormPanel(this, period);
        c.add(wp, "Center");
        JPanel ctrls = new JPanel(); // a row of textfields
        ctrls.setLayout(new BoxLayout(ctrls, BoxLayout.X_AXIS));
        jtfBox = new JTextField("Boxes used: 0");
        jtfBox.setEditable(false);
        ctrls.add(jtfBox);
        jtfTime = new JTextField("Time Spent: 0 secs");
        jtfTime.setEditable(false);
        ctrls.add(jtfTime);
        c.add(ctrls, "South");
    } // end of makeGUI()

    public void setBoxNumber(int no) {
        jtfBox.setText("Boxes used: " + no);
    }

    // ----------------- window listener methods -------------
    public void setTimeSpent(long t) {
        jtfTime.setText("Time Spent: " + t + " secs");
    }

    public void windowActivated(WindowEvent e) {
        wp.resumeGame();
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        wp.stopGame();
    }

    public void windowDeactivated(WindowEvent e) {
        wp.pauseGame();
    }

    public void windowDeiconified(WindowEvent e) {
        wp.resumeGame();
    }

    public void windowIconified(WindowEvent e) {
        wp.pauseGame();
    }

    // ----------------------------------------------------
    public void windowOpened(WindowEvent e) {
    }
} // end of WormChase class
