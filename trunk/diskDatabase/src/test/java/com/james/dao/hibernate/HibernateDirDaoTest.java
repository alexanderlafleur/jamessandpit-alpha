package com.james.dao.hibernate;

import java.io.File;

import junit.framework.TestCase;

import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.james.dao.DirDao;
import com.james.fileItems.Dir;
import com.james.helper.DirHelper;
import com.james.helper.DiskHelper;
import com.james.helper.WalletHelper;
import com.james.main.LoadMain;
import com.james.ui.dto.DirDTO;
import com.james.ui.dto.DiskDTO;
import com.james.ui.dto.WalletDTO;

public class HibernateDirDaoTest extends TestCase {
	private DirDao dao;

	private WalletHelper walletHelper;

	private DirHelper dirHelper;

	private DiskHelper diskHelper;

	private LoadMain loadMain;

	protected static final FileSystemXmlApplicationContext CONTEXT = new FileSystemXmlApplicationContext(
			new String[] { "src/test/java/com/james/dao/hibernate/applicationContextTest.xml" });

	protected void setUp() throws Exception {
		dao = (DirDao) CONTEXT.getBean("dirDao");
		walletHelper = (WalletHelper) CONTEXT.getBean("walletHelper");
		diskHelper = (DiskHelper) CONTEXT.getBean("diskHelper");
		dirHelper = (DirHelper) CONTEXT.getBean("dirHelper");
		loadMain = (LoadMain) CONTEXT.getBean("loadMain");
	}

	public void testGetParent() {
		DiskDTO disk = new DiskDTO();
		disk.setLabel("HibernateDirDaoTest");

		WalletDTO wallet = new WalletDTO();
		wallet.setDescription("HibernateDirDaoTest");
		wallet.addDisk(disk);

		DirDTO root = loadMain.loadDisk(new File("c:\\test"), wallet, disk);

		disk.setRoot(root);
		diskHelper.create(disk);
		walletHelper.create(wallet);

		Dir child = (Dir) root.getDirs().iterator().next();

		Dir found = dirHelper.load(child.getId());

		assertEquals(found.getParent(), root);
	}
}
