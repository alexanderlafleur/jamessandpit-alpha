package AlienTiles;

// AlienTiles.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* AlienTiles is a basic isometric game consisting of one player
 who must pick up 3 items (a cup, flower pot, and a watch)
 before 4 nasty aliens have hit him 3 times.

 The player is moved using the number pad keys: 7, 9, 3, 1, 5,
 and 2, as explained in the help screen.

 The aliens are of two types: those that actively chase after
 the player (AlienAStarSprite objects) and those that congregate
 around the pickup that the player is heading towards 
 (AlienQuadSprite objects). 

 The AlienAStarSprite class uses A* pathfinding to chase the player.

 The playing area is an isometric map containing 'no-go' tiles
 which the sprites cannot move on to. Some tiles 
 contain 'blocks' which also stop the sprites.

 When a sprite moves behind a block, the sprite is suitably obscured.

 There are various sound effects, and background music.

 The Player sprite does not actually move relative to the game's
 JPanel. Instead the tile map, pickups, blocks, and aliens are 
 repositioned. 

 The tile map graphic is a single GIF, not multiple tile images.

 -----
 Pausing/Resuming/Quiting are controlled via the frame's window
 listener methods.

 Active rendering is used to update the JPanel. See WormP for
 another example, with additional statistics generation.

 Using Java 3D's timer: J3DTimer.getValue()
 *  nanosecs rather than millisecs for the period

 The MidisLoader, ClipsLoader, and ImagesLoader
 classes are used for music and image manipulation.

 The player and tyhe alien sprites are subclasses of the 
 Sprite class discussed in chapter 6.
 */
import java.awt.Container;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

public class AlienTiles extends JFrame implements WindowListener {
    private static int DEFAULT_FPS = 40;
    private static final long serialVersionUID = 615308493168699580L;

    public static void main(String args[]) {
        long period = (long) 1000.0 / DEFAULT_FPS;
        // System.out.println("fps: " + DEFAULT_FPS + "; period: " + period + "
        // ms");
        new AlienTiles(period * 1000000L); // ms --> nanosecs
    }

    private AlienTilesPanel atp; // where the game is drawn
    private MidisLoader midisLoader;

    // ----------------- window listener methods -------------
    public AlienTiles(long period) {
        super("AlienTiles");
        // load the background MIDI sequence
        midisLoader = new MidisLoader();
        midisLoader.load("mi", "Mission_Impossible.mid");
        midisLoader.play("mi", true); // repeatedly play it
        Container c = getContentPane(); // default BorderLayout used
        atp = new AlienTilesPanel(this, period);
        c.add(atp, "Center");
        addWindowListener(this);
        pack();
        setResizable(false);
        setVisible(true);
    } // end of AlienTiles() constructor

    public void windowActivated(WindowEvent e) {
        atp.resumeGame();
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        atp.stopGame();
        midisLoader.close(); // not really required
    }

    public void windowDeactivated(WindowEvent e) {
        atp.pauseGame();
    }

    public void windowDeiconified(WindowEvent e) {
        atp.resumeGame();
    }

    public void windowIconified(WindowEvent e) {
        atp.pauseGame();
    }

    // ----------------------------------------------------
    public void windowOpened(WindowEvent e) {
    }
} // end of AlienTiles class
