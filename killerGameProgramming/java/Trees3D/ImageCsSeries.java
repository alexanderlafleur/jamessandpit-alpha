package Trees3D;

// ImageCsSeries.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* An ImagesCsSeries object is a 'screen', centered at (0,0,0),
 that can display an image from a series passed to it at creation
 time. 

 The size of the screen's sides is set by the screenSize variable.

 The screen is a subclass of OrientedShape3D, configured to rotate
 around the point (0,0, zCoord) relative to the user's viewpoint.

 The series of images are stored in an ImageComponent2D array.

 By default, the first image in the array is shown, and methods
 must be explicitly called to change the displayed picture. There
 is no default animation.

 A version of ImageCsSeries with default animation can be found in 
 FPShooter3D
 */

import javax.media.j3d.Appearance;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.OrientedShape3D;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Point3f;
import javax.vecmath.TexCoord2f;

public class ImageCsSeries extends OrientedShape3D {
    private static final int NUM_VERTS = 4;

    private ImageComponent2D[] ims; // the sequence of images

    private int imIndex, numImages;

    private Texture2D texture;

    public ImageCsSeries(float zCoord, float screenSize, ImageComponent2D[] ims) {
        this.ims = ims;
        this.imIndex = 0;
        this.numImages = ims.length;

        // set the orientation mode
        setAlignmentMode(OrientedShape3D.ROTATE_ABOUT_POINT);
        setRotationPoint(0.0f, 0.0f, zCoord);

        createGeometry(screenSize);
        createAppearance();
    } // end of ImageCsSeries()

    private void createGeometry(float sz) {
        QuadArray plane = new QuadArray(NUM_VERTS, GeometryArray.COORDINATES | GeometryArray.TEXTURE_COORDINATE_2);

        // the screen is centered at the center, size screenSize, facing +z axis
        Point3f p1 = new Point3f(-sz / 2, -sz / 2, 0.0f);
        Point3f p2 = new Point3f(sz / 2, -sz / 2, 0.0f);
        Point3f p3 = new Point3f(sz / 2, sz / 2, 0.0f);
        Point3f p4 = new Point3f(-sz / 2, sz / 2, 0.0f);

        // anti-clockwise from bottom left
        plane.setCoordinate(0, p1);
        plane.setCoordinate(1, p2);
        plane.setCoordinate(2, p3);
        plane.setCoordinate(3, p4);

        // set up texture coords for holding the image
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

    private void createAppearance() {
        Appearance app = new Appearance();

        // blended transparency so texture can be irregular
        TransparencyAttributes tra = new TransparencyAttributes();
        tra.setTransparencyMode(TransparencyAttributes.BLENDED);
        app.setTransparencyAttributes(tra);

        // Create a two dimensional texture with magnification filtering
        // Set the texture from the first loaded image
        this.texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, this.ims[0].getWidth(), this.ims[0].getHeight());
        this.texture.setMagFilter(Texture.BASE_LEVEL_LINEAR); // NICEST
        this.texture.setImage(0, this.ims[0]);
        this.texture.setCapability(Texture.ALLOW_IMAGE_WRITE); // texture can
        // change
        app.setTexture(this.texture);

        setAppearance(app);
    } // end of createAppearance()

    public void showImage(int i)
    // show the i th image in the sequence
    {
        if (i < 0) {
            this.texture.setImage(0, this.ims[0]);
            this.imIndex = 0;
        } else if (i >= this.numImages) {
            this.texture.setImage(0, this.ims[this.numImages - 1]);
            this.imIndex = this.numImages - 1;
        } else {
            this.texture.setImage(0, this.ims[i]);
            this.imIndex = i;
        }
    } // end of showImage()

    public void showNext()
    // show the next image in the sequence
    {
        if (this.imIndex < this.numImages - 1) {
            this.imIndex++;
            this.texture.setImage(0, this.ims[this.imIndex]);
        }
    }

    public void showPrev()
    // show the previous image in the sequence
    {
        if (this.imIndex > 0) {
            this.imIndex--;
            this.texture.setImage(0, this.ims[this.imIndex]);
        }
    }

} // end of ImageCsSeries class
