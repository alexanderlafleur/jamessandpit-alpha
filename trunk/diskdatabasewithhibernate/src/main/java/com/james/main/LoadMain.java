package com.james.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.james.dao.sql.NotFoundException;
import com.james.fileItems.Dir;
import com.james.helper.DirHelper;
import com.james.helper.DiskHelper;
import com.james.helper.InvalidFileItemException;
import com.james.helper.WalletHelper;
import com.james.ui.dto.DirDTO;
import com.james.ui.dto.DiskDTO;
import com.james.ui.dto.WalletDTO;

public class LoadMain extends BaseMain {

    public static void main(String args[]) throws InvalidFileItemException {
        LoadMain main = (LoadMain) CONTEXT.getBean("loadMain");

        WalletDTO wallet = main.chooseWallet();

        String diskName = main.getDiskName();

        long mediaNumber = main.askForMediaNumber();

        File baseDir = main.askForBaseDir();

        try {
            main.createDisk(wallet, diskName, mediaNumber, baseDir);

        } catch (Exception e) {
            System.out.println("There was an error creating disk: " + diskName + ": " + e.getMessage());
            e.printStackTrace();
            String result = main.getInput("Do you wish to delete this disk and load it again? Y/N");

            if ("Y".equalsIgnoreCase(result)) {
                main.deleteDiskByName(diskName);
            } else {
                System.out.println("Exiting");
                return;
            }
        }

    }

    private DirHelper dirHelper;

    private DiskHelper diskHelper;

    private Log logger = LogFactory.getLog(this.getClass());

    private WalletHelper walletHelper;

    private File askForBaseDir() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            String defaultBaseName = "d:";
            try {
                System.out.println("Enter base load directory e.g. Press enter for " + defaultBaseName);
                String baseName = in.readLine();

                if (baseName.trim().length() == 0) {
                    baseName = defaultBaseName;
                    // continue;
                }
                File baseDir = new File(baseName);

                return baseDir;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public long askForMediaNumber() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            try {
                System.out.println("Enter media number.");
                String sMediaNumber = in.readLine();

                try {
                    long mediaNumber = Long.parseLong(sMediaNumber);

                    try {
                        getDiskHelper().findByMediaNumber(mediaNumber);
                        // error
                        System.out.println("Media number already exists. Choose another.");
                        continue;

                    } catch (NotFoundException e) {
                        // ok
                    }

                    return mediaNumber;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid media number: " + sMediaNumber);
                    continue;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private WalletDTO chooseWallet() {
        List<WalletDTO> wallets = getWallets();

        if (wallets.size() == 0) {
            wallets = new LinkedList<WalletDTO>();
        }

        while (true) {
            int index = 0;
            Collections.sort(wallets, new WalletNameComparator());

            for (Iterator i = wallets.iterator(); i.hasNext(); index++) {
                WalletDTO wallet = (WalletDTO) i.next();
                System.out.println(wallet.getDescription());
            }

            System.out.println("=============");

            String walletName = getInput("Enter wallet name or the name of a new wallet");

            try {
                // Load wallet (and its disks as we are going update the number
                // of disks)
                WalletDTO wallet = getWalletHelper().loadByName(walletName, true);

                return wallet;

            } catch (NotFoundException e) {
                boolean createNew = getBooleanInput("Wallet " + walletName + " doesn't exist. Do you want to create it?");

                if (createNew) {
                    String desc = getWalletDesc();
                    WalletDTO wallet = new WalletDTO();
                    wallet.setDescription(desc);

                    wallet = walletHelper.create(wallet);

                    return wallet;
                } else {
                    continue;
                }

            }
        }
    }

    public void createDisk(WalletDTO wallet, String diskName, long mediaNumber, File baseDir) {
        DiskDTO disk = new DiskDTO();
        disk.setLabel(diskName);
        disk.setMediaNumber(mediaNumber);

        logger.info("Creating disk.");

        disk = diskHelper.create(disk);

        logger.info("Loading disk.");

        DirDTO root = loadDisk(baseDir, wallet, disk);

        disk.setRoot(root);

        logger.info("Updating disk.");

        diskHelper.update(disk);

        wallet.addDisk(disk);

        logger.info("Updating wallet.");

        walletHelper.update(wallet);

    }

    private void deleteDiskByName(String diskName) {
        List results = getDiskHelper().search(diskName);

        if (results.size() != 1) {
            System.err.println("Disk does not exist. Cannot delete it.");
        } else {
            DiskDTO disk = (DiskDTO) results.get(0);
            getDiskHelper().delete(disk.getId());
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

    private String getDiskName() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.println("Enter diskname:");
            String diskName;
            try {
                diskName = in.readLine();

                if (diskName.trim().length() == 0) {
                    continue;
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                continue;
            }

            if (getDiskHelper().search(diskName).size() > 0) {
                System.out.println("Disk already exists");
                continue;
            }

            return diskName;
        }
    }

    @Override
    public Log getLogger() {
        return logger;
    }

    private String getWalletDesc() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.println("Enter walletdesc:");
            String walletName;
            try {
                walletName = in.readLine();

                if (walletName.trim().length() == 0) {
                    continue;
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                continue;
            }
            return walletName;
        }
    }

    @Override
    public WalletHelper getWalletHelper() {
        return walletHelper;
    }

    private List<WalletDTO> getWallets() {
        List<WalletDTO> wallets = walletHelper.load();

        return wallets;
    }

    public DirDTO loadDisk(File baseDir, WalletDTO wallet, DiskDTO disk) {
        System.out.println("Loading disk: " + baseDir);

        Dir dir = diskHelper.read(baseDir, null);

        System.out.println("Loading disk... done");

        System.out.println("Converting Disk to DTO...");
        DirDTO dto = getDirHelper().toDTO(dir, false);

        System.out.println("Converting Disk to DTO... done");

        return dto;
    }

    public WalletDTO loadWallet(String id, boolean children) {
        return getWalletHelper().loadDTO(id, children);
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