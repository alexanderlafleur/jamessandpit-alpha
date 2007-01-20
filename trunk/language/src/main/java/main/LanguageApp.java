package main;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import controller.Controller;

public class LanguageApp extends JFrame {

	protected static final ApplicationContext CONTEXT = new ClassPathXmlApplicationContext(
			new String[] { "applicationContext.xml" });

	private static final long serialVersionUID = 1L;

	public static void main(String args[]) throws Exception {
		new LanguageApp();
	}

	private CategoryPanel categoryPanel;

	private Controller controller = new Controller(Controller.MATCH_PRONUNCIATION);

	private JMenuBar menuBar = new JMenuBar();

	public LanguageApp() throws Exception {
		super("Language App");

		categoryPanel = new CategoryPanel(controller);

		// categoryPanel.setBorder(LineBorder.createBlackLineBorder());
		// categoryPanel.setMinimumSize(new Dimension(400, 100));

		createUI();

		pack();

		setVisible(true);

	}

	@Override
	public Dimension getMinimumSize() {
		Dimension prefSize = getPreferredSize();
		return new Dimension(100, prefSize.height);
	}

	protected void createUI() throws Exception {
		// setSize(1024, 1024);
		setJMenuBar(menuBar);

		setDefaultLookAndFeelDecorated(true);

		Container content = getContentPane();

		BorderLayout layout = new BorderLayout();

		content.setLayout(layout);

		content.add(categoryPanel, BorderLayout.CENTER);
		// content.add(categoryPanel);

		content.add(controller.getControls(), BorderLayout.EAST);
		content.add(controller.getLogControl(), BorderLayout.SOUTH);

		// Exit the application when the window is closed.
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
}
