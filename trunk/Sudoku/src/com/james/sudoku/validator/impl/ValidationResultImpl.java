package com.james.sudoku.validator.impl;

import com.james.sudoku.validator.ValidationResult;
import java.util.*;

public class ValidationResultImpl implements ValidationResult {
    private Vector errors = new Vector();
    
    public ValidationResultImpl() {
    }
    
    public Vector getErrors() {
        return errors;
    }
    
    public void addErrors(Vector newErrors) {
        for (Enumeration e = newErrors.elements(); e.hasMoreElements();) {
            String curElement = (String) e.nextElement();
            errors.addElement(curElement);
        }
    }
    
}
