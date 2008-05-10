package BugRunner;

// BugRunner.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* A user can move a bat to hit a ball back up the screen.
 Each hit of a ball increments a 'rebounds' counter. Each
 missed ball decrements it.

 A 'new' ball is sent downwards from the top when the previous 
 ball has left the screen.

 When the number of rebounds reaches a prescribed maximum,
 or drops to 0, then the game is over, and a score is 
 reported.

 -----
 Pausing/Resuming/Quiting are controlled via the frame's window
 listener methods.

 Active rendering is used to update the JPanel. See WormP for
 another example, with additional statistics generation.

 Using Java 3D's timer: J3DTimer.getValue()
 *  nanosecs rather than millisecs for the period

 The MidisLoader, ClipsLoader, ImagesLoader, and ImagesPlayer
 classes are used for music, sounds, images, and animation.

 The bat and the ball are subclasses of a Sprite class.
 */
import java.awt.Container;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

public class BugRunner extends JFrame implements WindowListener {
    private static int DEFAULT_FPS = 40;
    /**
     * 
     */
    private static final long serialVersionUID = -8498593403214266917L;

    public static void main(String args[]) {
        long period = (long) 1000.0 / DEFAULT_FPS;
        // System.out.println("fps: " + DEFAULT_FPS + "; period: " + period + "
        // ms");
        new BugRunner(period * 1000000L); // ms --> nanosecs
    }

    private BugPanel bp; // where the game is drawn
    private MidisLoader midisLoader;

    // ----------------- window listener methods -------------
    public BugRunner(long period) {
        super("BugRunner");
        // load the background MIDI sequence
        midisLoader = new MidisLoader();
        midisLoader.load("br", "blade_runner.mid");
        midisLoader.play("br", true); // repeatedly play it
        Container c = getContentPane(); // default BorderLayout used
        bp = new BugPanel(this, period);
        c.add(bp, "Center");
        addWindowListener(this);
        pack();
        setResizable(false);
        setVisible(true);
    } // end of BugRunner() constructor

    public void windowActivated(WindowEvent e) {
        bp.resumeGame();
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        bp.stopGame();
        midisLoader.close(); // not really required
    }

    public void windowDeactivated(WindowEvent e) {
        bp.pauseGame();
    }

    public void windowDeiconified(WindowEvent e) {
        bp.resumeGame();
    }

    public void windowIconified(WindowEvent e) {
        bp.pauseGame();
    }

    // ----------------------------------------------------
    public void windowOpened(WindowEvent e) {
    }
} // end of BugRunner class
