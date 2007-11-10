package NetTour3D;

// Sprite3D.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Sprite3D loads a 3D image, placing it at (0,0,0). 
 We assume that the object's actual position is (0,0) in the XZ plane. 
 The Y position will vary but probably the base of the object is 
 touching the XZ plane.

 Movements are restricted to the XZ plane and rotations
 around the Y-axis.

 An object cannot move off the floor, or travel through obstacles 
 (as defined in the Obstacles object).

 Very sinmilar to Sprite3D in Tour3D. 
 New net-related code:
 - a userName above the sprite that stays oriented along +z axis;
 - return a detachable branchgroup for the scene;
 - store the current rotation around the y-axis, and methods to return
 and set it.
 */

import java.awt.Font;
import java.text.DecimalFormat;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Font3D;
import javax.media.j3d.FontExtrusion;
import javax.media.j3d.Material;
import javax.media.j3d.OrientedShape3D;
import javax.media.j3d.Switch;
import javax.media.j3d.Text3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class Sprite3D {
    private final static double OBS_FACTOR = 0.5;

    // used to reduce radius of bounding sphere around Sprite
    // when testing for intersection with obstacles

    private BranchGroup objectBG; // for adding/removing from scene

    private TransformGroup objectTG; // TG which the loaded object hangs off

    private Transform3D t3d, toMove, toRot; // for manipulating objectTG's
    // transform

    private Switch visSwitch; // to make object visible/invisible

    private double radius;

    private boolean isActive; // is the sprite active?

    private double currRotation;

    private Obstacles obs; // obstacles in the way of an object

    private DecimalFormat df; // for simpler output during debugging

    public Sprite3D(String userName, String fnm, Obstacles obs) {
        this.df = new DecimalFormat("0.###"); // 3 dp
        this.obs = obs;
        this.currRotation = 0.0;

        // load object and coords
        PropManager propMan = new PropManager(fnm, true);

        // get objLoc and radius of loaded object
        // hopefully objLoc is close to (0, ??, 0)
        Vector3d objLoc = propMan.getLoc();
        this.radius = propMan.getScale(); // assume radius == scale
        // printTuple(objLoc, "objLoc");
        // System.out.println("radius: " + df.format(radius));

        // create switch for visibility
        this.visSwitch = new Switch();
        this.visSwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);
        this.visSwitch.addChild(propMan.getTG()); // add object to switch
        this.visSwitch.setWhichChild(Switch.CHILD_ALL); // make visible

        // create a transform group for the object and its name
        this.objectTG = new TransformGroup();
        this.objectTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        this.objectTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        this.objectTG.addChild(this.visSwitch);
        this.objectTG.addChild(makeName(userName)); // object's name and TG

        // branch group for object
        this.objectBG = new BranchGroup();
        this.objectBG.setCapability(BranchGroup.ALLOW_DETACH);
        this.objectBG.addChild(this.objectTG);

        this.t3d = new Transform3D();
        this.toMove = new Transform3D();
        this.toRot = new Transform3D();
        this.isActive = true;
    } // end of Sprite3D()

    private TransformGroup makeName(String userName)
    /*
     * The name of the client, floating above the sprite, and always facing the viewer.
     */
    {
        // bright yellow text
        Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
        Color3f brightYellow = new Color3f(1.0f, 1.0f, 0.0f);
        Appearance apText = new Appearance();
        Material m = new Material(brightYellow, black, brightYellow, brightYellow, 64f);
        m.setLightingEnable(true);
        apText.setMaterial(m);

        // create y-axis oriented 3D text for name
        Font3D f3d = new Font3D(new Font("SansSerif", Font.PLAIN, 2), new FontExtrusion());
        Text3D txt = new Text3D(f3d, userName);
        txt.setAlignment(Text3D.ALIGN_CENTER);

        OrientedShape3D textShape = new OrientedShape3D();
        textShape.setGeometry(txt);
        textShape.setAppearance(apText);
        textShape.setAlignmentAxis(0.0f, 1.0f, 0.0f);

        // create a transform group for the object's name
        TransformGroup nameTG = new TransformGroup();
        Transform3D nameT3d = new Transform3D();
        // Assuming uniform size chars, set scale to fit string in view
        nameT3d.setScale(1.2 / (userName.length()));
        nameT3d.setTranslation(new Vector3d(0, this.radius * 2, 0));
        nameTG.setTransform(nameT3d);
        nameTG.addChild(textShape);

        return nameTG;
    } // end of makeName()

    public BranchGroup getBG() {
        return this.objectBG;
    }

    public void detach()
    // remove this sprite from the scene
    {
        this.objectBG.detach();
    }

    public void setPosition(double xPos, double zPos) {
        Point3d currLoc = getCurrLoc();
        double xMove = xPos - currLoc.x;
        double zMove = zPos - currLoc.z;
        moveBy(xMove, zMove);
    } // end of setPosition()

    public boolean moveBy(double x, double z)
    /*
     * Move the sprite by offsets x and z, but only if within the floor and there is no obstacle nearby.
     */
    {
        if (isActive()) {
            Point3d nextLoc = tryMove(new Vector3d(x, 0, z));
            if (this.obs.nearObstacle(nextLoc, this.radius * OBS_FACTOR)) {
                return false;
            } else {
                doMove(new Vector3d(x, 0, z)); // inefficient recalc
                return true;
            }
        } else {
            // not active
            return false;
        }
    } // end of moveBy()

    private void doMove(Vector3d theMove)
    // Move the sprite by the amount in theMove
    {
        this.objectTG.getTransform(this.t3d);
        this.toMove.setTranslation(theMove); // overwrite previous trans
        this.t3d.mul(this.toMove);
        this.objectTG.setTransform(this.t3d);
    } // end of doMove()

    private Point3d tryMove(Vector3d theMove)
    /*
     * Calculate the effect of the given translation but do not update the sprite's position until it's been tested.
     */
    {
        this.objectTG.getTransform(this.t3d);
        this.toMove.setTranslation(theMove);
        this.t3d.mul(this.toMove);
        Vector3d trans = new Vector3d();
        this.t3d.get(trans);
        // printTuple(trans, "nextLoc");
        return new Point3d(trans.x, trans.y, trans.z);
    } // end of tryMove()

    public void doRotateY(double radians)
    // Rotate the sprite by radians amount around its y-axis
    {
        this.objectTG.getTransform(this.t3d);
        this.toRot.rotY(radians); // overwrite previous rotation
        this.t3d.mul(this.toRot);
        this.objectTG.setTransform(this.t3d);
        this.currRotation += radians;
    } // end of doRotateY()

    public Point3d getCurrLoc() {
        this.objectTG.getTransform(this.t3d);
        Vector3d trans = new Vector3d();
        this.t3d.get(trans);
        // printTuple(trans, "currLoc");
        return new Point3d(trans.x, trans.y, trans.z);
    } // end of getCurrLoc()

    public void setCurrRotation(double rot) {
        double rotChange = rot - this.currRotation;
        doRotateY(rotChange);
    } // end of setCurrRotation()

    public double getCurrRotation() {
        return this.currRotation;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public void setActive(boolean b)
    // Activity changes affect the sprite's visibility
    {
        this.isActive = b;
        if (!this.isActive) {
            this.visSwitch.setWhichChild(Switch.CHILD_NONE); // make invisible
        } else if (this.isActive) {
            this.visSwitch.setWhichChild(Switch.CHILD_ALL); // make visible
        }
    } // end of setActive()

} // end of Sprite3D class
