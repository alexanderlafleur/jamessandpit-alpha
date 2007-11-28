/*
 * Created on Jan 30, 2005
 *
 */
package com.james.fileItems;

/**
 * @author James
 */
public class Disk {
    private long id;

    private String label;

    private long mediaNumber;

    private Dir root;

    public Disk() {

    }

    public Disk(String label) {
        this.label = label;
    }

    public long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public long getMediaNumber() {
        return mediaNumber;
    }

    public Dir getRoot() {
        return root;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setMediaNumber(long mediaNumber) {
        this.mediaNumber = mediaNumber;
    }

    public void setRoot(Dir root) {
        this.root = root;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("Disk: " + getId() + " " + getLabel() + ": " + getMediaNumber());

        return sb.toString();
    }

}