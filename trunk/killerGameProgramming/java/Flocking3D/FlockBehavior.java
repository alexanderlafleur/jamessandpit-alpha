package Flocking3D;

// FlockBehavior.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
// Sirinart Sakarin, March 2003, s4210315@calvin.coe.psu.ac.th
/* Flockbehavior maintains a BoidsList of Boid subclass objects, and
 a BranchGroup holding the BranchGroups for each boid.

 The BoidsList and BranchGroup are initialised by the subclasses of 
 FlockBehavior.

 FlockBehavior executes every DELAY milliseconds, and calls 
 animateBoid() on each of the boids in its ArrayList.

 FlockBehavior stores Reynold's velocity rules for:
 - cohesion
 - separation
 - alignment
 These rules are caled by each boid.

 The design approach is that the behavior object will store
 rules which require an examination of flockmates (or other
 dynamically changing things in the system).

 Extra velocity rules are stored in the subclasses of FlockBehavior.
 */
import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupOnElapsedTime;
import javax.vecmath.Vector3f;

public class FlockBehavior extends Behavior {
    private final static float ALIGNMENT_WEIGHT = 0.2f;
    // flockmates are boids that are this close (or closer)
    // scaling factors for the velocities calculated by the rules
    private final static float COHESION_WEIGHT = 0.2f;
    private final static int DELAY = 50;
    protected final static float PROXIMITY = 2.0f;
    private final static float SEPERATION_WEIGHT = 0.2f;
    // used for repeated calculations
    private Vector3f avgPosn = new Vector3f();
    private Vector3f avgVel = new Vector3f();
    protected BranchGroup boidsBG; // holds boid BGs
    protected BoidsList boidsList; // holds Boid subclass objects
    private Vector3f clearDist = new Vector3f();
    private Vector3f distFrom = new Vector3f();
    private Vector3f moveAway = new Vector3f();
    private WakeupCondition timeOut;

    public FlockBehavior(int numBoids) {
        boidsList = new BoidsList(numBoids);
        boidsBG = new BranchGroup();
        timeOut = new WakeupOnElapsedTime(DELAY);
    } // end of FlockBehavior()

    public Vector3f alignment(Vector3f boidPos, Vector3f boidVel)
    /*
     * A boid tries to travel with the average velocity of its flockmates. The velocity is scaled so that the boid adjusts its velocity gradually.
     */
    {
        avgVel.set(0, 0, 0); // reset
        int numFlockMates = 0;
        Vector3f pos;
        Boid b;
        int i = 0;
        while ((b = boidsList.getBoid(i)) != null) {
            distFrom.set(boidPos);
            pos = b.getBoidPos();
            distFrom.sub(pos);
            if (distFrom.length() < PROXIMITY) { // is the boid a flockmate?
                avgVel.add(((Boid) boidsList.get(i)).getBoidVel());
                // add its velcoity to the tally
                numFlockMates++;
            }
            i++;
        }
        avgVel.sub(boidVel); // don't include boid's own velocity
        numFlockMates--;
        if (numFlockMates > 0) {
            avgVel.scale(1.0f / numFlockMates);
            // scale to reduce velocity change towards the average
            avgVel.scale(ALIGNMENT_WEIGHT);
        }
        return avgVel;
    } // end of alignment()

    public Vector3f cohesion(Vector3f boidPos)
    /*
     * A boid tries to fly towards the average position of its flockmates. The velocity is a small step from the boid's current position (stored in
     * boidPos) towards the average position.
     */
    {
        avgPosn.set(0, 0, 0); // reset and default answer
        int numFlockMates = 0;
        Vector3f pos;
        Boid b;
        int i = 0;
        while ((b = boidsList.getBoid(i)) != null) {
            distFrom.set(boidPos);
            pos = b.getBoidPos();
            distFrom.sub(pos);
            if (distFrom.length() < PROXIMITY) { // is the boid a flockmate?
                avgPosn.add(pos); // add position to tally
                numFlockMates++;
            }
            i++;
        }
        avgPosn.sub(boidPos); // don't include the boid itself
        numFlockMates--;
        if (numFlockMates > 0) { // there were flockmates
            avgPosn.scale(1.0f / numFlockMates); // calculate average
            // position
            // calculate a small step towards the avg. posn
            avgPosn.sub(boidPos);
            avgPosn.scale(COHESION_WEIGHT);
        }
        return avgPosn;
    } // end of cohesion()

    public BranchGroup getBoidsBG()
    // used by WrapFlocking3D to add boids to the scene
    {
        return boidsBG;
    }

    public BoidsList getBoidsList() {
        return boidsList;
    }

    // ------------ cohesion, separation, and alignment rules ---------
    @Override
    public void initialize() {
        wakeupOn(timeOut);
    }

    @Override
    public void processStimulus(Enumeration en)
    // call animateBoids() on every visible boid
    {
        Boid b;
        int i = 0;
        while ((b = boidsList.getBoid(i)) != null) {
            b.animateBoid();
            i++;
        }
        wakeupOn(timeOut); // schedule next update
    } // end of processStimulus()

    public Vector3f separation(Vector3f boidPos)
    /*
     * A boid tries to keep a small distance away from its flockmates The velocity is the average distance of the flockmates from the boid, scaled so
     * that the boid moves a little bit away instead of a mighty leap.
     */
    {
        moveAway.set(0, 0, 0); // reset
        int numFlockMates = 0;
        Boid b;
        int i = 0;
        while ((b = boidsList.getBoid(i)) != null) {
            distFrom.set(boidPos);
            distFrom.sub(b.getBoidPos());
            if (distFrom.length() < PROXIMITY) { // is the boid a flockmate?
                moveAway.add(distFrom); // add distance away to tally
                numFlockMates++;
            }
            i++;
        }
        numFlockMates--; // don't count the boid's distance from itself
        if (numFlockMates > 0) {
            moveAway.scale(1.0f / numFlockMates); // calculate average
            // distance from
            // scale to reduce distance moved away
            moveAway.scale(SEPERATION_WEIGHT);
        }
        return moveAway;
    } // end of separation()
} // end of FlockBehavior class
