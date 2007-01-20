package com.james.sudoku.view;

public abstract class BaseDrawableImpl {
    
    private int height;
    
    private int width;
    
    private int xOrigin;
    
    private int yOrigin;
    
    public BaseDrawableImpl(int x, int y, int newWidth, int newHeight) {
        xOrigin = x;
        yOrigin = y;
        width = newWidth;
        height = newHeight;
    }
    
    public int getHeight() {
        return height;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getX() {
        return xOrigin;
    }
    
    public int getY() {
        return yOrigin;
    }
    
    public void setHeight(int height) {
        this.height = height;
    }
    
    public void setOrigin(int x, int y) {
        xOrigin = x;
        yOrigin = y;
    }
    
    public void setWidth(int width) {
        this.width = width;
    }
    
    public void setX(int x) {
        this.xOrigin = x;
    }
    
    public void setY(int y) {
        this.yOrigin = y;
    }
}
