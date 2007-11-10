package FPShooter3D;

// FPShooter3D.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* The usual checkboard world used to illustrate coding for a
 First Person Shooter (FPS).

 The gun hand is a texture added to the user's viewpoint, controlled 
 by the KeyBehaviour class. When the gun is fired, a beam appears.
 It explodes if it 'hits' the target, otherwise it just disappears.

 PropManager is used to load a robot 'target'.

 Several beams/explosions can be happening at the same time
 - done by threads, but the Shapes are created at initialisation
 time and reused.
 */

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;

public class FPShooter3D extends JFrame {
    /**
     * 
     */
    private static final long serialVersionUID = -8250482743643978898L;

    public FPShooter3D() {
        super("FPShooter3D");

        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        WrapFPShooter3D w3d = new WrapFPShooter3D();
        c.add(w3d, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setResizable(false); // fixed size display
        setVisible(true);
    } // end of FPShooter3D()

    // -----------------------------------------

    public static void main(String[] args) {
        new FPShooter3D();
    }

} // end of FPShooter3D class
