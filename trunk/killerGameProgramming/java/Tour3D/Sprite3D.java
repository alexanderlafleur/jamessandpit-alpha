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

    private TransformGroup objectTG; // TG which the loaded object hangs off

    private Transform3D t3d, toMove, toRot; // for manipulating objectTG's
    // transform

    private Switch visSwitch; // to make object visible/invisible

    private double radius;

    private boolean isActive; // is the sprite active?

    private Obstacles obs; // obstacles in the way of an object

    private DecimalFormat df; // for simpler output during debugging

    public Sprite3D(String fnm, Obstacles obs) {
        this.df = new DecimalFormat("0.###"); // 3 dp
        this.obs = obs;

        // load object and coords
        PropManager propMan = new PropManager(fnm, true);
        this.radius = propMan.getScale(); // assume radius == scale
        // System.out.println("radius: " + df.format(radius));

        // create switch for visibility
        this.visSwitch = new Switch();
        this.visSwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);
        this.visSwitch.addChild(propMan.getTG()); // add object to switch
        this.visSwitch.setWhichChild(Switch.CHILD_ALL); // make visible

        // create a new transform group for the object
        this.objectTG = new TransformGroup();
        this.objectTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        this.objectTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        this.objectTG.addChild(this.visSwitch);

        this.t3d = new Transform3D();
        this.toMove = new Transform3D();
        this.toRot = new Transform3D();
        this.isActive = true;
    } // end of Sprite3D()

    public TransformGroup getTG() {
        return this.objectTG;
    }

    public void setPosition(double xPos, double zPos)
    // move sprite to (xPos, zPos)
    {
        Point3d currLoc = getCurrLoc();
        double xMove = xPos - currLoc.x; // get offsets
        double zMove = zPos - currLoc.z;
        moveBy(xMove, zMove);
    } // end of setPosition()

    public boolean moveBy(double x, double z)
    // Move the sprite by offsets x and z, but only if within the floor
    // and there is no obstacle nearby.
    {
        if (isActive()) {
            Point3d nextLoc = tryMove(new Vector3d(x, 0, z));
            if (this.obs.nearObstacle(nextLoc, this.radius * OBS_FACTOR)) {
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

    private void doMove(Vector3d theMove)
    // Move the sprite by the amount in theMove
    {
        this.objectTG.getTransform(this.t3d);
        this.toMove.setTranslation(theMove); // overwrite previous trans
        this.t3d.mul(this.toMove);
        this.objectTG.setTransform(this.t3d);
    } // end of doMove()

    private Point3d tryMove(Vector3d theMove)
    /*
     * Calculate the effect of the given translation but do not update the sprite's position until it's been tested.
     */
    {
        this.objectTG.getTransform(this.t3d);
        this.toMove.setTranslation(theMove);
        this.t3d.mul(this.toMove);
        Vector3d trans = new Vector3d();
        this.t3d.get(trans);
        // printTuple(trans, "nextLoc");
        return new Point3d(trans.x, trans.y, trans.z);
    } // end of tryMove()

    public void doRotateY(double radians)
    // Rotate the sprite by radians amount around its y-axis
    {
        this.objectTG.getTransform(this.t3d);
        this.toRot.rotY(radians); // overwrite previous rotation
        this.t3d.mul(this.toRot);
        this.objectTG.setTransform(this.t3d);
    } // end of doRotateY()

    public Point3d getCurrLoc()
    // Return t sprite's current location
    {
        this.objectTG.getTransform(this.t3d);
        Vector3d trans = new Vector3d();
        this.t3d.get(trans);
        // printTuple(trans, "currLoc");
        return new Point3d(trans.x, trans.y, trans.z);
    } // end of getCurrLoc()

    public boolean isActive() {
        return this.isActive;
    }

    public void setActive(boolean b)
    // Activity changes affect the sprite's visibility
    {
        this.isActive = b;
        if (!this.isActive) {
            this.visSwitch.setWhichChild(Switch.CHILD_NONE); // make invisible
        } else if (this.isActive) {
            this.visSwitch.setWhichChild(Switch.CHILD_ALL); // make visible
        }
    } // end of setActive()

    protected void printTuple(Tuple3d t, String id)
    // used for debugging, here and in subclasses
    {
        System.out.println(id + " x: " + this.df.format(t.x) + ", " + id + " y: " + this.df.format(t.y) + ", " + id + " z: " + this.df.format(t.z));
    } // end of printTuple()

} // end of Sprite3D class
