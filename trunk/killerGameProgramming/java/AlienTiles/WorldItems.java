package AlienTiles;

// WorldItems.java 
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* WorldItems maintains a list of TileOccupier objects ordered by tile row.
 When the objects are drawn, the correct z-ordering is
 enforced, so objects in the 'foreground' are drawn in
 front of those further back.

 A TileOccupier can be a block, pickup, or sprite.

 A pickups is deleted from WorldItems when it is picked up by 
 the player.

 Sprites are added temporarily while drawing is carried out, then
 removed afterwards. This is because sprites move around, changing
 their tile position, which will affect where they are placed in
 the list.
 */

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class WorldItems {
    // max pixel width/height of a tile (including space to next tile)
    private int tileWidth, tileHeight;

    // 'start of first even row' coordinate
    /*
     * The coordinate is the top-left corner of the rectangle containing the diamond shape.
     */
    private int evenRowX, evenRowY;

    // 'start of first odd row' coordinate
    private int oddRowX, oddRowY;

    private ArrayList items;

    // a row-ordered list of TileOccupier objects

    public WorldItems(int w, int h, int erX, int erY, int orX, int orY) {
        this.tileWidth = w;
        this.tileHeight = h;
        this.evenRowX = erX;
        this.evenRowY = erY;
        this.oddRowX = orX;
        this.oddRowY = orY;
        this.items = new ArrayList();
    } // end of WorldItems()

    public void addItem(String name, int type, int x, int y, BufferedImage im)
    /*
     * When a TileOccupier object is created, it must be passed the relevant row coord information depending on if it is in an even or odd numbered row. The pixel dimensions of a
     * tile are also needed, so the item can be drawn at the correct place in the JPanel.
     * 
     * (x,y) is the tile coordinate for the item; type is one of BLOCK or PICKUP. The SPRITE entities are handled by positionSprites() below.
     */
    {
        TileOccupier toc;
        if (y % 2 == 0) {
            toc = new TileOccupier(name, type, x, y, im, this.evenRowX, this.evenRowY, this.tileWidth, this.tileHeight);
        } else {
            toc = new TileOccupier(name, type, x, y, im, this.oddRowX, this.oddRowY, this.tileWidth, this.tileHeight);
        }
        rowInsert(toc, x, y);
    } // end of addItem()

    private void rowInsert(TileOccupier toc, int x, int y)
    /*
     * Insert the toc item into the list in row order. Within a row, the items are orderd by their column position.
     */
    {
        TileOccupier item;
        Point itemPt;
        int i = 0;
        while (i < this.items.size()) {
            item = (TileOccupier) this.items.get(i);
            itemPt = item.getTileLoc();
            if (y < itemPt.y) {
                break;
            } else if ((y == itemPt.y) && (x < itemPt.x)) {
                break;
            }
            i++;
        }
        this.items.add(i, toc);
    } // end of rowInsert()

    public void draw(Graphics g, int xOffset, int yOffset)
    /*
     * Draw each item. Since the items are in row order, they will be drawn to the screen in the correct back-to-front z-ordering.
     */
    {
        TileOccupier item;
        for (int i = 0; i < this.items.size(); i++) {
            item = (TileOccupier) this.items.get(i);
            item.draw(g, xOffset, yOffset); // draw the item
        }
    } // end of draw()

    // ------------------------ sprite related -----------------

    public void positionSprites(PlayerSprite ps, AlienSprite[] aliens)
    // Add player and aliens to world items
    {
        posnSprite("bob", ps); // make up a 'unique' name
        for (int i = 0; i < aliens.length; i++) {
            posnSprite("alien " + i, aliens[i]);
        }
    } // end of positionSprites()

    private void posnSprite(String name, TiledSprite tSprite)
    /*
     * Add the TiledSprite object called name to the WorldItems list. This methiod is a variant of addItem().
     */
    {
        Point sPt = tSprite.getTileLoc();

        TileOccupier toc;
        if (sPt.y % 2 == 0) {
            toc = new TileOccupier(name, WorldDisplay.SPRITE, sPt.x, sPt.y, tSprite.getImage(), this.evenRowX, this.evenRowY, this.tileWidth, this.tileHeight);
        } else {
            toc = new TileOccupier(name, WorldDisplay.SPRITE, sPt.x, sPt.y, tSprite.getImage(), this.oddRowX, this.oddRowY, this.tileWidth, this.tileHeight);
        }

        toc.addSpriteRef(tSprite);
        /*
         * the sprite reference is used when drawing the item to ensure that the current sprite image is shown.
         */
        rowInsert(toc, sPt.x, sPt.y);
    } // end of posnSprite()

    public void removeSprites()
    // remove all the sprites in the WorldItems list, based on type
    {
        TileOccupier item;
        int i = 0;
        while (i < this.items.size()) {
            item = (TileOccupier) this.items.get(i);
            if (item.getType() == WorldDisplay.SPRITE) {
                this.items.remove(i);
            } else {
                i++;
            }
        }
    } // end of removeSprites()

    // -------------------------- pickup related ------------------

    public String findPickupName(Point pt)
    /*
     * If the p point is the same as a pickup point then return the pickup's name, otherwise null.
     */
    {
        TileOccupier item;
        for (int i = 0; i < this.items.size(); i++) {
            item = (TileOccupier) this.items.get(i);
            if ((item.getType() == WorldDisplay.PICKUP) && (pt.equals(item.getTileLoc()))) {
                return item.getName();
            }
        }
        return null;
    } // end of findPickupName()

    public Point nearestPickup(Point pt)
    /*
     * Return the pickup point nearest to pt. Compare pt with the _square_ of its distance to each pickup. The squared distance is use to avoid problems with negative distances.
     */
    {
        double minDist = 1000000; // dummy large value (a hack)
        Point minPoint = null;
        double dist;
        TileOccupier item;
        for (int i = 0; i < this.items.size(); i++) {
            item = (TileOccupier) this.items.get(i);
            if (item.getType() == WorldDisplay.PICKUP) {
                dist = pt.distanceSq(item.getTileLoc()); // get sq dist. to
                // pickup
                if (dist < minDist) {
                    minDist = dist; // store the smallest dist
                    minPoint = item.getTileLoc(); // store the associated pt
                }
            }
        }
        return minPoint;
    } // end of nearestPickup()

    public boolean removePickup(String name)
    // Attempt to delete the named pickup
    {
        TileOccupier item;
        for (int i = 0; i < this.items.size(); i++) {
            item = (TileOccupier) this.items.get(i);
            if ((item.getType() == WorldDisplay.PICKUP) && (name.equals(item.getName()))) { // find the named pickup
                this.items.remove(i);
                return true;
            }
        }
        return false;
    } // end of removePickup()

} // end of WorldItems class

