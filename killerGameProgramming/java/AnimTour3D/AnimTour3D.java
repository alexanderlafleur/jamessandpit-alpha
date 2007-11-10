package AnimTour3D;

// AnimTour3D.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* The usual checkboard world and a user-controlled 3D sprite 
 (the tourist) who has an animated walking movement.

 The tourist cam move about on the XZ plane but can not 
 move off the board.

 No scenery and obstacles, but it's easy to borrow code from
 /Tour3D
 */

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;

public class AnimTour3D extends JFrame {
    /**
     * 
     */
    private static final long serialVersionUID = -1844767616882557599L;

    public AnimTour3D() {
        super("Animated 3D Tour");

        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        WrapAnimTour3D w3d = new WrapAnimTour3D();
        c.add(w3d, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setResizable(false); // fixed size display
        setVisible(true);
    } // end of AnimTour3D()

    // -----------------------------------------

    public static void main(String[] args) {
        new AnimTour3D();
    }

} // end of AnimTour3D class

