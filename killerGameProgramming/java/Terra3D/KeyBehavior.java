package Terra3D;

// KeyBehavior.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* Similar to KeyBehavior in FractalLand3D/

 Arrow functionality:
 * forwards, backwards, left, right, up, down, turn left/right

 * where   //  prints the user's position in land coordinates

 The major change is that a move does not _immediately_ affect
 the user's height position on the terrain. The height calculation
 is delegated to the HeightFinder object, which may take 1-2 secs
 to obtain the height through picking. In the meantime, KeyBehavior
 continues to use the old height position.

 This means that the user can move inside mountains, but the user's 
 height will eventually be corrected.
 */
import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
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
    private static final double MOVE_STEP = 0.2; // was 0.2
    private static final Vector3d RIGHT = new Vector3d(MOVE_STEP, 0, 0);
    private static final double ROT_AMT = Math.PI / 36.0; // 5 degrees
    private static final Vector3d UP = new Vector3d(0, MOVE_STEP, 0);
    private static final double USER_HEIGHT = 0.5; // of head above the floor
    private int backKey = KeyEvent.VK_DOWN;
    private double currLandHeight; // floor height at current position (in
    // world coords)
    private DecimalFormat df; // for simpler output
    // key names
    private int forwardKey = KeyEvent.VK_UP;
    private HeightFinder hf; // HeightFinder processes height requests
    private WakeupCondition keyPress;
    private Landscape land;
    private int leftKey = KeyEvent.VK_LEFT;
    private int rightKey = KeyEvent.VK_RIGHT;
    // for repeated calcs
    private Transform3D t3d = new Transform3D();
    private Transform3D toMove = new Transform3D();
    private Transform3D toRot = new Transform3D();
    private Vector3d trans = new Vector3d();
    private int whereKey = KeyEvent.VK_W; // 'where' button
    private int zOffset; // used when moving up/down

    public KeyBehavior(Landscape ld, TransformGroup steerTG) {
        land = ld;
        zOffset = 0; // user is standing on the floor at the start
        initViewPosition(steerTG);
        df = new DecimalFormat("0.###"); // 3 dp
        hf = new HeightFinder(land, this);
        hf.start();
        keyPress = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
    } // end of KeyBehavior()

    public void adjustHeight(double newHeight)
    /*
     * Called by HeightFinder to adjust the viewer's y-axis position. It is synchronized, so that it won't interfere with a call to moveBy() which
     * also adjust's the viewer's position.
     */
    { // System.out.println("adjustHeight: " + newHeight);
        double heightChg = newHeight - currLandHeight - MOVE_STEP * zOffset;
        Vector3d upVec = new Vector3d(0, heightChg, 0);
        currLandHeight = newHeight; // update current height
        zOffset = 0; // back on floor, so no offset
        doMove(upVec);
    } // end of adjustHeight()

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

    synchronized private void doMove(Vector3d theMove)
    /*
     * Move the viewpoint by theMove offset. This method can be called by KeyBehavior from moveBy() and HeightFinder from adjustHeight(), so is
     * synchronized.
     */
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
    // place viewpoint at starting position, facing into scene
    {
        Vector3d startPosn = land.getOriginVec();
        // System.out.println("startPosn: " + startPosn);
        currLandHeight = startPosn.y; // store current floor height
        startPosn.y += USER_HEIGHT; // add user's height
        steerTG.getTransform(t3d);
        t3d.setTranslation(startPosn); // no targetTG yet, so use steerTG
        steerTG.setTransform(t3d);
    } // end of initViewPosition()

    private void moveBy(Vector3d theMove)
    /*
     * _Eventually_ calculate the next position on the floor (x,?,z). First, do a quick test to see if the move will be within the land boundaries.
     * Then, _request_ the floor height for the given (x,z) position. Don't wait for an answer (which may take 1-2 secs). Instead, continue with the
     * current floor height, which means a move with no change in the y-axis. The height request is sent to the HeightFinder object, which will query
     * the landscape through picking. When it has an answer, it will call KeyBehavior's adjustHeight(). This means that the user may walk through the
     * sides of mountains, but his/her y-position will *eventually* be adjusted. HeightFinder is coded so that when Keybehavior sends a request, it
     * will replace any previously pending request.
     */
    {
        // System.out.println("theMove: " + theMove);
        Vector3d nextLoc = tryMove(theMove); // next (x,?,z) user position
        if (!land.inLandscape(nextLoc.x, nextLoc.z)) {
            return;
        }
        hf.requestMoveHeight(nextLoc); // send height request to HeightFinder
        Vector3d actualMove = new Vector3d(theMove.x, 0, theMove.z); // no
        // y-axis
        // change
        doMove(actualMove);
    } // end of moveBy()

    // ----------------------- moves ----------------------------
    private void printLandLocation()
    // print the user's position in land coordinates
    {
        targetTG.getTransform(t3d);
        t3d.get(trans);
        trans.y -= MOVE_STEP * zOffset; // ignore user floating
        Vector3d whereVec = land.worldToLand(trans);
        System.out.println("Land location: (" + df.format(whereVec.x) + ", " + df.format(whereVec.y) + ", " + df.format(whereVec.z) + ")");
    } // end of printLandLocation()

    private void processKeyEvent(KeyEvent eventKey) {
        int keyCode = eventKey.getKeyCode();
        // System.out.println(keyCode);
        if (eventKey.isAltDown()) {
            altMove(keyCode);
        } else {
            standardMove(keyCode);
        }
    } // end of processKeyEvent()

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

    // -------------- rotation --------------------
    private void standardMove(int keycode)
    /*
     * viewer moves forward or backward; rotate left or right. Also 'w' for where
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
        } else if (keycode == whereKey) {
            printLandLocation();
        }
    } // end of standardMove()

    // --------------- current location ---------------------
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
