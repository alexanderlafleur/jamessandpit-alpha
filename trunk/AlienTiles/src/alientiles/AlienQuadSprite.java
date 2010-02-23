package alientiles;

// AlienQuadSprite.java 
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* A subclass of AlienSprite.
 It overrides:
 void playerHasMoved()   -- called when the player has moved
 void move()             -- move the alien

 This alien calculates a quadrant direction for the sprite 
 (NE, SE, SW, NW). The calculation is repeated made whenever 
 the player moves.

 The direction is obtained by finding the nearest pickup
 point to the player, then finding that pickup's quadrant
 position relative to the alien.

 This gives the alien a 'pickup guarding' behaviour, where an
 alien moves to guard the pickup that the player is heading
 towards.
 */

import java.awt.*;

public class AlienQuadSprite extends AlienSprite {
	// quadrant direction for the sprite
	private int currentQuad;

	public AlienQuadSprite(int x, int y, int w, int h, ImagesLoader imsLd, WorldDisplay wd) {
		super(x, y, w, h, imsLd, wd);
		currentQuad = getRandDirection(); // random starting quad direction
	}

	private int calcQuadrant(Point pickupPt)
		/*
				* Roughly calculate a quadrant by comparing the pickup's point with the alien's position.
				*/ {
		if (pickupPt.x > xTile && pickupPt.y > yTile) {
			return SE;
		} else if (pickupPt.x > xTile && pickupPt.y < yTile) {
			return NE;
		} else if (pickupPt.x < xTile && pickupPt.y > yTile) {
			return SW;
		} else {
			return NW;
		}
	}

	@Override
	protected void move()
		/*
				* Try to move in the currentQuad direction. If that way is blocked then randomly try another direction. This approach may lead to the sprite
				* getting stuck, but its unlikely.
				*/ {
		int quad = currentQuad;
		Point newPt;
		while ((newPt = tryMove(quad)) == null) {
			quad = getRandDirection();
		}
		// the loop could repeat for a while, but it should eventually find a
		// way
		setMove(newPt, quad);
	}

	@Override
	public void playerHasMoved(Point playerLoc)
		/*
				* Update quadrant direction by heading towards the pickup that is closest to the player.
				*/ {
		if (world.hasPickupsLeft()) {
			Point nearPickup = world.nearestPickup(playerLoc); // nearest
			// pickup to the
			// player
			currentQuad = calcQuadrant(nearPickup);
		}
	}
} 
