package com.james.fileItems.util;

import java.io.File;
import java.util.Comparator;

/*
 * Created on Jan 30, 2005
 */

/**
 * @author James
 *
 */
public class FileNameComparator implements Comparator {

    public int compare(Object arg0, Object arg1) {
        if (arg0 != null && arg1 != null && arg0 instanceof java.io.File
                && arg1 instanceof java.io.File) {

            return ((File) arg0).compareTo((File) arg1);

        }

        return 0;
    }
}