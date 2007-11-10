package Terra3D;

// Landscape.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Landscape loads a landscape made up of a mesh and a texture.
 If the user supplied the filename, fname, then Landscape looks
 in models/fname.obj for the mesh, and models/fname.jpg for the
 texture.

 The resulting BranchGroup, landBG, is added to the scene (sceneBG).

 The Landscape may contain two kinds of scenery:

 * full 3D shapes, loaded with PropManager.
 Intended for irregular objects which the user can move around,
 and perhaps enter (e.g. a castle).

 * ground cover, which are 2D images that rotate to always face
 the user. Intended for simple, symmetrical objects that
 'decorate' the scene, such as trees, bushes.

 Since many copies will be required, a SharedGroup node is
 used for each unique piece of ground cover.
 Ground cover is managed by a GroundCover object.

 The Landscape is surrounded by four walls which are covered
 in a mountain range image.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Geometry;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Node;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TexCoordGeneration;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4f;

import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.picking.PickTool;

public class Landscape {
    private static final double LAND_LEN = 60.0;

    // length of landscape in world coordinates
    private static final String WALL_PIC = "models/mountain2Sq.jpg";

    // texture used for the four walls around the landscape

    private BranchGroup sceneBG;

    private BranchGroup landBG = null; // holding the landscape parts

    private Shape3D landShape3D = null; // holding the landscape mesh

    // various mesh dimensions, set in getLandDimensions()
    private double landLength, minHeight, maxHeight;

    private double scaleLen;

    private Vector3d originVec = null;

    // where the user starts in the landscape (in world coordinates)

    public Landscape(BranchGroup sceneBG, String fname) {
        this.sceneBG = sceneBG;
        loadMesh(fname); // initialize landBG
        getLandShape(this.landBG); // initialize landShape3D

        // set the picking capabilities so that intersection
        // coords can be extracted after the shape is picked
        PickTool.setCapabilities(this.landShape3D, PickTool.INTERSECT_COORD);

        getLandDimensions(this.landShape3D); // extracts sizes from landShape3D

        makeScenery(this.landBG, fname); // add any scenery
        addWalls(); // walls around the landscape

        GroundCover gc = new GroundCover(fname);
        this.landBG.addChild(gc.getCoverBG()); // add any ground cover

        addLandtoScene(this.landBG);
        addLandTexture(this.landShape3D, fname);
    } // end Landscape()

    private void loadMesh(String fname)
    /*
     * Load the terrain mesh from models/fname.obj. We use Java 3D's OBJ loader. Then assign its BranchGroup to landBG.
     */
    {
        String fn = new String("models/" + fname + ".obj"); // assume OBJ file
        // in models/
        System.out.println("Loading terrain mesh from: " + fn + " ...");
        try {
            ObjectFile f = new ObjectFile(); // ObjectFile.STRIPIFY
            Scene loadedScene = f.load(fn);

            if (loadedScene == null) {
                System.out.println("Scene not found in: " + fn);
                System.exit(0);
            }

            this.landBG = loadedScene.getSceneGroup(); // the land's BG
            if (this.landBG == null) {
                System.out.println("No land branch group found");
                System.exit(0);
            }
        } catch (IOException ioe) {
            System.err.println("Terrain mesh load error: " + fn);
            System.exit(0);
        }
    } // end of loadMesh()

    private void getLandShape(BranchGroup landBG)
    /*
     * Check that landBG (the mesh's BranchGroup) contains a single Shape3D, and assign it to landShape3D for later. Also check that the shape holds a single GeometryArray.
     * 
     * landShape3D is later used for picking, and its dimensions are extracted.
     */
    {
        if (landBG.numChildren() > 1) {
            System.out.println("More than one child in land branch group");
        }

        Node node = landBG.getChild(0);
        if (!(node instanceof Shape3D)) {
            System.out.println("No Shape3D found in land branch group");
            System.exit(0);
        }
        this.landShape3D = (Shape3D) node;

        if (this.landShape3D == null) {
            System.out.println("Land Shape3D has no value");
            System.exit(0);
        }

        if (this.landShape3D.numGeometries() > 1) {
            System.out.println("More than 1 geometry in land branch group");
        }

        Geometry g = this.landShape3D.getGeometry();
        if (!(g instanceof GeometryArray)) {
            System.out.println("No Geometry Array found in land Shape3D");
            System.exit(0);
        }
    } // end of getLandShape()

    private void getLandDimensions(Shape3D landShape3D)
    /*
     * If we reach this point, then landShape3D contains a single GeometryArray of points representing the landscape's mesh.
     * 
     * We can get the dimensions of the mesh by using a BoundingBox.
     * 
     * Check if the XY plane is the 'floor' of the landscape, which is how the TerraGen mesh should have been exported. The X and Y lengths should be the same, and start at (0,0).
     * 
     * Set the following vars: landLength // length of the X (and Y) side scaleLen // the scaling necessary to fit landLength // into LAND_LEN units in the world minHeight and
     * maxHeight // along the Z axis
     */
    {
        // get the bounds of the shape
        BoundingBox boundBox = new BoundingBox(landShape3D.getBounds());
        Point3d lower = new Point3d();
        Point3d upper = new Point3d();
        boundBox.getLower(lower);
        boundBox.getUpper(upper);
        System.out.println("lower: " + lower + "\nupper: " + upper);

        if ((lower.y == 0) && (upper.x == upper.y)) {
            // System.out.println("XY being used as the floor");
        } else if ((lower.z == 0) && (upper.x == upper.z)) {
            System.out.println("Error: XZ set as the floor; change to XY in Terragen");
            System.exit(0);
        } else {
            System.out.println("Cannot determine floor axes");
            System.out.println("Y range should == X range, and start at 0");
            System.exit(0);
        }

        this.landLength = upper.x;
        this.scaleLen = LAND_LEN / this.landLength;
        System.out.println("scaleLen: " + this.scaleLen);
        this.minHeight = lower.z;
        this.maxHeight = upper.z;
    } // end of getLandDimensions()

    private void addLandtoScene(BranchGroup landBG)
    /*
     * The floor of the landscape is the XY plane, starting at (0,0), with sides of landLength units.
     * 
     * The landscape (landBG) is rotated to lie on the XZ plane, scaled to have floor sides of length LAND_LEN (scaleLen = LAND_LEN/landLength), then translated so that the center
     * of the landscape is at (0,0).
     */
    {
        // rotate and scale land
        Transform3D t3d = new Transform3D();
        t3d.rotX(-Math.PI / 2.0); // so land's XY is resting on the XZ plane
        t3d.setScale(new Vector3d(this.scaleLen, this.scaleLen, this.scaleLen)); // scale
        TransformGroup sTG = new TransformGroup(t3d);
        sTG.addChild(landBG);

        // center the land, which starts at (0,0) on the XZ plane,
        // so move it left and forward
        Transform3D t3d1 = new Transform3D();
        t3d1.set(new Vector3d(-LAND_LEN / 2, 0, LAND_LEN / 2)); // translate
        TransformGroup posTG = new TransformGroup(t3d1);
        posTG.addChild(sTG);

        this.sceneBG.addChild(posTG);
    } // end of addLandtoScene()

    // ----------------------- add texture to the land ------------------

    private void addLandTexture(Shape3D shape, String fname)
    /*
     * The land texture (in models/fname.jpg) shows the landscape from above, and is used to colour the mesh loaded from the OBJ file.
     * 
     * To save memory, the shape is one-sided. The texture is blended with a white material so that lighting effects can be shown.
     */
    {
        Appearance app = shape.getAppearance();

        // generate texture coords that 'stretch' the texture over the mesh
        app.setTexCoordGeneration(stampTexCoords(shape));

        // combine texture with colour and lighting of underlying surface
        TextureAttributes ta = new TextureAttributes();
        ta.setTextureMode(TextureAttributes.MODULATE);
        app.setTextureAttributes(ta);

        // apply texture to shape
        Texture2D tex = loadLandTexture(fname);
        if (tex != null) {
            app.setTexture(tex);
            shape.setAppearance(app);
        }
    } // end of addLandTexture()

    private Texture2D loadLandTexture(String fname)
    // load texture for colouring the landscape
    {
        String fn = new String("models/" + fname + ".jpg");
        TextureLoader texLoader = new TextureLoader(fn, null);
        Texture2D tex = (Texture2D) texLoader.getTexture();

        if (tex == null) {
            System.out.println("Cannot load land texture from " + fn);
        } else {
            System.out.println("Loaded land texture from " + fn);
            tex.setMagFilter(Texture.BASE_LEVEL_LINEAR);
            tex.setEnable(true);
        }
        return tex;
    } // end of loadLandTexture()

    private TexCoordGeneration stampTexCoords(Shape3D shape)
    /*
     * Adjust the genration planes so that the texture is 'stetched' over the entire mesh.
     * 
     * The mesh has equal X- and Y- axis sides of landLength, and the mesh's lower left-hand corner starts at (0,0), so the stretching is quite simple.
     */
    { /*
         * Adjust the generation planes so the mesh's XY plane is mapped to texture coordinates [0,0] and [1,1].
         */
        Vector4f planeS = new Vector4f((float) (1.0 / this.landLength), 0.0f, 0.0f, 0.0f);
        Vector4f planeT = new Vector4f(0.0f, (float) (1.0 / this.landLength), 0.0f, 0.0f);

        // generate new texture coordinates for GeometryArray
        TexCoordGeneration texGen = new TexCoordGeneration();
        texGen.setPlaneS(planeS);
        texGen.setPlaneT(planeT);

        return texGen;
    } // end of stampTexCoords()

    // --------------------- load scenery -------------------

    private void makeScenery(BranchGroup landBG, String fname)
    /*
     * The scenery file (models/fname.txt) has the format:
     * 
     * start x y z <object file> x y z scale <object file> x y z scale :
     * 
     * The start line must be included -- it says where the user is placed in the landscape. The scenery objects are optional.
     * 
     * Each scenery object is loaded with a PropManager object; we assume the existence of "coords" data files for the models.
     * 
     * The (x,y,z) values are in landscape coordinates, and scale makes the model the 'right' size for the landscape.
     */
    {
        boolean startSet = false;
        String sceneryFile = new String("models/" + fname + ".txt");
        System.out.println("Loading scenery file: " + sceneryFile);

        try {
            BufferedReader br = new BufferedReader(new FileReader(sceneryFile));
            String line, modelFnm;
            StringTokenizer tokens;
            double xCoord, yCoord, zCoord, scale;
            PropManager propMan;

            while ((line = br.readLine()) != null) {
                // System.out.println(line);
                tokens = new StringTokenizer(line);
                modelFnm = tokens.nextToken(); // may be a filename or 'start'
                xCoord = Double.parseDouble(tokens.nextToken());
                yCoord = Double.parseDouble(tokens.nextToken());
                zCoord = Double.parseDouble(tokens.nextToken());

                if (!modelFnm.equals("start")) {
                    scale = Double.parseDouble(tokens.nextToken());
                } else {
                    scale = 0.0; // no scale for the start line
                }

                System.out.println("\nAdding: " + modelFnm + " (" + xCoord + ", " + yCoord + ", " + zCoord + "), scale: " + scale);

                if (modelFnm.equals("start")) { // user's starting point
                    this.originVec = landToWorld(xCoord, yCoord, zCoord); // in
                    // world
                    // coords
                    startSet = true;
                } else {
                    propMan = new PropManager(modelFnm, true); // load scenery
                    placeScenery(landBG, propMan.getTG(), xCoord, yCoord, zCoord, scale);
                }
            }
            br.close();
            System.out.println();

            if (!startSet) {
                System.out.println("No starting position specified");
                System.exit(0); // give up if no starting position
            }
        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
    } // end of makeScenery()

    private Vector3d landToWorld(double xCoord, double yCoord, double zCoord)
    /*
     * Converting land to world coordinates involves scaling, translation, and rotation. The rotation is achieved by switching the y- and z- values.
     */
    {
        double x = (xCoord * this.scaleLen) - LAND_LEN / 2;
        double y = zCoord * this.scaleLen; // z-axis --> y-axis
        double z = (-yCoord * this.scaleLen) + LAND_LEN / 2; // y-axis --> z-axis
        return new Vector3d(x, y, z);
    } // end of landToWorld()

    private void placeScenery(BranchGroup landBG, TransformGroup modelTG, double x, double y, double z, double scale)
    /*
     * Add a scenery object to landBG using landscape coords. The object is translated, then rotated and scaled.
     */
    {
        modelTG.setPickable(false); // so not pickable in scene

        Transform3D t3d = new Transform3D();
        t3d.rotX(Math.PI / 2.0); // to counter the -ve rotation of land
        // since using XY as floor
        t3d.setScale(new Vector3d(scale, scale, scale)); // scaled
        TransformGroup scaleTG = new TransformGroup(t3d);
        scaleTG.addChild(modelTG);

        Transform3D t3d1 = new Transform3D();
        t3d1.set(new Vector3d(x, y, z)); // translated
        TransformGroup posTG = new TransformGroup(t3d1);
        posTG.addChild(scaleTG);

        landBG.addChild(posTG);
    } // end of placeScenery()

    private void addWalls()
    /*
     * Add 4 textured walls around the landscape. Each wall is a TexturedPlane object, which are placed using world coordinates, and so linked to sceneBG, not landBG. The texture
     * is hardwired to be WALL_PIC.
     */
    { // heights used for the walls, in world coords
        double minH = this.minHeight * this.scaleLen;
        double maxH = this.maxHeight * this.scaleLen;

        // the eight corner points
        // back, left
        Point3d p1 = new Point3d(-LAND_LEN / 2.0f, minH, -LAND_LEN / 2.0f);
        Point3d p2 = new Point3d(-LAND_LEN / 2.0f, maxH, -LAND_LEN / 2.0f);

        // front, left
        Point3d p3 = new Point3d(-LAND_LEN / 2.0f, minH, LAND_LEN / 2.0f);
        Point3d p4 = new Point3d(-LAND_LEN / 2.0f, maxH, LAND_LEN / 2.0f);

        // front, right
        Point3d p5 = new Point3d(LAND_LEN / 2.0f, minH, LAND_LEN / 2.0f);
        Point3d p6 = new Point3d(LAND_LEN / 2.0f, maxH, LAND_LEN / 2.0f);

        // back, right
        Point3d p7 = new Point3d(LAND_LEN / 2.0f, minH, -LAND_LEN / 2.0f);
        Point3d p8 = new Point3d(LAND_LEN / 2.0f, maxH, -LAND_LEN / 2.0f);

        // load and set the texture; set mag filter since the image is enlarged
        TextureLoader loader = new TextureLoader(WALL_PIC, null);
        Texture2D texture = (Texture2D) loader.getTexture();
        if (texture == null) {
            System.out.println("Cannot load wall image from " + WALL_PIC);
        } else {
            System.out.println("Loaded wall image: " + WALL_PIC);
            texture.setMagFilter(Texture.BASE_LEVEL_LINEAR);
        }

        // left wall; counter-clockwise
        this.sceneBG.addChild(new TexturedPlane(p3, p1, p2, p4, texture));
        // front wall; counter-clockwise from back
        this.sceneBG.addChild(new TexturedPlane(p5, p3, p4, p6, texture));
        // right wall
        this.sceneBG.addChild(new TexturedPlane(p7, p5, p6, p8, texture));
        // back wall
        this.sceneBG.addChild(new TexturedPlane(p1, p7, p8, p2, texture));
    } // end of addWalls()

    // ------------- public methods ------------------

    public Vector3d getOriginVec()
    // used by KeyBehavior
    {
        return this.originVec;
    }

    public BranchGroup getLandBG()
    // used by HeightFinder
    {
        return this.landBG;
    }

    public double getScaleLen()
    // used by HeightFinder
    {
        return this.scaleLen;
    }

    public boolean inLandscape(double x, double z)
    // is world (x,z) in the landscape? Used by KeyBehavior
    {
        Vector3d landVec = worldToLand(new Vector3d(x, 0, z));
        if ((landVec.x <= 0) || (landVec.x >= this.landLength) || (landVec.y <= 0) || (landVec.y >= this.landLength)) {
            return false;
        }
        return true;
    } // end of inLandscape()

    public Vector3d worldToLand(Vector3d worldVec)
    /*
     * Used by KeyBehavior Converting world to land coordinates involves translation, scaling, and rotation. The rotation is achieved by switching the y- and z- values.
     */
    {
        double xCoord = (worldVec.x + LAND_LEN / 2) / this.scaleLen;
        double yCoord = (-worldVec.z + LAND_LEN / 2) / this.scaleLen; // z-axis
        // -->
        // y-axis
        double zCoord = worldVec.y / this.scaleLen; // y-axis --> z-axis
        return new Vector3d(xCoord, yCoord, zCoord);
    } // end of worldToLand()

} // end of Landscape class
