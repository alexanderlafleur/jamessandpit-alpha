package com.james.dao;

import java.util.List;

import com.james.fileItems.Dir;
import com.james.fileItems.Disk;

public interface DiskDao {

    public abstract void create(Disk disk);

    public abstract void delete(Disk file);

    public abstract Disk load(long id);

    public abstract List loadAll(long walletId);

    public abstract List search(String label);

    public abstract void update(Disk disk);

    public abstract List getRootOrphanedRoots();

    public abstract List getOrphanedDisks();

    public abstract Disk findDisk(long id);

    public abstract Disk findDiskByMediaNumber(long mediaNumber);

    public abstract Dir loadRoot(String diskId);

    public abstract Disk loadByName(String name);

}