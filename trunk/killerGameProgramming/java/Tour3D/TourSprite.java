package Tour3D;

// TourSprite.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* TourSprite hides the movement and rotation amounts
 when the sprite is moved or rotated.
 */
public class TourSprite extends Sprite3D {
    private final static double MOVERATE = 0.3;
    private final static double ROTATE_AMT = Math.PI / 16.0;

    public TourSprite(String fnm, Obstacles obs) {
        super(fnm, obs);
    }

    public boolean moveBackward() {
        return moveBy(0.0, -MOVERATE);
    }

    // moves
    public boolean moveForward() {
        return moveBy(0.0, MOVERATE);
    }

    public boolean moveLeft() {
        return moveBy(-MOVERATE, 0.0);
    }

    public boolean moveRight() {
        return moveBy(MOVERATE, 0.0);
    }

    // rotations in Y-axis only
    public void rotClock() {
        doRotateY(-ROTATE_AMT);
    } // clockwise

    public void rotCounterClock() {
        doRotateY(ROTATE_AMT);
    } // counter-clockwise
} // end of TourSprite
