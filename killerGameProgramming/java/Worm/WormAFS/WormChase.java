package Worm.WormAFS;

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
 
 'Almost' full screen since the JFrame's title bar and insets (borders)
 are still present, as well as the OS's insets (e.g. the Windows
 taskbar) and the textfields under the JPanel. 

 The display includes two textfields for showing the current time
 and number of boxes. The average FPS/UPS values are drawn in
 the game's JPanel.

 Pausing/Resuming/Quiting are controlled via the frame's window
 listener methods.

 Any movement of the JFrame is undone, with the window moved back
 to its original position.

 Using Java 3D's timer: J3DTimer.getValue()
 *  nanosecs rather than millisecs for the period

 Average FPS / UPS
 20			50			80			100
 Win 98:         20/20       49/50       75/83       86/100
 Win 2000:       20/20       20/50       20/83       20/100
 Win XP (1):     20/20       50/50       82/83       87/100
 Win XP (2):     20/20       50/50       75/83       75/100

 Located in /WormAFS

 */

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class WormChase extends JFrame implements WindowListener {
    /**
     * 
     */
    private static final long serialVersionUID = -4267316454963357593L;

    private static int DEFAULT_FPS = 80;

    private WormPanel wp;

    private JTextField jtfBox; // displays no.of boxes used

    private JTextField jtfTime; // displays time spent in game

    private int pWidth, pHeight; // diemensions of the panel

    public WormChase(long period) {
        super("The Worm Chase");

        makeGUI();

        pack(); // first one (the GUI doesn't include the JPanel yet)
        setResizable(false); // sizes may change when non-resizable
        calcSizes();
        setResizable(true);

        Container c = getContentPane();
        this.wp = new WormPanel(this, period, this.pWidth, this.pHeight);
        c.add(this.wp, "Center");
        pack(); // second, after JPanel added

        addWindowListener(this);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e)
            /*
             * Called by the Component listener when the JFrame is moved. Put it back in its original position.
             */
            {
                setLocation(0, 0);
            }
        });

        setResizable(false);
        setVisible(true);
    } // end of WormChase() constructor

    private void makeGUI()
    // Create the GUI, minus the JPanel drawing area
    {
        Container c = getContentPane(); // default BorderLayout used

        JPanel ctrls = new JPanel(); // a row of textfields
        ctrls.setLayout(new BoxLayout(ctrls, BoxLayout.X_AXIS));

        this.jtfBox = new JTextField("Boxes used: 0");
        this.jtfBox.setEditable(false);
        ctrls.add(this.jtfBox);

        this.jtfTime = new JTextField("Time Spent: 0 secs");
        this.jtfTime.setEditable(false);
        ctrls.add(this.jtfTime);

        c.add(ctrls, "South");
    } // end of makeGUI()

    public void setBoxNumber(int no) {
        this.jtfBox.setText("Boxes used: " + no);
    }

    public void setTimeSpent(int t) {
        this.jtfTime.setText("Time Spent: " + t + " secs");
    }

    private void calcSizes()
    /*
     * Calculate the size of the drawing panel to fill the screen, but leaving room for the JFrame's title bar and insets, the OS's insets (e.g. taskbar) and the textfields under
     * the JPanel.
     */
    {
        GraphicsConfiguration gc = getGraphicsConfiguration();
        Rectangle screenRect = gc.getBounds();
        // System.out.println("Screen size: " + screenRect);

        Toolkit tk = Toolkit.getDefaultToolkit();
        Insets desktopInsets = tk.getScreenInsets(gc);
        // System.out.println("OS Insets: " + desktopInsets);

        Insets frameInsets = getInsets(); // only works after a pack() call
        // System.out.println("JFrame Insets: " + frameInsets);

        Dimension tfDim = this.jtfBox.getPreferredSize(); // size of text field
        // System.out.println("Box TF Size: " + tfDim );
        // System.out.println("Time TF Size: " + jtfTime.getPreferredSize() );

        this.pWidth = screenRect.width - (desktopInsets.left + desktopInsets.right) - (frameInsets.left + frameInsets.right);

        this.pHeight = screenRect.height - (desktopInsets.top + desktopInsets.bottom) - (frameInsets.top + frameInsets.bottom) - tfDim.height;

        // System.out.println("pWidth: " + pWidth + "; pHeight: " + pHeight);
    } // end of calcSizes()

    // ----------------- window listener methods -------------

    public void windowActivated(WindowEvent e) {
        this.wp.resumeGame();
    }

    public void windowDeactivated(WindowEvent e) {
        this.wp.pauseGame();
    }

    public void windowDeiconified(WindowEvent e) {
        this.wp.resumeGame();
    }

    public void windowIconified(WindowEvent e) {
        this.wp.pauseGame();
    }

    public void windowClosing(WindowEvent e) {
        this.wp.stopGame();
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }

    // ----------------------------------------------------

    public static void main(String args[]) {
        int fps = DEFAULT_FPS;
        if (args.length != 0) {
            fps = Integer.parseInt(args[0]);
        }

        long period = (long) 1000.0 / fps;
        System.out.println("fps: " + fps + "; period: " + period + " ms");

        new WormChase(period * 1000000L); // ms --> nanosecs
    }

} // end of WormChase class

