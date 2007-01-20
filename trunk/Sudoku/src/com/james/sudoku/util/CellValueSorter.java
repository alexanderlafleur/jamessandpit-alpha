package com.james.sudoku.util;

import java.util.Vector;

public class CellValueSorter {
    
    private static void shellSort(final Vector numbers) {
        int i, j, increment, temp;
        
        int array_size = numbers.size();
        
        increment = 3;
        
        while (increment > 0) {
            
            for (i = 0; i < array_size; i++) {
                j = i;
                temp = ((Integer) numbers.elementAt(i)).intValue();
                
                while ((j >= increment)
                && (((Integer) numbers.elementAt(j - increment))
                .intValue() > temp)) {
                    numbers.setElementAt(numbers.elementAt(j - increment), j);
                    j = j - increment;
                }
                
                numbers.setElementAt(new Integer(temp), j);
            }
            if (increment / 2 != 0) {
                increment = increment / 2;
            } else if (increment == 1) {
                increment = 0;
            } else {
                increment = 1;
            }
        }
    }
    
    public static final void sort(final Vector v) {
        shellSort(v);
    }
    
    private void shellSort2(int numbers[], int array_size) {
        int i, j, increment, temp;
        
        increment = 3;
        
        while (increment > 0) {
            
            for (i = 0; i < array_size; i++) {
                j = i;
                temp = numbers[i];
                
                while ((j >= increment) && (numbers[j - increment] > temp)) {
                    numbers[j] = numbers[j - increment];
                    j = j - increment;
                }
                
                numbers[j] = temp;
            }
        }
        
        if (increment / 2 != 0) {
            increment = increment / 2;
        } else if (increment == 1) {
            increment = 0;
        } else {
            increment = 1;
        }
    }
}
