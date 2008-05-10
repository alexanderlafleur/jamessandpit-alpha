package Mover3D;

// WrapMover3D.java
// Thana Konglikhit, October 2003, s4310170@maliwan.psu.ac.th
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* The checkboard world, and a multi-limbed figure.

 The Figure object and LocationBeh behavior are created here. 

 The locationBeh object allows figure commands to
 be entered from the keyboard.
 */
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Background;
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

public class WrapMover3D extends JPanel
// Holds the 3D canvas where the loaded image is displayed
{
    private static final int BOUNDSIZE = 100; // larger than world
    private static final int PHEIGHT = 512;
    private static final int PWIDTH = 512; // size of panel
    /**
     * 
     */
    private static final long serialVersionUID = 2574634140447886781L;
    private static final Point3d USERPOSN = new Point3d(0, 4.8, 6);
    // initial user position
    private BoundingSphere bounds; // for environment nodes
    private Figure figure; // the multi-limbed figure
    private BranchGroup sceneBG;
    private SimpleUniverse su;

    public WrapMover3D() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas3D = new Canvas3D(config);
        add("Center", canvas3D);
        canvas3D.setFocusable(true); // give focus to the canvas
        canvas3D.requestFocus();
        su = new SimpleUniverse(canvas3D);
        createSceneGraph();
        initUserPosition(); // set user's viewpoint
        orbitControls(canvas3D); // controls for moving the viewpoint
        su.addBranchGraph(sceneBG);
    } // end of WrapMover3D()

    private void addBackground()
    // A blue sky
    {
        Background back = new Background();
        back.setApplicationBounds(bounds);
        back.setColor(0.17f, 0.65f, 0.92f); // sky colour
        sceneBG.addChild(back);
    } // end of addBackground()

    private void addFigure()
    // add the figure and its behaviour to the scene
    {
        figure = new Figure();
        sceneBG.addChild(figure.getFigureTG()); // add figure's TG
        // add behavior
        LocationBeh locBeh = new LocationBeh(figure);
        locBeh.setSchedulingBounds(bounds);
        sceneBG.addChild(locBeh);
    } // end of addFigure()

    private void createSceneGraph()
    // initilise the scene
    {
        sceneBG = new BranchGroup();
        bounds = new BoundingSphere(new Point3d(0, 0, 0), BOUNDSIZE);
        lightScene(); // add the lights
        addBackground(); // add the sky
        sceneBG.addChild(new CheckerFloor().getBG()); // add the floor
        addFigure();
        sceneBG.compile(); // fix the scene
    } // end of createSceneGraph()

    public Figure getFigure() {
        return figure;
    }

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

    // -------------------------- figure code ------------
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
} // end of WrapMover3D class
