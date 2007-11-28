package com.james.ui.swing.node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import com.james.helper.DirHelper;
import com.james.helper.DiskHelper;
import com.james.helper.WalletHelper;
import com.james.ui.dto.DirDTO;
import com.james.ui.dto.DiskDTO;
import com.james.ui.dto.WalletDTO;

public class BaseNodeHelper {
    private DirHelper dirHelper;

    private DiskHelper diskHelper;

    private SaveHelper saveHelper;

    private WalletHelper walletHelper;

    // public String createHierarchyString(FileInfo info) {
    // return createHierarchyString(info.getDto());
    // }
    //
    // private String createHierarchyString(DiskFileDTO file) {
    // return createHierarchyString(file.getParent()) + file.getName() + "/";
    // }
    //
    // private String createHierarchyString(DirDTO dir) {
    // if (dir.getParent() == null) {
    // DiskDTO disk = getDiskHelper().findDisk(dir);
    //
    // return createHierarchyString(disk) + disk.getLabel() + "("
    // + disk.getMediaNumber() + ")/";
    // } else {
    // return createHierarchyString(dir.getParent()) + dir.getName() + "/";
    // }
    // }
    //
    // private String createHierarchyString(DiskDTO disk) {
    // WalletDTO wallet = getWalletHelper().findWallet(disk);
    //
    // return wallet.getDescription() + "/";
    // }

    public DiskDTO findDisk(DirDTO dto) {
        return getDiskHelper().findDisk(dto);
    }

    public WalletDTO findWallet(DiskDTO disk) {
        return getWalletHelper().findWallet(disk);
    }

    public DirHelper getDirHelper() {
        return dirHelper;
    }

    public DiskHelper getDiskHelper() {
        return diskHelper;
    }

    public SaveHelper getSaveHelper() {
        return saveHelper;
    }

    public WalletHelper getWalletHelper() {
        return walletHelper;
    }

    public void loadChildren(DirDTO root) {
        getDirHelper().readFiles(root);
        getDirHelper().readMP3s(root);
        getDirHelper().readDirs(root);
    }

    public DirDTO loadDir(DirDTO root) {
        // TODO Auto-generated method stub
        return null;
    }

    public List loadDisks(String walletId, boolean children) {
        WalletDTO walletDTO = getWalletHelper().loadDTO(walletId, children);

        List disks = walletDTO.getDisks();

        return disks;
    }

    public DirDTO loadRoot(String diskId) {
        return getDiskHelper().loadRoot(diskId);
    }

    public List loadWallets() {
        DefaultMutableTreeNode wallet;

        List wallets = walletHelper.load(false);

        List walletNodes = new ArrayList();

        for (Iterator i = wallets.iterator(); i.hasNext();) {
            WalletDTO walletDTO = (WalletDTO) i.next();

            wallet = new DefaultMutableTreeNode(new WalletInfo(walletDTO.getId(), walletDTO.getDescription(), this, walletDTO));

            walletNodes.add(wallet);
        }

        return walletNodes;
    }

    public void setDirHelper(DirHelper dirHelper) {
        this.dirHelper = dirHelper;
    }

    public void setDiskHelper(DiskHelper diskHelper) {
        this.diskHelper = diskHelper;
    }

    public void setSaveHelper(SaveHelper saveHelper) {
        this.saveHelper = saveHelper;
    }

    public void setWalletHelper(WalletHelper walletHelper) {
        this.walletHelper = walletHelper;
    }

}
