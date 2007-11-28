package com.james.dao.test;

import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.james.fileItems.TestNode;
import com.james.helper.TestNodeHelper;

public class TestTestNode extends TestCase {
    protected static final ApplicationContext CONTEXT = new ClassPathXmlApplicationContext(new String[] { "applicationContext.xml" });

    private Log log = LogFactory.getLog(this.getClass());

    public void test() {
        testNodeHelper = (TestNodeHelper) CONTEXT.getBean("nodeHelper");

        TestNode node = new TestNode();
        node.setName("TestTestNode");
        safeCreate(node);

        TestNode child = new TestNode();
        child.setName("child");
        node.setRoot(child);

        safeCreate(child);

        // TestNode child2 = new TestNode();
        // child2.setName("child2");
        // child2.setParent(node);
        // safeCreate(child2);
        //
        // node.addNode(child);
        // node.addNode(child2);

        update(node);

        log.info("---------------------");
        print("", node, true);
        log.info("---------------------");

        TestNode loaded = load("TestTestNode");

        log.info("+++ Loaded ++++++++++");

        print("", loaded, true);
    }

    private TestNode load(String name) {
        List results = testNodeHelper.search(name);

        return (TestNode) results.get(0);
    }

    private void update(TestNode node) {
        testNodeHelper.update(node);
    }

    private TestNodeHelper testNodeHelper;

    private void safeCreate(TestNode node) {
        List existing = testNodeHelper.search(node.getName());

        if (existing.size() != 0) {
            TestNode found = (TestNode) existing.get(0);
            testNodeHelper.delete(found);
        }
        testNodeHelper.create(node);
    }

    private void print(String indent, TestNode node, boolean children) {
        log.info(indent + node);

        print(" ", node.getParent(), children);
        // if (children) {
        //
        // for (Iterator i = node.getChildren().iterator(); i.hasNext();) {
        // TestNode child = (TestNode) i.next();
        //
        // print(indent + " ", child, children);
        // }
        // }
    }
}
