package ui;

import java.util.Collections;
import java.util.List;

import javax.swing.JTextArea;

import model.Choice;
import controller.ChoiceStatisticComparator;

public class RankingControl extends JTextArea {

	private static final long serialVersionUID = 1L;

	// private int numToShow;

	public RankingControl() {
		// /this.numToShow = numToShow;
	}

	// public int getNumToShow() {
	// return numToShow;
	// }

	public void load(List<Choice> choices) {

		Collections.sort(choices, new ChoiceStatisticComparator(
				ChoiceStatisticComparator.HIGHEST_CORRECT));

		// List highest = choices.subList(0, getNumToShow());

		String text = addChoices(choices);

		// text += "...\n";
		//
		// List lowest = choices.subList(choices.size() - getNumToShow(),
		// choices
		// .size());
		//
		// text += addChoices(lowest);

		setText(text);

		invalidate();
	}

	// private void dumpChoices(List choices) {
	// for (Iterator i = choices.iterator(); i.hasNext();) {
	// Choice choice = (Choice) i.next();
	//
	// System.out.println(choice.getScore() + " " + choice.getEnglish()
	// + " " + choice.getPronunciation());
	// }
	// }

	private String addChoices(List<Choice> choices) {
		String text = new String();

		for (Object element : choices) {
			Choice choice = (Choice) element;

			text += choice.getChinese() + ":" + truncate(choice.getScore())
					+ " - " + choice.getCorrect() + "/" + choice.getAttempted()
					+ "\n";
		}

		return text;
	}

	private String truncate(double d) {
		int dInt = (int) (d * 100.0);

		return String.valueOf((dInt) / 100.0);
	}

	// public void setNumToShow(int numToShow) {
	// this.numToShow = numToShow;
	// }
}
