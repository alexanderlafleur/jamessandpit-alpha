package NetFourByFour;

// WrapNetFBF.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Consists of 16 poles holding 4 spheres each. The player
 clicks on a sphere to make a turn, and the sphere turns
 into a large red sphere or blue box depending on the
 player.

 The game is shown using parallel projection so the poles 
 at the 'back' do not appear smaller than those at the front --
 there is no perspective effect.

 The game uses its own mouse behaviour, defined in 
 PickDragBehaviour, to select positions on the board, and to
 rotate the board.

 Changes for Net version:
 * make board global and added setPosn()
 * uses OverlayCanvas() instead of Canvas3D()
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Material;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class WrapNetFBF extends JPanel
// Holds the 3D canvas where the game appears
{
    /**
     * 
     */
    private static final long serialVersionUID = -6597083230642457995L;

    private static final int PWIDTH = 400; // size of panel

    private static final int PHEIGHT = 400;

    private static final int BOUNDSIZE = 100; // larger than world

    private SimpleUniverse su;

    private BranchGroup sceneBG;

    private BoundingSphere bounds; // for environment nodes

    private Board board;

    public WrapNetFBF(NetFourByFour fbf) {
        setLayout(new BorderLayout());
        setOpaque(false);
        setPreferredSize(new Dimension(PWIDTH, PHEIGHT));

        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        OverlayCanvas canvas3D = new OverlayCanvas(config, fbf);
        add("Center", canvas3D);
        canvas3D.setFocusable(true); // give focus to the canvas
        canvas3D.requestFocus();

        this.su = new SimpleUniverse(canvas3D);

        createSceneGraph(canvas3D, fbf);
        initUserPosition(); // set user's viewpoint

        this.su.addBranchGraph(this.sceneBG);
    } // end of WrapNetFBF()

    private void createSceneGraph(OverlayCanvas canvas3D, NetFourByFour fbf) {
        this.sceneBG = new BranchGroup();
        this.bounds = new BoundingSphere(new Point3d(0, 0, 0), BOUNDSIZE);

        // Create the transform group which moves the game
        TransformGroup tg = new TransformGroup();
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        this.sceneBG.addChild(tg);

        lightScene(); // add the lights
        addBackground(); // add the background

        tg.addChild(makePoles()); // add poles

        // posns holds the spheres/boxes which mark a player's turn
        // initially posns displays a series of small white spheres.
        Positions posns = new Positions();

        // board tracks the players moves on the game board
        this.board = new Board(posns, fbf);

        tg.addChild(posns.getChild()); // add position markers

        mouseControls(canvas3D, fbf, tg);

        this.sceneBG.compile(); // fix the scene
    } // end of createSceneGraph()

    private void lightScene()
    // One ambient light, one directional light
    {
        Color3f white = new Color3f(1.0f, 1.0f, 1.0f);

        // create an ambient light
        AmbientLight ambLight = new AmbientLight(white);
        ambLight.setInfluencingBounds(this.bounds);
        this.sceneBG.addChild(ambLight);

        // create a directional light
        Vector3f dir = new Vector3f(-1.0f, -1.0f, -1.0f);
        DirectionalLight dirLight = new DirectionalLight(white, dir);
        dirLight.setInfluencingBounds(this.bounds);
        this.sceneBG.addChild(dirLight);
    } // end of lightScene()

    private void addBackground() {
        Background back = new Background();
        back.setApplicationBounds(this.bounds);
        back.setColor(0.05f, 0.05f, 0.2f); // dark blue
        this.sceneBG.addChild(back);
    } // end of addBackground()

    private void mouseControls(OverlayCanvas c, NetFourByFour fbf, TransformGroup tg)
    // Create the mouse pick and drag behavior
    {
        PickDragBehavior mouseBeh = new PickDragBehavior(c, fbf, this.sceneBG, tg);
        mouseBeh.setSchedulingBounds(this.bounds);
        this.sceneBG.addChild(mouseBeh);
    } // end of mouseControls()

    private void initUserPosition()
    // Set the user's initial viewpoint.
    {
        View view = this.su.getViewer().getView();
        view.setProjectionPolicy(View.PARALLEL_PROJECTION); // Use parallel
        // projection

        // scale up and move back
        TransformGroup steerTG = this.su.getViewingPlatform().getViewPlatformTransform();
        Transform3D t3d = new Transform3D();
        t3d.set(65.0f, new Vector3f(0.0f, 0.0f, 400.0f));
        steerTG.setTransform(t3d);
    } // end of initUserPosition()

    // ------------------------ game poles -------------------

    private BranchGroup makePoles()
    /*
     * Create 16 poles (cylinders) which will each appear to support 4 position markers (spheres).
     */
    {
        Color3f grey = new Color3f(0.25f, 0.25f, 0.25f);
        Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
        Color3f diffuseWhite = new Color3f(0.7f, 0.7f, 0.7f);
        Color3f specularWhite = new Color3f(0.9f, 0.9f, 0.9f);

        // Create the pole appearance
        Material poleMaterial = new Material(grey, black, diffuseWhite, specularWhite, 110.f); // and shiny
        poleMaterial.setLightingEnable(true);
        Appearance poleApp = new Appearance();
        poleApp.setMaterial(poleMaterial);

        BranchGroup bg = new BranchGroup();
        float x = -30.0f;
        float z = -30.0f;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Transform3D t3d = new Transform3D();
                t3d.set(new Vector3f(x, 0.0f, z));
                TransformGroup tg = new TransformGroup(t3d);
                Cylinder cyl = new Cylinder(1.0f, 60.0f, poleApp);
                cyl.setPickable(false); // user cannot select the poles
                tg.addChild(cyl);
                bg.addChild(tg);
                x += 20.0f;
            }
            x = -30.0f;
            z += 20.0f;
        }
        return bg;
    } // end of makePoles()

    // -------------------- position method

    public void tryPosn(int posn, int pid)
    // called by top-level through JFrame
    {
        this.board.tryPosn(posn, pid);
    }

} // end of WrapNetFBF class
