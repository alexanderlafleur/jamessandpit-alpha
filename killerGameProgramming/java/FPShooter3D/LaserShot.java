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
    private static final double HIT_RANGE = 1.0; // how close to count as a
    // hit
    private static final Vector3d INCR_VEC = new Vector3d(0, 0, -STEP);
    private static final double MAX_RANGE = 6.0; // max distance travelled
    private static final long SLEEP_TIME = 100; // delay between moves of beam
    private static final double STEP = 1.0; // distance travelled each increment
    private Cylinder beam; // the beam is a cylinder
    private Switch beamSW; // for beam/explosion visibility
    private TransformGroup beamTG; // moving the beam/explosion
    private Vector3d currVec = new Vector3d();
    private ImageCsSeries explShape; // holds the explosion
    private boolean inUse; // whether beam is in flight or not
    private Transform3D localT3d = new Transform3D();
    private TransformGroup steerTG; // the TG for the user's viewpoint
    private Vector3d targetVec; // location of target
    // for repeated calcs
    private Transform3D tempT3d = new Transform3D();
    private Transform3D toMove = new Transform3D();

    public LaserShot(TransformGroup steerTG, ImageComponent2D[] exploIms, Vector3d tarVec) {
        this.steerTG = steerTG;
        makeBeam(exploIms);
        targetVec = tarVec;
        inUse = false;
    } // end of LaserShot()

    private boolean closeToTarget()
    /*
     * The beam is close if its current position (currVec) is a short distance from the target position (targetVec).
     */
    {
        beam.getLocalToVworld(localT3d); // get beam's TG in world coords
        localT3d.get(currVec); // get (x,y,z) component
        // System.out.println("currVec: " + currVec);
        currVec.sub(targetVec); // distance between two positions
        double sqLen = currVec.lengthSquared();
        if (sqLen < HIT_RANGE * HIT_RANGE) {
            return true;
        }
        return false;
    } // end of closeToTarget()

    private void doMove(Vector3d mv) {
        beamTG.getTransform(tempT3d);
        toMove.setTranslation(mv);
        // System.out.println("Moving: " + mv);
        tempT3d.mul(toMove);
        beamTG.setTransform(tempT3d);
    } // end of doMove()

    public TransformGroup getTG()
    // accessed by AmmoManager
    {
        return beamTG;
    }

    // ------------------------ shot methods ----------------
    private void makeBeam(ImageComponent2D[] exploIms)
    // Create the beam and explosion subgraph detailed above.
    {
        // beam position
        Transform3D beanT3d = new Transform3D();
        beanT3d.rotX(Math.PI / 2); // 90 degrees, so pointing into scene
        beanT3d.setTranslation(new Vector3d(0, -0.3, -0.25)); // down and in
        TransformGroup beanDir = new TransformGroup();
        beanDir.setTransform(beanT3d);
        beam = new Cylinder(0.05f, 0.5f, makeRedApp()); // thin red cylinder
        beam.setCapability(Node.ALLOW_LOCAL_TO_VWORLD_READ); // for testing
        // posn later
        beanDir.addChild(beam);
        // create switch for visibility
        beamSW = new Switch();
        beamSW.setCapability(Switch.ALLOW_SWITCH_WRITE);
        beamSW.addChild(beanDir); // add beam to the switch
        beamSW.setWhichChild(Switch.CHILD_NONE); // invisible initially
        // add the explosion to the switch, centered at (0,0,0), size 2.0f
        explShape = new ImageCsSeries(new Point3f(), 2.0f, exploIms);
        beamSW.addChild(explShape);
        beamSW.setWhichChild(Switch.CHILD_NONE); // invisible initially
        // top-level beam TG
        beamTG = new TransformGroup();
        beamTG.addChild(beamSW);
        beamTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        beamTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
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

    public void moveBeam()
    /*
     * Move the beam with a delay between each move. If the beam gets close to the target then show the explosion otherwise the beam disappears when
     * it reaches MAX_RANGE
     */
    { // place the beam at the current gun hand position
        steerTG.getTransform(tempT3d);
        // System.out.println("steerTG Transform3D: " + tempT3d);
        beamTG.setTransform(tempT3d);
        showBeam(true);
        double currDist = 0.0;
        boolean hitTarget = closeToTarget();
        while (currDist < MAX_RANGE && !hitTarget) {
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
        inUse = false; // shot is finished
    } // end of moveBeam()

    public boolean requestFiring()
    // If the beam is not being used then fire it
    {
        if (inUse) {
            return false;
        } else {
            inUse = true;
            new AnimBeam(this).start(); // calls moveBeam() inside a thread
            return true;
        }
    } // end of fireBeam()

    private void showBeam(boolean toVisible)
    // make beam visible/invisible
    {
        if (toVisible) {
            beamSW.setWhichChild(0); // make visible
        } else {
            beamSW.setWhichChild(Switch.CHILD_NONE); // make invisible
        }
    } // end of showBeam()

    private void showExplosion()
    // start the explosion
    {
        beamSW.setWhichChild(1); // make visible
        explShape.showSeries(); // boom!
        beamSW.setWhichChild(Switch.CHILD_NONE); // make invisible
    } // end of showExplosion()
} // end of LaserShot class
