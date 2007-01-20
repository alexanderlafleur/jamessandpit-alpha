package com.james.main;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.james.helper.WalletHelper;
import com.james.ui.dto.DiskDTO;
import com.james.ui.dto.WalletDTO;

public class ListMain extends BaseMain {

	public static void main(String args[]) throws IOException {
		ListMain main = (ListMain) CONTEXT.getBean("listMain");

		main.list();
	}

	private Log log = LogFactory.getLog(this.getClass());

	private WalletHelper walletHelper;

	public Log getLogger() {
		return log;
	}

	public WalletHelper getWalletHelper() {
		return walletHelper;
	}

	private void list() throws IOException {
		// Wallets
		List wallets = loadWallets();

		printWallets(wallets, false);

		String walletName = getInput("Choose wallet to print, or hit return for all");

		if (walletName.trim().length() == 0) {
			wallets = loadWallets();

			printWallets(wallets, true);

		} else {
			WalletDTO wallet = loadWallet(walletName.trim(), false);

			// wallets = new ArrayList();
			// wallets.add(wallet);

			// Disks
			Set disks = getDiskHelper().loadAll(wallet.getId());
			printDisks(disks, false);

			String diskName = getInput("Choose disk to print, or hit return for all");

			if (diskName.trim().length() == 0) {
				disks = getDiskHelper().loadAll(wallet.getId());

			} else {
				disks = new HashSet();
				DiskDTO disk = loadDisk(diskName.trim());

				disks.add(disk);
			}

			printDisks(disks, true);
		}
	}

	private DiskDTO loadDisk(String diskName) {
		return getDiskHelper().loadByName(diskName, true);
	}

	private WalletDTO loadWallet(String walletName, boolean children) {
		return getWalletHelper().loadByName(walletName, children);
	}

	public List loadWallets() {
		List wallets = walletHelper.load();

		return wallets;
	}

	public void setWalletHelper(WalletHelper walletHelper) {
		this.walletHelper = walletHelper;
	}

}