package Checkers3D;

// WrapCheckers3D.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* Illustrates how to create a simple world. The checkboard
 floor is created by CheckerFloor. The background, lighting,
 key controls, and initial user positioning is done here.

 Most of this will stay the same from one example to another.

 floatingSphere() shows how to create a coloured, shiny object
 which is affected by the lighting in the world.

 The scene graph display code is in WrapCheckers3D() and 
 createSceneGraph().
 */
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Material;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

// import com.tornadolabs.j3dtree.*; // for displaying the scene graph
public class WrapCheckers3D extends JPanel
// Holds the 3D canvas where the loaded image is displayed
{
    private static final int BOUNDSIZE = 100; // larger than world
    private static final int PHEIGHT = 512;
    private static final int PWIDTH = 512; // size of panel
    /**
     * 
     */
    private static final long serialVersionUID = 2050429884755831656L;
    private static final Point3d USERPOSN = new Point3d(0, 5, 20);
    // initial user position
    private BoundingSphere bounds; // for environment nodes
    private BranchGroup sceneBG;
    private SimpleUniverse su;

    // private Java3dTree j3dTree; // frame to hold tree display
    public WrapCheckers3D()
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
        su = new SimpleUniverse(canvas3D);
        // j3dTree = new Java3dTree(); // create a display tree for the SG
        createSceneGraph();
        initUserPosition(); // set user's viewpoint
        orbitControls(canvas3D); // controls for moving the viewpoint
        su.addBranchGraph(sceneBG);
        // j3dTree.updateNodes( su ); // build the tree display window
    } // end of WrapCheckers3D()

    private void addBackground()
    // A blue sky
    {
        Background back = new Background();
        back.setApplicationBounds(bounds);
        back.setColor(0.17f, 0.65f, 0.92f); // sky colour
        sceneBG.addChild(back);
    } // end of addBackground()

    private void createSceneGraph()
    // initilise the scene
    {
        sceneBG = new BranchGroup();
        bounds = new BoundingSphere(new Point3d(0, 0, 0), BOUNDSIZE);
        lightScene(); // add the lights
        addBackground(); // add the sky
        sceneBG.addChild(new CheckerFloor().getBG()); // add the floor
        floatingSphere(); // add the floating sphere
        // j3dTree.recursiveApplyCapability( sceneBG ); // set capabilities for
        // tree display
        sceneBG.compile(); // fix the scene
    } // end of createSceneGraph()

    private void floatingSphere()
    // A shiny blue sphere located at (0,4,0)
    {
        // Create the blue appearance node
        Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
        Color3f blue = new Color3f(0.3f, 0.3f, 0.8f);
        Color3f specular = new Color3f(0.9f, 0.9f, 0.9f);
        Material blueMat = new Material(blue, black, blue, specular, 25.0f);
        // sets ambient, emissive, diffuse, specular, shininess
        blueMat.setLightingEnable(true);
        Appearance blueApp = new Appearance();
        blueApp.setMaterial(blueMat);
        // position the sphere
        Transform3D t3d = new Transform3D();
        t3d.set(new Vector3f(0, 4, 0));
        TransformGroup tg = new TransformGroup(t3d);
        tg.addChild(new Sphere(2.0f, blueApp)); // set its radius and appearance
        sceneBG.addChild(tg);
    } // end of floatingSphere()

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

    // ---------------------- floating sphere -----------------
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
} // end of WrapCheckers3D class
