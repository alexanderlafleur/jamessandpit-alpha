package com.james.fileItems;

import java.util.HashSet;
import java.util.Set;

public class Dir {
    private Set<Dir> dirs;

    private Set<DiskFile> files;

    private long id;

    private Set<MP3> mp3s;

    private String name;

    private Dir parent;

    public Dir() {
        dirs = new HashSet<Dir>();
        files = new HashSet<DiskFile>();
        mp3s = new HashSet<MP3>();
    }

    public void addDir(Dir dir) {
        dirs.add(dir);
    }

    public void addFile(DiskFile file) {
        files.add(file);
    }

    public void addFiles(Set<DiskFile> files) {
        files.addAll(files);
    }

    public void addMp3(MP3 mp3) {
        mp3s.add(mp3);
    }

    @Override
    public boolean equals(Object obj) {
        Dir other = (Dir) obj;
        return this.getId() == other.getId() && this.getName().equals(other.getName());
    }

    public Set getDirs() {
        return dirs;
    }

    public Set getFiles() {
        return files;
    }

    public long getId() {
        return id;
    }

    public Set getMp3s() {
        return mp3s;
    }

    public String getName() {
        return name;
    }

    public Dir getParent() {
        return parent;
    }

    public void setDirs(Set<Dir> dirs) {
        this.dirs = dirs;
    }

    public void setFiles(Set<DiskFile> files) {
        this.files = files;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setMp3s(Set<MP3> mp3s) {
        this.mp3s = mp3s;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParent(Dir parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("Dir: " + getId() + " " + getName() + " (" + getParent() + ")");

        return sb.toString();
    }
}