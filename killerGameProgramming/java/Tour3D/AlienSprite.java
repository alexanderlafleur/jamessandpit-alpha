package Tour3D;

// AlienSprite.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* We assume the alien is facing in the +z-axis direction
 and is originally at (0, ??, 0). Rotations are specified
 relative to the original orientation along the z-axis.

 Its behaviour is
 specified in headTowardsTourist() which is called by
 update(), which is called by a TimeBehaviour object
 every few 100ms.

 Similar to the behaviour used AlienQuadSprite in Code/Tiles/
 */
import java.text.DecimalFormat;

import javax.vecmath.Point3d;

public class AlienSprite extends TourSprite {
    private static final double CLOSE_DIST = 0.2;
    private double currAngle; // current y-axis angle of sprite
    private DecimalFormat df; // for simpler output during debugging
    private TourSprite ts;

    public AlienSprite(String fnm, Obstacles obs, TourSprite ts) {
        super(fnm, obs);
        df = new DecimalFormat("0.###"); // 3 dp
        this.ts = ts;
        currAngle = 0.0;
    } // end of AlienSprite()

    private double calcTurn(Point3d alienLoc, Point3d touristLoc)
    /*
     * Calculate the angle to turn the alien around the y-axis, so that it is facing towards the tourist. This angle is relative to the original
     * orientation facing along +z-axis
     */
    { // printTuple(alienLoc, "alienLoc");
        // printTuple(touristLoc, "touristLoc");
        double zDiff = touristLoc.z - alienLoc.z;
        double xDiff = touristLoc.x - alienLoc.x;
        // System.out.println("xDiff: " + df.format(xDiff) +
        // "; zDiff: " + df.format(zDiff) );
        double turnAngle = 0.0; // the angle to rotate the alien
        if (zDiff != 0.0) {
            double angle = Math.atan(xDiff / zDiff);
            // the angle from alienLoc to touristLoc
            // System.out.println("angle: " + df.format(angle));
            if (xDiff > 0 && zDiff > 0) {
                turnAngle = angle;
            } else if (xDiff > 0 && zDiff < 0) {
                turnAngle = Math.PI + angle; // since angle has neg value
            } else if (xDiff < 0 && zDiff < 0) {
                turnAngle = Math.PI + angle;
            } else if (xDiff < 0 && zDiff > 0) {
                turnAngle = angle; // since angle has neg value
            }
        } else { // zDiff == 0.0, which is unlikely with doubles
            if (xDiff > 0) {
                turnAngle = Math.PI / 2;
            } else if (xDiff < 0) {
                turnAngle = -Math.PI / 2;
            } else {
                // alien and sprite at *exact* same place
                turnAngle = 0.0;
            }
        }
        // System.out.println("turnAngle: " + df.format(turnAngle));
        return turnAngle;
    } // end of calcAngle()

    private boolean closeTogether(Point3d alienLoc, Point3d touristLoc)
    // Are the alien and Tourist close together?
    {
        double distApart = alienLoc.distance(touristLoc);
        if (distApart < CLOSE_DIST) {
            return true;
        }
        return false;
    } // end of closeTogether()

    private void headTowardsTourist()
    /*
     * Turn to face tourist then move towards him, unless there is an obstacle, then try to move round it. rotAngle is the angle from the original
     * orientation facing along the +z-axis. We use angleChg to move the alien which is the difference from the current angle of the alien.
     */
    {
        double rotAngle = calcTurn(getCurrLoc(), ts.getCurrLoc());
        double angleChg = rotAngle - currAngle;
        // System.out.println("angleChg: " + df.format(angleChg));
        doRotateY(angleChg);
        currAngle = rotAngle; // store new angle for next time
        // move forward if possible, or left, right, or back
        // this fixed ordering can lead the alien into livelock
        // if the obstacles are positioned carefully :(
        if (moveForward()) {
            ;
        } else if (moveLeft()) {
            ;
        } else if (moveRight()) {
            ;
        } else if (moveBackward()) {
            ;
        } else {
            System.out.println("Alien stuck!");
        }
    } // end of headTowardsTourist()

    public void update()
    // called by TimeBehaviour to update the alien
    {
        if (isActive()) {
            headTowardsTourist();
            if (closeTogether(getCurrLoc(), ts.getCurrLoc())) {
                System.out.println("Alien and Tourist are close together");
            }
        }
    } // end of updateSprite()
} // end of AlienSprite
