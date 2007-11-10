package Trees3D;

// WrapTrees3D.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* The standard checkboard scene, but several trees now sprout
 and grow, spread over several seconds.
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
import javax.media.j3d.ImageComponent;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

public class WrapTrees3D extends JPanel
// Holds the 3D canvas where the loaded image is displayed
{
    /**
     * 
     */
    private static final long serialVersionUID = 8824832255700650326L;

    private static final int PWIDTH = 512; // size of panel

    private static final int PHEIGHT = 512;

    private static final int BOUNDSIZE = 100; // larger than world

    private static final Point3d USERPOSN = new Point3d(0, 7, 30);

    // initial user position (further back than usual)

    private final static int Y_AXIS = 1;

    private final static int Z_AXIS = 2;

    private SimpleUniverse su;

    private BranchGroup sceneBG;

    private BoundingSphere bounds; // for environment nodes

    public WrapTrees3D()
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

        createSceneGraph();
        initUserPosition(); // set user's viewpoint
        orbitControls(canvas3D); // controls for moving the viewpoint

        // turn on depth-sorting to deal with all the transparent
        // GIFs used for the leaves
        View view = this.su.getViewer().getView();
        // depth-sort transparent objects on a per-geometry basis
        view.setTransparencySortingPolicy(View.TRANSPARENCY_SORT_GEOMETRY);

        this.su.addBranchGraph(this.sceneBG);
    } // end of WrapTrees3D()

    private void createSceneGraph()
    // initilise the scene
    {
        this.sceneBG = new BranchGroup();
        this.bounds = new BoundingSphere(new Point3d(0, 0, 0), BOUNDSIZE);

        lightScene(); // add the lights
        addBackground(); // add the sky
        this.sceneBG.addChild(new CheckerFloor().getBG()); // add the floor

        growTrees(); // the new bit

        this.sceneBG.compile(); // fix the scene
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

    // ---------------------- grow trees -----------------

    private void growTrees()
    /*
     * Position three trees and create the behaviour that controls their growth.
     */
    {
        // starting position for the first tree: (0,0,-5)
        Transform3D t3d = new Transform3D();
        t3d.set(new Vector3f(0, 0, -5));
        TransformGroup tg0 = new TransformGroup(t3d);
        this.sceneBG.addChild(tg0);
        // create the tree
        TreeLimb t0 = new TreeLimb(Z_AXIS, 0, 0.05f, 0.5f, tg0, null);

        // second tree
        t3d.set(new Vector3f(-5, 0, 5));
        TransformGroup tg1 = new TransformGroup(t3d);
        this.sceneBG.addChild(tg1);
        TreeLimb t1 = new TreeLimb(Y_AXIS, 45, 0.05f, 0.5f, tg1, null);

        // third tree
        t3d.set(new Vector3f(5, 0, 5));
        TransformGroup tg2 = new TransformGroup(t3d);
        this.sceneBG.addChild(tg2);
        TreeLimb t2 = new TreeLimb(Y_AXIS, -60, 0.05f, 0.5f, tg2, null);

        // fourth tree
        t3d.set(new Vector3f(-9, 0, -9));
        TransformGroup tg3 = new TransformGroup(t3d);
        this.sceneBG.addChild(tg3);
        TreeLimb t3 = new TreeLimb(Y_AXIS, 30, 0.05f, 0.5f, tg3, null);

        // 5th tree
        t3d.set(new Vector3f(9, 0, -9));
        TransformGroup tg4 = new TransformGroup(t3d);
        this.sceneBG.addChild(tg4);
        TreeLimb t4 = new TreeLimb(Y_AXIS, -30, 0.05f, 0.5f, tg4, null);

        // load the leaf images used by all the trees
        ImageComponent2D[] leafIms = loadImages("images/leaf", 6);

        // the behaviour that manages the growing of the trees
        GrowthBehavior grower = new GrowthBehavior(leafIms);
        grower.setSchedulingBounds(this.bounds);

        // add the trees to GrowthBehavior
        grower.addLimb(t0);
        grower.addLimb(t1);
        grower.addLimb(t2);
        grower.addLimb(t3);
        grower.addLimb(t4);

        this.sceneBG.addChild(grower);
    } // end of growTrees()

    private ImageComponent2D[] loadImages(String fNms, int numIms)
    /*
     * Load the leaf images: they all start with fNms, and there are numIms of them.
     */
    {
        String filename;
        TextureLoader loader;
        ImageComponent2D[] ims = new ImageComponent2D[numIms];
        System.out.println("Loading " + numIms + " textures from " + fNms);
        for (int i = 0; i < numIms; i++) {
            filename = new String(fNms + i + ".gif");
            loader = new TextureLoader(filename, null);
            ims[i] = loader.getImage();
            if (ims[i] == null) {
                System.out.println("Load failed for texture in : " + filename);
            }
            ims[i].setCapability(ImageComponent.ALLOW_SIZE_READ);
        }
        return ims;
    } // end of loadImages()

} // end of WrapTrees3D class
