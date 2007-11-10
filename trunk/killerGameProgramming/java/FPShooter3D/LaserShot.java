package FPShooter3D;

// LaserShot.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/*  Creates the scene subgraph:

 beamTG --> beamSW --> beamDir TG --> beam cylinder
 |
 --> explShape (explosion anim)
 
 The beamTG is for moving the beam and explosion. The Switch is
 for visibility (nothing, beam, or explosion), and the beamDir TG
 is for tweaking the position of the cylinder and rotating it.

 The KeyBehavior object calls fireBeam() in AmmoManager, which
 then calls requestFiring() in each of its LaserShot objects. 

 If the LaserShot is not in use (inUse == false) then LaserShot starts
 an AnimBeam thread which calls LaserShot's moveBeam(). inUse is set
 to true.

 moveBeam() incrementally moves the beam forward, starting from the 
 current viewer position (steerTG). If the beam gets close
 to the target then the explosion is shown, otherwise the beam disappears
 after reaching a ceratin MAX_RANGE from the gun. inUse is set to false 
 again.
 */

import javax.media.j3d.Appearance;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.Switch;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.geometry.Cylinder;

public class LaserShot {
    private static final double MAX_RANGE = 6.0; // max distance travelled

    private static final double STEP = 1.0; // distance travelled each increment

    private static final Vector3d INCR_VEC = new Vector3d(0, 0, -STEP);

    private static final long SLEEP_TIME = 100; // delay between moves of beam

    private static final double HIT_RANGE = 1.0; // how close to count as a
    // hit

    private TransformGroup steerTG; // the TG for the user's viewpoint

    private TransformGroup beamTG; // moving the beam/explosion

    private Switch beamSW; // for beam/explosion visibility

    private Cylinder beam; // the beam is a cylinder

    private ImageCsSeries explShape; // holds the explosion

    private boolean inUse; // whether beam is in flight or not

    private Vector3d targetVec; // location of target

    // for repeated calcs
    private Transform3D tempT3d = new Transform3D();

    private Transform3D toMove = new Transform3D();

    private Transform3D localT3d = new Transform3D();

    private Vector3d currVec = new Vector3d();

    public LaserShot(TransformGroup steerTG, ImageComponent2D[] exploIms, Vector3d tarVec) {
        this.steerTG = steerTG;
        makeBeam(exploIms);
        this.targetVec = tarVec;
        this.inUse = false;
    } // end of LaserShot()

    private void makeBeam(ImageComponent2D[] exploIms)
    // Create the beam and explosion subgraph detailed above.
    {
        // beam position
        Transform3D beanT3d = new Transform3D();
        beanT3d.rotX(Math.PI / 2); // 90 degrees, so pointing into scene
        beanT3d.setTranslation(new Vector3d(0, -0.3, -0.25)); // down and in
        TransformGroup beanDir = new TransformGroup();
        beanDir.setTransform(beanT3d);

        this.beam = new Cylinder(0.05f, 0.5f, makeRedApp()); // thin red cylinder
        this.beam.setCapability(Node.ALLOW_LOCAL_TO_VWORLD_READ); // for testing
        // posn later

        beanDir.addChild(this.beam);

        // create switch for visibility
        this.beamSW = new Switch();
        this.beamSW.setCapability(Switch.ALLOW_SWITCH_WRITE);

        this.beamSW.addChild(beanDir); // add beam to the switch
        this.beamSW.setWhichChild(Switch.CHILD_NONE); // invisible initially

        // add the explosion to the switch, centered at (0,0,0), size 2.0f
        this.explShape = new ImageCsSeries(new Point3f(), 2.0f, exploIms);
        this.beamSW.addChild(this.explShape);
        this.beamSW.setWhichChild(Switch.CHILD_NONE); // invisible initially

        // top-level beam TG
        this.beamTG = new TransformGroup();
        this.beamTG.addChild(this.beamSW);
        this.beamTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        this.beamTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
    } // end of makeBeam()

    private Appearance makeRedApp() {
        Color3f medRed = new Color3f(0.8f, 0.4f, 0.3f);
        Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
        Color3f specular = new Color3f(0.9f, 0.9f, 0.9f);

        Material redMat = new Material(medRed, black, medRed, specular, 80.0f);
        redMat.setLightingEnable(true);

        Appearance redApp = new Appearance();
        redApp.setMaterial(redMat);
        return redApp;
    } // end of makeRedApp()

    public TransformGroup getTG()
    // accessed by AmmoManager
    {
        return this.beamTG;
    }

    // ------------------------ shot methods ----------------

    public boolean requestFiring()
    // If the beam is not being used then fire it
    {
        if (this.inUse) {
            return false;
        } else {
            this.inUse = true;
            new AnimBeam(this).start(); // calls moveBeam() inside a thread
            return true;
        }
    } // end of fireBeam()

    public void moveBeam()
    /*
     * Move the beam with a delay between each move. If the beam gets close to the target then show the explosion otherwise the beam disappears when it reaches MAX_RANGE
     */
    { // place the beam at the current gun hand position
        this.steerTG.getTransform(this.tempT3d);
        // System.out.println("steerTG Transform3D: " + tempT3d);
        this.beamTG.setTransform(this.tempT3d);
        showBeam(true);

        double currDist = 0.0;
        boolean hitTarget = closeToTarget();
        while ((currDist < MAX_RANGE) && (!hitTarget)) {
            doMove(INCR_VEC);
            hitTarget = closeToTarget();
            currDist += STEP;
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (Exception ex) {
            }
        }

        showBeam(false); // make beam invisible
        if (hitTarget) {
            showExplosion(); // if a hit, show explosion
        }
        this.inUse = false; // shot is finished
    } // end of moveBeam()

    private void doMove(Vector3d mv) {
        this.beamTG.getTransform(this.tempT3d);
        this.toMove.setTranslation(mv);
        // System.out.println("Moving: " + mv);
        this.tempT3d.mul(this.toMove);
        this.beamTG.setTransform(this.tempT3d);
    } // end of doMove()

    private boolean closeToTarget()
    /*
     * The beam is close if its current position (currVec) is a short distance from the target position (targetVec).
     */
    {
        this.beam.getLocalToVworld(this.localT3d); // get beam's TG in world coords
        this.localT3d.get(this.currVec); // get (x,y,z) component
        // System.out.println("currVec: " + currVec);

        this.currVec.sub(this.targetVec); // distance between two positions
        double sqLen = this.currVec.lengthSquared();
        if (sqLen < HIT_RANGE * HIT_RANGE) {
            return true;
        }
        return false;
    } // end of closeToTarget()

    private void showBeam(boolean toVisible)
    // make beam visible/invisible
    {
        if (toVisible) {
            this.beamSW.setWhichChild(0); // make visible
        } else {
            this.beamSW.setWhichChild(Switch.CHILD_NONE); // make invisible
        }
    } // end of showBeam()

    private void showExplosion()
    // start the explosion
    {
        this.beamSW.setWhichChild(1); // make visible
        this.explShape.showSeries(); // boom!
        this.beamSW.setWhichChild(Switch.CHILD_NONE); // make invisible
    } // end of showExplosion()

} // end of LaserShot class
