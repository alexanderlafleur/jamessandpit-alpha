package com.james.main;

public class DiskFileSearchCriteria {
    private String name;

    private String path;

    private Long sizeHigh;

    private Long sizeLow;

    private String type;

    @Override
    public String toString() {
        return "name: " + name + " " + " path: " + path + " sizeHigh: " + sizeHigh + " sizeLow: " + sizeLow + " type: " + type;
    }

    public DiskFileSearchCriteria(String name, String type, String path, Long sizeLow, Long sizeHigh) {
        this.name = name;
        this.type = type;
        this.path = path;
        this.sizeLow = sizeLow;
        this.sizeHigh = sizeHigh;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public Long getSizeHigh() {
        return sizeHigh;
    }

    public Long getSizeLow() {
        return sizeLow;
    }

    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSizeHigh(Long sizeHigh) {
        this.sizeHigh = sizeHigh;
    }

    public void setSizeLow(Long sizeLow) {
        this.sizeLow = sizeLow;
    }

    public void setType(String type) {
        this.type = type;
    }
}
