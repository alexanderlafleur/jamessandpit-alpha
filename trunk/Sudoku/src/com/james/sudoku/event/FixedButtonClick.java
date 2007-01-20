package com.james.sudoku.event;

public class FixedButtonClick implements ClickType {
    
    private int num;
    
    public FixedButtonClick(int num) {
        this.num = num;
    }
    
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        
        if (obj instanceof FixedButtonClick) {
            FixedButtonClick other = (FixedButtonClick) obj;
            
            return other.num == num;
        } else {
            return false;
        }
    }
    
    public String toString() {
        return "Pencil" + num;
    }
}
