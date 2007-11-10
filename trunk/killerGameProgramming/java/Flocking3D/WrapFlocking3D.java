package Flocking3D;

// WrapFlocking3D.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
// Sirinart Sakarin, March 2003, s4210315@calvin.coe.psu.ac.th

/* The flocking predators and prey are created and added to
 the scene in addFlockingBoids(). Obstacles for the boids are
 also created there.

 The rest of the code is standard. 
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

public class WrapFlocking3D extends JPanel
// Holds the 3D canvas where the loaded image is displayed
{
    /**
     * 
     */
    private static final long serialVersionUID = 6930899248113853793L;

    private static final int PWIDTH = 512; // size of panel

    private static final int PHEIGHT = 512;

    private static final int BOUNDSIZE = 100; // larger than world

    private static final Point3d USERPOSN = new Point3d(0, 5, 20);

    // initial user position

    private SimpleUniverse su;

    private BranchGroup sceneBG;

    private BoundingSphere bounds; // for environment nodes

    public WrapFlocking3D(int numPreds, int numPrey, int numObs)
    // A panel holding a 3D canvas: the usual way of linking Java 3D to Swing
    {
        setLayout(new BorderLayout());
        setOpaque(false);
        setPreferredSize(new Dimension(PWIDTH, PHEIGHT));

        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas3D = new Canvas3D(config);
        add("Center", canvas3D);
        canvas3D.setFocusable(true); // give focus to the canvas
        canvas3D.requestFocus();

        this.su = new SimpleUniverse(canvas3D);
        createSceneGraph(numPreds, numPrey, numObs);
        initUserPosition(); // set user's viewpoint
        orbitControls(canvas3D); // controls for moving the viewpoint

        this.su.addBranchGraph(this.sceneBG);
    } // end of WrapFlocking3D()

    private void createSceneGraph(int numPreds, int numPrey, int numObs)
    // initilise the scene
    {
        this.sceneBG = new BranchGroup();
        this.bounds = new BoundingSphere(new Point3d(0, 0, 0), BOUNDSIZE);

        lightScene(); // add the lights
        addBackground(); // add the sky
        this.sceneBG.addChild(new CheckerFloor().getBG()); // add the floor

        addFlockingBoids(numPreds, numPrey, numObs);
    } // end of createSceneGraph()

    private void lightScene()
    /* One ambient light, 2 directional lights */
    {
        Color3f white = new Color3f(1.0f, 1.0f, 1.0f);

        // Set up the ambient light
        AmbientLight ambientLightNode = new AmbientLight(white);
        ambientLightNode.setInfluencingBounds(this.bounds);
        this.sceneBG.addChild(ambientLightNode);

        // Set up the directional lights
        Vector3f light1Direction = new Vector3f(-1.0f, -1.0f, -1.0f);
        // left, down, backwards
        Vector3f light2Direction = new Vector3f(1.0f, -1.0f, 1.0f);
        // right, down, forwards

        DirectionalLight light1 = new DirectionalLight(white, light1Direction);
        light1.setInfluencingBounds(this.bounds);
        this.sceneBG.addChild(light1);

        DirectionalLight light2 = new DirectionalLight(white, light2Direction);
        light2.setInfluencingBounds(this.bounds);
        this.sceneBG.addChild(light2);
    } // end of lightScene()

    private void addBackground()
    // A blue sky
    {
        Background back = new Background();
        back.setApplicationBounds(this.bounds);
        back.setColor(0.17f, 0.65f, 0.92f); // sky colour
        this.sceneBG.addChild(back);
    } // end of addBackground()

    private void orbitControls(Canvas3D c)
    /*
     * OrbitBehaviour allows the user to rotate around the scene, and to zoom in and out.
     */
    {
        OrbitBehavior orbit = new OrbitBehavior(c, OrbitBehavior.REVERSE_ALL);
        orbit.setSchedulingBounds(this.bounds);

        ViewingPlatform vp = this.su.getViewingPlatform();
        vp.setViewPlatformBehavior(orbit);
    } // end of orbitControls()

    private void initUserPosition()
    // Set the user's initial viewpoint using lookAt()
    {
        ViewingPlatform vp = this.su.getViewingPlatform();
        TransformGroup steerTG = vp.getViewPlatformTransform();

        Transform3D t3d = new Transform3D();
        steerTG.getTransform(t3d);

        // args are: viewer posn, where looking, up direction
        t3d.lookAt(USERPOSN, new Point3d(0, 0, 0), new Vector3d(0, 1, 0));
        t3d.invert();

        steerTG.setTransform(t3d);
    } // end of initUserPosition()

    // ---------------------- flocking boids -----------------

    private void addFlockingBoids(int numPreds, int numPrey, int numObs) {
        // create obstacles
        Obstacles obs = new Obstacles(numObs);
        this.sceneBG.addChild(obs.getObsBG()); // add obstacles to the scene

        // make the predator manager
        PredatorBehavior predBeh = new PredatorBehavior(numPreds, obs);
        predBeh.setSchedulingBounds(this.bounds);
        this.sceneBG.addChild(predBeh.getBoidsBG()); // add predators to the scene

        // make the prey manager
        PreyBehavior preyBeh = new PreyBehavior(numPrey, obs);
        preyBeh.setSchedulingBounds(this.bounds);
        this.sceneBG.addChild(preyBeh.getBoidsBG()); // add prey to the scene

        // tell behaviours about each other
        predBeh.setPreyBeh(preyBeh);
        preyBeh.setPredBeh(predBeh);

    } // end of addFlockingBoids()

} // end of WrapFlocking3D class
