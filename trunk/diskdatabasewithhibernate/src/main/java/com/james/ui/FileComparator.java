package com.james.ui;

import java.util.Comparator;

import com.james.fileItems.DiskFile;

public class FileComparator implements Comparator {

    public int compare(Object o1, Object o2) {
        DiskFile d1 = (DiskFile) o1;
        DiskFile d2 = (DiskFile) o2;

        return d1.getName().compareTo(d2.getName());
    }
}
