package Shooter3D;

// LaserBeam.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* A LaserBeam object is a red cylinder which is rotated to
 point at the loaction picked by the user on the checkboard.
 FireBeam calls shootBeam() to move the beam step-by-step towards 
 the location, to simulate a shooting beam. The beam also has
 a sound which moves with it.

 BG-->beam TG-->red Cylinder
 |
 -->beamPS PointSound
 */

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.PointSound;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.geometry.Cylinder;

public class LaserBeam {
    private static final Vector3d ORIGIN = new Vector3d(0, 0, 0);

    private static final double STEP_SIZE = 1.0; // distance travelled each
    // increment

    private static final long SLEEP_TIME = 100; // ms time between moves of beam

    private BranchGroup beamBG;

    private TransformGroup beamTG;

    private PointSound beamPS;

    private Vector3d startVec, currVec, stepVec;

    private Point3d startPt;

    // for repeated calculations
    private Transform3D beamT3d = new Transform3D();

    private Vector3d currTrans = new Vector3d();

    private Transform3D rotT3d = new Transform3D();

    public LaserBeam(Vector3d start, PointSound snd) {
        this.startVec = start;
        this.startPt = new Point3d(start.x, start.y, start.z);
        this.beamPS = snd;

        this.currVec = new Vector3d(); // for movement calculations
        this.stepVec = new Vector3d();

        makeBeam();
    } // end of LaserBeam()

    private void makeBeam()
    // build the scene graph branch for the laser beam
    {
        // create transform group for the laser beam cylinder
        this.beamTG = new TransformGroup();
        this.beamTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        this.beamTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        // position laser beam
        this.beamT3d.set(this.startVec); // so inside cone
        this.beamTG.setTransform(this.beamT3d);

        // initialise red appearance
        Appearance apRed = new Appearance();
        ColoringAttributes redCA = new ColoringAttributes();
        Color3f medRed = new Color3f(0.8f, 0.4f, 0.3f);
        redCA.setColor(medRed);
        apRed.setColoringAttributes(redCA);

        Cylinder beam = new Cylinder(0.1f, 0.5f, apRed); // thin red cylinder
        beam.setPickable(false);

        // add beam and sound to TransformGroup
        this.beamTG.addChild(beam);
        this.beamTG.addChild(this.beamPS);

        // branchGroup holding the TG
        this.beamBG = new BranchGroup();
        this.beamBG.addChild(this.beamTG);
    } // end of makeBeam()

    public BranchGroup getBeamBG() {
        return this.beamBG;
    }

    public void shootBeam(Point3d intercept)
    /*
     * Move the beam with a delay between each move, until the beam reaches its intercept. Set off the explosion. Called by FireBeam.
     */
    {
        double travelDist = this.startPt.distance(intercept);
        calcStepVec(intercept, travelDist);

        this.beamPS.setEnable(true); // switch on laser beam sound

        double currDist = 0.0;
        this.currVec.set(this.startVec);
        this.beamTG.getTransform(this.beamT3d); // get current beam transform

        while (currDist <= travelDist) { // not at destination yet
            this.beamT3d.setTranslation(this.currVec); // move the laser beam
            this.beamTG.setTransform(this.beamT3d);
            this.currVec.add(this.stepVec);
            currDist += STEP_SIZE;
            try {
                Thread.sleep(SLEEP_TIME); // wait a while
            } catch (Exception ex) {
            }
        }

        // reset beam to its orignal coordinates
        this.beamT3d.setTranslation(this.startVec);
        this.beamTG.setTransform(this.beamT3d);

        this.beamPS.setEnable(false); // switch off laser beam sound
    } // end of shootBeam()

    private void calcStepVec(Point3d intercept, double travelDist)
    /*
     * Calculate the step vector, stepVec, to move the cylinder in steps from the start position to the intercept.
     */
    {
        double moveFrac = STEP_SIZE / travelDist;
        double incrX = (intercept.x - this.startPt.x) * moveFrac;
        double incrY = (intercept.y - this.startPt.y) * moveFrac;
        double incrZ = (intercept.z - this.startPt.z) * moveFrac;
        this.stepVec.set(incrX, incrY, incrZ); // store as a vector
    } // end of calcStepVec()

    public void makeRotation(AxisAngle4d rotAxis)
    // Rotate the laser beam. Called by ShootingBehaviour.
    {
        this.beamTG.getTransform(this.beamT3d); // get current transform
        // System.out.println("Start beamT3d: " + beamT3d);
        this.beamT3d.get(this.currTrans); // get current translation
        this.beamT3d.setTranslation(ORIGIN); // translate to origin

        this.rotT3d.setRotation(rotAxis); // apply rotation
        this.beamT3d.mul(this.rotT3d);

        this.beamT3d.setTranslation(this.currTrans); // translate back
        this.beamTG.setTransform(this.beamT3d);
        // System.out.println("End beamT3d: " + beamT3d);
    } // end of makeRotation()

} // end of LaserBeam class
