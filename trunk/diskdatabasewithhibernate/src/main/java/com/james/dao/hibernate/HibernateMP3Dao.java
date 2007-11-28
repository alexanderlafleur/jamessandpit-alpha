package com.james.dao.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.GenericJDBCException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.james.dao.MP3Dao;
import com.james.fileItems.MP3;

public class HibernateMP3Dao extends HibernateDaoSupport implements MP3Dao {

    private static Log log = LogFactory.getLog(HibernateDiskFileDao.class);

    public void create(MP3 mp3) {
        if (mp3 == null) {
            throw new InvalidStateException("Unable to create null mp3");
        }
        logger.debug("Creating mp3 " + mp3);
        try {
            getHibernateTemplate().save(mp3);

        } catch (GenericJDBCException e) {
            e.printStackTrace();
        }

        getHibernateTemplate().save(mp3);
    }

    public void delete(MP3 mp3) {
        getHibernateTemplate().delete(mp3);
    }

    public MP3 getMP3(long id) {
        // TODO Auto-generated method stub
        return null;
    }

    public MP3 load(long fileId) {
        // TODO Auto-generated method stub
        return null;
    }

    public List search(MP3SearchCriteria searchCriteria) {
        log.info("Searching for " + searchCriteria + " mp3.");

        DetachedCriteria criteria = DetachedCriteria.forClass(MP3.class);

        if (searchCriteria.getName() != null) {
            criteria.add(Restrictions.ilike("name", "%" + searchCriteria.getName() + "%"));
        }
        if (searchCriteria.getAlbum() != null) {
            criteria.add(Restrictions.ilike("album", "%" + searchCriteria.getAlbum() + "%"));
        }
        if (searchCriteria.getType() != null) {
            criteria.add(Restrictions.ilike("name", "%." + searchCriteria.getType()));
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

    public void update(MP3 mp3) {
        getHibernateTemplate().update(mp3);
    }

}
