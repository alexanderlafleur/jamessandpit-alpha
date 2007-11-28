package com.james.ui.swing.main;

import com.james.ui.dto.DiskFileDTO;
import com.james.ui.swing.node.BaseNodeHelper;
import com.james.ui.swing.node.FileInfo;

public class FileInfoWrapper extends BaseInfoWrapper {

    public FileInfoWrapper(String id, String name, DiskFileDTO fileDTO, BaseNodeHelper nodeHelper) {
        super(new FileInfo(id, name, fileDTO, nodeHelper), nodeHelper);
    }

    @Override
    public String toString() {
        return getInfo().toString();
    }

    // private WalletInfo convertParent(DirDTO dto) {
    // if (dto.getParent() == null) {
    // DiskDTO disk = getHelper().findDisk(dto);
    //
    // return convertParent(disk);
    //
    // } else {
    // return convertParent(dto.getParent());
    // }
    // }
    //
    // private WalletInfo convertParent(DiskDTO disk) {
    // WalletDTO dto = getHelper().findWallet(disk);
    //
    // return new WalletInfo(dto.getId(), dto.getDescription(), getHelper(),
    // dto);
    // }
    //
    // private WalletInfo convertParent(DiskFileDTO dto) {
    // return convertParent(dto.getParent());
    // }
    //
    // public WalletInfo convertToPartialTree() {
    // FileInfo file = (FileInfo) getInfo();
    //
    // DiskFileDTO dto = file.getDto();
    //
    // return convertParent(dto);
    // }

}
