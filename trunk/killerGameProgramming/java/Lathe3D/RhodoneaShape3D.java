package Lathe3D;

// RhodoneaShape3D.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* So named because it resembles a rose. The equation is:
 r = a cos(k . angle)
 There will be k or 2k petals depending on if k is an odd 
 or even integer.

 When angle == 0, r == a == radius, so we use radius as the
 'a' value. 
 After obtaining r, we must convert back to cartesian (x,y)
 coordinates.
 */

import javax.media.j3d.Texture;
import javax.vecmath.Color3f;

public class RhodoneaShape3D extends LatheShape3D {
    public RhodoneaShape3D(double[] xsIn, double[] ysIn, Color3f darkCol, Color3f lightCol) {
        super(xsIn, ysIn, darkCol, lightCol);
    }

    public RhodoneaShape3D(double[] xsIn, double[] ysIn, Texture tex) {
        super(xsIn, ysIn, tex);
    }

    @Override
    protected double xCoord(double radius, double angle) {
        double r = radius * Math.cos(4 * angle); // 8 petals
        return r * Math.cos(angle);
    }

    @Override
    protected double zCoord(double radius, double angle) {
        double r = radius * Math.cos(4 * angle);
        return r * Math.sin(angle);
    }

} // end of RhodoneaShape3D class
