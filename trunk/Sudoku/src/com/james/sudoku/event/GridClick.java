package com.james.sudoku.event;

public class GridClick implements ClickType {
    
    private final int col;
    
    private final int row;
    
    public GridClick(int newRow, int newCol) {
        this.row = newRow;
        this.col = newCol;
    }
    
    public int getCol() {
        return col;
    }
    
    public int getRow() {
        return row;
    }
}