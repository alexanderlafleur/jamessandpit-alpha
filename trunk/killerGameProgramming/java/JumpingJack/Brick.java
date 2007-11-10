package JumpingJack;

// Brick.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Information for a single brick.

 (mapX, mapY) are indicies in the brick map, which starts at (0,0)
 with the first row of bricks and increases to the left 
 and down.
 
 locY is the y-coordinate in the bricks ribbon, which starts
 at (0,0) at the top-left of the panel, and stretches down and 
 to the right.

 The brick is represented by a static image; animated or moveable
 bricks are not supported.
 */

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Brick {
    private int mapX, mapY; // indicies in the bricks map

    private int imageID;

    /*
     * the ID corresponds to the index position of the brick's image in the image strip
     */

    private BufferedImage image;

    private int height; // of the brick image

    private int locY;

    // the y- coordinate of the brick in the bricks ribbon

    public Brick(int id, int x, int y) {
        this.mapX = x;
        this.mapY = y;
        this.imageID = id;
    }

    public int getMapX() {
        return this.mapX;
    }

    public int getMapY() {
        return this.mapY;
    }

    public int getImageID() {
        return this.imageID;
    }

    public void setImage(BufferedImage im) {
        this.image = im;
        this.height = im.getHeight();
    } // end of setImage()

    public void setLocY(int pHeight, int maxYBricks) {
        this.locY = pHeight - ((maxYBricks - this.mapY) * this.height);
    }

    public int getLocY() {
        return this.locY;
    }

    public void display(Graphics g, int xScr)
    // called by BricksManager's drawBricks()
    {
        g.drawImage(this.image, xScr, this.locY, null);
    }

} // end of Brick class
