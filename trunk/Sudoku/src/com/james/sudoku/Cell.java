/*
 * Cell.java
 *
 * Created on 5 October 2006, 16:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.james.sudoku;

import java.util.Vector;

/**
 *
 * @author james
 */
public class Cell {
    
    private int x;
    
    private int y;
    
    // private int[] values;
    private Vector values;
    
    private boolean fixed;
    
    public Cell(int x, int y, int[] val, boolean fixed) {
        this.values = new Vector(6);
        setX(x);
        setY(y);
        addValues(val);
        setFixed(fixed);
    }
    
    public Cell(int x, int y, int[] values) {
        this(x, y, values, false);
    }
    
    public int getX() {
        return x;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public int getY() {
        return y;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    public int getValue(int i) {
        return ((Integer) values.elementAt(i)).intValue();
    }
    
    public Vector getValues() {
        return values;
    }
    
    public void setValues(int[] newValues) {
        for (int i = 0; i < newValues.length; i++) {
            values.addElement(new Integer(newValues[i]));
        }
    }
    
    public void addValues(int[] newValues) {
        
    }
    
    public void addValue(int value) {
        values.addElement(new Integer(value));
    }
    
    public boolean isFixed() {
        return fixed;
    }
    
    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }
    
}
