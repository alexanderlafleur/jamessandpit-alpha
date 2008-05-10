package Terra3D;

// HeightFinder.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* HeightFinder was designed to hide the slowness of picking
 for such a large object (the mesh loaded from the landscape's 
 OBJ file).

 Picking is carried out by getLandHeight(), applied to the 
 landBG BranchGroup. getLandHeight() may take 1-2 seconds to
 complete.

 KeyBehaviour interacts with HeightFinder by calling 
 requestMoveHeight() which stores a (x,y,z) coordinate in theMove. 
 The y-value is a dummy, but the
 (x,z) pair will be used by getLandHeight() to get the y-height at 
 that point.

 requestMoveHeight() returns immediately; KeyBehavior does not wait
 for an answer, or it would be waiting for 1-2 seconds, maybe more.
 Instead, KeyBehavior simply uses the current y-height for its move.

 HeightFinder's run() method constantly loops: it reads the current
 move request from theMove, then calls getLandHeight(). At the end
 of getLandHeight(), the new height is passed back to KeyBehavior by
 calling its adjustHeight() method.

 This approach decouples KeyBehavior from the slow picking operation.
 The downside is that KeyBehavior will use an out-of-date height
 until told a new height by HeightFinder.

 KeyBehavior may send many move requests to HeightFinder while 
 getLandHeight() is slowly churing away, which is fine. When 
 getLandHeight() returns, run() will get the most recent request 
 from theMove and start picking again.

 This approach means that KeyBehavior can deal with very fast user
 keypresses, even though picking is slow. 
 */
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.picking.PickIntersection;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.PickTool;

public class HeightFinder extends Thread {
    private final static Vector3d DOWN_VEC = new Vector3d(0.0, -1.0, 0.0);
    private boolean calcRequested;
    private KeyBehavior keyBeh;
    private Landscape land;
    private PickTool picker; // used to pick into the landscape
    private double scaleLen;
    private Vector3d theMove; // holds the current move request from

    // KeyBehavior
    public HeightFinder(Landscape ld, KeyBehavior kb) {
        land = ld;
        keyBeh = kb;
        picker = new PickTool(land.getLandBG()); // only check the landscape
        picker.setMode(PickTool.GEOMETRY_INTERSECT_INFO);
        scaleLen = land.getScaleLen();
        theMove = new Vector3d();
        calcRequested = false;
    } // end of heightFinder()

    private void getLandHeight(double x, double z)
    /*
     * Picking is a bit flakey, especially near edges and corners, so if no PickResult is found, no intersections found, or the extraction of
     * intersection coords fails, then keyBeh.adjustHeight() is not called. pickStart and the pick ray use world coordinates, but the intersection
     * info is returned in landscape coords (floor is XY, height is Z). This means that the height must be converted to world coordinates (i.e.
     * scaled) before being sent to KeyBehavior.
     */
    {
        Point3d pickStart = new Point3d(x, 2000, z); // high up in world
        // coords
        picker.setShapeRay(pickStart, DOWN_VEC); // shoot a ray downwards in
        // world coords
        PickResult picked = picker.pickClosest();
        // System.out.println("picked value: " + picked);
        if (picked != null) { // pick sometimes misses at an edge/corner
            if (picked.numIntersections() != 0) { // sometimes no intersects
                // are found
                PickIntersection pi = picked.getIntersection(0);
                Point3d nextPt;
                try { // handles 'Interp point outside quad' error
                    nextPt = pi.getPointCoordinates();
                    // System.out.println("Picked: " + nextPt);
                } catch (Exception e) {
                    System.out.println(e);
                    return; // don't talk to KeyBehavior since no height was
                    // found
                }
                double nextYHeight = nextPt.z * scaleLen; // z-axis land -->
                // y-axis world
                // System.out.println("nextYHeight: " + nextYHeight);
                keyBeh.adjustHeight(nextYHeight); // tell KeyBehavior the new
                // height
            }
        }
    } // end of getLandHeight()

    synchronized private Vector3d getMove()
    /*
     * getLandHeight() gets the move request. A synchronized method is used so that the get cannot be mixed with a set if Keybehavior happens to call
     * requestMoveHeight() at the same time.
     */
    {
        calcRequested = false;
        return new Vector3d(theMove.x, theMove.y, theMove.z);
    }

    synchronized public void requestMoveHeight(Vector3d mv)
    // KeyBehaviour sends a new move request
    {
        theMove.set(mv.x, mv.y, mv.z); // this will overwrite any pending
        // request
        calcRequested = true;
    }

    @Override
    public void run()
    /*
     * Repeatedly get a move request and process it by picking. If there is no pending request (calcRequested == false), then sleep for a short time.
     */
    {
        Vector3d vec;
        while (true) {
            if (calcRequested) {
                vec = getMove(); // get the requested move
                getLandHeight(vec.x, vec.z); // pick with it
            } else { // no pending request
                try {
                    Thread.sleep(200); // sleep a little
                } catch (InterruptedException e) {
                }
            }
        }
    } // end of run()
} // end of HeightFinder class
