package com.james.dao;

import java.util.List;

import com.james.fileItems.Dir;

public interface DirDao {
	public void create(Dir dir);

	public void delete(Dir dir);

	public Dir load(long dirId);

	public void loadChildren(Dir dir, boolean children);

	public List loadDirs(long id);

	public List loadFiles(long id);

	public List loadMp3s(long id);

	public List search(String name, String path, String type);

	public void update(Dir dir);
}