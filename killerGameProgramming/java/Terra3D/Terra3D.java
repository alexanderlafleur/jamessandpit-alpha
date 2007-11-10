package Terra3D;

// Terra3D.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Load a terrain created using the TerraGen application,
 and exported as a .obj file (a mesh) and a .jpg (a texture).

 Use the Terra3D.bat file which takes the name of the
 terrain as an argument.
 e.g. Terrain3D test2

 This looks in /models for test2.obj and test2.jpg.

 The terrain may include 3D scenery, and *must* have a start
 position for the user, which are specified in 
 /models/<fn>.txt
 e.g.    /models/test2.txt

 The terrain may be populated with ground cover at random locations
 within a certain range of heights.
 The name of the ground cover GIFs, and the number required are
 specified in 
 /models/<fn>GC.txt
 e.g.    /models/test2GC.txt

 The user can navigate over the surface of the terrain using
 key controls.


 ------------------------------------------
 Compilation can be carried out with compileTerra3D.bat:
 javac -classpath "%CLASSPATH%;ncsa\portfolio.jar" *.java
 */

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;

public class Terra3D extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = -3020505106328410325L;

    public Terra3D(String fn) {
        super("Terra3D");

        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        WrapTerra3D w3d = new WrapTerra3D(fn);
        c.add(w3d, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setResizable(false); // fixed size display
        setVisible(true);
    } // end of Terra3D()

    // -----------------------------------------

    public static void main(String[] args) {
        if (args.length == 1) {
            new Terra3D(args[0]);
        } else {
            System.out.println("Usage: java -cp %CLASSPATH%;ncsa\\portfolio.jar Terra3D  <file>");
        }
    }

} // end of Terra3D class

