package com.james.dao.hibernate;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.james.fileItems.Dir;
import com.james.fileItems.DiskFile;
import com.james.helper.DirHelper;
import com.james.helper.DiskFileHelper;
import com.james.helper.DiskHelper;
import com.james.helper.WalletHelper;

public class HibernateFileDaoTest extends TestCase {

	private WalletHelper walletHelper;

	private DirHelper dirHelper;

	private DiskHelper diskHelper;

	private DiskFileHelper fileHelper;

	protected static final ApplicationContext CONTEXT = new ClassPathXmlApplicationContext(
			new String[] { "applicationContext.xml" });

	protected void setUp() throws Exception {
		walletHelper = (WalletHelper) CONTEXT.getBean("walletHelper");
		diskHelper = (DiskHelper) CONTEXT.getBean("diskHelper");
		dirHelper = (DirHelper) CONTEXT.getBean("dirHelper");
		fileHelper = (DiskFileHelper) CONTEXT.getBean("diskFileHelper");
	}

	public void test() {

		// Root
		Dir root = new Dir();
		root.setName("test");

		// File1
		DiskFile file1 = new DiskFile();
		file1.setName("test");
		file1.setType("test");
		file1.setPath("test");
		file1.setSize(1);
		file1.setParent(root);

		// File2
		DiskFile file2 = new DiskFile();
		file2.setName("test2");
		file2.setType("test2");
		file2.setPath("test2");
		file2.setSize(1);
		file2.setParent(root);

		dirHelper.create(root);

		root.addFile(file1);
		root.addFile(file2);

		// file1.setParent(root);
		// file2.setParent(root);

		dirHelper.create(root);

		// dirHelper.save(root);
		//
		// file.setParent(root);
		//
		// fileHelper.save(file1);
		// fileHelper.save(file2);

		Dir loadedRoot = dirHelper.load(root.getId());

		System.out.println(loadedRoot);

	}
}
