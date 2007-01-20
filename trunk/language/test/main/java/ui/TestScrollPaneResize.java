package ui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JList;

public class TestScrollPaneResize {
	public static final void main(String args[]) throws Exception {
		new TestScrollPaneResize().run();
	}

	private void run() throws Exception {
		// MyJFrame frame =
		new MyJFrame();

	}

	private class MyJFrame extends JFrame {
		// private MyLabel label = new MyLabel("MyLabel");

		// private MyJScrollPane scrollPane = new MyJScrollPane();

		private static final long serialVersionUID = -540647505536892565L;

		private MyJList listOne = new MyJList("List Data One");

		private MyJList listTwo = new MyJList("List Data Two");

		public MyJFrame() throws Exception {
			createUI();

			pack();

			setVisible(true);
		}

		// if (ae.getSource() == getBtnManageTask()) {
		// tblTasks.setModel(getTaskTableModel(7,7));
		// setupColumns(gettblTasks()) ;
		// scrTasks.revalidate();
		// }

		protected void createUI() throws Exception {
			// setSize(1024, 1024);
			setDefaultLookAndFeelDecorated(true);

			Container content = getContentPane();

			GridLayout layout = new GridLayout(1, 2);

			content.setLayout(layout);

			// content.add(label);
			// content.add(scrollPane);
			content.add(listOne);
			content.add(listTwo);

			// Exit the application when the window is closed.
			addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});
		}

		@Override
		public Dimension getMinimumSize() {
			Dimension prefSize = getPreferredSize();
			return new Dimension(100, prefSize.height);
		}
	}

	private class MyJList extends JList {

		private static final long serialVersionUID = -657929074816087121L;

		public MyJList(String prefix) {
			List<String> data = new ArrayList<String>();
			for (int i = 0; i < 30; i++) {
				data.add(prefix + " " + (i + 1));
			}

			setListData(data.toArray());
		}
	}

	// private class MyJScrollPane extends JScrollPane {
	//
	// private static final long serialVersionUID = 1L;
	//
	// private MyJList list = new MyJList("One");
	//
	// public MyJScrollPane() {
	// setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	// setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	//
	// add(list);
	//
	// // setMaxSize(10);
	// // setFontSize(24);
	// // setVisibleRowCount(20);
	//
	// }
	// }

	// private class MyLabel extends JLabel {
	//
	// private static final long serialVersionUID = 1L;
	//
	// public MyLabel(String label) {
	// super(label);
	// }
	// }
}
