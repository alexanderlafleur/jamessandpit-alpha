package com.james.ui.dto;

import java.util.ArrayList;
import java.util.List;

public class WalletDTO extends DTO {
    private String description;

    private List<DiskDTO> disks = new ArrayList<DiskDTO>();

    public void addDisk(DiskDTO disk) {
        disks.add(disk);
    }

    public String getDescription() {
        return description;
    }

    public List<DiskDTO> getDisks() {
        return disks;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDisks(List<DiskDTO> disks) {
        this.disks = disks;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append(getDescription() + "(" + getId() + ")");

        return sb.toString();
    }
}
