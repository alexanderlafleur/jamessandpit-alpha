package Trees3D;

// CheckerFloor.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* The floor is a blue and green chessboard, with a small red square
 at the (0,0) position on the (X,Z) plane, and with numbers along
 the X- and Z- axes.
 */
import java.awt.Font;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.geometry.Text2D;

public class CheckerFloor {
    // colours for floor, etc
    private final static Color3f blue = new Color3f(0.0f, 0.1f, 0.4f);
    private final static int FLOOR_LEN = 20; // should be even
    private final static Color3f green = new Color3f(0.0f, 0.5f, 0.1f);
    private final static Color3f medRed = new Color3f(0.8f, 0.4f, 0.3f);
    private final static Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
    private BranchGroup floorBG;

    public CheckerFloor()
    // create tiles, add origin marker, then the axes labels
    {
        floorBG = new BranchGroup();
        boolean isBlue;
        for (int z = -FLOOR_LEN / 2; z <= FLOOR_LEN / 2 - 1; z++) {
            isBlue = z % 2 == 0 ? true : false; // set colour for new row
            for (int x = -FLOOR_LEN / 2; x <= FLOOR_LEN / 2 - 1; x++) {
                createTile(x, z, isBlue);
                isBlue = !isBlue;
            }
        }
        addOriginMarker();
        labelAxes();
    } // end of CheckerFloor()

    private void addOriginMarker()
    // A red square centered at (0,0,0), of length 0.5
    {
        Point3f p4 = new Point3f(-0.25f, 0.01f, -0.25f); // a bit above the
        // floor
        Point3f p3 = new Point3f(0.25f, 0.01f, -0.25f); // points created
        // clockwise
        Point3f p2 = new Point3f(0.25f, 0.01f, 0.25f);
        Point3f p1 = new Point3f(-0.25f, 0.01f, 0.25f);
        floorBG.addChild(new ColouredTile(p1, p2, p3, p4, medRed));
    } // end of addOriginMarker();

    private void createTile(int x, int z, boolean isBlue)
    // A single blue or green square, its left hand corner at (x,0,z)
    {
        Point3f p4 = new Point3f(x, 0.0f, z); // points created in clockwise
        // order
        Point3f p3 = new Point3f(x + 1.0f, 0.0f, z);
        Point3f p2 = new Point3f(x + 1.0f, 0.0f, z + 1.0f);
        Point3f p1 = new Point3f(x, 0.0f, z + 1.0f);
        Color3f col = isBlue ? blue : green;
        floorBG.addChild(new ColouredTile(p1, p2, p3, p4, col));
    } // end of createTile()

    public BranchGroup getBG() {
        return floorBG;
    }

    private void labelAxes()
    // Place numbers along the X- and Z-axes at the integer positions
    {
        Vector3d pt = new Vector3d();
        for (int i = -FLOOR_LEN / 2; i <= FLOOR_LEN / 2; i++) {
            pt.x = i;
            floorBG.addChild(makeText(pt, "" + i)); // along x-axis
        }
        pt.x = 0;
        for (int i = -FLOOR_LEN / 2; i <= FLOOR_LEN / 2; i++) {
            pt.z = i;
            floorBG.addChild(makeText(pt, "" + i)); // along z-axis
        }
    } // end of labelAxes()

    private TransformGroup makeText(Vector3d vertex, String text)
    // Create a Text2D object at the specified vertex
    {
        Text2D message = new Text2D(text, white, "SansSerif", 36, Font.BOLD);
        // 36 point bold Sans Serif
        TransformGroup tg = new TransformGroup();
        Transform3D t3d = new Transform3D();
        t3d.setTranslation(vertex);
        tg.setTransform(t3d);
        tg.addChild(message);
        return tg;
    } // end of getTG()
} // end of CheckerFloor class
