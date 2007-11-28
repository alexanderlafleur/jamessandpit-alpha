package com.james.dao.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.GenericJDBCException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.james.dao.DiskFileDao;
import com.james.fileItems.DiskFile;
import com.james.main.DiskFileSearchCriteria;

public class HibernateDiskFileDao extends HibernateDaoSupport implements DiskFileDao {

    private static Log log = LogFactory.getLog(HibernateDiskFileDao.class);

    public void delete(DiskFile file) {
        getHibernateTemplate().delete(file);
    }

    public DiskFile getFile(long fileId) {
        return (DiskFile) getHibernateTemplate().load(DiskFile.class, new Long(fileId));
    }

    public void create(DiskFile file) {
        if (file == null) {
            throw new InvalidStateException("Unable to create null file");
        }
        logger.debug("Creating file " + file);
        try {
            getHibernateTemplate().save(file);
        } catch (GenericJDBCException e) {
            e.printStackTrace();
        }
    }

    public void update(DiskFile file) {
        if (file == null) {
            throw new InvalidStateException("Unable to update null file");
        }
        logger.debug("Updating file " + file);

        getHibernateTemplate().update(file);
    }

    public List search(DiskFileSearchCriteria searchCriteria) {
        log.info("Searching for " + searchCriteria + " file.");

        DetachedCriteria criteria = DetachedCriteria.forClass(DiskFile.class);

        if (searchCriteria.getName() != null) {
            criteria.add(Restrictions.ilike("name", "%" + searchCriteria.getName() + "%"));
        }
        if (searchCriteria.getPath() != null) {
            criteria.add(Restrictions.eq("path", searchCriteria.getPath()));
        }
        if (searchCriteria.getType() != null) {
            criteria.add(Restrictions.eq("type", searchCriteria.getType()));
        }
        if (searchCriteria.getSizeHigh() != null) {
            if (searchCriteria.getSizeLow() != null) {
                criteria.add(Restrictions.between("size", searchCriteria.getSizeLow(), searchCriteria.getSizeHigh()));
            } else {
                criteria.add(Restrictions.ge("size", searchCriteria.getSizeLow()));
            }
        } else {
            if (searchCriteria.getSizeHigh() != null) {
                criteria.add(Restrictions.le("size", searchCriteria.getSizeHigh()));
            } else {
                // nothing
            }
        }

        List results = getHibernateTemplate().findByCriteria(criteria);

        return results;
    }
}
