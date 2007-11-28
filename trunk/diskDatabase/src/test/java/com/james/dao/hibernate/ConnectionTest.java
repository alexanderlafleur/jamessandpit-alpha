package com.james.dao.hibernate;

import java.sql.SQLException;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ConnectionTest extends TestCase {
	protected static final ApplicationContext CONTEXT = new ClassPathXmlApplicationContext(
			new String[] { "applicationContext.xml"
			// , "applicationContextDatabase.xml"// ,
			// "applicationContextMain.xml"
			});

	// protected static final ApplicationContext CONTEXT = new
	// FileSystemXmlApplicationContext(
	// new String[] { "src/main/webapp/WEB-INF/classes/applicationContext.xml"
	// });

	// private DiskHelper diskHelper;

	//
	// private DirHelper dirHelper;

	// private com.james.main.LoadMain loadMain;

	// private WalletHelper walletHelper;

	// private DirDao dao;

	protected void setUp() throws Exception {
		// dao = (DirDao) CONTEXT.getBean("dirDao");
		// walletHelper = (WalletHelper) CONTEXT.getBean("walletHelper");
		// diskHelper = (DiskHelper) CONTEXT.getBean("diskHelper");
		// dirHelper = (DirHelper) CONTEXT.getBean("dirHelper");
		// loadMain = (com.james.main.LoadMain) CONTEXT.getBean("loadMain");
	}

	public void testGetParent() throws SQLException {
		// ConnectionFactory factory = new DataSourceConnectionFactory(
		// (DataSource) CONTEXT.getBean("dataSource"));

		// Connection cxn = factory.createConnection();
		//
		// Statement stmt = cxn.createStatement();
		//
		// stmt.execute("select * from test");

		// Disk disk = new Disk();
		// disk.setLabel("HibernateDirDaoTest");
		//
		// Wallet wallet = new Wallet();
		// wallet.setDescription("HibernateDirDaoTest");
		// wallet.add(disk);
		//
		// Dir root = loadMain.loadDisk(new File("c:\\test"), wallet, disk);
		//
		// disk.setRoot(root);
		// diskHelper.save(disk);
		// walletHelper.save(wallet);
		//
		// Dir child = (Dir) root.getDirs().iterator().next();
		//
		// Dir found = dirHelper.load(child.getId());
		//
		// assertEquals(found.getParent(), root);
	}
}
