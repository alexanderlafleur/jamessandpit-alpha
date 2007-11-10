package FourByFour;

// Board.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* @(#)Board.java 1.8 01/01/11 07:32:09
 * Copyright (c) 1996-2001 Sun Microsystems, Inc. All Rights Reserved.
 */
/*  Board stores the players turns and detects when one of 
 them has won.

 The game is a 4*4*4 version of tic-tac-toe, where the players 
 must fill in a line with their 'marker' (a sphere or square).

 A line is a series of 4 positions in the X, Y, or Z directions, 
 or along a diagonal.

 -----------
 Changes:
 - removed all of game playing logic for the computer's turn
 - removed rendering into 2D canvas
 */

public class Board {
    private final static int NUM_SPOTS = 64; // in the game board (4*4*4)

    private final static int WIN_WAYS = 76;

    /*
     * The number of ways of winning the game. A win is a completed row, column, or diagonal by one player, which is called a 'line' here.
     */

    private final static int UNOCCUPIED = 0;

    private final static int PLAYER1 = 1;

    private final static int PLAYER2 = 2; // used to be MACHINE

    private int player;

    // data structures to represent the current state of play
    private int[][] winLines; // was called combinations

    private int[][] posToLines;

    // variables to calculate the final score
    private int nmoves;

    private long startTime;

    private boolean gameOver;

    private FourByFour fbf;

    private Positions positions;

    public Board(Positions posns, FourByFour fbf) {
        this.positions = posns;
        this.fbf = fbf; // so can send messages to GUI

        initGameData();

        this.player = PLAYER1; // set the player with the first move
        this.nmoves = 0; // initialize the number of moves
        this.startTime = System.currentTimeMillis(); // record the start time
        this.gameOver = false;
    } // end of Board()

    private void initGameData() {
        // Allocate space to the game data structures
        this.winLines = new int[WIN_WAYS][6];
        this.posToLines = new int[NUM_SPOTS][8];

        /*
         * The winLines[] array contains all the possible winning lines. Each winLines[x] row has information about winning line no. x. It has the format:
         * 
         * winLines[x][0] = line status = 0 : no player has selected positions in this line = -1 : both players have selected positions in this line = 1 to 4 : number of positions
         * occupied by player
         * 
         * winLines[x][1] = player who owns this line (valid only if status = 1-4)
         * 
         * winLines[x][2] = board position that defines the line x winLines[x][3] = board position that defines the line x winLines[x][4] = board position that defines the line x
         * winLines[x][5] = board position that defines the line x note: a line is made up of 4 positions
         */

        // Initialize winLines[] flags
        for (int i = 0; i < WIN_WAYS; i++) {
            this.winLines[i][0] = 0;
            this.winLines[i][1] = UNOCCUPIED; // no player owns line i
        }

        // x-axis lines (16 lines)
        this.winLines[0][2] = 0;
        this.winLines[1][2] = 4;
        this.winLines[2][2] = 8;
        this.winLines[3][2] = 12;
        this.winLines[0][3] = 1;
        this.winLines[1][3] = 5;
        this.winLines[2][3] = 9;
        this.winLines[3][3] = 13;
        this.winLines[0][4] = 2;
        this.winLines[1][4] = 6;
        this.winLines[2][4] = 10;
        this.winLines[3][4] = 14;
        this.winLines[0][5] = 3;
        this.winLines[1][5] = 7;
        this.winLines[2][5] = 11;
        this.winLines[3][5] = 15;

        this.winLines[4][2] = 16;
        this.winLines[5][2] = 20;
        this.winLines[6][2] = 24;
        this.winLines[7][2] = 28;
        this.winLines[4][3] = 17;
        this.winLines[5][3] = 21;
        this.winLines[6][3] = 25;
        this.winLines[7][3] = 29;
        this.winLines[4][4] = 18;
        this.winLines[5][4] = 22;
        this.winLines[6][4] = 26;
        this.winLines[7][4] = 30;
        this.winLines[4][5] = 19;
        this.winLines[5][5] = 23;
        this.winLines[6][5] = 27;
        this.winLines[7][5] = 31;

        this.winLines[8][2] = 32;
        this.winLines[9][2] = 36;
        this.winLines[10][2] = 40;
        this.winLines[11][2] = 44;
        this.winLines[8][3] = 33;
        this.winLines[9][3] = 37;
        this.winLines[10][3] = 41;
        this.winLines[11][3] = 45;
        this.winLines[8][4] = 34;
        this.winLines[9][4] = 38;
        this.winLines[10][4] = 42;
        this.winLines[11][4] = 46;
        this.winLines[8][5] = 35;
        this.winLines[9][5] = 39;
        this.winLines[10][5] = 43;
        this.winLines[11][5] = 47;

        this.winLines[12][2] = 48;
        this.winLines[13][2] = 52;
        this.winLines[14][2] = 56;
        this.winLines[15][2] = 60;
        this.winLines[12][3] = 49;
        this.winLines[13][3] = 53;
        this.winLines[14][3] = 57;
        this.winLines[15][3] = 61;
        this.winLines[12][4] = 50;
        this.winLines[13][4] = 54;
        this.winLines[14][4] = 58;
        this.winLines[15][4] = 62;
        this.winLines[12][5] = 51;
        this.winLines[13][5] = 55;
        this.winLines[14][5] = 59;
        this.winLines[15][5] = 63;

        // y-axis lines (16 lines)
        this.winLines[16][2] = 0;
        this.winLines[17][2] = 1;
        this.winLines[18][2] = 2;
        this.winLines[19][2] = 3;
        this.winLines[16][3] = 4;
        this.winLines[17][3] = 5;
        this.winLines[18][3] = 6;
        this.winLines[19][3] = 7;
        this.winLines[16][4] = 8;
        this.winLines[17][4] = 9;
        this.winLines[18][4] = 10;
        this.winLines[19][4] = 11;
        this.winLines[16][5] = 12;
        this.winLines[17][5] = 13;
        this.winLines[18][5] = 14;
        this.winLines[19][5] = 15;

        this.winLines[20][2] = 16;
        this.winLines[21][2] = 17;
        this.winLines[22][2] = 18;
        this.winLines[23][2] = 19;
        this.winLines[20][3] = 20;
        this.winLines[21][3] = 21;
        this.winLines[22][3] = 22;
        this.winLines[23][3] = 23;
        this.winLines[20][4] = 24;
        this.winLines[21][4] = 25;
        this.winLines[22][4] = 26;
        this.winLines[23][4] = 27;
        this.winLines[20][5] = 28;
        this.winLines[21][5] = 29;
        this.winLines[22][5] = 30;
        this.winLines[23][5] = 31;

        this.winLines[24][2] = 32;
        this.winLines[25][2] = 33;
        this.winLines[26][2] = 34;
        this.winLines[27][2] = 35;
        this.winLines[24][3] = 36;
        this.winLines[25][3] = 37;
        this.winLines[26][3] = 38;
        this.winLines[27][3] = 39;
        this.winLines[24][4] = 40;
        this.winLines[25][4] = 41;
        this.winLines[26][4] = 42;
        this.winLines[27][4] = 43;
        this.winLines[24][5] = 44;
        this.winLines[25][5] = 45;
        this.winLines[26][5] = 46;
        this.winLines[27][5] = 47;

        this.winLines[28][2] = 48;
        this.winLines[29][2] = 49;
        this.winLines[30][2] = 50;
        this.winLines[31][2] = 51;
        this.winLines[28][3] = 52;
        this.winLines[29][3] = 53;
        this.winLines[30][3] = 54;
        this.winLines[31][3] = 55;
        this.winLines[28][4] = 56;
        this.winLines[29][4] = 57;
        this.winLines[30][4] = 58;
        this.winLines[31][4] = 59;
        this.winLines[28][5] = 60;
        this.winLines[29][5] = 61;
        this.winLines[30][5] = 62;
        this.winLines[31][5] = 63;

        // XY plane diagonals (8 lines)
        this.winLines[32][2] = 0;
        this.winLines[33][2] = 16;
        this.winLines[34][2] = 32;
        this.winLines[35][2] = 48;
        this.winLines[32][3] = 5;
        this.winLines[33][3] = 21;
        this.winLines[34][3] = 37;
        this.winLines[35][3] = 53;
        this.winLines[32][4] = 10;
        this.winLines[33][4] = 26;
        this.winLines[34][4] = 42;
        this.winLines[35][4] = 58;
        this.winLines[32][5] = 15;
        this.winLines[33][5] = 31;
        this.winLines[34][5] = 47;
        this.winLines[35][5] = 63;

        this.winLines[36][2] = 3;
        this.winLines[37][2] = 19;
        this.winLines[38][2] = 35;
        this.winLines[39][2] = 51;
        this.winLines[36][3] = 6;
        this.winLines[37][3] = 22;
        this.winLines[38][3] = 38;
        this.winLines[39][3] = 54;
        this.winLines[36][4] = 9;
        this.winLines[37][4] = 25;
        this.winLines[38][4] = 41;
        this.winLines[39][4] = 57;
        this.winLines[36][5] = 12;
        this.winLines[37][5] = 28;
        this.winLines[38][5] = 44;
        this.winLines[39][5] = 60;

        // z-axis lines (16 lines)
        this.winLines[40][2] = 51;
        this.winLines[41][2] = 55;
        this.winLines[42][2] = 59;
        this.winLines[43][2] = 63;
        this.winLines[40][3] = 35;
        this.winLines[41][3] = 39;
        this.winLines[42][3] = 43;
        this.winLines[43][3] = 47;
        this.winLines[40][4] = 19;
        this.winLines[41][4] = 23;
        this.winLines[42][4] = 27;
        this.winLines[43][4] = 31;
        this.winLines[40][5] = 3;
        this.winLines[41][5] = 7;
        this.winLines[42][5] = 11;
        this.winLines[43][5] = 15;

        this.winLines[44][2] = 50;
        this.winLines[45][2] = 54;
        this.winLines[46][2] = 58;
        this.winLines[47][2] = 62;
        this.winLines[44][3] = 34;
        this.winLines[45][3] = 38;
        this.winLines[46][3] = 42;
        this.winLines[47][3] = 46;
        this.winLines[44][4] = 18;
        this.winLines[45][4] = 22;
        this.winLines[46][4] = 26;
        this.winLines[47][4] = 30;
        this.winLines[44][5] = 2;
        this.winLines[45][5] = 6;
        this.winLines[46][5] = 10;
        this.winLines[47][5] = 14;

        this.winLines[48][2] = 49;
        this.winLines[49][2] = 53;
        this.winLines[50][2] = 57;
        this.winLines[51][2] = 61;
        this.winLines[48][3] = 33;
        this.winLines[49][3] = 37;
        this.winLines[50][3] = 41;
        this.winLines[51][3] = 45;
        this.winLines[48][4] = 17;
        this.winLines[49][4] = 21;
        this.winLines[50][4] = 25;
        this.winLines[51][4] = 29;
        this.winLines[48][5] = 1;
        this.winLines[49][5] = 5;
        this.winLines[50][5] = 9;
        this.winLines[51][5] = 13;

        this.winLines[52][2] = 48;
        this.winLines[53][2] = 52;
        this.winLines[54][2] = 56;
        this.winLines[55][2] = 60;
        this.winLines[52][3] = 32;
        this.winLines[53][3] = 36;
        this.winLines[54][3] = 40;
        this.winLines[55][3] = 44;
        this.winLines[52][4] = 16;
        this.winLines[53][4] = 20;
        this.winLines[54][4] = 24;
        this.winLines[55][4] = 28;
        this.winLines[52][5] = 0;
        this.winLines[53][5] = 4;
        this.winLines[54][5] = 8;
        this.winLines[55][5] = 12;

        // YZ plane diagonals (8 lines)
        this.winLines[56][2] = 51;
        this.winLines[57][2] = 50;
        this.winLines[58][2] = 49;
        this.winLines[59][2] = 48;
        this.winLines[56][3] = 39;
        this.winLines[57][3] = 38;
        this.winLines[58][3] = 37;
        this.winLines[59][3] = 36;
        this.winLines[56][4] = 27;
        this.winLines[57][4] = 26;
        this.winLines[58][4] = 25;
        this.winLines[59][4] = 24;
        this.winLines[56][5] = 15;
        this.winLines[57][5] = 14;
        this.winLines[58][5] = 13;
        this.winLines[59][5] = 12;

        this.winLines[60][2] = 3;
        this.winLines[61][2] = 2;
        this.winLines[62][2] = 1;
        this.winLines[63][2] = 0;
        this.winLines[60][3] = 23;
        this.winLines[61][3] = 22;
        this.winLines[62][3] = 21;
        this.winLines[63][3] = 20;
        this.winLines[60][4] = 43;
        this.winLines[61][4] = 42;
        this.winLines[62][4] = 41;
        this.winLines[63][4] = 40;
        this.winLines[60][5] = 63;
        this.winLines[61][5] = 62;
        this.winLines[62][5] = 61;
        this.winLines[63][5] = 60;

        // XZ plane diagonals (8 lines)
        this.winLines[64][2] = 63;
        this.winLines[65][2] = 59;
        this.winLines[66][2] = 55;
        this.winLines[67][2] = 51;
        this.winLines[64][3] = 46;
        this.winLines[65][3] = 42;
        this.winLines[66][3] = 38;
        this.winLines[67][3] = 34;
        this.winLines[64][4] = 29;
        this.winLines[65][4] = 25;
        this.winLines[66][4] = 21;
        this.winLines[67][4] = 17;
        this.winLines[64][5] = 12;
        this.winLines[65][5] = 8;
        this.winLines[66][5] = 4;
        this.winLines[67][5] = 0;

        this.winLines[68][2] = 15;
        this.winLines[69][2] = 11;
        this.winLines[70][2] = 7;
        this.winLines[71][2] = 3;
        this.winLines[68][3] = 30;
        this.winLines[69][3] = 26;
        this.winLines[70][3] = 22;
        this.winLines[71][3] = 18;
        this.winLines[68][4] = 45;
        this.winLines[69][4] = 41;
        this.winLines[70][4] = 37;
        this.winLines[71][4] = 33;
        this.winLines[68][5] = 60;
        this.winLines[69][5] = 56;
        this.winLines[70][5] = 52;
        this.winLines[71][5] = 48;

        // Corner to Corner
        this.winLines[72][2] = 0;
        this.winLines[73][2] = 3;
        this.winLines[74][2] = 12;
        this.winLines[75][2] = 15;
        this.winLines[72][3] = 21;
        this.winLines[73][3] = 22;
        this.winLines[74][3] = 25;
        this.winLines[75][3] = 26;
        this.winLines[72][4] = 42;
        this.winLines[73][4] = 41;
        this.winLines[74][4] = 38;
        this.winLines[75][4] = 37;
        this.winLines[72][5] = 63;
        this.winLines[73][5] = 60;
        this.winLines[74][5] = 51;
        this.winLines[75][5] = 48;

        setPosToLines();
    } // end of initGameData()

    private void setPosToLines()
    /*
     * Set up the posToLines[] array to point to every winning line that a given position may be involved in.
     * 
     * posToLines[i] is information about position i - posToLines[i][0] is the number of lines using position i - posToLines[i][1..8] holds the line indexes (max of 7)
     */
    {
        int count;
        for (int i = 0; i < NUM_SPOTS; i++) {
            count = 1;
            this.posToLines[i][0] = 0;
            for (int j = 0; j < WIN_WAYS; j++) {
                for (int k = 2; k < 6; k++) {
                    if (this.winLines[j][k] == i) {
                        this.posToLines[i][0]++; // increment total no. of lines for
                        // i
                        this.posToLines[i][count++] = j; // pos i is used in line j
                    }
                }
            }
        }
    } // end of setPosToLines()

    public void tryPosn(int pos)
    /*
     * The current player has selected position pos for their move. Called by PickDragBehaviour.
     */
    {
        if (this.gameOver) {
            return;
        }

        this.positions.set(pos, this.player); // change the 3D marker shown at pos
        playMove(pos); // play the move on the board

        // switch players, if the game isn't over
        if (!this.gameOver) {
            this.player = ((this.player == PLAYER1) ? PLAYER2 : PLAYER1);
            if (this.player == PLAYER1) {
                this.fbf.showMessage("Player 1's turn (red spheres)");
            } else {
                this.fbf.showMessage("Player 2's turn (blue cubes)");
            }
        }
    } // end of tryPosn()

    private void playMove(int pos)
    /*
     * Apply the player's move to the board, and see if it is a winner.
     */
    {
        this.nmoves++; // update the number of moves

        // get number of lines that this position is involved in
        int numWinLines = this.posToLines[pos][0];

        /*
         * Go through each line associated with this position and update its status. If we have a winner, stop the game.
         */

        int line;
        for (int j = 0; j < numWinLines; j++) {
            line = this.posToLines[pos][j + 1];
            if (this.winLines[line][1] != this.player && this.winLines[line][1] != UNOCCUPIED) {
                this.winLines[line][0] = -1;
            } else {
                this.winLines[line][1] = this.player; // this line belongs to this player
                this.winLines[line][0]++; // one more position used in line
                if (this.winLines[line][0] == 4) { // all positions used,
                    this.gameOver = true; // so this player has won
                    reportWinner();
                }
            }
        }
    } // end of playMove()

    private void reportWinner() {
        long end_time = System.currentTimeMillis();
        long time = (end_time - this.startTime) / 1000;

        int score = (NUM_SPOTS + 2 - this.nmoves) * 111 - (int) Math.min(time * 1000, 5000);

        if (this.player == PLAYER1) {
            this.fbf.showMessage("Game over, player 1 wins with score " + score);
        } else {
            // PLAYER2
            this.fbf.showMessage("Game over, player 2 wins with score " + score);
        }
    } // end of reportWinner()

} // end of Board class
