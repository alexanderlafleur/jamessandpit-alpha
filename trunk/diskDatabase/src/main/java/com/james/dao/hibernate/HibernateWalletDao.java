package com.james.dao.hibernate;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.james.dao.WalletDao;
import com.james.dao.sql.NotFoundException;
import com.james.fileItems.Wallet;
import com.james.helper.DiskHelper;

public class HibernateWalletDao extends HibernateDaoSupport implements
		WalletDao {
	private DiskHelper diskHelper;

	public void create(Wallet wallet) {
		logger.debug("Creating wallet " + wallet);

		getHibernateTemplate().save(wallet);
	}

	public void delete(Wallet wallet) {
		getHibernateTemplate().delete(wallet);
	}

	public Wallet findWallet(long diskId) {
		// List results = getHibernateTemplate().find("select w from Disk d,
		// Wallet w where d.id = ?", "" + diskId);

		List results = getHibernateTemplate()
				.find(
						"select wallet from Wallet wallet where wallet.disks.id in (select disk.id from Disk disk where disk.id = ?)",
						"" + diskId);

		// "select disk from Disk disk where disk.id not in (select
		// wallet.disks.id from Wallet wallet)");

		Wallet wallet;

		if (results.size() == 0) {
			throw new NotFoundException("Unable to find Disk with diskId of: "
					+ diskId);

		} else if (results.size() == 1) {
			wallet = (Wallet) results.get(0);

		} else {
			throw new NotFoundException("More than one disk found with id of: "
					+ diskId);

		}

		return wallet;
	}

	public DiskHelper getDiskHelper() {
		return diskHelper;
	}

	public List load() {
		List wallets = getHibernateTemplate().loadAll(Wallet.class);

		return wallets;
	}

	public Wallet load(long walletId) {
		return (Wallet) getHibernateTemplate().load(Wallet.class,
				new Long(walletId));
	}

	public Wallet loadByDescription(String description) {
		List results = getHibernateTemplate().find(
				"from Wallet where description = ?", description);

		Wallet wallet;

		if (results.size() == 0) {
			throw new NotFoundException(
					"Unable to find wallet with description of: " + description);

		} else if (results.size() == 1) {
			wallet = (Wallet) results.get(0);

		} else {
			throw new NotFoundException(
					"More than one wallet found with description of: "
							+ description);

		}

		return wallet;
	}

	public List search(String description) {
		List results = getHibernateTemplate().find(
				"from Wallet where description like ?", description);
		return results;
	}

	public void setDiskHelper(DiskHelper diskHelper) {
		this.diskHelper = diskHelper;
	}

	public void update(Wallet wallet) {
		logger.debug("Updating wallet " + wallet);

		getHibernateTemplate().update(wallet);
	}
}