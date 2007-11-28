/*
 * Created on Mar 13, 2005
 *
 */
package com.james.fileItems.test;

import org.springframework.dao.DataIntegrityViolationException;

import com.james.dao.test.BaseTestCase;
import com.james.ui.dto.DiskDTO;
import com.james.ui.dto.WalletDTO;

/**
 * @author James
 */
public class WalletTest extends BaseTestCase {

	public void testCreateDupliateFail() {
		String label = "testDupliateFail";

		WalletDTO wallet = new WalletDTO();
		wallet.setDescription(label);

		safeCreate(wallet);

		wallet = new WalletDTO();
		wallet.setDescription(label);

		try {
			getWalletHelper().create(wallet);
			fail("Didn't detect duplicate");

		} catch (DataIntegrityViolationException e) {
			// ok
		}
	}

	public void testCreateNewSimple() {
		WalletDTO wallet = new WalletDTO();
		wallet.setDescription("testCreateNewSimple"
				+ System.currentTimeMillis());

		getWalletHelper().create(wallet);
	}

	public void testCreateNewWithDisks() {
		String label = "testCreateNewWithDisks";

		WalletDTO wallet = new WalletDTO();
		wallet.setDescription(label);

		safeCreate(wallet);

		DiskDTO disk1 = new DiskDTO();
		disk1.setLabel("disk1");

		wallet.addDisk(disk1);

		getWalletHelper().create(wallet);

	}

	public void testDelete() {
	}

	public void testUpdate() {

	}

	// private void checkWallets() throws SQLException, NotFoundException {
	// WalletDao walletDAO = HDaoFactory.newWalletDao();
	// List wallets = walletDAO.getWallets();
	//
	// List disks;
	//
	// for (Iterator i = wallets.iterator(); i.hasNext();) {
	// Wallet wallet = (Wallet) i.next();
	//
	// disks = wallet.getDisks();
	//
	// assertTrue(disks.size() == wallet.getDisks().size());
	//
	// Disk disk;
	// for (Iterator j = disks.iterator(); j.hasNext();) {
	// disk = (Disk) j.next();
	//
	// disk.delete();
	// }
	// }
	// }

}