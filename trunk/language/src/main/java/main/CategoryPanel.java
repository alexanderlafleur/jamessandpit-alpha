package main;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import controller.Controller;

public class CategoryPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public CategoryPanel(Controller controller) {
		setLayout(new GridLayout(1, 3));
		// setLayout(new FlowLayout());

		JScrollPane scrollPane = new JScrollPane(controller
				.getChineseCategory());
		// scrollPane.setMinimumSize(new Dimension(10,0));
		add(scrollPane);

		add(new JScrollPane(controller.getPronunciationCategory()));

		add(new JScrollPane(controller.getEnglishCategory()));
	}
}
