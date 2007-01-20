
package com.james.fileItems.util;
import java.io.File;
import java.io.FileFilter;

/*
 * Created on Jan 30, 2005
 */

/**
 * @author James
 *
 */
public class FileOrDirectoryFilter implements FileFilter {
    private boolean findDirs;

    public FileOrDirectoryFilter(boolean dirs) {
        findDirs = dirs;

    }

    public boolean accept(File arg0) {
        return arg0 != null && (findDirs && arg0.isDirectory() || !findDirs && !arg0.isDirectory());
    }
}