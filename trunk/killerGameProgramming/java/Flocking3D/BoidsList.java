package Flocking3D;

// BoidsList.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
// Sirinart Sakarin, March 2003, s4210315@calvin.coe.psu.ac.th

/* An ArrayList for boids. 
 The methods are synchronized so that boid removal cannot
 affect the retrieval of a reference to a boid.
 */

import java.util.ArrayList;

public class BoidsList extends ArrayList {
    /**
     * 
     */
    private static final long serialVersionUID = -2382432228864285099L;

    public BoidsList(int num) {
        super(num);
    }

    synchronized public Boid getBoid(int i)
    // return the boid if it is visible; null otherwise
    {
        if (i < super.size()) {
            return (Boid) get(i);
        }
        return null;
    }

    synchronized public boolean removeBoid(int i)
    // attempt to remove the i'th boid
    {
        if (i < super.size()) {
            super.remove(i);
            return true;
        }
        return false;
    }

} // end of BoidsList class
