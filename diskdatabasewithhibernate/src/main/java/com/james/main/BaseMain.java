package com.james.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.james.helper.DirHelper;
import com.james.helper.DiskFileHelper;
import com.james.helper.DiskHelper;
import com.james.helper.WalletHelper;
import com.james.ui.dto.DirDTO;
import com.james.ui.dto.DiskDTO;
import com.james.ui.dto.DiskFileDTO;
import com.james.ui.dto.MP3DTO;
import com.james.ui.dto.WalletDTO;

public abstract class BaseMain {
    protected static final ApplicationContext CONTEXT = new ClassPathXmlApplicationContext(new String[] { "applicationContext.xml", "applicationContextMain.xml" });

    // private Log logger = LogFactory.getLog(this.getClass());

    private DirHelper dirHelper;

    private WalletHelper walletHelper;

    private DiskFileHelper diskFileHelper;

    public DirHelper getDirHelper() {
        return dirHelper;
    }

    public void setDirHelper(DirHelper dirHelper) {
        this.dirHelper = dirHelper;
    }

    public void setDiskFileHelper(DiskFileHelper diskFileHelper) {
        this.diskFileHelper = diskFileHelper;
    }

    public void setDiskHelper(DiskHelper diskHelper) {
        this.diskHelper = diskHelper;
    }

    public DiskFileHelper getDiskFileHelper() {
        return diskFileHelper;
    }

    public DiskHelper getDiskHelper() {
        return diskHelper;
    }

    private DiskHelper diskHelper;

    protected String getInput(String msg) {
        System.out.println(msg);

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        while (true) {

            try {
                String b = in.readLine();

                return b.trim();
            } catch (IOException e) {
                System.err.append(e.getMessage());
            }
        }
    }

    protected String getInput(String msg, BufferedReader in) throws IOException {
        System.out.println(msg);
        String value = in.readLine();
        if (value == null || value.trim().length() == 0) {
            return null;
        }

        return value.trim();
    }

    public boolean getBooleanInput(String msg) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.println(msg);

            String value;
            try {
                value = reader.readLine();

            } catch (IOException e) {
                System.err.println("Invalid response");
                continue;
            }

            if (value == null || value.trim().length() == 0) {
                System.err.println("Invalid response " + value);
                continue;
            }

            return parseBooleanQuestion(value);
        }
    }

    private boolean parseBooleanQuestion(String value) {
        return ("true".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value) || "y".equalsIgnoreCase(value));
    }

    public abstract Log getLogger();

    protected Long getLongInput(String msg, BufferedReader in) throws IOException {
        String value = getInput(msg, in);

        if (value == null) {
            return null;
        }
        return new Long(value);
    }

    private void print(DirDTO dir) {
        getLogger().info(dir);

        for (Iterator d = dir.getDirs().iterator(); d.hasNext();) {
            DirDTO childDir = (DirDTO) d.next();

            print(childDir);
        }

        for (Iterator f = dir.getFiles().iterator(); f.hasNext();) {
            DiskFileDTO childFile = (DiskFileDTO) f.next();

            print(childFile);
        }

        for (Iterator f = dir.getMp3s().iterator(); f.hasNext();) {
            MP3DTO childFile = (MP3DTO) f.next();

            print(childFile);
        }
    }

    public void print(DirDTO dir, String indent) {
        getLogger().info(indent + dir);
        if (dir == null) {
            return;
        }

        // getDirHelper().loadChildren(Long.parseLong(dir.getId()), false);

        for (Iterator d = dir.getDirs().iterator(); d.hasNext();) {
            DirDTO childDir = (DirDTO) d.next();

            print(childDir, indent + "\t");
        }

        for (Iterator f = dir.getFiles().iterator(); f.hasNext();) {
            DiskFileDTO childFile = (DiskFileDTO) f.next();

            print(childFile, indent + "\t");
        }

        for (Iterator f = dir.getMp3s().iterator(); f.hasNext();) {
            MP3DTO childFile = (MP3DTO) f.next();

            print(childFile, indent + "\t");
        }
    }

    protected void print(DiskDTO disk) {
        getLogger().info(disk);

        DirDTO root = disk.getRoot();

        print(root);

    }

    private void print(DiskFileDTO file) {
        getLogger().info(file);
    }

    public void print(DiskFileDTO file, String indent) {
        getLogger().info(indent + file);
    }

    private void print(MP3DTO mp3) {
        getLogger().info(mp3);
    }

    public void print(MP3DTO file, String indent) {
        getLogger().info(indent + file);
    }

    // private void print(WalletDTO wallet) {
    // getLogger().info(wallet);
    //
    // for (Iterator d = wallet.getDisks().iterator(); d.hasNext();) {
    // DiskDTO disk = (DiskDTO) d.next();
    //
    // print(disk);
    // }
    // }

    protected void printDirs(List dirs) {
        getLogger().info("Dirs");

        for (Iterator d = dirs.iterator(); d.hasNext();) {
            DirDTO dir = (DirDTO) d.next();

            print(dir);
        }
    }

    public void printDisk(DiskDTO disk, boolean children) {
        getLogger().info("\t" + disk.getLabel());

        if (children) {
            DirDTO root = getDiskHelper().loadRoot(disk.getId());

            print(root, "\t\t");

        }
    }

    protected void printDisks(List disks) {
        getLogger().info("Disks");

        for (Iterator d = disks.iterator(); d.hasNext();) {
            DiskDTO disk = (DiskDTO) d.next();

            print(disk);
        }
    }

    protected void printDisks(Set disks, boolean children) {
        for (Iterator i = disks.iterator(); i.hasNext();) {
            DiskDTO disk = (DiskDTO) i.next();

            printDisk(disk, children);

        }
    }

    // protected void printDisks(String walletId) {
    // Set disks = diskHelper.loadAll(walletId);
    //
    // for (Iterator i = disks.iterator(); i.hasNext();) {
    // DiskDTO disk = (DiskDTO) i.next();
    //
    // System.out.println(disk.getId() + " " + disk.getLabel());
    // }
    // }

    protected void printFiles(List files) {
        getLogger().info("Files");
        for (Iterator d = files.iterator(); d.hasNext();) {
            DiskFileDTO file = (DiskFileDTO) d.next();

            print(file);
        }
    }

    protected void printMP3s(List mp3s) {
        getLogger().info("MP3s");

        for (Iterator i = mp3s.iterator(); i.hasNext();) {
            MP3DTO mp3 = (MP3DTO) i.next();

            print(mp3);
        }
    }

    public void printWallet(WalletDTO wallet, boolean children) {
        getLogger().info(wallet.getDescription());

        if (children) {
            Set disks = getDiskHelper().loadAll(wallet.getId());

            for (Iterator d = disks.iterator(); d.hasNext();) {
                DiskDTO disk = (DiskDTO) d.next();

                printDisk(disk, children);
            }
        }
    }

    // private void printWallets(List wallets) {
    // getLogger().info("Wallets");
    //
    // for (Iterator i = wallets.iterator(); i.hasNext();) {
    // WalletDTO wallet = (WalletDTO) i.next();
    //
    // System.out.println(wallet.getId() + " " + wallet.getDescription());
    // }
    // }

    public void printWallets(List wallets, boolean children) {
        for (Iterator i = wallets.iterator(); i.hasNext();) {
            WalletDTO wallet = (WalletDTO) i.next();

            printWallet(wallet, children);
        }
    }

    public WalletHelper getWalletHelper() {
        return walletHelper;
    }

    public void setWalletHelper(WalletHelper walletHelper) {
        this.walletHelper = walletHelper;
    }

}
