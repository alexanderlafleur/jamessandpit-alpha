package AlienTiles;

// TiledSprite.java 
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* A TiledSprite object moves from one tile to another.
 Movement is limited to four compass directions (quadrants):
 NE, SE, SW, NW, and STILL for no movement.

 TiledSprite extends Sprite but does not use its (locx,locy)
 pixel coordinates. Instead it maintains its own current
 tile position (xTile, yTile).

 This approach means that a TiledSprite cannot move around 
 inside a tile, only move between tiles.

 The sprite's pixel position (i.e. its position in the JPanel)
 is updated by WorldDisplay just prior to the game being drawn
 in the JPanel. 
 */
import java.awt.Point;

public class TiledSprite extends Sprite {
    // sprite quadrant directions
    public final static int NE = 0;
    public final static int NUM_DIRS = 4;
    public final static int NW = 3;
    public final static int SE = 1;
    public final static int STILL = 4;
    public final static int SW = 2;
    protected WorldDisplay world;
    // protected vars
    protected int xTile, yTile; // tile coordinate for the sprite

    public TiledSprite(int x, int y, int w, int h, ImagesLoader imsLd, String name, WorldDisplay wd) {
        super(0, 0, w, h, imsLd, name);
        setStep(0, 0); // no movement
        world = wd;
        if (!world.validTileLoc(x, y)) { // ask world if tile at (x,y) is
            // valid
            System.out.println("Alien tile location (" + x + "," + y + ") not valid; using (0,0)");
            x = 0;
            y = 0;
        }
        xTile = x;
        yTile = y;
    } // end of TiledSprite()

    public int getRandDirection()
    // return a random quadrant direction
    {
        return (int) (NUM_DIRS * Math.random());
    }

    public Point getTileLoc() {
        return new Point(xTile, yTile);
    }

    // ------------------ movement methods ---------------------
    public void setTileLoc(Point pt) {
        xTile = pt.x;
        yTile = pt.y;
    }

    public Point tryMove(int quad)
    /*
     * The quadrant is translated into a tile coordinate, and tested for validity. The translation varies depending on if the row position of the
     * curent tile (yTile) is even or odd. null is returned if the new position is invalid.
     */
    {
        Point nextPt;
        if (quad == NE) {
            nextPt = yTile % 2 == 0 ? new Point(xTile, yTile - 1) : new Point(xTile + 1, yTile - 1);
        } else if (quad == SE) {
            nextPt = yTile % 2 == 0 ? new Point(xTile, yTile + 1) : new Point(xTile + 1, yTile + 1);
        } else if (quad == SW) {
            nextPt = yTile % 2 == 0 ? new Point(xTile - 1, yTile + 1) : new Point(xTile, yTile + 1);
        } else if (quad == NW) {
            nextPt = yTile % 2 == 0 ? new Point(xTile - 1, yTile - 1) : new Point(xTile, yTile - 1);
        } else {
            return null;
        }
        if (world.validTileLoc(nextPt.x, nextPt.y)) {
            // ask WorldDisplay if the proposed tile is in a valid location
            return nextPt;
        } else {
            return null;
        }
    } // end of tryMove()

    public int whichQuadrant(Point p)
    /*
     * Relative to this sprite's tile position, which quadrant direction is needed to get to p? p is assumed to be adjacent to this tile.
     * whichQuadrant() return -1 if there is no suitable direction.
     */
    { // System.out.println("tile: " + xTile + ", " + yTile);
        // System.out.println("to: " + p.x + ", " + p.y);
        if (xTile == p.x && yTile == p.y) {
            return STILL; // the sprite is already on tile p
        }
        if (yTile % 2 == 0) { // the current tile is on an even row
            if (xTile == p.x && yTile - 1 == p.y) {
                return NE;
            }
            if (xTile == p.x && yTile + 1 == p.y) {
                return SE;
            }
            if (xTile - 1 == p.x && yTile + 1 == p.y) {
                return SW;
            }
            if (xTile - 1 == p.x && yTile - 1 == p.y) {
                return NW;
            }
        } else { // the current tile is on an odd row
            if (xTile + 1 == p.x && yTile - 1 == p.y) {
                return NE;
            }
            if (xTile + 1 == p.x && yTile + 1 == p.y) {
                return SE;
            }
            if (xTile == p.x && yTile + 1 == p.y) {
                return SW;
            }
            if (xTile == p.x && yTile - 1 == p.y) {
                return NW;
            }
        }
        return -1;
    } // end of whichQuadrant()
} // end of TiledSprite class
