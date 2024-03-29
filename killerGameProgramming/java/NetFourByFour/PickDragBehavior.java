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
    private BranchGroup bg; // used for picking
    private TransformGroup boardTG; // used to move the board
    private OverlayCanvas canvas3D;
    private NetFourByFour fbf;
    private Transform3D imWorldT3d; // for image plate-->world transform
    private boolean isStartDrag; // to signal the start of dragging
    private Transform3D modelTrans; // stores the total rotation of the board
    private WakeupOr mouseCriterion;
    private WakeupCriterion[] mouseEvents;
    private Point3d mousePos;
    private SceneGraphPath nodePath;
    private PickRay pickRay = new PickRay();
    private Transform3D transformX, transformY; // for repeated calcs.
    private int xPrev, yPrev; // coords used while dragging

    public PickDragBehavior(OverlayCanvas c3D, NetFourByFour fbf, BranchGroup bg, TransformGroup boardTG) {
        canvas3D = c3D;
        this.fbf = fbf;
        this.bg = bg;
        this.boardTG = boardTG;
        modelTrans = new Transform3D();
        transformX = new Transform3D();
        transformY = new Transform3D();
        mousePos = new Point3d();
        imWorldT3d = new Transform3D();
        xPrev = 0;
        yPrev = 0;
        isStartDrag = true;
    } // end of PickDragBehavior()

    @Override
    public void initialize() {
        mouseEvents = new WakeupCriterion[3];
        mouseEvents[0] = new WakeupOnAWTEvent(MouseEvent.MOUSE_DRAGGED);
        mouseEvents[1] = new WakeupOnAWTEvent(MouseEvent.MOUSE_PRESSED);
        mouseEvents[2] = new WakeupOnAWTEvent(MouseEvent.MOUSE_RELEASED);
        mouseCriterion = new WakeupOr(mouseEvents);
        wakeupOn(mouseCriterion);
    } // end of initialize()

    private void processDrag(int xPos, int yPos)
    /*
     * Convert the distance dragged into a X- and Y- rotation. Include these in the model translation and apply to the top-level transformGroup
     * boardTG.
     */
    {
        if (isStartDrag) {
            isStartDrag = false;
        } else { // not the start of a drag, so can calculate offset
            int dx = xPos - xPrev; // get distances dragged
            int dy = yPos - yPrev;
            transformX.rotX(dy * YFACTOR); // convert to rotations
            transformY.rotY(dx * XFACTOR);
            modelTrans.mul(transformX, modelTrans); // include with existing
            // rotations
            modelTrans.mul(transformY, modelTrans);
            boardTG.setTransform(modelTrans); // apply to transform group
        }
        xPrev = xPos; // save loc so can work out drag next time
        yPrev = yPos;
    } // end of processDrag()

    private void processPress(int xPos, int yPos)
    /*
     * Send a pick ray into the world starting from the mouse press position. Get the closest intersecting node, and accept the first Shape3D as the
     * selected board position.
     */
    {
        canvas3D.getPixelLocationInImagePlate(xPos, yPos, mousePos);
        // get the mouse position on the image plate
        canvas3D.getImagePlateToVworld(imWorldT3d);
        // get image plate --> world transform
        imWorldT3d.transform(mousePos); // convert mousePos to world coords
        pickRay.set(mousePos, IN_VEC);
        // ray starts at mouse pos, and goes straight in
        nodePath = bg.pickClosest(pickRay); // get 1st node along pickray (and
        // its path)
        // System.out.println("nodePath: " + nodePath);
        if (nodePath != null) {
            selectedPosn(nodePath);
        }
    } // end of processPress()

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
                        isStartDrag = true; // may be end of a drag
                    }
                }
            }
        }
        wakeupOn(mouseCriterion);
    } // end of processStimulus()

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
                fbf.tryMove(posID.intValue()); // talk to top-level
            }
        }
    } // end of selectedPosn()
} // end of PickDragBehavior class
