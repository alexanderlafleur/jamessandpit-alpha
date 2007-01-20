package com.james.sudoku.view;

import com.james.sudoku.controller.Controller;
import com.james.sudoku.event.ClickType;
import com.james.sudoku.event.impl.FixedType;
import com.james.sudoku.event.impl.GridType;
import com.james.sudoku.event.impl.PencilType;
import com.james.sudoku.view.grid.Grid;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public class Renderer {
    
    private static final int DARK_GREY = 0xaaaaaa;
    
    private static final int LIGHT_GREY = 0x333333;
    
    private static final int RED = 0xff0000;
    
    private static final int WHITE = 0xffffff;
    
    private Button fixedButtons[];
    
    private Grid grid;
    
    private Button pencilButtons[];
    
    private Button controlButton;
    
    private String statusText;
    
    public static Font SMALL_FONT = Font.getFont(Font.FONT_STATIC_TEXT,
            Font.STYLE_PLAIN, Font.SIZE_SMALL);
    
    public static Font LARGE_FONT = Font.getFont(Font.FONT_STATIC_TEXT,
            Font.STYLE_BOLD, Font.SIZE_LARGE);
    
    public static Font LARGE_BOLD_FONT = Font.getFont(Font.FONT_STATIC_TEXT,
            Font.STYLE_BOLD, Font.SIZE_LARGE);
    
    public Renderer() {
        setGrid(new Grid(0, 0, 0, 0));
        
        setPencilButtons(new Button[Grid.NUM_ROWS]);
        for (int i = 0; i < getPencilButtons().length; i++) {
            getPencilButtons()[i] = new Button(0, 0, 0, 0, String
                    .valueOf(i + 1));
        }
        
        setFixedButtons(new Button[Grid.NUM_COLS]);
        for (int i = 0; i < getFixedButtons().length; i++) {
            getFixedButtons()[i] = new Button(0, 0, 0, 0, String.valueOf(i + 1));
        }
        
        setControlButton(new Button(0,0,0,0,"*"));
    }
    
    public final ClickType getClickType(int x, int y) {
        ClickType type;
        
        if (x < getGrid().getWidth() && y < getGrid().getHeight()) {
            int row = getGrid().getRow(x);
            int col = getGrid().getCol(y);
            
            type = new GridType(row, col);
            
        } else {
            type = null;
            
            for (int i = 0; i < getFixedButtons().length; i++) {
                if (getFixedButtons()[i].contains(x, y)) {
                    type = new FixedType(i + 1);
                    break;
                }
            }
            if (type == null) {
                for (int i = 0; i < getPencilButtons().length; i++) {
                    if (getPencilButtons()[i].contains(x, y)) {
                        type = new PencilType(i + 1);
                        break;
                    }
                }
            }
        }
        
        return type;
    }
    
    private void drawStatus(Graphics g, int width, int height) {
        g.setColor(WHITE);
        
        g.drawString(getStatusText(), 20, 20, Graphics.TOP | Graphics.LEFT);
    }
    
    public final void render(final Graphics g, final Controller controller, int width,
            int height) {
        
        g.setFont(SMALL_FONT);
        
        g.setColor(0);
        g.fillRect(0, 0, width, height);
        
        g.setColor(0xffffff);
        
        // drawStatus(g, width, height);
        
        getGrid().setOrigin(0, 0);
        getGrid().setWidth(width - width / Grid.NUM_ROWS);
        getGrid().setHeight(height - height / Grid.NUM_COLS);
        
        getGrid().draw(g, controller);
        
        int buttonWidth = (width - width / Grid.NUM_ROWS) / Grid.NUM_ROWS;
        int buttonHeight = (height - height / Grid.NUM_COLS) / Grid.NUM_COLS;
        
        for (int i = 0; i < getPencilButtons().length; i++) {
            Button button = getPencilButtons()[i];
            
            button.setWidth(buttonWidth);
            button.setHeight(buttonHeight);
            
            button.setOrigin(i * button.getWidth(), getGrid().getHeight());
            
            button.draw(g, controller);
        }
        
        for (int i = 0; i < getFixedButtons().length; i++) {
            Button button = getFixedButtons()[i];
            
            button.setWidth(buttonWidth);
            button.setHeight(buttonHeight);
            
            button.setOrigin(getGrid().getWidth(), button.getHeight() * i);
            
            button.draw(g, controller);
        }
        
        getControlButton().setWidth(buttonWidth);
        getControlButton().setWidth(buttonWidth);
        getControlButton().setOrigin(getGrid().getWidth(), getGrid().getHeight());
        getControlButton().draw(g, controller);
    }
    
    public static int getDARK_GREY() {
        return DARK_GREY;
    }
    
    public static int getLIGHT_GREY() {
        return LIGHT_GREY;
    }
    
    public static int getRED() {
        return RED;
    }
    
    public static int getWHITE() {
        return WHITE;
    }
    
    public Button[] getFixedButtons() {
        return fixedButtons;
    }
    
    public void setFixedButtons(Button[] fixedButtons) {
        this.fixedButtons = fixedButtons;
    }
    
    public Grid getGrid() {
        return grid;
    }
    
    public void setGrid(Grid grid) {
        this.grid = grid;
    }
    
    public Button[] getPencilButtons() {
        return pencilButtons;
    }
    
    public void setPencilButtons(Button[] pencilButtons) {
        this.pencilButtons = pencilButtons;
    }
    
    public String getStatusText() {
        if (statusText == null) {
            statusText = "Unset";
        }
        
        return statusText;
    }
    
    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }
    
    public Button getControlButton() {
        return controlButton;
    }
    
    public void setControlButton(Button controlButton) {
        this.controlButton = controlButton;
    }
}