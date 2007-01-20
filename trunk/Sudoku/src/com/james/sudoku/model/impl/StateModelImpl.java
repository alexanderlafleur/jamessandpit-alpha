package com.james.sudoku.model.impl;

import com.james.sudoku.model.ModelCell;
import com.james.sudoku.model.StateModel;
import com.james.sudoku.persistence.StateModelSerialisationParser;
import java.util.Enumeration;
import java.util.Vector;

public class StateModelImpl implements StateModel {
    private int curRow;
    
    private int curCol;
    
    private final ModelCell data[][];
    
    private int numRows;
    
    private int numCols;
    
    private boolean fix = true;
    
    public ModelCell[] getRow(int row) {
        return getData()[row];
    }
    
    public int[] getValues(int row, int col) {
        
        ModelCell cell = get(row, col);
        
        Vector values = cell.getValues();
        
        int intValues[] = new int[values.size()];
        
        int i = 0;
        for (Enumeration e = values.elements(); e.hasMoreElements(); i++) {
            Integer iValue = (Integer) e.nextElement();
            intValues[i] = iValue.intValue();
        }
        
        return intValues;
    }
    
    public ModelCell[] getCol(int col) {
        return new ModelCell[] { getData()[0][col], getData()[1][col], getData()[2][col],
        getData()[3][col], getData()[4][col], getData()[5][col], getData()[6][col],
        getData()[7][col], getData()[8][col] };
    }
    
    public StateModelImpl(int width, int height) {
        this.setNumRows(width);
        this.setNumCols(height);
        
        data = new ModelCell[width][];
        int curRow = 0;
        int curCol = 0;
        
        for (int row = 0; row < height; row++) {
            getData()[row] = new ModelCell[height];
            
            for (int col = 0; col < width; col++) {
                getData()[row][col] = new ModelCell(new Vector(), false, true);
            }
        }
    }
    
    public void addNumber(int value) {
        get().addValue(value);
    }
    
    public ModelCell get() {
        return getData()[getCurRow()][getCurCol()];
    }
    
    public ModelCell get(int row, int col) {
        return getData()[row][col];
    }
    
    public int getCurRow() {
        return curRow;
    }
    
    public int getCurCol() {
        return curCol;
    }
    
    public void set(int row, int col, Vector values, boolean fixed) {
        int[] intValues = new int[values.size()];
        
        int i = 0;
        for (Enumeration e = values.elements(); e.hasMoreElements(); i++){
            Integer I = (Integer) e.nextElement();
            
            intValues[i] = I.intValue();
        }
        
        set(row, col, intValues, fixed);
    }
    
    public void set(int row, int col, int[] values, boolean fixed) {
        ModelCell c = get(row, col);
        c.setValues(values);
        c.setFixed(fixed);
    }
    
    public void set(int[] values) {
        get().setValues(values);
    }
    
    public void set(int value) {
        Vector v = new Vector();
        v.addElement(new Integer(value));
        
        get().setValues(v);
    }
    
    public void setCurRow(int row) {
        this.curRow = row;
    }
    
    public void setCurCol(int col) {
        this.curCol = col;
    }
    
    public void setFixed(int row, int col, boolean fixed) {
        get(row, col).setFixed(fixed);
    }
    
    public void setValues(int row, int col, int[] values) {
        get(row, col).setValues(values);
    }
    
    public ModelCell[][] getData() {
        return data;
    }
    
    public int getNumRows() {
        return numRows;
    }
    
    public void setNumRows(int numRows) {
        this.numRows = numRows;
    }
    
    public int getNumCols() {
        return numCols;
    }
    
    public void setValid(int r, int c, boolean valid) {
        get(r, c).setValid(valid);
    }
    
    public void setValid(boolean valid) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                get(r, c).setValid(valid);
            }
        }
    }
    
    public boolean isValid(int r, int c) {
        return get(r, c).isValid();
    }
    
    public void setNumCols(int numCols) {
        this.numCols = numCols;
    }
    
    public void toggleFix() {
        this.setFix(!isFix());
    }
    
    public String serialise() {
        return getParser().unparse(this);
        
//        StringBuffer sb = new StringBuffer();
//
//
//        for (int r = 0; r < 9; r++) {
//            for (int c = 0; c < 9; c++) {
//                ModelCell cell = getData()[r][c];
//
//                Vector values = cell.getValues();
//
//
//                if (cell.isFixed()) {
//                    sb.append(getFIXED());
//                } else {
//                    sb.append(getNOT_FIXED());
//                }
//
//                for(Enumeration e = values.elements(); e.hasMoreElements(); ) {
//                    Integer i = (Integer) e.nextElement();
//
//                    sb.append(i.intValue());
//
//                }
//
//                sb.append(0);
//            }
//        }
//
//        return sb.toString();
    }
    
    
    private StateModelSerialisationParser parser = new StateModelSerialisationParser();
    
    public void deserialise(String s) {
        
        getParser().parse(s, this);
    }
    
    public boolean isFix() {
        return fix;
    }
    
    public void setFix(boolean fix) {
        this.fix = fix;
    }
    
    
    
    public StateModelSerialisationParser getParser() {
        return parser;
    }
    
    public void setParser(StateModelSerialisationParser parser) {
        this.parser = parser;
    }
    
    
}
