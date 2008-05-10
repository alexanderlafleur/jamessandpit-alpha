package AlienTiles;

// WorldDisplay.java 
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* WorldDisplay manages:
 - a moving tile floor, represented by a single GIF
 - no-go areas on the floor
 - blocks occupying certain tiles
 - pickups occupying certain tiles
 - the player and aliens sprites

 WorldDisplay manages the communication between the player and the
 sprites so that it can monitor/control the interactions.

 The 3 main data structures used here are:
 - an onstacles[][] array holding info on which tiles
 are no-gps or hold blocks.

 - a WorldItems objects which stores blocks, pickups, and
 sprites in row order to make them easier to draw with
 the correct z-ordering

 - a numPickups counter to record how many pickups ae still
 left to be picked up

 The methods fall into 5 main groups:
 - loading of floor info;           // info related to the floor image
 - loading of world objects info    // info about no-gos, blocks, pickups
 - pickups related
 - sprites related
 - others, e.g. draw()
 */

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class WorldDisplay {
    private final static String WORLD_DIR = "World/";

    // types of tile occupier
    public static final int BLOCK = 0;

    public static final int PICKUP = 1;

    public static final int SPRITE = 2;

    // world size in number of tiles
    private int numXTiles, numYTiles;

    // max pixel width/height of a tile (including any space to next tile)
    private int tileWidth, tileHeight;

    // 'start of first even row' coordinate
    /*
     * The coordinate is the top-left corner of the rectangle containing the diamond shape.
     */
    private int evenRowX, evenRowY;

    // 'start of first odd row' coordinate
    private int oddRowX, oddRowY;

    /*
     * Pixel offset for drawing the top-left corner of the floor image (and all of its contents) relative to the top-left corner (0,0) of the JPanel.
     */
    private int xOffset, yOffset;

    private ImagesLoader imsLoader;

    private AlienTilesPanel atPanel;

    private BufferedImage floorIm; // the floor image (the background)

    private boolean obstacles[][];

    // specify which tiles are no-gos, or are occupied by blocks;
    // pickups are not classed as aobstacles -- sprites can walk through them

    private WorldItems wItems;

    /*
     * WorldItems maintains a list of things that appear on the tile floor: blocks, pickups, and sprites, including where they are. The list is in back-to-front row order to make
     * the drawing of the scene use the right z-ordering.
     */

    private int numPickups = 0; // current no. of pickups in the world

    private int blocksCounter = 0;

    // a counter used to label the blocks as they are stored in WorldItems

    private PlayerSprite player; // sprites

    private AlienSprite aliens[];

    public WorldDisplay(ImagesLoader imsLd, AlienTilesPanel atp) {
        this.imsLoader = imsLd;
        this.atPanel = atp;

        this.xOffset = 0;
        this.yOffset = 0;
        loadFloorInfo("worldInfo.txt");

        /*
         * The WorldItem object uses the floor info data for deciding where the world objects should be drawn in the JPanel.
         */
        this.wItems = new WorldItems(this.tileWidth, this.tileHeight, this.evenRowX, this.evenRowY, this.oddRowX, this.oddRowY);
        initObstacles();

        loadWorldObjects("worldObjs.txt");
        /*
         * The world objects information is stored in the obstacles[][] array and the WorldItems object.
         */
    } // end of WorldDisplay()

    // ----------------- load floor info ------------------

    private void loadFloorInfo(String wFNm)
    /*
     * The format of the input lines are: image <name> // the name of the floor GIF numTiles x y // number of tiles in the x- and y- directions dimTile w h // the width and height
     * of a single tile evenRow x y // start of first even row oddRow x y // start of first odd row and blank lines and comment lines.
     */
    {
        String worldFNm = WORLD_DIR + wFNm;
        System.out.println("Reading file: " + worldFNm);
        try {
            InputStream in = this.getClass().getResourceAsStream(worldFNm);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            // BufferedReader br = new BufferedReader( new
            // FileReader(worldFNm));
            String line;
            String[] tokens;
            while ((line = br.readLine()) != null) {
                if (line.length() == 0) {
                    continue;
                }
                if (line.startsWith("//")) {
                    continue;
                }

                // System.out.println("Line: " + line);
                tokens = line.split("\\s+");
                if (tokens[0].equals("image")) {
                    this.floorIm = this.imsLoader.getImage(tokens[1]); // load image
                } else if (tokens[0].equals("numTiles")) {
                    this.numXTiles = getNumber(tokens[1]);
                    this.numYTiles = getNumber(tokens[2]);
                } else if (tokens[0].equals("dimTile")) {
                    this.tileWidth = getNumber(tokens[1]);
                    this.tileHeight = getNumber(tokens[2]);
                } else if (tokens[0].equals("evenRow")) {
                    this.evenRowX = getNumber(tokens[1]);
                    this.evenRowY = getNumber(tokens[2]);
                } else if (tokens[0].equals("oddRow")) {
                    this.oddRowX = getNumber(tokens[1]);
                    this.oddRowY = getNumber(tokens[2]);
                } else {
                    System.out.println("Do not recognize line: " + line);
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error reading file: " + worldFNm);
            System.exit(1);
        }
    } // end of loadFloorInfo()

    private int getNumber(String token)
    // extract a number or return 0
    {
        int num = 0;
        try {
            num = Integer.parseInt(token);
        } catch (NumberFormatException ex) {
            System.out.println("Incorrect format for " + token);
        }
        return num;
    } // end of getNumber()

    // ----------------- load world objects info ----------------

    private void loadWorldObjects(String woFNm)
    /*
     * There are three kinds of world objects: no-gos (n), blocks (b), pickups (p), which are positioned at a given tile coordinate (x,y). The block and pickup names refer to their
     * image filenames.
     * 
     * The format of the input lines are: n <x1>-<y1> <x2>-<y2> ..... .... #
     * 
     * b <blockName> <x1>-<y1> <x2>-<y2> ..... .... #
     * 
     * p <pickupName> <x>-<y>
     * 
     * and blank lines and comment lines.
     */
    {
        String objsFNm = WORLD_DIR + woFNm;
        System.out.println("Reading file: " + objsFNm);
        try {
            InputStream in = this.getClass().getResourceAsStream(objsFNm);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            // BufferedReader br = new BufferedReader( new FileReader(objsFNm));
            String line;
            char ch;
            while ((line = br.readLine()) != null) {
                if (line.length() == 0) {
                    continue;
                }
                if (line.startsWith("//")) {
                    continue;
                }
                ch = Character.toLowerCase(line.charAt(0));
                if (ch == 'n') {
                    getObstacles(line.substring(1), br); // skip 'n' at start
                } else if (ch == 'b') {
                    getBlocks(line, br);
                } else if (ch == 'p') {
                    getPickup(line);
                } else {
                    System.out.println("Do not recognize line: " + line);
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error reading file: " + objsFNm);
            System.exit(1);
        }
    } // end of loadWorldObjects()

    private void getObstacles(String line, BufferedReader br)
    /*
     * Read multiple coordinate lines until the line contains a #: <x1>-<y1> (x2>-<y2> ..... .... #
     */
    {
        boolean reachedEnd = getObstaclesLine(line);
        try {
            while (!reachedEnd) {
                line = br.readLine();
                if (line == null) {
                    System.out.println("Unexpected end of obstacles info");
                    System.exit(1);
                }
                reachedEnd = getObstaclesLine(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading obstacles info");
            System.exit(1);
        }
    } // end of getObstacles

    private boolean getObstaclesLine(String line)
    /*
     * Format of a line: <x1>-<y1> (x2>-<y2> ..... [ # ] Each one is used tp set obstacles[x][y] to true
     */
    {
        StringTokenizer tokens = new StringTokenizer(line);
        String token;
        Point coord;
        while (tokens.hasMoreTokens()) {
            token = tokens.nextToken();
            if (token.equals("#")) {
                return true;
            }
            coord = getCoord(token);
            this.obstacles[coord.x][coord.y] = true; // set the obstacle
            // System.out.println("Added coordinate (" + coord.x + "," + coord.y
            // + ")");
        }
        return false;
    } // end of getObstaclesLine()

    private Point getCoord(String token)
    // Token's format is <x>-<y>; return it as a Point object
    {
        int x = 0;
        int y = 0;
        String[] results = token.split("-");
        if (results.length != 2) {
            System.out.println("Incorrect coordinates in " + token);
        } else {
            try {
                x = Integer.parseInt(results[0]);
                y = Integer.parseInt(results[1]);
            } catch (NumberFormatException ex) {
                System.out.println("Incorrect format for coordinates in " + token);
            }
        }

        if (x >= this.numXTiles) {
            System.out.println("x coordinate too large in " + token);
            x = this.numXTiles - 1;
        }
        if (y >= this.numYTiles) {
            System.out.println("x coordinate too large in " + token);
            x = this.numYTiles - 1;
        }

        return new Point(x, y);
    } // end of getCoord()

    private void getBlocks(String line, BufferedReader br)
    /*
     * b <blockName> <x1>-<y1> (x2>-<y2> ..... .... #
     * 
     * Each <blockName> will be stored with a unique name (<blockName> + blocksCounter) along with the image stored in Images/<blockName>.gif
     */
    {
        boolean reachedEnd = false;
        StringTokenizer tokens = new StringTokenizer(line);
        tokens.nextToken(); // skip 'b'
        String blockName = tokens.nextToken(); // name of block
        BufferedImage blockIm = this.imsLoader.getImage(blockName);

        try {
            while (!reachedEnd) {
                line = br.readLine();
                if (line == null) {
                    System.out.println("Unexpected end of blocks info");
                    System.exit(1);
                }
                reachedEnd = getBlocksLine(line, blockName, blockIm);
            }
        } catch (IOException e) {
            System.out.println("Error reading blocks info");
            System.exit(1);
        }
    } // end of getBlocks()

    private boolean getBlocksLine(String line, String blockName, BufferedImage im)
    /*
     * Format of a line: <x1>-<y1> (x2>-<y2> ..... [ # ]
     * 
     * Each coordinate is stored as a new entry in the WorldItems object and obstacles[x][y] is set to true.
     */
    {
        StringTokenizer tokens = new StringTokenizer(line);
        String token;
        Point coord;
        while (tokens.hasMoreTokens()) {
            token = tokens.nextToken();
            if (token.equals("#")) {
                return true;
            }
            coord = getCoord(token);

            // store in WorldItems and add an obstacle
            this.wItems.addItem(blockName + this.blocksCounter, BLOCK, coord.x, coord.y, im);
            this.obstacles[coord.x][coord.y] = true;
            // System.out.println("Added " + blockName + blocksCounter +
            // " at (" + coord.x + "," + coord.y + ")");
            this.blocksCounter++;
        }
        return false;
    } // end of getBlocksLine()

    private void getPickup(String line)
    /*
     * Format of a line: p <pickupName> <x>-<y>
     * 
     * The coordinate is stored as a new entry in the WorldItems object.
     */
    {
        StringTokenizer tokens = new StringTokenizer(line);
        tokens.nextToken(); // skip 'p'

        String pickupName = tokens.nextToken(); // name of pickup
        BufferedImage pickupIm = this.imsLoader.getImage(pickupName);

        Point coord = getCoord(tokens.nextToken());
        this.wItems.addItem(pickupName, PICKUP, coord.x, coord.y, pickupIm);
        // System.out.println("Added " + pickupName +
        // " at (" + coord.x + "," + coord.y + ")");
        this.numPickups++;
    } // end of getPickup()

    // ---------------------------- others -----------------------

    private void initObstacles()
    // initially there are no obstacles in the world
    {
        this.obstacles = new boolean[this.numXTiles][this.numYTiles];
        for (int i = 0; i < this.numXTiles; i++) {
            for (int j = 0; j < this.numYTiles; j++) {
                this.obstacles[i][j] = false;
            }
        }
    } // end of initObstacles()

    public boolean validTileLoc(int x, int y)
    // Is tile coord (x,y) on the tile map and not contain an obstacle?
    {
        if ((x < 0) || (x >= this.numXTiles) || (y < 0) || (y >= this.numYTiles)) {
            return false;
        }
        if (this.obstacles[x][y]) {
            return false;
        }
        return true;
    } // end of validTileLoc()

    public void draw(Graphics g)
    /*
     * Draw the world (with all its contents) to the screen. The WorldItems object already contains the blocks and pickups.
     * 
     * The sprites must be added, and then WorldItems' draw() method is called. The sprites are then removed, since they will probably move before the next draw, and will need to
     * be added again in a new position.
     */
    {
        g.drawImage(this.floorIm, this.xOffset, this.yOffset, null); // draw the floor image
        this.wItems.positionSprites(this.player, this.aliens); // add the sprites
        this.wItems.draw(g, this.xOffset, this.yOffset); // draw the things in the game
        this.wItems.removeSprites(); // remove the sprites
    } // end of draw()

    // ------------------ pickups related --------------------

    public String overPickup(Point pt)
    /*
     * Is the tile location, pt, occupied by a pickup? findPickupName() will return null if there is no pickup there. This method is called by PlayerSprite
     */
    {
        return this.wItems.findPickupName(pt);
    }

    public void removePickup(String name)
    /*
     * Delete the pickup called name from WorldItems, and decrement the number of pickups. Tell AlienTilePanel that the game is over when the number of pickups drops to 0. This
     * method is called by PlayerSprite.
     */
    {
        if (this.wItems.removePickup(name)) { // try to remove it
            this.numPickups--;
            if (this.numPickups == 0) {
                this.atPanel.gameOver();
            }
        } else {
            System.out.println("Cannot delete unknown pickup: " + name);
        }
    } // end of removePickup()

    public String getPickupsStatus()
    // AlienTilesPanel uses this to update the screen stats
    {
        return "" + this.numPickups + " pickups left";
    }

    public boolean hasPickupsLeft()
    // called by AlienQuadSprite
    {
        return this.numPickups != 0;
    }

    public Point nearestPickup(Point pt)
    /*
     * Return the pickup nearest to tile pt. Called by AlienQuadSprite
     */
    {
        return this.wItems.nearestPickup(pt);
    }

    // ----------------- sprites related ----------------------

    public void addSprites(PlayerSprite ps, AlienSprite as[])
    // add the player and aliens
    {
        this.player = ps;
        this.aliens = as;
    }

    public Point getPlayerLoc()
    /*
     * Used by the alien sprites to find the player's location. This means that WorldDisplay controls access to player info.
     */
    {
        return this.player.getTileLoc();
    }

    public void hitByAlien()
    /*
     * An alien tells WorldDisplay that the player has been hit. Pass this on to the player.
     */
    {
        this.player.hitByAlien();
    }

    public void playerHasMoved(Point newPt, int moveQuad)
    /*
     * The player calls this method to tell WorldDisplay that it has moved; tell the aliens and update the world display's offsets.
     */
    {
        for (AlienSprite element : this.aliens) {
            element.playerHasMoved(newPt); // tell the aliens
        }

        updateOffsets(moveQuad); // update world's offset
    } // end of playerHasMoved()

    private void updateOffsets(int moveQuad)
    /*
     * xOffset and yOffset are pixel offsets for drawing the top-left corner of the floor image (and all of its contents) relative to the top-left corner (0,0) of the JPanel.
     * 
     * The offsets change in the _opposite_ direction to the player's apparent movement. This means that whe a player moves, it is actually the floor, and all of its contents,
     * which move.
     * 
     * moveQuad is the quadrant direction just 'moved' by the player.
     */
    {
        if (moveQuad == TiledSprite.SW) { // offset to NE
            this.xOffset += this.tileWidth / 2;
            this.yOffset -= this.tileHeight / 2;
        } else if (moveQuad == TiledSprite.NW) { // offset to SE
            this.xOffset += this.tileWidth / 2;
            this.yOffset += this.tileHeight / 2;
        } else if (moveQuad == TiledSprite.NE) { // offset to SW
            this.xOffset -= this.tileWidth / 2;
            this.yOffset += this.tileHeight / 2;
        } else if (moveQuad == TiledSprite.SE) { // offset to NW
            this.xOffset -= this.tileWidth / 2;
            this.yOffset -= this.tileHeight / 2;
        } else if (moveQuad == TiledSprite.STILL) { // do nothing to offsets
        } else {
            System.out.println("moveQuad error detected");
        }
    } // end of updateOffsets()

} // end of WorldDisplay class