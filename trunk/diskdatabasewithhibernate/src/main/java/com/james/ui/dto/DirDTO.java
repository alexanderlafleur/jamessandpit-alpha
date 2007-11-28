package com.james.ui.dto;

import java.util.ArrayList;
import java.util.List;

public class DirDTO extends DTO {
    private List<DirDTO> dirs = new ArrayList<DirDTO>();

    private List<DiskFileDTO> files = new ArrayList<DiskFileDTO>();

    private List<MP3DTO> mp3s = new ArrayList<MP3DTO>();

    private String name;

    private DirDTO parent;

    public void addDir(DirDTO dir) {
        dirs.add(dir);
    }

    public void addFile(DiskFileDTO file) {
        files.add(file);
    }

    public void addMp3(MP3DTO mp3) {
        mp3s.add(mp3);
    }

    public List getDirs() {
        return dirs;
    }

    public List<DiskFileDTO> getFiles() {
        return files;
    }

    public List getMp3s() {
        return mp3s;
    }

    public String getName() {
        return name;
    }

    public DirDTO getParent() {
        return parent;
    }

    public void setDirs(List<DirDTO> dirs) {
        this.dirs = dirs;
    }

    public void setFiles(List<DiskFileDTO> files) {
        this.files = files;
    }

    public void setMp3s(List<MP3DTO> mp3s) {
        this.mp3s = mp3s;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParent(DirDTO parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("DirDTO: " + getId() + " " + getName() + " (" + getParent() + ")");

        return sb.toString();
    }
}
