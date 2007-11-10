package JumpingJack;

// JackPanel.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* The game's drawing surface. Uses active rendering to a JPanel
 with the help of Java 3D's timer.

 Set up the background and sprites, and update and draw
 them every period nanosecs.

 The background is a series of ribbons (wraparound images
 that move), and a bricks ribbon which the JumpingSprite
 (called 'jack') runs and jumps along.

 'Jack' doesn't actually move horizontally, but the movement
 of the background gives the illusion that it is.

 There is a fireball sprite which tries to hit jack. It shoots
 out horizontally from the right hand edge of the panel. After
 MAX_HITS hits, the game is over. Each hit is accompanied 
 by an animated explosion and sound effect.

 The game begins with a simple introductory screen, which
 doubles as a help window during the course of play. When
 the help is shown, the game pauses.

 The game is controlled only from the keyboard, no mouse
 events are caught.
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

public class JackPanel extends JPanel implements Runnable, ImagesPlayerWatcher {
    /**
     * 
     */
    private static final long serialVersionUID = 6664180835652802441L;

    private static final int PWIDTH = 500; // size of panel

    private static final int PHEIGHT = 360;

    private static final int NO_DELAYS_PER_YIELD = 16;

    /*
     * Number of frames with a delay of 0 ms before the animation thread yields to other running threads.
     */
    private static final int MAX_FRAME_SKIPS = 5;

    // no. of frames that can be skipped in any one animation loop
    // i.e the games state is updated but not rendered

    // image, bricks map, clips loader information files
    private static final String IMS_INFO = "imsInfo.txt";

    private static final String BRICKS_INFO = "bricksInfo.txt";

    private static final String SNDS_FILE = "clipsInfo.txt";

    // names of the explosion clips
    private static final String[] exploNames = { "explo1", "explo2", "explo3" };

    private static final int MAX_HITS = 20;

    // number of times jack can be hit by a fireball before the game is over

    private Thread animator; // the thread that performs the animation

    private volatile boolean running = false; // used to stop the animation
    // thread

    private volatile boolean isPaused = false;

    private long period; // period between drawing in _nanosecs_

    private ClipsLoader clipsLoader;

    private JumperSprite jack; // the sprites

    private FireBallSprite fireball;

    private RibbonsManager ribsMan; // the ribbons manager

    private BricksManager bricksMan; // the bricks manager

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

    // explosion-related
    private ImagesPlayer explosionPlayer = null;

    private boolean showExplosion = false;

    private int explWidth, explHeight; // image dimensions

    private int xExpl, yExpl; // coords where image is drawn

    private int numHits = 0; // the number of times 'jack' has been hit

    public JackPanel(JumpingJack jj, long period) {
        this.period = period;

        setDoubleBuffered(false);
        setBackground(Color.white);
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

        // initialise the game entities
        this.bricksMan = new BricksManager(PWIDTH, PHEIGHT, BRICKS_INFO, imsLoader);
        int brickMoveSize = this.bricksMan.getMoveSize();

        this.ribsMan = new RibbonsManager(PWIDTH, PHEIGHT, brickMoveSize, imsLoader);

        this.jack = new JumperSprite(PWIDTH, PHEIGHT, brickMoveSize, this.bricksMan, imsLoader, (int) (period / 1000000L)); // in ms

        this.fireball = new FireBallSprite(PWIDTH, PHEIGHT, imsLoader, this, this.jack);

        // prepare the explosion animation
        this.explosionPlayer = new ImagesPlayer("explosion", (int) (period / 1000000L), 0.5, false, imsLoader);
        BufferedImage explosionIm = imsLoader.getImage("explosion");
        this.explWidth = explosionIm.getWidth();
        this.explHeight = explosionIm.getHeight();
        this.explosionPlayer.setWatcher(this); // report animation's end back here

        // prepare title/help screen
        this.helpIm = imsLoader.getImage("title");
        this.showHelp = true; // show at start-up
        this.isPaused = true;

        // set up message font
        this.msgsFont = new Font("SansSerif", Font.BOLD, 24);
        this.metrics = this.getFontMetrics(this.msgsFont);
    } // end of JackPanel()

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
            // move the sprite and ribbons based on the arrow key pressed
            if (keyCode == KeyEvent.VK_LEFT) {
                this.jack.moveLeft();
                this.bricksMan.moveRight(); // bricks and ribbons move the other way
                this.ribsMan.moveRight();
            } else if (keyCode == KeyEvent.VK_RIGHT) {
                this.jack.moveRight();
                this.bricksMan.moveLeft();
                this.ribsMan.moveLeft();
            } else if (keyCode == KeyEvent.VK_UP) {
                this.jack.jump(); // jumping has no effect on the bricks/ribbons
            } else if (keyCode == KeyEvent.VK_DOWN) {
                this.jack.stayStill();
                this.bricksMan.stayStill();
                this.ribsMan.stayStill();
            }
        }
    } // end of processKey()

    public void showExplosion(int x, int y)
    // called by fireball sprite when it hits jack at (x,y)
    {
        if (!this.showExplosion) { // only allow a single explosion at a time
            this.showExplosion = true;
            this.xExpl = x - this.explWidth / 2; // \ (x,y) is the center of the
            // explosion
            this.yExpl = y - this.explHeight / 2;

            /*
             * Play an explosion clip, but cycle through them. This adds variety, and gets round not being able to play multiple instances of a clip at the same time.
             */
            this.clipsLoader.play(exploNames[this.numHits % exploNames.length], false);
            this.numHits++;
        }
    } // end of showExplosion()

    public void sequenceEnded(String imageName)
    // called by ImagesPlayer when the explosion animation finishes
    {
        this.showExplosion = false;
        this.explosionPlayer.restartAt(0); // reset animation for next time

        if (this.numHits >= MAX_HITS) {
            this.gameOver = true;
            this.score = (int) ((J3DTimer.getValue() - this.gameStartTime) / 1000000000L);
            this.clipsLoader.play("applause", false);

        }
    } // end of sequenceEnded()

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
            if (this.jack.willHitBrick()) { // collision checking first
                this.jack.stayStill(); // stop jack and scenery
                this.bricksMan.stayStill();
                this.ribsMan.stayStill();
            }
            this.ribsMan.update(); // update background and sprites
            this.bricksMan.update();
            this.jack.updateSprite();
            this.fireball.updateSprite();

            if (this.showExplosion) {
                this.explosionPlayer.updateTick(); // update the animation
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

        // draw a white background
        this.dbg.setColor(Color.white);
        this.dbg.fillRect(0, 0, PWIDTH, PHEIGHT);

        // draw the game elements: order is important
        this.ribsMan.display(this.dbg); // the background ribbons
        this.bricksMan.display(this.dbg); // the bricks
        this.jack.drawSprite(this.dbg); // the sprites
        this.fireball.drawSprite(this.dbg);

        if (this.showExplosion) {
            this.dbg.drawImage(this.explosionPlayer.getCurrentImage(), this.xExpl, this.yExpl, null);
        }

        reportStats(this.dbg);

        if (this.gameOver) {
            gameOverMessage(this.dbg);
        }

        if (this.showHelp) {
            this.dbg.drawImage(this.helpIm, (PWIDTH - this.helpIm.getWidth()) / 2, (PHEIGHT - this.helpIm.getHeight()) / 2, null);
        }
    } // end of gameRender()

    private void reportStats(Graphics g)
    // Report the number of hits, and time spent playing
    {
        if (!this.gameOver) {
            this.timeSpentInGame = (int) ((J3DTimer.getValue() - this.gameStartTime) / 1000000000L); // ns
        }
        // -->
        // secs
        g.setColor(Color.red);
        g.setFont(this.msgsFont);
        g.drawString("Hits: " + this.numHits + "/" + MAX_HITS, 15, 25);
        g.drawString("Time: " + this.timeSpentInGame + " secs", 15, 50);
        g.setColor(Color.black);
    } // end of reportStats()

    private void gameOverMessage(Graphics g)
    // Center the game-over message in the panel.
    {
        String msg = "Game Over. Your score: " + this.score;

        int x = (PWIDTH - this.metrics.stringWidth(msg)) / 2;
        int y = (PHEIGHT - this.metrics.getHeight()) / 2;
        g.setColor(Color.black);
        g.setFont(this.msgsFont);
        g.drawString(msg, x, y);
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

} // end of JackPanel class
