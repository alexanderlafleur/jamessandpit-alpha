package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import model.Choice;
import controller.Controller;

public class Controls extends JPanel {

	private static final long serialVersionUID = 1L;

	private Controller controller;

	private RankingControl rankings = new RankingControl();

	private JButton rate = new JButton("Review");

	private JButton skip = new JButton("Skip");

	private JButton start = new JButton("Start");

	public Controls(Controller controller) {
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(layout);

		add(start);
		add(rate);
		add(skip);
		add(new JScrollPane(rankings));

		this.controller = controller;
		rate.setToolTipText("Review your selection.");
		start.setToolTipText("Start matching.");
		skip.setToolTipText("Skip this match. This will score against you.");

		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getController().start();
			}
		});

		rate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getController().rate();
			}
		});

		skip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getController().skip();
			}
		});
	}

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public void logStats(List<Choice> choices) {
		rankings.load(choices);
	}

}
