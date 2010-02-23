package alientiles;

// AlienSprite.java 
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* Represents an alien as a subclass of TiledSprite.

 A subclass of AlienSprite will overide:
 void playerHasMoved()   -- called when the player has moved
 void move()             -- move the alien
 */

import java.awt.*;

public class AlienSprite extends TiledSprite {
    private final static int UPDATE_FREQ = 30;
    private int updateCounter = 0;

    public AlienSprite(int x, int y, int w, int h, ImagesLoader imsLd, WorldDisplay wd) {
        super(x, y, w, h, imsLd, "baddieStill", wd);
    }

    private boolean hitPlayer()
        /*
        * If the alien is on the same tile as the player, then the player is 'hit'. Tell WorldDisplay. The returned boolean states whether there was a
        * hit or not.
        */ {
        Point playerLoc = world.getPlayerLoc();
        if (playerLoc.equals(getTileLoc())) {
            world.hitByAlien(); // whack!
            return true;
        }
        return false;
    }

    protected void move()
        /*
        * Move in a random direction. If that way is blocked then randomly try another direction. This approach may lead to the sprite getting stuck, but
        * its unlikely.
        */ {
        int quad = getRandDirection();
        Point newPt;
        while ((newPt = tryMove(quad)) == null) {
            quad = getRandDirection();
        }
        // the loop could repeat for a while,
        // but it should eventually find a direction
        setMove(newPt, quad);
    }

    public void playerHasMoved(Point playerLoc)
        /*
        * Called by the WorldDisplay object whenever the player has moved. This can be used to trigger a recalculation of the alien's movement 'plan'.
        */ {
    }

    protected void setMove(Point newPt, int quad)
        /*
        * Move the alien to a new tile and change its sprite. The new point is checked, but this should have been done previously when the new point was
        * being selected.
        */ {
        if (world.validTileLoc(newPt.x, newPt.y)) { // should be ok
            setTileLoc(newPt);
            if (quad == NE || quad == SE) {
                setImage("baddieRight");
            } else if (quad == SW || quad == NW) {
                setImage("baddieLeft");
            } else {
                System.out.println("Unknown alien quadrant: " + quad);
            }
        } else {
            System.out.println("Cannot move alien to (" + newPt.x + ", " + newPt.y + ")");
        }
    }

    public void update()
        /*
        * Called by AlienTilesPanel to update the alien. Try to hit the player. If the player cannot be hit then move. The alien will only be updated
        * every UPDATE_FREQ calls to update() thereby slowing it down, otherwise it responds to quickly to the player's movements.
        */ {
        updateCounter = (updateCounter + 1) % UPDATE_FREQ;
        if (updateCounter == 0) { // reduced update frequency
            if (!hitPlayer()) {
                move();
            }
        }
    }
} 
