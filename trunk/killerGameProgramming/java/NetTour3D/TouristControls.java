package NetTour3D;

// TouristControls.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Arrow keys to move and rotate a 3D sprite.

 We have restricted the functionality to XZ plane movement
 and Y rotation in the sprite, so not all movements
 and rotations need to have keys here.

 The viewpoint is set here, based on the TourSprite's position.
 As the sprite moves, the viewpoint is also moved. 

 The user can press 'i' or 'o' to 'zoom' in or out on the sprite.

 Unchanged from the version in Tour3D.
 */

import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class TouristControls extends Behavior {
    private WakeupCondition keyPress;

    private final static int forwardKey = KeyEvent.VK_DOWN;

    private final static int backKey = KeyEvent.VK_UP;

    private final static int leftKey = KeyEvent.VK_LEFT;

    private final static int rightKey = KeyEvent.VK_RIGHT;

    private final static int inKey = KeyEvent.VK_I; // for moving viewer

    private final static int outKey = KeyEvent.VK_O;

    private final static double HEIGHT = 2.0; // height of viewer

    private final static double ZOFFSET = 8.0; // distance from sprite

    private final static double ZSTEP = 1.0; // viewer's zoom step

    private TourSprite bob; // the 3D sprite being controlled from here

    private TransformGroup viewerTG; // the viewpoint TG

    private Transform3D t3d, toMove; // used to affect viewerTG;

    private Point3d bobPosn;

    private DecimalFormat df; // for simpler output during debugging

    public TouristControls(TourSprite b, TransformGroup vTG) {
        this.df = new DecimalFormat("0.###"); // 3 dp
        this.bob = b;
        this.viewerTG = vTG;
        this.t3d = new Transform3D();
        this.toMove = new Transform3D();
        setViewer();
        this.keyPress = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
    } // end of TouristControls()

    private void setViewer()
    /*
     * Position the viewpoint so it is offset by ZOFFSET from the sprite along the z-axis, and is at height HEIGHT.
     */
    {
        this.bobPosn = this.bob.getCurrLoc(); // start location for bob
        this.viewerTG.getTransform(this.t3d);
        // args are: viewer posn, where looking, up direction
        this.t3d.lookAt(new Point3d(this.bobPosn.x, HEIGHT, this.bobPosn.z + ZOFFSET), new Point3d(this.bobPosn.x, HEIGHT, this.bobPosn.z), new Vector3d(0, 1, 0));
        this.t3d.invert();
        this.viewerTG.setTransform(this.t3d);
    } // end of setViewer()

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

        viewerMove();
    } // end of processKeyEvent()

    private void standardMove(int keycode)
    // make sprite moves forward or backward; rotate left or right
    // the in and out keys affect the viewer's z-axis position
    {
        if (keycode == forwardKey) {
            this.bob.moveForward();
        } else if (keycode == backKey) {
            this.bob.moveBackward();
        } else if (keycode == leftKey) {
            this.bob.rotClock();
        } else if (keycode == rightKey) {
            this.bob.rotCounterClock();
        } else if (keycode == inKey) {
            shiftViewer(-ZSTEP); // move viewer negatively on z-axis
        } else if (keycode == outKey) {
            shiftViewer(ZSTEP);
        }
    } // end of standardMove()

    private void altMove(int keycode)
    // moves sprite left or right
    {
        if (keycode == leftKey) {
            this.bob.moveLeft();
        } else if (keycode == rightKey) {
            this.bob.moveRight();
        }
    } // end of altMove()

    private void shiftViewer(double zDist)
    // move the viewer inwards or outwards
    {
        Vector3d trans = new Vector3d(0, 0, zDist);
        this.viewerTG.getTransform(this.t3d);
        this.toMove.setTranslation(trans); // overwrites previous translation
        this.t3d.mul(this.toMove);
        this.viewerTG.setTransform(this.t3d);
    } // end of viewerMove()

    private void viewerMove()
    // Updates the view point by the translation change of the sprite
    {
        Point3d newLoc = this.bob.getCurrLoc();
        // printTuple(newLoc, "newLoc");
        Vector3d trans = new Vector3d(newLoc.x - this.bobPosn.x, 0, newLoc.z - this.bobPosn.z);
        this.viewerTG.getTransform(this.t3d);
        this.toMove.setTranslation(trans); // overwrites previous translation
        this.t3d.mul(this.toMove);
        this.viewerTG.setTransform(this.t3d);

        this.bobPosn = newLoc; // save for next time
    } // end of viewerMove()

} // end of TouristControls class
