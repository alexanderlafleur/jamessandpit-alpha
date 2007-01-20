package com.james.sudoku.util;

import com.james.sudoku.controller.Controller;
import com.james.sudoku.model.StateModel;
import com.james.sudoku.model.impl.StateModelImpl;
import com.james.sudoku.controller.ControllerImpl;
import com.james.sudoku.persistence.DataStore;

public class StateGenerator {
    private final static int height = 9;
    
    private final static int width = 9;
    
    private static final StateGenerator instance = new StateGenerator();
    private DataStore dataStore = new DataStore();
    
    public static StateGenerator getInstance() {
        return instance;
    }
    
    public Controller generateNewStateModel() {
        
        StateModel model = loadModel();
        
        Controller sontroller = new ControllerImpl(model, getWidth(), getHeight());
        return sontroller;
    }
    
    private String games[] = {
        "0,0,0,7,0,0,0,0,0\n" + "9,0,0,0,0,0,1,0,0\n"
                + "8,4,2,6,0,0,0,0,0\n" + "0,3,0,0,6,0,5,2,0\n"
                + "0,2,8,0,3,0,6,7,0\n" + "0,6,7,0,5,0,0,4,0\n"
                + "0,0,0,0,0,9,2,1,3\n" + "0,0,1,0,0,0,0,0,5\n"
                + "0,0,0,0,0,3,0,0,0",
        
        "4,0,2,0,0,0,0,8,3\n" + "0,0,0,0,2,0,1,0,0\n"
                + "0,6,1,0,4,0,0,0,0\n" + "3,0,0,0,0,9,0,1,5\n"
                + "2,0,5,4,0,3,8,0,9\n" + "6,8,0,5,0,0,0,0,2\n"
                + "0,0,0,0,3,0,9,5,0\n" + "0,0,3,0,7,0,0,0,0\n"
                + "8,9,0,0,0,0,7,0,6" };
    
    private int lastGameIndex = 0;
    
    private StateModel loadModel() {
        StateModel model = getDataStore().load();
        
        if (model == null) {
            model = new StateModelImpl(getWidth(), getHeight());
        }
        
        //    getNextGame(model);
        
        return model;
    }
    
    private void getNextGame(StateModel model) {
        String gameRow = GridLoader.nextGame(getLastGameIndex());
        
        for (int i = 0; i < 81; i++) {
            int val = Character.digit(gameRow.charAt(i),10);
            
            int x = i % 9;
            int y = i / 9;
            
            System.out.println(i+": ("+x+","+y+")= "+val);
            
            
            model.set(y, x, new int[]{val}, true);
        }
        
        // int index = (++lastGameIndex) % games.length;
        // String game = games[index];
        //
        // splitInt(model, game,"\n");
    }
    
    public void splitInt(StateModel model, String game, String delimiter) {
        
        int lastIndex = 0;
        int index = 0;
        
        for (int i = 0; i < 9; i++) {
            index = game.indexOf(delimiter, lastIndex);
            
            if (index == -1) {
                index = game.length();
            }
            String row = game.substring(lastIndex, index);
            
            int split[] = split(row, ",");
            
            setRow(model, i, split);
            lastIndex = index + 1;
        }
    }
    
    public int[] split(String row, String delimiter) {
        int[] split = new int[9];
        
        int lastIndex = 0;
        int index = 0;
        
        for (int i = 0; i < 9; i++) {
            index = row.indexOf(delimiter, lastIndex);
            
            if (index == -1) {
                index = row.length();
            }
            
            String s = row.substring(lastIndex, index);
            
            int j = Integer.parseInt(s);
            
            split[i] = j;
            lastIndex = index + 1;
        }
        
        return split;
    }
    
    private StateModel setRow(StateModel model, int row, int cols[]) {
        for (int c = 0; c < cols.length; c++) {
            int values[];
            boolean fixed;
            
            if (cols[c] == 0) {
                values = new int[] {};
                fixed = false;
            } else {
                values = new int[] { cols[c] };
                fixed = true;
            }
            
            model.set(row, c, values, fixed);
        }
        
        return model;
    }
    
    private StateGenerator() {
    }
    
    public static int getHeight() {
        return height;
    }
    
    public static int getWidth() {
        return width;
    }
    
    public DataStore getDataStore() {
        return dataStore;
    }
    
    public void setDataStore(DataStore dataStore) {
        this.dataStore = dataStore;
    }
    
    public String[] getGames() {
        return games;
    }
    
    public void setGames(String[] games) {
        this.games = games;
    }
    
    public int getLastGameIndex() {
        return lastGameIndex;
    }
    
    public void setLastGameIndex(int lastGameIndex) {
        this.lastGameIndex = lastGameIndex;
    }
}