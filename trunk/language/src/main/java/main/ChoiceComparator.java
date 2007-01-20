package main;

import java.util.Comparator;

import model.Choice;

public class ChoiceComparator implements Comparator {

	public int compare(Object a, Object b) {
		if ((a instanceof Choice) && (b instanceof Choice)) {
			Choice choiceA = (Choice) a;
			Choice choiceB = (Choice) b;
			return choiceA.getEnglish().compareTo(choiceB.getEnglish());

		} else {
			throw new ClassCastException("Unable to compare " + a + " and " + b);
		}
	}
}
