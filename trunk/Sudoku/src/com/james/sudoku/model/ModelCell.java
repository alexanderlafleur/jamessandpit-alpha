package com.james.sudoku.model;

import com.james.sudoku.util.CellValueContainsUtility;
import com.james.sudoku.util.CellValueSorter;
import java.util.Vector;

public class ModelCell {
    private Vector values = new Vector();
    
    private boolean fixed;
    
    private boolean valid;
    
    public ModelCell(int[] newValues, boolean fixed, boolean valid) {
        this(ModelCell.convertToVector(newValues), fixed, valid);
    }
    
    private static final Vector convertToVector(int vals[]) {
        Vector newValues = new Vector(vals.length);
        
        for (int i = 0; i < vals.length; i++) {
            newValues.addElement(new Integer(vals[i]));
        }
        
        return newValues;
    }
    
    public ModelCell(Vector values, boolean fixed, boolean valid) {
        this.setValues(values);
        this.setFixed(fixed);
        this.setValid(valid);
    }
    
    public Vector getValues() {
        return values;
    }
    
    public void addValue(int value) {
        addValue(new Integer(value), true);
    }
    
    private void addValue(Integer value, boolean sort) {
        
        if (CellValueContainsUtility.contains(getValues(), value)) {
            this.getValues().removeElement(value);
            
        } else {
            this.getValues().addElement(value);
            
            if (sort && this.getValues().size() > 1) {
                CellValueSorter.sort(this.getValues());
            }
        }
    }
    
    public void setValues(Vector newValues) {
        values = new Vector();
        
        for (int i = 0, size = newValues.size(); i < size; i++) {
            addValue((Integer) newValues.elementAt(i), false);
        }
        
        if (this.values.size() > 1) {
            CellValueSorter.sort(this.values);
        }
        
    }
    
    public void setValues(int values[]) {
        setValues(convertToVector(values));
    }
    
    public boolean isFixed() {
        return fixed;
    }
    
    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }
    
    public boolean isValid() {
        return valid;
    }
    
    public void setValid(boolean valid) {
        this.valid = valid;
    }
    
}
