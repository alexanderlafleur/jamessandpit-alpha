package Maze3D.MazeGen;

// MazeGen.java
// Nawapoom Lohajarernvanich, CoE Student ID: 4110181
// Revised by Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Create an ASCII file holding a 2D maze where walls are 
 represented by 'b' (blocks) and 'c' (cylinders). 
 The player starts at the 's' position,
 and there is one entrance/exit to the maze at the top of
 the maze, in the center.

 The maze sides are an odd length (e.g. 15, 17) because
 there is a border of blanks along the top, bottom, left, and right.

 The path through the maze is started at an even location 
 (e.g. (2,2)), and the algorithm only looks at even locations to 
 extend the path.
 The path cannot extend outside 2 to size-3 so that there
 is an outer wall to the maze.

 The maze is saved to MAZE_FNM, or a user-supplied file.

 Unlike MazeMaker there are no rooms.
 */

import java.awt.Point;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class MazeGen {
    // default input parameters
    private static final int WIDTH = 41; // must be odd

    private static final int HEIGHT = 41;

    private static final String MAZE_FNM = "maze.txt";

    // directions for the maze corridors
    private static final int UP = 0;

    private static final int DOWN = 1;

    private static final int LEFT = 2;

    private static final int RIGHT = 3;

    private static final int NUM_DIRS = 4;

    // percentage of blocks to change to cylinders
    private final static double CYLINDER_PERCENT = 0.2; // 20%

    private char maze[][];

    private int width, height;

    private String mazeFNm;

    private Point startCell; // where the player starts

    public MazeGen(String[] args) {
        processArgs(args);
        this.maze = new char[this.height][this.width];
        fillGrid();
        cutPath();
        makeCylinders();
        makeEntrance();
    } // end of MazeGen()

    private void processArgs(String[] args) {
        if (args.length == 0) {
            this.width = WIDTH;
            this.height = HEIGHT;
            this.mazeFNm = MAZE_FNM;
        } else if (args.length == 2) {
            setMazeSize(args[0], args[1]);
            this.mazeFNm = MAZE_FNM;
        } else if (args.length == 3) {
            setMazeSize(args[0], args[1]);
            this.mazeFNm = args[2];
        } else {
            System.out.println("Usage: java MazeGen <width> <height> <fnm>");
            System.exit(0);
        }
        System.out.println("Width: " + this.width + "; Height: " + this.height + " (2 added for borders)");
        System.out.println("Saving maze to file: " + this.mazeFNm);
    } // end of processArgs()

    private void setMazeSize(String wStr, String hStr) {
        try {
            this.width = Integer.parseInt(wStr) + 2; // for borders
            this.height = Integer.parseInt(hStr) + 2;
            if (this.width % 2 != 1) {
                System.out.println("Width must be odd; adding 1");
                this.width++;
            }
            if (this.height % 2 != 1) {
                System.out.println("Height must be odd; adding 1");
                this.height++;
            }
        } catch (NumberFormatException ex) {
            System.out.println("Incorrect format for size data");
            this.width = WIDTH;
            this.height = HEIGHT;
        }
    } // end of setMazeSize()

    private void fillGrid()
    /*
     * The initial grid has a border of blanks, the rest filled with 'b's. The algorithm removes b's.
     */
    { // fill the maze with 'b's
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                this.maze[i][j] = 'b';
            }
        }

        // fill top and bottom borders with blanks
        for (int j = 0; j < this.width; j++) {
            this.maze[0][j] = ' ';
            this.maze[this.height - 1][j] = ' ';
        }

        // fill left and right borders with blanks
        for (int i = 0; i < this.height; i++) {
            this.maze[i][0] = ' ';
            this.maze[i][this.width - 1] = ' ';
        }
    } // end of fillGrid()

    private void cutPath()
    /*
     * Select a starting cell at an even position. Each move after this is in steps of two which ensures that a path is created rather than a big block of space.
     */
    { // initialize the starting cell with an even (x,y)
        int x = randomCoord(this.height);
        int y = randomCoord(this.width);
        System.out.println("Starting point: (" + x + ", " + y + ")");

        this.startCell = new Point(x, y);
        this.maze[x][y] = ' ';
        createPath(this.startCell); // start making the path
        this.maze[x][y] = 's'; // label (x,y) as a starting point
    } // end of cutPath()

    private int randomCoord(int max)
    // Return an even int between 2 and max-3, to avoid
    // blank border and wall positions. max is odd.
    {
        int range = max - 4; // which is an odd number
        int randRange = (int) Math.floor((Math.random() * range) + 2);
        if (randRange % 2 == 1) {
            return randRange + 1;
        }
        return randRange;
    } // send of randomCoord()

    private void createPath(Point cell)
    /*
     * From the current cell, randomly try to visit all the other cells two steps away. Use depth-first traversal.
     * 
     * A cell is not visited more than once, and so not every direction will be opened up with a path.
     */
    {
        RandomInRange rr = new RandomInRange(NUM_DIRS);
        int dir;
        while ((dir = rr.getNumber()) != -1) {
            Point nextCell = nextPos(cell, dir);
            // System.out.println("nextCell: " + nextCell.toString());
            if (!beenVisited(nextCell)) {
                visit(nextCell, dir);
                createPath(nextCell); // recursive creation
            }
        }
    } // end of createPath()

    private Point nextPos(Point cell, int dir)
    /*
     * Return the next cell position. Do not check the limits of the maze. The next position is always two steps away.
     */
    {
        int newX = cell.x;
        int newY = cell.y;
        if (dir == UP) {
            newY = newY - 2;
        } else if (dir == DOWN) {
            newY = newY + 2;
        } else if (dir == LEFT) {
            newX = newX - 2;
        } else if (dir == RIGHT) {
            newX = newX + 2;
        } else {
            System.out.println("Do not recognise direction");
        }

        if (inBounds(newX, newY)) {
            return new Point(newX, newY);
        } else {
            return cell; // no change
        }
    } // end of nextPos()

    private boolean inBounds(int x, int y) {
        if ((x >= 2) && (x <= this.height - 2) && // exclude borders
                (y >= 2) && (y <= this.width - 2)) {
            return true;
        }
        return false;
    } // end of inBounds()

    private boolean beenVisited(Point cell)
    // has cell already been visited?
    {
        if (this.maze[cell.x][cell.y] == ' ') {
            return true;
        } else {
            return false;
        }
    } // end of beenVisited()

    private void visit(Point cell, int dir)
    /*
     * Blank out the current cell, and the one between the current cell and previous cell. This makes a path between the previous and current cells.
     */
    {
        this.maze[cell.x][cell.y] = ' ';
        if (dir == UP) {
            this.maze[cell.x][cell.y + 1] = ' ';
        } else if (dir == DOWN) {
            this.maze[cell.x][cell.y - 1] = ' ';
        } else if (dir == LEFT) {
            this.maze[cell.x + 1][cell.y] = ' ';
        } else if (dir == RIGHT) {
            this.maze[cell.x - 1][cell.y] = ' ';
        } else {
            System.out.println("Do not recognise cell direction");
        }
    } // end of visit()

    public void printMaze() {
        for (int i = 0; i < this.height; i++) {
            System.out.println(this.maze[i]);
        }
    } // end of PrintMaze()

    public void printMazeToFile() {
        File f = new File(this.mazeFNm);
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter(f), true);
            for (int i = 0; i < this.height; i++) {
                pw.println(new String(this.maze[i]));
            }
            System.out.println("Maze written to file " + this.mazeFNm);
        } catch (Exception e) {
            System.out.println("Could not write maze to file " + this.mazeFNm);
        }

    } // end of printMazeToFile()

    private void makeCylinders()
    // convert roughly CYLINDER_PERCENT blocks to cylinders
    {
        for (int x = 0; x < this.height; x++) {
            for (int y = 0; y < this.width; y++) {
                if ((this.maze[x][y] == 'b') && (Math.random() <= CYLINDER_PERCENT)) {
                    this.maze[x][y] = 'c';
                }
            }
        }
    } // end of makeCylinders

    private void makeEntrance()
    /*
     * Try to make an entrance along the top row of the maze, near the center. This may fail if the maze is very small.
     */
    {
        for (int y = this.width / 2; y < this.width - 3; y++) { // height is odd
            if (this.maze[2][y] == ' ') { // blank inside maze
                this.maze[1][y] = ' '; // make entrance
                return;
            }
        }
        System.out.println("Oops! No entrance made");
    } // end of makeEntrance()

    // ---------------------- main() -----------------------

    public static void main(String args[]) {
        MazeGen mg = new MazeGen(args);
        mg.printMaze();
        mg.printMazeToFile();
    } // end of main()

} // end of MazeGen class
