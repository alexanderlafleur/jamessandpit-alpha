package Terra3D;

// GroundShape.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* A transparent GIF (stored in fnm) is displayed in a QuadArray.

 The center of the quad's base is at (0,0,0), so is resting on 
 the ground. It has sides of screenSize, and is always oriented 
 towards the viewer.

 The intention is to load scenery which can be represented by 2D
 images that always face the viewer, such as trees, plants, etc.

 Min and mag filtering is used to improve the GIFs appearance 
 when viewed close to and from far away.
 */

import javax.media.j3d.Appearance;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.OrientedShape3D;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Point3f;
import javax.vecmath.TexCoord2f;

import com.sun.j3d.utils.image.TextureLoader;

public class GroundShape extends OrientedShape3D {
    private static final int NUM_VERTS = 4;

    public GroundShape(float screenSize, String fnm) {
        // rotate about the y-axis to follow the viewer
        setAlignmentAxis(0.0f, 1.0f, 0.0f);

        createGeometry(screenSize);
        createAppearance(fnm);
    } // end of GroundShape()

    private void createGeometry(float sz) {
        QuadArray plane = new QuadArray(NUM_VERTS, GeometryArray.COORDINATES | GeometryArray.TEXTURE_COORDINATE_2);

        // the base is centered at (0,0,0), size screenSize,
        // facing +z axis
        Point3f p1 = new Point3f(-sz / 2, 0.0f, 0.0f);
        Point3f p2 = new Point3f(sz / 2, 0.0f, 0.0f);
        Point3f p3 = new Point3f(sz / 2, sz, 0.0f);
        Point3f p4 = new Point3f(-sz / 2, sz, 0.0f);

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

    private void createAppearance(String fnm) {
        Appearance app = new Appearance();

        // blended transparency so texture can be irregular
        TransparencyAttributes tra = new TransparencyAttributes();
        tra.setTransparencyMode(TransparencyAttributes.BLENDED);
        app.setTransparencyAttributes(tra);

        // Create a two dimensional texture with min and mag filtering
        TextureLoader loader = new TextureLoader(fnm, null);
        Texture2D tex = (Texture2D) loader.getTexture();
        if (tex == null) {
            System.out.println("Image Loading Failed for " + fnm);
        } else {
            tex.setMinFilter(Texture.BASE_LEVEL_LINEAR);
            tex.setMagFilter(Texture.BASE_LEVEL_LINEAR);
            app.setTexture(tex);
        }

        setAppearance(app);
    } // end of createAppearance()

} // end of GroundShape class
