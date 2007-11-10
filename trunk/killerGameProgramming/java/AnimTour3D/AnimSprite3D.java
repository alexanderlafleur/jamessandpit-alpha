package AnimTour3D;

// AnimSprite3D.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* AnimSprite3D loads a predetermined set of 3ds models
 listed in the poses[] array using PropManager. The models
 all represent the same sprite, but in different poses.

 The poses are all variants of the "Stick Child" figure from 
 the 3D application MetaCreations Poses 3.

 They are placed in a switch below the objectTG TransformGroup.

 The sprite is moved by applying transforms to its objectTG
 node. The transforms are cause the switch to display different
 poses. Movements are restricted to the XZ plane and rotations
 around the Y-axis.

 AnimSprite3D is closely related to Sprite3D in /Tour3D.
 Main changes:
 * no obstacles, but the sprite checks that it is within
 the floor area.

 */

import java.text.DecimalFormat;
import java.util.BitSet;

import javax.media.j3d.Switch;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

public class AnimSprite3D {
    private final static double FLOOR_LEN = 20.0;

    // 3ds images used to animate the sprite commands
    // located in /models/xxx.3ds
    private final static String poses[] = { "stand", "walk1", "walk2", "rev1", "rev2", "rotClock", "rotCC", "mleft", "mright", "punch1", "punch2" };

    private final static int STAND_NUM = 0; // index of "stand" Pose

    private TransformGroup objectTG; // TG which the loaded object hangs off

    private Transform3D t3d, toMove, toRot; // for manipulating objectTG's

    // transform

    private Switch imSwitch; // to make different Poses visible

    private BitSet visIms; // used for switching

    private int currPoseNo, maxPoses; // Pose number info

    private boolean isActive; // is the sprite active?

    private DecimalFormat df; // for simpler output during debugging

    public AnimSprite3D() {
        this.df = new DecimalFormat("0.###"); // 3 dp

        loadPoses();

        // create a new transform group for the object
        this.objectTG = new TransformGroup();
        this.objectTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        this.objectTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        this.objectTG.addChild(this.imSwitch);

        this.t3d = new Transform3D();
        this.toMove = new Transform3D();
        this.toRot = new Transform3D();
        this.isActive = true;
    } // end of AnimSprite3D()

    private void loadPoses()
    // load the images referred to in the poses[]
    {
        PropManager propMan;

        this.imSwitch = new Switch(Switch.CHILD_MASK);
        this.imSwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);

        this.maxPoses = poses.length;
        for (int i = 0; i < this.maxPoses; i++) {
            propMan = new PropManager(poses[i] + ".3ds", true);
            this.imSwitch.addChild(propMan.getTG()); // add object to switch
        }

        this.visIms = new BitSet(this.maxPoses); // initialise bitset used for switching
        this.currPoseNo = STAND_NUM; // sprite standing still
        setPoseNum(this.currPoseNo);
    } // end of loadPoses()

    public TransformGroup getTG()
    // make top-level TG of sprite available
    {
        return this.objectTG;
    }

    public void setPosition(double xPos, double zPos)
    // put sprite at coordinate (xPos, zPos)
    {
        Point3d currLoc = getCurrLoc();
        double xMove = xPos - currLoc.x;
        double zMove = zPos - currLoc.z;
        moveBy(xMove, zMove);
    } // end of setPosition()

    public boolean moveBy(double x, double z)
    // move the sprite by an (x,z) offset
    {
        if (isActive()) {
            Point3d nextLoc = tryMove(new Vector3d(x, 0, z));
            if (beyondEdge(nextLoc.x) || beyondEdge(nextLoc.z)) {
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

    private boolean beyondEdge(double pos)
    // is pos off the floor?
    {
        if ((pos < -FLOOR_LEN / 2) || (pos > FLOOR_LEN / 2)) {
            return true;
        }
        return false;
    } // end of beyondEdge()

    private void doMove(Vector3d theMove) {
        this.objectTG.getTransform(this.t3d);
        this.toMove.setTranslation(theMove); // overwrite previous trans
        this.t3d.mul(this.toMove);
        this.objectTG.setTransform(this.t3d);
    } // end of doMove()

    private Point3d tryMove(Vector3d theMove)
    /*
     * Calculate the effect of the given translation but do not update the object's position until it's been tested.
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

    public void doRotateY(double radians) {
        if (isActive()) {
            this.objectTG.getTransform(this.t3d);
            this.toRot.rotY(radians);
            this.t3d.mul(this.toRot);
            this.objectTG.setTransform(this.t3d);
        }
    } // end of doRotateY()

    public Point3d getCurrLoc() {
        this.objectTG.getTransform(this.t3d);
        Vector3d trans = new Vector3d();
        this.t3d.get(trans);
        // printTuple(trans, "currLoc");
        return new Point3d(trans.x, trans.y, trans.z);
    } // end of getCurrLoc()

    // --------------- pose switching operations -----------------------

    public boolean setPose(String name)
    // represent the sprite by the 'name' pose
    {
        if (isActive()) {
            int idx = getPoseIndex(name);
            if ((idx < 0) || (idx > this.maxPoses - 1)) {
                return false;
            }
            setPoseNum(idx);
            return true;
        } else {
            return false;
        }
    } // end of setPose()

    private int getPoseIndex(String name)
    // find the position of name in poses[]
    {
        for (int i = 0; i < this.maxPoses; i++) {
            if (name.equals(poses[i])) {
                return i;
            }
        }
        return -1;
    } // end of getPoseIndex()

    private void setPoseNum(int idx)
    // change the Pose to the one with index idx
    {
        this.visIms.clear();
        this.visIms.set(idx); // switch on new pose
        this.imSwitch.setChildMask(this.visIms);
        this.currPoseNo = idx;
    } // end of setPoseNum()

    public boolean isActive() {
        return this.isActive;
    }

    public void setActive(boolean b) {
        this.isActive = b;
        if (!this.isActive) {
            this.visIms.clear();
            this.imSwitch.setChildMask(this.visIms); // display nothing
        } else if (this.isActive) {
            setPoseNum(this.currPoseNo); // make visible
        }
    } // end of setActive()

    // ----------------------- debugging methods ------------------

    protected void printTuple(Tuple3d t, String id)
    // used for debugging
    {
        System.out.println(id + " x: " + this.df.format(t.x) + ", " + id + " y: " + this.df.format(t.y) + ", " + id + " z: " + this.df.format(t.z));
    } // end of printTuple()

} // end of AnimSprite3D class
