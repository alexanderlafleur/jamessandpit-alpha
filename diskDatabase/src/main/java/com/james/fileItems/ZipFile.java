package com.james.fileItems;

public class ZipFile {
	private int id;

	private String name;

	private Dir parent;

	private String[] zippedFiles;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Dir getParent() {
		return parent;
	}

	public String[] getZippedFiles() {
		return zippedFiles;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setParent(Dir parent) {
		this.parent = parent;
	}

	public void setZippedFiles(String[] zippedFiles) {
		this.zippedFiles = zippedFiles;
	}
}