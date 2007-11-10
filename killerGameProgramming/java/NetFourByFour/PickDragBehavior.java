package NetFourByFour;

// PickDragBehavior.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* PickDragBehavior.java 1.8 01/01/11 07:32:11
 * Copyright (c) 1996-2001 Sun Microsystems, Inc. All Rights Reserved.

 Respond to mouse pick and drag events on the 3D board

 If a position was selected on the board, pass that position
 to the Board object for evaluation by calling tryPosn().

 If the mouse was dragged, then rotate the board.

 Mouse releases are also caught, so that dragging doesn't
 inadvertantly use a mouse position from an earlier drag
 that has finished.

 Changes for net version:
 * call tryMove() in top-level rather than tryPosn()
 in Board when executing selectedPosn()
 */

import java.awt.AWTEvent;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Node;
import javax.media.j3d.PickRay;
import javax.media.j3d.SceneGraphPath;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.media.j3d.WakeupOr;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class PickDragBehavior extends Behavior {
    private final static Vector3d IN_VEC = new Vector3d(0.f, 0.f, -1.f);

    // direction for picking -- into the scene

    private final static double XFACTOR = 0.02; // for rotations

    private final static double YFACTOR = 0.02;

    private WakeupCriterion[] mouseEvents;

    private WakeupOr mouseCriterion;

    private int xPrev, yPrev; // coords used while dragging

    private boolean isStartDrag; // to signal the start of dragging

    private Transform3D modelTrans; // stores the total rotation of the board

    private Transform3D transformX, transformY; // for repeated calcs.

    private TransformGroup boardTG; // used to move the board

    private BranchGroup bg; // used for picking

    private OverlayCanvas canvas3D;

    private NetFourByFour fbf;

    private Point3d mousePos;

    private Transform3D imWorldT3d; // for image plate-->world transform

    private PickRay pickRay = new PickRay();

    private SceneGraphPath nodePath;

    public PickDragBehavior(OverlayCanvas c3D, NetFourByFour fbf, BranchGroup bg, TransformGroup boardTG) {
        this.canvas3D = c3D;
        this.fbf = fbf;
        this.bg = bg;
        this.boardTG = boardTG;

        this.modelTrans = new Transform3D();
        this.transformX = new Transform3D();
        this.transformY = new Transform3D();

        this.mousePos = new Point3d();
        this.imWorldT3d = new Transform3D();

        this.xPrev = 0;
        this.yPrev = 0;
        this.isStartDrag = true;
    } // end of PickDragBehavior()

    @Override
    public void initialize() {
        this.mouseEvents = new WakeupCriterion[3];
        this.mouseEvents[0] = new WakeupOnAWTEvent(MouseEvent.MOUSE_DRAGGED);
        this.mouseEvents[1] = new WakeupOnAWTEvent(MouseEvent.MOUSE_PRESSED);
        this.mouseEvents[2] = new WakeupOnAWTEvent(MouseEvent.MOUSE_RELEASED);
        this.mouseCriterion = new WakeupOr(this.mouseEvents);
        wakeupOn(this.mouseCriterion);
    } // end of initialize()

    @Override
    public void processStimulus(Enumeration criteria) {
        WakeupCriterion wakeup;
        AWTEvent[] event;
        int id;
        int xPos, yPos;
        while (criteria.hasMoreElements()) {
            wakeup = (WakeupCriterion) criteria.nextElement();
            if (wakeup instanceof WakeupOnAWTEvent) {
                event = ((WakeupOnAWTEvent) wakeup).getAWTEvent();
                for (AWTEvent element : event) {
                    xPos = ((MouseEvent) element).getX();
                    yPos = ((MouseEvent) element).getY();
                    id = element.getID();
                    if (id == MouseEvent.MOUSE_DRAGGED) {
                        processDrag(xPos, yPos);
                    } else if (id == MouseEvent.MOUSE_PRESSED) {
                        processPress(xPos, yPos);
                    } else if (id == MouseEvent.MOUSE_RELEASED) {
                        this.isStartDrag = true; // may be end of a drag
                    }
                }
            }
        }
        wakeupOn(this.mouseCriterion);
    } // end of processStimulus()

    private void processDrag(int xPos, int yPos)
    /*
     * Convert the distance dragged into a X- and Y- rotation. Include these in the model translation and apply to the top-level transformGroup boardTG.
     */
    {
        if (this.isStartDrag) {
            this.isStartDrag = false;
        } else { // not the start of a drag, so can calculate offset
            int dx = xPos - this.xPrev; // get distances dragged
            int dy = yPos - this.yPrev;
            this.transformX.rotX(dy * YFACTOR); // convert to rotations
            this.transformY.rotY(dx * XFACTOR);
            this.modelTrans.mul(this.transformX, this.modelTrans); // include with existing
            // rotations
            this.modelTrans.mul(this.transformY, this.modelTrans);
            this.boardTG.setTransform(this.modelTrans); // apply to transform group
        }

        this.xPrev = xPos; // save loc so can work out drag next time
        this.yPrev = yPos;
    } // end of processDrag()

    private void processPress(int xPos, int yPos)
    /*
     * Send a pick ray into the world starting from the mouse press position. Get the closest intersecting node, and accept the first Shape3D as the selected board position.
     */
    {
        this.canvas3D.getPixelLocationInImagePlate(xPos, yPos, this.mousePos);
        // get the mouse position on the image plate
        this.canvas3D.getImagePlateToVworld(this.imWorldT3d);
        // get image plate --> world transform
        this.imWorldT3d.transform(this.mousePos); // convert mousePos to world coords

        this.pickRay.set(this.mousePos, IN_VEC);
        // ray starts at mouse pos, and goes straight in

        this.nodePath = this.bg.pickClosest(this.pickRay); // get 1st node along pickray (and
        // its path)
        // System.out.println("nodePath: " + nodePath);
        if (this.nodePath != null) {
            selectedPosn(this.nodePath);
        }
    } // end of processPress()

    private void selectedPosn(SceneGraphPath np)
    /*
     * Find selected board position, if one exists, and pass it to the board for processing.
     */
    {
        Node node = np.getObject(); // get terminal node of path
        if (node instanceof Shape3D) { // check for shape3D
            Integer posID = (Integer) node.getUserData(); // get posn index
            if (posID != null) {
                // board.tryPosn( posID.intValue() );
                this.fbf.tryMove(posID.intValue()); // talk to top-level
            }
        }
    } // end of selectedPosn()

} // end of PickDragBehavior class
