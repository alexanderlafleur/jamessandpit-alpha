package com.james.ui.dto;

public class DiskFileDTO extends DTO {
	private String name;

	private DirDTO parent;

	private String path;

	private String size;

	private String type;

	public String getName() {
		return name;
	}

	public DirDTO getParent() {
		return parent;
	}

	public String getPath() {
		return path;
	}

	public String getSize() {
		return size;
	}

	public String getType() {
		return type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setParent(DirDTO parent) {
		this.parent = parent;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("FileDTO: " + getId() + " " + getName() + " " + getSize()
				+ " (" + getParent() + ")");

		return sb.toString();
	}

}
