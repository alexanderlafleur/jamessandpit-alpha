package Mover3D;

// Mover3D.java
// Thana Konglikhit, October 2003, s4310170@maliwan.psu.ac.th
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* A 3D moveable figure in the chcekboard world.
 The figure can be moved (like the sprite in Tour3D), but
 also has moveable limbs.

 A command panel allows figure and limb movement commands
 to be entered. The user can also control the figure's
 position via the keyboard.
 */
import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;

public class Mover3D extends JFrame {
    /**
     * 
     */
    private static final long serialVersionUID = -1139236572379165159L;

    public static void main(String[] args) {
        new Mover3D();
    }

    // -----------------------------------------
    public Mover3D() {
        super("3D Moveable Figure");
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        WrapMover3D w3d = new WrapMover3D();
        c.add(w3d, BorderLayout.CENTER);
        Figure figure = w3d.getFigure();
        CommandsPanel cp = new CommandsPanel(figure);
        c.add(cp, BorderLayout.SOUTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setResizable(false); // fixed size display
        setVisible(true);
    } // end of Mover3D()
} // end of Mover3D class
