package com.james.fileItems;

public class DiskFile {
    private long id;

    private String name;

    private Dir parent;

    private String path;

    private long size;

    private String type;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Dir getParent() {
        return parent;
    }

    public String getPath() {
        return path;
    }

    public long getSize() {
        return size;
    }

    public String getType() {
        return type;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParent(Dir parent) {
        this.parent = parent;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("File: " + getId() + " " + getName() + " " + getSize() + " (" + getParent() + ")");

        return sb.toString();
    }
}
