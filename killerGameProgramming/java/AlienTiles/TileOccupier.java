package AlienTiles;

// TileOccupier.java 
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* A tile occupier can be a block, pickup, or a sprite,
 which is indicated by its type.

 A tile occupier has a unique name, a tile coordinate
 (xTile, yTile), and a coordinate relative to
 the top-left corner of the floor image (xDraw,yDraw)
 where it should be drawn.

 Drawing involves adding an offset to the occupier's floor
 coordinate since the floor will have moved as the player
 apparently moves. 

 A sprite draws itself, and must be positioned in
 the JPanel first.
 */
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class TileOccupier {
    private BufferedImage image;
    private String name;
    private TiledSprite sprite = null;
    private int type; // BLOCK, PICKUP, or SPRITE
    private int xDraw, yDraw;
    // coordinate relative to the floor image where the tile
    // occupier should be drawn
    private int xTile, yTile; // tile coordinate

    // used when the TileOccupier is a sprite
    public TileOccupier(String nm, int ty, int x, int y, BufferedImage im, int xRowStart, int yRowStart, int xTileWidth, int yTileHeight) {
        name = nm;
        type = ty;
        xTile = x;
        yTile = y;
        image = im;
        calcPosition(xRowStart, yRowStart, xTileWidth, yTileHeight);
    } // end of TileOccupier()

    public void addSpriteRef(TiledSprite s) {
        if (type == WorldDisplay.SPRITE) {
            sprite = s;
        }
    }

    private void calcPosition(int xRowStart, int yRowStart, int xTileWidth, int yTileHeight)
    /*
     * Calculate the coordinate (xDraw,yDraw) relative to the floor image. The coordinate is used to draw the TileOccupier's image so that its 'feet'
     * appear to be resting on the tile, centered in the x-direction, and a little forward of the middle in the y-direction.
     */
    { // top-left corner of image relative to its tile
        int xImOffset = xTileWidth / 2 - image.getWidth() / 2; // in the middle
        int yImOffset = yTileHeight - image.getHeight() - yTileHeight / 5;
        // up a little from bottom point of the diamond
        // top-left corner of image relative to floor image
        xDraw = xRowStart + xTile * xTileWidth + xImOffset;
        if (yTile % 2 == 0) {
            yDraw = yRowStart + yTile / 2 * yTileHeight + yImOffset;
        } else {
            // on an odd row
            yDraw = yRowStart + (yTile - 1) / 2 * yTileHeight + yImOffset;
        }
    } // end of calcPosition()

    public void draw(Graphics g, int xOffset, int yOffset)
    /*
     * Draw the TileOccupier offset from the top-left of the JPanel by (xOffset,yOffset). For a sprite, the drawing is left to the sprite itself, so
     * that its current image will be used. First, the sprite must be placed correctly in the JPanel.
     */
    {
        if (type == WorldDisplay.SPRITE) {
            sprite.setPosition(xDraw + xOffset, yDraw + yOffset); // position
            // in JPanel
            sprite.drawSprite(g); // let the sprite do the drawing
        } else {
            // the entity is a PICKUP or BLOCK
            g.drawImage(image, xDraw + xOffset, yDraw + yOffset, null);
        }
    } // end of draw()

    public String getName() {
        return name;
    }

    public Point getTileLoc() {
        return new Point(xTile, yTile);
    }

    public int getType() {
        return type;
    }
} // end of TileOccupier class
