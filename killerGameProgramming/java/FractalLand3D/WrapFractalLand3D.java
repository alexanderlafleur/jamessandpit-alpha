package FractalLand3D;

// WrapFractalLand3D.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* A fractal landscape is generated, made out of a mesh
 of textured squares. Squares at different heights are
 textured in different ways.

 The landscape is surrounded by blue walls, and is poorly lit to
 suggest evening; the sky is a dark blue. LinearFog obscures
 the distance.

 The user can 'walk' over the landscape using the
 similar left/right/front/back/turn/up/down moves
 as in the FPShooter3D example.
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;

import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.LinearFog;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

public class WrapFractalLand3D extends JPanel
/* Holds the 3D fractal landscape in a Swing container. */
{
    /**
     * 
     */
    private static final long serialVersionUID = 374638266047292854L;

    private final static int PWIDTH = 512; // size of panel

    private final static int PHEIGHT = 512;

    private static final int BOUNDSIZE = 100; // larger than world

    private Color3f skyColour = new Color3f(0.17f, 0.07f, 0.45f);

    // used for the Background and LinearFog nodes

    private SimpleUniverse su;

    private BranchGroup sceneBG;

    private BoundingSphere bounds; // for environment nodes

    private Landscape land; // creates the floor and walls

    public WrapFractalLand3D(double flatness) {
        setLayout(new BorderLayout());
        setOpaque(false);
        setPreferredSize(new Dimension(PWIDTH, PHEIGHT));

        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas3D = new Canvas3D(config);
        add("Center", canvas3D);
        canvas3D.setFocusable(true);
        canvas3D.requestFocus();
        this.su = new SimpleUniverse(canvas3D);

        createSceneGraph(flatness);
        createUserControls();

        this.su.addBranchGraph(this.sceneBG);
    } // end of WrapFractalLand3D()

    void createSceneGraph(double flatness)
    // initilise the scene
    {
        this.sceneBG = new BranchGroup();
        this.bounds = new BoundingSphere(new Point3d(0, 0, 0), BOUNDSIZE);

        lightScene(); // add the lights
        addBackground(); // add the sky
        addFog(); // add the fog; comment this line out to switch off fog

        // create the landscape: the floor and walls
        this.land = new Landscape(flatness);
        this.sceneBG.addChild(this.land.getLandBG());

        this.sceneBG.compile(); // fix the scene
    } // end of createScene()

    private void lightScene()
    // one directional light
    {
        Color3f white = new Color3f(1.0f, 1.0f, 1.0f);

        Vector3f lightDir = new Vector3f(1.0f, -1.0f, -0.8f); // upper left
        DirectionalLight light1 = new DirectionalLight(white, lightDir);
        light1.setInfluencingBounds(this.bounds);
        this.sceneBG.addChild(light1);
    } // end of lightScene()

    private void addBackground()
    // An early evening sky
    {
        Background back = new Background();
        back.setApplicationBounds(this.bounds);
        back.setColor(this.skyColour); // darkish blue
        this.sceneBG.addChild(back);
    } // end of addBackground()

    private void addFog()
    // linear fog
    {
        LinearFog fogLinear = new LinearFog(this.skyColour, 15.0f, 30.0f);
        fogLinear.setInfluencingBounds(this.bounds); // same as background
        this.sceneBG.addChild(fogLinear);
    } // end of addFog()

    // ---------------------- user controls --------------------------

    private void createUserControls()
    /*
     * Adjust the clip distances and set up the KeyBehaviour. The behaviour initialises the viewpoint at the origin on the XZ plane.
     */
    {
        // original clips are 10 and 0.1; keep ratio between 100-1000
        View view = this.su.getViewer().getView();
        view.setBackClipDistance(20); // can see a long way
        view.setFrontClipDistance(0.05); // can see close things

        ViewingPlatform vp = this.su.getViewingPlatform();
        TransformGroup steerTG = vp.getViewPlatformTransform();

        // set up keyboard controls (and position viewpoint)
        KeyBehavior keybeh = new KeyBehavior(this.land, steerTG);
        keybeh.setSchedulingBounds(this.bounds);
        vp.setViewPlatformBehavior(keybeh);
    } // end of createUserControls()

} // end of WrapFractalLand3D class
