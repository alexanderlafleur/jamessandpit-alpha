package Checkers3D;

// Checkers3D.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* A simple basic world consisting of a checkboard floor, 
 with a red center square, and labelled XZ axes.

 A floating, shiny blue sphere is placed at the center.
 */
import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;

public class Checkers3D extends JFrame {
    /**
     * 
     */
    private static final long serialVersionUID = -5024121903454882795L;

    public static void main(String[] args) {
        new Checkers3D();
    }

    // -----------------------------------------
    public Checkers3D() {
        super("Checkers3D");
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        WrapCheckers3D w3d = new WrapCheckers3D(); // panel holding the 3D
        // canvas
        c.add(w3d, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setResizable(false); // fixed size display
        setVisible(true);
    } // end of Checkers3D()
} // end of Checkers3D class
