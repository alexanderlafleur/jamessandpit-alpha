package Terra3D;

// TexturedPlane.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* Creates a single quad array of 4 vertices with a texture.

 The Appearance component is much simpler than other versions
 of the TexturedPlane class (e.g. the one in FPShooter3D), since
 the texture is applied without any blending with a material.
 */
import javax.media.j3d.Appearance;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Texture2D;
import javax.vecmath.Point3d;
import javax.vecmath.TexCoord2f;

public class TexturedPlane extends Shape3D {
    private static final int NUM_VERTS = 4;

    public TexturedPlane(Point3d p1, Point3d p2, Point3d p3, Point3d p4, Texture2D tex) {
        createGeometry(p1, p2, p3, p4);
        Appearance app = new Appearance();
        app.setTexture(tex); // set the texture
        setAppearance(app);
    } // end of TexturedPlane()

    private void createGeometry(Point3d p1, Point3d p2, Point3d p3, Point3d p4) {
        QuadArray plane = new QuadArray(NUM_VERTS, GeometryArray.COORDINATES | GeometryArray.TEXTURE_COORDINATE_2);
        // anti-clockwise from bottom left
        plane.setCoordinate(0, p1);
        plane.setCoordinate(1, p2);
        plane.setCoordinate(2, p3);
        plane.setCoordinate(3, p4);
        TexCoord2f q = new TexCoord2f();
        q.set(0.0f, 0.0f);
        plane.setTextureCoordinate(0, 0, q);
        q.set(1.0f, 0.0f);
        plane.setTextureCoordinate(0, 1, q);
        q.set(1.0f, 1.0f);
        plane.setTextureCoordinate(0, 2, q);
        q.set(0.0f, 1.0f);
        plane.setTextureCoordinate(0, 3, q);
        setGeometry(plane);
    } // end of createGeometry()
} // end of TexturedPlane class
