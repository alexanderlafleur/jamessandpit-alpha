package com.james.helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.james.dao.WalletDao;
import com.james.fileItems.Disk;
import com.james.fileItems.Wallet;
import com.james.ui.dto.DiskDTO;
import com.james.ui.dto.WalletDTO;
import com.james.ui.swing.node.WalletInfo;

public class WalletHelper extends BaseWallet {
    private WalletDao dao;

    private DiskHelper diskHelper;

    protected final Log logger = LogFactory.getLog(getClass());

    public WalletDTO create(WalletDTO walletDTO) {
        Wallet wallet = fromDTO(walletDTO);

        dao.create(wallet);

        return toDTO(wallet, false);

    }

    public void delete(long walletId) {
        Wallet wallet = dao.load(walletId);
        dao.delete(wallet);
    }

    public void delete(Wallet wallet) {
        dao.delete(wallet);
    }

    public WalletDTO findWallet(DiskDTO disk) {
        Wallet wallet = dao.findWallet(Long.parseLong(disk.getId()));

        return toDTO(wallet, false);
    }

    private Wallet fromDTO(WalletDTO dto) {
        Wallet wallet = new Wallet();
        if (dto.getId() != null) {
            wallet.setId(Long.parseLong(dto.getId()));
        }
        wallet.setDescription(dto.getDescription());

        for (Iterator i = dto.getDisks().iterator(); i.hasNext();) {
            DiskDTO diskDTO = (DiskDTO) i.next();

            wallet.addDisk(getDiskHelper().fromDTO(diskDTO));
        }

        return wallet;
    }

    public WalletDao getDao() {
        return dao;
    }

    public DiskHelper getDiskHelper() {
        return diskHelper;
    }

    public List<WalletDTO> load() {
        return load(false);
    }

    public List<WalletDTO> load(boolean children) {
        List<Wallet> wallets = dao.load();

        List<WalletDTO> dtos = new ArrayList<WalletDTO>();

        for (Iterator i = wallets.iterator(); i.hasNext();) {
            Wallet wallet = (Wallet) i.next();

            dtos.add(toDTO(wallet, children));
        }

        return dtos;
    }

    public Wallet load(long walletId) {
        Wallet wallet = dao.load(walletId);

        return wallet;
    }

    public WalletDTO loadByName(String walletName, boolean children) {
        Wallet wallet = dao.loadByDescription(walletName);

        return toDTO(wallet, children);
    }

    public WalletDTO loadDTO(String id, boolean children) {
        Wallet wallet = load(Long.parseLong(id));

        return toDTO(wallet, children);
    }

    public WalletDTO populateFromFields(WalletInfo info) {
        Map fields = info.getFields();

        WalletDTO dto = new WalletDTO();

        for (Iterator i = fields.keySet().iterator(); i.hasNext();) {
            String key = (String) i.next();

            if (WalletInfo.FIELD_DESCRIPTION.equals(key)) {
                dto.setDescription((String) fields.get(key));
            } else {
                logger.error("Unknown field: " + key);
            }

        }

        return dto;
    }

    public List search(String label) {
        List wallets = dao.search(label);

        List<WalletDTO> dtos = new ArrayList<WalletDTO>(wallets.size());

        for (Iterator i = dtos.iterator(); i.hasNext();) {
            Wallet wallet = (Wallet) i.next();

            dtos.add(toDTO(wallet, false));
        }
        return dtos;
    }

    public void setDao(WalletDao dao) {
        this.dao = dao;
    }

    public void setDiskHelper(DiskHelper diskHelper) {
        this.diskHelper = diskHelper;
    }

    private WalletDTO toDTO(Wallet wallet, boolean children) {
        WalletDTO dto = new WalletDTO();
        dto.setId(String.valueOf(wallet.getId()));
        dto.setDescription(wallet.getDescription());

        if (children) {
            for (Iterator i = wallet.getDisks().iterator(); i.hasNext();) {
                Disk disk = (Disk) i.next();

                dto.addDisk(getDiskHelper().toDTO(disk, false));
            }
        }

        return dto;
    }

    public void update(WalletDTO dto) {
        dao.update(fromDTO(dto));
    }

    // public void verify(Wallet wallet) throws
    // InvalidFileItemException {
    // if (wallet == null) {
    // return;
    // }
    //
    // for (Iterator i = wallet.getDisks().iterator(); i.hasNext();) {
    // Disk disk = (Disk) i.next();
    //
    // getDiskHelper().verify(disk);
    // }
    // }
}