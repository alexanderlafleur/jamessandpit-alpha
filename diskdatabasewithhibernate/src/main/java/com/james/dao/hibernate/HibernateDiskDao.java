package com.james.dao.hibernate;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.james.dao.DiskDao;
import com.james.dao.sql.NotFoundException;
import com.james.fileItems.Dir;
import com.james.fileItems.Disk;

public class HibernateDiskDao extends HibernateDaoSupport implements DiskDao {
    public void create(Disk disk) {
        logger.debug("Creating disk " + disk);

        getHibernateTemplate().save(disk);
    }

    public void delete(Disk disk) {
        getHibernateTemplate().delete(disk);
    }

    /**
     * Finds the Disk that has root as its "root"
     * 
     * @param root
     * @return
     */
    public Disk findDisk(long rootId) {
        List results = getHibernateTemplate().find("from Disk where root_Id = ?", "" + rootId);

        Disk disk;

        if (results.size() == 0) {
            throw new NotFoundException("Unable to find Disk with root id of: " + rootId);

        } else if (results.size() == 1) {
            disk = (Disk) results.get(0);

        } else {
            throw new NotFoundException("More than one disk found with root id of: " + rootId);

        }

        return disk;
    }

    public Disk findDiskByMediaNumber(long mediaNumber) {
        List results = getHibernateTemplate().find("from Disk where mediaNumber = ?", "" + mediaNumber);

        Disk disk;

        if (results.size() == 0) {
            throw new NotFoundException("Unable to find Disk with mediaNumber of: " + mediaNumber);

        } else if (results.size() == 1) {
            disk = (Disk) results.get(0);

        } else {
            throw new NotFoundException("More than one disk found with mediaNumber of: " + mediaNumber);

        }

        return disk;
    }

    public List getOrphanedDisks() {
        return getHibernateTemplate().find("select disk from Disk disk where disk.id not in (select wallet.disks.id from Wallet wallet)");

    }

    public List getRootOrphanedRoots() {
        return getHibernateTemplate().find("select dir from Dir dir where dir.parent is null and dir.id not in (select disk.root from Disk as disk)");

    }

    public Disk load(long id) {
        return (Disk) getHibernateTemplate().load(Disk.class, new Long(id));
    }

    public List loadAll(long walletId) {
        List results = getHibernateTemplate().find("from Disk where wallet_Id = ?", "" + walletId);
        return results;
    }

    public Disk loadByName(String label) {
        List results = getHibernateTemplate().find("from Disk where label = ?", "" + label);

        Disk disk;

        if (results.size() == 0) {
            throw new NotFoundException("Unable to find Disk with label of: " + label);

        } else if (results.size() == 1) {
            disk = (Disk) results.get(0);

        } else {
            throw new NotFoundException("More than one disk found with label of: " + label);

        }

        return disk;
    }

    public Dir loadRoot(String diskId) {
        Disk disk = load(Long.parseLong(diskId));

        return disk.getRoot();
    }

    public List search(String label) {
        List results = getHibernateTemplate().find("from Disk where label like ?", label);
        return results;
    }

    public void update(Disk disk) {
        logger.debug("Updating disk " + disk);

        getHibernateTemplate().update(disk);
    }
}
