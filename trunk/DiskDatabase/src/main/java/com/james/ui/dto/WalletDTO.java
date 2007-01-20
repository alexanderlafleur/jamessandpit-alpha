package com.james.ui.dto;

import java.util.ArrayList;
import java.util.List;

public class WalletDTO extends DTO {
	private String description;

	private List disks = new ArrayList();

	public void addDisk(DiskDTO disk) {
		disks.add(disk);
	}

	public String getDescription() {
		return description;
	}

	public List getDisks() {
		return disks;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDisks(List disks) {
		this.disks = disks;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append(getDescription() + "(" + getId() + ")");

		return sb.toString();
	}
}
