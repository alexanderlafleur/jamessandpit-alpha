package AnimTour3D;

// KeyBehavior.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Key presses are caught and passed to an Animator object for
 processing.

 Key presses caught:
 * arrow keys for movement
 * 'i', 'o' for viewer zooming
 * 'a' to toggle the isActive flag for the sprite
 * 'p' for punching
 */

import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnAWTEvent;

public class KeyBehavior extends Behavior {
    private WakeupCondition keyPress;

    private final static int forwardKey = KeyEvent.VK_DOWN;

    private final static int backKey = KeyEvent.VK_UP;

    private final static int leftKey = KeyEvent.VK_LEFT;

    private final static int rightKey = KeyEvent.VK_RIGHT;

    private final static int activeKey = KeyEvent.VK_A;

    private final static int punchKey = KeyEvent.VK_P;

    private final static int inKey = KeyEvent.VK_I; // for moving viewer

    private final static int outKey = KeyEvent.VK_O;

    private Animator animBeh; // animator for the sprite

    public KeyBehavior(Animator ab) {
        this.animBeh = ab;
        this.keyPress = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
    }

    @Override
    public void initialize() {
        wakeupOn(this.keyPress);
    }

    @Override
    public void processStimulus(Enumeration criteria) {
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
     * Make the sprite moves forward or backward; rotate left or right; 'i' and 'o' keys affect the viewer's position; 'a' to toggle the sprite's activity, 'p' for punch;
     */
    {
        if (keycode == forwardKey) {
            this.animBeh.moveForward();
        } else if (keycode == backKey) {
            this.animBeh.moveBackward();
        } else if (keycode == leftKey) {
            this.animBeh.rotClock();
        } else if (keycode == rightKey) {
            this.animBeh.rotCounterClock();
        } else if (keycode == activeKey) {
            this.animBeh.toggleActive();
        } else if (keycode == punchKey) {
            this.animBeh.punch();
        } else if (keycode == inKey) {
            this.animBeh.shiftInViewer();
        } else if (keycode == outKey) {
            this.animBeh.shiftOutViewer();
        }
    } // end of standardMove()

    private void altMove(int keycode)
    // moves left or right
    {
        if (keycode == leftKey) {
            this.animBeh.moveLeft();
        } else if (keycode == rightKey) {
            this.animBeh.moveRight();
        }
    } // end of altMove()

} // end of KeyBehavior class
