package com.james.sudoku.util;

public class GridLoader {
    
    public GridLoader() {
    }
    
    public static String nextGame(int lastGameIndex) {
        int rand = (int) System.currentTimeMillis() << 5;
        rand = Math.abs(rand);
        
        int nextGameIndex = rand % GridData.grid.length;
        
        String rows =
                GridData.grid[nextGameIndex]+
                GridData.grid[nextGameIndex+1]+
                GridData.grid[nextGameIndex+2];
        
        return rows;
        //String gameGrid[] = split(rows);
        
        //return gameGrid;
    }
    
//    private static String[] split(String rows) {
//
//
//        for(int i=0;i < 81; i++){
//            rows.charAt(i);
//        }
//        rows[0].substring(0, 3),
//                rows[0].substring(3, 6),
//                rows[0].substring(6, 9),
//                rows[0].substring(9, 12),
//                rows[0].substring(12, 15),
//                rows[0].substring(15, 18),
//                rows[1].substring(0, 3),
//                rows[1].substring(3, 6),
//                rows[1].substring(6, 9),
//                rows[1].substring(9, 12),
//                rows[1].substring(12, 15),
//                rows[1].substring(15, 18),
//                rows[2].substring(0, 3),
//                rows[2].substring(3, 6),
//                rows[2].substring(6, 9),
//                rows[2].substring(9, 12),
//                rows[2].substring(12, 15),
//                rows[2].substring(15, 18)
    
    
//        return s;
//    }
}