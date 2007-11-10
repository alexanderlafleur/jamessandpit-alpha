package NetTour3D;

// NetTour3D.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* The Tour3D world (checkboard, obstacles, scenery). 
 The scenery and obstacles are loaded from a 'tour' 
 file, as before.

 Now several robots can be present, one for
 each 'visitor' to the networked world. 

 The visitor's name appears above their robot sprite.

 The command line may also include a starting (x,z) position.

 Usage:
 java -cp %CLASSPATH%;ncsa\\portfolio.jar NetTour3D 
 <userName>  [<xPosn> <zPosn>]  <tourFile>
 */

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class NetTour3D extends JFrame {
    /**
     * 
     */
    private static final long serialVersionUID = -6263795973343117972L;

    private WrapNetTour3D w3d;

    private String userName = null;

    private String tourFnm = null;

    private double xPosn = 0.0; // default starting coord

    private double zPosn = 0.0;

    public NetTour3D(String args[]) {
        super("3D NetTour");
        processArgs(args);
        setTitle(getTitle() + " for " + this.userName);

        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        this.w3d = new WrapNetTour3D(this.userName, this.tourFnm, this.xPosn, this.zPosn);
        c.add(this.w3d, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                NetTour3D.this.w3d.closeLink();
            }
        });

        pack();
        setResizable(false); // fixed size display
        setVisible(true);
    } // end of NetTour3D()

    private void processArgs(String[] args)
    /*
     * Extract the name, the starting (x,z) coord, and tour file name from the command line. The coord is optional.
     */
    {
        if (args.length == 4) {
            this.userName = args[0];
            this.tourFnm = args[3];

            try {
                this.xPosn = Double.parseDouble(args[1]);
            } catch (NumberFormatException ex) {
                System.out.println("xPosn value must be double");
            }

            try {
                this.zPosn = Double.parseDouble(args[2]);
            } catch (NumberFormatException ex) {
                System.out.println("zPosn value must be double");
            }
        } else if (args.length == 2) {
            this.userName = args[0];
            this.tourFnm = args[1];
            System.out.println("xPosn and zPosn set to 0.0");
        } else {
            System.out.println("Usage: java -cp %CLASSPATH%;ncsa\\portfolio.jar NetTour3D <userName> [<xPosn> <zPosn>] <tourFile>");
            System.exit(0);
        }
    } // end of processArgs()

    // -----------------------------------------

    public static void main(String[] args) {
        new NetTour3D(args);
    }

} // end of NetTour3D class

