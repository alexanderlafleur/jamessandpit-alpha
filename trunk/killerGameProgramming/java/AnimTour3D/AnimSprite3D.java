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
    private int currPoseNo, maxPoses; // Pose number info
    private DecimalFormat df; // for simpler output during debugging
    // transform
    private Switch imSwitch; // to make different Poses visible
    private boolean isActive; // is the sprite active?
    private TransformGroup objectTG; // TG which the loaded object hangs off
    private Transform3D t3d, toMove, toRot; // for manipulating objectTG's
    private BitSet visIms; // used for switching

    public AnimSprite3D() {
        df = new DecimalFormat("0.###"); // 3 dp
        loadPoses();
        // create a new transform group for the object
        objectTG = new TransformGroup();
        objectTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        objectTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objectTG.addChild(imSwitch);
        t3d = new Transform3D();
        toMove = new Transform3D();
        toRot = new Transform3D();
        isActive = true;
    } // end of AnimSprite3D()

    private boolean beyondEdge(double pos)
    // is pos off the floor?
    {
        if (pos < -FLOOR_LEN / 2 || pos > FLOOR_LEN / 2) {
            return true;
        }
        return false;
    } // end of beyondEdge()

    private void doMove(Vector3d theMove) {
        objectTG.getTransform(t3d);
        toMove.setTranslation(theMove); // overwrite previous trans
        t3d.mul(toMove);
        objectTG.setTransform(t3d);
    } // end of doMove()

    public void doRotateY(double radians) {
        if (isActive()) {
            objectTG.getTransform(t3d);
            toRot.rotY(radians);
            t3d.mul(toRot);
            objectTG.setTransform(t3d);
        }
    } // end of doRotateY()

    public Point3d getCurrLoc() {
        objectTG.getTransform(t3d);
        Vector3d trans = new Vector3d();
        t3d.get(trans);
        // printTuple(trans, "currLoc");
        return new Point3d(trans.x, trans.y, trans.z);
    } // end of getCurrLoc()

    private int getPoseIndex(String name)
    // find the position of name in poses[]
    {
        for (int i = 0; i < maxPoses; i++) {
            if (name.equals(poses[i])) {
                return i;
            }
        }
        return -1;
    } // end of getPoseIndex()

    public TransformGroup getTG()
    // make top-level TG of sprite available
    {
        return objectTG;
    }

    public boolean isActive() {
        return isActive;
    }

    private void loadPoses()
    // load the images referred to in the poses[]
    {
        PropManager propMan;
        imSwitch = new Switch(Switch.CHILD_MASK);
        imSwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);
        maxPoses = poses.length;
        for (int i = 0; i < maxPoses; i++) {
            propMan = new PropManager(poses[i] + ".3ds", true);
            imSwitch.addChild(propMan.getTG()); // add object to switch
        }
        visIms = new BitSet(maxPoses); // initialise bitset used for switching
        currPoseNo = STAND_NUM; // sprite standing still
        setPoseNum(currPoseNo);
    } // end of loadPoses()

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

    // --------------- pose switching operations -----------------------
    protected void printTuple(Tuple3d t, String id)
    // used for debugging
    {
        System.out.println(id + " x: " + df.format(t.x) + ", " + id + " y: " + df.format(t.y) + ", " + id + " z: " + df.format(t.z));
    } // end of printTuple()

    public void setActive(boolean b) {
        isActive = b;
        if (!isActive) {
            visIms.clear();
            imSwitch.setChildMask(visIms); // display nothing
        } else if (isActive) {
            setPoseNum(currPoseNo); // make visible
        }
    } // end of setActive()

    public boolean setPose(String name)
    // represent the sprite by the 'name' pose
    {
        if (isActive()) {
            int idx = getPoseIndex(name);
            if (idx < 0 || idx > maxPoses - 1) {
                return false;
            }
            setPoseNum(idx);
            return true;
        } else {
            return false;
        }
    } // end of setPose()

    private void setPoseNum(int idx)
    // change the Pose to the one with index idx
    {
        visIms.clear();
        visIms.set(idx); // switch on new pose
        imSwitch.setChildMask(visIms);
        currPoseNo = idx;
    } // end of setPoseNum()

    public void setPosition(double xPos, double zPos)
    // put sprite at coordinate (xPos, zPos)
    {
        Point3d currLoc = getCurrLoc();
        double xMove = xPos - currLoc.x;
        double zMove = zPos - currLoc.z;
        moveBy(xMove, zMove);
    } // end of setPosition()

    // ----------------------- debugging methods ------------------
    private Point3d tryMove(Vector3d theMove)
    /*
     * Calculate the effect of the given translation but do not update the object's position until it's been tested.
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
} // end of AnimSprite3D class
