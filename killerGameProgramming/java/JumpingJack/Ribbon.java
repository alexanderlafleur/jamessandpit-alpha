package JumpingJack;

// Ribbon.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* A ribbon manages an image which is wider than the game panel's
 width: width >= pWidth

 When a sprite is instructed to move left or right, the 
 sprite doesn't actually move, instead the ribbon moves in
 the _opposite_direction (right or left). The amount of movement 
 is specified in moveSize.

 The image is wrapped around the panel, so at a given moment
 the tail of the image, followed by its head may be visible 
 in the panel.

 A collection of ribbons are managed by a RibbonsManager object.
 */

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Ribbon {
    private BufferedImage im;

    private int width; // the width of the image (>= pWidth)

    private int pWidth, pHeight; // dimensions of display panel

    private int moveSize; // size of the image move (in pixels)

    private boolean isMovingRight; // movement flags

    private boolean isMovingLeft;

    private int xImHead;

    /*
     * The x-coord in the panel where the start of the image (its head) should be drawn. It can range between -width to width (exclusive), so can have a value beyond the confines
     * of the panel (0-pWidth).
     * 
     * As xImHead varies, the on-screen ribbon will usually be a combination of its tail followed by its head.
     */

    public Ribbon(int w, int h, BufferedImage im, int moveSz) {
        this.pWidth = w;
        this.pHeight = h;

        this.im = im;
        this.width = im.getWidth(); // no need to store the height
        if (this.width < this.pWidth) {
            System.out.println("Ribbon width < panel width");
        }

        this.moveSize = moveSz;
        this.isMovingRight = false; // no movement at start
        this.isMovingLeft = false;
        this.xImHead = 0;
    } // end of Ribbon()

    public void moveRight()
    // move the ribbon image to the right on the next update
    {
        this.isMovingRight = true;
        this.isMovingLeft = false;
    }

    public void moveLeft()
    // move the ribbon image to the left on the next update
    {
        this.isMovingRight = false;
        this.isMovingLeft = true;
    }

    public void stayStill()
    // don't move the ribbon image on the next update
    {
        this.isMovingRight = false;
        this.isMovingLeft = false;
    }

    public void update()
    /*
     * Increment the xImHead value depending on the movement flags. It can range between -width to width (exclusive), which is the width of the image.
     */
    {
        if (this.isMovingRight) {
            this.xImHead = (this.xImHead + this.moveSize) % this.width;
        } else if (this.isMovingLeft) {
            this.xImHead = (this.xImHead - this.moveSize) % this.width;
        }

        // System.out.println("xImHead is " + xImHead);
    } // end of update()

    public void display(Graphics g)
    /*
     * Consider 5 cases: when xImHead == 0, draw only the im head when xImHead > 0, draw the im tail and im head, or only the im tail. when xImHead < 0, draw the im tail, or the im
     * tail and im head
     * 
     * xImHead can range between -width to width (exclusive)
     */
    {
        if (this.xImHead == 0) {
            draw(g, this.im, 0, this.pWidth, 0, this.pWidth);
        } else if ((this.xImHead > 0) && (this.xImHead < this.pWidth)) {
            // draw im tail at (0,0) and im head at (xImHead,0)
            draw(g, this.im, 0, this.xImHead, this.width - this.xImHead, this.width); // im tail
            draw(g, this.im, this.xImHead, this.pWidth, 0, this.pWidth - this.xImHead); // im head
        } else if (this.xImHead >= this.pWidth) {
            draw(g, this.im, 0, this.pWidth, this.width - this.xImHead, this.width - this.xImHead + this.pWidth); // im
        } else if ((this.xImHead < 0) && (this.xImHead >= this.pWidth - this.width)) {
            draw(g, this.im, 0, this.pWidth, -this.xImHead, this.pWidth - this.xImHead); // im body
        } else if (this.xImHead < this.pWidth - this.width) {
            // draw im tail at (0,0) and im head at (width+xImHead,0)
            draw(g, this.im, 0, this.width + this.xImHead, -this.xImHead, this.width); // im tail
            draw(g, this.im, this.width + this.xImHead, this.pWidth, 0, this.pWidth - this.width - this.xImHead); // im
            // head
        }
    } // end of display()

    private void draw(Graphics g, BufferedImage im, int scrX1, int scrX2, int imX1, int imX2)
    /*
     * The y-coords of the image always starts at 0 and ends at pHeight (the height of the panel), so are hardwired.
     */
    {
        g.drawImage(im, scrX1, 0, scrX2, this.pHeight, imX1, 0, imX2, this.pHeight, null);
    }

} // end of Ribbon
