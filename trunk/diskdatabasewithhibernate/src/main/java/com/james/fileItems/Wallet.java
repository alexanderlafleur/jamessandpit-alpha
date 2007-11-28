package com.james.fileItems;

import java.util.HashSet;
import java.util.Set;

public class Wallet {

    private String description;

    private Set<Disk> disks;

    private long id;

    public Wallet() {
        disks = new HashSet<Disk>();
    }

    public Wallet(String discription) {
        this();
        this.description = discription;
    }

    public void addDisk(Disk disk) {
        disks.add(disk);
    }

    public String getDescription() {
        return description;
    }

    public Set<Disk> getDisks() {
        return disks;
    }

    public long getId() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDisks(Set<Disk> disks) {
        this.disks = disks;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("Wallet: " + getId() + " " + getDescription());

        return sb.toString();
    }
}