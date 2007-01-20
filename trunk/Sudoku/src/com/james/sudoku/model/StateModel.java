package com.james.sudoku.model;

import java.util.Vector;

public interface StateModel {
    void addNumber(int value);
    
    ModelCell get();
    
    ModelCell get(int row, int y);
    
    ModelCell[] getRow(int row);
    
    ModelCell[] getCol(int row);
    
    int[] getValues(int row, int col);
    
    int getNumRows();
    
    int getNumCols();
    
    void set(int row, int col, Vector values, boolean fixed);
    
    void set(int row, int y, int[] values, boolean fixed);
    
    void set(int[] values);
    
    void set(int value);
    
    boolean isValid(int r, int c);
    
    int getCurRow();
    
    int getCurCol();
    
    void setCurRow(int row);
    
    void setCurCol(int y);
    
    void setFixed(int row, int y, boolean fixed);
    
    void setValues(int row, int y, int[] values);
    
    void setValid(int r, int c, boolean valid);
    
    void setValid(boolean valid);
    
    String serialise();
    
    void deserialise(String string);
}