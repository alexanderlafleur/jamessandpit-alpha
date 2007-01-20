package com.james.sudoku.event;

public class PencilButtonClick implements ClickType {
    
    private int num;
    
    public PencilButtonClick(int num) {
        this.num = num;
    }
    
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        
        if (obj instanceof PencilButtonClick) {
            PencilButtonClick other = (PencilButtonClick) obj;
            
            return other.num == num;
        } else {
            return false;
        }
    }
    
    public String toString() {
        return "Pencil" + num;
    }
}
