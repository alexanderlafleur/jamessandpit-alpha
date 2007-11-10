package Terra3D;

// WrapTerra3D.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Set up the 3D world: lighting, background, key controls.

 Use a Landscape object to load the landscape (mesh and
 texture), landscape walls, 3D scenery, and ground cover.

 The dark blue sky contains stars (points of light).

 The user navigates over the terrain using a KeyBehavior object.
 But the size of the terrain mesh means that there is a lag time 
 before a user's height position correctly reflects the height of the
 terrain.

 For example, it is possible to move _inside_ a mountain, but when the 
 height calculation (done through picking) is eventually completed, then 
 the user's height will be corrected.

 This behaviour is intentional -- it allows the user to move about the
 terrain quickly without being forced to wait for the height calculations
 to finish.
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.util.Random;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.PointArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

public class WrapTerra3D extends JPanel
// Holds the 3D canvas where the loaded image is displayed
{
    /**
     * 
     */
    private static final long serialVersionUID = -2167431878716004471L;

    private static final int PWIDTH = 512; // size of panel

    private static final int PHEIGHT = 512;

    private static final int BOUNDSIZE = 100; // larger than world

    private static final Color3f white = new Color3f(1.0f, 1.0f, 1.0f);

    private final static int NUM_STARS = 5000; // no. stars in the sky

    private SimpleUniverse su;

    private BranchGroup sceneBG;

    private BoundingSphere bounds; // for environment nodes

    private Landscape land;

    public WrapTerra3D(String fname)
    // construct the 3D canvas
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

        createSceneGraph(fname);
        createUserControls();

        this.su.addBranchGraph(this.sceneBG);
    } // end of WrapTerra3D()

    void createSceneGraph(String fname)
    // initilise the scene
    {
        this.sceneBG = new BranchGroup();
        this.bounds = new BoundingSphere(new Point3d(0, 0, 0), BOUNDSIZE);

        lightScene(); // add the lights
        addBackground(); // add the sky and stars
        this.land = new Landscape(this.sceneBG, fname);
        // add the landscape, scenery, walls, trees

        this.sceneBG.compile(); // fix the scene
    } // end of createSceneGraph()

    private void lightScene()
    // One ambient light, one directional light
    {
        // Set up the ambient light
        AmbientLight ambientLightNode = new AmbientLight(white);
        ambientLightNode.setInfluencingBounds(this.bounds);
        this.sceneBG.addChild(ambientLightNode);

        // Set up the directional lights
        Vector3f light1Direction = new Vector3f(-1.0f, -1.0f, -1.0f);
        // left, down, backwards
        Vector3f light2Direction = new Vector3f(1.0f, -1.0f, 1.0f);
        // right, down, forwards
        /*
         * DirectionalLight light1 = new DirectionalLight(white, light1Direction); light1.setInfluencingBounds(bounds); sceneBG.addChild(light1);
         */
        DirectionalLight light2 = new DirectionalLight(white, light2Direction);
        light2.setInfluencingBounds(this.bounds);
        this.sceneBG.addChild(light2);
    } // end of lightScene()

    private void addBackground()
    // the background is a medium blue and random placed stars
    {
        Background back = new Background();
        back.setApplicationBounds(this.bounds);
        // back.setColor(0.17f, 0.07f, 0.45f); // dark blue
        // back.setColor(0.17f, 0.65f, 0.92f); // light blue
        back.setColor(0.17f, 0.50f, 0.92f);
        back.setGeometry(addStars());
        this.sceneBG.addChild(back);
    } // end of addBackground()

    private BranchGroup addStars()
    /*
     * The stars are a PointArray with random colours, placed in a BranchGroup Originally by Kevin Duling; see his example at
     * http://home.earthlink.net/~kduling/Java3D/Stars/index.html
     */
    {
        PointArray starField = new PointArray(NUM_STARS, GeometryArray.COORDINATES | GeometryArray.COLOR_3);
        float[] pt = new float[3];
        float[] brightness = new float[3];
        Random rand = new Random();

        for (int i = 0; i < NUM_STARS; i++) {
            pt[0] = (rand.nextInt(2) == 0) ? -rand.nextFloat() : rand.nextFloat();
            pt[1] = rand.nextFloat(); // only above the y-axis
            pt[2] = (rand.nextInt(2) == 0) ? -rand.nextFloat() : rand.nextFloat();
            starField.setCoordinate(i, pt);

            float mag = rand.nextFloat();
            brightness[0] = mag;
            brightness[1] = mag;
            brightness[2] = mag;
            starField.setColor(i, brightness);
        }

        BranchGroup bg = new BranchGroup();
        bg.addChild(new Shape3D(starField));
        return bg;
    } // end of addStars()

    private void createUserControls()
    /*
     * Adjust the clip distances and set up the KeyBehaviour. The behaviour initialises the viewpoint at the origin on the XZ plane. Also turn on depth-sorting to deal with all the
     * transparent GIFs used for the trees.
     */
    {
        // original clips are 10 and 0.1; keep ratio between 100-1000
        View view = this.su.getViewer().getView();
        view.setBackClipDistance(20); // can see a long way
        view.setFrontClipDistance(0.05); // can see close things

        // depth-sort transparent objects on a per-geometry basis
        view.setTransparencySortingPolicy(View.TRANSPARENCY_SORT_GEOMETRY);

        ViewingPlatform vp = this.su.getViewingPlatform();
        TransformGroup steerTG = vp.getViewPlatformTransform();

        // set up keyboard controls (and position viewpoint)
        KeyBehavior keybeh = new KeyBehavior(this.land, steerTG);
        keybeh.setSchedulingBounds(this.bounds);
        vp.setViewPlatformBehavior(keybeh);
    } // end of createUserControls()

} // end of WrapTerra3D class
