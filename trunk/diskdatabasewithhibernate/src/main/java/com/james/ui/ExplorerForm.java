package com.james.ui;

import java.util.Collections;
import java.util.List;

import com.james.ui.dto.DirDTO;
import com.james.ui.dto.DiskDTO;
import com.james.ui.dto.DiskFileDTO;
import com.james.ui.dto.WalletDTO;

public class ExplorerForm {
    private Action action;

    private DirDTO dir;

    private List dirs;

    private DiskDTO disk;

    private List disks;

    private DiskFileDTO file;

    private List files;

    private DirDTO parent;

    private WalletDTO wallet;

    public Action getAction() {
        return action;
    }

    public DirDTO getDir() {
        return dir;
    }

    public List getDirs() {
        return dirs;
    }

    public DiskDTO getDisk() {
        return disk;
    }

    public List getDisks() {
        return disks;
    }

    public DiskFileDTO getFile() {
        return file;
    }

    public List getFiles() {
        return files;
    }

    public DirDTO getParent() {
        return parent;
    }

    public WalletDTO getWallet() {
        return wallet;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setDir(DirDTO dir) {
        this.dir = dir;
    }

    public void setDirs(List dirs) {
        Collections.sort(dirs, new DirComparator());

        this.dirs = dirs;
    }

    public void setDisk(DiskDTO disk) {
        this.disk = disk;
    }

    public void setDisks(List disks) {
        this.disks = disks;
    }

    public void setFile(DiskFileDTO file) {
        this.file = file;
    }

    public void setFiles(List files) {
        Collections.sort(files, new FileComparator());

        this.files = files;
    }

    public void setParent(DirDTO parent) {
        this.parent = parent;
    }

    public void setWallet(WalletDTO wallet) {
        this.wallet = wallet;
    }
}
