package com.james.dao.hibernate;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.james.dao.DirDao;
import com.james.fileItems.Dir;

public class HibernateDirDao extends HibernateDaoSupport implements DirDao {

    public void create(Dir dir) {
        if (dir == null) {
            logger.error("Unable to create null directory");
            // throw new InvalidStateException("Unable to create null
            // directory");
            return;
        }

        logger.debug("Creating dir " + dir);

        getHibernateTemplate().save(dir);

    }

    public void delete(Dir dir) {
        if (dir == null) {
            throw new InvalidStateException("Unable to delete null directory");
        }

        getHibernateTemplate().delete(dir);
    }

    public Dir load(long dirId) {
        return (Dir) getHibernateTemplate().load(Dir.class, new Long(dirId));
    }

    public void loadChildren(Dir dir, boolean children) {
        logger.debug("Loading children for dir " + dir);

        List files = getHibernateTemplate().find("from DiskFile where parent_id = ?",
                String.valueOf(dir.getId()));

        List mp3s = getHibernateTemplate().find("from MP3 where parent_id = ?",
                String.valueOf(dir.getId()));

        List dirs = getHibernateTemplate().find("from Dir where parent_id = ?",
                String.valueOf(dir.getId()));

        dir.setFiles(new HashSet(files));
        dir.setDirs(new HashSet(dirs));
        dir.setMp3s(new HashSet(mp3s));

        if (children) {
            for (Iterator i = dir.getDirs().iterator(); i.hasNext();) {
                Dir d = (Dir) i.next();

                loadChildren(d, false);
            }
        }
    }

    public List loadDirs(long dirId) {
        logger.debug("Loading dirs for dir " + dirId);

        List dirs = getHibernateTemplate().find("from Dir where parent_id = ?",
                String.valueOf(dirId));

        return dirs;
    }

    public List loadFiles(long dirId) {
        logger.debug("Loading files for dir " + dirId);

        List files = getHibernateTemplate().find("from DiskFile where parent_id = ?",
                String.valueOf(dirId));

        return files;
    }

    public List loadMp3s(long dirId) {
        logger.debug("Loading mp3s for dir " + dirId);

        List mp3s = getHibernateTemplate().find("from MP3 where parent_id = ?",
                String.valueOf(dirId));

        return mp3s;
    }

    public List search(String name, String path, String type) {
        logger.info("Searching for directory with only name parameter. Ignoring path and type");

        List results = getHibernateTemplate().find("from Dir where name like ?",
                new String[] { name });
        return results;
    }

    public void update(Dir dir) {
        if (dir == null) {
            throw new InvalidStateException("Unable to update null directory");
        }
        logger.debug("Updating dir " + dir);

        getHibernateTemplate().update(dir);
    }
}
