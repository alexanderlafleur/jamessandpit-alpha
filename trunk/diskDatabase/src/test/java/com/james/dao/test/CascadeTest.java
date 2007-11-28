/*
 * Created on Mar 13, 2005
 */
package com.james.dao.test;

import java.util.Iterator;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.james.fileItems.Dir;
import com.james.fileItems.Disk;
import com.james.fileItems.Wallet;
import com.james.helper.DirHelper;
import com.james.helper.DiskFileHelper;
import com.james.helper.DiskHelper;
import com.james.helper.WalletHelper;
import com.james.ui.dto.DiskDTO;
import com.james.ui.dto.WalletDTO;

/**
 * @author James
 */
public class CascadeTest extends BaseTestCase {

	protected static final ApplicationContext CONTEXT = new ClassPathXmlApplicationContext(
			new String[] { "applicationContext.xml" });

	private DirHelper dirHelper;

	private DiskFileHelper diskFileHelper;

	private DiskHelper diskHelper;

	private WalletHelper walletHelper;

	private void assertNoDirsLoaded(Wallet wallet) {
		for (Iterator d = wallet.getDisks().iterator(); d.hasNext();) {
			Disk disk = (Disk) d.next();

			if (disk.getRoot() != null) {
				Dir root = disk.getRoot();

				assertTrue(root.getDirs().size() == 0);
			}
		}
	}

	private Dir createTree() {
		// Create Root
		Dir root = new Dir();
		root.setName("root" + System.currentTimeMillis());
		dirHelper.create(root);

		// Create rest of tree
		Dir dir1 = new Dir();
		dir1.setName("dir1");
		dir1.setParent(root);
		dirHelper.create(dir1);

		Dir dir2 = new Dir();
		dir2.setName("dir2");
		dir2.setParent(root);
		dirHelper.create(dir2);

		root.addDir(dir1);
		root.addDir(dir2);

		Dir dir1_1 = new Dir();
		dir1_1.setName("dir1_1");
		dir1_1.setParent(dir1);
		dirHelper.create(dir1_1);

		dir1.addDir(dir1_1);
		dirHelper.update(dir1);

		Dir dir1_2 = new Dir();
		dir1_2.setName("dir1_2");
		dir1_2.setParent(dir1);
		dirHelper.create(dir1_2);

		Dir dir1_1_1 = new Dir();
		dir1_1_1.setName("dir1_1_1");
		dir1_1_1.setParent(dir1_1);
		dirHelper.create(dir1_1_1);

		dir1_1.addDir(dir1_1_1);
		dirHelper.update(dir1_1);

		dirHelper.update(root);

		return root;
	}

	public void test() {

		walletHelper = (WalletHelper) CONTEXT.getBean("walletHelper");
		diskHelper = (DiskHelper) CONTEXT.getBean("diskHelper");
		dirHelper = (DirHelper) CONTEXT.getBean("dirHelper");
		diskFileHelper = (DiskFileHelper) CONTEXT.getBean("diskFileHelper");

		WalletDTO wallet = new WalletDTO();
		wallet.setDescription("CascadeTestWallet");
		safeCreate(wallet);

		DiskDTO disk = new DiskDTO();
		disk.setLabel("CascadeTestDisk");
		safeCreate(disk);

		// Assign disk to wallet
		wallet.addDisk(disk);

		// Create tree
		Dir root = createTree();

		// Assign root to disk
		// disk.setRoot(root);

		walletHelper.update(wallet);

		Wallet loadedWallet = walletHelper.load(Long.parseLong(wallet.getId()));

		assertNoDirsLoaded(loadedWallet);

	}
}
