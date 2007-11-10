package Shooter3D;

// GunTurret.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* The GunTurret is a cylinder with a cone top which rotates
 to point at the checkboard location picked by the user.

 gun BG --> base TG --> cylinder
 |
 --> gun TG  --> cone

 The base TG is used only to initially position the cylinder so
 it is resting on the XZ plane. The gun TG handle the cone
 rotation.
 */

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Material;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.image.TextureLoader;

public class GunTurret {
    private static final Vector3d ORIGIN = new Vector3d(0, 0, 0);

    private BranchGroup gunBG;

    private TransformGroup gunTG;

    private Vector3d startVec;

    // for repeated calculations
    private Transform3D gunT3d = new Transform3D();

    private Vector3d currTrans = new Vector3d();

    private Transform3D rotT3d = new Transform3D();

    public GunTurret(Vector3d svec) {
        this.startVec = svec;
        this.gunBG = new BranchGroup();
        Appearance apStone = stoneApp();
        placeGunBase(apStone);
        placeGun(apStone);
    } // end of GunTurret()

    private Appearance stoneApp()
    // stone appearance using a texture
    {
        Material stoneMat = new Material(); // white by default
        stoneMat.setLightingEnable(true);

        Appearance apStone = new Appearance();
        apStone.setMaterial(stoneMat);

        TextureLoader stoneTex = new TextureLoader("images/stone.jpg", null);
        if (stoneTex != null) {
            apStone.setTexture(stoneTex.getTexture());
        }

        // combine the texture with material and lighting
        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.MODULATE);
        apStone.setTextureAttributes(texAttr);

        return apStone;
    } // end of stoneApp()

    private void placeGunBase(Appearance apStone)
    // a cylinder resting on the XZ plane, height 2 units
    {
        Transform3D baseT3d = new Transform3D();
        baseT3d.set(new Vector3d(0, 1, 0)); // so resting on XZ plane
        TransformGroup baseTG = new TransformGroup();
        baseTG.setTransform(baseT3d);
        Cylinder cyl = new Cylinder(0.25f, 2.0f, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, apStone);
        cyl.setPickable(false); // gun base is unpickable
        baseTG.addChild(cyl);

        this.gunBG.addChild(baseTG);
    } // end of placeGunBase()

    private void placeGun(Appearance apStone)
    // a rotatable cone, whose center is 2 unit above XZ plane
    {
        this.gunTG = new TransformGroup();
        this.gunTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE); // can
        // rotate
        this.gunTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        this.gunT3d.set(this.startVec); // centered at the top of the cylinder
        this.gunTG.setTransform(this.gunT3d);
        Cone cone = new Cone(1.0f, 2.0f, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, apStone);
        cone.setPickable(false); // gun cone is unpickable
        this.gunTG.addChild(cone);

        this.gunBG.addChild(this.gunTG);
    } // end of placeGun()

    public BranchGroup getGunBG() {
        return this.gunBG;
    }

    public void makeRotation(AxisAngle4d rotAxis)
    // rotate the cone of the gun turret
    {
        this.gunTG.getTransform(this.gunT3d); // get current transform
        // System.out.println("Start gunT3d: " + gunT3d);
        this.gunT3d.get(this.currTrans); // get current translation
        this.gunT3d.setTranslation(ORIGIN); // translate to origin

        this.rotT3d.setRotation(rotAxis); // apply rotation
        this.gunT3d.mul(this.rotT3d);

        this.gunT3d.setTranslation(this.currTrans); // translate back
        this.gunTG.setTransform(this.gunT3d);
        // System.out.println("End gunT3d: " + gunT3d);
    } // end of makeRotation()

} // end of GunTurret class
