package com.james.sudoku.util.test;

import com.james.sudoku.util.CellValueSorter;
import java.util.Vector;

public class CellValueSorterTest {
    
    public CellValueSorterTest() {
    }
    
    public static void main(String args[]) {
        CellValueSorter sorter = new CellValueSorter();
        Vector values = new Vector();
        
        values.addElement(new Integer(1));
        values.addElement(new Integer(5));
        values.addElement(new Integer(3));
        values.addElement(new Integer(2));
        values.addElement(new Integer(4));
        values.addElement(new Integer(6));
        
        sorter.sort(values);
    }
}
