package com.james.sudoku;

import com.james.sudoku.controller.Controller;
import com.james.sudoku.event.EventHandler;
import com.james.sudoku.event.ClickType;
import com.james.sudoku.view.Renderer;
import com.james.sudoku.view.StateChangeListener;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

public class MainCanvas extends Canvas implements StateChangeListener // ,
// EventHandler
{
    private EventHandler eventHandler;
    
    private Renderer renderer;
    
    private Controller controller;
    
    public MainCanvas(EventHandler eventHandler, Controller controller, Renderer renderer) {
        setEventHandler(eventHandler);
        setController(controller);
        setRenderer(renderer);
    }
    
    public EventHandler getEventHandler() {
        return eventHandler;
    }
    
    public Renderer getRenderer() {
        return renderer;
    }
    
    public Controller getController() {
        return controller;
    }
    
    private String msg = new String();
    
    protected void keyPressed(int keyCode) {
        // System.out.println("keyPressed");
        // repaint();
    }
    
    protected void keyRepeated(int keyCode) {
        // System.out.println("keyRepeated");
        // repaint();
    }
    
    protected void keyReleased(int keyCode) {
        // System.out.println("keyRepeated");
        getEventHandler().keyReleased(keyCode);
        // repaint();
    }
    
        /*
         * protected void pointerPressed(int x, int y) { msg = "pointerPressed";
         *
         * System.out.println("pointerPressed"); repaint(); }
         *
         * protected void pointerDragged(int x, int y) { msg = "pointerDragged";
         *
         * System.out.println("pointerDragged"); repaint(); }
         */
    
    protected void pointerReleased(int x, int y) {
        // msg = "pointerReleased";
        
        // System.out.println("pointerReleased");
        ClickType type = getEventHandler().pointerPressed(x, y, getWidth(),
                getHeight());
        
        // if (type instanceof GridType) {
        // GridType gType = (GridType) type;
        //
        // msg += gType.getRow()+" "+gType.getCol();
        //
        // } else if (type instanceof PencilType) {
        // PencilType pType = (PencilType) type;
        //
        // msg += pType.getI();
        //
        // } else if (type instanceof FixedType) {
        // FixedType fType = (FixedType) type;
        //
        // msg += fType.getI();
        //
        // }
        
        // repaint();
    }
    
    //
    // public void keyPressed(int keyCode) {
    // // System.out.println("Key pressed "+keyCode);
    // // getEventHandler().keyReleased(keyCode);
    // }
    //
    // public void keyReleased(int keyCode) {
    // // System.out.println("Key released "+keyCode);
    // getEventHandler().keyReleased(keyCode);
    // }
    //
    // public void keyRepeated(int keyCode) {
    // // System.out.println("Key repeated");
    // }
    
    public void notifyStateChanged() {
        // System.out.println("State changed");
        repaint();
    }
    
    protected void paint(Graphics g) {
        getRenderer().render(g, getController(), getWidth(), getHeight());
        
        g.setColor(0xffffff);
        // g.drawString(msg, 0, 0, Graphics.LEFT | Graphics.TOP);
    }
    
    protected void pointerDragged(int x, int y) {
        // System.out.println("pointer dragged");
        pointerReleased(x, y);
    }
    
    // protected void pointerPressed(int x, int y) {
    // System.out.println("pressed " + x + ", " + y);
    //
    // getRenderer().setStatusText("Pnt " + x + ", " + y);
    // getEventHandler().pointerPressed(x, y, getWidth(), getHeight());
    //
    // }
    
    public void setEventHandler(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }
    
    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }
    
    public void setController(Controller controller) {
        this.controller = controller;
    }
    
    // public void pointerPressed(int x, int y, int width, int height) {
    // System.out.println("pointer released " + x + ", " + y + " " + width + " "
    // + height);
    //
    // getRenderer().setStatusText("Pnt " + x + ", " + y);
    // getEventHandler().pointerPressed(x, y, getWidth(), getHeight());
    // }
}