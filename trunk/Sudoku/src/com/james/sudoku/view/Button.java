package com.james.sudoku.view;

import com.james.sudoku.controller.Controller;
import com.james.sudoku.view.grid.GridCellNumber;
import javax.microedition.lcdui.Graphics;

public class Button extends BaseDrawableImpl implements Drawable {
    
    private String text;
    
    private static final int LIGHT_GREY = 0x555555;
    
    public Button(int x, int y, int width, int height, String text) {
        super(x, y, width, height);
        setText(text);
    }
    
    public void draw(Graphics g, Controller controller) {
        g.setFont(Renderer.LARGE_FONT);
        
        g.setColor(LIGHT_GREY);
        g.drawRect(getX(), getY(), getWidth(), getHeight());
        
        int fontWidth = g.getFont().charWidth('2');
        int fontHeight = g.getFont().getHeight();
        
        int x = getX() + getWidth() / 2 - fontWidth / 2;
        int y = getY();
        
        // g.setColor(GridCellNumber.getColour(Integer.parseInt(getText())));
        g.drawString(getText(), x, y, Graphics.TOP | Graphics.LEFT);
        
    }
    
    public boolean contains(int x1, int y1) {
        return x1 >= getX() && y1 >= getY() && x1 <= getX() + getWidth()
        && y1 <= getY() + getHeight();
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
}