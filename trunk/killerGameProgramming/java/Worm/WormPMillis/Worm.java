package Worm.WormPMillis;

// Worm.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Contains the worm's internal data structure (a circular buffer)
 and code for deciding on the position and compass direction
 of the next worm move.
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;

public class Worm {
    // size and number of dots in a worm
    private static final int DOTSIZE = 12;

    private static final int RADIUS = DOTSIZE / 2;

    private static final int MAXPOINTS = 40;

    // compass direction/bearing constants
    private static final int NUM_DIRS = 8;

    private static final int N = 0; // north, etc going clockwise

    private static final int NE = 1;

    private static final int E = 2;

    private static final int SE = 3;

    private static final int S = 4;

    private static final int SW = 5;

    private static final int W = 6;

    private static final int NW = 7;

    private int currCompass; // stores the current compass dir/bearing

    // Stores the increments in each of the compass dirs.
    // An increment is added to the old head position to get the
    // new position.
    Point2D.Double incrs[];

    // probabiliy info for selecting a compass dir.
    private static final int NUM_PROBS = 9;

    private int probsForOffset[];

    // cells[] stores the dots making up the worm
    // it is treated like a circular buffer
    private Point cells[];

    private int nPoints;

    private int tailPosn, headPosn; // the tail and head of the buffer

    private int pWidth, pHeight; // panel dimensions

    private Obstacles obs;

    public Worm(int pW, int pH, Obstacles os) {
        this.pWidth = pW;
        this.pHeight = pH;
        this.obs = os;
        this.cells = new Point[MAXPOINTS]; // initialise buffer
        this.nPoints = 0;
        this.headPosn = -1;
        this.tailPosn = -1;

        // increments for each compass dir
        this.incrs = new Point2D.Double[NUM_DIRS];
        this.incrs[N] = new Point2D.Double(0.0, -1.0);
        this.incrs[NE] = new Point2D.Double(0.7, -0.7);
        this.incrs[E] = new Point2D.Double(1.0, 0.0);
        this.incrs[SE] = new Point2D.Double(0.7, 0.7);
        this.incrs[S] = new Point2D.Double(0.0, 1.0);
        this.incrs[SW] = new Point2D.Double(-0.7, 0.7);
        this.incrs[W] = new Point2D.Double(-1.0, 0.0);
        this.incrs[NW] = new Point2D.Double(-0.7, -0.7);

        // probability info for selecting a compass dir.
        // 0 = no change, -1 means 1 step anti-clockwise,
        // 1 means 1 step clockwise, etc.
        /*
         * The array means that usually the worm continues in the same direction but may bear slightly to the left or right.
         */
        this.probsForOffset = new int[NUM_PROBS];
        this.probsForOffset[0] = 0;
        this.probsForOffset[1] = 0;
        this.probsForOffset[2] = 0;
        this.probsForOffset[3] = 1;
        this.probsForOffset[4] = 1;
        this.probsForOffset[5] = 2;
        this.probsForOffset[6] = -1;
        this.probsForOffset[7] = -1;
        this.probsForOffset[8] = -2;

    } // end of Worm()

    public boolean nearHead(int x, int y)
    // is (x,y) near the worm's head?
    {
        if (this.nPoints > 0) {
            if ((Math.abs(this.cells[this.headPosn].x + RADIUS - x) <= DOTSIZE) && (Math.abs(this.cells[this.headPosn].y + RADIUS - y) <= DOTSIZE)) {
                return true;
            }
        }
        return false;
    } // end of nearHead()

    public boolean touchedAt(int x, int y)
    // is (x,y) near any part of the worm's body?
    {
        int i = this.tailPosn;
        while (i != this.headPosn) {
            if ((Math.abs(this.cells[i].x + RADIUS - x) <= RADIUS) && (Math.abs(this.cells[i].y + RADIUS - y) <= RADIUS)) {
                return true;
            }
            i = (i + 1) % MAXPOINTS;
        }
        return false;
    } // end of touchedAt()

    public void move()
    /*
     * A move causes the addition of a new dot to the front of the worm, which becomes its new head. A dot has a position and compass direction/bearing, which is derived from the
     * position and bearing of the old head.
     * 
     * move() is complicated by having to deal with 3 cases: when the worm is first created when the worm is growing when the worm is MAXPOINTS long (then the addition of a new
     * head must be balanced by the removal of a tail dot)
     */
    {
        int prevPosn = this.headPosn; // save old head posn while creating new one
        this.headPosn = (this.headPosn + 1) % MAXPOINTS;

        if (this.nPoints == 0) { // empty array at start
            this.tailPosn = this.headPosn;
            this.currCompass = (int) (Math.random() * NUM_DIRS); // random dir.
            this.cells[this.headPosn] = new Point(this.pWidth / 2, this.pHeight / 2); // center pt
            this.nPoints++;
        } else if (this.nPoints == MAXPOINTS) { // array is full
            this.tailPosn = (this.tailPosn + 1) % MAXPOINTS; // forget last tail
            newHead(prevPosn);
        } else { // still room in cells[]
            newHead(prevPosn);
            this.nPoints++;
        }
    } // end of move()

    private void newHead(int prevPosn)
    /*
     * Create new head position and compass direction/bearing.
     * 
     * This has two main parts. Initially we try to generate a head by varying the old position/bearing. But if the new head hits an obstacle, then we shift to a second phase.
     * 
     * In the second phase we try a head which is 90 degrees clockwise, 90 degress clockwise, or 180 degrees reversed so that the obstacle is avoided. These bearings are stored in
     * fixedOffs[].
     */
    {
        int fixedOffs[] = { -2, 2, -4 }; // offsets to avoid an obstacle

        int newBearing = varyBearing();
        Point newPt = nextPoint(prevPosn, newBearing);
        // Get a new position based on a semi-random
        // variation of the current position.

        if (this.obs.hits(newPt, DOTSIZE)) {
            for (int element : fixedOffs) {
                newBearing = calcBearing(element);
                newPt = nextPoint(prevPosn, newBearing);
                if (!this.obs.hits(newPt, DOTSIZE)) {
                    break; // one of the fixed offsets will work
                }
            }
        }
        this.cells[this.headPosn] = newPt; // new head position
        this.currCompass = newBearing; // new compass direction
    } // end of newHead()

    private int varyBearing()
    // vary the compass bearing semi-randomly
    {
        int newOffset = this.probsForOffset[(int) (Math.random() * NUM_PROBS)];
        return calcBearing(newOffset);
    } // end of varyBearing()

    private int calcBearing(int offset)
    // Use the offset to calculate a new compass bearing based
    // on the current compass direction.
    {
        int turn = this.currCompass + offset;
        // ensure that turn is between N to NW (0 to 7)
        if (turn >= NUM_DIRS) {
            turn = turn - NUM_DIRS;
        } else if (turn < 0) {
            turn = NUM_DIRS + turn;
        }
        return turn;
    } // end of calcBearing()

    private Point nextPoint(int prevPosn, int bearing)
    /*
     * Return the next coordinate based on the previous position and a compass bearing.
     * 
     * Convert the compass bearing into predetermined increments (stored in incrs[]). Add the increments multiplied by the DOTSIZE to the old head position. Deal with wraparound.
     */
    {
        // get the increments for the compass bearing
        Point2D.Double incr = this.incrs[bearing];

        int newX = this.cells[prevPosn].x + (int) (DOTSIZE * incr.x);
        int newY = this.cells[prevPosn].y + (int) (DOTSIZE * incr.y);

        // modify newX/newY if < 0, or > pWidth/pHeight; use wraparound
        if (newX + DOTSIZE < 0) {
            newX = newX + this.pWidth;
        } else if (newX > this.pWidth) {
            newX = newX - this.pWidth;
        }

        if (newY + DOTSIZE < 0) {
            newY = newY + this.pHeight;
        } else if (newY > this.pHeight) {
            newY = newY - this.pHeight;
        }

        return new Point(newX, newY);
    } // end of nextPoint()

    public void draw(Graphics g)
    // draw a black worm with a red head
    {
        if (this.nPoints > 0) {
            g.setColor(Color.black);
            int i = this.tailPosn;
            while (i != this.headPosn) {
                g.fillOval(this.cells[i].x, this.cells[i].y, DOTSIZE, DOTSIZE);
                i = (i + 1) % MAXPOINTS;
            }
            g.setColor(Color.red);
            g.fillOval(this.cells[this.headPosn].x, this.cells[this.headPosn].y, DOTSIZE, DOTSIZE);
        }
    } // end of draw()

} // end of Worm class

