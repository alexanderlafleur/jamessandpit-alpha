package JWS.BugRunner;

// BugPanel.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* The game's drawing surface. Uses active rendering to a JPanel
 with the help of Java 3D's timer.
 See WormP for a version with statistics generation.
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.sun.j3d.utils.timer.J3DTimer;

public class BugPanel extends JPanel implements Runnable {
    /**
     * 
     */
    private static final long serialVersionUID = 1462450050178353721L;

    private static final int PWIDTH = 500; // size of panel

    private static final int PHEIGHT = 400;

    private static final int NO_DELAYS_PER_YIELD = 16;

    /*
     * Number of frames with a delay of 0 ms before the animation thread yields to other running threads.
     */
    private static int MAX_FRAME_SKIPS = 5;

    // no. of frames that can be skipped in any one animation loop
    // i.e the games state is updated but not rendered

    // image and clip loader information files
    private static final String IMS_INFO = "imsInfo.txt";

    private static final String SNDS_FILE = "clipsInfo.txt";

    private Thread animator; // the thread that performs the animation

    private volatile boolean running = false; // used to stop the animation
    // thread

    private volatile boolean isPaused = false;

    private long period; // period between drawing in _nanosecs_

    private ClipsLoader clipsLoader;

    private BallSprite ball; // the sprites

    private BatSprite bat;

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

    // holds the background image
    private BufferedImage bgImage = null;

    public BugPanel(BugRunner br, long period) {
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

        // load the background image
        ImagesLoader imsLoader = new ImagesLoader(IMS_INFO);
        this.bgImage = imsLoader.getImage("bladerunner");

        // initialise the clips loader
        this.clipsLoader = new ClipsLoader(SNDS_FILE);

        // create game sprites
        this.bat = new BatSprite(PWIDTH, PHEIGHT, imsLoader, (int) (period / 1000000L)); // in ms
        this.ball = new BallSprite(PWIDTH, PHEIGHT, imsLoader, this.clipsLoader, this, this.bat);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                testPress(e.getX());
            } // handle mouse presses
        });

        // set up message font
        this.msgsFont = new Font("SansSerif", Font.BOLD, 24);
        this.metrics = this.getFontMetrics(this.msgsFont);

    } // end of BugPanel()

    private void processKey(KeyEvent e)
    // handles termination and game-play keys
    {
        int keyCode = e.getKeyCode();

        // termination keys
        // listen for esc, q, end, ctrl-c on the canvas to
        // allow a convenient exit from the full screen configuration
        if ((keyCode == KeyEvent.VK_ESCAPE) || (keyCode == KeyEvent.VK_Q) || (keyCode == KeyEvent.VK_END) || ((keyCode == KeyEvent.VK_C) && e.isControlDown())) {
            this.running = false;
        }

        // game-play keys
        if (!this.isPaused && !this.gameOver) {
            if (keyCode == KeyEvent.VK_LEFT) {
                this.bat.moveLeft();
            } else if (keyCode == KeyEvent.VK_RIGHT) {
                this.bat.moveRight();
            } else if (keyCode == KeyEvent.VK_DOWN) {
                this.bat.stayStill();
            }
        }
    } // end of processKey()

    public void gameOver()
    // called by BallSprite to signal that the game is over
    {
        int finalTime = (int) ((J3DTimer.getValue() - this.gameStartTime) / 1000000000L); // ns
        // -->
        // secs
        this.score = finalTime; // could be more fancy!
        this.clipsLoader.play("gameOver", false); // play a game over clip once
        this.gameOver = true;
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
        this.isPaused = false;
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

    private void testPress(int x)
    // use a mouse press to control the bat
    {
        if (!this.isPaused && !this.gameOver) {
            this.bat.mouseMove(x);
        }
    } // end of testPress()

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
            this.ball.updateSprite();
            this.bat.updateSprite();
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

        // draw the background: use the image or a black colour
        if (this.bgImage == null) {
            this.dbg.setColor(Color.black);
            this.dbg.fillRect(0, 0, PWIDTH, PHEIGHT);
        } else {
            this.dbg.drawImage(this.bgImage, 0, 0, this);
        }

        // draw game elements
        this.ball.drawSprite(this.dbg);
        this.bat.drawSprite(this.dbg);

        reportStats(this.dbg);

        if (this.gameOver) {
            gameOverMessage(this.dbg);
        }
    } // end of gameRender()

    private void reportStats(Graphics g)
    // Report the number of returned balls, and time spent playing
    {
        if (!this.gameOver) {
            this.timeSpentInGame = (int) ((J3DTimer.getValue() - this.gameStartTime) / 1000000000L); // ns
            // -->
            // secs
        }

        g.setColor(Color.yellow);
        g.setFont(this.msgsFont);

        this.ball.drawBallStats(g, 15, 25); // the ball sprite reports the ball
        // stats
        g.drawString("Time: " + this.timeSpentInGame + " secs", 15, 50);

        g.setColor(Color.black);
    } // end of reportStats()

    private void gameOverMessage(Graphics g)
    // center the game-over message in the panel
    {
        String msg = "Game Over. Your score: " + this.score;

        int x = (PWIDTH - this.metrics.stringWidth(msg)) / 2;
        int y = (PHEIGHT - this.metrics.getHeight()) / 2;
        g.setColor(Color.red);
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

} // end of BugPanel class
