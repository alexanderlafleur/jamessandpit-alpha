package AlienTiles;

// TileNode.java 
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* A TileNode object stores the individual tile details used by 
 the A* algorithm.
 A* pathfinding is utilized by AlienAStarSprite objects.

 The total score for a node is:
 costFromStart + costToGoal

 costFromStart contains the cost of the path that led to this
 node from the starting tile. In the A* literature, this is
 sometimes called the g(x) function. We use the length of the path,
 with a step between adjacent tiles assigned a value of 1

 costToGoal is an estimate (heuristic) of the cost of going
 from the current node to the goal. This is sometimes called
 the h(x) function.
 
 costToGoal() calculates the _floor_ of the straight line 
 distance from the current tile to the goal. This is not always 
 less than or equal to the actual cheapest path cost, so the
 path found by A* may not be optimal. However, the calculation is 
 simple and fast, and the path is adequate enough for AlienTiles.

 Each TileNode has a reference to its parent, the node that
 was visited before it. The sequence of nodes back to the starting tile
 defines the path to this node (when reversed). 
 */

import java.awt.Point;
import java.util.ArrayList;

public class TileNode {
    private Point coord;

    private double costFromStart;

    private double costToGoal;

    private TileNode parent;

    // can be used to build a path from the starting tile to here

    public TileNode(Point p) {
        this.coord = p;
        this.parent = null;
        this.costFromStart = 0.0;
    }

    public void setCostFromStart(double v) {
        this.costFromStart = v;
    }

    public double getCostFromStart() {
        return this.costFromStart;
    }

    public void costToGoal(Point goal)
    // calculate _floor_ of the straight line dist. to the goal
    {
        double dist = this.coord.distance(goal.x, goal.y);
        this.costToGoal = Math.floor(dist);
        // System.out.println(coord + " to " + goal + ": " + costToGoal);
    }

    public double getScore() {
        return this.costFromStart + this.costToGoal;
    }

    public Point getPoint() {
        return this.coord;
    }

    public void setParent(TileNode p) {
        this.parent = p;
    }

    public TileNode getParent() {
        return this.parent;
    }

    public TileNode makeNeighbour(int quad, WorldDisplay wd)
    /*
     * Return the neighbouring tile node in the quad direction, except when that location is invalid according to WorldDisplay.
     */
    {
        TileNode newNode;
        int x = this.coord.x; // so less typing in the next few lines :)
        int y = this.coord.y;
        if (quad == TiledSprite.NE) {
            newNode = ((y % 2 == 0) ? makeNode(x, y - 1, wd) : makeNode(x + 1, y - 1, wd));
        } else if (quad == TiledSprite.SE) {
            newNode = ((y % 2 == 0) ? makeNode(x, y + 1, wd) : makeNode(x + 1, y + 1, wd));
        } else if (quad == TiledSprite.SW) {
            newNode = ((y % 2 == 0) ? makeNode(x - 1, y + 1, wd) : makeNode(x, y + 1, wd));
        } else if (quad == TiledSprite.NW) {
            newNode = ((y % 2 == 0) ? makeNode(x - 1, y - 1, wd) : makeNode(x, y - 1, wd));
        } else {
            System.out.println("makeNeighbour() error");
            newNode = null;
        }
        return newNode;
    } // end of makeNeighbour()

    private TileNode makeNode(int x, int y, WorldDisplay wd)
    /*
     * Create a neigbouring tile node. costFromStart is one more than the current node's value, since the new node is one node further along the path.
     */
    {
        if (!wd.validTileLoc(x, y)) {
            return null;
        }
        TileNode newNode = new TileNode(new Point(x, y));
        newNode.setCostFromStart(getCostFromStart() + 1.0);
        newNode.setParent(this);
        return newNode;
    } // end of makeNode()

    public ArrayList buildPath()
    /*
     * Build a path (a list of Points) from the next tile after the start up to the this tile.
     */
    {
        ArrayList path = new ArrayList();
        path.add(this.coord);
        TileNode temp = this.parent;
        while (temp != null) {
            path.add(0, temp.getPoint()); // add at start to reverse the path
            temp = temp.getParent();
        }
        path.remove(0); // remove the starting tile, since the alien is already
        // there
        return path;
    }

} // end of TilesNode class
