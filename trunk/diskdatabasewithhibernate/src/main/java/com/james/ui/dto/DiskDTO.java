package com.james.ui.dto;

public class DiskDTO extends DTO {

    private String label;

    private long mediaNumber;

    private DirDTO root;

    public String getLabel() {
        return label;
    }

    public long getMediaNumber() {
        return mediaNumber;
    }

    public DirDTO getRoot() {
        return root;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setMediaNumber(long mediaNumber) {
        this.mediaNumber = mediaNumber;
    }

    public void setRoot(DirDTO root) {
        this.root = root;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append(getLabel() + "(" + getId() + "-" + +getMediaNumber() + ")");

        return sb.toString();
    }
}
