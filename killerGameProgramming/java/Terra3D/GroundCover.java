package Terra3D;

// GroundCover.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* GroundCover decorates the landscape with ground cover objects
 specified in a 'GC' file.

 GroundCover reads in a 'GC' file, which should have the
 name /models/<fnm>GC.txt

 The format of the file is:
 range [ <min> <max> ]
 <gc file1> <scale1> <number1>
 <gc file2> <scale2> <number2>
 :

 The range specifies the height range within which the ground
 cover should appear. Note: the 'range' word must be included
 even if there are no <min> or <max> values supplied.

 The 'range' keyword triggers the loading of the .obj file called
 /models/<fnm>.obj as a text file, so that the (x,y,z) coordinates
 of its mesh can be stored as Vector3d objects in the coords
 ArrayList.

 Each of the <gc file>s are loaded using a GroundShape object.
 The GroundShape object is wrapped in a SharedGroup node, so that
 only one object is needed rather than <number> of them.
 
 Each shared version of the GroundShape object is placed at a random
 location somewhere on the surface of the landscape, by randomly
 selecting a coordinate from the coords list.

 <scale> will scale the ground cover image. Experiment until the
 image looks right! 90 is a good starting value.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Link;
import javax.media.j3d.SharedGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;

public class GroundCover {
    private ArrayList coords; // OBJ file mesh coordinates (as Vector3d
    // objects)

    private BranchGroup coverBG; // top-level BG for all the ground cover

    public GroundCover(String fnm) {
        this.coords = new ArrayList();
        this.coverBG = new BranchGroup();

        loadCoverInfo(fnm);
    } // end of GroundCover()

    // --------------- loading methods ----------------------

    private void loadCoverInfo(String fnm)
    // load the 'GC' file /models/<fnm>GC.txt
    {
        String gcFile = new String("models/" + fnm + "GC.txt");
        System.out.println("Loading ground cover file: " + gcFile);
        try {
            BufferedReader br = new BufferedReader(new FileReader(gcFile));
            String line;

            if ((line = br.readLine()) != null) {
                // line
                loadObj(fnm, line);
            } else {
                System.out.println(gcFile + " is empty!");
                return;
            }

            // read in the multiple <gc file> <scale> <number> lines
            StringTokenizer tokens;
            String gcFnm;
            int numItems = 0;
            while ((line = br.readLine()) != null) {
                tokens = new StringTokenizer(line);
                gcFnm = tokens.nextToken();
                double scale = Double.parseDouble(tokens.nextToken());
                int gcNo = Integer.parseInt(tokens.nextToken());
                loadGC(gcFnm, scale, gcNo); // load a GroundShape object for
                // this gc
                numItems++;
            }
            System.out.println(numItems + " ground cover types");
            br.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    } // end of loadCoverInfo()

    private void loadObj(String fnm, String line)
    /*
     * line is range [ <min> <max> ] The OBJ file /models/<fnm>.obj is loaded as a text file, and the coordinates of its mesh are stored in the ArrayList coords.
     * 
     * However, any coordinates with heights outside the range min-max are not stored.
     */

    {
        StringTokenizer tokens = new StringTokenizer(line);
        tokens.nextToken(); // skip 'range' word

        if (tokens.hasMoreTokens()) {
            try {
                double min = Double.parseDouble(tokens.nextToken());
                double max = Double.parseDouble(tokens.nextToken());
                readObj(fnm, min, max, true);
                System.out.println("Ground cover ranges -- min: " + min + "; max: " + max);
                return;
            } catch (NumberFormatException ex) {
                System.out.println("min/max values must be double");
            }
        }

        readObj(fnm, 0, 0, false); // default loading; min == max == 0
    } // end of loadObj()

    private void readObj(String fnm, double min, double max, boolean hasRange)
    /*
     * The OBJ file is loaded as a text file. The data present should be a series of mesh coordinates, of the format: v x-coord y-coord z-coord
     * 
     * The coords are added to the coords ArrayList as Vector3d objects, except for ones with heights (z-axis values) outside the min-max range. We also exclude points on the edge
     * of the landscape when x or y == 0.
     * 
     * The only other data generated by Terragen is face info, of the form: f index1 index2 index3 which is ignored here.
     * 
     * A good description of the OBJ file format can be found in Java 3D's documentation for the ObjectFile class.
     * 
     * An OBJ file can be opened by any good text editor, but one that is capable of loading multi-megabyte size files.
     */
    {
        String objFile = new String("models/" + fnm + ".obj");
        System.out.println("Loading terrain mesh file: " + objFile);

        try {
            BufferedReader br = new BufferedReader(new FileReader(objFile));
            String line;
            StringTokenizer tokens;
            double xCoord, yCoord, zCoord;
            int numExcluded = 0;

            while ((line = br.readLine()) != null) {
                // System.out.println(line);
                if (line.startsWith("v")) {
                    tokens = new StringTokenizer(line); // line is v x y z
                    tokens.nextToken(); // skip 'v'
                    xCoord = Double.parseDouble(tokens.nextToken());
                    yCoord = Double.parseDouble(tokens.nextToken());
                    zCoord = Double.parseDouble(tokens.nextToken());

                    // System.out.println(" (" + xCoord + ", " + yCoord +
                    // ", " + zCoord + ")");
                    if (hasRange) {
                        if ((zCoord >= min) && (zCoord <= max)) {
                            this.coords.add(new Vector3d(xCoord, yCoord, zCoord));
                        } else {
                            numExcluded++;
                        }
                    } else {
                        if ((xCoord != 0) && (zCoord != 0)) {
                            this.coords.add(new Vector3d(xCoord, yCoord, zCoord));
                        } else {
                            numExcluded++;
                        }
                    }
                }
            }
            br.close();
            // System.out.println("No. of coords: " + coords.size());
            // System.out.println("Coords excluded: " + numExcluded);
        } catch (Exception e) {
            System.out.println("Error reading: " + objFile);
        }
    } // end of readObj()

    private void loadGC(String gcFnm, double scale, int gcNo)
    /*
     * Each GC file is loaded as a GroundShape object, then wrapped in a SharedGroup (SG) node so that only one object is required rather than gcNo of them.
     * 
     * Multiple TransfromGroups are created, which place the SG at random positions on the landscape.
     */
    {
        String gcFile = new String("models/" + gcFnm);
        System.out.println("Loading GC file: " + gcFile + "; scale: " + scale + "; " + gcNo + " copies");

        SharedGroup gcSG = new SharedGroup();
        gcSG.addChild(new GroundShape((float) scale, gcFile));
        gcSG.setPickable(false); // so not pickable in scene

        Vector3d coordVec;
        for (int i = 0; i < gcNo; i++) { // create multiple TGs using same SG
            coordVec = selectCoord();
            placeCover(gcSG, coordVec);
        }
    } // end of loadGC()

    private Vector3d selectCoord()
    // randomly select a landscape coordinate
    {
        int index = (int) Math.floor(Math.random() * this.coords.size());
        return (Vector3d) this.coords.get(index);
    }

    private void placeCover(SharedGroup gcSG, Vector3d coordVec)
    /*
     * Place ground cover onto the landscape at coordVec. The resulting subgraph is: coverBG -----> posTG ----> rotTG ----> Link ---> gcSG
     * 
     * Multiple copies of the ground cover will have their own position, rotation, and Link nodes, but the Link nodes all link to the same SharedGroup node.
     */
    {
        Transform3D t3d = new Transform3D();
        t3d.rotX(Math.PI / 2.0); // to counter the -ve rotation of the land
        // since using XY as floor
        TransformGroup rotTG = new TransformGroup(t3d);
        rotTG.addChild(new Link(gcSG));

        Transform3D t3d1 = new Transform3D();
        t3d1.set(coordVec);
        TransformGroup posTG = new TransformGroup(t3d1);
        posTG.addChild(rotTG);

        this.coverBG.addChild(posTG);
    } // end of placeCover()

    public BranchGroup getCoverBG()
    // used by the Landscape object
    {
        return this.coverBG;
    }

} // end of GroundCover class
