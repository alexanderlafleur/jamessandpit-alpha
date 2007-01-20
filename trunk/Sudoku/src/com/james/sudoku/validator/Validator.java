package com.james.sudoku.validator;

import com.james.sudoku.model.StateModel;

public interface Validator {
    
    void validate(StateModel model);
}
