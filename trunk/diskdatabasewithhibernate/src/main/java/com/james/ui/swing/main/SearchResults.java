package com.james.ui.swing.main;

import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.TreeSelectionEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.james.ui.swing.node.BaseNodeHelper;

public class SearchResults extends JPanel {

    // private static String lineStyle = "Angeld";

    // private static boolean playWithLineStyle = true;
    protected final Log logger = LogFactory.getLog(getClass());

    private static final long serialVersionUID = -1L;

    private SearchController controller;

    private JList list = new JList();

    // private JTree tree = new JTree();

    public SearchResults(SearchController controller, BaseNodeHelper baseNodeHelper) {
        this.controller = controller;

        setLayout(new GridLayout());

        list.addListSelectionListener(controller);

        add(list);
    }

    // private DefaultMutableTreeNode createTreeNodes(WalletInfo node) {
    //
    // // WalletDTO dto = node.getWalletDTO();
    // //
    // // List disks = dto.getDisks();
    // //
    // // for (Iterator i = disks.iterator(); i.hasNext();) {
    // // DiskInfo disk = (DiskInfo) i.next();
    // // createTreeNodes(disk);
    // // }
    //
    // DefaultMutableTreeNode walletNode = new DefaultMutableTreeNode(node);
    //
    // return walletNode;
    // }

    public SearchController getController() {
        return controller;
    }

    public void setResults(Vector infoList) {
        logger.info("Setting results " + infoList.size());

        list.removeAll();
        list.clearSelection();

        list.setListData(infoList);

        // infoList.clear();
    }

    // /**
    // * These wallets are only populated with nodes that have results
    // */
    // public void setResults(List wallets) {
    // DefaultMutableTreeNode root = new DefaultMutableTreeNode();
    //
    // for (Iterator i = wallets.iterator(); i.hasNext();) {
    // WalletInfo walletNode = (WalletInfo) i.next();
    //
    // DefaultMutableTreeNode walletTreeNode = createTreeNodes(walletNode);
    //
    // root.add(walletTreeNode);
    // }
    //
    // DefaultTreeModel model = new DefaultTreeModel(root, true);
    //
    // tree.setModel(model);
    // }

    public void setSearchController(SearchController controller) {
        this.controller = controller;
    }

    public void valueChanged(TreeSelectionEvent e) {

    }

    // public void valueChanged(ListSelectionEvent e) {
    // JList list = (JList) e.getSource();
    //
    // BaseInfo info = (BaseInfo) list.getSelectedValue();
    // if (info != null) {
    // getController().showDetail(info);
    // }
    // }
    // public void setFileResults(List newResults) {
    // List listData = new ArrayList();
    //
    // setFileResults(newResults, listData);
    // }

    // public void setFileResults(List newResults, List listData) {
    //
    // for (Iterator i = newResults.iterator(); i.hasNext();) {
    // DiskFileDTO file = (DiskFileDTO) i.next();
    //
    // listData.add(new FileInfo(file.getId(), file.getName(), file,
    // getController().getHelper()));
    // }
    //
    // results.setListData(listData.toArray());
    //
    // }

    // public void setMP3Results(List newResults) {
    // List listData = new ArrayList();
    //
    // setMP3Results(newResults, listData);
    // }

    // public void setMP3Results(List newResults, List listData) {
    //
    // for (Iterator i = newResults.iterator(); i.hasNext();) {
    // MP3DTO mp3 = (MP3DTO) i.next();
    //
    // listData.add(new MP3Info(mp3.getId(), mp3.getName(), mp3,
    // getController().getHelper()));
    // }
    //
    // results.setListData(listData.toArray());
    //
    // }
    //
}
