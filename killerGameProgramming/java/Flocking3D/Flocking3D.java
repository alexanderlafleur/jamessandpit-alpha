package Flocking3D;

// Flocking3D.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
// Sirinart Sakarin, March 2003, s4210315@calvin.coe.psu.ac.th

/* This program illustrates flocking boids.

 Boids follow simple rules that govern their movement.
 When these simple rules are combine at run-time, complex
 behaviours emerge without the need for explicit programming.

 Our boid behaviours include:
 * perching occasionally
 * avoiding obstacles
 * staying within the scene volume
 * not moving faster than a set maximum speed
 * Reynold's velocity rules for:
 - cohesion
 - separation
 - alignment
 * the Predators chase (and eat) the prey boids
 * the prey boids try to out-run the predators

 Some coding ideas came from the Boids Demo by Anthony Steed 
 (A.Steed@cs.ucl.ac.uk) at 
 http://www.cs.ucl.ac.uk/staff/A.Steed/3ddesktop

 Some algorithm ideas came from the Boids pseudocode paper by 
 Conrad Parker ?? at
 http://www.vergenet.net/~conrad/boidsList/pseudocode.html
 */

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;

public class Flocking3D extends JFrame {
    /**
     * 
     */
    private static final long serialVersionUID = 2082904137579853563L;

    // constants for the numbers of predators, prey, and obstacles
    private static final int NUM_PREDATORS = 40;

    private static final int NUM_PREY = 160;

    private static final int NUM_OBSTACLES = 20;

    public Flocking3D(String[] args) {
        super("Flocking Predator and Prey Boids");

        int numPreds = NUM_PREDATORS;
        int numPrey = NUM_PREY;
        int numObstacles = NUM_OBSTACLES;

        // simple extraction of command line args.
        if (args.length >= 1) {
            try {
                numPreds = Integer.parseInt(args[0]);
                numPrey = numPreds; // same number of predators and prey
            } catch (NumberFormatException e) {
                System.out.println("Illegal number of predators");
            }
        }

        if (args.length >= 2) {
            try {
                numPrey = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Illegal number of prey");
            }
        }

        if (args.length == 3) {
            try {
                numObstacles = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                System.out.println("Illegal number of obstacles");
            }
        }

        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        WrapFlocking3D w3d = new WrapFlocking3D(numPreds, numPrey, numObstacles);
        c.add(w3d, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setResizable(false); // fixed size display
        setVisible(true);
    } // end of Flocking3D()

    // -----------------------------------------

    public static void main(String[] args) {
        new Flocking3D(args);
    }

} // end of Flocking3D class

