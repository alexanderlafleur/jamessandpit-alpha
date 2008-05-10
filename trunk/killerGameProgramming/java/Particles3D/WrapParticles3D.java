package Particles3D;

// WrapParticles3D.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* Three different implementations of Particle systems:
 * points in a PointArray
 * lines in a LineArray
 * quads in a QuadArray

 Geometrries are stored using BY_REFERENCE, and updated with
 a GeometryUpdater subclass.

 The QuadArray example illustrates how to apply a single texture 
 to each of the quads, and how to blend texture and colour.

 The examples are all variations on a fountain of particles 
 emmitted from the origin in a parabolic arc. Particles are 
 reused when they drop below the XZ plane.
 */
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Background;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

public class WrapParticles3D extends JPanel
// Holds the 3D canvas where the loaded image is displayed
{
    private static final int BOUNDSIZE = 100; // larger than world
    private static final int PHEIGHT = 512;
    private static final int PWIDTH = 512; // size of panel
    /**
     * 
     */
    private static final long serialVersionUID = -6958814758465695932L;
    private static final Point3d USERPOSN = new Point3d(0, 5, 20);
    // initial user position
    private BoundingSphere bounds; // for environment nodes
    private BranchGroup sceneBG;
    private SimpleUniverse su;

    public WrapParticles3D(int numParticles, int fountainChoice)
    // construct the 3D canvas
    {
        setLayout(new BorderLayout());
        setOpaque(false);
        setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas3D = new Canvas3D(config);
        add("Center", canvas3D);
        canvas3D.setFocusable(true);
        canvas3D.requestFocus(); // the canvas now has focus, so receives key
        // events
        su = new SimpleUniverse(canvas3D);
        createSceneGraph(numParticles, fountainChoice);
        initUserPosition(); // set user's viewpoint
        orbitControls(canvas3D); // controls for moving the viewpoint
        su.addBranchGraph(sceneBG);
    } // end of WrapParticles3D()

    private void addBackground()
    // A blue sky
    {
        Background back = new Background();
        back.setApplicationBounds(bounds);
        back.setColor(0.17f, 0.65f, 0.92f); // sky colour
        sceneBG.addChild(back);
    } // end of addBackground()

    private void addLinesFountain(int numParts) {
        LineParticles linesFountain = new LineParticles(numParts, 20); // time
        // delay
        sceneBG.addChild(linesFountain); // will start at origin
        // timed behaviour to animate the fountain
        Behavior partBeh = linesFountain.getParticleBeh();
        partBeh.setSchedulingBounds(bounds);
        sceneBG.addChild(partBeh);
    } // end of addLinesRefFountain()

    private void addPointsFountain(int numParts) {
        PointParticles ptsFountain = new PointParticles(numParts, 20); // time
        // delay
        // move particles start position to (2,0,1)
        TransformGroup posnTG = new TransformGroup();
        Transform3D trans = new Transform3D();
        trans.setTranslation(new Vector3d(2.0f, 0.0f, 1.0f));
        posnTG.setTransform(trans);
        posnTG.addChild(ptsFountain);
        sceneBG.addChild(posnTG);
        // timed behaviour to animate the fountain
        Behavior partBeh = ptsFountain.getParticleBeh();
        partBeh.setSchedulingBounds(bounds);
        sceneBG.addChild(partBeh);
    } // end of addPointsFountain()

    private void addQuadFountain(int numParts) {
        QuadParticles quadsFountain = new QuadParticles(numParts, 20); // time
        // delay
        sceneBG.addChild(quadsFountain); // will start at origin
        // timed behaviour to animate the fountain
        Behavior partBeh = quadsFountain.getParticleBeh();
        partBeh.setSchedulingBounds(bounds);
        sceneBG.addChild(partBeh);
    } // end of addQuadFountain()

    private void createSceneGraph(int numParts, int fountainChoice)
    // initilise the scene
    {
        sceneBG = new BranchGroup();
        bounds = new BoundingSphere(new Point3d(0, 0, 0), BOUNDSIZE);
        lightScene(); // add the lights
        addBackground(); // add the sky
        sceneBG.addChild(new CheckerFloor().getBG()); // add the floor
        switch (fountainChoice) {
        case 1:
            addPointsFountain(numParts);
            break;
        case 2:
            addLinesFountain(numParts);
            break;
        case 3:
            addQuadFountain(numParts);
            break;
        default:
            break; // say nothing
        }
        sceneBG.compile(); // fix the scene
    } // end of createSceneGraph()

    // -------------------------- fountains -------------
    private void initUserPosition()
    // Set the user's initial viewpoint using lookAt()
    {
        ViewingPlatform vp = su.getViewingPlatform();
        TransformGroup steerTG = vp.getViewPlatformTransform();
        Transform3D t3d = new Transform3D();
        steerTG.getTransform(t3d);
        // args are: viewer posn, where looking, up direction
        t3d.lookAt(USERPOSN, new Point3d(0, 0, 0), new Vector3d(0, 1, 0));
        t3d.invert();
        steerTG.setTransform(t3d);
    } // end of initUserPosition()

    private void lightScene()
    /* One ambient light, 2 directional lights */
    {
        Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
        // Set up the ambient light
        AmbientLight ambientLightNode = new AmbientLight(white);
        ambientLightNode.setInfluencingBounds(bounds);
        sceneBG.addChild(ambientLightNode);
        // Set up the directional lights
        Vector3f light1Direction = new Vector3f(-1.0f, -1.0f, -1.0f);
        // left, down, backwards
        Vector3f light2Direction = new Vector3f(1.0f, -1.0f, 1.0f);
        // right, down, forwards
        DirectionalLight light1 = new DirectionalLight(white, light1Direction);
        light1.setInfluencingBounds(bounds);
        sceneBG.addChild(light1);
        DirectionalLight light2 = new DirectionalLight(white, light2Direction);
        light2.setInfluencingBounds(bounds);
        sceneBG.addChild(light2);
    } // end of lightScene()

    private void orbitControls(Canvas3D c)
    /*
     * OrbitBehaviour allows the user to rotate around the scene, and to zoom in and out.
     */
    {
        OrbitBehavior orbit = new OrbitBehavior(c, OrbitBehavior.REVERSE_ALL);
        orbit.setSchedulingBounds(bounds);
        ViewingPlatform vp = su.getViewingPlatform();
        vp.setViewPlatformBehavior(orbit);
    } // end of orbitControls()
} // end of WrapParticles3D class
