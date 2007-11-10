package Tour3D;

// Tour3D.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Usual checkboard world but with:
 * extra scenery and obstacles
 - the scenery is loaded with PropManager objects
 - the obstacles cannot be passed through by the sprites
 * a touring 3D sprite controlled from the keyboard
 * an autonomous chasing alien hand
 * a view point that moves with the sprite (a 3rd person camera)
 * textured background
 * full screen 

 The touring sprite cam move about on the XZ plane but can not 
 pass through obstacles or move off the board.

 The scenery and obstacles are specified in a tour textfile
 specified on the command line and read from the /models
 subdirectory.
 */

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;

public class Tour3D extends JFrame {
    /**
     * 
     */
    private static final long serialVersionUID = -3370051493053475866L;

    public Tour3D(String args[]) {
        super("3D Tour");
        String tourFnm = null;

        if (args.length == 1) {
            tourFnm = args[0];
        } else {
            System.out.println("Usage: java -cp %CLASSPATH%;ncsa\\portfolio.jar Tour3D <TourFile>");
            System.exit(0);
        }

        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        WrapTour3D w3d = new WrapTour3D(tourFnm, this);
        c.add(w3d, BorderLayout.CENTER);

        setUndecorated(true); // no menu bars, borders
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pack();
        setResizable(false); // fixed size display
        setVisible(true);
    } // end of Tour3D()

    // -----------------------------------------

    public static void main(String[] args) {
        new Tour3D(args);
    }

} // end of Tour3D class

