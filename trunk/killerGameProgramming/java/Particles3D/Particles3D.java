package Particles3D;

// Particles3D.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Three different implementations of Particle systems:
 * points in a PointArray
 * lines in a LineArray
 * quads in a QuadArray

 Geometrries are stored using BY_REFERENCE, and updated with
 a GeometryUpdater subclass.

 The QuadArray example illustrates how to apply a single texture 
 to each of the quads, and how to blend texture and colour.
 */

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;

public class Particles3D extends JFrame {
    /**
     * 
     */
    private static final long serialVersionUID = -5718522886296524936L;

    private static final int NUM_PARTICLES = 3000;

    private static final int FOUNTAIN_CHOICE = 1;

    public Particles3D(String args[]) {
        super("Particles3D");

        int numParticles = NUM_PARTICLES;
        int fountainChoice = FOUNTAIN_CHOICE;

        if (args.length > 0) {
            try {
                numParticles = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Illegal number of particles");
            }

            if (numParticles < 0) {
                System.out.println("Number of particles must be positive");
                numParticles = NUM_PARTICLES;
            }
        }

        if (args.length > 1) {
            try {
                fountainChoice = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Illegal fountain choice");
            }

            if ((fountainChoice < 1) || (fountainChoice > 3)) {
                System.out.println("Fountain choices are 1-3");
                fountainChoice = FOUNTAIN_CHOICE;
            }
        }

        System.out.println("numParticles: " + numParticles + "; fountainChoice: " + fountainChoice);

        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        WrapParticles3D w3d = new WrapParticles3D(numParticles, fountainChoice);
        c.add(w3d, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setResizable(false); // fixed size display
        setVisible(true);
    } // end of Particles3D()

    // -----------------------------------------

    public static void main(String[] args) {
        new Particles3D(args);
    }

} // end of Particles3D class

