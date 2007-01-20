package com.james.sudoku.controller;

import com.james.sudoku.model.ModelCell;
import com.james.sudoku.model.StateModel;
import com.james.sudoku.view.StateChangeListener;

public interface Controller {
    ModelCell get(int row, int col);
    
    int getCurRow();
    
    int getCurCol();
    
    StateModel getCurModel();
    
    void validate();
    
    void inputNumber(int number, boolean replace);
    
    void moveDown();
    
    void moveLeft();
    
    void moveRight();
    
    void moveUp();
    
    void select();
    
    void select(int row, int col);

    StateChangeListener getStateChangeListener();
    
    void setStateChangeListener(StateChangeListener listener);
    
    void toggleFix();

    void setCurModel(StateModel stateModel);
}
