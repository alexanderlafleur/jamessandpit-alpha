package com.james.ui.swing.main;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.james.ui.swing.node.BaseInfo;
import com.james.ui.swing.node.BaseNodeHelper;
import com.james.ui.swing.node.RootInfo;

public class DiskDatabaseView extends JPanel implements TreeSelectionListener, ActionListener {

    protected static final ApplicationContext CONTEXT = new ClassPathXmlApplicationContext(new String[] { "applicationContext.xml" });

    // Optionally play with line styles. Possible values are
    // "Angled" (the default), "Horizontal", and "None".
    private static String lineStyle = "Angeld";

    private static final String MENU_COMMAND_LOAD = "Load";

    private static boolean playWithLineStyle = true;

    private static final long serialVersionUID = 1L;

    // Optionally set the look and feel.
    private static boolean useSystemLookAndFeel = false;

    /**
     * Create the GUI and show it. For thread safety, this method should be invoked from the event-dispatching thread.
     */
    private static void createAndShowGUI() {
        if (useSystemLookAndFeel) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Couldn't use system look and feel.");
            }
        }

        // Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        // Create and set up the window.
        JFrame frame = new JFrame("TreeDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create and set up the content pane.
        DiskDatabaseView view = new DiskDatabaseView(frame);

        frame.setJMenuBar(view.createMenu());

        // content panes must be opaque
        view.setOpaque(true);
        frame.setContentPane(view);

        // Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private BaseNodeHelper baseNodeHelper = (BaseNodeHelper) CONTEXT.getBean("baseNodeHelper");

    private DetailPane detailPane;

    // private DiskHelper diskHelper = (DiskHelper)
    // CONTEXT.getBean("diskHelper");

    private JFrame frame;

    private DefaultMutableTreeNode root;

    private JTree tree;

    // private WalletHelper walletHelper = (WalletHelper) CONTEXT
    // .getBean("walletHelper");

    public DiskDatabaseView(JFrame frame) {
        super(new GridLayout(0, 1));
        setFrame(frame);

        // Create the nodes.
        RootInfo rootInfo = new RootInfo("", "Root", baseNodeHelper);
        root = new DefaultMutableTreeNode(rootInfo);
        rootInfo.loadChildren();

        // loadWallets(root);

        // Create a tree that allows one selection at a time.
        tree = new JTree(root);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        // Listen for when the selection changes.
        tree.addTreeSelectionListener(this);

        if (playWithLineStyle) {
            tree.putClientProperty("JTree.lineStyle", lineStyle);
        }

        detailPane = new DetailPane(); // new JEditorPane

        // Create the scroll pane and add the tree to it.
        JScrollPane treeView = new JScrollPane(tree);
        SearchView searchView = new SearchView(detailPane, baseNodeHelper);

        JTabbedPane tab = new JTabbedPane();
        tab.addTab("Tree", treeView);
        tab.addTab("Search", searchView);

        // detailPane.setEditable(false);
        JScrollPane detailView = new JScrollPane(detailPane);

        // Add the scroll panes to a split pane.
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(tab);
        splitPane.setRightComponent(detailView);

        // Dimension minimumSize = new Dimension(100, 50);
        // detailView.setMinimumSize(minimumSize);
        // treeView.setMinimumSize(minimumSize);
        splitPane.setDividerLocation(500);

        splitPane.setPreferredSize(new Dimension(1000, 400));

        // Add the split pane to this panel.
        add(splitPane);
    }

    public void actionPerformed(ActionEvent event) {
        // JMenuItem source = (JMenuItem) (event.getSource());

        if (MENU_COMMAND_LOAD.equals(event.getActionCommand())) {
            System.out.println("Loading...");
            // loadWallets(root);

        } else {
            System.out.println("Unknown command...");

        }
    }

    public JMenuBar createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu;
        JMenuItem menuItem;

        menu = new JMenu("Tools");
        menu.setMnemonic(KeyEvent.VK_T);
        menu.getAccessibleContext().setAccessibleDescription("Useful tools.");
        menuBar.add(menu);

        menuItem = new JMenuItem(MENU_COMMAND_LOAD, KeyEvent.VK_L);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription("Load new files.");

        menuItem.addActionListener(this);
        menu.add(menuItem);

        return menuBar;
    }

    private void displayDetail(BaseInfo info) {
        // detailPane.add(new JButton("display detail"));
        detailPane.show(info);
    }

    public JFrame getFrame() {
        return frame;
    }

    private void setFrame(JFrame frame) {
        this.frame = frame;
    }

    /**
     * Required by TreeSelectionListener interface.
     */
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

        if (node == null) {
            return;
        }

        BaseInfo nodeInfo = (BaseInfo) node.getUserObject();

        List children = nodeInfo.loadChildren();

        for (Iterator i = children.iterator(); i.hasNext();) {
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) i.next();

            node.add(childNode);
        }

        displayDetail(nodeInfo);

        JFrame frame = getFrame();

        // frame.pack();
        frame.setVisible(true);
    }
}
