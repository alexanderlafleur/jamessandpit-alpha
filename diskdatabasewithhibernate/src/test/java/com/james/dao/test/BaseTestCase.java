package com.james.dao.test;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.james.fileItems.Dir;
import com.james.fileItems.Wallet;
import com.james.helper.DirHelper;
import com.james.helper.DiskHelper;
import com.james.helper.WalletHelper;
import com.james.ui.dto.DirDTO;
import com.james.ui.dto.DiskDTO;
import com.james.ui.dto.DiskFileDTO;
import com.james.ui.dto.WalletDTO;

public class BaseTestCase extends TestCase {
    protected static final ApplicationContext CONTEXT = new ClassPathXmlApplicationContext(new String[] { "applicationContext.xml" });

    private DirHelper dirHelper;

    private DiskHelper diskHelper;

    protected Session session;

    protected SessionFactory sessionFactory;

    private WalletHelper walletHelper;

    public BaseTestCase() {
        walletHelper = (WalletHelper) CONTEXT.getBean("walletHelper");
        diskHelper = (DiskHelper) CONTEXT.getBean("diskHelper");
        dirHelper = (DirHelper) CONTEXT.getBean("dirHelper");
    }

    public DirHelper getDirHelper() {
        return dirHelper;
    }

    public DiskHelper getDiskHelper() {
        return diskHelper;
    }

    public WalletHelper getWalletHelper() {
        return walletHelper;
    }

    public void print(DirDTO dir, String tab) {
        System.out.println(tab + dir.getName() + " - " + dir.getId() + " (" + (dir.getParent() == null ? "" : dir.getParent().getName()) + ")");

        for (Iterator i = dir.getDirs().iterator(); i.hasNext();) {
            DirDTO d = (DirDTO) i.next();

            print(d, tab + " ");
        }

        for (Iterator i = dir.getFiles().iterator(); i.hasNext();) {
            DiskFileDTO f = (DiskFileDTO) i.next();

            print(f, tab + " ");
        }
    }

    protected void print(DiskDTO disk) {
        System.out.println(disk.getLabel() + " - " + disk.getId());

        print(disk.getRoot(), " + ");
    }

    // public void print(Dir dir, String tab) {
    // System.out.println(tab + " " + dir.getName());
    //
    // System.out.println(tab + dir.getName() + ", "
    // + (dir.getParent() == null ? "" : dir.getParent().getName()));
    //
    // for (Iterator i = dir.getDirs().iterator(); i.hasNext();) {
    // Dir d = (Dir) i.next();
    //
    // print(d, tab + " ");
    // }
    //
    // }

    private void print(DiskFileDTO f, String string) {
        // TODO Auto-generated method stub

    }

    protected void print(WalletDTO wallet) {
        System.out.println(wallet.getDescription() + " - " + wallet.getId());

        for (Iterator i = wallet.getDisks().iterator(); i.hasNext();) {
            DiskDTO disk = (DiskDTO) i.next();

            // diskHelper.loadChildren(disk);

            print(disk);
        }
    }

    // public Wallet createWallet(String description, boolean b) {
    // List results = walletHelper.search(description);
    //
    // assertTrue(results.size() == 0 || results.size() == 1);
    //
    // if (results.size() == 1) {
    // // remove existing wallet
    // Wallet wallet = (Wallet) results.get(0);
    // walletHelper.delete(wallet);
    // }
    //
    // Wallet wallet = new Wallet();
    // wallet.setDescription(description);
    //
    // getWalletHelper().create(wallet);
    //
    // return wallet;
    // }

    public void safeCreate(DirDTO dir, Dir parent) {
        List dirs = dirHelper.search(dir.getName());

        if (dirs.size() > 0) {
            DirDTO found = (DirDTO) dirs.get(0);
            dirHelper.delete(found.getId());
        }

        dirHelper.createDTO(dir, parent);

    }

    public void safeCreate(DiskDTO disk) {
        List disks = diskHelper.search(disk.getLabel());

        if (disks.size() > 0) {
            DiskDTO found = (DiskDTO) disks.get(0);
            diskHelper.delete(found.getId());
        }

        diskHelper.create(disk);
    }

    public void safeCreate(WalletDTO wallet) {
        List wallets = walletHelper.search(wallet.getDescription());

        if (wallets.size() > 0) {
            Wallet found = (Wallet) wallets.get(0);
            walletHelper.delete(found);
        }

        walletHelper.create(wallet);

    }

    public void setDirHelper(DirHelper dirHelper) {
        this.dirHelper = dirHelper;
    }

    public void setDiskHelper(DiskHelper diskHelper) {
        this.diskHelper = diskHelper;
    }

    @Override
    protected void setUp() throws Exception {
        sessionFactory = (SessionFactory) CONTEXT.getBean("sessionFactory");
        session = SessionFactoryUtils.getSession(this.sessionFactory, true);
        TransactionSynchronizationManager.bindResource(this.sessionFactory, new SessionHolder(session));
    }

    public void setWalletHelper(WalletHelper walletHelper) {
        this.walletHelper = walletHelper;
    }

    @Override
    protected void tearDown() throws Exception {
        TransactionSynchronizationManager.unbindResource(sessionFactory);
        SessionFactoryUtils.releaseSession(session, sessionFactory);
    }
}
