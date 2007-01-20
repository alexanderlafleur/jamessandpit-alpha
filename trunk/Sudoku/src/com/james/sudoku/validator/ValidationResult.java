package com.james.sudoku.validator;

import java.util.*;

public interface ValidationResult {
    Vector getErrors();
    
    void addErrors(Vector errors);
}
