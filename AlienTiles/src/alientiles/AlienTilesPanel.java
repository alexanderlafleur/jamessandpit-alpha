package alientiles;

// AlienTilesPanel.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* The game's drawing surface. Uses active rendering to a JPanel
 with the help of Java 3D's timer.

 Set up the game world and sprites, and update and draw
 them every period nanosecs.

 The complexity of the game world: a moving tile floor, containing
 pickups, blocks, and moving sprites, is managed by a WorldDisplay
 object.

 The game begins with a simple introductory screen, which
 doubles as a help window during the course of play. When
 the help is shown, the game pauses.

 The game is controlled only from the keyboard, no mouse
 events are caught.

 The game finished (gameOver is set to true) either when the player
 has been hit the required number of times, or when he has picked
 up all the pickups (a cup, flower pot, and watch).
 */

import com.sun.j3d.utils.timer.J3DTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class AlienTilesPanel extends JPanel implements Runnable {
    // images, clips loader information files
    private static final String IMS_INFO = "imsInfo.txt";
    // light blue for the background
    private static final Color lightBlue = new Color(0.17f, 0.87f, 1.0f);
    /*
     * Number of frames with a delay of 0 ms before the animation thread yields to other running threads.
     */
    private static final int MAX_FRAME_SKIPS = 5;
    private static final int NO_DELAYS_PER_YIELD = 16;
    private static final int PHEIGHT = 400;
    // no. of frames that can be skipped in any one animation loop
    // i.e the games state is updated but not rendered
    private static final int PWIDTH = 800; // size of panel
    /**
     *
     */
    private static final long serialVersionUID = 4794633445753988209L;
    private static final String SNDS_FILE = "clipsInfo.txt";
    private AlienSprite aliens[];
    private Thread animator; // the thread that performs the animation
    // thread
    private ClipsLoader clipsLoader;
    // off-screen rendering
    private Graphics dbg;
    private Image dbImage = null;
    // used at game termination
    private volatile boolean gameOver = false;
    private long gameStartTime; // when the game started
    private BufferedImage helpIm;
    private volatile boolean isPaused = false;
    private FontMetrics metrics;
    // for displaying messages
    private Font msgsFont;
    private long period; // period between drawing in _nanosecs_
    private PlayerSprite player;
    private volatile boolean running = false; // used to stop the animation
    private int score = 0;
    // to display the title/help screen
    private boolean showHelp;
    private int timeSpentInGame;
    // game entities
    private WorldDisplay world;

    public AlienTilesPanel(AlienTiles at, long period) {
        this.period = period;
        setDoubleBuffered(false);
        setBackground(Color.black);
        setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
        setFocusable(true);
        requestFocus(); // the JPanel now has focus, so receives key events
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                processKey(e);
            }
        });
        // initialise the loaders
        ImagesLoader imsLoader = new ImagesLoader(IMS_INFO);
        clipsLoader = new ClipsLoader(SNDS_FILE);
        // create the world, the player, and aliens
        createWorld(imsLoader);
        // prepare title/help screen
        helpIm = imsLoader.getImage("title");
        showHelp = true; // show at start-up
        isPaused = true;
        // set up message showing stuff
        msgsFont = new Font("SansSerif", Font.BOLD, 24);
        metrics = this.getFontMetrics(msgsFont);
    }

    @Override
    public void addNotify()
    // wait for the JPanel to be added to the JFrame before starting
    {
        super.addNotify(); // creates the peer
        startGame(); // start the thread
    }

    private void createWorld(ImagesLoader imsLoader)
    // create the game world, the player, and aliens
    {
        world = new WorldDisplay(imsLoader, this); // game world, a
        // WorldDisplay object
        player = new PlayerSprite(7, 12, PWIDTH, PHEIGHT, clipsLoader, imsLoader, world, this); // start
        // on
        // tile
        // (7,12)
        aliens = new AlienSprite[4];
        aliens[0] = new AlienAStarSprite(10, 11, PWIDTH, PHEIGHT, imsLoader, world);
        aliens[1] = new AlienQuadSprite(6, 21, PWIDTH, PHEIGHT, imsLoader, world);
        aliens[2] = new AlienQuadSprite(14, 20, PWIDTH, PHEIGHT, imsLoader, world);
        aliens[3] = new AlienAStarSprite(34, 34, PWIDTH, PHEIGHT, imsLoader, world);
        // use 2 AStar and 2 quad alien sprites
        // the 4th alien is positioned at an illegal tile location (34,34)
        world.addSprites(player, aliens); // tell the world about the sprites
    }

    public void gameOver()
        /*
        * PlayerSprite or WorldDisplay can call gameOver(). PlayerSprite calls it when it has been hit by aliens enough times. WorldDisplay calls it when
        * all the pickups have been picked up.
        */ {
        if (!gameOver) {
            gameOver = true;
            score = (int) ((J3DTimer.getValue() - gameStartTime) / 1000000000L);
            clipsLoader.play("applause", false);
        }
    }

    private void gameOverMessage(Graphics g)
    // Center the game-over message in the panel.
    {
        String msg = "Game Over. Your score: " + score;
        int x = (PWIDTH - metrics.stringWidth(msg)) / 2;
        int y = (PHEIGHT - metrics.getHeight()) / 2;
        g.setColor(Color.red);
        g.setFont(msgsFont);
        g.drawString(msg, x, y);
        g.setColor(Color.black);
    }

    private void gameRender() {
        if (dbImage == null) {
            dbImage = createImage(PWIDTH, PHEIGHT);
            if (dbImage == null) {
                System.out.println("dbImage is null");
                return;
            } else {
                dbg = dbImage.getGraphics();
            }
        }
        // a light blue background
        dbg.setColor(lightBlue);
        dbg.fillRect(0, 0, PWIDTH, PHEIGHT);
        // draw the game elements: order is important
        world.draw(dbg);
        /*
         * WorldDisplay draws the game world: the tile floor, blocks, pickups, and the sprites.
         */
        reportStats(dbg);
        if (gameOver) {
            gameOverMessage(dbg);
        }
        if (showHelp) {
            dbg.drawImage(helpIm, (PWIDTH - helpIm.getWidth()) / 2, (PHEIGHT - helpIm.getHeight()) / 2, null);
        }
    }

    // ------------- game life cycle methods ------------
    // called by the JFrame's window listener methods

    private void gameUpdate() {
        if (!isPaused && !gameOver) {
            for (AlienSprite element : aliens) {
                element.update(); // update all the aliens
            }
        }
    }

    private void paintScreen()
    // use active rendering to put the buffered image on-screen
    {
        Graphics g;
        try {
            g = this.getGraphics();
            if (g != null && dbImage != null) {
                g.drawImage(dbImage, 0, 0, null);
            }
            // Sync the display on some systems.
            // (on Linux, this fixes event queue problems)
            Toolkit.getDefaultToolkit().sync();
            g.dispose();
        } catch (Exception e) {
            System.out.println("Graphics context error: " + e);
        }
    }

    public void pauseGame()
    // called when the JFrame is deactivated / iconified
    {
        isPaused = true;
    }

    // ----------------------------------------------

    private void processKey(KeyEvent e)
    // handles termination, help, and game-play keys
    {
        int keyCode = e.getKeyCode();
        // termination keys
        // listen for esc, q, end, ctrl-c on the canvas to
        // allow a convenient exit from the full screen configuration
        if (keyCode == KeyEvent.VK_ESCAPE || keyCode == KeyEvent.VK_Q || keyCode == KeyEvent.VK_END || keyCode == KeyEvent.VK_C && e.isControlDown()) {
            running = false;
        }
        // help controls
        if (keyCode == KeyEvent.VK_H) {
            if (showHelp) { // help being shown
                showHelp = false; // switch off
                isPaused = false;
            } else { // help not being shown
                showHelp = true; // show it
                isPaused = true; // isPaused may already be true
            }
        }
        // game-play keys
        if (!isPaused && !gameOver) {
            // move the player based on the numpad key pressed
            if (keyCode == KeyEvent.VK_NUMPAD7) {
                player.move(TiledSprite.NW); // move north west
            } else if (keyCode == KeyEvent.VK_NUMPAD9) {
                player.move(TiledSprite.NE); // north east
            } else if (keyCode == KeyEvent.VK_NUMPAD3) {
                player.move(TiledSprite.SE); // south east
            } else if (keyCode == KeyEvent.VK_NUMPAD1) {
                player.move(TiledSprite.SW); // south west
            } else if (keyCode == KeyEvent.VK_NUMPAD5) {
                player.standStill(); // stand still
            } else if (keyCode == KeyEvent.VK_NUMPAD2) {
                player.tryPickup(); // try to pick up from this tile
            }
        }
    }

    private void reportStats(Graphics g)
    // Report time spent playing, the number of hits, pickups left
    {
        if (!gameOver) {
            timeSpentInGame = (int) ((J3DTimer.getValue() - gameStartTime) / 1000000000L); // ns
        }
        // -->
        // secs
        g.setColor(Color.red);
        g.setFont(msgsFont);
        g.drawString("Time: " + timeSpentInGame + " secs", 15, 25);
        g.drawString(player.getHitStatus(), 15, 50); // ask the player
        g.drawString(world.getPickupsStatus(), 15, 75); // ask WorldDisplay
        g.setColor(Color.black);
    }

    public void resumeGame()
    // called when the JFrame is activated / deiconified
    {
        if (!showHelp) {
            isPaused = false;
        }
    }

    public void run()
        /* The frames of the animation are drawn inside the while loop. */ {
        long beforeTime, afterTime, timeDiff, sleepTime;
        long overSleepTime = 0L;
        int noDelays = 0;
        long excess = 0L;
        gameStartTime = J3DTimer.getValue();
        beforeTime = gameStartTime;
        running = true;
        while (running) {
            gameUpdate();
            gameRender();
            paintScreen();
            afterTime = J3DTimer.getValue();
            timeDiff = afterTime - beforeTime;
            sleepTime = period - timeDiff - overSleepTime;
            if (sleepTime > 0) { // some time left in this cycle
                try {
                    Thread.sleep(sleepTime / 1000000L); // nano -> ms
                } catch (InterruptedException ex) {
                }
                overSleepTime = J3DTimer.getValue() - afterTime - sleepTime;
            } else { // sleepTime <= 0; the frame took longer than the period
                excess -= sleepTime; // store excess time value
                overSleepTime = 0L;
                if (++noDelays >= NO_DELAYS_PER_YIELD) {
                    Thread.yield(); // give another thread a chance to run
                    noDelays = 0;
                }
            }
            beforeTime = J3DTimer.getValue();
            /*
             * If frame animation is taking too long, update the game state without rendering it, to get the updates/sec nearer to the required FPS.
             */
            int skips = 0;
            while (excess > period && skips < MAX_FRAME_SKIPS) {
                excess -= period;
                gameUpdate(); // update state but don't render
                skips++;
            }
        }
        System.exit(0); // so window disappears
    }

    private void startGame()
    // initialise and start the thread
    {
        if (animator == null || !running) {
            animator = new Thread(this);
            animator.start();
        }
    }

    public void stopGame()
    // called when the JFrame is closing
    {
        running = false;
    }
} 
