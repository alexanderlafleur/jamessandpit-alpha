package AlienTiles;

// TilesPriQueue.java 
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* A subclass of TilesList.

 A TilesPriQueue object stores the 'open' list of tiles that 
 have not yet been visited by the A* algorithm.
 The TileNodes in TilesList are ordered by decreasing node score.

 A* pathfinding is utilized by AlienAStarSprite objects.
 */

public class TilesPriQueue extends TilesList {
    public TilesPriQueue() {
        super();
    }

    public TilesPriQueue(TileNode node) {
        super(node);
    }

    @Override
    public void add(TileNode node)
    /*
     * Override add() so the list is built in decreasing order by score.
     */
    {
        double newScore = node.getScore();
        TileNode entry;
        for (int i = 0; i < this.nodes.size(); i++) {
            entry = (TileNode) this.nodes.get(i);
            if (newScore <= entry.getScore()) {
                this.nodes.add(i, node);
                return;
            }
        }
        this.nodes.add(node); // add node at end of list
    } // end of add()

    public TileNode removeFirst() {
        return (TileNode) this.nodes.remove(0);
    }

} // end of TilesPriQueue
