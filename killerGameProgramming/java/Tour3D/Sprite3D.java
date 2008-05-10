package Tour3D;

// Sprite3D.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* Sprite3D loads a 3D image from fnm, and places it at (0,0,0). 
 We assume that the object's actual position is (0,0) in the XZ plane. 
 The Y position will vary but probably the base of the object is 
 touching the XZ plane.

 Movements are restricted to the XZ plane and rotations
 around the Y-axis.

 An object cannot move off the floor, or travel through obstacles 
 (as defined in the Obstacles object). 
 */
import java.text.DecimalFormat;

import javax.media.j3d.Switch;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

public class Sprite3D {
    private final static double OBS_FACTOR = 0.5;
    // used to reduce radius of bounding sphere around Sprite
    // when testing for intersection with obstacles
    private DecimalFormat df; // for simpler output during debugging
    private boolean isActive; // is the sprite active?
    private TransformGroup objectTG; // TG which the loaded object hangs off
    private Obstacles obs; // obstacles in the way of an object
    private double radius;
    private Transform3D t3d, toMove, toRot; // for manipulating objectTG's
    // transform
    private Switch visSwitch; // to make object visible/invisible

    public Sprite3D(String fnm, Obstacles obs) {
        df = new DecimalFormat("0.###"); // 3 dp
        this.obs = obs;
        // load object and coords
        PropManager propMan = new PropManager(fnm, true);
        radius = propMan.getScale(); // assume radius == scale
        // System.out.println("radius: " + df.format(radius));
        // create switch for visibility
        visSwitch = new Switch();
        visSwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);
        visSwitch.addChild(propMan.getTG()); // add object to switch
        visSwitch.setWhichChild(Switch.CHILD_ALL); // make visible
        // create a new transform group for the object
        objectTG = new TransformGroup();
        objectTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        objectTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objectTG.addChild(visSwitch);
        t3d = new Transform3D();
        toMove = new Transform3D();
        toRot = new Transform3D();
        isActive = true;
    } // end of Sprite3D()

    private void doMove(Vector3d theMove)
    // Move the sprite by the amount in theMove
    {
        objectTG.getTransform(t3d);
        toMove.setTranslation(theMove); // overwrite previous trans
        t3d.mul(toMove);
        objectTG.setTransform(t3d);
    } // end of doMove()

    public void doRotateY(double radians)
    // Rotate the sprite by radians amount around its y-axis
    {
        objectTG.getTransform(t3d);
        toRot.rotY(radians); // overwrite previous rotation
        t3d.mul(toRot);
        objectTG.setTransform(t3d);
    } // end of doRotateY()

    public Point3d getCurrLoc()
    // Return t sprite's current location
    {
        objectTG.getTransform(t3d);
        Vector3d trans = new Vector3d();
        t3d.get(trans);
        // printTuple(trans, "currLoc");
        return new Point3d(trans.x, trans.y, trans.z);
    } // end of getCurrLoc()

    public TransformGroup getTG() {
        return objectTG;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean moveBy(double x, double z)
    // Move the sprite by offsets x and z, but only if within the floor
    // and there is no obstacle nearby.
    {
        if (isActive()) {
            Point3d nextLoc = tryMove(new Vector3d(x, 0, z));
            if (obs.nearObstacle(nextLoc, radius * OBS_FACTOR)) {
                return false;
            } else {
                doMove(new Vector3d(x, 0, z)); // inefficient recalc
                return true;
            }
        } else {
            // not active
            return false;
        }
    } // end of moveBy()

    protected void printTuple(Tuple3d t, String id)
    // used for debugging, here and in subclasses
    {
        System.out.println(id + " x: " + df.format(t.x) + ", " + id + " y: " + df.format(t.y) + ", " + id + " z: " + df.format(t.z));
    } // end of printTuple()

    public void setActive(boolean b)
    // Activity changes affect the sprite's visibility
    {
        isActive = b;
        if (!isActive) {
            visSwitch.setWhichChild(Switch.CHILD_NONE); // make invisible
        } else if (isActive) {
            visSwitch.setWhichChild(Switch.CHILD_ALL); // make visible
        }
    } // end of setActive()

    public void setPosition(double xPos, double zPos)
    // move sprite to (xPos, zPos)
    {
        Point3d currLoc = getCurrLoc();
        double xMove = xPos - currLoc.x; // get offsets
        double zMove = zPos - currLoc.z;
        moveBy(xMove, zMove);
    } // end of setPosition()

    private Point3d tryMove(Vector3d theMove)
    /*
     * Calculate the effect of the given translation but do not update the sprite's position until it's been tested.
     */
    {
        objectTG.getTransform(t3d);
        toMove.setTranslation(theMove);
        t3d.mul(toMove);
        Vector3d trans = new Vector3d();
        t3d.get(trans);
        // printTuple(trans, "nextLoc");
        return new Point3d(trans.x, trans.y, trans.z);
    } // end of tryMove()
} // end of Sprite3D class
