package Flocking3D;

// BoidShape.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
// Sirinart Sakarin, March 2003, s4210315@calvin.coe.psu.ac.th
/* A boid is represented by an IndexedTriangleArray, with
 a body colour specified as an argument to the constructor. The
 front coordinate (the nose) of the shape is purple.

 The shape is a spear-head, with a long thin base
 and short height.

 We do not change the shape's coordinates (e.g. its size) or
 colour during execution, so there is no need for a GeometryUpdater.

 The shape does not change its Appearance node component.
 */
import javax.media.j3d.GeometryArray;
import javax.media.j3d.IndexedTriangleArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;

public class BoidShape extends Shape3D {
    private static final int NUM_INDICES = 12;
    private static final int NUM_VERTS = 4;
    private static final Color3f purple = new Color3f(0.5f, 0.2f, 0.8f);

    public BoidShape(Color3f col) {
        IndexedTriangleArray plane = new IndexedTriangleArray(NUM_VERTS, GeometryArray.COORDINATES | GeometryArray.COLOR_3, NUM_INDICES);
        // the shape's coordinates
        Point3f[] pts = new Point3f[NUM_VERTS];
        pts[0] = new Point3f(0.0f, 0.0f, 0.25f);
        pts[1] = new Point3f(0.2f, 0.0f, -0.25f);
        pts[2] = new Point3f(-0.2f, 0.0f, -0.25f);
        pts[3] = new Point3f(0.0f, 0.25f, -0.2f);
        // anti-clockwise face definition
        int[] indices = { 2, 0, 3, // left face
                2, 1, 0, // bottom face
                0, 1, 3, // right face
                1, 2, 3 }; // back face
        plane.setCoordinates(0, pts);
        plane.setCoordinateIndices(0, indices);
        // the shape's vertex colours
        Color3f[] cols = new Color3f[NUM_VERTS];
        cols[0] = purple; // a purple nose
        for (int i = 1; i < NUM_VERTS; i++) {
            cols[i] = col;
        }
        plane.setColors(0, cols);
        plane.setColorIndices(0, indices);
        setGeometry(plane);
    } // end of BoidShape()
} // end of BoidShape class
