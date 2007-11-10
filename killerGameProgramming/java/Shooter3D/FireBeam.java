package Shooter3D;

// FireBeam.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* The beam is shot at the intercept point.
 A boolean back in the ShootingBehaviour object
 is set when the beam has reached the intercept.

 The explosion (ExplosionsClip) is also triggered from here.
 Another beam can be fired while the explosion is going off.

 We use a thread since the beam animation is meant to be separate
 from other activities, and is triggered from ShootingBehavior
 after the cone and beam are pointing in the right direction.
 */

import javax.vecmath.Point3d;

public class FireBeam extends Thread {
    private Point3d intercept;

    private ShootingBehaviour shooter;

    private LaserBeam laser;

    private ExplosionsClip explsClip;

    private double turnAngle;

    public FireBeam(Point3d ipt, ShootingBehaviour b, LaserBeam lb, ExplosionsClip ec, double ta) {
        this.intercept = ipt;
        this.shooter = b; // so boolean can be set back in calling behaviour
        this.laser = lb;
        this.explsClip = ec;
        this.turnAngle = ta;
    } // end of FireBeam

    @Override
    public void run() {
        this.laser.shootBeam(this.intercept);
        this.shooter.setFinishedShot(); // beam has reached its target
        this.explsClip.showExplosion(this.turnAngle, this.intercept); // boom!
        /*
         * If this method is called at the same time by two copies of this thread then only a partial animation appears.
         */
    } // end of run()

} // end of FireBeam class
