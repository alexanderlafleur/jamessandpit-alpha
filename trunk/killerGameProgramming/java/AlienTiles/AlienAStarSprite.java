package AlienTiles;

// AlienAStarSprite.java 
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* A subclass of AlienSprite.
 It overrides:
 void playerHasMoved()   -- called when the player has moved
 void move()             -- move the alien

 The alien calculates a path to the player using the A* path
 finding algorithm. The path is saved as a series of points to
 move to, one of which is carried out at each call to update().

 The path is recalculated in playerHasMoved() only when the
 player has moved MAX_MOVES. This saves on computation time,
 and makes things a bit easier for the player.

 The path is also recalculated when all the moves in the current
 path have been carried out.

 This gives the alien a 'player chasing' behaviour.
 */

import java.awt.Point;
import java.util.ArrayList;

public class AlienAStarSprite extends AlienSprite {
    // max number of player moves before path recalculation is allowed
    private final static int MAX_MOVES = 5;

    private int numPlayerMoves = 0;

    // list of Points (i.e. the path) to the player
    private ArrayList path;

    private int pathIndex = 0;

    public AlienAStarSprite(int x, int y, int w, int h, ImagesLoader imsLd, WorldDisplay wd) {
        super(x, y, w, h, imsLd, wd);
        this.path = new ArrayList();
    } // end of AlienQuadSprite()

    @Override
    public void playerHasMoved(Point playerLoc)
    /*
     * Slow down the frequency of path generation by only calculating it after MAX_MOVES by the player.
     */
    {
        if (this.numPlayerMoves == 0) {
            calcNewPath(playerLoc);
        } else {
            this.numPlayerMoves = (this.numPlayerMoves + 1) % MAX_MOVES;
        }
    } // end of playerHasMoved()

    private void calcNewPath(Point playerLoc) {
        this.path = aStarSearch(getTileLoc(), playerLoc);
        this.pathIndex = 0; // reset the index for the new path
        // printPath(); // for debugging
    } // end of calcNewPath()

    @Override
    protected void move()
    /*
     * Move to the next point in the calculated path. If the current path is used up, calculate a new one.
     */
    {
        if (this.pathIndex == this.path.size()) {
            calcNewPath(this.world.getPlayerLoc());
        }
        Point nextPt = (Point) this.path.get(this.pathIndex);
        this.pathIndex++;
        int quad = whichQuadrant(nextPt);
        setMove(nextPt, quad);
    } // end of move()

    private ArrayList aStarSearch(Point startLoc, Point goalLoc)
    /*
     * Derived from pseudo-code in: "The Basics of A* for Path Planning", Bryan Stout In 'Game Programming Gems', Mike DeLoura (ed.) Charles River Media, 2000, part 3.3, pp.
     * 254-263
     */
    {
        double newCost;
        TileNode bestNode, newNode;

        TileNode startNode = new TileNode(startLoc); // set start node
        startNode.costToGoal(goalLoc);

        // create the open queue and closed list
        TilesPriQueue open = new TilesPriQueue(startNode);
        TilesList closed = new TilesList();

        while (open.size() != 0) { // while some node still left to investigate
            bestNode = open.removeFirst();
            if (goalLoc.equals(bestNode.getPoint())) {
                return bestNode.buildPath(); // return a path to that goal
            } else {
                for (int i = 0; i < NUM_DIRS; i++) { // try every direction
                    if ((newNode = bestNode.makeNeighbour(i, this.world)) != null) {
                        newCost = newNode.getCostFromStart();
                        TileNode oldVer;
                        // if this tile already has a cheaper open or closed
                        // node
                        // then ignore the new node
                        if (((oldVer = open.findNode(newNode.getPoint())) != null) && (oldVer.getCostFromStart() <= newCost)) {
                            continue;
                        } else if (((oldVer = closed.findNode(newNode.getPoint())) != null) && (oldVer.getCostFromStart() <= newCost)) {
                            continue;
                        } else { // store the new/improved node, removing the old
                            // one
                            newNode.costToGoal(goalLoc);
                            // delete the old details (if they exist)
                            closed.delete(newNode.getPoint()); // may do
                            // nothing
                            open.delete(newNode.getPoint()); // may do
                            // nothing
                            open.add(newNode);
                        }
                    }
                } // end of for block
            } // end of if-else
            closed.add(bestNode);
        }
        return null; // no path found
    } // end of aStarSearch()

} // end of AlienAStarSprite class
