package AlienTiles;

// TilesList.java 
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* A TilesList object stores the 'closed' list of tiles that 
 have already been visited by the A* algorithm.
 The TileNodes in TilesList are unordered.

 A* pathfinding is utilized by AlienAStarSprite objects.
 */
import java.awt.Point;
import java.util.ArrayList;

public class TilesList {
    protected ArrayList nodes; // list of TileNode objects

    public TilesList() {
        nodes = new ArrayList();
    }

    public TilesList(TileNode node) {
        nodes = new ArrayList();
        nodes.add(node);
    }

    public void add(TileNode node) {
        nodes.add(node);
    }

    public boolean delete(Point p)
    /*
     * Try to delete the tile at point p from the list. If p is not present then do nothing.
     */
    {
        Point entry;
        for (int i = 0; i < nodes.size(); i++) {
            entry = ((TileNode) nodes.get(i)).getPoint();
            if (entry.equals(p)) {
                nodes.remove(i);
                return true;
            }
        }
        return false;
    } // end of delete()

    public TileNode findNode(Point p)
    // a linear search looking for the tile at point p;
    {
        TileNode entry;
        for (int i = 0; i < nodes.size(); i++) {
            entry = (TileNode) nodes.get(i);
            if (entry.getPoint().equals(p)) {
                return entry;
            }
        }
        return null;
    } // end of findNode()

    public int size() {
        return nodes.size();
    }
} // end of TilesList
