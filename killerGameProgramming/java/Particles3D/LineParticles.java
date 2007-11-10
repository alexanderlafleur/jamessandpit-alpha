package Particles3D;

// LineParticles.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* A particle system for showing a fountain effect using lines.

 The points are stored in a LineArray using a BY_REFERENCE geometry.

 Only the line's coordinates are changed.

 The GeometryUpdater and Behavior subclasses are inner classes of
 LineParticles. This allows the updater to directly access the 
 coordinates when its updateData() method is triggered. The Behaviour
 subclass can refer to the LineArray.
 */

import java.util.Enumeration;

import javax.media.j3d.Appearance;
import javax.media.j3d.Behavior;
import javax.media.j3d.Geometry;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.GeometryUpdater;
import javax.media.j3d.LineArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupOnElapsedTime;
import javax.vecmath.Color3f;

public class LineParticles extends Shape3D {
    private static final float GRAVITY = 9.8f;

    private static final float TIMESTEP = 0.05f;

    private static final float XZ_VELOCITY = 2.0f;

    private static final float Y_VELOCITY = 8.0f;

    // particle colours
    private final static Color3f red = new Color3f(1.0f, 0.0f, 0.0f);

    private final static Color3f yellow = new Color3f(1.0f, 1.0f, 0.6f);

    private LineArray lineParts; // Geometry holding the coords and colours

    private PartclesControl partBeh; // the Behaviour triggering the updates

    private float[] cs, vels, accs, cols; // we must use floats unfortunately

    private int numPoints;

    public LineParticles(int nps, int delay) {
        if (nps % 2 == 1) {
            nps++;
        }
        this.numPoints = nps;

        // BY_REFERENCE LineArray
        this.lineParts = new LineArray(this.numPoints, GeometryArray.COORDINATES | GeometryArray.COLOR_3 | GeometryArray.BY_REFERENCE);

        // lineParts.setCapability(LineArray.ALLOW_COORDINATE_WRITE);
        // the referenced data can be read and written
        this.lineParts.setCapability(GeometryArray.ALLOW_REF_DATA_READ);
        this.lineParts.setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);

        // Shape3D capabilities
        // setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);

        LinesUpdater updater = new LinesUpdater();
        this.partBeh = new PartclesControl(delay, updater);

        createGeometry();
        createAppearance();
    } // end of LineParticles()

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

        // step in 6's == two (x,y,z) coords == one line
        for (int i = 0; i < this.numPoints * 3; i = i + 6) {
            initTwoParticles(i);
        }

        // store the coordinates and colours in the LineArray
        this.lineParts.setCoordRefFloat(this.cs); // use BY_REFERENCE
        this.lineParts.setColorRefFloat(this.cols);

        setGeometry(this.lineParts);
    } // end of createGeometry()

    private void initTwoParticles(int i)
    /*
     * Initialise coords in cs[] i to i+5 (2 points / 1 line). We do things in 2 point groups since they are the two ends of a line.
     */
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

        // next particle starts the same, but is one update advanced
        this.cs[i + 3] = this.cs[i];
        this.cs[i + 4] = this.cs[i + 1];
        this.cs[i + 5] = this.cs[i + 2];
        this.vels[i + 3] = this.vels[i];
        this.vels[i + 4] = this.vels[i + 1];
        this.vels[i + 5] = this.vels[i + 2];
        this.accs[i + 3] = this.accs[i];
        this.accs[i + 4] = this.accs[i + 1];
        this.accs[i + 5] = this.accs[i + 2];
        updateParticle(i + 3);

        // set initial colours for the first particle
        Color3f col = (Math.random() < 0.5) ? yellow : red;
        this.cols[i] = col.x;
        this.cols[i + 1] = col.y;
        this.cols[i + 2] = col.z;
        // the next particle has the same colours
        this.cols[i + 3] = col.x;
        this.cols[i + 4] = col.y;
        this.cols[i + 5] = col.z;

    } // end of initTwoParticles()

    private void updateParticle(int i)
    /*
     * Calculate the particle's new position and velocity (treating is as a projectile). The acceleration is constant.
     */
    {
        this.cs[i] += this.vels[i] * TIMESTEP + 0.5 * this.accs[i] * TIMESTEP * TIMESTEP; // x
        // coord
        this.cs[i + 1] += this.vels[i + 1] * TIMESTEP + 0.5 * this.accs[i + 1] * TIMESTEP * TIMESTEP; // y coord
        this.cs[i + 2] += this.vels[i + 2] * TIMESTEP + 0.5 * this.accs[i + 2] * TIMESTEP * TIMESTEP; // z coord

        // calculate new velocities
        this.vels[i] += this.accs[i] * TIMESTEP; // x vel
        this.vels[i + 1] += this.accs[i + 1] * TIMESTEP; // y vel
        this.vels[i + 2] += this.accs[i + 2] * TIMESTEP; // z vel
    } // end of updateParticle()

    private void createAppearance() {
        Appearance app = new Appearance();

        LineAttributes la = new LineAttributes();
        // la.setLineWidth( LINEWIDTH ); // causes z-ordering bug
        // la.setLineAntialiasingEnable(true);
        app.setLineAttributes(la);

        setAppearance(app);
    } // end of createAppearance()

    // -------------- LinesUpdater inner class ----------------

    public class LinesUpdater implements GeometryUpdater {
        public void updateData(Geometry geo)
        /*
         * An update of the geometry is triggered by the system. Rather than use geo, we directly access the cs[] array back in LineParticles.
         */
        { // GeometryArray ga = (GeometryArray) geo;
            // float cds[] = ga.getCoordRefFloat();

            // step in 6's == two (x,y,z) coords == one line
            for (int i = 0; i < LineParticles.this.numPoints * 3; i = i + 6) {
                if ((LineParticles.this.cs[i + 1] < 0.0f) && (LineParticles.this.cs[i + 4] < 0.0f)) {
                    // both particles in the line have dropped below the y-axis
                    initTwoParticles(i); // re-initialise them
                } else { // update the two particles
                    updateParticle(i);
                    updateParticle(i + 3);
                }
            }
        } // end of updateData()

    } // end of LinesUpdater class

    // ---------------- PartclesControl inner class --------------

    public class PartclesControl extends Behavior
    // Request an update every timedelay ms by using the updater object.
    {
        private WakeupCondition timedelay;

        private LinesUpdater updater;

        public PartclesControl(int delay, LinesUpdater updt) {
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
            LineParticles.this.lineParts.updateData(this.updater); // request an update of the geometry
            wakeupOn(this.timedelay);
        }

    } // end of PartclesControl class

} // end of LineParticles class
