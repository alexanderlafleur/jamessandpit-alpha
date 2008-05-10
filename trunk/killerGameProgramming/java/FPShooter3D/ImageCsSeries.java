package FPShooter3D;

// ImageCsSeries.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* Shows a series of images as an animation, displayed on a transparent 
 QuadArray. The center of the quad is at center, has sides of screenSize,
 and is oriented along the +z axis.
 The GIFs have already been loaded into an ImageComponent2D array
 which is passed in via the constructor.

 This version of ImagesSeries from /Shooter3D does not
 load its own GIFs, and specifies a center point for the quad.
 */
import javax.media.j3d.Appearance;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Point3f;
import javax.vecmath.TexCoord2f;

public class ImageCsSeries extends Shape3D {
    private static final int DELAY = 100; // ms delay between frames
    private static final int NUM_VERTS = 4;
    private ImageComponent2D[] ims;
    private Texture2D texture;

    public ImageCsSeries(Point3f center, float screenSize, ImageComponent2D[] ims) {
        this.ims = ims;
        createGeometry(center, screenSize);
        createAppearance();
    } // end of ImageCsSeries()

    private void createAppearance() {
        Appearance app = new Appearance();
        // blended transparency so texture can be irregular
        TransparencyAttributes tra = new TransparencyAttributes();
        tra.setTransparencyMode(TransparencyAttributes.BLENDED);
        app.setTransparencyAttributes(tra);
        // Create a two dimensional texture with magnification filtering
        // Set the texture from the first loaded image
        texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, ims[0].getWidth(), ims[0].getHeight());
        texture.setMagFilter(Texture.BASE_LEVEL_LINEAR); // NICEST
        texture.setImage(0, ims[0]);
        texture.setCapability(Texture.ALLOW_IMAGE_WRITE); // texture can
        // change
        app.setTexture(texture);
        setAppearance(app);
    } // end of createAppearance()

    private void createGeometry(Point3f c, float sz) {
        QuadArray plane = new QuadArray(NUM_VERTS, GeometryArray.COORDINATES | GeometryArray.TEXTURE_COORDINATE_2);
        // the screen is centered at the center, size screenSize, facing +z axis
        Point3f p1 = new Point3f(c.x - sz / 2, c.y - sz / 2, c.z);
        Point3f p2 = new Point3f(c.x + sz / 2, c.y - sz / 2, c.z);
        Point3f p3 = new Point3f(c.x + sz / 2, c.y + sz / 2, c.z);
        Point3f p4 = new Point3f(c.x - sz / 2, c.y + sz / 2, c.z);
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

    public void showSeries() {
        for (ImageComponent2D element : ims) {
            texture.setImage(0, element);
            try {
                Thread.sleep(DELAY); // wait a while
            } catch (Exception ex) {
            }
        }
    } // end of showSeries()
} // end of ImageCsSeries class
