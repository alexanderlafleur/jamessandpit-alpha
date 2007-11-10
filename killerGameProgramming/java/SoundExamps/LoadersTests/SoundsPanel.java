package SoundExamps.LoadersTests;

// SoundsPanel.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* SoundsPanel displays 4 images (a dog, cat, sheep, and chicken),
 and monitors mouse presses on its canvas. If a click is
 over an image, then playClip() is called in LoadersTests
 to play the corresponding clip.

 SoundsPanel uses ImagesLoader to load its 4 images, which
 is a bit of over-kill for such simple needs.
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class SoundsPanel extends JPanel {
    /**
     * 
     */
    private static final long serialVersionUID = -3469460471725512481L;

    // clip image names
    private final static String[] names = { "dog", "cat", "sheep", "chicken" };

    // on-screen top-left coords for the images
    private final static int[] xCoords = { 20, 210, 20, 210 };

    private final static int[] yCoords = { 25, 25, 170, 170 };

    // location of image and sound info
    private final static String IMS_FILE = "imagesInfo.txt";

    private static final int PWIDTH = 350; // size of this panel

    private static final int PHEIGHT = 350;

    private int numImages;

    private BufferedImage[] images;

    private Rectangle[] hotSpots; // a click inside these rectangles triggers

    // the playing of a clip

    private LoadersTests topLevel;

    public SoundsPanel(LoadersTests sts) {
        this.topLevel = sts;

        setPreferredSize(new Dimension(PWIDTH, PHEIGHT));

        initImages();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                selectImage(e.getX(), e.getY());
            }
        });
    } // end of SoundsPanel()

    private void initImages()
    // load and initialise the images, and build their 'hot-spots'
    {
        this.numImages = names.length;

        this.hotSpots = new Rectangle[this.numImages];
        this.images = new BufferedImage[this.numImages];

        ImagesLoader imsLoader = new ImagesLoader(IMS_FILE);

        for (int i = 0; i < this.numImages; i++) {
            this.images[i] = imsLoader.getImage(names[i]);
            this.hotSpots[i] = new Rectangle(xCoords[i], yCoords[i], this.images[i].getWidth(), this.images[i].getHeight());
            // use the images' dimensions to determine the size of the
            // rectangles
        }
    } // end of initImages()

    @Override
    public void paintComponent(Graphics g)
    // show the images against a white background
    {
        super.paintComponent(g);
        g.setColor(Color.white);
        g.fillRect(0, 0, PWIDTH, PHEIGHT); // white background

        // display the images
        for (int i = 0; i < this.numImages; i++) {
            g.drawImage(this.images[i], xCoords[i], yCoords[i], this);
        }
    } // end of paintComponent()

    private void selectImage(int x, int y)
    /*
     * Work out which image was clicked on (perhaps none), and request that its corresponding clip be played.
     */
    {
        for (int i = 0; i < this.numImages; i++) {
            if (this.hotSpots[i].contains(x, y)) {
                this.topLevel.playClip(names[i], i); // play the image's clip
                break;
            }
        }
    } // end of selectImage()

} // end of SoundsPanel class
