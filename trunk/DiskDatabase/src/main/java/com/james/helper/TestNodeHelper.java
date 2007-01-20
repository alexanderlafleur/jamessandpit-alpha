package com.james.helper;

import java.util.List;

import com.james.dao.TestNodeDao;
import com.james.fileItems.TestNode;

/**
 * @author James
 */
public class TestNodeHelper {
	private TestNodeDao dao;

	public void create(TestNode node) {
		dao.create(node);
	}

	public void delete(TestNode node) {
		dao.delete(node);
	}

	public TestNodeDao getDao() {
		return dao;
	}

	public TestNode load(long nodeId) {
		TestNode node = dao.load(nodeId);

		// dao.loadChildren(TestNode);

		return node;
	}

	public void setDao(TestNodeDao dao) {
		this.dao = dao;
	}

	public void update(TestNode node) {
		dao.update(node);
	}

	public List search(String name) {
		return dao.search(name);
	}

	// public void verify(TestNode TestNode) throws InvalidFileItemException {
	// if (TestNode == null) {
	// return;
	// }
	//
	// if (TestNode.getName() == null
	// || TestNode.getName().trim().length() == 0) {
	// throw new InvalidFileItemException("TestNode is invalid. "
	// + TestNode + " name is invalid.");
	// }
	// for (Iterator i = TestNode.getTestNodes().iterator(); i.hasNext();) {
	// TestNode d = (TestNode) i.next();
	//
	// if (d.equals(TestNode)) {
	// throw new InvalidFileItemException("TestNode is invalid. "
	// + TestNode + " child " + d + " is its parent.");
	// }
	// verify(d);
	// }
	//
	// for (Iterator i = TestNode.getFiles().iterator(); i.hasNext();) {
	// DiskFile file = (DiskFile) i.next();
	//
	// getFileHelper().verify(file);
	// }
	//
	// // TestNode.getMp3s();
	//
	// if (TestNode.getParent() == TestNode) {
	// throw new InvalidFileItemException("TestNode is invalid. "
	// + TestNode + " parent is itself.");
	// }
	// }
}