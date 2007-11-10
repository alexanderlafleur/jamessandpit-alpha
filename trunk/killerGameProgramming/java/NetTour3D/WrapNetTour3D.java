package NetTour3D;

// WrapNetTour3D.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Sets up a Tour3D world, but one including visitors from 
 other machines.

 New net-related code:
 * makeContact() for making contact with the TourServer;

 * closeLink() to break the link with the server and exit;

 * addVisitor() for creating distributed tourist(s),
 called by TourWatcher when the server sends it a request;

 * sendDetails() for sending sprite info to the server

 Minor differences from Tour3D:
 * no alien
 * no full-screen
 * no keyboard-driven quitting; use the close box instead
 * the frame is 500x300 rather than 512x512
 * light blue background, not a texture
 * position the tourist with command-line args
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DecimalFormat;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Group;
import javax.media.j3d.TransformGroup;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

public class WrapNetTour3D extends JPanel
// Holds the 3D canvas where the loaded image is displayed
{
    /**
     * 
     */
    private static final long serialVersionUID = 7559320695894751544L;

    private static final int PWIDTH = 500; // size of panel

    private static final int PHEIGHT = 300;

    private static final int BOUNDSIZE = 100; // larger than world

    private static final int PORT = 5555; // server details

    private static final String HOST = "localhost";

    private SimpleUniverse su;

    private BranchGroup sceneBG;

    private BoundingSphere bounds; // for environment nodes

    private Obstacles obs;

    private TourSprite bob; // the tourist

    private Socket sock;

    private BufferedReader in; // IO for the client

    private PrintWriter out;

    private DecimalFormat df; // for simpler output

    public WrapNetTour3D(String userName, String tourFnm, double xPosn, double zPosn)
    // construct the 3D canvas
    {
        setLayout(new BorderLayout());
        setOpaque(false);
        this.df = new DecimalFormat("0.####"); // 4 dp

        setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas3D = new Canvas3D(config);
        add("Center", canvas3D);

        canvas3D.setFocusable(true);
        canvas3D.requestFocus();

        this.su = new SimpleUniverse(canvas3D);
        createSceneGraph(userName, tourFnm, xPosn, zPosn);

        this.su.addBranchGraph(this.sceneBG);
    } // end of WrapNetTour3D()

    void createSceneGraph(String userName, String tourFnm, double xPosn, double zPosn)
    // initilise the scene
    {
        this.sceneBG = new BranchGroup();
        this.bounds = new BoundingSphere(new Point3d(0, 0, 0), BOUNDSIZE);

        // to allow clients to be added/removed from the world at run time
        this.sceneBG.setCapability(Group.ALLOW_CHILDREN_READ);
        this.sceneBG.setCapability(Group.ALLOW_CHILDREN_WRITE);
        this.sceneBG.setCapability(Group.ALLOW_CHILDREN_EXTEND);

        lightScene(); // add the lights
        addBackground(); // add the sky
        this.sceneBG.addChild(new CheckerFloor().getBG()); // add the floor

        makeScenery(tourFnm); // add scenery and obstacles

        makeContact(); // contact server (after Obstacles object created)

        addTourist(userName, xPosn, zPosn);
        // add the user-controlled 3D sprite
        this.sceneBG.compile(); // fix the scene
    } // end of createSceneGraph()

    private void lightScene()
    // One ambient light, 2 directional lights
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

    // --------------------- obstacles -----------------------

    private void makeScenery(String tourFnm)
    /*
     * Parse the tour file which contains the names of scenery objects to load, and the position of obstacles. The format of the tour filename is: <object file> [-o (x1,z1) (x2,z2)
     * ... ] // obstacle coordinates <object file> : The scenery is loaded with PropManager objects, and we assume the existence of "coords" data files for thscenery models.
     * 
     * The obstacles are stored in an Obstacles object, and then added to the scene.
     */
    {
        this.obs = new Obstacles();
        PropManager propMan;
        String tourFile = "models/" + tourFnm;
        System.out.println("Loading tour file:" + tourFile);
        try {
            BufferedReader br = new BufferedReader(new FileReader(tourFile));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                if (line.startsWith("-o")) {
                    this.obs.store(line.substring(2).trim()); // save obstacle
                } else {
                    propMan = new PropManager(line.trim(), true); // load
                    // scenery
                    this.sceneBG.addChild(propMan.getTG()); // add to world
                }
            }
            br.close();
            // System.out.println("Finished reading tour file: " + tourFile);
            // obs.print(); // for debugging
            this.sceneBG.addChild(this.obs.getObsGroup()); // add obstacles to the
            // scene
        } catch (IOException e) {
            System.out.println("Error reading tour file: " + tourFile);
            System.exit(1);
        }
    } // end of makeScenery()

    // --------------------- tourists ---------------------------

    private void addTourist(String userName, double xPosn, double zPosn)
    // create sprite for this client
    {
        this.bob = new TourSprite(userName, "Coolrobo.3ds", this.obs, xPosn, zPosn, this.out); // sprite
        this.sceneBG.addChild(this.bob.getBG());

        ViewingPlatform vp = this.su.getViewingPlatform();
        TransformGroup viewerTG = vp.getViewPlatformTransform(); // view
        // point TG

        TouristControls tcs = new TouristControls(this.bob, viewerTG); // sprite's
        // controls
        tcs.setSchedulingBounds(this.bounds);

        this.sceneBG.addChild(tcs);
    } // end of addTourist()

    public DistTourSprite addVisitor(String userName, double xPosn, double zPosn, double rotRadians)
    /*
     * Create a sprite for a visitor to the world. This method is called from TourWatcher, in response to a request from the TourServer.
     * 
     * There were intermittent bugs with adding a new BranchGroup at run time. Solved (I think) by compiling the new BranchGroup, and delaying a little before adding it.
     */
    {
        DistTourSprite dtSprite = new DistTourSprite(userName, "Coolrobo.3ds", this.obs, xPosn, zPosn);
        if (rotRadians != 0) {
            dtSprite.setCurrRotation(rotRadians);
        }

        BranchGroup sBG = dtSprite.getBG();

        sBG.compile(); // generally a good idea
        try {
            Thread.sleep(200); // delay a little, so world has been finished
        } catch (InterruptedException e) {
        }
        this.sceneBG.addChild(sBG);

        if (!sBG.isLive()) {
            System.out.println("Visitor Sprite is NOT live");
        } else {
            System.out.println("Visitor Sprite is now live");
        }

        return dtSprite;
    } // end of addVisitor()

    // ------------------------- network related --------------------------

    private void makeContact()
    /*
     * Contact the server, and set up a TourWatcher to monitor the server.
     */
    {
        try {
            this.sock = new Socket(HOST, PORT);
            this.in = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
            this.out = new PrintWriter(this.sock.getOutputStream(), true); // autoflush

            new TourWatcher(this, this.in, this.obs).start(); // start watching for server
            // msgs
        } catch (Exception e) {
            System.out.println("No contact with server");
            System.exit(0);
        }
    } // end of makeContact()

    public void closeLink()
    /*
     * Called by the top-level JFrame when the close box is clicked. Say goodbye to server, and exit. This will kill the TourWatcher thread as well.
     */
    {
        try {
            this.out.println("bye"); // tell server that client is disconnecting
            this.sock.close();
        } catch (Exception e) {
            System.out.println("Link terminated");
        }

        System.exit(0);
    } // end of closeLink()

    public void sendDetails(String cliAddr, String strPort)
    /*
     * Send details of local sprite to the client at location (cliAddr, strPort). Msg format: detailsFor cliAddr strPort xPosn zPosn rotRadians
     */
    {
        Point3d currLoc = this.bob.getCurrLoc();
        double currRotation = this.bob.getCurrRotation();
        String msg = new String("detailsFor " + cliAddr + " " + strPort + " " + this.df.format(currLoc.x) + " " + this.df.format(currLoc.z) + " " + this.df.format(currRotation));
        this.out.println(msg);
    } // end of sendDetails()

} // end of WrapNetTour3D class
