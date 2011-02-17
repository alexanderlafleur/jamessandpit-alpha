package FPShooter3D;

// KeyBehavior.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* Use key presses to move the viewpoint and fire the gun.
 Movement is restricted to: forward, backwards, move left,
 move right, rotate left, rotate right, up, down.

 The behaviour is added to the user's viewpoint. 
 It extends ViewPlatformBehavior so that targetTG is available
 to it, the ViewPlatform's tranform.
 */
import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

import javax.media.j3d.Transform3D;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.behaviors.vp.ViewPlatformBehavior;

public class KeyBehavior extends ViewPlatformBehavior {
    private static final double MOVE_STEP = 0.2;
    private static final Vector3d BACK = new Vector3d(0, 0, MOVE_STEP);
    private static final Vector3d DOWN = new Vector3d(0, -MOVE_STEP, 0);
    // hardwired movement vectors
    private static final Vector3d FWD = new Vector3d(0, 0, -MOVE_STEP);
    private static final Vector3d LEFT = new Vector3d(-MOVE_STEP, 0, 0);
    private static final Vector3d RIGHT = new Vector3d(MOVE_STEP, 0, 0);
    private static final double ROT_AMT = Math.PI / 36.0; // 5 degrees
    private static final Vector3d UP = new Vector3d(0, MOVE_STEP, 0);
    private AmmoManager ammoMan;
    private int backKey = KeyEvent.VK_DOWN;
    private int fireKey = KeyEvent.VK_F; // fire button
    // key names
    private int forwardKey = KeyEvent.VK_UP;
    private WakeupCondition keyPress;
    private int leftKey = KeyEvent.VK_LEFT;
    private int rightKey = KeyEvent.VK_RIGHT;
    // for repeated calcs
    private Transform3D t3d = new Transform3D();
    private Transform3D toMove = new Transform3D();
    private Transform3D toRot = new Transform3D();

    public KeyBehavior(AmmoManager am) {
        ammoMan = am;
        keyPress = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
    } // end of KeyBehavior()

    private void altMove(int keycode)
    // moves viewer up or down, left or right
    {
        if (keycode == forwardKey) {
            doMove(UP);
        } else if (keycode == backKey) {
            doMove(DOWN);
        } else if (keycode == leftKey) {
            doMove(LEFT);
        } else if (keycode == rightKey) {
            doMove(RIGHT);
        }
    } // end of altMove()

    private void doMove(Vector3d theMove) {
        targetTG.getTransform(t3d);
        toMove.setTranslation(theMove);
        t3d.mul(toMove);
        targetTG.setTransform(t3d);
    } // end of doMove()

    @Override
    public void initialize() {
        wakeupOn(keyPress);
    }

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
        targetTG.getTransform(t3d); // targetTG is the ViewPlatform's tranform
        toRot.rotY(radians);
        t3d.mul(toRot);
        targetTG.setTransform(t3d);
    } // end of rotateY()

    private void standardMove(int keycode)
    /*
     * Make viewer moves forward or backward; rotate left or right; fire gun
     */
    {
        if (keycode == forwardKey) {
            doMove(FWD);
        } else if (keycode == backKey) {
            doMove(BACK);
        } else if (keycode == leftKey) {
            rotateY(ROT_AMT);
        } else if (keycode == rightKey) {
            rotateY(-ROT_AMT);
        } else if (keycode == fireKey) {
            ammoMan.fireBeam();
        }
    } // end of standardMove()
} // end of KeyBehavior class
