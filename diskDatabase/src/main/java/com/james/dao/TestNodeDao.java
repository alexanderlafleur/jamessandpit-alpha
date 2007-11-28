package com.james.dao;

import java.util.List;

import com.james.fileItems.TestNode;

public interface TestNodeDao {

	public abstract void create(TestNode node);

	public abstract void delete(TestNode n);

	public abstract TestNode load(long id);

	public abstract void update(TestNode node);

	public abstract List search(String name);

}