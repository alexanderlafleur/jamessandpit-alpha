package com.james.dao;

import java.util.List;

import com.james.fileItems.DiskFile;
import com.james.main.DiskFileSearchCriteria;

public interface DiskFileDao {

	public abstract void create(DiskFile disk);

	public abstract void delete(DiskFile file);

	public abstract DiskFile getFile(long id);

	public abstract List search(DiskFileSearchCriteria criteria);

	public abstract void update(DiskFile disk);

}