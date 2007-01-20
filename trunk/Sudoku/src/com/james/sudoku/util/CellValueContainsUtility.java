package com.james.sudoku.util;

import java.util.Vector;

public class CellValueContainsUtility {
    
    public static final boolean contains(Vector vector, int value) {
        boolean contains = false;
        
        for (int i = 0, size = vector.size(); i < size; i++) {
            Integer iVal = ((Integer) vector.elementAt(i));
            
            if (iVal.intValue() == value) {
                contains = true;
                break;
            }
        }
        
        return contains;
    }
    
    public static final boolean contains(Vector vector, Integer value) {
        return contains(vector, value.intValue());
    }
}
