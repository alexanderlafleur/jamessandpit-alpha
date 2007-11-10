package Mover3D;

// EllipticLimb.java
// Thana Konglikhit, October 2003, s4310170@maliwan.psu.ac.th
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* An unmoveable Limb object but using an elliptical lathe shape
 rather than a circular one.
 */

import javax.media.j3d.Texture;

import com.sun.j3d.utils.image.TextureLoader;

public class EllipticLimb extends Limb {
    public EllipticLimb(int lNo, String jn0, String jn1, int axis, double angle, double[] xs, double[] ys, String tex) {
        super(lNo, jn0, jn1, axis, angle, xs, ys, tex);
    }

    @Override
    protected void makeShape()
    // overridden to make a EllipseShape3D instead of LatheShape3D
    {
        EllipseShape3D es;
        if (this.texPath != null) {
            // System.out.println("Loading textures/" + texPath);
            TextureLoader texLd = new TextureLoader("textures/" + this.texPath, null);
            Texture tex = texLd.getTexture();
            es = new EllipseShape3D(this.xsIn, this.ysIn, tex);
        } else {
            es = new EllipseShape3D(this.xsIn, this.ysIn, null);
        }

        this.zAxisTG.addChild(es); // add the shape to the limb's graph
    } // end of makeShape()

} // end of EllipticLimb class
