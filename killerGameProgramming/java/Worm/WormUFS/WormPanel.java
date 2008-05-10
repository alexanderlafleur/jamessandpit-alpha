package Worm.WormUFS;

// WormPanel.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* The game's drawing surface. It shows:
 - the moving worm
 - the obstacles (blue boxes)
 - the current average FPS and UPS
 - quit and pause 'buttons' drawn on the JPanel

 Located in /WormUFF
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.text.DecimalFormat;

import javax.swing.JPanel;

import com.sun.j3d.utils.timer.J3DTimer;

public class WormPanel extends JPanel implements Runnable {
    private static int MAX_FRAME_SKIPS = 5; // was 2;
    private static long MAX_STATS_INTERVAL = 1000000000L;
    // private static long MAX_STATS_INTERVAL = 1000L;
    // record stats every 1 second (roughly)
    private static final int NO_DELAYS_PER_YIELD = 16;
    /*
     * Number of frames with a delay of 0 ms before the animation thread yields to other running threads.
     */
    private static int NUM_FPS = 10;
    // no. of frames that can be skipped in any one animation loop
    // i.e the games state is updated but not rendered
    /**
     * 
     */
    private static final long serialVersionUID = -6425541281626212818L;
    // number of FPS values stored to get an average
    private Thread animator; // the thread that performs the animation
    private double averageFPS = 0.0;
    private double averageUPS = 0.0;
    private int boxesUsed = 0;
    // off-screen rendering
    private Graphics dbg;
    private Image dbImage = null;
    private DecimalFormat df = new DecimalFormat("0.##"); // 2 dp
    private boolean finishedOff = false;
    private Font font;
    private double fpsStore[];
    private long frameCount = 0;
    private long framesSkipped = 0L;
    private Worm fred; // the worm
    // used at game termination
    private volatile boolean gameOver = false;
    private long gameStartTime;
    // used by the pause 'button'
    private volatile boolean isOverPauseButton = false;
    // used by quit 'button'
    private volatile boolean isOverQuitButton = false;
    private volatile boolean isPaused = false;
    private FontMetrics metrics;
    private Obstacles obs; // the obstacles
    private Rectangle pauseArea;
    private long period; // period between drawing in _nanosecs_
    private long prevStatsTime;
    private int pWidth, pHeight; // panel dimensions
    private Rectangle quitArea;
    private volatile boolean running = false; // used to stop the animation
    // thread
    private int score = 0;
    private long statsCount = 0;
    // used for gathering statistics
    private long statsInterval = 0L; // in ns
    private DecimalFormat timedf = new DecimalFormat("0.####"); // 4 dp
    private int timeSpentInGame = 0; // in seconds
    private long totalElapsedTime = 0L;
    private long totalFramesSkipped = 0L;
    private double upsStore[];

    public WormPanel(WormChase wc, long period) {
        this.period = period;
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension scrDim = tk.getScreenSize();
        pWidth = scrDim.width;
        pHeight = scrDim.height;
        setBackground(Color.white);
        setPreferredSize(scrDim);
        setFocusable(true);
        requestFocus(); // the JPanel now has focus, so receives key events
        readyForTermination();
        // create game components
        obs = new Obstacles(this);
        fred = new Worm(pWidth, pHeight, obs);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                testPress(e.getX(), e.getY());
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                testMove(e.getX(), e.getY());
            }
        });
        // set up message font
        font = new Font("SansSerif", Font.BOLD, 24);
        metrics = this.getFontMetrics(font);
        // specify screen areas for the buttons
        pauseArea = new Rectangle(pWidth - 100, pHeight - 45, 70, 15);
        quitArea = new Rectangle(pWidth - 100, pHeight - 20, 70, 15);
        // initialise timing elements
        fpsStore = new double[NUM_FPS];
        upsStore = new double[NUM_FPS];
        for (int i = 0; i < NUM_FPS; i++) {
            fpsStore[i] = 0.0;
            upsStore[i] = 0.0;
        }
    } // end of WormPanel()

    @Override
    public void addNotify()
    // wait for the JPanel to be added to the JFrame before starting
    {
        super.addNotify(); // creates the peer
        if (animator == null || !running) { // start the thread
            animator = new Thread(this);
            animator.start();
        }
    } // end of addNotify()

    private void drawButtons(Graphics g) {
        g.setColor(Color.black);
        // draw the pause 'button'
        if (isOverPauseButton) {
            g.setColor(Color.green);
        }
        g.drawOval(pauseArea.x, pauseArea.y, pauseArea.width, pauseArea.height);
        if (isPaused) {
            g.drawString("Paused", pauseArea.x, pauseArea.y + 10);
        } else {
            g.drawString("Pause", pauseArea.x + 5, pauseArea.y + 10);
        }
        if (isOverPauseButton) {
            g.setColor(Color.black);
        }
        // draw the quit 'button'
        if (isOverQuitButton) {
            g.setColor(Color.green);
        }
        g.drawOval(quitArea.x, quitArea.y, quitArea.width, quitArea.height);
        g.drawString("Quit", quitArea.x + 15, quitArea.y + 10);
        if (isOverQuitButton) {
            g.setColor(Color.black);
        }
    } // drawButtons()

    private void finishOff()
    /*
     * Tasks to do before terminating. Called at end of run() and via the shutdown hook in readyForTermination(). The call at the end of run() is not
     * really necessary, but included for safety. The flag stops the code being called twice.
     */
    {
        if (!finishedOff) {
            finishedOff = true;
            printStats();
            System.exit(0);
        }
    } // end of finishedOff()

    private void gameOverMessage(Graphics g)
    // center the game-over message in the panel
    {
        String msg = "Game Over. Your Score: " + score;
        int x = (pWidth - metrics.stringWidth(msg)) / 2;
        int y = (pHeight - metrics.getHeight()) / 2;
        g.setColor(Color.red);
        g.setFont(font);
        g.drawString(msg, x, y);
    } // end of gameOverMessage()

    private void gameRender() {
        if (dbImage == null) {
            dbImage = createImage(pWidth, pHeight);
            if (dbImage == null) {
                System.out.println("dbImage is null");
                return;
            } else {
                dbg = dbImage.getGraphics();
            }
        }
        // clear the background
        dbg.setColor(Color.white);
        dbg.fillRect(0, 0, pWidth, pHeight);
        dbg.setColor(Color.blue);
        dbg.setFont(font);
        // report frame count & average FPS and UPS at top left
        // dbg.drawString("Frame Count " + frameCount, 10, 25);
        dbg.drawString("Average FPS/UPS: " + df.format(averageFPS) + ", " + df.format(averageUPS), 20, 25);
        // report time used and bosex used at bottom left
        dbg.drawString("Time Spent: " + timeSpentInGame + " secs", 10, pHeight - 15);
        dbg.drawString("Boxes used: " + boxesUsed, 260, pHeight - 15);
        // draw the pause and quit 'buttons'
        drawButtons(dbg);
        dbg.setColor(Color.black);
        // draw game elements: the obstacles and the worm
        obs.draw(dbg);
        fred.draw(dbg);
        if (gameOver) {
            gameOverMessage(dbg);
        }
    } // end of gameRender()

    private void gameUpdate() {
        if (!isPaused && !gameOver) {
            fred.move();
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
        } catch (Exception e) // quite commonly seen at applet destruction
        {
            System.out.println("Graphics error: " + e);
        }
    } // end of paintScreen()

    private void printStats() {
        System.out.println("Frame Count/Loss: " + frameCount + " / " + totalFramesSkipped);
        System.out.println("Average FPS: " + df.format(averageFPS));
        System.out.println("Average UPS: " + df.format(averageUPS));
        System.out.println("Time Spent: " + timeSpentInGame + " secs");
        System.out.println("Boxes used: " + boxesUsed);
    } // end of printStats()

    private void readyForTermination() {
        addKeyListener(new KeyAdapter() {
            // listen for esc, q, end, ctrl-c on the canvas to
            // allow a convenient exit from the full screen configuration
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_ESCAPE || keyCode == KeyEvent.VK_Q || keyCode == KeyEvent.VK_END || keyCode == KeyEvent.VK_C
                        && e.isControlDown()) {
                    running = false;
                }
            }
        });
        // for shutdown tasks
        // a shutdown may not only come from the program
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                running = false;
                // System.out.println("Shutdown hook executed");
                finishOff();
            }
        });
    } // end of readyForTermination()

    public void run()
    /* The frames of the animation are drawn inside the while loop. */
    {
        long beforeTime, afterTime, timeDiff, sleepTime;
        long overSleepTime = 0L;
        int noDelays = 0;
        long excess = 0L;
        gameStartTime = J3DTimer.getValue();
        prevStatsTime = gameStartTime;
        beforeTime = gameStartTime;
        running = true;
        while (running) {
            gameUpdate();
            gameRender(); // render the game to a buffer
            paintScreen(); // draw the buffer on-screen
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
            framesSkipped += skips;
            // excess = excess % period;
            storeStats();
        }
        finishOff();
    } // end of run()

    public void setBoxNumber(int no)
    // called from Obstacles object
    {
        boxesUsed = no;
    }

    private void storeStats()
    /*
     * The statistics: - the summed periods for all the iterations in this interval (period is the amount of time a single frame iteration should
     * take), the actual elapsed time in this interval, the error between these two numbers; - the total frame count, which is the total number of
     * calls to run(); - the frames skipped in this interval, the total number of frames skipped. A frame skip is a game update without a
     * corresponding render; - the FPS (frames/sec) and UPS (updates/sec) for this interval, the average FPS & UPS over the last NUM_FPSs intervals.
     * The data is collected every MAX_STATS_INTERVAL (1 sec).
     */
    {
        frameCount++;
        statsInterval += period;
        if (statsInterval >= MAX_STATS_INTERVAL) { // record stats every
            // MAX_STATS_INTERVAL
            long timeNow = J3DTimer.getValue();
            timeSpentInGame = (int) ((timeNow - gameStartTime) / 1000000000L); // ns
            // -->
            // secs
            long realElapsedTime = timeNow - prevStatsTime; // time since last
            // stats collection
            totalElapsedTime += realElapsedTime;
            totalFramesSkipped += framesSkipped;
            double actualFPS = 0; // calculate the latest FPS and UPS
            double actualUPS = 0;
            if (totalElapsedTime > 0) {
                actualFPS = (double) frameCount / totalElapsedTime * 1000000000L;
                actualUPS = (double) (frameCount + totalFramesSkipped) / totalElapsedTime * 1000000000L;
            }
            // store the latest FPS and UPS
            fpsStore[(int) statsCount % NUM_FPS] = actualFPS;
            upsStore[(int) statsCount % NUM_FPS] = actualUPS;
            statsCount = statsCount + 1;
            double totalFPS = 0.0; // total the stored FPSs and UPSs
            double totalUPS = 0.0;
            for (int i = 0; i < NUM_FPS; i++) {
                totalFPS += fpsStore[i];
                totalUPS += upsStore[i];
            }
            if (statsCount < NUM_FPS) { // obtain the average FPS and UPS
                averageFPS = totalFPS / statsCount;
                averageUPS = totalUPS / statsCount;
            } else {
                averageFPS = totalFPS / NUM_FPS;
                averageUPS = totalUPS / NUM_FPS;
            }
            /*
             * System.out.println(timedf.format( (double) statsInterval/1000000000L) + " " + timedf.format((double) realElapsedTime/1000000000L) + "s " +
             * df.format(timingError) + "% " + frameCount + "c " + framesSkipped + "/" + totalFramesSkipped + " skip; " + df.format(actualFPS) + " " +
             * df.format(averageFPS) + " afps; " + df.format(actualUPS) + " " + df.format(averageUPS) + " aups" );
             */
            framesSkipped = 0;
            prevStatsTime = timeNow;
            statsInterval = 0L; // reset
        }
    } // end of storeStats()

    private void testMove(int x, int y)
    // is (x,y) over the pause or quit button?
    {
        if (running) { // stops problems with a rapid move after pressing
            // 'quit'
            isOverPauseButton = pauseArea.contains(x, y) ? true : false;
            isOverQuitButton = quitArea.contains(x, y) ? true : false;
        }
    }

    private void testPress(int x, int y)
    /*
     * Deal with pause and quit buttons. Also, is (x,y) near the head, or should an obstacle be added?
     */
    {
        if (isOverPauseButton) {
            isPaused = !isPaused; // toggle pausing
        } else if (isOverQuitButton) {
            running = false;
        } else {
            if (!isPaused && !gameOver) {
                if (fred.nearHead(x, y)) { // was mouse pressed near the head?
                    gameOver = true;
                    score = 40 - timeSpentInGame + 40 - boxesUsed;
                    // hack together a score
                } else { // add an obstacle if possible
                    if (!fred.touchedAt(x, y)) {
                        // untouched?
                        obs.add(x, y);
                    }
                }
            }
        }
    } // end of testPress()
} // end of WormPanel class
