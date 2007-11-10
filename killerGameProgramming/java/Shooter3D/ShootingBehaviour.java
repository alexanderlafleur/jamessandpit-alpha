package Shooter3D;

// ShootingBehaviour.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* The gun cone and laser beam are rotated to point at the placed
 clicked on the checkboard. Then a FireBeam thread is created
 to move ('fire') the beam at the location, and display an explosion.
 */

import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.picking.PickIntersection;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.PickTool;
import com.sun.j3d.utils.picking.behaviors.PickMouseBehavior;

public class ShootingBehaviour extends PickMouseBehavior {
    // initial orientation of firing cone: straight up
    private static final Vector3d UPVEC = new Vector3d(0.0, 1.0, 0.0);

    private Point3d startPt;

    private boolean firstRotation, finishedShot;

    private double shootAngle;

    private ExplosionsClip explsClip;

    private LaserBeam laser;

    private GunTurret gun;

    // for repeated calculations
    private AxisAngle4d rotAxisAngle = new AxisAngle4d();

    private Vector3d clickVec = new Vector3d();

    private Vector3d axisVec = new Vector3d();

    public ShootingBehaviour(Canvas3D canvas, BranchGroup root, Bounds bounds, Point3d sp, ExplosionsClip ec, LaserBeam lb, GunTurret g) {
        super(canvas, root, bounds);
        setSchedulingBounds(bounds);

        this.pickCanvas.setMode(PickTool.GEOMETRY_INTERSECT_INFO);
        // allows PickIntersection objects to be returned

        this.startPt = sp; // location of the gun cone
        this.explsClip = ec;
        this.laser = lb;
        this.gun = g;

        this.firstRotation = true; // to signal the undoing of previous rotations
        this.finishedShot = true; // when true, we can fire another beam
    } // end of ShootingBehaviour()

    @Override
    public void updateScene(int xpos, int ypos)
    /*
     * This method is called when something is picked. Only the floor is pickable, so obtain the intersect pt, rotate the gun cone and laser beam to point at it, then fire the
     * beam.
     */
    {
        if (this.finishedShot) { // previous shot has finished
            this.pickCanvas.setShapeLocation(xpos, ypos);

            Point3d eyePos = this.pickCanvas.getStartPosition(); // the viewer
            // location

            PickResult pickResult = null;
            pickResult = this.pickCanvas.pickClosest();

            if (pickResult != null) {
                // pickResultInfo(pickResult);

                PickIntersection pi = pickResult.getClosestIntersection(this.startPt);
                // get intercesection closest to the gun cone
                Point3d intercept = pi.getPointCoordinatesVW();

                rotateToPoint(intercept); // rotate the cone and beam
                double turnAngle = calcTurn(eyePos, intercept);

                this.finishedShot = false;
                new FireBeam(intercept, this, this.laser, this.explsClip, turnAngle).start();
                // fire the beam and show the explosion
            }
        }
    } // end of updateScene()

    private void rotateToPoint(Point3d intercept)
    /*
     * Turn the gun and beam cylinder to point at the point where the mouse was clicked.
     */
    {
        if (!this.firstRotation) { // undo previous rotations to gun and beam
            this.axisVec.negate();
            this.rotAxisAngle.set(this.axisVec, this.shootAngle);
            this.gun.makeRotation(this.rotAxisAngle);
            this.laser.makeRotation(this.rotAxisAngle);
        }

        this.clickVec.set(intercept.x - this.startPt.x, intercept.y - this.startPt.y, intercept.z - this.startPt.z);
        this.clickVec.normalize();
        this.axisVec.cross(UPVEC, this.clickVec);
        // System.out.println("axisVec: " + axisVec);

        this.shootAngle = UPVEC.angle(this.clickVec);
        // shootAngle = Math.acos( UPVEC.dot(clickVec));
        // System.out.println("shootAngle: " + shootAngle);

        this.rotAxisAngle.set(this.axisVec, this.shootAngle); // build rotation
        // System.out.println("rotAxisAngle: " + rotAxisAngle);

        this.gun.makeRotation(this.rotAxisAngle);
        this.laser.makeRotation(this.rotAxisAngle);

        this.firstRotation = false;
    } // end of rotateToPoint()

    private double calcTurn(Point3d eyePos, Point3d intercept)
    /*
     * Calculate the angle to turn the explosion image around the y-axis, so that it is facing towards the eye position. The image is not rotated around the x- or z- axes.
     */
    {
        double zDiff = eyePos.z - intercept.z;
        double xDiff = eyePos.x - intercept.x;
        // System.out.println("zDiff: " + zDiff + "; xDiff: " + xDiff);

        double turnAngle = Math.atan2(xDiff, zDiff);
        // System.out.println("turnAngle: " + turnAngle);
        return turnAngle;
    } // end of calcAngle()

    public void setFinishedShot()
    /*
     * Called by the FireBeam thread when it has moved the laser beam to its destination. The beam has been reset and this method is called to allow a new firing.
     */
    {
        this.finishedShot = true;
    }

} // end of ShootingBehaviour class
