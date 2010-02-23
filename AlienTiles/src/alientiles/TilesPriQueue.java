package alientiles;

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
        */ {
        double newScore = node.getScore();
        TileNode entry;
        for (int i = 0; i < nodes.size(); i++) {
            entry = (TileNode) nodes.get(i);
            if (newScore <= entry.getScore()) {
                nodes.add(i, node);
                return;
            }
        }
        nodes.add(node); // add node at end of list
    }

    public TileNode removeFirst() {
        return (TileNode) nodes.remove(0);
    }
}
