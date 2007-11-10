package Loader3D;

// Loader3D.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Load an object into the checkboard world, with its
 bounding sphere scaled with radius 1. A 3ds model is
 also rotated -90 around the x-axis.
 The Portfolio loaders are used.

 This class handles the moves, rotations, and scaling
 GUI interface, and a save button with stores the 
 placement info in a 'coords' file.

 When an object is loaded it can optionally be loaded
 with its coords file, created in an earlier execution
 of Loader.
 */

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class Loader3D extends JFrame implements ActionListener {
    /**
     * 
     */
    private static final long serialVersionUID = -8963964463411567947L;

    // for specifying moves and rotations
    private static final int X_AXIS = 0;

    private static final int Y_AXIS = 1;

    private static final int Z_AXIS = 2;

    private static final int INCR = 0;

    private static final int DECR = 1;

    private WrapLoader3D w3d; // the J3D canvas for the loader

    // the GUI elements
    private JButton xPosLeftBut, xPosRightBut, yPosLeftBut, yPosRightBut, zPosLeftBut, zPosRightBut;

    private JButton xRotLeftBut, xRotRightBut, yRotLeftBut, yRotRightBut, zRotLeftBut, zRotRightBut;

    private JTextField scaleTF;

    private JTextField xyzTF, rotTF, scaleTotTF;

    private JButton saveBut;

    private DecimalFormat df; // for textfield output

    public Loader3D(String args[]) {
        super("3D Loader");

        boolean hasCoordsInfo = false;
        String filename = null;
        if ((args.length == 2) && (args[0].equals("-c"))) {
            hasCoordsInfo = true;
            filename = args[1];
        } else if (args.length == 1) {
            filename = args[0];
        } else {
            System.out.println("Usage: java Loader3D [-c] <file>");
            System.exit(0);
        }

        this.w3d = new WrapLoader3D(filename, hasCoordsInfo);
        initGUI();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setResizable(false); // fixed size display
        setVisible(true);
    } // end of Loader3D()

    private void initGUI()
    /*
     * The GUI consists of the 3D canvas in the center, and a column of buttons, textfields, etc., down the right hand side (the control panel). The top half of the control panel
     * are for inputs: a series of buttons for carrying out moves and rotations, and a textfield for entering scaling info. The bottom half of the panel is for displaying the
     * current position, rotation, and scaling details.
     */
    {
        ImageIcon upIcon = new ImageIcon("icons/up.gif");
        ImageIcon downIcon = new ImageIcon("icons/down.gif");
        ImageIcon leftIcon = new ImageIcon("icons/left.gif");
        ImageIcon rightIcon = new ImageIcon("icons/right.gif");
        ImageIcon inIcon = new ImageIcon("icons/in.gif");
        ImageIcon outIcon = new ImageIcon("icons/out.gif");

        this.df = new DecimalFormat("0.###"); // 3 dp

        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        c.add(this.w3d, BorderLayout.CENTER);

        // build input controls

        JPanel p1 = new JPanel();
        JLabel xPosLabel = new JLabel("X incr:");
        this.xPosLeftBut = new JButton(leftIcon);
        this.xPosLeftBut.addActionListener(this);
        this.xPosRightBut = new JButton(rightIcon);
        this.xPosRightBut.addActionListener(this);
        p1.add(xPosLabel);
        p1.add(this.xPosLeftBut);
        p1.add(this.xPosRightBut);

        JPanel p2 = new JPanel();
        JLabel yPosLabel = new JLabel("Y incr:");
        this.yPosLeftBut = new JButton(downIcon);
        this.yPosLeftBut.addActionListener(this);
        this.yPosRightBut = new JButton(upIcon);
        this.yPosRightBut.addActionListener(this);
        p2.add(yPosLabel);
        p2.add(this.yPosLeftBut);
        p2.add(this.yPosRightBut);

        JPanel p3 = new JPanel();
        JLabel zPosLabel = new JLabel("Z incr:");
        this.zPosLeftBut = new JButton(inIcon);
        this.zPosLeftBut.addActionListener(this);
        this.zPosRightBut = new JButton(outIcon);
        this.zPosRightBut.addActionListener(this);
        p3.add(zPosLabel);
        p3.add(this.zPosLeftBut);
        p3.add(this.zPosRightBut);

        JPanel p4 = new JPanel();
        JLabel xRotLabel = new JLabel("X rot:");
        this.xRotLeftBut = new JButton(leftIcon);
        this.xRotLeftBut.addActionListener(this);
        this.xRotRightBut = new JButton(rightIcon);
        this.xRotRightBut.addActionListener(this);
        p4.add(xRotLabel);
        p4.add(this.xRotLeftBut);
        p4.add(this.xRotRightBut);

        JPanel p5 = new JPanel();
        JLabel yRotLabel = new JLabel("Y rot:");
        this.yRotLeftBut = new JButton(leftIcon);
        this.yRotLeftBut.addActionListener(this);
        this.yRotRightBut = new JButton(rightIcon);
        this.yRotRightBut.addActionListener(this);
        p5.add(yRotLabel);
        p5.add(this.yRotLeftBut);
        p5.add(this.yRotRightBut);

        JPanel p6 = new JPanel();
        JLabel zRotLabel = new JLabel("Z rot:");
        this.zRotLeftBut = new JButton(leftIcon);
        this.zRotLeftBut.addActionListener(this);
        this.zRotRightBut = new JButton(rightIcon);
        this.zRotRightBut.addActionListener(this);
        p6.add(zRotLabel);
        p6.add(this.zRotLeftBut);
        p6.add(this.zRotRightBut);

        JPanel p7 = new JPanel();
        JLabel scaleLabel = new JLabel("Scale mult:");
        this.scaleTF = new JTextField("1.1", 4);
        this.scaleTF.addActionListener(this);
        p7.add(scaleLabel);
        p7.add(this.scaleTF);

        JPanel p8 = new JPanel();
        this.saveBut = new JButton("Save Coords");
        this.saveBut.addActionListener(this);
        p8.add(this.saveBut);

        // build info. reporting controls

        JLabel xyzLabel = new JLabel("Pos (x,y,z):");
        this.xyzTF = new JTextField(10);
        this.xyzTF.setEditable(false);

        JLabel rotLabel = new JLabel("Rot (x,y,z):");
        this.rotTF = new JTextField(10);
        this.rotTF.setEditable(false);

        JPanel pScale = new JPanel();
        JLabel scaleTotLabel = new JLabel("Tot Scale:");
        this.scaleTotTF = new JTextField(4);
        this.scaleTotTF.setEditable(false);
        pScale.add(scaleTotLabel);
        pScale.add(this.scaleTotTF);

        // main control panel
        JPanel ctrlPanel = new JPanel();
        ctrlPanel.setLayout(new BoxLayout(ctrlPanel, BoxLayout.Y_AXIS));
        // add input controls
        ctrlPanel.add(p1);
        ctrlPanel.add(p2);
        ctrlPanel.add(p3);
        ctrlPanel.add(p4);
        ctrlPanel.add(p5);
        ctrlPanel.add(p6);
        ctrlPanel.add(p7);
        ctrlPanel.add(p8);

        // add a bit of space between the inputs and reporters
        ctrlPanel.add(javax.swing.Box.createVerticalStrut(15));

        // add info. reporting controls
        ctrlPanel.add(xyzLabel);
        ctrlPanel.add(this.xyzTF);
        ctrlPanel.add(rotLabel);
        ctrlPanel.add(this.rotTF);
        ctrlPanel.add(pScale);

        JPanel ctrlP = new JPanel();
        ctrlP.add(ctrlPanel);

        c.add(ctrlP, BorderLayout.EAST);

        showPosInfo(); // update on-screen display
        showRotInfo();
        showScale();
    } // end of initGUI()

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.saveBut) {
            this.w3d.saveCoordFile();
        } else if (e.getSource() == this.xPosLeftBut) {
            this.w3d.movePos(X_AXIS, DECR);
        } else if (e.getSource() == this.xPosRightBut) {
            this.w3d.movePos(X_AXIS, INCR);
        } else if (e.getSource() == this.yPosLeftBut) {
            this.w3d.movePos(Y_AXIS, DECR);
        } else if (e.getSource() == this.yPosRightBut) {
            this.w3d.movePos(Y_AXIS, INCR);
        } else if (e.getSource() == this.zPosLeftBut) {
            this.w3d.movePos(Z_AXIS, DECR);
        } else if (e.getSource() == this.zPosRightBut) {
            this.w3d.movePos(Z_AXIS, INCR);
        } else if (e.getSource() == this.xRotLeftBut) {
            this.w3d.rotate(X_AXIS, DECR);
        } else if (e.getSource() == this.xRotRightBut) {
            this.w3d.rotate(X_AXIS, INCR);
        } else if (e.getSource() == this.yRotLeftBut) {
            this.w3d.rotate(Y_AXIS, INCR);
        } else if (e.getSource() == this.yRotRightBut) {
            this.w3d.rotate(Y_AXIS, DECR);
        } else if (e.getSource() == this.zRotLeftBut) {
            this.w3d.rotate(Z_AXIS, INCR);
        } else if (e.getSource() == this.zRotRightBut) {
            this.w3d.rotate(Z_AXIS, DECR);
        } else if (e.getSource() == this.scaleTF) { // scale
            try {
                double d = Double.parseDouble(e.getActionCommand());
                this.w3d.scale(d);
            } catch (NumberFormatException ex) {
                System.out.println("Scale input was not a number");
            }
        }
        showPosInfo(); // update on-screen display
        showRotInfo();
        showScale();
    } // end of actionPerformed()

    private void showPosInfo() {
        Vector3d loc = this.w3d.getLoc();
        this.xyzTF.setText("( " + this.df.format(loc.x) + ", " + this.df.format(loc.y) + ", " + this.df.format(loc.z) + " )");
    }

    private void showRotInfo() {
        Point3d rots = this.w3d.getRotations();
        this.rotTF.setText("( " + this.df.format(rots.x) + ", " + this.df.format(rots.y) + ", " + this.df.format(rots.z) + " )");
    }

    private void showScale() {
        double scale = this.w3d.getScale();
        this.scaleTotTF.setText(this.df.format(scale));
    }

    // ----------------------------------------

    public static void main(String[] args) {
        new Loader3D(args);
    }

} // end of Loader3D class

