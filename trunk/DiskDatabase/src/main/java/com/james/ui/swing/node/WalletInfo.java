package com.james.ui.swing.node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.tree.DefaultMutableTreeNode;

import com.james.ui.dto.DiskDTO;
import com.james.ui.dto.WalletDTO;
import com.james.ui.swing.main.DetailPane;

public class WalletInfo extends BaseInfo {

    public static final String FIELD_DESCRIPTION = "Description";

    private static final long serialVersionUID = -1L;

    private WalletDTO walletDTO;

    public WalletInfo(String id, String label, BaseNodeHelper helper, WalletDTO walletDTO) {
        super(id, label, helper);
        this.walletDTO = walletDTO;

        createFields(walletDTO);
        createButtons();
    }

    private void createButtons() {
        getButtons().clear();

        DataButton save = new DataButton(SaveHelper.SAVE_WALLET, this);
        save.addActionListener(getHelper().getSaveHelper());

        getButtons().add(save);

    }

    private void createFields(WalletDTO wallet) {
        getFields().put(FIELD_DESCRIPTION,
                new FormField(FIELD_DESCRIPTION, wallet.getDescription()));
    }

    public WalletDTO getDto() {
        return walletDTO;
    }

    public List loadChildren() {
        List disks = getHelper().loadDisks(walletDTO.getId(), true);

        List diskNodes = new ArrayList();

        for (Iterator i = disks.iterator(); i.hasNext();) {
            DiskDTO diskDTO = (DiskDTO) i.next();

            DefaultMutableTreeNode node = new DefaultMutableTreeNode(new DiskInfo(diskDTO.getId(),
                    diskDTO.getLabel(), diskDTO, getHelper()));

            diskNodes.add(node);
        }

        return diskNodes;
    }

    public void setWalletDTO(WalletDTO walletDTO) {
        this.walletDTO = walletDTO;
    }

    public void show(DetailPane detail) {
        detail.removeAll();
        JComponent predecessor = null;

        for (Iterator i = getFields().values().iterator(); i.hasNext();) {
            FormField field = (FormField) i.next();

            predecessor = getLayoutHelper().layoutField(field.getLabel(), field.getField(),
                    predecessor, detail);
        }
        for (Iterator i = getButtons().iterator(); i.hasNext();) {
            JButton button = (JButton) i.next();

            predecessor = getLayoutHelper().layoutButton(button, predecessor, detail);
        }
    }
}
