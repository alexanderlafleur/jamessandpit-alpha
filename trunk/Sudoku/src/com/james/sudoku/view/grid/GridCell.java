package com.james.sudoku.view.grid;

import com.james.sudoku.model.ModelCell;
import com.james.sudoku.view.BaseDrawableImpl;
import javax.microedition.lcdui.Graphics;

public class GridCell extends BaseDrawableImpl {
    
    private int LIGHT_RED = 0x550000;
    
    private int MED_GREY = 0x555555;
    
    private int DARK_GREY = 0x222222;
    
    private int RED = 0xff0000;
    
    private int GREEN = 0x00ff00;
    
    private boolean fixed;
    
    private int x;
    
    private int y;
    
    private boolean selected;
    
    private GridCellNumber number;
    
    public void draw(Graphics g, ModelCell cellModel) {
        
        if (!cellModel.isValid()) {
            g.setColor(LIGHT_RED);
            g.fillRect(getX() + 1, getY() + 1, getWidth() - 2, getHeight() - 2);
            
        } else if (cellModel.isFixed()) {
            g.setColor(MED_GREY);
            g.fillRect(getX() + 1, getY() + 1, getWidth() - 1, getHeight() - 1);
            
        } else {
            // g.drawRect(getX(), getY(), getWidth(), getHeight());
        }
        
        if (isSelected()) {
            g.setColor(GREEN);
            g.drawRect(getX(), getY(), getWidth(), getHeight());
        }
        
        getNumber().setX(getX());
        getNumber().setY(getY());
        getNumber().setWidth(getWidth());
        getNumber().setHeight(getHeight());
        
        getNumber().draw(g, cellModel, cellModel.isFixed());
    }
    
    public GridCell(int x, int y, int width, int height, boolean selected,
            GridCellNumber number) {
        super(x, y, width, height);
        this.setNumber(number);
        this.setSelected(selected);
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public boolean isFixed() {
        return fixed;
    }
    
    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    public GridCellNumber getNumber() {
        return number;
    }
    
    public void setNumber(GridCellNumber number) {
        this.number = number;
    }
    
    public int getLIGHT_RED() {
        return LIGHT_RED;
    }
    
    public void setLIGHT_RED(int LIGHT_RED) {
        this.LIGHT_RED = LIGHT_RED;
    }
    
    public int getDARK_GREY() {
        return DARK_GREY;
    }
    
    public void setDARK_GREY(int DARK_GREY) {
        this.DARK_GREY = DARK_GREY;
    }
    
    public boolean isSelected() {
        return selected;
    }
    
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
