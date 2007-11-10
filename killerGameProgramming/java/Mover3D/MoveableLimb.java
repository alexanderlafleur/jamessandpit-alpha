package Mover3D;

// MoveableLimb.java
// Thana Konglikhit, October 2003, s4310170@maliwan.psu.ac.th
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* MoveableLimb allows a limb to be moved around the x-, y-, and z-axes.
 This is done by affecting the xAxisTG, yAxisTG, and zAxisTG TGs
 in the limb's graph:

 startLimbTG
 |
 |
 V
 orientTG --> xAxisTG --> yAxisTG --> zAxisTG ----> LatheShape3D
 |
 -------> endLimbTG

 MoveableLimb maintains range information for the three axes, and
 ignores rotations which would move the limb outside of those
 ranges. If a range is not specified, then it is assumed
 to be 0 (i.e. rotation is not possible around that axis).


 The rotations are processed by updateLimb() which is called
 with axis and angle arguments.

 The limb can be reset to its initial orientation, via a call 
 to reset().

 MoveableLimb also includes a limb name, which is only used
 here to make error messages more intelligable.
 */

import javax.media.j3d.Transform3D;

public class MoveableLimb extends Limb {
    // axis constants
    private final static int X_AXIS = 0;

    private final static int Y_AXIS = 1;

    private final static int Z_AXIS = 2;

    private String limbName;

    private double xMin, xMax, yMin, yMax, zMin, zMax; // axis ranges

    private double xCurrAng, yCurrAng, zCurrAng; // current angle in 3 axes

    private Transform3D currTrans, rotTrans; // used for repeated calcs

    public MoveableLimb(String lName, int lNo, String jn0, String jn1, int axis, double angle, double[] xs, double[] ys, String tex) {
        super(lNo, jn0, jn1, axis, angle, xs, ys, tex);

        this.rotTrans = new Transform3D();
        this.currTrans = new Transform3D();

        this.limbName = lName;
        this.xMin = 0;
        this.xMax = 0; // these default ranges prevent any rotations
        this.yMin = 0;
        this.yMax = 0;
        this.zMin = 0;
        this.zMax = 0;
        this.xCurrAng = 0;
        this.yCurrAng = 0;
        this.zCurrAng = 0;
    } // end of MoveableLimb()

    public void setRanges(double x1, double x2, double y1, double y2, double z1, double z2)
    // set all the axis ranges
    {
        setRange(X_AXIS, x1, x2);
        setRange(Y_AXIS, y1, y2);
        setRange(Z_AXIS, z1, z2);
    } // end of setRanges()

    public void setRange(int axis, double angle1, double angle2)
    // set the range for axis only
    {
        if (angle1 > angle2) {
            System.out.println(this.limbName + ": range in wrong order... swapping");
            double temp = angle1;
            angle1 = angle2;
            angle2 = temp;
        }
        if (axis == X_AXIS) {
            this.xMin = angle1;
            this.xMax = angle2;
        } else if (axis == Y_AXIS) {
            this.yMin = angle1;
            this.yMax = angle2;
        } else { // Z_AXIS
            this.zMin = angle1;
            this.zMax = angle2;
        }
    } // end of setRange()

    @Override
    public void printLimb() {
        super.printLimb();
        System.out.println("     < " + this.limbName + " (" + this.xMin + ", " + this.xMax + ") (" + this.yMin + ", " + this.yMax + ") (" + this.zMin + ", " + this.zMax + ")>");
    } // end of printLimb()

    @Override
    public void updateLimb(int axis, double angleStep)
    // Attempt to rotate this limb by angleStep around axis
    {
        if (axis == X_AXIS) {
            applyAngleStep(angleStep, this.xCurrAng, axis, this.xMax, this.xMin);
        } else if (axis == Y_AXIS) {
            applyAngleStep(angleStep, this.yCurrAng, axis, this.yMax, this.yMin);
        } else {
            // Z_AXIS
            applyAngleStep(angleStep, this.zCurrAng, axis, this.zMax, this.zMin);
        }
    } // end of updateLimb()

    private void applyAngleStep(double angleStep, double currAngle, int axis, double max, double min)
    /*
     * Before any rotation, check that the angle step moves the limb within the ranges for this axis. If not then rotate to the range limit, and no further.
     */
    {
        if ((currAngle >= max) && (angleStep > 0)) { // will exceed max
            System.out.println(this.limbName + ": no rot; already at max");
            return;
        }

        if (currAngle <= min && (angleStep < 0)) { // will drop below min
            System.out.println(this.limbName + ": no rot; already at min");
            return;
        }

        double newAngle = currAngle + angleStep;
        if (newAngle > max) {
            System.out.println(this.limbName + ": reached max angle");
            angleStep = max - currAngle; // rotate to max angle only
        } else if (newAngle < min) {
            System.out.println(this.limbName + ": reached min angle");
            angleStep = min - currAngle; // rotate to min angle only
        }
        // System.out.println("angleStep: " + angleStep);

        makeUpdate(axis, angleStep); // do the rotation
    } // end of applyAngleStep()

    private void makeUpdate(int axis, double angleStep)
    // rotate the limb by angleStep around the given axis
    {
        if (axis == X_AXIS) {
            this.rotTrans.rotX(Math.toRadians(angleStep));
            this.xAxisTG.getTransform(this.currTrans);
            this.currTrans.mul(this.rotTrans);
            this.xAxisTG.setTransform(this.currTrans);
            this.xCurrAng += angleStep;
        } else if (axis == Y_AXIS) {
            this.rotTrans.rotY(Math.toRadians(angleStep));
            this.yAxisTG.getTransform(this.currTrans);
            this.currTrans.mul(this.rotTrans);
            this.yAxisTG.setTransform(this.currTrans);
            this.yCurrAng += angleStep;
        } else { // z-axis
            this.rotTrans.rotZ(Math.toRadians(angleStep));
            this.zAxisTG.getTransform(this.currTrans);
            this.currTrans.mul(this.rotTrans);
            this.zAxisTG.setTransform(this.currTrans);
            this.zCurrAng += angleStep;
        }
    } // end of makeUpdate()

    @Override
    public void reset()
    // reset the limb to its initial orientation
    {
        this.rotTrans.rotX(Math.toRadians(-this.xCurrAng)); // reset x angle
        this.xAxisTG.getTransform(this.currTrans);
        this.currTrans.mul(this.rotTrans);
        this.xAxisTG.setTransform(this.currTrans);
        this.xCurrAng = 0;

        this.rotTrans.rotY(Math.toRadians(-this.yCurrAng)); // reset y angle
        this.yAxisTG.getTransform(this.currTrans);
        this.currTrans.mul(this.rotTrans);
        this.yAxisTG.setTransform(this.currTrans);
        this.yCurrAng = 0;

        this.rotTrans.rotZ(Math.toRadians(-this.zCurrAng)); // reset z angle
        this.zAxisTG.getTransform(this.currTrans);
        this.currTrans.mul(this.rotTrans);
        this.zAxisTG.setTransform(this.currTrans);
        this.zCurrAng = 0;
    } // end of reset()

} // end of MoveableLimb class
