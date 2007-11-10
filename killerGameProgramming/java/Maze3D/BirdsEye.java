package Maze3D;

// BirdsEye.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* This class handles the overview display, which contains
 an unchanging image of the maze, a sprite that moves 
 and changes heading, and the occasional display of
 a warning when the player hits an obstacle.

 The sprite may move outside of the display region since
 the 3D area includes a wide floor that extends beyond the maze.
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class BirdsEye extends JPanel {
    /**
     * 
     */
    private static final long serialVersionUID = 6521039311587011177L;

    private static final int PWIDTH = 256; // size of panel

    private static final int PHEIGHT = 256;

    private static final int NUM_DIRS = 4;

    // these dirs have counter-clockwise ordering when viewed from above
    private static final int FORWARD = 0;

    private static final int LEFT = 1;

    private static final int BACK = 2;

    private static final int RIGHT = 3;

    private static final String BANG_MSG = "BANG!";

    // warning message when the player hits an obstacle

    private MazeManager mm;

    private Image mazeIm; // for the maze image

    private Image userIm; // for the user's arrow

    private Image arrowIms[]; // the range of possible arrows

    private int arrowWidth, arrowHeight;

    private Point moves[];

    private Point currPosn; // player current position in image

    private int step; // distance a player moves in the image

    private int compass; // the current compass heading

    private boolean showBang; // true if player tried to move through a wall

    private Font msgFont;

    public BirdsEye(MazeManager mazeMan) {
        this.mm = mazeMan;
        setBackground(Color.white);
        setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
        this.msgFont = new Font("SansSerif", Font.BOLD, 24);

        this.mazeIm = this.mm.getMazeImage(); // get the maze image
        initMoves();
        loadArrows();
        initPosition();
        repaint();
    } // end of BirdsEye()

    private void initMoves()
    /*
     * Moves in (x,y) in 4 directions. The sprite starts by pointing downwards (when viewed from above), and that is set to be forward. Moving downwards (forward) means increasing
     * the y-axis value.
     * 
     * LEFT, RIGHT, BACK are relative to the initial FORWARD direction.
     */
    {
        this.moves = new Point[NUM_DIRS];
        this.step = this.mm.getImageStep();
        this.moves[FORWARD] = new Point(0, this.step); // move downwards on-screen
        this.moves[LEFT] = new Point(this.step, 0); // right on-screen
        this.moves[BACK] = new Point(0, -this.step); // up on-screen
        this.moves[RIGHT] = new Point(-this.step, 0); // left on-screen
    } // end of initMoves()

    private void loadArrows()
    /*
     * The arrows represent the four directions in which the sprite can move.
     */
    {
        this.arrowIms = new Image[NUM_DIRS];

        ImageIcon imIcon = new ImageIcon("images/arrowFwd.gif");
        this.arrowIms[FORWARD] = imIcon.getImage();
        this.arrowWidth = imIcon.getIconWidth();
        this.arrowHeight = imIcon.getIconHeight();

        this.arrowIms[LEFT] = new ImageIcon("images/arrowLeft.gif").getImage();
        this.arrowIms[BACK] = new ImageIcon("images/arrowBack.gif").getImage();
        this.arrowIms[RIGHT] = new ImageIcon("images/arrowRight.gif").getImage();
    } // end of loadArrows()

    private void initPosition()
    /*
     * The initial position is the start position obtained from the maze manager, and the default heading, which is FORWARD (down the screen).
     */
    {
        this.currPosn = this.mm.getImageStartPosn();
        this.compass = FORWARD;
        this.userIm = this.arrowIms[FORWARD];
        // the sprite starts facing down the screen
        this.showBang = false;
    } // end of initPosition()

    public void setMove(int dir)
    /*
     * dir = 0-3 (FORWARD, LEFT, BACK, or RIGHT) The actual heading depends on combining the current compass value with dir.
     */
    {
        int actualHd = (this.compass + dir) % NUM_DIRS;
        Point move = this.moves[actualHd];
        this.currPosn.x += move.x;
        this.currPosn.y += move.y;
        repaint();
    } // end of setMove()

    public void setRotation(int dir)
    /*
     * Rotations affect the compass heading, which will then affect future movements. dir is LEFT or RIGHT.
     */
    {
        this.compass = (this.compass + dir) % NUM_DIRS;
        this.userIm = this.arrowIms[this.compass];
        repaint();
    } // end of setRotation()

    public void bangAlert()
    // Request a redraw so that the bang message will be displayed
    {
        this.showBang = true;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g)
    /*
     * Paint the maze image, then the player, then the bang message if it is set.
     */
    {
        super.paintComponent(g); // repaint standard stuff first
        g.drawImage(this.mazeIm, 0, 0, null); // draw the maze

        int xPos = this.currPosn.x + this.step / 2 - this.arrowWidth / 2;
        int yPos = this.currPosn.y + this.step / 2 - this.arrowHeight / 2;
        g.drawImage(this.userIm, xPos, yPos, null); // draw the player

        if (this.showBang) { // show the bang message
            g.setColor(Color.red);
            g.setFont(this.msgFont);
            g.drawString(BANG_MSG, PWIDTH / 2, PHEIGHT / 2);
            this.showBang = false;
        }
    } // end of paintComponent()

} // end of BirdsEye class
