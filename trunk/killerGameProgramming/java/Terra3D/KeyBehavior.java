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
    private static final double ROT_AMT = Math.PI / 36.0; // 5 degrees

    private static final double MOVE_STEP = 0.2; // was 0.2

    private static final double USER_HEIGHT = 0.5; // of head above the floor

    // hardwired movement vectors
    private static final Vector3d FWD = new Vector3d(0, 0, -MOVE_STEP);

    private static final Vector3d BACK = new Vector3d(0, 0, MOVE_STEP);

    private static final Vector3d LEFT = new Vector3d(-MOVE_STEP, 0, 0);

    private static final Vector3d RIGHT = new Vector3d(MOVE_STEP, 0, 0);

    private static final Vector3d UP = new Vector3d(0, MOVE_STEP, 0);

    private static final Vector3d DOWN = new Vector3d(0, -MOVE_STEP, 0);

    // key names
    private int forwardKey = KeyEvent.VK_UP;

    private int backKey = KeyEvent.VK_DOWN;

    private int leftKey = KeyEvent.VK_LEFT;

    private int rightKey = KeyEvent.VK_RIGHT;

    private int whereKey = KeyEvent.VK_W; // 'where' button

    private WakeupCondition keyPress;

    private Landscape land;

    private HeightFinder hf; // HeightFinder processes height requests

    private double currLandHeight; // floor height at current position (in
    // world coords)

    private int zOffset; // used when moving up/down

    private DecimalFormat df; // for simpler output

    // for repeated calcs
    private Transform3D t3d = new Transform3D();

    private Transform3D toMove = new Transform3D();

    private Transform3D toRot = new Transform3D();

    private Vector3d trans = new Vector3d();

    public KeyBehavior(Landscape ld, TransformGroup steerTG) {
        this.land = ld;
        this.zOffset = 0; // user is standing on the floor at the start
        initViewPosition(steerTG);
        this.df = new DecimalFormat("0.###"); // 3 dp

        this.hf = new HeightFinder(this.land, this);
        this.hf.start();

        this.keyPress = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
    } // end of KeyBehavior()

    private void initViewPosition(TransformGroup steerTG)
    // place viewpoint at starting position, facing into scene
    {
        Vector3d startPosn = this.land.getOriginVec();
        // System.out.println("startPosn: " + startPosn);

        this.currLandHeight = startPosn.y; // store current floor height
        startPosn.y += USER_HEIGHT; // add user's height

        steerTG.getTransform(this.t3d);
        this.t3d.setTranslation(startPosn); // no targetTG yet, so use steerTG
        steerTG.setTransform(this.t3d);
    } // end of initViewPosition()

    @Override
    public void initialize() {
        wakeupOn(this.keyPress);
    }

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
        wakeupOn(this.keyPress);
    } // end of processStimulus()

    private void processKeyEvent(KeyEvent eventKey) {
        int keyCode = eventKey.getKeyCode();
        // System.out.println(keyCode);

        if (eventKey.isAltDown()) {
            altMove(keyCode);
        } else {
            standardMove(keyCode);
        }
    } // end of processKeyEvent()

    private void standardMove(int keycode)
    /*
     * viewer moves forward or backward; rotate left or right. Also 'w' for where
     */
    {
        if (keycode == this.forwardKey) {
            moveBy(FWD);
        } else if (keycode == this.backKey) {
            moveBy(BACK);
        } else if (keycode == this.leftKey) {
            rotateY(ROT_AMT);
        } else if (keycode == this.rightKey) {
            rotateY(-ROT_AMT);
        } else if (keycode == this.whereKey) {
            printLandLocation();
        }
    } // end of standardMove()

    private void altMove(int keycode)
    // moves viewer up or down, left or right
    {
        if (keycode == this.forwardKey) {
            doMove(UP); // no testing using moveBy()
            this.zOffset++;
        } else if (keycode == this.backKey) {
            if (this.zOffset > 0) {
                doMove(DOWN); // no testing using moveBy()
                this.zOffset--;
            }
        } else if (keycode == this.leftKey) {
            moveBy(LEFT);
        } else if (keycode == this.rightKey) {
            moveBy(RIGHT);
        }
    } // end of altMove()

    // ----------------------- moves ----------------------------

    private void moveBy(Vector3d theMove)
    /*
     * _Eventually_ calculate the next position on the floor (x,?,z).
     * 
     * First, do a quick test to see if the move will be within the land boundaries.
     * 
     * Then, _request_ the floor height for the given (x,z) position. Don't wait for an answer (which may take 1-2 secs). Instead, continue with the current floor height, which
     * means a move with no change in the y-axis.
     * 
     * The height request is sent to the HeightFinder object, which will query the landscape through picking. When it has an answer, it will call KeyBehavior's adjustHeight().
     * 
     * This means that the user may walk through the sides of mountains, but his/her y-position will *eventually* be adjusted.
     * 
     * HeightFinder is coded so that when Keybehavior sends a request, it will replace any previously pending request.
     */
    {
        // System.out.println("theMove: " + theMove);
        Vector3d nextLoc = tryMove(theMove); // next (x,?,z) user position
        if (!this.land.inLandscape(nextLoc.x, nextLoc.z)) {
            return;
        }

        this.hf.requestMoveHeight(nextLoc); // send height request to HeightFinder

        Vector3d actualMove = new Vector3d(theMove.x, 0, theMove.z); // no
        // y-axis
        // change
        doMove(actualMove);
    } // end of moveBy()

    public void adjustHeight(double newHeight)
    /*
     * Called by HeightFinder to adjust the viewer's y-axis position. It is synchronized, so that it won't interfere with a call to moveBy() which also adjust's the viewer's
     * position.
     */
    { // System.out.println("adjustHeight: " + newHeight);
        double heightChg = newHeight - this.currLandHeight - (MOVE_STEP * this.zOffset);
        Vector3d upVec = new Vector3d(0, heightChg, 0);

        this.currLandHeight = newHeight; // update current height
        this.zOffset = 0; // back on floor, so no offset
        doMove(upVec);
    } // end of adjustHeight()

    private Vector3d tryMove(Vector3d theMove)
    /*
     * Calculate the effect of the given translation to get the new (x,?, z) coord. Do not update the viewpoint's TG yet
     */
    {
        this.targetTG.getTransform(this.t3d);
        this.toMove.setTranslation(theMove);
        this.t3d.mul(this.toMove);
        this.t3d.get(this.trans);
        return this.trans;
    } // end of tryMove()

    synchronized private void doMove(Vector3d theMove)
    /*
     * Move the viewpoint by theMove offset. This method can be called by KeyBehavior from moveBy() and HeightFinder from adjustHeight(), so is synchronized.
     */
    {
        this.targetTG.getTransform(this.t3d);
        this.toMove.setTranslation(theMove);
        this.t3d.mul(this.toMove);
        this.targetTG.setTransform(this.t3d); // update viewpoint's TG
    } // end of doMove()

    // -------------- rotation --------------------

    private void rotateY(double radians)
    // rotate about y-axis by radians
    {
        this.targetTG.getTransform(this.t3d);
        this.toRot.rotY(radians);
        this.t3d.mul(this.toRot);
        this.targetTG.setTransform(this.t3d);
    } // end of rotateY()

    // --------------- current location ---------------------

    private void printLandLocation()
    // print the user's position in land coordinates
    {
        this.targetTG.getTransform(this.t3d);
        this.t3d.get(this.trans);
        this.trans.y -= MOVE_STEP * this.zOffset; // ignore user floating
        Vector3d whereVec = this.land.worldToLand(this.trans);

        System.out.println("Land location: (" + this.df.format(whereVec.x) + ", " + this.df.format(whereVec.y) + ", " + this.df.format(whereVec.z) + ")");
    } // end of printLandLocation()

} // end of KeyBehavior class
