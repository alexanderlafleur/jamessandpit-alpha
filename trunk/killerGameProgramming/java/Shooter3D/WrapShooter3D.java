package Shooter3D;

// WrapShooter3D.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Create the usual scene, and add a gun, laser beam, explosion
 and sounds in makeGun().
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.AudioDevice;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.MediaContainer;
import javax.media.j3d.PointSound;
import javax.media.j3d.Sound;
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

public class WrapShooter3D extends JPanel
// Holds the 3D canvas where the loaded image is displayed
{
    /**
     * 
     */
    private static final long serialVersionUID = -2951486319872287123L;

    private static final int PWIDTH = 512; // size of panel

    private static final int PHEIGHT = 512;

    private static final int BOUNDSIZE = 100; // larger than world

    private static final Point3d USERPOSN = new Point3d(0, 5, 20);

    // initial user position

    private SimpleUniverse su;

    private BranchGroup sceneBG;

    private BoundingSphere bounds; // for environment nodes

    public WrapShooter3D()
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

        // create an audio device
        AudioDevice audioDev = this.su.getViewer().createAudioDevice();

        createSceneGraph(canvas3D);
        initUserPosition(); // set user's viewpoint
        orbitControls(canvas3D); // controls for moving the viewpoint

        this.su.addBranchGraph(this.sceneBG);
    } // end of WrapShooter3D()

    private void createSceneGraph(Canvas3D canvas3D)
    // initilise the scene
    {
        this.sceneBG = new BranchGroup();
        /*
         * // used for picking debugging sceneBG.setUserData("the sceneBG node"); sceneBG.setCapability(BranchGroup.ENABLE_PICK_REPORTING);
         */
        this.bounds = new BoundingSphere(new Point3d(0, 0, 0), BOUNDSIZE);

        lightScene(); // add the lights
        addBackground(); // add the sky
        this.sceneBG.addChild(new CheckerFloor().getBG()); // add the floor

        makeGun(canvas3D);
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

    // ---------------------- gun -----------------

    private void makeGun(Canvas3D canvas3D)
    /*
     * The gun is a cylinder with a cone on top which can rotate. 'Inside' the cone is a small red cylinder, which is animated to become a 'laser beam'. When the user clicks on the
     * checkboard, the beam is fired at the location of the click. When it hits the target, an explosion occurs. There are sounds for the beam and explosion.
     */
    { // starting vector for the gun cone and beam
        Vector3d startVec = new Vector3d(0, 2, 0);

        // the gun
        GunTurret gun = new GunTurret(startVec);
        this.sceneBG.addChild(gun.getGunBG());

        // explosion and sound
        PointSound explPS = initSound("Explo1.wav");
        ExplosionsClip expl = new ExplosionsClip(startVec, explPS);
        this.sceneBG.addChild(expl.getExplBG());

        // laser beam and sound
        PointSound beamPS = initSound("laser2.wav");
        LaserBeam laser = new LaserBeam(startVec, beamPS);
        this.sceneBG.addChild(laser.getBeamBG());

        // the behaviour that controls the shooting
        ShootingBehaviour shootBeh = new ShootingBehaviour(canvas3D, this.sceneBG, this.bounds, new Point3d(0, 2, 0), expl, laser, gun);
        this.sceneBG.addChild(shootBeh);
    } // end of makeGun()

    private PointSound initSound(String filename)
    // create a point sound using filename (located at (0,0,0))
    {
        MediaContainer soundMC = null;
        try {
            soundMC = new MediaContainer("file:sounds/" + filename);
            soundMC.setCacheEnable(true); // load sound into media container
        } catch (Exception ex) {
            System.out.println(ex);
        }

        // create a point sound
        PointSound ps = new PointSound();
        ps.setSchedulingBounds(this.bounds);
        ps.setSoundData(soundMC);

        ps.setInitialGain(1.0f); // full on sound from the start

        ps.setCapability(Sound.ALLOW_ENABLE_WRITE); // can be switched
        // on/off
        ps.setCapability(PointSound.ALLOW_POSITION_WRITE); // position can be
        // adjusted

        System.out.println("PointSound created from sounds/" + filename);
        return ps;
    } // end of initSound()

} // end of WrapShooter3D class
