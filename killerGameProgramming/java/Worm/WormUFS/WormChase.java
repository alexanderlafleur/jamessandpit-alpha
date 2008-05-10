package Worm.WormUFS;

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

 Full screen JFrame with no title bar or insets (borders).
 They are switched off with setUndecorated().

 No decoration means that only the JPanel is drawn, with active
 rendering in WormPanel, and so we can ignore repaint() events
 by calling setIgnoreRepaint(). 

 Pausing/Resuming/Quiting are controlled via on-screen pause 
 and quit buttons. We also monitor for a shutdown event.


 Using Java 3D's timer: J3DTimer.getValue()
 *  nanosecs rather than millisecs for the period

 Average FPS / UPS
 20			50			80			100
 Win 98 (work):  20/20       48/50       70/83       70/100
 Win 2000:       18/20       19/50       18/83       18/100
 Win XP (1):     20/20       50/50       77/83       73/100
 Win XP (2):     20/20       50/50       68/83       69/100


 Located in /WormUFS
 */
import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;

public class WormChase extends JFrame {
    private static int DEFAULT_FPS = 80;
    /**
     * 
     */
    private static final long serialVersionUID = -5356013894243272164L;

    public static void main(String args[]) {
        int fps = DEFAULT_FPS;
        if (args.length != 0) {
            fps = Integer.parseInt(args[0]);
        }
        long period = (long) 1000.0 / fps;
        System.out.println("fps: " + fps + "; period: " + period + " ms");
        new WormChase(period * 1000000L); // ms --> nanosecs
    }

    // -----------------------------------------
    public WormChase(long period) {
        super("The Worm Chase");
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        WormPanel wp = new WormPanel(this, period);
        c.add(wp, "Center");
        setUndecorated(true); // no borders or title bar
        setIgnoreRepaint(true); // turn off all paint events since doing active
        // rendering
        pack();
        setResizable(false);
        setVisible(true);
    } // end of WormChase() constructor
} // end of WormChase class
