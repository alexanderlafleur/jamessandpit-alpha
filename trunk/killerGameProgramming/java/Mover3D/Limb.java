package Mover3D;

// Limb.java
// Thana Konglikhit, October 2003, s4310170@maliwan.psu.ac.th
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* A limb consists of:
 * a unique limb number;

 * its start and end joints names;

 * an axis of orientation, and angle to that axis;

 * optional lathe shape coordinates and texture
 (if they are not present then a limb length must be
 supplied, and the resulting limb will be invisible)

 The scene graph for a limb contains:

 startLimbTG
 |
 |
 V
 orientTG --> xAxisTG --> yAxisTG --> zAxisTG ----> LatheShape3D
 |
 -------> endLimbTG


 The startLimbTG is the location in space linked to the starting
 joint for this limb. This will bave been created when the 'parent'
 limb was built.

 The endLimbTG is calculated by this limb, and stored with
 the ending joint name, ready to be used by any 'children' of this
 limb.

 endLimbTG is a location _near_ the end of the limb, so that any
 child limbs will be placed so they overlap slightly with this
 limb. This enhances the effect that limbs are 'connected',
 especially when a limb moves.

 Meaning of the TGs:
 * orientTG:     rotates the limb to point in a particular direction
 at limb creation time;

 * xAxisTG:      holds any rotations around the x-axis
 carried out when the limb is moved;
 * yAxisTG, zAxisTG:   same idea, but for the y- and z- axes

 The reason for separating out the movement rotations into three
 parts is to simplify the resetting of the limb to its starting
 orientation.

 Rotations cannot be applied to a Limb object.
 The MoveableLimb subclass adds the ability to move the limb,
 and includes a limb name.

 */
import java.util.HashMap;

import javax.media.j3d.Texture;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.image.TextureLoader;

public class Limb {
    private static final double OVERLAP = 0.1;
    // percentage overlap of children with this limb
    // axis constants
    private final static int X_AXIS = 0;
    private final static int Y_AXIS = 1;
    private double limbLen;
    private int limbNo;
    private double orientAngle = 0; // angle to orientation axis
    private int orientAxis; // limb's axis of initial orientation
    private String startJoint, endJoint;
    protected String texPath; // shape's texture filename
    // the following vars are used by subclasses
    private boolean visibleLimb = false;
    protected TransformGroup xAxisTG, yAxisTG, zAxisTG;
    // used in repeated limb rotation calculations
    protected double xsIn[], ysIn[]; // coordinates of lathe shape

    public Limb(int lNo, String jn0, String jn1, int axis, double angle, double len) {
        limbNo = lNo;
        startJoint = jn0;
        endJoint = jn1;
        orientAxis = axis;
        orientAngle = angle;
        limbLen = len;
        visibleLimb = false; // no lathe shape details given
    } // end of Limb()

    public Limb(int lNo, String jn0, String jn1, int axis, double angle, double[] xs, double[] ys, String tex) {
        this(lNo, jn0, jn1, axis, angle, 0); // dummy limb length of 0
        visibleLimb = true; // since lathe shape details are supplied
        xsIn = xs;
        ysIn = ys;
        texPath = tex;
        limbLen = ysIn[ysIn.length - 1]; // really set length
        /*
         * we assume that the final value in the LatheShape3D y-values is the maximum y- value (i.e. its height)
         */
    } // end of Limb()

    public void growLimb(HashMap joints)
    /*
     * Orientate the limb with respect to its starting position, then add the limb.
     */
    {
        TransformGroup startLimbTG = (TransformGroup) joints.get(startJoint);
        // joints contains (jointName, TG) pairs
        if (startLimbTG == null) {
            System.out.println("No transform group info for " + startJoint);
        } else {
            setOrientation(startLimbTG);
            makeLimb(joints);
        }
    } // end of growLimb()

    private void makeLimb(HashMap joints)
    // make the limb's shape, and create and store it's endLimbTG
    {
        if (visibleLimb) {
            makeShape();
        }
        TransformGroup endLimbTG = new TransformGroup();
        Transform3D trans = new Transform3D();
        trans.setTranslation(new Vector3d(0.0, limbLen * (1.0 - OVERLAP), 0.0));
        /*
         * The end position is just short of the actual length of the limb so that any child limbs will be placed so they overlap with this one.
         */
        endLimbTG.setTransform(trans);
        zAxisTG.addChild(endLimbTG);
        joints.put(endJoint, endLimbTG); // store (jointName, TG) pair
    } // end of makeLimb()

    protected void makeShape()
    /*
     * Construct a lathe shape using the supplied lathe coordinates and texture. This method may be overridden to change the LatheShape3D class to a
     * subclass, such as EllipseShape3D.
     */
    {
        LatheShape3D ls;
        if (texPath != null) {
            // System.out.println("Loading textures/" + texPath);
            TextureLoader texLd = new TextureLoader("textures/" + texPath, null);
            Texture tex = texLd.getTexture();
            ls = new LatheShape3D(xsIn, ysIn, tex);
        } else {
            ls = new LatheShape3D(xsIn, ysIn, null);
        }
        zAxisTG.addChild(ls); // add the shape to the limb's graph
    } // end of makeShape()

    public void printLimb() {
        System.out.println(limbNo + " = < (" + startJoint + ", " + endJoint + ") " + orientAngle + " " + limbLen + " " + texPath + " " + visibleLimb
                + ">");
    } // end of printLimb()

    public void reset() {
    }

    // -----------------------------------
    /*
     * updateLimb() and reset() affect the position of the limb, and so are not used in Limb. They will be overridden by subclasses of Limb.
     */
    private void setOrientation(TransformGroup tg)
    /*
     * Set the orientation of this limb. By default, a limb points upwards along the y-axis _in_ its local coordinates system. Its actual orientation
     * in the world depends on the orientation of the limbs in the hierarchy above it. The orientation is set in orientTG, above the rotation TGs
     * xAxisTG, yAxisTG, and zAxisTG.
     */
    {
        TransformGroup orientTG = new TransformGroup();
        if (orientAngle != 0) {
            Transform3D trans = new Transform3D();
            if (orientAxis == X_AXIS) {
                trans.rotX(Math.toRadians(orientAngle));
            } else if (orientAxis == Y_AXIS) {
                trans.rotY(Math.toRadians(orientAngle));
            } else {
                // must be z-axis
                trans.rotZ(Math.toRadians(orientAngle));
            }
            orientTG.setTransform(trans);
        }
        xAxisTG = new TransformGroup();
        xAxisTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        xAxisTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE); // can
        // alter
        yAxisTG = new TransformGroup();
        yAxisTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        yAxisTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE); // can
        // alter
        zAxisTG = new TransformGroup();
        zAxisTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        zAxisTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE); // can
        // alter
        // scene graph's sequence of TG's
        tg.addChild(orientTG);
        orientTG.addChild(xAxisTG);
        xAxisTG.addChild(yAxisTG);
        yAxisTG.addChild(zAxisTG);
    } // end of setOrientation()

    public void updateLimb(int axis, double angleStep) {
    }
} // end of Limb class
