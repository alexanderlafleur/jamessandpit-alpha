package Worm.WormFSEM;

// WormChase.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* A worm moves around the screen and the user must
 click (press) on its head to complete the game.

 If the user misses the worm's head then a blue box
 will be added to the screen (if the worm's body was
 not clicked upon).

 A worm cannot move over a box, so the added obstacles
 *may* make it easier to catch the worm.

 A worm starts at 0 length and increases to a maximum
 length which it keeps from then on.

 A score is displayed on screen at the end, calculated
 from the number of boxes used and the time taken. Less
 boxes and less time mean a higher score.

 -------------
 
 Uses full-screen exclusive mode, active rendering,
 and double buffering/page flipping.

 On-screen pause and quit buttons.

 Using Java 3D's timer: J3DTimer.getValue()
 *  nanosecs rather than millisecs for the period

 Average FPS / UPS
 20			50			80			100
 Win 98:         20/20       50/50       81/83       84/100
 Win 2000:       20/20       50/50       60/83       60/100
 Win XP (1):     20/20       50/50       74/83       76/100
 Win XP (2):     20/20       50/50       83/83       85/100

 Located in /WormFSEM

 Updated: 12th Feb 2004
 * added extra sleep to the end of our setDisplayMode();

 * moved createBufferStrategy() call to a separate
 setBufferStrategy() method, with added extras
 ----
 */

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferStrategy;
import java.text.DecimalFormat;

import javax.swing.JFrame;

import com.sun.j3d.utils.timer.J3DTimer;

public class WormChase extends JFrame implements Runnable {
    /**
     * 
     */
    private static final long serialVersionUID = 7773333380423469665L;

    private static final int NUM_BUFFERS = 2; // used for page flipping

    private static int DEFAULT_FPS = 100;

    private static long MAX_STATS_INTERVAL = 1000000000L;

    // record stats every 1 second (roughly)

    private static final int NO_DELAYS_PER_YIELD = 16;

    /*
     * Number of frames with a delay of 0 ms before the animation thread yields to other running threads.
     */

    private static int MAX_FRAME_SKIPS = 5; // was 2;

    // no. of frames that can be skipped in any one animation loop
    // i.e the games state is updated but not rendered

    private static int NUM_FPS = 10;

    // number of FPS values stored to get an average

    private int pWidth, pHeight; // panel dimensions

    // used for gathering statistics
    private long statsInterval = 0L; // in ns

    private long prevStatsTime;

    private long totalElapsedTime = 0L;

    private long gameStartTime;

    private int timeSpentInGame = 0; // in seconds

    private long frameCount = 0;

    private double fpsStore[];

    private long statsCount = 0;

    private double averageFPS = 0.0;

    private long framesSkipped = 0L;

    private long totalFramesSkipped = 0L;

    private double upsStore[];

    private double averageUPS = 0.0;

    private DecimalFormat df = new DecimalFormat("0.##"); // 2 dp

    private DecimalFormat timedf = new DecimalFormat("0.####"); // 4 dp

    private Thread animator; // the thread that performs the animation

    private volatile boolean running = false; // used to stop the animation
    // thread

    private long period; // period between drawing in _nanosecs_

    private Worm fred; // the worm

    private Obstacles obs; // the obstacles

    private int boxesUsed = 0;

    // used at game termination
    private volatile boolean gameOver = false;

    private int score = 0;

    private Font font;

    private FontMetrics metrics;

    private boolean finishedOff = false;

    // used by quit 'button'
    private volatile boolean isOverQuitButton = false;

    private Rectangle quitArea;

    // used by the pause 'button'
    private volatile boolean isOverPauseButton = false;

    private Rectangle pauseArea;

    private volatile boolean isPaused = false;

    // used for full-screen exclusive mode
    private GraphicsDevice gd;

    private Graphics gScr;

    private BufferStrategy bufferStrategy;

    public WormChase(long period) {
        super("Worm Chase");

        this.period = period;
        initFullScreen();

        readyForTermination();

        // create game components
        this.obs = new Obstacles(this);
        this.fred = new Worm(this.pWidth, this.pHeight, this.obs);

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
        this.font = new Font("SansSerif", Font.BOLD, 24);
        this.metrics = this.getFontMetrics(this.font);

        // specify screen areas for the buttons
        this.pauseArea = new Rectangle(this.pWidth - 100, this.pHeight - 45, 70, 15);
        this.quitArea = new Rectangle(this.pWidth - 100, this.pHeight - 20, 70, 15);

        // initialise timing elements
        this.fpsStore = new double[NUM_FPS];
        this.upsStore = new double[NUM_FPS];
        for (int i = 0; i < NUM_FPS; i++) {
            this.fpsStore[i] = 0.0;
            this.upsStore[i] = 0.0;
        }

        gameStart();
    } // end of WormChase()

    private void initFullScreen() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        this.gd = ge.getDefaultScreenDevice();

        setUndecorated(true); // no menu bar, borders, etc. or Swing
        // components
        setIgnoreRepaint(true); // turn off all paint events since doing active
        // rendering
        setResizable(false);

        if (!this.gd.isFullScreenSupported()) {
            System.out.println("Full-screen exclusive mode not supported");
            System.exit(0);
        }
        this.gd.setFullScreenWindow(this); // switch on full-screen exclusive mode

        // we can now adjust the display modes, if we wish
        showCurrentMode();

        // setDisplayMode(800, 600, 8); // or try 8 bits
        // setDisplayMode(1280, 1024, 32);

        // reportCapabilities();

        this.pWidth = getBounds().width;
        this.pHeight = getBounds().height;

        setBufferStrategy();
    } // end of initFullScreen()

    // private String getFlipText(BufferCapabilities.FlipContents flip) {
    // if (flip == null) {
    // return "false";
    // } else if (flip == BufferCapabilities.FlipContents.UNDEFINED) {
    // return "Undefined";
    // } else if (flip == BufferCapabilities.FlipContents.BACKGROUND) {
    // return "Background";
    // } else if (flip == BufferCapabilities.FlipContents.PRIOR) {
    // return "Prior";
    // } else {
    // // if (flip == BufferCapabilities.FlipContents.COPIED)
    // return "Copied";
    // }
    // } // end of getFlipTest()

    private void setBufferStrategy()
    /*
     * Switch on page flipping: NUM_BUFFERS == 2 so there will be a 'primary surface' and one 'back buffer'.
     * 
     * The use of invokeAndWait() is to avoid a possible deadlock with the event dispatcher thread. Should be fixed in J2SE 1.5
     * 
     * createBufferStrategy) is an asynchronous operation, so sleep a bit so that the getBufferStrategy() call will get the correct details.
     */
    {
        try {
            EventQueue.invokeAndWait(new Runnable() {
                public void run() {
                    createBufferStrategy(NUM_BUFFERS);
                }
            });
        } catch (Exception e) {
            System.out.println("Error while creating buffer strategy");
            System.exit(0);
        }

        try { // sleep to give time for the buffer strategy to be carried out
            Thread.sleep(500); // 0.5 sec
        } catch (InterruptedException ex) {
        }

        this.bufferStrategy = getBufferStrategy(); // store for later
    } // end of setBufferStrategy()

    private void readyForTermination() {
        addKeyListener(new KeyAdapter() {
            // listen for esc, q, end, ctrl-c on the canvas to
            // allow a convenient exit from the full screen configuration
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if ((keyCode == KeyEvent.VK_ESCAPE) || (keyCode == KeyEvent.VK_Q) || (keyCode == KeyEvent.VK_END) || ((keyCode == KeyEvent.VK_C) && e.isControlDown())) {
                    WormChase.this.running = false;
                }
            }
        });

        // for shutdown tasks
        // a shutdown may not only come from the program
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                WormChase.this.running = false;
                finishOff();
            }
        });
    } // end of readyForTermination()

    private void gameStart()
    // initialise and start the thread
    {
        if (this.animator == null || !this.running) {
            this.animator = new Thread(this);
            this.animator.start();
        }
    } // end of gameStart()

    private void testPress(int x, int y)
    /*
     * Deal with pause and quit buttons. Also, is (x,y) near the head, or should an obstacle be added?
     */
    {
        if (this.isOverPauseButton) {
            this.isPaused = !this.isPaused; // toggle pausing
        } else if (this.isOverQuitButton) {
            this.running = false;
        } else {
            if (!this.isPaused && !this.gameOver) {
                if (this.fred.nearHead(x, y)) { // was mouse pressed near the head?
                    this.gameOver = true;
                    this.score = (40 - this.timeSpentInGame) + (40 - this.boxesUsed);
                    // hack together a score
                } else { // add an obstacle if possible
                    if (!this.fred.touchedAt(x, y)) {
                        // untouched?
                        this.obs.add(x, y);
                    }
                }
            }
        }
    } // end of testPress()

    private void testMove(int x, int y)
    // is (x,y) over the pause or quit buttons?
    {
        if (this.running) { // stops problems with a rapid move after pressing
            // 'quit'
            this.isOverPauseButton = this.pauseArea.contains(x, y) ? true : false;
            this.isOverQuitButton = this.quitArea.contains(x, y) ? true : false;
        }
    }

    public void setBoxNumber(int no)
    // called from Obstacles object
    {
        this.boxesUsed = no;
    }

    public void run()
    /* The frames of the animation are drawn inside the while loop. */
    {
        long beforeTime, afterTime, timeDiff, sleepTime;
        long overSleepTime = 0L;
        int noDelays = 0;
        long excess = 0L;

        this.gameStartTime = J3DTimer.getValue();
        this.prevStatsTime = this.gameStartTime;
        beforeTime = this.gameStartTime;

        this.running = true;

        while (this.running) {
            gameUpdate();
            screenUpdate();

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
            this.framesSkipped += skips;

            storeStats();
        }
        finishOff();
    } // end of run()

    private void gameUpdate() {
        if (!this.isPaused && !this.gameOver) {
            this.fred.move();
        }
    } // end of gameUpdate()

    private void screenUpdate()
    // use active rendering
    {
        try {
            this.gScr = this.bufferStrategy.getDrawGraphics();
            gameRender(this.gScr);
            this.gScr.dispose();
            if (!this.bufferStrategy.contentsLost()) {
                this.bufferStrategy.show();
            } else {
                System.out.println("Contents Lost");
            }
            // Sync the display on some systems.
            // (on Linux, this fixes event queue problems)
            Toolkit.getDefaultToolkit().sync();
        } catch (Exception e) {
            e.printStackTrace();
            this.running = false;
        }
    } // end of screenUpdate()

    private void gameRender(Graphics gScr) {
        // clear the background
        gScr.setColor(Color.white);
        gScr.fillRect(0, 0, this.pWidth, this.pHeight);

        gScr.setColor(Color.blue);
        gScr.setFont(this.font);

        // report frame count & average FPS and UPS at top left
        // gScr.drawString("Frame Count " + frameCount, 10, 25);
        gScr.drawString("Average FPS/UPS: " + this.df.format(this.averageFPS) + ", " + this.df.format(this.averageUPS), 20, 25); // was (10,55)

        // report time used and boxes used at bottom left
        gScr.drawString("Time Spent: " + this.timeSpentInGame + " secs", 10, this.pHeight - 15);
        gScr.drawString("Boxes used: " + this.boxesUsed, 260, this.pHeight - 15);

        // draw the pause and quit 'buttons'
        drawButtons(gScr);

        gScr.setColor(Color.black);

        // draw game elements: the obstacles and the worm
        this.obs.draw(gScr);
        this.fred.draw(gScr);

        if (this.gameOver) {
            gameOverMessage(gScr);
        }
    } // end of gameRender()

    private void drawButtons(Graphics g) {
        g.setColor(Color.black);

        // draw the pause 'button'
        if (this.isOverPauseButton) {
            g.setColor(Color.green);
        }

        g.drawOval(this.pauseArea.x, this.pauseArea.y, this.pauseArea.width, this.pauseArea.height);
        if (this.isPaused) {
            g.drawString("Paused", this.pauseArea.x, this.pauseArea.y + 10);
        } else {
            g.drawString("Pause", this.pauseArea.x + 5, this.pauseArea.y + 10);
        }

        if (this.isOverPauseButton) {
            g.setColor(Color.black);
        }

        // draw the quit 'button'
        if (this.isOverQuitButton) {
            g.setColor(Color.green);
        }

        g.drawOval(this.quitArea.x, this.quitArea.y, this.quitArea.width, this.quitArea.height);
        g.drawString("Quit", this.quitArea.x + 15, this.quitArea.y + 10);

        if (this.isOverQuitButton) {
            g.setColor(Color.black);
        }
    } // drawButtons()

    private void gameOverMessage(Graphics g)
    // center the game-over message in the panel
    {
        String msg = "Game Over. Your Score: " + this.score;
        int x = (this.pWidth - this.metrics.stringWidth(msg)) / 2;
        int y = (this.pHeight - this.metrics.getHeight()) / 2;
        g.setColor(Color.red);
        g.setFont(this.font);
        g.drawString(msg, x, y);
    } // end of gameOverMessage()

    private void storeStats()
    /*
     * The statistics: - the summed periods for all the iterations in this interval (period is the amount of time a single frame iteration should take), the actual elapsed time in
     * this interval, the error between these two numbers; - the total frame count, which is the total number of calls to run(); - the frames skipped in this interval, the total
     * number of frames skipped. A frame skip is a game update without a corresponding render; - the FPS (frames/sec) and UPS (updates/sec) for this interval, the average FPS & UPS
     * over the last NUM_FPSs intervals.
     * 
     * The data is collected every MAX_STATS_INTERVAL (1 sec).
     */
    {
        this.frameCount++;
        this.statsInterval += this.period;

        if (this.statsInterval >= MAX_STATS_INTERVAL) { // record stats every
            // MAX_STATS_INTERVAL
            long timeNow = J3DTimer.getValue();
            this.timeSpentInGame = (int) ((timeNow - this.gameStartTime) / 1000000000L); // ns
            // -->
            // secs

            long realElapsedTime = timeNow - this.prevStatsTime; // time since last
            // stats collection
            this.totalElapsedTime += realElapsedTime;

            this.totalFramesSkipped += this.framesSkipped;

            double actualFPS = 0; // calculate the latest FPS and UPS
            double actualUPS = 0;
            if (this.totalElapsedTime > 0) {
                actualFPS = (((double) this.frameCount / this.totalElapsedTime) * 1000000000L);
                actualUPS = (((double) (this.frameCount + this.totalFramesSkipped) / this.totalElapsedTime) * 1000000000L);
            }

            // store the latest FPS and UPS
            this.fpsStore[(int) this.statsCount % NUM_FPS] = actualFPS;
            this.upsStore[(int) this.statsCount % NUM_FPS] = actualUPS;
            this.statsCount = this.statsCount + 1;

            double totalFPS = 0.0; // total the stored FPSs and UPSs
            double totalUPS = 0.0;
            for (int i = 0; i < NUM_FPS; i++) {
                totalFPS += this.fpsStore[i];
                totalUPS += this.upsStore[i];
            }

            if (this.statsCount < NUM_FPS) { // obtain the average FPS and UPS
                this.averageFPS = totalFPS / this.statsCount;
                this.averageUPS = totalUPS / this.statsCount;
            } else {
                this.averageFPS = totalFPS / NUM_FPS;
                this.averageUPS = totalUPS / NUM_FPS;
            }
            /*
             * System.out.println(timedf.format( (double) statsInterval/1000000000L) + " " + timedf.format((double) realElapsedTime/1000000000L) + "s " + df.format(timingError) + "% " +
             * frameCount + "c " + framesSkipped + "/" + totalFramesSkipped + " skip; " + df.format(actualFPS) + " " + df.format(averageFPS) + " afps; " + df.format(actualUPS) + " " +
             * df.format(averageUPS) + " aups" );
             */
            this.framesSkipped = 0;
            this.prevStatsTime = timeNow;
            this.statsInterval = 0L; // reset
        }
    } // end of storeStats()

    private void finishOff()
    /*
     * Tasks to do before terminating. Called at end of run() and via the shutdown hook in readyForTermination().
     * 
     * The call at the end of run() is not really necessary, but included for safety. The flag stops the code being called twice.
     */
    { // System.out.println("finishOff");
        if (!this.finishedOff) {
            this.finishedOff = true;
            printStats();
            restoreScreen();
            System.exit(0);
        }
    } // end of finishedOff()

    private void printStats() {
        System.out.println("Frame Count/Loss: " + this.frameCount + " / " + this.totalFramesSkipped);
        System.out.println("Average FPS: " + this.df.format(this.averageFPS));
        System.out.println("Average UPS: " + this.df.format(this.averageUPS));
        System.out.println("Time Spent: " + this.timeSpentInGame + " secs");
        System.out.println("Boxes used: " + this.boxesUsed);
    } // end of printStats()

    private void restoreScreen()
    /*
     * Switch off full screen mode. This also resets the display mode if it's been changed.
     */
    {
        Window w = this.gd.getFullScreenWindow();
        if (w != null) {
            w.dispose();
        }
        this.gd.setFullScreenWindow(null);
    } // end of restoreScreen()

    // ------------------ display mode methods -------------------

    // private boolean isDisplayModeAvailable(int width, int height, int bitDepth)
    // /*
    // * Check that a displayMode with this width, height, bit depth is available. We don't care about the refresh rate,
    // * which is probably REFRESH_RATE_UNKNOWN anyway.
    // */
    // {
    // DisplayMode[] modes = this.gd.getDisplayModes();
    // showModes(modes);
    //
    // for (DisplayMode element : modes) {
    // if (width == element.getWidth() && height == element.getHeight() && bitDepth == element.getBitDepth()) {
    // return true;
    // }
    // }
    // return false;
    // } // end of isDisplayModeAvailable()

    // private void showModes(DisplayMode[] modes)
    // // pretty print the display mode information in modes
    // {
    // System.out.println("Modes");
    // for (int i = 0; i < modes.length; i++) {
    // System.out.print("(" + modes[i].getWidth() + "," + modes[i].getHeight() + "," + modes[i].getBitDepth() + "," + modes[i].getRefreshRate() + ") ");
    // if ((i + 1) % 4 == 0) {
    // System.out.println();
    // }
    // }
    // System.out.println();
    // } // end of showModes()

    private void showCurrentMode()
    // print the display mode details for the graphics device
    {
        DisplayMode dm = this.gd.getDisplayMode();
        System.out.println("Current Display Mode: (" + dm.getWidth() + "," + dm.getHeight() + "," + dm.getBitDepth() + "," + dm.getRefreshRate() + ")  ");
    }

    // -----------------------------------------

    public static void main(String args[]) {
        int fps = DEFAULT_FPS;
        if (args.length != 0) {
            fps = Integer.parseInt(args[0]);
        }

        long period = (long) 1000.0 / fps;
        System.out.println("fps: " + fps + "; period: " + period + " ms");
        new WormChase(period * 1000000L); // ms --> nanosecs
    } // end of main()

} // end of WormChase class

