package FractalLand3D;

// KeyBehavior.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* Arrow functionality:
 forwards, backwards, left, right, up, down, turn left/right

 Very similar to KeyBehavior in FPShooter3D/

 When a move is requested by the user, KeyBehavior calculates
 the new position of the (x,z) coords, then asks the Landscape
 object if it is on the floor. If it is then Landscape is used
 to find the y-component (the floor height at that point).
 */
import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.behaviors.vp.ViewPlatformBehavior;

public class KeyBehavior extends ViewPlatformBehavior {
    private static final Vector3d BACK = new Vector3d(0, 0, MOVE_STEP);
    private static final Vector3d DOWN = new Vector3d(0, -MOVE_STEP, 0);
    // hardwired movement vectors
    private static final Vector3d FWD = new Vector3d(0, 0, -MOVE_STEP);
    private static final Vector3d LEFT = new Vector3d(-MOVE_STEP, 0, 0);
    private static final double MOVE_STEP = 0.2;
    private static final Vector3d RIGHT = new Vector3d(MOVE_STEP, 0, 0);
    private static final double ROT_AMT = Math.PI / 36.0; // 5 degrees
    private static final Vector3d UP = new Vector3d(0, MOVE_STEP, 0);
    private static final double USER_HEIGHT = 1.0; // of head above the floor
    private int backKey = KeyEvent.VK_DOWN;
    private double currLandHeight; // floor height at current position
    // key names
    private int forwardKey = KeyEvent.VK_UP;
    private WakeupCondition keyPress;
    private Landscape land; // used for checking/calculating moves
    private int leftKey = KeyEvent.VK_LEFT;
    private int rightKey = KeyEvent.VK_RIGHT;
    // for repeated calcs
    private Transform3D t3d = new Transform3D();
    private Transform3D toMove = new Transform3D();
    private Transform3D toRot = new Transform3D();
    private Vector3d trans = new Vector3d();
    private int zOffset; // used when moving up/down

    public KeyBehavior(Landscape ld, TransformGroup steerTG) {
        land = ld;
        zOffset = 0; // user is standing on the floor at the start
        initViewPosition(steerTG);
        keyPress = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
    } // end of KeyBehavior()

    private void altMove(int keycode)
    // moves viewer up or down, left or right
    {
        if (keycode == forwardKey) {
            doMove(UP); // no testing using moveBy()
            zOffset++;
        } else if (keycode == backKey) {
            if (zOffset > 0) {
                doMove(DOWN); // no testing using moveBy()
                zOffset--;
            }
        } else if (keycode == leftKey) {
            moveBy(LEFT);
        } else if (keycode == rightKey) {
            moveBy(RIGHT);
        }
    } // end of altMove()

    private void doMove(Vector3d theMove)
    // move the viewpoint by theMove offset
    {
        targetTG.getTransform(t3d);
        toMove.setTranslation(theMove);
        t3d.mul(toMove);
        targetTG.setTransform(t3d); // update viewpoint's TG
    } // end of doMove()

    @Override
    public void initialize() {
        wakeupOn(keyPress);
    }

    private void initViewPosition(TransformGroup steerTG)
    // place viewpoint at (0,?,0), facing into scene
    {
        Vector3d startPosn = land.getOriginVec();
        // startPosn is (0, <height of floor>, 0)
        currLandHeight = startPosn.y; // store current floor height
        startPosn.y += USER_HEIGHT; // add user's height
        steerTG.getTransform(t3d); // targetTG not yet available
        t3d.setTranslation(startPosn); // so use steerTG
        steerTG.setTransform(t3d);
    } // end of initViewPosition()

    private void moveBy(Vector3d theMove)
    /*
     * Calculate the next position on the floor (x,?,z). Test if it is within the floor boundaries. If it is then ask Landscape to get the floor
     * height for that (x,z). Then set the y value to a change so that the viewpoint will rest on the floor at (x,z)
     */
    {
        Vector3d nextLoc = tryMove(theMove); // next (x,?,z) user position
        if (!land.inLandscape(nextLoc.x, nextLoc.z)) {
            return;
        }
        // Landscape returns floor height at (x,z)
        double floorHeight = land.getLandHeight(nextLoc.x, nextLoc.z, currLandHeight);
        // Calculate the change from the current y-position.
        // Reset any offset upwards back to 0.
        double heightChg = floorHeight - currLandHeight - MOVE_STEP * zOffset;
        currLandHeight = floorHeight; // update current height
        zOffset = 0; // back on floor, so no offset
        Vector3d actualMove = new Vector3d(theMove.x, heightChg, theMove.z);
        doMove(actualMove);
    } // end of moveBy()

    private void processKeyEvent(KeyEvent eventKey) {
        int keyCode = eventKey.getKeyCode();
        // System.out.println(keyCode);
        if (eventKey.isAltDown()) {
            altMove(keyCode);
        } else {
            standardMove(keyCode);
        }
    } // end of processKeyEvent()

    // ----------------------- moves ----------------------------
    @Override
    public void processStimulus(Enumeration criteria)
    // respond to a keypress
    {
        WakeupCriterion wakeup;
        AWTEvent[] event;
        while (criteria.hasMoreElements()) {
            wakeup = (WakeupCriterion) criteria.nextElement();
            if (wakeup instanceof WakeupOnAWTEvent) {
                event = ((WakeupOnAWTEvent) wakeup).getAWTEvent();
                for (AWTEvent element : event) {
                    if (element.getID() == KeyEvent.KEY_PRESSED) {
                        processKeyEvent((KeyEvent) element);
                    }
                }
            }
        }
        wakeupOn(keyPress);
    } // end of processStimulus()

    private void rotateY(double radians)
    // rotate about y-axis by radians
    {
        targetTG.getTransform(t3d);
        toRot.rotY(radians);
        t3d.mul(toRot);
        targetTG.setTransform(t3d);
    } // end of rotateY()

    private void standardMove(int keycode)
    /*
     * viewer moves forward or backward; rotate left or right
     */
    {
        if (keycode == forwardKey) {
            moveBy(FWD);
        } else if (keycode == backKey) {
            moveBy(BACK);
        } else if (keycode == leftKey) {
            rotateY(ROT_AMT);
        } else if (keycode == rightKey) {
            rotateY(-ROT_AMT);
        }
    } // end of standardMove()

    // -------------- rotation --------------------
    private Vector3d tryMove(Vector3d theMove)
    /*
     * Calculate the effect of the given translation to get the new (x,?, z) coord. Do not update the viewpoint's TG yet
     */
    {
        targetTG.getTransform(t3d);
        toMove.setTranslation(theMove);
        t3d.mul(toMove);
        t3d.get(trans);
        return trans;
    } // end of tryMove()
} // end of KeyBehavior class
