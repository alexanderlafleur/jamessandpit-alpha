package FourByFour;

// Positions.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/*
 *	@(#)Positions.java 1.11 01/01/11 07:32:12
 * Copyright (c) 1996-2001 Sun Microsystems, Inc. All Rights Reserved

 The Positions class creates the spheres and boxes used to indicate
 a player's move. Initially small white spheres occupy the positions,
 but these are switched for red spheres or blue boxes when a 
 player clicks on a position.

 Player 1 is represented by red spheres.
 Player 2 is represented by blue cubes.
 */

import java.util.BitSet;

import javax.media.j3d.Appearance;
import javax.media.j3d.Group;
import javax.media.j3d.Material;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Switch;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Sphere;

public class Positions {
    private final static int NUM_SPOTS = 64; // in the game board (4*4*4)

    private final static int PLAYER1 = 1;

    private final static int PLAYER2 = 2; // was MACHINE

    private Switch posSwitch, player1Switch, player2Switch;

    private BitSet posMask, player1Mask, player2Mask;

    private Appearance blueApp, redApp, whiteApp;

    private Vector3f[] points;

    private Group group;

    public Positions() {
        initAppearances();
        initLocations();

        this.group = new Group();
        // create the various markers and add them to a group
        makeWhiteSpheres();
        makeRedSpheres();
        makeBlueCubes();
    } // end of Positions()

    private void initAppearances()
    // create the white, red and blue materials
    {
        // define colors
        Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
        Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
        Color3f red = new Color3f(0.9f, 0.1f, 0.2f);
        Color3f blue = new Color3f(0.3f, 0.3f, 0.8f);
        Color3f ambRed = new Color3f(0.3f, 0.03f, 0.03f);
        Color3f ambBlue = new Color3f(0.03f, 0.03f, 0.3f);
        Color3f ambWhite = new Color3f(0.3f, 0.3f, 0.3f);
        Color3f specular = new Color3f(1.0f, 1.0f, 1.0f);

        // Create the red appearance node
        Material redMat = new Material(ambRed, black, red, specular, 100.f);
        redMat.setLightingEnable(true);
        this.redApp = new Appearance();
        this.redApp.setMaterial(redMat);

        // Create the blue appearance node
        Material blueMat = new Material(ambBlue, black, blue, specular, 100.f);
        blueMat.setLightingEnable(true);
        this.blueApp = new Appearance();
        this.blueApp.setMaterial(blueMat);

        // Create the white appearance node
        Material whiteMat = new Material(ambWhite, black, white, specular, 100.f);
        whiteMat.setLightingEnable(true);
        this.whiteApp = new Appearance();
        this.whiteApp.setMaterial(whiteMat);
    } // end of initAppearances()

    private void initLocations()
    /*
     * Load point array with the offsets (coordinates) for each of the NUM_SPOTS positions.
     */
    {
        this.points = new Vector3f[NUM_SPOTS];
        int count = 0;
        for (int z = -30; z < 40; z += 20) {
            for (int y = -30; y < 40; y += 20) {
                for (int x = -30; x < 40; x += 20) {
                    this.points[count] = new Vector3f(x, y, z);
                    count++;
                }
            }
        }
    } // end of initLocations()

    private void makeWhiteSpheres()
    /*
     * Create the small white spheres that mark unoccupied positions. Place them in a switch with a bitset for turning each one on/off.
     */
    {
        // Create the switch nodes
        this.posSwitch = new Switch(Switch.CHILD_MASK);
        // Set the capability bits
        this.posSwitch.setCapability(Switch.ALLOW_SWITCH_READ);
        this.posSwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);
        this.posMask = new BitSet(); // create the bit mask

        Sphere posSphere;
        for (int i = 0; i < NUM_SPOTS; i++) {
            Transform3D t3d = new Transform3D();
            t3d.set(this.points[i]); // set position
            TransformGroup tg = new TransformGroup(t3d);
            posSphere = new Sphere(2.0f, this.whiteApp);
            Shape3D shape = posSphere.getShape();
            shape.setUserData(new Integer(i)); // add board position ID to each
            // shape
            tg.addChild(posSphere);
            this.posSwitch.addChild(tg);
            this.posMask.set(i); // make visible
        }
        // Set the positions mask
        this.posSwitch.setChildMask(this.posMask);

        this.group.addChild(this.posSwitch);
    } // end of makeWhiteSpheres()

    private void makeRedSpheres()
    // Create the red spheres that mark the user's positions.
    // Place them in a switch with a bitset for turning each one on/off
    {
        // Create the switch nodes
        this.player1Switch = new Switch(Switch.CHILD_MASK);
        // Set the capability bits
        this.player1Switch.setCapability(Switch.ALLOW_SWITCH_READ);
        this.player1Switch.setCapability(Switch.ALLOW_SWITCH_WRITE);
        this.player1Mask = new BitSet(); // create the bit mask

        for (int i = 0; i < NUM_SPOTS; i++) {
            Transform3D t3d = new Transform3D();
            t3d.set(this.points[i]); // set position
            TransformGroup tg = new TransformGroup(t3d);
            tg.addChild(new Sphere(7.0f, this.redApp));
            this.player1Switch.addChild(tg);
            this.player1Mask.clear(i); // make invisible
        }
        // Set the positions mask
        this.player1Switch.setChildMask(this.player1Mask);

        this.group.addChild(this.player1Switch);
    } // end of makeRedSpheres()

    private void makeBlueCubes()
    // Create the blue cubes that mark the computer's positions.
    // Place them in a switch with a bitset for turning each one on/off
    {
        // Create the switch nodes
        this.player2Switch = new Switch(Switch.CHILD_MASK);
        // Set the capability bits
        this.player2Switch.setCapability(Switch.ALLOW_SWITCH_READ);
        this.player2Switch.setCapability(Switch.ALLOW_SWITCH_WRITE);
        this.player2Mask = new BitSet(); // create the bit mask

        for (int i = 0; i < NUM_SPOTS; i++) {
            Transform3D t3d = new Transform3D();
            t3d.set(this.points[i]); // set position
            TransformGroup tg = new TransformGroup(t3d);
            tg.addChild(new Box(5.0f, 5.0f, 5.0f, this.blueApp));
            this.player2Switch.addChild(tg);
            this.player2Mask.clear(i); // make invisible
        }
        // Set the positions mask
        this.player2Switch.setChildMask(this.player2Mask);

        this.group.addChild(this.player2Switch);
    } // end of makeBlueCubes()

    public Group getChild() {
        return this.group;
    }

    public void set(int pos, int player)
    // called by Board to update the 3D scene
    {
        // System.out.println("Player " + player + " clicked on position " +
        // pos);
        // turn off the white position marker for the given position
        this.posMask.clear(pos);
        this.posSwitch.setChildMask(this.posMask);

        // turn on one of the player markers
        if (player == PLAYER1) {
            this.player1Mask.set(pos);
            this.player1Switch.setChildMask(this.player1Mask); // red sphere for p1
        } else if (player == PLAYER2) {
            this.player2Mask.set(pos);
            this.player2Switch.setChildMask(this.player2Mask); // blue cube for p2
        } else {
            System.out.println("Positions set() call with illegal player value: " + player);
        }
    } // end of set()

} // end of Positions class
