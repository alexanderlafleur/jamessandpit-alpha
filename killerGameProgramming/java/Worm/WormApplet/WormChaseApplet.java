package Worm.WormApplet;

// WormChaseApplet.java
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

 The Web page, WormChaseApplet.html, specifies 80 FPS:

 <applet code="WormChaseApplet.class" width="500" height="515">
 <param name="fps" value="80">
 </applet>

 The display includes two textfields for showing the current time
 amnd number of boxes. The average FPS/UPS values are drawn in
 the game's JPanel.

 Pausing/Resuming/Quiting are controlled via the applet's life
 cycle methods: stop(), start(), destroy()

 Using Java 3D's timer: J3DTimer.getValue()
 *  nanosecs rather than millisecs for the period

 Average FPS / UPS
 20			50			80			100
 Win 98 (work):  20/20       50/50       82/83       98/100
 Win 98 (home):  20/20       50/50       81/83       96/100
 Win 2000:       20/20       46/50       63/83       61/100   // slow machine
 Win XP:         20/20       50/50       83/83      100/100

 */
import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class WormChaseApplet extends JApplet {
    private static final int DEFAULT_FPS = 80;
    /**
     * 
     */
    private static final long serialVersionUID = 8794015237463029164L;
    private JTextField jtfBox; // displays no.of boxes used
    private JTextField jtfTime; // displays time spent in game
    private WormPanel wp; // where the worm is drawn

    @Override
    public void destroy() {
        wp.stopGame();
    }

    @Override
    public void init() {
        String str = getParameter("fps");
        int fps = str != null ? Integer.parseInt(str) : DEFAULT_FPS;
        long period = (long) 1000.0 / fps;
        System.out.println("fps: " + fps + "; period: " + period + " ms");
        makeGUI(period);
        wp.startGame();
    } // end of init()

    private void makeGUI(long period) {
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        wp = new WormPanel(this, period * 1000000L); // ms --> nanosecs
        c.add(wp, "Center");
        JPanel ctrls = new JPanel(); // holds a row of two textfields
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

    // -------------------- applet life cycle methods --------------
    public void setTimeSpent(int t) {
        jtfTime.setText("Time Spent: " + t + " secs");
    }

    @Override
    public void start() {
        wp.resumeGame();
    }

    @Override
    public void stop() {
        wp.pauseGame();
    }
} // end of WormChaseApplet class
