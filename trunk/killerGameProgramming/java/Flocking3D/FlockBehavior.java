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
    protected final static float PROXIMITY = 2.0f;

    // flockmates are boids that are this close (or closer)

    // scaling factors for the velocities calculated by the rules
    private final static float COHESION_WEIGHT = 0.2f;

    private final static float SEPERATION_WEIGHT = 0.2f;

    private final static float ALIGNMENT_WEIGHT = 0.2f;

    private final static int DELAY = 50;

    protected BoidsList boidsList; // holds Boid subclass objects

    protected BranchGroup boidsBG; // holds boid BGs

    private WakeupCondition timeOut;

    // used for repeated calculations
    private Vector3f avgPosn = new Vector3f();

    private Vector3f distFrom = new Vector3f();

    private Vector3f moveAway = new Vector3f();

    private Vector3f avgVel = new Vector3f();

    private Vector3f clearDist = new Vector3f();

    public FlockBehavior(int numBoids) {
        this.boidsList = new BoidsList(numBoids);
        this.boidsBG = new BranchGroup();
        this.timeOut = new WakeupOnElapsedTime(DELAY);
    } // end of FlockBehavior()

    @Override
    public void initialize() {
        wakeupOn(this.timeOut);
    }

    @Override
    public void processStimulus(Enumeration en)
    // call animateBoids() on every visible boid
    {
        Boid b;
        int i = 0;
        while ((b = this.boidsList.getBoid(i)) != null) {
            b.animateBoid();
            i++;
        }
        wakeupOn(this.timeOut); // schedule next update
    } // end of processStimulus()

    public BoidsList getBoidsList() {
        return this.boidsList;
    }

    public BranchGroup getBoidsBG()
    // used by WrapFlocking3D to add boids to the scene
    {
        return this.boidsBG;
    }

    // ------------ cohesion, separation, and alignment rules ---------

    public Vector3f cohesion(Vector3f boidPos)
    /*
     * A boid tries to fly towards the average position of its flockmates.
     * 
     * The velocity is a small step from the boid's current position (stored in boidPos) towards the average position.
     */
    {
        this.avgPosn.set(0, 0, 0); // reset and default answer
        int numFlockMates = 0;
        Vector3f pos;
        Boid b;

        int i = 0;
        while ((b = this.boidsList.getBoid(i)) != null) {
            this.distFrom.set(boidPos);
            pos = b.getBoidPos();
            this.distFrom.sub(pos);
            if (this.distFrom.length() < PROXIMITY) { // is the boid a flockmate?
                this.avgPosn.add(pos); // add position to tally
                numFlockMates++;
            }
            i++;
        }
        this.avgPosn.sub(boidPos); // don't include the boid itself
        numFlockMates--;

        if (numFlockMates > 0) { // there were flockmates
            this.avgPosn.scale(1.0f / numFlockMates); // calculate average
            // position
            // calculate a small step towards the avg. posn
            this.avgPosn.sub(boidPos);
            this.avgPosn.scale(COHESION_WEIGHT);
        }
        return this.avgPosn;
    } // end of cohesion()

    public Vector3f separation(Vector3f boidPos)
    /*
     * A boid tries to keep a small distance away from its flockmates
     * 
     * The velocity is the average distance of the flockmates from the boid, scaled so that the boid moves a little bit away instead of a mighty leap.
     */
    {
        this.moveAway.set(0, 0, 0); // reset
        int numFlockMates = 0;
        Boid b;

        int i = 0;
        while ((b = this.boidsList.getBoid(i)) != null) {
            this.distFrom.set(boidPos);
            this.distFrom.sub(b.getBoidPos());
            if (this.distFrom.length() < PROXIMITY) { // is the boid a flockmate?
                this.moveAway.add(this.distFrom); // add distance away to tally
                numFlockMates++;
            }
            i++;
        }
        numFlockMates--; // don't count the boid's distance from itself
        if (numFlockMates > 0) {
            this.moveAway.scale(1.0f / numFlockMates); // calculate average
            // distance from
            // scale to reduce distance moved away
            this.moveAway.scale(SEPERATION_WEIGHT);
        }
        return this.moveAway;
    } // end of separation()

    public Vector3f alignment(Vector3f boidPos, Vector3f boidVel)
    /*
     * A boid tries to travel with the average velocity of its flockmates. The velocity is scaled so that the boid adjusts its velocity gradually.
     */
    {
        this.avgVel.set(0, 0, 0); // reset
        int numFlockMates = 0;
        Vector3f pos;
        Boid b;

        int i = 0;
        while ((b = this.boidsList.getBoid(i)) != null) {
            this.distFrom.set(boidPos);
            pos = b.getBoidPos();
            this.distFrom.sub(pos);
            if (this.distFrom.length() < PROXIMITY) { // is the boid a flockmate?
                this.avgVel.add(((Boid) this.boidsList.get(i)).getBoidVel());
                // add its velcoity to the tally
                numFlockMates++;
            }
            i++;
        }
        this.avgVel.sub(boidVel); // don't include boid's own velocity
        numFlockMates--;

        if (numFlockMates > 0) {
            this.avgVel.scale(1.0f / numFlockMates);
            // scale to reduce velocity change towards the average
            this.avgVel.scale(ALIGNMENT_WEIGHT);
        }
        return this.avgVel;
    } // end of alignment()

} // end of FlockBehavior class
