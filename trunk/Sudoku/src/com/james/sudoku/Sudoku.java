package com.james.sudoku;

import com.james.sudoku.event.EventHandler;
import com.james.sudoku.event.impl.EventHandlerImpl;
import com.james.sudoku.controller.Controller;
import com.james.sudoku.persistence.DataStore;
import com.james.sudoku.view.Renderer;
import com.james.sudoku.util.StateGenerator;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;

public class Sudoku extends MIDlet implements CommandListener {
    
    private Display display;
    
    private Command newCommand;
    
    private Command saveCommand;
    
    private Command loadCommand;
    
    private Command exitCommand;
    
    private DataStore dataStore = new DataStore();
    
    private boolean started;
    
    public void commandAction(Command c, Displayable d) {
        if (c == getExitCommand()) {
            destroyApp(true);
            notifyDestroyed();
            
        } else if (c == getNewCommand()) {
            restartApp();
            
        } else if (c == getSaveCommand()) {
            save();
            
        } else if (c == getLoadCommand()) {
            load();
            
        }
    }
    
    private Controller controller;
    
    private Canvas createCanvas() {
        
        setController(StateGenerator.getInstance().generateNewStateModel());
        
        Renderer renderer = new Renderer();
        
        EventHandler eventHandler = new EventHandlerImpl(getController(), renderer);
        
        MainCanvas canvas = new MainCanvas(eventHandler, getController(), renderer);
        
        if (canvas.hasPointerEvents()) {
            System.out.println("Canvas has pointer events");
        } else {
            System.out.println("Canvas doesn't have pointer events");
        }
        
        if (canvas.hasPointerMotionEvents()) {
            System.out.println("Canvas has pointer motion events");
        } else {
            System.out.println("Canvas doesn't have pointer motion events");
        }
        
        getController().setStateChangeListener(canvas);
        
        setExitCommand(new Command("Exit", Command.EXIT, 0));
        setNewCommand(new Command("New", Command.SCREEN, 1));
        setSaveCommand(new Command("Save", Command.OK, 2));
        setLoadCommand(new Command("Load", Command.OK, 3));
        
        canvas.addCommand(getExitCommand());
        canvas.addCommand(getNewCommand());
        canvas.addCommand(getSaveCommand());
        canvas.addCommand(getLoadCommand());
        
        canvas.setCommandListener(this);
        
        // canvas.setEventHandler(canvas);
        
        
        return canvas;
    }
    
    protected void destroyApp(boolean unconditional) {
    }
    
    protected void pauseApp() {
    }
    
    protected void restartApp() {
        if (isStarted()) {
            setDisplay(Display.getDisplay(this));
            
            Canvas canvas = createCanvas();
            
            getDisplay().setCurrent(canvas);
        }
    }
    
    protected void startApp() {
        if (!isStarted()) {
            setDisplay(Display.getDisplay(this));
            
            Canvas canvas = createCanvas();
            
            getDisplay().setCurrent(canvas);
            
            setStarted(true);
        }
    }
    
    private void save() {
        getDataStore().save(getController().getCurModel());
    }
    
    private void load() {
        getController().setCurModel(getDataStore().load());
        getController().validate();
        getController().getStateChangeListener().notifyStateChanged();
    }
    
    public Display getDisplay() {
        return display;
    }
    
    public void setDisplay(Display display) {
        this.display = display;
    }
    
    public Command getNewCommand() {
        return newCommand;
    }
    
    public void setNewCommand(Command newCommand) {
        this.newCommand = newCommand;
    }
    
    public Command getSaveCommand() {
        return saveCommand;
    }
    
    public void setSaveCommand(Command saveCommand) {
        this.saveCommand = saveCommand;
    }
    
    public Command getExitCommand() {
        return exitCommand;
    }
    
    public void setExitCommand(Command exitCommand) {
        this.exitCommand = exitCommand;
    }
    
    public DataStore getDataStore() {
        return dataStore;
    }
    
    public void setDataStore(DataStore dataStore) {
        this.dataStore = dataStore;
    }
    
    public boolean isStarted() {
        return started;
    }
    
    public void setStarted(boolean started) {
        this.started = started;
    }
    
    public Command getLoadCommand() {
        return loadCommand;
    }
    
    public void setLoadCommand(Command loadCommand) {
        this.loadCommand = loadCommand;
    }
    
    public Controller getController() {
        return controller;
    }
    
    public void setController(Controller controller) {
        this.controller = controller;
    }
}