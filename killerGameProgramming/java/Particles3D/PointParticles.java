package Particles3D;

// PointParticles.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* A particle system for showing a fountain effect using points.

 The points are stored in a PointArray using a BY_REFERENCE geometry.

 The geometry's coordinates and colours are changed. The colour
 of each particle changes gradually from yellow to red.

 The GeometryUpdater and Behavior subclasses are inner classes of
 LineParticles. This allows the updater to directly access the 
 coordinates and colours when its updateData() method is triggered. 
 The Behaviour subclass can refer to the PointArray.
 */

import java.util.Enumeration;

import javax.media.j3d.Appearance;
import javax.media.j3d.Behavior;
import javax.media.j3d.Geometry;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.GeometryUpdater;
import javax.media.j3d.PointArray;
import javax.media.j3d.PointAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupOnElapsedTime;
import javax.vecmath.Color3f;

public class PointParticles extends Shape3D {
    private final static int POINTSIZE = 3;

    private final static float FADE_INCR = 0.05f;

    private static final float GRAVITY = 9.8f;

    private static final float TIMESTEP = 0.05f;

    private static final float XZ_VELOCITY = 2.0f;

    private static final float Y_VELOCITY = 6.0f;

    // initial particle colour
    private final static Color3f yellow = new Color3f(1.0f, 1.0f, 0.6f);

    private PointArray pointParts; // Geometry holding the coords and colours

    private PartclesControl partBeh; // the Behaviour triggering the updates

    private float[] cs, vels, accs, cols; // we must use floats unfortunately

    private int numPoints;

    public PointParticles(int nps, int delay) {
        this.numPoints = nps;

        // BY_REFERENCE PointArray
        this.pointParts = new PointArray(this.numPoints, GeometryArray.COORDINATES | GeometryArray.COLOR_3 | GeometryArray.BY_REFERENCE);

        // pointParts.setCapability(PointArray.ALLOW_COORDINATE_WRITE);
        // pointParts.setCapability(PointArray.ALLOW_COLOR_WRITE);

        // the referenced data can be read and written
        this.pointParts.setCapability(GeometryArray.ALLOW_REF_DATA_READ);
        this.pointParts.setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);

        // Shape3D capabilities
        // setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);

        PointsUpdater updater = new PointsUpdater();
        this.partBeh = new PartclesControl(delay, updater);

        createGeometry();
        createAppearance();
    } // end of PointParticles()

    public Behavior getParticleBeh()
    // used by WrapParticles3D
    {
        return this.partBeh;
    }

    private void createGeometry() {
        this.cs = new float[this.numPoints * 3]; // to store each (x,y,z)
        this.vels = new float[this.numPoints * 3];
        this.accs = new float[this.numPoints * 3];
        this.cols = new float[this.numPoints * 3];

        // step in 3's == one (x,y,z) coord
        for (int i = 0; i < this.numPoints * 3; i = i + 3) {
            initParticle(i);
        }

        // store the coordinates and colours in the PointArray
        this.pointParts.setCoordRefFloat(this.cs); // use BY_REFERENCE
        this.pointParts.setColorRefFloat(this.cols);

        setGeometry(this.pointParts);
    } // end of createGeometry()

    private void initParticle(int i)
    // initialise coords in cs[] i to i+2 (1 point)
    {
        this.cs[i] = 0.0f;
        this.cs[i + 1] = 0.0f;
        this.cs[i + 2] = 0.0f; // (x,y,z) at origin

        // random velocity in XZ plane with combined vector XZ_VELOCITY
        double xvel = Math.random() * XZ_VELOCITY;
        double zvel = Math.sqrt((XZ_VELOCITY * XZ_VELOCITY) - (xvel * xvel));
        this.vels[i] = (float) ((Math.random() < 0.5) ? -xvel : xvel); // x vel
        this.vels[i + 2] = (float) ((Math.random() < 0.5) ? -zvel : zvel); // z vel
        // y velocity
        this.vels[i + 1] = (float) (Math.random() * Y_VELOCITY);

        // unchanging accelerations, downwards in y direction
        this.accs[i] = 0.0f;
        this.accs[i + 1] = -GRAVITY;
        this.accs[i + 2] = 0.0f;

        // initial particle colour is yellow
        this.cols[i] = yellow.x;
        this.cols[i + 1] = yellow.y;
        this.cols[i + 2] = yellow.z;
    } // end of initParticle()

    private void createAppearance() {
        Appearance app = new Appearance();

        PointAttributes pa = new PointAttributes();
        pa.setPointSize(POINTSIZE); // causes z-ordering bug
        app.setPointAttributes(pa);

        setAppearance(app);
    } // end of createAppearance()

    // -------------- PointsUpdater inner class ----------------

    public class PointsUpdater implements GeometryUpdater {
        public void updateData(Geometry geo)
        /*
         * An update of the geometry is triggered by the system. Rather than use geo, we directly access the cs[] and cols[] arrays back in PointParticles.
         */
        { // GeometryArray ga = (GeometryArray) geo;
            // float cds[] = ga.getCoordRefFloat();

            // step in 3's == one (x,y,z) coord
            for (int i = 0; i < PointParticles.this.numPoints * 3; i = i + 3) {
                if (PointParticles.this.cs[i + 1] < 0.0f) {
                    initParticle(i); // re-initialise it
                } else {
                    // update the particle
                    updateParticle(i);
                }
            }
        } // end of updateData()

        private void updateParticle(int i)
        /*
         * Calculate the particle's new position and velocity (treating is as a projectile). The acceleration is constant.
         */
        {
            PointParticles.this.cs[i] += PointParticles.this.vels[i] * TIMESTEP + 0.5 * PointParticles.this.accs[i] * TIMESTEP * TIMESTEP; // x
            // coord
            PointParticles.this.cs[i + 1] += PointParticles.this.vels[i + 1] * TIMESTEP + 0.5 * PointParticles.this.accs[i + 1] * TIMESTEP * TIMESTEP; // y coord
            PointParticles.this.cs[i + 2] += PointParticles.this.vels[i + 2] * TIMESTEP + 0.5 * PointParticles.this.accs[i + 2] * TIMESTEP * TIMESTEP; // z coord

            // calculate new velocities
            PointParticles.this.vels[i] += PointParticles.this.accs[i] * TIMESTEP; // x vel
            PointParticles.this.vels[i + 1] += PointParticles.this.accs[i + 1] * TIMESTEP; // y vel
            PointParticles.this.vels[i + 2] += PointParticles.this.accs[i + 2] * TIMESTEP; // z vel

            updateColour(i);
        } // end of updateParticle()

        private void updateColour(int i)
        /*
         * Fade colour to red by reducing the green and blue parts of the initial colour in FADE_INCR steps
         */
        {
            PointParticles.this.cols[i + 1] = PointParticles.this.cols[i + 1] - FADE_INCR; // green part
            if (PointParticles.this.cols[i + 1] < 0.0f) {
                PointParticles.this.cols[i + 1] = 0.0f;
            }

            PointParticles.this.cols[i + 2] = PointParticles.this.cols[i + 2] - FADE_INCR; // blue part
            if (PointParticles.this.cols[i + 2] < 0.0f) {
                PointParticles.this.cols[i + 2] = 0.0f;
            }
        } // end of updateColour()

    } // end of PointsUpdater class

    // ---------------- PartclesControl inner class --------------

    public class PartclesControl extends Behavior
    // Request an update every timedelay ms by using the updater object.
    {
        private WakeupCondition timedelay;

        private PointsUpdater updater;

        public PartclesControl(int delay, PointsUpdater updt) {
            this.timedelay = new WakeupOnElapsedTime(delay);
            this.updater = updt;
        }

        @Override
        public void initialize() {
            wakeupOn(this.timedelay);
        }

        @Override
        public void processStimulus(Enumeration criteria) { // ignore criteria
            // update the line array by calling the updater
            PointParticles.this.pointParts.updateData(this.updater); // request an update of the geometry
            wakeupOn(this.timedelay);
        }
    } // end of PartclesControl class

} // end of PointParticles class
