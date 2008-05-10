package Particles3D;

// QuadParticles.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* A particle system for showing a fountain effect using a 
 textured and coloured QuadArray consisting of many quads.

 Only a single texture is used, and the colour comes from
 a Material node component.

 The quad points are stored in a QuadArray using BY_REFERENCE geometry.

 The geometry's coordinates are changed.

 The GeometryUpdater and Behavior subclasses are inner classes of
 QuadParticles. This allows the updater to directly access the 
 coordinates when its updateData() method is triggered. 
 The Behaviour subclass can refer to the QuadArray.

 QuadParticles is a subclass of OrientedShape3D, not Shape3D, and
 is set to rotate about the positive y-axis.
 */
import java.util.Enumeration;

import javax.media.j3d.Appearance;
import javax.media.j3d.Behavior;
import javax.media.j3d.Geometry;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.GeometryUpdater;
import javax.media.j3d.Material;
import javax.media.j3d.OrientedShape3D;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.TransparencyAttributes;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupOnElapsedTime;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.image.TextureLoader;

public class QuadParticles extends OrientedShape3D {
    public class PartclesControl extends Behavior
    // Request an update every timedelay ms by using the updater object.
    {
        private WakeupCondition timedelay;
        private QuadsUpdater updater;

        public PartclesControl(int delay, QuadsUpdater updt) {
            timedelay = new WakeupOnElapsedTime(delay);
            updater = updt;
        }

        @Override
        public void initialize() {
            wakeupOn(timedelay);
        }

        @Override
        public void processStimulus(Enumeration criteria) { // ignore criteria
            // update the line array by calling the updater
            quadParts.updateData(updater); // request an update of the geometry
            wakeupOn(timedelay);
        }
    } // end of PartclesControl class

    public class QuadsUpdater implements GeometryUpdater {
        private float perturbate(float f, float range)
        // return a float which is f added to a number between -range to range
        {
            float randomRange = (float) (Math.random() * range * 2.0f) - range;
            return f + randomRange;
        } // end of perturbate()

        public void updateData(Geometry geo)
        /*
         * An update of the geometry is triggered by the system. Rather than use geo, we directly access the cs[] array back in QuadParticles.
         */
        { // GeometryArray ga = (GeometryArray) geo;
            // float cds[] = ga.getCoordRefFloat();
            // step in 12's == 4 (x,y,z) coords == one quad
            for (int i = 0; i < numPoints * 3; i = i + 12) {
                updateQuadParticle(i);
            }
        } // end of updateData()

        private void updateParticle(int i)
        /*
         * Calculate the particle's new position and velocity (treating is as a projectile). The acceleration is constant.
         */
        {
            cs[i] += vels[i] * TIMESTEP + 0.5 * accs[i] * TIMESTEP * TIMESTEP; // x
            // coord
            cs[i + 1] += vels[i + 1] * TIMESTEP + 0.5 * accs[i + 1] * TIMESTEP * TIMESTEP; // y coord
            cs[i + 2] += vels[i + 2] * TIMESTEP + 0.5 * accs[i + 2] * TIMESTEP * TIMESTEP; // z coord
            // perturbate (alter a bit) the (x,y,z) values
            /*
             * Without this each quad stays a fixed size since each (x,y,z) coord is moved by the same amount
             */
            cs[i] = perturbate(cs[i], DELTA);
            cs[i + 1] = perturbate(cs[i + 1], DELTA);
            cs[i + 1] = perturbate(cs[i + 1], DELTA);
            // calculate new velocities
            vels[i] += accs[i] * TIMESTEP; // x vel
            vels[i + 1] += accs[i + 1] * TIMESTEP; // y vel
            vels[i + 2] += accs[i + 2] * TIMESTEP; // z vel
        } // end of updateParticle()

        private void updateQuadParticle(int i) {
            if (cs[i + 1] < 0.0f && cs[i + 4] < 0.0f && cs[i + 7] < 0.0f && cs[i + 10] < 0.0f) {
                // all of the quad has dropped below the y-axis
                initQuadParticle(i);
            } else {
                updateParticle(i); // all points in a quad change the same way
                updateParticle(i + 3);
                updateParticle(i + 6);
                updateParticle(i + 9);
            }
        } // end of updateQuadParticle()
    } // end of QuadsUpdater class

    // colours for particles
    private final static Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
    private final static Color3f darkRed = new Color3f(1.0f, 0.0f, 0.0f);
    private static final float DELTA = 0.05f; // for randomly adjusting the
    // coords
    private static final float GRAVITY = 9.8f;
    private static final float QUAD_LEN = 0.5f;
    private final static Color3f red = new Color3f(0.9f, 0.1f, 0.2f);
    private final static String TEX_FNM = "smoke.gif";
    private static final float TIMESTEP = 0.05f;
    private final static Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
    private static final float XZ_VELOCITY = 2.0f;
    private static final float Y_VELOCITY = 8.0f;
    private float[] cs, vels, accs, norms; // we must use floats arrays
    private int numPoints;
    // starting points for a quad, counter-clockewise from bottom-left
    private float[] p1 = { -QUAD_LEN / 2, 0.0f, 0.0f };
    private float[] p2 = { QUAD_LEN / 2, 0.0f, 0.0f };
    private float[] p3 = { QUAD_LEN / 2, QUAD_LEN, 0.0f };
    private float[] p4 = { -QUAD_LEN / 2, QUAD_LEN, 0.0f };
    private PartclesControl partBeh; // the Behaviour triggering the updates
    private QuadArray quadParts; // Geometry holding the coords, etc.
    private float[] tcoords;

    public QuadParticles(int nps, int delay) {
        if (nps % 4 != 0) {
            nps = (nps + 4) / 4 * 4; // round up to nearest multiple
        }
        numPoints = nps;
        // rotate about the y-axis to follow the viewer
        // setAlignmentAxis( 0.0f, 1.0f, 0.0f);
        setAlignmentAxis(0.0f, 0.0f, 1.0f);
        // BY_REFERENCE QuadArray
        quadParts =
                new QuadArray(numPoints, GeometryArray.COORDINATES | GeometryArray.TEXTURE_COORDINATE_2 | GeometryArray.NORMALS
                        | GeometryArray.BY_REFERENCE);
        // the referenced data can be read and written
        quadParts.setCapability(GeometryArray.ALLOW_REF_DATA_READ);
        quadParts.setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);
        QuadsUpdater updater = new QuadsUpdater();
        partBeh = new PartclesControl(delay, updater);
        createGeometry();
        createAppearance();
    } // end of QuadParticles()

    private void copyCoord(float[] fs, int to, int from)
    // copy 3 floats starting at fs[from] to fs[to]
    {
        fs[to] = fs[from];
        fs[to + 1] = fs[from + 1];
        fs[to + 2] = fs[from + 2];
    } // end of copyCoord()

    private void createAppearance() {
        Appearance app = new Appearance();
        // turn off back face culling
        // PolygonAttributes pa = new PolygonAttributes();
        // pa.setCullFace( PolygonAttributes.CULL_NONE );
        // app.setPolygonAttributes( pa );
        // blended transparency so texture can be irregular
        TransparencyAttributes tra = new TransparencyAttributes();
        tra.setTransparencyMode(TransparencyAttributes.BLENDED);
        app.setTransparencyAttributes(tra);
        // mix the texture and the material colour
        TextureAttributes ta = new TextureAttributes();
        ta.setTextureMode(TextureAttributes.MODULATE);
        app.setTextureAttributes(ta);
        // load and set the texture
        System.out.println("Loading textures from " + TEX_FNM);
        TextureLoader loader = new TextureLoader(TEX_FNM, null);
        Texture2D texture = (Texture2D) loader.getTexture();
        app.setTexture(texture); // set the texture
        // set the material: bloody
        Material mat = new Material(darkRed, black, red, white, 20.f);
        mat.setLightingEnable(true);
        app.setMaterial(mat);
        setAppearance(app);
    } // end of createAppearance()

    private void createGeometry()
    /*
     * Create the float arrays for the coords, vels, accs, normals, and texture coords. Assign the coords, normals and texture coords to the QuadArray
     * using BY_REFERENCE. Only the coords will be changed later.
     */
    {
        cs = new float[numPoints * 3]; // to store each (x,y,z)
        vels = new float[numPoints * 3];
        accs = new float[numPoints * 3];
        norms = new float[numPoints * 3];
        tcoords = new float[numPoints * 2]; // two texture coords per coord
        // textureCoordsArray = new TexCoord2f[numPoints];
        // initialise position, vel, and acc
        // steps of 12 == 4 (x,y,z) coords == 1 quad at a time
        for (int i = 0; i < numPoints * 3; i = i + 12) {
            initQuadParticle(i);
        }
        // refer to the coordinates in the QuadArray
        quadParts.setCoordRefFloat(cs); // use BY_REFERENCE
        /*
         * Initialise texture coords. We are applying the texture to each quad in the array For each quad (4 points): { 0.0f, 0.0f, 1.0f, 0.0f, 1.0f,
         * 1.0f, 0.0f, 1.0f }; One texture coords (s,t) for each point (x,y,z).
         */
        for (int i = 0; i < numPoints * 2; i = i + 8) {
            tcoords[i] = 0.0f;
            tcoords[i + 1] = 0.0f; // for 1 point
            tcoords[i + 2] = 1.0f;
            tcoords[i + 3] = 0.0f;
            tcoords[i + 4] = 1.0f;
            tcoords[i + 5] = 1.0f;
            tcoords[i + 6] = 0.0f;
            tcoords[i + 7] = 1.0f;
        }
        quadParts.setTexCoordRefFloat(0, tcoords); // use BY_REFERENCE
        // initialise normals to face in a general positive z-axis direction
        Vector3f norm = new Vector3f();
        for (int i = 0; i < numPoints * 3; i = i + 3) {
            randomNormal(norm);
            norms[i] = norm.x;
            norms[i + 1] = norm.y;
            norms[i + 2] = norm.z;
        }
        quadParts.setNormalRefFloat(norms); // use BY_REFERENCE
        setGeometry(quadParts);
    } // end of createGeometry()

    public Behavior getParticleBeh()
    // used by WrapParticles3D
    {
        return partBeh;
    }

    private void initQuadParticle(int i)
    // initialise quad made up of 4 particles from i to i+11
    {
        setCoord(cs, i, p1); // set starting points
        setCoord(cs, i + 3, p2);
        setCoord(cs, i + 6, p3);
        setCoord(cs, i + 9, p4);
        // random velocity in XZ plane with combined vector XZ_VELOCITY
        double xvel = Math.random() * XZ_VELOCITY;
        double zvel = Math.sqrt(XZ_VELOCITY * XZ_VELOCITY - xvel * xvel);
        vels[i] = (float) (Math.random() < 0.5 ? -xvel : xvel); // x vel
        vels[i + 2] = (float) (Math.random() < 0.5 ? -zvel : zvel); // z vel
        // y velocity
        vels[i + 1] = (float) (Math.random() * Y_VELOCITY);
        // unchanging accelerations, downwards in y direction
        accs[i] = 0.0f;
        accs[i + 1] = -GRAVITY;
        accs[i + 2] = 0.0f;
        // all the particles in the quad have the same vel and acc
        copyCoord(vels, i + 3, i);
        copyCoord(vels, i + 6, i);
        copyCoord(vels, i + 9, i);
        copyCoord(accs, i + 3, i);
        copyCoord(accs, i + 6, i);
        copyCoord(accs, i + 9, i);
    } // end of initQuadParticle()

    // -------------- QuadsUpdater inner class ----------------
    private void randomNormal(Vector3f v)
    // Create a unit vector. The x- and y- values can be +ve or -ve.
    // The z-value is positive, so facing towards the viewer.
    {
        float z = (float) Math.random(); // between 0-1
        float x = (float) (Math.random() * 2.0 - 1.0); // -1 to 1
        float y = (float) (Math.random() * 2.0 - 1.0); // -1 to 1
        v.set(x, y, z);
        v.normalize();
    } // end of randomNormal()

    // ---------------- PartclesControl inner class --------------
    private void setCoord(float[] fs, int i, float[] p)
    // copy the 3 floats in p[] into fs starting at i
    {
        fs[i] = p[0];
        fs[i + 1] = p[1];
        fs[i + 2] = p[2];
    } // end of setCoord()
} // end of QuadParticles class
