package com.james.dao.hibernate;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.james.dao.TestNodeDao;
import com.james.fileItems.TestNode;

public class HibernateTestNodeDao extends HibernateDaoSupport implements TestNodeDao {

    public void create(TestNode node) {
        if (node == null) {
            logger.error("Unable to create null node");
            return;
        }

        logger.debug("Creating node " + node);

        getHibernateTemplate().save(node);

    }

    public void delete(TestNode node) {
        if (node == null) {
            throw new InvalidStateException("Unable to delete null node");
        }

        getHibernateTemplate().delete(node);
    }

    public TestNode load(long nodeId) {
        return (TestNode) getHibernateTemplate().load(TestNode.class, new Long(nodeId));
    }

    public void update(TestNode node) {
        if (node == null) {
            throw new InvalidStateException("Unable to update null node");
        }
        logger.debug("Updating node " + node);

        getHibernateTemplate().update(node);
    }

    public List search(String name) {
        List results = getHibernateTemplate().find("from TestNode where name like ?", name);
        return results;
    }

}
