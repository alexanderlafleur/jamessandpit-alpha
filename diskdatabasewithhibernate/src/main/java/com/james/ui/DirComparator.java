package com.james.ui;

import java.util.Comparator;

import com.james.fileItems.Dir;

public class DirComparator implements Comparator {

    public int compare(Object o1, Object o2) {
        Dir d1 = (Dir) o1;
        Dir d2 = (Dir) o2;

        return d1.getName().compareTo(d2.getName());
    }
}
