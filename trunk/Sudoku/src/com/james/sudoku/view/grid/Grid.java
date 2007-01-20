package com.james.sudoku.view.grid;

import com.james.sudoku.controller.Controller;
import com.james.sudoku.model.ModelCell;
import com.james.sudoku.view.BaseDrawableImpl;
import javax.microedition.lcdui.Graphics;

public class Grid extends BaseDrawableImpl {
    
    private static final int LIGHT_GREY = 0x666666;
    
    private static final int MED_GREY = 0xaaaaaa;
    
    private static final int DARK_GREY = 0x333333;
    
    private static final int RED = 0xff0000;
    
    private static final int WHITE = 0xffffff;
    
    public static final int NUM_COLS = 9;
    
    public static final int NUM_ROWS = 9;
    
    private Controller controller;
    
    private GridCell cells[][];
    
    public Grid(int x, int y, int width, int height) {
        super(x, y, width, height);
        
        cells = new GridCell[NUM_ROWS][];
        
        for (int row = 0; row < NUM_ROWS; row++) {
            cells[row] = new GridCell[NUM_COLS];
            
            for (int col = 0; col < NUM_COLS; col++) {
                cells[row][col] = new GridCell(row, col, 10, 10, false,
                        new GridCellNumber(-1, -1, -1, -1));
            }
        }
    }
    
    public int getRow(int x) {
        int unitWidth = getWidth() / NUM_COLS;
        
        for (int i = 0; i < NUM_COLS; i++) {
            if (x < unitWidth * (i + 1)) {
                return i;
            }
        }
        // System.err.println("Failed to find row for xpos: " + x);
        return -1;
    }
    
    public int getCol(int y) {
        int unitHeight = getHeight() / NUM_ROWS;
        
        for (int i = 0; i < NUM_ROWS; i++) {
            if (y < unitHeight * (i + 1)) {
                return i;
            }
        }
        // System.err.println("Failed to find col for ypos: " + y);
        return -1;
    }
    
    private void drawGrid(Graphics g, Controller controller) {
        
        int unitWidth = getWidth() / NUM_COLS;
        int unitHeight = getHeight() / NUM_ROWS;
        
        // g.setColor(DARK_GREY);
        // g.fillRect(getX(), unitHeight * controller.getCurRow(), NUM_ROWS *
        // unitWidth, unitHeight);
        // g.setColor(DARK_GREY);
        // g.fillRect(unitWidth * controller.getCurCol(), getY(), unitWidth,
        // NUM_COLS* unitHeight);
        
        int selectedRow = controller.getCurRow();
        int selectedCol = controller.getCurCol();
        
        for (int row = 0; row < NUM_ROWS; row++) {
            
            if (row % 3 == 0 && row != 0) {
                g.setColor(WHITE);
            } else {
                g.setColor(LIGHT_GREY);
            }
            g.drawLine(getX(), row * unitHeight, NUM_ROWS * unitWidth, row
                    * unitHeight);
            g.setColor(WHITE);
            
            for (int col = 0; col < NUM_COLS; col++) {
                
                if (col % 3 == 0 && col != 0) {
                    g.setColor(WHITE);
                } else {
                    g.setColor(LIGHT_GREY);
                }
                g.drawLine(col * unitWidth, getY(), col * unitWidth, NUM_COLS
                        * unitHeight);
                g.setColor(WHITE);
                
                GridCell cell = cells[row][col];
                cell.setX(unitWidth * col);
                cell.setY(unitHeight * row);
                cell.setWidth(unitWidth);
                cell.setHeight(unitHeight);
                
                cell.setSelected(false);
                
                if (selectedRow == row) {
                    if (selectedCol == col) {
                        cell.setSelected(true);
                    }
                }
            }
        }
    }
    
    private void drawNumbers(Graphics g, Controller controller) {
        
        for (int row = 0; row < NUM_ROWS; row++) {
            
            for (int col = 0; col < NUM_COLS; col++) {
                
                ModelCell model = controller.get(row, col);
                
                GridCell cell = cells[row][col];
                
                cell.draw(g, model);
            }
        }
    }
    
    public void draw(Graphics g, Controller controller) {
        drawGrid(g, controller);
        drawNumbers(g, controller);
    }
    
    public Controller getController() {
        return controller;
    }
    
    public void setcontroller(Controller controller) {
        this.controller = controller;
    }
}