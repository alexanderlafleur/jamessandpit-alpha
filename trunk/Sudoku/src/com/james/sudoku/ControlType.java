package com.james.sudoku;

public final class ControlType {
    
    public static final ControlType GRID_CLICKED = new ControlType("Grid");
    
    public static final ControlType FIXED_BUTTON = new ControlType(
            "Fixed Button");
    
    public static final ControlType PENCIL_BUTTON = new ControlType(
            "Pencil Button");
    
    private ControlType(final String newName) {
        name = newName;
    }
    
    private final int id = nextId++;
    
    private static int nextId = 0;
    
    private final String name;
    
    public final boolean equals(Object obj) {
        return super.equals(obj);
    }
    
    public String toString() {
        return name;
    }
    
    public int hashCode() {
        int retValue;
        
        retValue = super.hashCode();
        return retValue;
    }
    
}
