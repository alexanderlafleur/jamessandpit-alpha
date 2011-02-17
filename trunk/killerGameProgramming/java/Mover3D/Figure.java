package Mover3D;

// Figure.java
// Thana Konglikhit, October 2003, s4310170@maliwan.psu.ac.th
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* The Figure class carries out three main tasks:
 1. constructs the figure by connecting Limb objects;

 2. processes limb commands by passing them 
 onto the relavant Limb objects

 3. processes figure commands by moving or rotating
 the TransformGroup for the figure (figureTG).

 The Limb hierarchy:

 Limb
 |---------------------
 |                    |             
 MoveableLimb         EllipticLimb
 |
 |
 MoveableEllipticLimb


 Limb defines the appearance of a limb (using a Lathe shape), 
 and how it is connected to other limbs via joints. 
 The limb's initial orientation is set. Limb and EllipticLimb
 cannot be moved, and do not use limb names.

 The MoveableLimb and MoveableEllipticLimb classes are moveable.

 They have limb names, and x-, y-, z- axis ranges in which they
 can be rotated. If a range is not specified, then it is assumed
 to be 0 (i.e. rotation is not possible around that axis).


 The figure's component limb objects are stored in the limbs
 ArrayList. A limb object is stored in the ArrayList at the
 position specified by its limb number.

 A limbNames hashmap stores (limb name, no.) pairs. 
 It is used to determine a limb's number when a limb name is 
 supplied by the user in a limb command.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;

public class Figure {
    // figure movement constants
    private final static double MOVERATE = 0.3;
    private final static int BACK = 1;
    private static final Vector3d backVec = new Vector3d(0, 0, -MOVERATE);
    private final static int CCLOCK = 1; // counter clockwise
    private final static int CLOCK = 0; // clockwise turn
    private final static int DOWN = 5;
    private static final Vector3d downVec = new Vector3d(0, -MOVERATE, 0);
    private final static int FWD = 0;
    // step to move in each direction
    private static final Vector3d fwdVec = new Vector3d(0, 0, MOVERATE);
    private final static int LEFT = 2;
    private static final Vector3d leftVec = new Vector3d(-MOVERATE, 0, 0);
    private final static int RIGHT = 3;
    private static final Vector3d rightVec = new Vector3d(MOVERATE, 0, 0);
    private final static double ROTATE_AMT = Math.PI / 16.0; // 11.25 degrees
    private final static int UP = 4;
    private static final Vector3d upVec = new Vector3d(0, MOVERATE, 0);
    // axis constants
    private final static int X_AXIS = 0;
    private final static int Z_AXIS = 2;
    // for moving the figure as a whole
    private TransformGroup figureTG;
    // Arraylist of Limb objects, indexed by limb number
    private HashMap limbNames;
    // holds (name, limbNo) pairs of visible limbs
    private ArrayList limbs;
    private Transform3D t3d, toMove, toRot; // for TG operations
    private int yCount; // used to position the figure along the y-axis

    public Figure() {
        yCount = 0; // the figure is on the floor initially
        t3d = new Transform3D();
        toMove = new Transform3D();
        toRot = new Transform3D();
        limbs = new ArrayList();
        limbNames = new HashMap();
        // construct the figure in parts (connected Limb objects)
        buildTorso();
        buildHead();
        buildRightArm();
        buildLeftArm();
        buildRightLeg();
        buildLeftLeg();
        printLimbsInfo();
        buildFigureGraph(); // convert the figure into a Java 3D graph
    } // end of Figure()

    private void buildFigureGraph()
    /*
     * Convert the figure into a Java 3D subgraph, stored in figureTG. offsetTG is connected to figureTG; it specifies the distance from the floor to
     * the first joint and limb. The figure's graph will hang below offsetTG.
     */
    {
        HashMap joints = new HashMap();
        /*
         * joints will contain (jointName, TG) pairs. Each TG is the position of the joint in the scene. A limb connected to a joint is placed in the
         * scene by using the TG associated with that joint.
         */
        figureTG = new TransformGroup();
        figureTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        figureTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE); // can
        // alter
        TransformGroup offsetTG = new TransformGroup();
        Transform3D trans = new Transform3D();
        trans.setTranslation(new Vector3d(0, 1.24, 0));
        // an offset from the ground to the first joint
        offsetTG.setTransform(trans);
        joints.put("j0", offsetTG); // store starting joint (assume it is j0)
        /*
         * Grow the subgraph for each limb object, attaching it to the figure's subgraph below offsetTG.
         */
        Limb li;
        for (int i = 0; i < limbs.size(); i++) {
            li = (Limb) limbs.get(i);
            li.growLimb(joints);
        }
        figureTG.addChild(offsetTG);
    } // end of buildFigureGraph()

    private void buildHead() {
        // figure's neck
        double xsIn3[] = { -0.001, -0.09, -0.09, 0.001 };
        double ysIn3[] = { 0, 0, 0.1, 0.1 };
        Limb limb3 = new Limb(3, "j2", "j3", Z_AXIS, 0, xsIn3, ysIn3, "skin.jpg");
        // no movement
        // figure's head
        double xsIn4[] = { -0.001, 0.09, 0.17, 0 };
        double ysIn4[] = { 0, 0, 0.2, 0.4 };
        MoveableLimb limb4 = new MoveableLimb("head", 4, "j3", "j4", Z_AXIS, 0, xsIn4, ysIn4, "head.jpg");
        limb4.setRanges(-40, 40, -40, 40, -30, 30);
        limbs.add(limb3);
        limbs.add(limb4);
        limbNames.put("head", new Integer(4));
    } // end of buildHead()

    private void buildLeftArm()
    // very similar to buildRightArm()
    {
        // invisible limb connecting the neck and upper left arm
        Limb limb9 = new Limb(9, "j2", "j9", Z_AXIS, -95, 0.35);
        // upper left arm
        double xsIn10[] = { 0, 0.1, 0.08, 0 };
        double ysIn10[] = { 0, 0.08, 0.45, 0.55 };
        MoveableLimb limb10 = new MoveableLimb("ulArm", 10, "j9", "j10", Z_AXIS, -80, xsIn10, ysIn10, "leftarm.jpg");
        limb10.setRanges(-60, 180, -90, 90, -30, 90);
        // lower left arm
        double xsIn11[] = { 0, 0.08, 0.055, 0 };
        double ysIn11[] = { 0, 0.08, 0.38, 0.43 };
        MoveableLimb limb11 = new MoveableLimb("llArm", 11, "j10", "j11", Z_AXIS, -5, xsIn11, ysIn11, "skin.jpg");
        limb11.setRanges(0, 150, -90, 90, -90, 90);
        // left hand
        double xsIn12[] = { 0, 0.06, 0.04, 0 };
        double ysIn12[] = { 0, 0.07, 0.16, 0.2 };
        MoveableEllipticLimb limb12 = new MoveableEllipticLimb("lHand", 12, "j11", "j12", Z_AXIS, 0, xsIn12, ysIn12, "skin.jpg");
        limb12.setRanges(-50, 50, -90, 40, -40, 40);
        limbs.add(limb9);
        limbs.add(limb10);
        limbs.add(limb11);
        limbs.add(limb12);
        limbNames.put("ulArm", new Integer(10));
        limbNames.put("llArm", new Integer(11));
        limbNames.put("lHand", new Integer(12));
    } // end of buildLeftArm()

    private void buildLeftLeg()
    // very similar to buildRightLeg()
    {
        // invisible limb connecting the bottom and upper left leg
        Limb limb18 = new Limb(18, "j0", "j18", Z_AXIS, -50, 0.20);
        // upper left leg
        double xsIn19[] = { 0, 0.12, 0.1, 0 };
        double ysIn19[] = { 0, 0.1, 0.6, 0.7 };
        MoveableLimb limb19 = new MoveableLimb("ulLeg", 19, "j18", "j19", Z_AXIS, -130, xsIn19, ysIn19, "denim.jpg");
        limb19.setRanges(-45, 80, -20, 20, -30, 45);
        // lower left leg
        double xsIn20[] = { 0, 0.10, 0.06, 0 };
        double ysIn20[] = { 0, 0.15, 0.62, 0.7 };
        MoveableLimb limb20 = new MoveableLimb("llLeg", 20, "j19", "j20", Z_AXIS, 0, xsIn20, ysIn20, "lowDenim.jpg");
        limb20.setRange(X_AXIS, -120, 0);
        // invisible limb connecting between lower left leg and left foot
        Limb limb21 = new Limb(21, "j20", "j21", Z_AXIS, 0, 0.07);
        // left foot
        double xsIn22[] = { 0, 0.08, 0.06, 0 };
        double ysIn22[] = { 0, 0.07, 0.21, 0.25 };
        MoveableEllipticLimb limb22 = new MoveableEllipticLimb("lFoot", 22, "j21", "j22", X_AXIS, 90, xsIn22, ysIn22, "shoes.jpg");
        limb22.setRanges(-90, 0, 0, 0, -30, 30);
        limbs.add(limb18);
        limbs.add(limb19);
        limbs.add(limb20);
        limbs.add(limb21);
        limbs.add(limb22);
        limbNames.put("ulLeg", new Integer(19));
        limbNames.put("llLeg", new Integer(20));
        limbNames.put("lFoot", new Integer(22));
    } // end of buildLeftLeg()

    private void buildRightArm() {
        /*
         * An invisible limb connecting the neck and upper right arm. It is invisible since no lathe shape coords or texture are supplied. However, it
         * does have a length (0.35).
         */
        Limb limb5 = new Limb(5, "j2", "j5", Z_AXIS, 95, 0.35);
        // upper right arm
        double xsIn6[] = { 0, 0.1, 0.08, 0 };
        double ysIn6[] = { 0, 0.08, 0.45, 0.55 };
        MoveableLimb limb6 = new MoveableLimb("urArm", 6, "j5", "j6", Z_AXIS, 80, xsIn6, ysIn6, "rightarm.jpg");
        limb6.setRanges(-60, 180, -90, 90, -90, 30);
        // lower right arm
        double xsIn7[] = { 0, 0.08, 0.055, 0 };
        double ysIn7[] = { 0, 0.08, 0.38, 0.43 };
        MoveableLimb limb7 = new MoveableLimb("lrArm", 7, "j6", "j7", Z_AXIS, 5, xsIn7, ysIn7, "skin.jpg");
        limb7.setRanges(0, 150, -90, 90, -90, 90);
        // right hand
        double xsIn8[] = { 0, 0.06, 0.04, 0 };
        double ysIn8[] = { 0, 0.07, 0.16, 0.2 };
        MoveableEllipticLimb limb8 = new MoveableEllipticLimb("rHand", 8, "j7", "j8", Z_AXIS, 0, xsIn8, ysIn8, "skin.jpg");
        limb8.setRanges(-50, 50, -40, 90, -40, 40);
        limbs.add(limb5);
        limbs.add(limb6);
        limbs.add(limb7);
        limbs.add(limb8);
        limbNames.put("urArm", new Integer(6));
        limbNames.put("lrArm", new Integer(7));
        limbNames.put("rHand", new Integer(8));
    } // end of buildRightArm()

    private void buildRightLeg() {
        // invisible limb connecting the bottom and upper right leg
        Limb limb13 = new Limb(13, "j0", "j13", Z_AXIS, 50, 0.20);
        // upper right leg
        double xsIn14[] = { 0, 0.12, 0.1, 0 };
        double ysIn14[] = { 0, 0.1, 0.6, 0.7 };
        MoveableLimb limb14 = new MoveableLimb("urLeg", 14, "j13", "j14", Z_AXIS, 130, xsIn14, ysIn14, "denim.jpg");
        limb14.setRanges(-45, 80, -20, 20, -45, 30);
        // lower right leg
        double xsIn15[] = { 0, 0.10, 0.06, 0 };
        double ysIn15[] = { 0, 0.15, 0.62, 0.7 };
        MoveableLimb limb15 = new MoveableLimb("lrLeg", 15, "j14", "j15", Z_AXIS, 0, xsIn15, ysIn15, "lowDenim.jpg");
        limb15.setRange(X_AXIS, -120, 0);
        // invisible limb connecting lower right leg and right foot
        Limb limb16 = new Limb(16, "j15", "j16", Z_AXIS, 0, 0.07);
        // right foot
        double xsIn17[] = { 0, 0.08, 0.06, 0 };
        double ysIn17[] = { 0, 0.07, 0.21, 0.25 };
        MoveableEllipticLimb limb17 = new MoveableEllipticLimb("rFoot", 17, "j16", "j17", X_AXIS, 90, xsIn17, ysIn17, "shoes.jpg");
        limb17.setRanges(-90, 0, 0, 0, -30, 30);
        limbs.add(limb13);
        limbs.add(limb14);
        limbs.add(limb15);
        limbs.add(limb16);
        limbs.add(limb17);
        limbNames.put("urLeg", new Integer(14));
        limbNames.put("lrLeg", new Integer(15));
        limbNames.put("rFoot", new Integer(17));
    } // end of buildRightLeg()

    private void buildTorso()
    /*
     * A limb requires a limb number, its start and end joints, an axis of orientation and angle to that axis, and lathe shape coordinates and
     * texture. If the limb will be moveable (i.e. a MoveableLimb or MoveableEllipticLimb object), then it also requires a name and x-, y-, z- ranges
     * to restrict its movements.
     */
    {
        // the figure's bottom
        double xsIn1[] = { 0, -0.1, 0.22, -0.2, 0.001 };
        double ysIn1[] = { 0, 0.03, 0.08, 0.25, 0.25 };
        EllipticLimb limb1 = new EllipticLimb(1, "j0", "j1", Z_AXIS, 0, xsIn1, ysIn1, "denim.jpg");
        // no movement, so no name or ranges
        // the figure's chest: moveable so has a name ("chest") and rotation
        // ranges
        double xsIn2[] = { -0.001, -0.2, 0.36, 0.001 };
        double ysIn2[] = { 0, 0, 0.50, 0.68 };
        MoveableEllipticLimb limb2 = new MoveableEllipticLimb("chest", 2, "j1", "j2", Z_AXIS, 0, xsIn2, ysIn2, "camoflage.jpg");
        limb2.setRanges(0, 120, -60, 60, -40, 40);
        // x range: 0 to 120; y range: -60 to 60; z range: -40 to 40
        limbs.add(limb1);
        limbs.add(limb2);
        limbNames.put("chest", new Integer(2));
        // store (name,number) pairs in limbNames
    } // end of buildTorso()

    public int checkLimbNo(int no)
    // Is no a limb number?
    {
        if (limbNames.containsValue(new Integer(no))) {
            return no;
        } else {
            return -1; // no not found in limbNames
        }
    } // end of checkLimbNo()

    public void doMove(int dir)
    /*
     * Move the figure forwards, backwards, left, right, up, or down. doMove() can be called from LocationBeh or CommandsPanel.
     */
    {
        if (dir == FWD) {
            doMoveVec(fwdVec);
        } else if (dir == BACK) {
            doMoveVec(backVec);
        } else if (dir == LEFT) {
            doMoveVec(leftVec);
        } else if (dir == RIGHT) {
            doMoveVec(rightVec);
        } else if (dir == UP) {
            doMoveVec(upVec);
            yCount++;
        } else if (dir == DOWN) {
            if (yCount > 0) {
                doMoveVec(downVec);
                yCount--;
            }
        } else {
            System.out.println("Unknown doMove() call");
        }
    } // end of doMove()

    // ----------------- limb-related operations ---------------------
    private void doMoveVec(Vector3d theMove)
    // Move the figure by the amount in theMove
    {
        figureTG.getTransform(t3d);
        toMove.setTranslation(theMove); // overwrite previous trans
        t3d.mul(toMove);
        figureTG.setTransform(t3d);
    } // end of doMoveVec()

    public void doRotateY(int rotDir)
    /*
     * Rotate the figure clockwise ot counter-clockwise around the y-axis. doRotateY() can be called from LocationBeh or CommandsPanel.
     */
    {
        if (rotDir == CLOCK) {
            doRotateYRad(-ROTATE_AMT); // clockwise
        } else if (rotDir == CCLOCK) {
            doRotateYRad(ROTATE_AMT); // counter-clockwise
        } else {
            System.out.println("Unknown doRotateY() call");
        }
    } // end of doRotateY()

    private void doRotateYRad(double radians)
    // Rotate the figure by radians amount around its y-axis
    {
        figureTG.getTransform(t3d);
        toRot.rotY(radians); // overwrite previous rotation
        t3d.mul(toRot);
        figureTG.setTransform(t3d);
    } // end of doRotateYRad()

    public int findLimbNo(String name)
    // Find the limb number for this name
    {
        Integer iNo = (Integer) limbNames.get(name);
        if (iNo == null) {
            return -1;
        } else {
            return iNo.intValue();
        }
    } // end of findLimbNo()

    // ------------------- figure movement operations ------------
    public TransformGroup getFigureTG() {
        return figureTG;
    }

    private void printLimbsInfo()
    /*
     * Used for debugging. Print limb info for each Limb object, and the name->number limb mapping.
     */
    {
        /*
         * for(int i = 0; i < limbs.size(); i++) ((Limb)limbs.get(i)).printLimb();
         */
        int i = 0;
        Iterator it = limbNames.keySet().iterator();
        while (it.hasNext()) {
            String lNm = (String) it.next();
            System.out.print("(" + lNm + " = " + limbNames.get(lNm) + ") ");
            i++;
            if (i == 5) { // max number of elements on a line
                System.out.println();
                i = 0;
            }
        }
        System.out.println();
    } // end of printLimbsInfo()

    public void reset()
    // restore each limb to its original position in space
    {
        Limb li;
        for (int i = 0; i < limbs.size(); i++) {
            li = (Limb) limbs.get(i);
            li.reset();
        }
    } // end of reset()

    public void updateLimb(int limbNo, int axis, double angle)
    /*
     * Apply the limb operation to the limb with limbNo. The operation is a rotation of angle degress around axis.
     */
    {
        Limb li = (Limb) limbs.get(limbNo - 1); // assume limbNo input is
        // correct
        li.updateLimb(axis, angle); // send axis and angle
    } // end of updateLimb()
} // end of Figure class
