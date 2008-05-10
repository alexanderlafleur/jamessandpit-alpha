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
    // image and clip loader information files
    private static final String IMS_INFO = "imsInfo.txt";
    /*
     * Number of frames with a delay of 0 ms before the animation thread yields to other running threads.
     */
    private static int MAX_FRAME_SKIPS = 5;
    private static final int NO_DELAYS_PER_YIELD = 16;
    private static final int PHEIGHT = 400;
    private static final int PWIDTH = 500; // size of panel
    // no. of frames that can be skipped in any one animation loop
    // i.e the games state is updated but not rendered
    /**
     * 
     */
    private static final long serialVersionUID = 1462450050178353721L;
    private static final String SNDS_FILE = "clipsInfo.txt";
    private Thread animator; // the thread that performs the animation
    private BallSprite ball; // the sprites
    private BatSprite bat;
    // holds the background image
    private BufferedImage bgImage = null;
    private ClipsLoader clipsLoader;
    // off-screen rendering
    private Graphics dbg;
    private Image dbImage = null;
    // used at game termination
    private volatile boolean gameOver = false;
    private long gameStartTime; // when the game started
    private volatile boolean isPaused = false;
    private FontMetrics metrics;
    // for displaying messages
    private Font msgsFont;
    private long period; // period between drawing in _nanosecs_
    private volatile boolean running = false; // used to stop the animation
    // thread
    private int score = 0;
    private int timeSpentInGame;

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
        bgImage = imsLoader.getImage("bladerunner");
        // initialise the clips loader
        clipsLoader = new ClipsLoader(SNDS_FILE);
        // create game sprites
        bat = new BatSprite(PWIDTH, PHEIGHT, imsLoader, (int) (period / 1000000L)); // in ms
        ball = new BallSprite(PWIDTH, PHEIGHT, imsLoader, clipsLoader, this, bat);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                testPress(e.getX());
            } // handle mouse presses
        });
        // set up message font
        msgsFont = new Font("SansSerif", Font.BOLD, 24);
        metrics = this.getFontMetrics(msgsFont);
    } // end of BugPanel()

    @Override
    public void addNotify()
    // wait for the JPanel to be added to the JFrame before starting
    {
        super.addNotify(); // creates the peer
        startGame(); // start the thread
    }

    public void gameOver()
    // called by BallSprite to signal that the game is over
    {
        int finalTime = (int) ((J3DTimer.getValue() - gameStartTime) / 1000000000L); // ns
        // -->
        // secs
        score = finalTime; // could be more fancy!
        clipsLoader.play("gameOver", false); // play a game over clip once
        gameOver = true;
    } // end of gameOver()

    private void gameOverMessage(Graphics g)
    // center the game-over message in the panel
    {
        String msg = "Game Over. Your score: " + score;
        int x = (PWIDTH - metrics.stringWidth(msg)) / 2;
        int y = (PHEIGHT - metrics.getHeight()) / 2;
        g.setColor(Color.red);
        g.setFont(msgsFont);
        g.drawString(msg, x, y);
    } // end of gameOverMessage()

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
        // draw the background: use the image or a black colour
        if (bgImage == null) {
            dbg.setColor(Color.black);
            dbg.fillRect(0, 0, PWIDTH, PHEIGHT);
        } else {
            dbg.drawImage(bgImage, 0, 0, this);
        }
        // draw game elements
        ball.drawSprite(dbg);
        bat.drawSprite(dbg);
        reportStats(dbg);
        if (gameOver) {
            gameOverMessage(dbg);
        }
    } // end of gameRender()

    // ------------- game life cycle methods ------------
    // called by the JFrame's window listener methods
    private void gameUpdate() {
        if (!isPaused && !gameOver) {
            ball.updateSprite();
            bat.updateSprite();
        }
    } // end of gameUpdate()

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
    } // end of paintScreen()

    public void pauseGame()
    // called when the JFrame is deactivated / iconified
    {
        isPaused = true;
    }

    // ----------------------------------------------
    private void processKey(KeyEvent e)
    // handles termination and game-play keys
    {
        int keyCode = e.getKeyCode();
        // termination keys
        // listen for esc, q, end, ctrl-c on the canvas to
        // allow a convenient exit from the full screen configuration
        if (keyCode == KeyEvent.VK_ESCAPE || keyCode == KeyEvent.VK_Q || keyCode == KeyEvent.VK_END || keyCode == KeyEvent.VK_C && e.isControlDown()) {
            running = false;
        }
        // game-play keys
        if (!isPaused && !gameOver) {
            if (keyCode == KeyEvent.VK_LEFT) {
                bat.moveLeft();
            } else if (keyCode == KeyEvent.VK_RIGHT) {
                bat.moveRight();
            } else if (keyCode == KeyEvent.VK_DOWN) {
                bat.stayStill();
            }
        }
    } // end of processKey()

    private void reportStats(Graphics g)
    // Report the number of returned balls, and time spent playing
    {
        if (!gameOver) {
            timeSpentInGame = (int) ((J3DTimer.getValue() - gameStartTime) / 1000000000L); // ns
            // -->
            // secs
        }
        g.setColor(Color.yellow);
        g.setFont(msgsFont);
        ball.drawBallStats(g, 15, 25); // the ball sprite reports the ball
        // stats
        g.drawString("Time: " + timeSpentInGame + " secs", 15, 50);
        g.setColor(Color.black);
    } // end of reportStats()

    public void resumeGame()
    // called when the JFrame is activated / deiconified
    {
        isPaused = false;
    }

    public void run()
    /* The frames of the animation are drawn inside the while loop. */
    {
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
    } // end of run()

    private void startGame()
    // initialise and start the thread
    {
        if (animator == null || !running) {
            animator = new Thread(this);
            animator.start();
        }
    } // end of startGame()

    public void stopGame()
    // called when the JFrame is closing
    {
        running = false;
    }

    private void testPress(int x)
    // use a mouse press to control the bat
    {
        if (!isPaused && !gameOver) {
            bat.mouseMove(x);
        }
    } // end of testPress()
} // end of BugPanel class
