package AlienTiles;

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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.sun.j3d.utils.timer.J3DTimer;

public class AlienTilesPanel extends JPanel implements Runnable {
    /**
     * 
     */
    private static final long serialVersionUID = 4794633445753988209L;

    private static final int PWIDTH = 800; // size of panel

    private static final int PHEIGHT = 400;

    private static final int NO_DELAYS_PER_YIELD = 16;

    /*
     * Number of frames with a delay of 0 ms before the animation thread yields to other running threads.
     */
    private static final int MAX_FRAME_SKIPS = 5;

    // no. of frames that can be skipped in any one animation loop
    // i.e the games state is updated but not rendered

    // images, clips loader information files
    private static final String IMS_INFO = "imsInfo.txt";

    private static final String SNDS_FILE = "clipsInfo.txt";

    // light blue for the background
    private static final Color lightBlue = new Color(0.17f, 0.87f, 1.0f);

    private Thread animator; // the thread that performs the animation

    private volatile boolean running = false; // used to stop the animation

    // thread

    private volatile boolean isPaused = false;

    private long period; // period between drawing in _nanosecs_

    private ClipsLoader clipsLoader;

    // game entities
    private WorldDisplay world;

    private PlayerSprite player;

    private AlienSprite aliens[];

    private long gameStartTime; // when the game started

    private int timeSpentInGame;

    // used at game termination
    private volatile boolean gameOver = false;

    private int score = 0;

    // for displaying messages
    private Font msgsFont;

    private FontMetrics metrics;

    // off-screen rendering
    private Graphics dbg;

    private Image dbImage = null;

    // to display the title/help screen
    private boolean showHelp;

    private BufferedImage helpIm;

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
        this.clipsLoader = new ClipsLoader(SNDS_FILE);

        // create the world, the player, and aliens
        createWorld(imsLoader);

        // prepare title/help screen
        this.helpIm = imsLoader.getImage("title");
        this.showHelp = true; // show at start-up
        this.isPaused = true;

        // set up message showing stuff
        this.msgsFont = new Font("SansSerif", Font.BOLD, 24);
        this.metrics = this.getFontMetrics(this.msgsFont);
    } // end of AlienTilesPanel()

    private void createWorld(ImagesLoader imsLoader)
    // create the game world, the player, and aliens
    {
        this.world = new WorldDisplay(imsLoader, this); // game world, a
        // WorldDisplay object

        this.player = new PlayerSprite(7, 12, PWIDTH, PHEIGHT, this.clipsLoader, imsLoader, this.world, this); // start
        // on
        // tile
        // (7,12)

        this.aliens = new AlienSprite[4];

        this.aliens[0] = new AlienAStarSprite(10, 11, PWIDTH, PHEIGHT, imsLoader, this.world);
        this.aliens[1] = new AlienQuadSprite(6, 21, PWIDTH, PHEIGHT, imsLoader, this.world);
        this.aliens[2] = new AlienQuadSprite(14, 20, PWIDTH, PHEIGHT, imsLoader, this.world);
        this.aliens[3] = new AlienAStarSprite(34, 34, PWIDTH, PHEIGHT, imsLoader, this.world);
        // use 2 AStar and 2 quad alien sprites
        // the 4th alien is positioned at an illegal tile location (34,34)

        this.world.addSprites(this.player, this.aliens); // tell the world about the sprites
    } // end of createWorld()

    private void processKey(KeyEvent e)
    // handles termination, help, and game-play keys
    {
        int keyCode = e.getKeyCode();

        // termination keys
        // listen for esc, q, end, ctrl-c on the canvas to
        // allow a convenient exit from the full screen configuration
        if ((keyCode == KeyEvent.VK_ESCAPE) || (keyCode == KeyEvent.VK_Q) || (keyCode == KeyEvent.VK_END) || ((keyCode == KeyEvent.VK_C) && e.isControlDown())) {
            this.running = false;
        }

        // help controls
        if (keyCode == KeyEvent.VK_H) {
            if (this.showHelp) { // help being shown
                this.showHelp = false; // switch off
                this.isPaused = false;
            } else { // help not being shown
                this.showHelp = true; // show it
                this.isPaused = true; // isPaused may already be true
            }
        }

        // game-play keys
        if (!this.isPaused && !this.gameOver) {
            // move the player based on the numpad key pressed
            if (keyCode == KeyEvent.VK_NUMPAD7) {
                this.player.move(TiledSprite.NW); // move north west
            } else if (keyCode == KeyEvent.VK_NUMPAD9) {
                this.player.move(TiledSprite.NE); // north east
            } else if (keyCode == KeyEvent.VK_NUMPAD3) {
                this.player.move(TiledSprite.SE); // south east
            } else if (keyCode == KeyEvent.VK_NUMPAD1) {
                this.player.move(TiledSprite.SW); // south west
            } else if (keyCode == KeyEvent.VK_NUMPAD5) {
                this.player.standStill(); // stand still
            } else if (keyCode == KeyEvent.VK_NUMPAD2) {
                this.player.tryPickup(); // try to pick up from this tile
            }
        }
    } // end of processKey()

    public void gameOver()
    /*
     * PlayerSprite or WorldDisplay can call gameOver(). PlayerSprite calls it when it has been hit by aliens enough times. WorldDisplay calls it when all the pickups have been
     * picked up.
     */
    {
        if (!this.gameOver) {
            this.gameOver = true;
            this.score = (int) ((J3DTimer.getValue() - this.gameStartTime) / 1000000000L);
            this.clipsLoader.play("applause", false);
        }
    } // end of gameOver()

    @Override
    public void addNotify()
    // wait for the JPanel to be added to the JFrame before starting
    {
        super.addNotify(); // creates the peer
        startGame(); // start the thread
    }

    private void startGame()
    // initialise and start the thread
    {
        if (this.animator == null || !this.running) {
            this.animator = new Thread(this);
            this.animator.start();
        }
    } // end of startGame()

    // ------------- game life cycle methods ------------
    // called by the JFrame's window listener methods

    public void resumeGame()
    // called when the JFrame is activated / deiconified
    {
        if (!this.showHelp) {
            this.isPaused = false;
        }
    }

    public void pauseGame()
    // called when the JFrame is deactivated / iconified
    {
        this.isPaused = true;
    }

    public void stopGame()
    // called when the JFrame is closing
    {
        this.running = false;
    }

    // ----------------------------------------------

    public void run()
    /* The frames of the animation are drawn inside the while loop. */
    {
        long beforeTime, afterTime, timeDiff, sleepTime;
        long overSleepTime = 0L;
        int noDelays = 0;
        long excess = 0L;

        this.gameStartTime = J3DTimer.getValue();
        beforeTime = this.gameStartTime;

        this.running = true;

        while (this.running) {
            gameUpdate();
            gameRender();
            paintScreen();

            afterTime = J3DTimer.getValue();
            timeDiff = afterTime - beforeTime;
            sleepTime = (this.period - timeDiff) - overSleepTime;

            if (sleepTime > 0) { // some time left in this cycle
                try {
                    Thread.sleep(sleepTime / 1000000L); // nano -> ms
                } catch (InterruptedException ex) {
                }
                overSleepTime = (J3DTimer.getValue() - afterTime) - sleepTime;
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
            while ((excess > this.period) && (skips < MAX_FRAME_SKIPS)) {
                excess -= this.period;
                gameUpdate(); // update state but don't render
                skips++;
            }
        }
        System.exit(0); // so window disappears
    } // end of run()

    private void gameUpdate() {
        if (!this.isPaused && !this.gameOver) {
            for (AlienSprite element : this.aliens) {
                element.update(); // update all the aliens
            }
        }
    } // end of gameUpdate()

    private void gameRender() {
        if (this.dbImage == null) {
            this.dbImage = createImage(PWIDTH, PHEIGHT);
            if (this.dbImage == null) {
                System.out.println("dbImage is null");
                return;
            } else {
                this.dbg = this.dbImage.getGraphics();
            }
        }

        // a light blue background
        this.dbg.setColor(lightBlue);
        this.dbg.fillRect(0, 0, PWIDTH, PHEIGHT);

        // draw the game elements: order is important
        this.world.draw(this.dbg);
        /*
         * WorldDisplay draws the game world: the tile floor, blocks, pickups, and the sprites.
         */

        reportStats(this.dbg);

        if (this.gameOver) {
            gameOverMessage(this.dbg);
        }

        if (this.showHelp) {
            this.dbg.drawImage(this.helpIm, (PWIDTH - this.helpIm.getWidth()) / 2, (PHEIGHT - this.helpIm.getHeight()) / 2, null);
        }
    } // end of gameRender()

    private void reportStats(Graphics g)
    // Report time spent playing, the number of hits, pickups left
    {
        if (!this.gameOver) {
            this.timeSpentInGame = (int) ((J3DTimer.getValue() - this.gameStartTime) / 1000000000L); // ns
        }
        // -->
        // secs
        g.setColor(Color.red);
        g.setFont(this.msgsFont);
        g.drawString("Time: " + this.timeSpentInGame + " secs", 15, 25);
        g.drawString(this.player.getHitStatus(), 15, 50); // ask the player
        g.drawString(this.world.getPickupsStatus(), 15, 75); // ask WorldDisplay
        g.setColor(Color.black);
    } // end of reportStats()

    private void gameOverMessage(Graphics g)
    // Center the game-over message in the panel.
    {
        String msg = "Game Over. Your score: " + this.score;

        int x = (PWIDTH - this.metrics.stringWidth(msg)) / 2;
        int y = (PHEIGHT - this.metrics.getHeight()) / 2;
        g.setColor(Color.red);
        g.setFont(this.msgsFont);
        g.drawString(msg, x, y);
        g.setColor(Color.black);
    } // end of gameOverMessage()

    private void paintScreen()
    // use active rendering to put the buffered image on-screen
    {
        Graphics g;
        try {
            g = this.getGraphics();
            if ((g != null) && (this.dbImage != null)) {
                g.drawImage(this.dbImage, 0, 0, null);
            }
            // Sync the display on some systems.
            // (on Linux, this fixes event queue problems)
            Toolkit.getDefaultToolkit().sync();
            g.dispose();
        } catch (Exception e) {
            System.out.println("Graphics context error: " + e);
        }
    } // end of paintScreen()

} // end of AlienTilesPanel class
