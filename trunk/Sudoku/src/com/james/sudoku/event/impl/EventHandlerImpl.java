package com.james.sudoku.event.impl;

import com.james.sudoku.event.EventHandler;
import com.james.sudoku.controller.Controller;
import com.james.sudoku.event.ClickType;
import com.james.sudoku.view.Renderer;
import javax.microedition.lcdui.Canvas;

public class EventHandlerImpl implements EventHandler {
    private static int[] gameActions = { Canvas.UP, Canvas.DOWN, Canvas.LEFT,
    Canvas.RIGHT, Canvas.FIRE, Canvas.GAME_A, Canvas.GAME_B,
    Canvas.GAME_C, Canvas.GAME_D };
    
    private static String[] gameNames = { "UP", "DOWN", "LEFT", "RIGHT",
    "FIRE", "GAME_A", "GAME_B", "GAME_C", "GAME_D" };
    
    private static int[] keyCodes = { Canvas.KEY_NUM0, Canvas.KEY_NUM1,
    Canvas.KEY_NUM2, Canvas.KEY_NUM3, Canvas.KEY_NUM4, Canvas.KEY_NUM5,
    Canvas.KEY_NUM6, Canvas.KEY_NUM7, Canvas.KEY_NUM8, Canvas.KEY_NUM9,
    Canvas.KEY_POUND, Canvas.KEY_STAR };
    
    private static String[] keyNames = { "KEY_NUM0", "KEY_NUM1", "KEY_NUM2",
    "KEY_NUM3", "KEY_NUM4", "KEY_NUM5", "KEY_NUM6", "KEY_NUM7",
    "KEY_NUM8", "KEY_NUM9", "KEY_POUND", "KEY_STAR" };
    
    private int lastKeyCode = 0;
    
    private int lastX;
    
    private int lastY;
    
    private boolean pointer;
    
    private Controller controller;
    
    private Renderer renderer;
    
    public EventHandlerImpl(Controller controller, Renderer renderer) {
        setController(controller);
        setRenderer(renderer);
    }
    
    private void changeController(int keyCode) {
        switch (keyCode) {
            case Canvas.KEY_NUM0:
                getController().inputNumber(0, false);
                break;
                
            case Canvas.KEY_NUM1:
                getController().inputNumber(1, false);
                break;
                
            case Canvas.KEY_NUM2:
                getController().inputNumber(2, false);
                break;
                
            case Canvas.KEY_NUM3:
                getController().inputNumber(3, false);
                break;
                
            case Canvas.KEY_NUM4:
                getController().inputNumber(4, false);
                break;
                
            case Canvas.KEY_NUM5:
                getController().inputNumber(5, false);
                break;
                
            case Canvas.KEY_NUM6:
                getController().inputNumber(6, false);
                break;
                
            case Canvas.KEY_NUM7:
                getController().inputNumber(7, false);
                break;
                
            case Canvas.KEY_NUM8:
                getController().inputNumber(8, false);
                break;
                
            case Canvas.KEY_NUM9:
                getController().inputNumber(9, false);
                break;
                
            case Canvas.UP:
            case -1:
                getController().moveUp();
                break;
                
            case Canvas.DOWN:
            case -2:
                getController().moveDown();
                break;
                
            case Canvas.LEFT:
            case -3:
                getController().moveLeft();
                break;
                
            case Canvas.RIGHT:
            case -4:
                getController().moveRight();
                break;
                
            case Canvas.FIRE:
                getController().select();
                break;
                
            case Canvas.KEY_STAR:
                getController().toggleFix();
                break;
                
            case Canvas.KEY_POUND:
            case Canvas.GAME_A:
            case Canvas.GAME_B:
            case Canvas.GAME_C:
            case Canvas.GAME_D:
            default:
                System.out.println("Unimplemented keycode " + keyCode);
        }
    }
    
    public Controller getController() {
        return controller;
    }
    
    public void keyReleased(int keyCode) {
        // getRenderer().setStatusText("Keycode" + keyCode);
        
        changeController(keyCode);
    }
    
    public ClickType pointerPressed(int x, int y, int width, int height) {
        // getRenderer().setStatusText("Pointer" + x + "," + y);
        
        ClickType type = getRenderer().getClickType(x, y);
        Controller controller = getController();
        
        if (type instanceof GridType) {
            GridType gType = (GridType) type;
            
            controller.select(gType.getCol(), gType.getRow());
            
        } else if (type instanceof PencilType) {
            
            PencilType pt = (PencilType) type;
            
            controller.inputNumber(pt.getI(), false);
            
        } else if (type instanceof FixedType) {
            FixedType ft = (FixedType) type;
            
            controller.inputNumber(ft.getI(), true);
        }
        
        return type;
    }
    
    public void setController(Controller controller) {
        this.controller = controller;
    }
    
    public static int[] getGameActions() {
        return gameActions;
    }
    
    public static void setGameActions(int[] aGameActions) {
        gameActions = aGameActions;
    }
    
    public static String[] getGameNames() {
        return gameNames;
    }
    
    public static void setGameNames(String[] aGameNames) {
        gameNames = aGameNames;
    }
    
    public static int[] getKeyCodes() {
        return keyCodes;
    }
    
    public static void setKeyCodes(int[] aKeyCodes) {
        keyCodes = aKeyCodes;
    }
    
    public static String[] getKeyNames() {
        return keyNames;
    }
    
    public static void setKeyNames(String[] aKeyNames) {
        keyNames = aKeyNames;
    }
    
    public int getLastKeyCode() {
        return lastKeyCode;
    }
    
    public void setLastKeyCode(int lastKeyCode) {
        this.lastKeyCode = lastKeyCode;
    }
    
    public int getLastX() {
        return lastX;
    }
    
    public void setLastX(int lastX) {
        this.lastX = lastX;
    }
    
    public int getLastY() {
        return lastY;
    }
    
    public void setLastY(int lastY) {
        this.lastY = lastY;
    }
    
    public boolean isPointer() {
        return pointer;
    }
    
    public void setPointer(boolean pointer) {
        this.pointer = pointer;
    }
    
    public Renderer getRenderer() {
        return renderer;
    }
    
    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }
}
