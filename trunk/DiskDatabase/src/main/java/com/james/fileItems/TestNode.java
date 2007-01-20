package com.james.fileItems;

import java.util.HashSet;
import java.util.Set;

public class TestNode {
	private Set children;

	private long id;

	private String name;

	private TestNode parent;

	private TestNode root;

	public TestNode() {
		children = new HashSet();
	}

	public void addNode(TestNode node) {
		children.add(node);
	}

	public boolean equals(Object obj) {
		TestNode other = (TestNode) obj;
		return this.getId() == other.getId()
				&& this.getName().equals(other.getName());
	}

	public Set getChildren() {
		return children;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public TestNode getParent() {
		return parent;
	}

	public TestNode getRoot() {
		return root;
	}

	public void setChildren(Set dirs) {
		this.children = dirs;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setParent(TestNode parent) {
		this.parent = parent;
	}

	public void setRoot(TestNode root) {
		this.root = root;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("Node: " + getId() + " " + getName() + " (" + getParent()
				+ ")");

		return sb.toString();
	}
}