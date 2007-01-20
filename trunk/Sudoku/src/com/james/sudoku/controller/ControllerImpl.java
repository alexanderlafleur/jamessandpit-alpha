package com.james.sudoku.controller;

import com.james.sudoku.model.ModelCell;
import com.james.sudoku.model.StateModel;
import com.james.sudoku.validator.Validator;
import com.james.sudoku.validator.impl.ValidatorImpl;
import com.james.sudoku.view.StateChangeListener;

public class ControllerImpl implements Controller {
    private StateModel curModel;
    
    private StateChangeListener listener;
    
    private int numCols;
    
    private boolean fix;
    
    private int numRows;
    
    public ControllerImpl(StateModel curModel, int numRows, int numCols) {
        setCurModel(curModel);
        
        setNumRows(numRows);
        setNumCols(numCols);
        
        setValidator(new ValidatorImpl());
    }
    
    public ControllerImpl(StateChangeListener listener, StateModel curModel,
            int numRows, int numCols) {
        this(curModel, numRows, numCols);
        
        setStateChangeListener(listener);
    }
    
    public boolean equals(Object obj) {
        boolean retValue;
        
        retValue = super.equals(obj);
        return retValue;
    }
    
    public int getCurRow() {
        return getCurModel().getCurRow();
    }
    
    public int getCurCol() {
        return getCurModel().getCurCol();
    }
    
    public ModelCell get(int x, int y) {
        return getCurModel().get(x, y);
    }
    
    public StateModel getCurModel() {
        return curModel;
    }
    
    public StateChangeListener getStateChangeListener() {
        return listener;
    }
    
    public int hashCode() {
        int retValue;
        
        retValue = super.hashCode();
        return retValue;
    }
    
    public void inputNumber(int number, boolean replace) {
        
        ModelCell cell = getCurModel().get();
        
        if (isFix()){
            getCurModel().set(number);
            cell.setFixed(true);
            
        } else {
            if (cell.isFixed()) {
                if (replace) {
                    getCurModel().set(number);
                }
                
            } else {
                if (replace) {
                    getCurModel().set(number);
                } else {
                    getCurModel().addNumber(number);
                }
                
            }
        }
        
        getValidator().validate(getCurModel());
        
        stateChanged();
    }
    
    private void stateChanged() {
        getStateChangeListener().notifyStateChanged();
    }
    
    public void moveDown() {
        // System.out.println("Move down");
        if (getCurRow() == getNumRows() - 1) {
            // nothing
        } else {
            getCurModel().setCurRow(getCurRow() + 1);
        }
        stateChanged();
    }
    
    public void moveLeft() {
        // System.out.println("Move left");
        if (getCurCol() == 0) {
            // nothing
        } else {
            getCurModel().setCurCol(getCurCol() - 1);
        }
        stateChanged();
    }
    
    public void moveRight() {
        // System.out.println("Move right");
        if (getCurCol() == getNumCols() - 1) {
            // nothing
        } else {
            getCurModel().setCurCol(getCurCol() + 1);
        }
        stateChanged();
    }
    
    public void moveUp() {
        // System.out.println("Move up");
        if (getCurRow() == 0) {
            // nothing
        } else {
            getCurModel().setCurRow(getCurRow() - 1);
        }
        stateChanged();
    }
    
    public void select() {
        getStateChangeListener().notifyStateChanged();
    }
    
    public void select(int row, int col) {
        getCurModel().setCurRow(row);
        getCurModel().setCurCol(col);
        
        stateChanged();
    }
    
    public void setCurModel(StateModel curModel) {
        this.curModel = curModel;
    }
    
    public void setNumCols(int numCols) {
        this.numCols = numCols;
    }
    
    public void setNumRows(int numRows) {
        this.numRows = numRows;
    }
    
    public void setStateChangeListener(StateChangeListener listener) {
        this.setListener(listener);
    }
    
    public void setListener(StateChangeListener listener) {
        this.listener = listener;
    }
    
    public int getNumCols() {
        return numCols;
    }
    
    public int getNumRows() {
        return numRows;
    }
    
    public void validate() {
        getValidator().validate(getCurModel());
    }
    
    private Validator validator;
    
    public Validator getValidator() {
        return validator;
    }
    
    public void setValidator(Validator validator) {
        this.validator = validator;
    }
    
    public boolean isFix() {
        return fix;
    }
    
    public void setFix(boolean fix) {
        this.fix = fix;
    }
    
    public void toggleFix(){
        setFix(!isFix());
    }
}