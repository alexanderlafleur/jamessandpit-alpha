package com.james.main;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.james.fileItems.Disk;
import com.james.helper.DirHelper;
import com.james.helper.DiskHelper;
import com.james.helper.WalletHelper;
import com.james.ui.dto.DirDTO;
import com.james.ui.dto.DiskDTO;
import com.james.ui.dto.WalletDTO;

public class DeleteMain extends BaseMain {

    public static void main(String args[]) {
        DeleteMain main = (DeleteMain) CONTEXT.getBean("deleteMain");

        List wallets = main.getWallets();

        main.deleteWallet(wallets);

        main.deleteOrphaned();
    }

    private DirHelper dirHelper;

    private DiskHelper diskHelper;

    private Log logger = LogFactory.getLog(this.getClass());

    private WalletHelper walletHelper;

    private void deleteOrphaned() {
        logger.info("Finding orphaned dirs.");
        List orphanedDirs = diskHelper.getOrphanedRoots();

        logger.info("Deleting " + orphanedDirs.size() + " orphaned dirs.");

        for (Iterator i = orphanedDirs.iterator(); i.hasNext();) {
            DirDTO root = (DirDTO) i.next();

            logger.info("Deleting root: " + root);

            dirHelper.delete(root.getId());
        }

        logger.info("Finding orphaned disks.");

        List orphanedDisks = diskHelper.getOrphanedDisks();

        logger.info("Deleting " + orphanedDisks.size() + " orphaned disks.");

        for (Iterator i = orphanedDisks.iterator(); i.hasNext();) {
            Disk disk = (Disk) i.next();

            logger.info("Deleting disk: " + disk.getLabel());

            diskHelper.delete(disk);
        }
    }

    private void deleteWallet(List wallets) {
        if (wallets == null || wallets.size() == 0) {
            System.out.println("No wallets");
            return;
        }

        printWallets(wallets, false);

        String walletName = getInput("Choose a wallet to delete or hit return to just clean up orphaned records.");

        if (walletName.length() == 0) {
            deleteOrphaned();

        } else {
            WalletDTO wallet = getWalletHelper().loadByName(walletName, false);

            Set disks = getDiskHelper().loadAll(wallet.getId());

            printDisks(disks, false);

            String diskName = getInput("Enter disk name to delete or enter for all.");

            if (diskName.length() == 0) {
                walletHelper.delete(Long.parseLong(wallet.getId()));
            } else {
                DiskDTO disk = diskHelper.loadByName(diskName, false);
                diskHelper.delete(disk.getId());
            }
        }
    }

    @Override
    public DirHelper getDirHelper() {
        return dirHelper;
    }

    @Override
    public DiskHelper getDiskHelper() {
        return diskHelper;
    }

    @Override
    public Log getLogger() {
        return logger;
    }

    @Override
    public WalletHelper getWalletHelper() {
        return walletHelper;
    }

    // private void deleteAllWallets(List wallets) {
    // for (Iterator i = wallets.iterator(); i.hasNext();) {
    //
    // Wallet wallet = (Wallet) i.next();
    // log.info("Deleting wallet " + wallet.getDescription() + " ("
    // + wallet.getId() + ")");
    // walletHelper.delete(wallet);
    // }
    // }

    private List getWallets() {
        List wallets = walletHelper.load();

        return wallets;
    }

    @Override
    public void setDirHelper(DirHelper dirHelper) {
        this.dirHelper = dirHelper;
    }

    @Override
    public void setDiskHelper(DiskHelper diskHelper) {
        this.diskHelper = diskHelper;
    }

    @Override
    public void setWalletHelper(WalletHelper walletHelper) {
        this.walletHelper = walletHelper;
    }
}