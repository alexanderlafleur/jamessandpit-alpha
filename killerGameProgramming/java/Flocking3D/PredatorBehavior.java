package Flocking3D;

// PredatorBehavior.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
// Sirinart Sakarin, March 2003, s4210315@calvin.coe.psu.ac.th

/* PredatorBehavior maintains its own BoidsList of Boids, _and_ a
 reference to the prey BoidsList. The list is obtained via a reference
 to the PreyBehavior object.

 PredatorBehavior initialises its BoidsList of Boids and the 
 BranchGroup of Boid BranchGroup nodes.

 PredatorBehavior maintains one extra velocity rule: findClosePrey()
 - if the predator is hungry then it looks for nearby prey to eat
 This method is called by each PredatorBoid object.

 There is also an eatClosePrey() method which is called by PredatorBoid
 to actual eat PreyBoid if one (or more) is sufficiently close.
 */

import javax.vecmath.Vector3f;

public class PredatorBehavior extends FlockBehavior {
    // scaling factor for the velocity calculated by findClosePrey() rule
    private final static float FIND_WEIGHT = 0.2f;

    private BoidsList preyList;

    private PreyBehavior preyBeh;

    // used for repeated calculations
    private Vector3f preyPos = new Vector3f();

    private Vector3f distFrom = new Vector3f();

    public PredatorBehavior(int numBoids, Obstacles obs) {
        super(numBoids);
        System.out.println("Num. Predators: " + numBoids);
        createBoids(numBoids, obs);
    }

    private void createBoids(int numBoids, Obstacles obs)
    /*
     * Initialise the Boidslist of PredatorBoid objects and add the TransformGroups for these boids to boidsBG. Boid is a subclass of TransformGroup so can be added to boidsBG
     * directly.
     */
    {
        PredatorBoid pb;
        for (int i = 0; i < numBoids; i++) {
            pb = new PredatorBoid(obs, this);
            this.boidsBG.addChild(pb); // add to BranchGroup
            this.boidsList.add(pb); // add to BoidsList
        }
        this.boidsBG.addChild(this); // store the pred. behaviour with its BG
    } // end of createBoids()

    public void setPreyBeh(PreyBehavior pb)
    // store the prey's behavior
    {
        this.preyBeh = pb;
    }

    public int eatClosePrey(Vector3f boidPos)
    /*
     * Find a nearby PreyBoid (or PreyBoids) and eat it. This causes the PreyBoid to be detached from the scene and from its BoidsList. Return the number of prey eaten (usually 1
     * or 0). Called by animateBoid() in PredatorBoid.
     */
    {
        this.preyList = this.preyBeh.getBoidsList();
        int numPrey = this.preyList.size();
        int numEaten = 0;
        PreyBoid b;

        int i = 0;
        while ((b = (PreyBoid) this.preyList.getBoid(i)) != null) {
            this.distFrom.set(boidPos);
            this.distFrom.sub(b.getBoidPos());
            if (this.distFrom.length() < PROXIMITY / 3.0) { // is boid v.close to
                // the prey
                this.preyBeh.eatBoid(i); // found prey, so eat it
                numPrey--;
                numEaten++;
                System.out.println("numPrey: " + numPrey);
            } else {
                i++;
            }
        }
        return numEaten;
    } // end of eatClosePrey()

    public Vector3f findClosePrey(Vector3f boidPos)
    /*
     * Find all the nearby prey, then move towards their average position by a scaled amount.
     * 
     * This is an extra velocity rule used by each PredatorBoid. Called by doVelocityRules() in PredatorBoid.
     */
    {
        this.preyList = this.preyBeh.getBoidsList(); // get prey list
        int numClosePrey = 0;
        this.preyPos.set(0, 0, 0);
        Vector3f pos;
        PreyBoid b;

        int i = 0;
        while ((b = (PreyBoid) this.preyList.getBoid(i)) != null) {
            pos = b.getBoidPos();
            this.distFrom.set(pos);
            this.distFrom.sub(boidPos);
            if (this.distFrom.length() < PROXIMITY * 1.5f) { // see further
                this.preyPos.add(this.distFrom); // add distance to tally
                numClosePrey++;
            }
            i++;
        }
        if (numClosePrey > 0) {
            this.preyPos.scale(1.0f / numClosePrey); // calculate average position
            // scale to reduce distance moved towards the position
            this.preyPos.scale(FIND_WEIGHT);
        }
        return this.preyPos;
    } // end of findClosePrey()

} // end of PredatorBehavior class
