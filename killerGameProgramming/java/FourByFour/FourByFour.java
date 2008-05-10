package FourByFour;

// FourByFour.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* 
 Derived from  FourByFour.java 1.13 01/01/11 07:32:10
 Copyright (c) 1996-2000 Sun Microsystems, Inc. All Rights Reserved.

 A 3D version of Tic-Tac-Toe made up of a 4x4x4 grid.
 Two player compete to make a row, column, or diagonal of
 4 spheres or boxes.

 This game is the basis of the network application
 in NetFourByFour/

 Changes from Sun version:
 - removed game logic; replaced with second player
 * this has made the Board class *much* simpler
 - removed most of GUI: no skill levels, no instructions,
 no new game option
 - used Swing rather than AWT 
 - used Java3D utilities: Box, Cylinder
 - removed ID class; now I store an Integer objet in the
 user data field of each shape
 - modified the PickDragBehavior logic so that dragging 
 doesn't 'jump' when first used
 */
import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FourByFour extends JFrame {
    /**
     * 
     */
    private static final long serialVersionUID = 6233185937072018778L;

    public static void main(String[] args) {
        new FourByFour();
    }

    private JTextField messageTF;

    public FourByFour() {
        super("Four By Four");
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        WrapFourByFour w3d = new WrapFourByFour(this);
        c.add(w3d, BorderLayout.CENTER);
        JLabel mesgLabel = new JLabel("Messages: ");
        messageTF = new JTextField(30);
        messageTF.setText("Player 1's turn");
        messageTF.setEditable(false);
        JPanel p1 = new JPanel();
        p1.add(mesgLabel);
        p1.add(messageTF);
        c.add(p1, BorderLayout.SOUTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setResizable(false); // fixed size display
        setVisible(true);
    } // end of FourByFour()

    // -----------------------------------------
    public void showMessage(String mesg) {
        messageTF.setText(mesg);
    }
} // end of FourByFour class
