package Trees3D;

// Trees3D.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Illustrates techniques for simulating growth, applied to
 trees that grow and sprout leaves.

 1. Use scaling to gradually stretch cylinders representing tree limbs.

 2. Incrementally change the colour of the cylinders from green to blue.

 3. Leaves on a tree limb are actually just a single image on a 'screen'.
 The image can be replaced by another one, which contains more leaves,
 creating the effect that the leaves are 'growing'.

 The changes carried out on a tree limb in each time frame are specified
 by 'rules' in the GrowthBehavior object. These can be adjusted to make
 a variety of different effects.
 */

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;

public class Trees3D extends JFrame {
    /**
     * 
     */
    private static final long serialVersionUID = -8883364531563854844L;

    public Trees3D() {
        super("Trees3D");
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        WrapTrees3D w3d = new WrapTrees3D(); // panel holding the 3D canvas
        c.add(w3d, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setResizable(false); // fixed size display
        setVisible(true);
    } // end of Trees3D()

    // -----------------------------------------

    public static void main(String[] args) {
        new Trees3D();
    }

} // end of Trees3D class
