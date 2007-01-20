package com.james.sudoku.event;

import com.james.sudoku.controller.Controller;

public interface EventHandler {
    Controller getController();
    
    void keyReleased(int keyCode);
    
    // void keyPressed(int keyCode);
    //
    // void keyRepeated(int keyCode);
    //
    // void pointerDragged(int x, int y);
    
    ClickType pointerPressed(int x, int y, int width, int height);
    
    // void pointerReleased(int x, int y);
    
    void setController(Controller controller);
    
}
