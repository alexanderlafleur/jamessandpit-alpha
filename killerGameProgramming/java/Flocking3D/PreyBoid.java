package Flocking3D;

// PreyBoid.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
// Sirinart Sakarin, March 2003, s4210315@calvin.coe.psu.ac.th

/* A prey boid has an orange body and wants to avoid predators.
 When it sees nearby predators, it tries to evade them.

 Prey boid have a higher maximum speed than predators.

 A Prey boid can be 'eaten' which means it is detached
 from the scene and removed from its BoidsList.
 */

import javax.media.j3d.BranchGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

public class PreyBoid extends Boid {
    private final static Color3f orange = new Color3f(1.0f, 0.75f, 0.0f);

    public PreyBoid(Obstacles obs, PreyBehavior beh) {
        super(orange, 2.0f, obs, beh); // orange and with a higher max speed
        setCapability(BranchGroup.ALLOW_DETACH); // prey boids can be 'eaten'
    } // end of PreyBoid()

    @Override
    protected void doVelocityRules()
    /*
     * Override doVelocityRules() to check for nearby predators and evade them.
     */
    {
        Vector3f v = ((PreyBehavior) this.beh).seePredators(this.boidPos);
        this.velChanges.add(v);
        super.doVelocityRules();
    } // end of doVelocityRules()

    public void boidDetach()
    // detach this boid from the scene, since being 'eaten'
    {
        detach();
    }

} // end of PreyBoid class
