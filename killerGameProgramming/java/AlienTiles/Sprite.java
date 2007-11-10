package AlienTiles;

// Sprite.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* A Sprite has a position, velocity (in terms of steps),
 an image, and can be deactivated.

 The sprite's image is managed with an ImagesLoader object,
 and an ImagesPlayer object for looping.

 The images stored until the image 'name' can be looped
 through by calling loopImage(), which uses an
 ImagesPlayer object.

 ------------------
 ADDED: getImage()

 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Sprite {
    // default step sizes (how far to move in each update)
    private static final int XSTEP = 5;

    private static final int YSTEP = 5;

    // default dimensions when there is no image
    private static final int SIZE = 12;

    // image-related
    private ImagesLoader imsLoader;

    private String imageName;

    private BufferedImage image;

    private int width, height; // image dimensions

    private ImagesPlayer player; // for playing a loop of images

    private boolean isLooping;

    private int pWidth, pHeight; // panel dimensions

    private boolean isActive = true;

    // a sprite is updated and drawn only when it is active

    // protected vars
    protected int locx, locy; // location of sprite

    protected int dx, dy; // amount to move for each update

    public Sprite(int x, int y, int w, int h, ImagesLoader imsLd, String name) {
        this.locx = x;
        this.locy = y;
        this.pWidth = w;
        this.pHeight = h;
        this.dx = XSTEP;
        this.dy = YSTEP;

        this.imsLoader = imsLd;
        setImage(name); // the sprite's default image is 'name'
    } // end of Sprite()

    public void setImage(String name)
    // assign the name image to the sprite
    {
        this.imageName = name;
        this.image = this.imsLoader.getImage(this.imageName);
        if (this.image == null) { // no image of that name was found
            System.out.println("No sprite image for " + this.imageName);
            this.width = SIZE;
            this.height = SIZE;
        } else {
            this.width = this.image.getWidth();
            this.height = this.image.getHeight();
        }
        // no image loop playing
        this.player = null;
        this.isLooping = false;
    } // end of setImage()

    public BufferedImage getImage()
    // NEW in AlienTiles
    {
        if (this.isLooping) {
            return this.player.getCurrentImage();
        } else {
            return this.image;
        }
    } // getImage()

    public void loopImage(int animPeriod, double seqDuration)
    /*
     * Switch on loop playing. The total time for the loop is seqDuration secs. The update interval (from the enclosing panel) is animPeriod ms.
     */
    {
        if (this.imsLoader.numImages(this.imageName) > 1) {
            this.player = null; // to encourage garbage collection of previous
            // player
            this.player = new ImagesPlayer(this.imageName, animPeriod, seqDuration, true, this.imsLoader);
            this.isLooping = true;
        } else {
            System.out.println(this.imageName + " is not a sequence of images");
        }
    } // end of loopImage()

    public void stopLooping() {
        if (this.isLooping) {
            this.player.stop();
            this.isLooping = false;
        }
    } // end of stopLooping()

    public int getWidth() // of the sprite's image
    {
        return this.width;
    }

    public int getHeight() // of the sprite's image
    {
        return this.height;
    }

    public int getPWidth() // of the enclosing panel
    {
        return this.pWidth;
    }

    public int getPHeight() // of the enclosing panel
    {
        return this.pHeight;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public void setActive(boolean a) {
        this.isActive = a;
    }

    public void setPosition(int x, int y) {
        this.locx = x;
        this.locy = y;
    }

    public void translate(int xDist, int yDist) {
        this.locx += xDist;
        this.locy += yDist;
    }

    public int getXPosn() {
        return this.locx;
    }

    public int getYPosn() {
        return this.locy;
    }

    public void setStep(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int getXStep() {
        return this.dx;
    }

    public int getYStep() {
        return this.dy;
    }

    public Rectangle getMyRectangle() {
        return new Rectangle(this.locx, this.locy, this.width, this.height);
    }

    public void updateSprite()
    // move the sprite
    {
        if (isActive()) {
            this.locx += this.dx;
            this.locy += this.dy;
            if (this.isLooping) {
                this.player.updateTick(); // update the player
            }
        }
    } // end of updateSprite()

    public void drawSprite(Graphics g) {
        if (isActive()) {
            if (this.image == null) { // the sprite has no image
                g.setColor(Color.yellow); // draw a yellow circle instead
                g.fillOval(this.locx, this.locy, SIZE, SIZE);
                g.setColor(Color.black);
            } else {
                if (this.isLooping) {
                    this.image = this.player.getCurrentImage();
                }
                g.drawImage(this.image, this.locx, this.locy, null);
            }
        }
    } // end of drawSprite()

} // end of Sprite class
