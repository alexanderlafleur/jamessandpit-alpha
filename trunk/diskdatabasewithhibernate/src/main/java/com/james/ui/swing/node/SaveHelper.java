package com.james.ui.swing.node;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.james.helper.DirHelper;
import com.james.helper.DiskFileHelper;
import com.james.helper.DiskHelper;
import com.james.helper.MP3Helper;
import com.james.helper.WalletHelper;
import com.james.ui.dto.DirDTO;
import com.james.ui.dto.DiskDTO;
import com.james.ui.dto.DiskFileDTO;
import com.james.ui.dto.MP3DTO;
import com.james.ui.dto.WalletDTO;

public class SaveHelper implements ActionListener {
    public static final String SAVE_DIR = "Save Dir";

    static final String SAVE_DISK = "Save Disk";

    static final String SAVE_FILE = "Save File";

    static final String SAVE_MP3 = "Save MP3";

    static final String SAVE_WALLET = "Save Wallet";

    private DirHelper dirHelper;

    private DiskHelper diskHelper;

    private DiskFileHelper fileHelper;

    protected final Log logger = LogFactory.getLog(getClass());

    private MP3Helper mp3Helper;

    private WalletHelper walletHelper;

    public void actionPerformed(ActionEvent e) {
        DataButton button = (DataButton) e.getSource();

        if (SAVE_MP3.equals(e.getActionCommand())) {
            MP3Info info = (MP3Info) button.getInfo();

            MP3DTO mp3 = getMp3Helper().populateFromFields(info);

            getMp3Helper().update(mp3);

        } else if (SAVE_FILE.equals(e.getActionCommand())) {
            FileInfo info = (FileInfo) button.getInfo();

            DiskFileDTO file = getFileHelper().populateFromFields(info);

            getFileHelper().update(file);

        } else if (SAVE_DIR.equals(e.getActionCommand())) {
            DirInfo info = (DirInfo) button.getInfo();

            DirDTO dir = getDirHelper().populateFromFields(info);

            getDirHelper().update(dir);

        } else if (SAVE_DISK.equals(e.getActionCommand())) {
            DiskInfo info = (DiskInfo) button.getInfo();

            DiskDTO disk = getDiskHelper().populateFromFields(info);

            getDiskHelper().update(disk);

        } else if (SAVE_WALLET.equals(e.getActionCommand())) {
            WalletInfo info = (WalletInfo) button.getInfo();

            WalletDTO wallet = getWalletHelper().populateFromFields(info);

            getWalletHelper().update(wallet);

        } else {
            logger.info("Not updating " + e.getActionCommand());
        }

        System.out.println("Action " + e.getActionCommand());
        logger.info(e.getSource());
        logger.info(e.getActionCommand());
    }

    public DirHelper getDirHelper() {
        return dirHelper;
    }

    public DiskHelper getDiskHelper() {
        return diskHelper;
    }

    public DiskFileHelper getFileHelper() {
        return fileHelper;
    }

    public MP3Helper getMp3Helper() {
        return mp3Helper;
    }

    public WalletHelper getWalletHelper() {
        return walletHelper;
    }

    public void setDirHelper(DirHelper dirHelper) {
        this.dirHelper = dirHelper;
    }

    public void setDiskHelper(DiskHelper diskHelper) {
        this.diskHelper = diskHelper;
    }

    public void setFileHelper(DiskFileHelper fileHelper) {
        this.fileHelper = fileHelper;
    }

    public void setMp3Helper(MP3Helper mp3Helper) {
        this.mp3Helper = mp3Helper;
    }

    public void setWalletHelper(WalletHelper walletHelper) {
        this.walletHelper = walletHelper;
    }
}
