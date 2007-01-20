package com.james.sudoku.test;

import com.james.sudoku.util.GridLoader;

public class TestGridLoader {
    
    public TestGridLoader() {
    }
    
    public void testFirstGame() {
        GridLoader g = new GridLoader();
        
        String game= g.nextGame(0);
        
        System.out.println(game);
    }
    
    public static void main(String args[]) {
        TestGridLoader loader = new TestGridLoader();
        
        loader.testFirstGame();
        
    }
}
