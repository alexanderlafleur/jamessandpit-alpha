package Worm.WormFSEM;

// Obstacles.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* A collection of boxes which the worm cannot move over
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Obstacles {
    private static final int BOX_LENGTH = 12;
    private ArrayList boxes; // arraylist of Rectangle objects
    private WormChase wormChase;

    public Obstacles(WormChase wc) {
        boxes = new ArrayList();
        wormChase = wc;
    }

    synchronized public void add(int x, int y) {
        boxes.add(new Rectangle(x, y, BOX_LENGTH, BOX_LENGTH));
        wormChase.setBoxNumber(boxes.size()); // report new number of boxes
    }

    synchronized public void draw(Graphics g)
    // draw a series of blue boxes
    {
        Rectangle box;
        g.setColor(Color.blue);
        for (int i = 0; i < boxes.size(); i++) {
            box = (Rectangle) boxes.get(i);
            g.fillRect(box.x, box.y, box.width, box.height);
        }
    } // end of draw()

    synchronized public int getNumObstacles() {
        return boxes.size();
    }

    synchronized public boolean hits(Point p, int size)
    // does p intersect with any of the obstacles?
    {
        Rectangle r = new Rectangle(p.x, p.y, size, size);
        Rectangle box;
        for (int i = 0; i < boxes.size(); i++) {
            box = (Rectangle) boxes.get(i);
            if (box.intersects(r)) {
                return true;
            }
        }
        return false;
    } // end of intersects()
} // end of Obstacles class
