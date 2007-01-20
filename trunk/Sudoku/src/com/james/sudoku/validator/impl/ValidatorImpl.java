package com.james.sudoku.validator.impl;

import com.james.sudoku.model.ModelCell;
import com.james.sudoku.model.StateModel;
import com.james.sudoku.validator.Validator;

public class ValidatorImpl implements Validator {
    
    public void validate(StateModel model) {
        model.setValid(true);
        
        check(model);
    }
    
    private void checkRows(StateModel model) {
        
        for (int r = 0, numRows = model.getNumRows(); r < numRows; r++) {
            
            ModelCell row[] = model.getRow(r);
            
            boolean b[] = new boolean[9];
            
            for (int c = 0; c < row.length; c++) {
                int values[] = model.getValues(r, c);
                
                if (values.length == 1) {
                    int value = values[0] - 1;
                    
                    if (value < 0 || value > 8) {
                        break;
                    }
                    
                    if (b[value]) {
                        model.setValid(r, c, false);
                        
                    }
                    b[value] = true;
                }
            }
        }
    }
    
    private void checkCols(StateModel model) {
        for (int c = 0, numCols = model.getNumCols(); c < numCols; c++) {
            
            ModelCell col[] = model.getCol(c);
            
            boolean b[] = new boolean[9];
            
            for (int r = 0; r < col.length; r++) {
                int values[] = model.getValues(r, c);
                
                if (values.length == 1) {
                    if (b[values[0] - 1]) {
                        model.setValid(r, c, false);
                        
                    }
                    b[values[0] - 1] = true;
                }
            }
        }
    }
    
    private void checkSquares(StateModel model) {
        
        for (int x = 0; x < 3; x++) {
            
            for (int y = 0; y < 3; y++) {
                boolean b[] = new boolean[9];
                
                for (int c = 0; c < 3; c++) {
                    
                    for (int r = 0; r < 3; r++) {
                        
                        int row = 3 * x + r;
                        int col = 3 * y + c;
                        
                        // System.out.println("x: "+x+" y: "+y+" c: "+c+" r:
                        // "+r+" row: "+row+" col: "+col);
                        
                        int values[] = model.getValues(row, col);
                        
                        if (values.length == 1) {
                            if (b[values[0] - 1]) {
                                model.setValid(row, col, false);
                            }
                            
                            b[values[0] - 1] = true;
                        }
                    }
                }
            }
        }
        
        // for (int i = 0; i < 9; i++) {
        //
        // boolean b[] = new boolean[9];
        //
        // for (int c = 0; c < 3; c++) {
        //
        // for (int r = 0; r < 3; r++) {
        //
        // int row = (3 * i + 3 + r) % 9;
        // int col = (i / 3) + c;
        //
        // System.out.println("i: "+i+" c: "+c+" r: "+r+" row: "+row+" col:
        // "+col);
        //
        // int values[] = model.getValues(row, col);
        //
        // if (values.length == 1) {
        // if (b[values[0] - 1]) {
        // model.setValid(row, col, false);
        // }
        //
        // b[values[0] - 1] = true;
        // }
        // }
        // }
        // System.out.println("Square "+i);
        // }
    }
    
    private void check(StateModel model) {
        checkRows(model);
        checkCols(model);
        checkSquares(model);
    }
}
