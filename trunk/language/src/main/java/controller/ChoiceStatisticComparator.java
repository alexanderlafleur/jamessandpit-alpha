package controller;

import java.util.Comparator;

import model.Choice;

public class ChoiceStatisticComparator implements Comparator {

	public static final int HIGHEST_CORRECT = 1;

	public static final int LOWEST_CORRECT = 2;

	private int mode;

	public ChoiceStatisticComparator(int mode) {
		this.mode = mode;
	}

	public int compare(Object o1, Object o2) {
		if ((o1 instanceof Choice) && (o2 instanceof Choice)) {
			Choice c1 = (Choice) o1;
			Choice c2 = (Choice) o2;

			double c1Score = c1.getScore();
			double c2Score = c2.getScore();

			if (c1Score == c2Score) {
				if (c1.getAttempted() > c2.getAttempted()) {
					return -1;
				} else if (c2.getAttempted() > c1.getAttempted()) {
					return 1;
				} else {
					return 0;
				}
			} else {
				if (mode == HIGHEST_CORRECT) {
					System.out.println("Sorting high " + c1Score + " "
							+ c2Score + " " + (c1Score - c2Score));
					if (c1Score - c2Score > 0) {
						return -1;
					} else {
						return 1;
					}
				} else {
					System.out.println("Sorting low  " + c1Score + " "
							+ c2Score + " " + (c2Score - c1Score));
					if (c2Score - c1Score > 0) {
						return -1;
					} else {
						return 1;
					}
				}
			}
		} else {
			throw new ClassCastException("Unable to compare " + o1 + " and "
					+ o2);
		}
	}
}
