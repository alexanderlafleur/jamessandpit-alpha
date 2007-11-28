package com.james.fileItems;

import java.util.HashSet;
import java.util.Set;

public class Wallet {

	private String description;

	private Set disks;

	private long id;

	public Wallet() {
		disks = new HashSet();
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

	public Set getDisks() {
		return disks;
	}

	public long getId() {
		return id;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDisks(Set disks) {
		this.disks = disks;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("Wallet: " + getId() + " " + getDescription());

		return sb.toString();
	}
}