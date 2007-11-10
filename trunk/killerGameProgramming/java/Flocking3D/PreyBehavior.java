package Flocking3D;

// PreyBehavior.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
// Sirinart Sakarin, March 2003, s4210315@calvin.coe.psu.ac.th

/* PreyBehavior maintains its own BoidsList of Boids, _and_ a
 reference to the predator BoidsList. The list is obtained 
 via a reference to the PredatorBehavior object.

 PreyBehavior initialises its BoidsList of Boids and the 
 BranchGroup of Boid BranchGroup nodes.

 PreyBehaviour maintains one extra velocity rule: seePredators()
 - if a predator is near then flee
 This method is called by each PreyBoid object.

 */

import javax.media.j3d.Group;
import javax.vecmath.Vector3f;

public class PreyBehavior extends FlockBehavior {
    // scaling factor for the velocity calculated by seePredators() rule
    private final static float FLEE_WEIGHT = 0.2f;

    private BoidsList predsList;

    private PredatorBehavior predBeh;

    // used for repeated calculations
    private Vector3f avoidPred = new Vector3f();

    private Vector3f distFrom = new Vector3f();

    public PreyBehavior(int numBoids, Obstacles obs) {
        super(numBoids);
        System.out.println("Num. Prey: " + numBoids);
        createBoids(numBoids, obs);
    }

    private void createBoids(int numBoids, Obstacles obs)
    /*
     * Initialise the Boidslist of PreyBoid objects and add the TransformGroups for these boids to boidsBG. Boid is a subclass of TransformGroup so can be added to boidsBG
     * directly.
     */
    { // preyBoids can be detached from the scene
        this.boidsBG.setCapability(Group.ALLOW_CHILDREN_WRITE);
        this.boidsBG.setCapability(Group.ALLOW_CHILDREN_EXTEND);

        PreyBoid pb;
        for (int i = 0; i < numBoids; i++) {
            pb = new PreyBoid(obs, this);
            this.boidsBG.addChild(pb); // add to BranchGroup
            this.boidsList.add(pb); // add to BoidsList
        }
        this.boidsBG.addChild(this); // store the prey behaviour with its BG
    } // end of createBoids()

    public void setPredBeh(PredatorBehavior pb)
    // store the predator's behavior
    {
        this.predBeh = pb;
    }

    public Vector3f seePredators(Vector3f boidPos)
    /*
     * If the boid is close to a predator, then it moves in the opposite direction by a scaled amount. This is an extra velocity rule, used by each PreyBoid by calling
     * doVelocityRules()
     */
    {
        this.predsList = this.predBeh.getBoidsList(); // refer to pred list
        this.avoidPred.set(0, 0, 0); // reset
        Vector3f predPos;
        PredatorBoid b;

        int i = 0;
        while ((b = (PredatorBoid) this.predsList.getBoid(i)) != null) {
            this.distFrom.set(boidPos);
            predPos = b.getBoidPos();
            this.distFrom.sub(predPos);
            if (this.distFrom.length() < PROXIMITY) { // is the pred. boid close?
                this.avoidPred.set(this.distFrom);
                this.avoidPred.scale(FLEE_WEIGHT);
                break;
            }
            i++;
        }
        return this.avoidPred;
    } // end of seePredators()

    public void eatBoid(int i)
    /*
     * PreyBoid 'i' is to be eaten. This causes it to be detached from the scene and removed from its BoidsList. Called by eatFood() in PredatorBehavior.
     */
    {
        ((PreyBoid) this.boidsList.getBoid(i)).boidDetach();
        this.boidsList.removeBoid(i);
    }

} // end of PreyBehavior class
