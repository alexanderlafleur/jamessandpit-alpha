package FPShooter3D;

// WrapFPShooter3D.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* A FPS example in the checkerboard world.

 The target is loaded using PropManager in createSceneGraph().

 initUserControls() does most of the FPS-specific stuff:
 * sets up the user's gun-in-hand image
 * moves the user's viewpoint
 * calls AmmoManager to create the beams and explosions
 * creates a KeyBehavior object to process keyboard input
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
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.universe.PlatformGeometry;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

public class WrapFPShooter3D extends JPanel
// Holds the 3D canvas where the loaded image is displayed
{
    private static final int BOUNDSIZE = 100; // larger than world
    private static final String GUN_PIC = "images/gunDoom.gif";
    private static final int PHEIGHT = 512;
    private static final int PWIDTH = 512; // size of panel
    /**
     * 
     */
    private static final long serialVersionUID = -2506158809332894471L;
    private static final String TARGET = "Coolrobo.3ds"; // 3DS robot
    // info used to position initial viewpoint
    private final static double Z_START = 9.0;
    private BoundingSphere bounds; // for environment nodes
    private BranchGroup sceneBG;
    private SimpleUniverse su;

    public WrapFPShooter3D()
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
        createSceneGraph();
        su.addBranchGraph(sceneBG);
    } // end of WrapFPShooter3D()

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
        // add the target
        PropManager propMan = new PropManager(TARGET, true);
        sceneBG.addChild(propMan.getTG());
        Vector3d targetVec = propMan.getLoc();
        System.out.println("Location of target: " + targetVec);
        initUserControls(targetVec);
        sceneBG.compile(); // fix the scene
    } // end of createSceneGraph()

    private PlatformGeometry gunHand()
    // Add a 'gun in hand' image to the platform geometry
    {
        PlatformGeometry pg = new PlatformGeometry();
        Point3f p1 = new Point3f(-0.1f, -0.3f, -0.7f);
        Point3f p2 = new Point3f(0.1f, -0.3f, -0.7f);
        Point3f p3 = new Point3f(0.1f, -0.1f, -0.7f);
        Point3f p4 = new Point3f(-0.1f, -0.1f, -0.7f);
        TexturedPlane tp = new TexturedPlane(p1, p2, p3, p4, GUN_PIC);
        pg.addChild(tp);
        return pg;
    } // end of gunHand()

    // --------------------- user controls ----------------------------------
    private void initUserControls(Vector3d targetVec)
    /*
     * Set up the user's gun-in-hand image. Fix the viewpoint. Call AmmoManager to create the beams and explosions. Create a KeyBehavior object to
     * process keyboard input.
     */
    {
        // add a 'gun in hand' image to the viewpoint
        ViewingPlatform vp = su.getViewingPlatform();
        PlatformGeometry pg = gunHand();
        vp.setPlatformGeometry(pg);
        // position viewpoint
        TransformGroup steerTG = vp.getViewPlatformTransform();
        Transform3D t3d = new Transform3D();
        steerTG.getTransform(t3d);
        t3d.setTranslation(new Vector3d(0, 1, Z_START));
        steerTG.setTransform(t3d);
        // create ammo (beams and explosions)
        AmmoManager ammoMan = new AmmoManager(steerTG, sceneBG, targetVec);
        // set up keyboard controls
        KeyBehavior keyBeh = new KeyBehavior(ammoMan);
        // keyBeh can ask the ammoManager to fire a beam
        keyBeh.setSchedulingBounds(bounds);
        vp.setViewPlatformBehavior(keyBeh);
    } // end of initUserControls()

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
} // end of WrapFPShooter3D class
