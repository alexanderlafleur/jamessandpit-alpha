package NetTour3D;

// TourSprite.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* TourSprite hides the movement and rotation amounts
 when the sprite is moved or rotated.

 Net-related additions:
 * a PrintWriter stream connected to the server so the 
 sprite can send creation, movement, and rotation commands to it.

 They will be passed to the other clients by the server,
 so the client's sprite on those machines will be updated.

 * extra arguments in the constructor to set the
 sprite's (x,z) position
 */

import java.io.PrintWriter;

public class TourSprite extends Sprite3D {
    private final static double MOVERATE = 0.3;

    private final static double ROTATE_AMT = Math.PI / 16.0;

    PrintWriter out; // for sending commands to the server

    public TourSprite(String userName, String fnm, Obstacles obs, double xPosn, double zPosn, PrintWriter o) {
        super(userName, fnm, obs);
        setPosition(xPosn, zPosn);
        this.out = o;
        this.out.println("create " + userName + " " + xPosn + " " + zPosn);
    } // end of TourSprite()

    // moves
    public boolean moveForward() {
        this.out.println("forward");
        return moveBy(0.0, MOVERATE);
    }

    public boolean moveBackward() {
        this.out.println("back");
        return moveBy(0.0, -MOVERATE);
    }

    public boolean moveLeft() {
        this.out.println("left");
        return moveBy(-MOVERATE, 0.0);
    }

    public boolean moveRight() {
        this.out.println("right");
        return moveBy(MOVERATE, 0.0);
    }

    // rotations in Y-axis only
    public void rotClock() {
        this.out.println("rotClock");
        doRotateY(-ROTATE_AMT); // clockwise
    }

    public void rotCounterClock() {
        this.out.println("rotCClock");
        doRotateY(ROTATE_AMT); // counter-clockwise
    }

} // end of TourSprite
