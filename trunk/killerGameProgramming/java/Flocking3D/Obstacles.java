package Flocking3D;

// Obstacles.java
// Sirinart Sakarin, March 2003, s4210315@calvin.coe.psu.ac.th
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

import java.util.ArrayList;

import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Material;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.geometry.Cylinder;

public class Obstacles {
    private final static int FLOOR_LEN = 20;

    // should be the same as the floor size in CheckerFloor, and be even
    private final static float MAX_HEIGHT = 8.0f;

    private final static float RADIUS = 0.3f;

    private ArrayList obsList; // of BoundingBox'es

    private BranchGroup obsBG; // obstacles in the scene

    public Obstacles(int numObs) {
        System.out.println("Num. Obstacles: " + numObs);

        this.obsList = new ArrayList();
        this.obsBG = new BranchGroup();

        Appearance blueApp = makeBlueApp();

        float x, z, height;
        Point3d lower, upper;
        BoundingBox bb;
        for (int i = 0; i < numObs; i++) {
            // random obstacle position, less than the maximum height
            x = randomFloorPosn();
            z = randomFloorPosn();
            height = (float) (Math.random() * MAX_HEIGHT);
            lower = new Point3d(x - RADIUS, 0.0f, z - RADIUS);
            upper = new Point3d(x + RADIUS, height, z + RADIUS);

            bb = new BoundingBox(lower, upper);
            this.obsList.add(bb);

            this.obsBG.addChild(makeSceneOb(height, x, z, blueApp));
        }
    } // end of Obstacles()

    private float randomFloorPosn()
    // returns a float between -FLOOR_LEN/2 and FLOOR_LEN/2
    {
        return (float) ((Math.random() * FLOOR_LEN) - (FLOOR_LEN / 2));
    }

    private Appearance makeBlueApp() {
        Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
        Color3f blue = new Color3f(0.3f, 0.3f, 0.8f);
        Color3f specular = new Color3f(0.9f, 0.9f, 0.9f);

        Material blueMat = new Material(blue, black, blue, specular, 80.0f);
        blueMat.setLightingEnable(true);

        Appearance blueApp = new Appearance();
        blueApp.setMaterial(blueMat);
        return blueApp;
    }// end of createObstacle()

    private TransformGroup makeSceneOb(float height, float x, float z, Appearance blueApp) {
        Cylinder cyl = new Cylinder(RADIUS, height, blueApp);

        // fix obs's position
        TransformGroup tg = new TransformGroup();
        Transform3D trans = new Transform3D();
        trans.setTranslation(new Vector3d(x, height / 2, z));
        tg.setTransform(trans);
        tg.addChild(cyl);
        return tg;
    } // end of makeSceneOb()

    public BranchGroup getObsBG() {
        return this.obsBG;
    }

    public boolean isOverlapping(BoundingSphere bs)
    // Does bs overlap any of the BoundingBox obstacles?
    {
        BoundingBox bb;
        for (int i = 0; i < this.obsList.size(); i++) {
            bb = (BoundingBox) this.obsList.get(i);
            if (bb.intersect(bs)) {
                return true;
            }
        }
        return false;
    } // end of isOverlapping()

} // end of Obstacles class

