package Lathe3D;

// WrapLathe3D.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* Illustrates how to create lathe shapes in the usual checkboard
 world. See the examples in addLatheShapes() at the end of this file.
 */
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsConfiguration;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Texture;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.Text2D;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

public class WrapLathe3D extends JPanel
// Holds the 3D canvas where the loaded image is displayed
{
    private static final int BOUNDSIZE = 100; // larger than world
    private static final int PHEIGHT = 512;
    private static final int PWIDTH = 512; // size of panel
    /**
     * 
     */
    private static final long serialVersionUID = 322183570621274544L;
    private static final Point3d USERPOSN = new Point3d(0, 7, 20);
    // initial user position
    private static final Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
    private BoundingSphere bounds; // for environment nodes
    private BranchGroup sceneBG;
    private SimpleUniverse su;

    public WrapLathe3D()
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
        createSceneGraph();
        initUserPosition(); // set user's viewpoint
        orbitControls(canvas3D); // controls for moving the viewpoint
        su.addBranchGraph(sceneBG);
    } // end of WrapLathe3D()

    private void addBackground()
    // A blue sky
    {
        Background back = new Background();
        back.setApplicationBounds(bounds);
        back.setColor(0.17f, 0.65f, 0.92f); // sky colour
        sceneBG.addChild(back);
    } // end of addBackground()

    private void addLatheShapes()
    // many examples using LatheShape3D (and subclasses)
    {
        // use default colours (pink, dark pink)
        double xsIn1[] = { 0, 1, 0 };
        double ysIn1[] = { 0, 1.5, 2.5 };
        LatheShape3D ls1 = new LatheShape3D(xsIn1, ysIn1, null); // big pink
        // balloon
        displayLathe(ls1, -7.0f, -5.0f, "egg");
        // --------------------- load textures -------------
        System.out.println("Loading R texture");
        TextureLoader texLd1 = new TextureLoader("textures/r.gif", null);
        Texture rTex = texLd1.getTexture();
        System.out.println("Loading skin texture");
        TextureLoader texLd2 = new TextureLoader("textures/skin.jpg", null);
        Texture skinTex = texLd2.getTexture();
        System.out.println("Loading water texture");
        TextureLoader texLd3 = new TextureLoader("textures/water.jpg", null);
        Texture waterTex = texLd3.getTexture();
        System.out.println("Loading metal plate texture");
        TextureLoader texLd4 = new TextureLoader("textures/plate.jpg", null);
        Texture plateTex = texLd4.getTexture();
        System.out.println("Loading brick texture");
        TextureLoader texLd5 = new TextureLoader("textures/brick.gif", null);
        Texture brickTex = texLd5.getTexture();
        System.out.println("Loading lava texture");
        TextureLoader texLd6 = new TextureLoader("textures/lava.jpg", null);
        Texture lavaTex = texLd6.getTexture();
        System.out.println("Loading sky texture");
        TextureLoader texLd7 = new TextureLoader("textures/sky.jpg", null);
        Texture skyTex = texLd7.getTexture();
        System.out.println("Loading cobbles texture");
        TextureLoader texLd8 = new TextureLoader("textures/cobbles.jpg", null);
        Texture cobblesTex = texLd8.getTexture();
        System.out.println("Loading bark texture");
        TextureLoader texLd9 = new TextureLoader("textures/bark1.jpg", null);
        Texture barkTex = texLd9.getTexture();
        System.out.println("Loading swirled texture");
        TextureLoader texLd10 = new TextureLoader("textures/swirled.jpg", null);
        Texture swirledTex = texLd10.getTexture();
        // ------------------------------
        // curves and texture
        double xsIn15[] = { 0, 0.1, 0.7, 0 };
        double ysIn15[] = { 0, 0.1, 1.5, 2 };
        LatheShape3D ls2 = new LatheShape3D(xsIn15, ysIn15, waterTex);
        displayLathe(ls2, -3.5f, -5.0f, "drip");
        // mix straight lines and curves, and textures
        double xsIn2[] = { -0.001, -0.7, -0.25, 0.25, 0.7, -0.6, -0.5 }; // uses
        // -ve
        // coords
        double ysIn2[] = { 0, 0, 0.5, 1, 2.5, 3, 3 };
        LatheShape3D ls3 = new LatheShape3D(xsIn2, ysIn2, swirledTex);
        displayLathe(ls3, -1.0f, -5.0f, "cup");
        // skin-textured limb
        double xsIn25[] = { 0, 0.4, 0.6, 0 };
        double ysIn25[] = { 0, 0.4, 2.2, 3 };
        LatheShape3D ls4 = new LatheShape3D(xsIn25, ysIn25, skinTex);
        displayLathe(ls4, 3.0f, -5.0f, "limb");
        // various shape rotations using subclasses
        double xsIn3[] = { -1, -1 };
        double ysIn3[] = { 0, 1 };
        LatheShape3D ls5 = new LatheShape3D(xsIn3, ysIn3, rTex); // circular
        displayLathe(ls5, 6.0f, -5.0f, "round R");
        EllipseShape3D ls6 = new EllipseShape3D(xsIn3, ysIn3, rTex); // elliptic
        displayLathe(ls6, 6.0f, 0, "oval R");
        RhodoneaShape3D ls7 = new RhodoneaShape3D(xsIn3, ysIn3, null); // pink
        displayLathe(ls7, 3.0f, 0, "flower");
        double xsIn4[] = { -0.001, -0.4, -0.2, 0.2, 0.3, -0.2, -0.3, -0.001 };
        double ysIn4[] = { 0, 0, 1, 1.2, 1.4, 1.6, 1.8, 1.8 };
        LatheShape3D ls8 = new LatheShape3D(xsIn4, ysIn4, skyTex);
        displayLathe(ls8, -1.0f, 0, "chess");
        // all straight lines: use -0.001 to start a line at 0
        double xsIn5[] = { -0.001, -0.4, -0.15, -0.001 };
        double ysIn5[] = { 0, 0, 3, 3 };
        LatheShape3D ls9 = new LatheShape3D(xsIn5, ysIn5, barkTex);
        displayLathe(ls9, -3.5f, 0, "branch");
        // rotated circle: a torus
        double xsIn6[] = { 1, 1.5, 1, 0.5, 1 };
        double ysIn6[] = { 0, 0.5, 1, 0.5, 0 };
        LatheShape3D ls10 = new LatheShape3D(xsIn6, ysIn6, lavaTex);
        displayLathe(ls10, -7.0f, 0, "torus");
        double xsIn7[] = { 0, 0.4, -0.1, 0.1, 0.4, 0 };
        double ysIn7[] = { 0, 0.4, 0.8, 1.2, 1.6, 2 };
        LatheShape3D ls11 = new LatheShape3D(xsIn7, ysIn7, cobblesTex);
        displayLathe(ls11, -3.5f, 5.0f, "dumbbell");
        double xsIn8[] = { -0.01, 1, 0 };
        double ysIn8[] = { 0, 0, 1 };
        LatheShape3D ls12 = new LatheShape3D(xsIn8, ysIn8, brickTex);
        displayLathe(ls12, -1.0f, 5.0f, "dome");
        double xsIn9[] = { -0.01, 0.5, -1, -1.2, 1.4, -0.5, -0.5, 0 };
        double ysIn9[] = { 0, 0, 1.5, 1.5, 2, 2.5, 2.7, 2.7 };
        EllipseShape3D ls13 = new EllipseShape3D(xsIn9, ysIn9, plateTex); // elliptic
        displayLathe(ls13, 3.0f, 5.0f, "armour");
        // use colours
        Color3f brown = new Color3f(0.3f, 0.2f, 0.0f);
        Color3f darkBrown = new Color3f(0.15f, 0.1f, 0.0f);
        double xsIn10[] = { 0, 0.75, 0.9, 0.75, 0 };
        double ysIn10[] = { 0, 0.23, 0.38, 0.53, 0.75 };
        LatheShape3D ls14 = new LatheShape3D(xsIn10, ysIn10, darkBrown, brown);
        displayLathe(ls14, 6.0f, 5.0f, "saucer");
    } // end of addLatheShapes()

    private void createSceneGraph()
    // initilise the scene
    {
        sceneBG = new BranchGroup();
        bounds = new BoundingSphere(new Point3d(0, 0, 0), BOUNDSIZE);
        lightScene(); // add the lights
        addBackground(); // add the sky
        sceneBG.addChild(new CheckerFloor().getBG()); // add the floor
        addLatheShapes();
        sceneBG.compile(); // fix the scene
    } // end of createSceneGraph()

    private void displayLathe(LatheShape3D ls, float x, float z, String label) {
        // position the LatheShape3D
        Transform3D t3d = new Transform3D();
        t3d.set(new Vector3f(x, 1.5f, z));
        TransformGroup tg1 = new TransformGroup(t3d);
        tg1.addChild(ls);
        sceneBG.addChild(tg1);
        // position the label for the shape
        Text2D message = new Text2D(label, white, "SansSerif", 72, Font.BOLD);
        // 72 point bold Sans Serif
        t3d.set(new Vector3f(x - 0.4f, 0.75f, z));
        TransformGroup tg2 = new TransformGroup(t3d);
        tg2.addChild(message);
        sceneBG.addChild(tg2);
    } // end of displayLathe()

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

    // ---------------------- shapes -----------------
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
} // end of WrapLathe3D class
