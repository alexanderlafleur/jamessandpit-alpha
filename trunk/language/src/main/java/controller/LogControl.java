package controller;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class LogControl extends JScrollPane {
	private static final long serialVersionUID = 1L;

	private JTextArea textArea = new JTextArea("Ready...");

	public LogControl() {
		setViewportView(textArea);

		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);

		setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		setPreferredSize(new Dimension(250, 145));
		setMinimumSize(new Dimension(10, 10));
	}

	public void log(String msg) {
		String currentText = textArea.getText();

		textArea.setText(currentText + "\n" + msg);
	}
}
