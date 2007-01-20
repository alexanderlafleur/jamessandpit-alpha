package controller;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.border.LineBorder;

import model.Choice;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ui.Category;
import ui.Controls;
import util.FontFinder;
import dao.ChoiceException;
import dao.ChoiceHelper;

/**
 * Controls the Chinese, English, Pronunication and Controls components.
 * 
 * @author James
 */
public class Controller implements AppController {
	protected static final ApplicationContext CONTEXT = new ClassPathXmlApplicationContext(
			new String[] { "applicationContext.xml" });

	public static final int MATCH_ALL = 4;

	public static final int MATCH_CHARACTER = 1;

	public static final int MATCH_ENGLISH = 2;

	public static final int MATCH_PRONUNCIATION = 3;

	private Category chineseCategory;

	private ChoiceHelper choiceHelper;

	private List<Choice> choices = new ArrayList<Choice>();

	private Controls controls = new Controls(this);

	private Category englishCategory;

	private FontFinder fontFinder = new FontFinder();

	private LogControl logControl = new LogControl();

	private int mode;

	private Category pronunciationCategory;

	private int size;

	// private SpeechHelper speechHelper = new SpeechHelper(false);

	public Controller(int mode) {

		chineseCategory = new ChineseCategory(this);
		pronunciationCategory = new PronunciationCategory(this);
		englishCategory = new EnglishCategory(this);

		setMode(mode);
		setSize(size);

		logControl.setBorder(LineBorder.createBlackLineBorder());
		logControl.setMinimumSize(new Dimension(500, 10));
		// logControl.setAutoscrolls(true);
		// logControl.setMaximumSize(new Dimension(500,200));
		// setFont(new Font("MS-PMincho", Font.PLAIN, 24));

		choiceHelper = (ChoiceHelper) CONTEXT.getBean("choiceHelper");
	}

	public Category getChineseCategory() {
		return chineseCategory;
	}

	public ChoiceHelper getChoiceHelper() {
		return choiceHelper;
	}

	public Controls getControls() {
		return controls;
	}

	public Category getEnglishCategory() {
		return englishCategory;
	}

	public FontFinder getFontFinder() {
		return fontFinder;
	}

	public LogControl getLogControl() {
		return logControl;
	}

	public int getMode() {
		return mode;
	}

	public Category getPronunciationCategory() {
		return pronunciationCategory;
	}

	public int getSize() {
		return size;
	}

	// public SpeechHelper getSpeechHelper() {
	// return speechHelper;
	// }

	private List<Choice> loadAllChoices() {
		return getChoiceHelper().loadAll();
	}

	private Choice loadChinese(String c) {
		return getChoiceHelper().loadChinese(c);
	}

	public Choice loadChoice(int i) {
		return getChoiceHelper().load(String.valueOf(i));
	}

	private Choice loadCorrect(String c, String e, String p) {
		boolean cFixed = getChineseCategory().isFixed();
		boolean eFixed = getEnglishCategory().isFixed();
		boolean pFixed = getPronunciationCategory().isFixed();

		Choice correct;
		if (cFixed) {
			correct = loadChinese(c);
		} else if (eFixed) {
			correct = loadEnglish(e);
		} else if (pFixed) {
			correct = loadPronunciation(p);
		} else {
			correct = null;
		}

		return correct;

	}

	private Choice loadEnglish(String e) {
		return getChoiceHelper().loadEnglish(e);
	}

	private Choice loadPronunciation(String p) {
		return getChoiceHelper().loadPronunciation(p);
	}

	private void logStats(List<Choice> choices) {
		controls.logStats(choices);
	}

	private void placeOnScreen(List<Choice> newChoices) {
		reset();

		choices = newChoices;

		shuffle(choices);

		for (Choice choice : choices) {
			getChineseCategory().add(choice);
			getEnglishCategory().add(choice);
			getPronunciationCategory().add(choice);
		}

		if (!getChineseCategory().isFixed()) {
			getChineseCategory().sort();
		}
		if (!getEnglishCategory().isFixed()) {
			getEnglishCategory().sort();
		}
		if (!getPronunciationCategory().isFixed()) {
			getPronunciationCategory().sort();
		}
	}

	private void print(List<Choice> choices) {
		System.out.println("Printing choices");
		for (Object element : choices) {
			Choice choice = (Choice) element;

			System.out.println(choice);
		}
		System.out.println("...done");

	}

	public void rate() {
		String c = (String) getChineseCategory().getSelectedValue();
		String e = (String) getEnglishCategory().getSelectedValue();
		String p = (String) getPronunciationCategory().getSelectedValue();

		Choice correct = loadCorrect(c, e, p);

		if (correct.equals(c, e, p)) {
			getLogControl().log(
					"Match - " + "Chinese: "
							+ getChineseCategory().getSelectedValue()
							+ " Pronunciation: "
							+ getPronunciationCategory().getSelectedValue()
							+ " Engish: "
							+ getEnglishCategory().getSelectedValue());

			removeChoice(correct);

			scoreCorrect(correct);

			placeOnScreen(choices);

			// getSpeechHelper().speak("Chong Jok");

		} else {
			getLogControl().log(
					"No match - Correct choice is " + "Chinese: "
							+ correct.getChinese() + " Pronunciation: "
							+ getPronunciationCategory().getSelectedValue()
							+ " Engish: " + correct.getEnglish()
							+ " instead of "
							+ getEnglishCategory().getSelectedValue());

			scoreIncorrect(correct);

			placeOnScreen(choices);

			// getSpeechHelper().speak("tshorr");

		}

		List<Choice> choices = getChoiceHelper().loadAll();
		logStats(choices);
	}

	private void removeChoice(Choice choice) {
		if (!choices.remove(choice)) {
			throw new ChoiceException("Unable to remove choice: " + choice);
		}
	}

	private void reset() {
		getChineseCategory().reset();
		getEnglishCategory().reset();
		getPronunciationCategory().reset();
	}

	/**
	 * Makes small changes to choices
	 * 
	 * @param choices
	 */
	private void ripple(List<Choice> choices) {
		int numChoices = choices.size();

		print(choices);

		for (int i = 0; i < numChoices; i++) {
			if (Math.random() > 0.5) {
				Choice a = choices.get(i);

				int targetIndex = (int) (Math.random() * 5 * ((Math.random() > 0.5) ? -1
						: 1));

				if (targetIndex >= choices.size()) {
					targetIndex = choices.size() - 1;
				}

				if (targetIndex < 0) {
					targetIndex = 0;
				}

				Choice b = choices.get(targetIndex);

				choices.set(i, b);
				choices.set(targetIndex, a);
			}
		}

		print(choices);

	}

	private void scoreCorrect(Choice choice) {
		choice.setCorrect(choice.getCorrect() + 1);
		choice.setAttempted(choice.getAttempted() + 1);

		getChoiceHelper().update(choice);
	}

	private void scoreIncorrect(Choice choice) {
		choice.setAttempted(choice.getAttempted() + 1);

		getChoiceHelper().update(choice);
	}

	public void setCharacterChooser(Category characterChooser) {
		this.chineseCategory = characterChooser;
	}

	public void setChoiceHelper(ChoiceHelper storeHelper) {
		this.choiceHelper = storeHelper;
	}

	public void setControls(Controls controls) {
		this.controls = controls;
	}

	public void setEnglishCategory(Category englishChooser) {
		this.englishCategory = englishChooser;
	}

	public void setFontFinder(FontFinder fontFinder) {
		this.fontFinder = fontFinder;
	}

	public void setLogControl(LogControl log) {
		this.logControl = log;
	}

	public void setMode(int mode) {
		this.mode = mode;

		switch (mode) {
		case MATCH_ENGLISH:
			chineseCategory.fix();
			englishCategory.unfix();
			pronunciationCategory.fix();
			break;
		case MATCH_PRONUNCIATION:
			chineseCategory.fix();
			englishCategory.fix();
			pronunciationCategory.unfix();
			break;
		case MATCH_CHARACTER:
			chineseCategory.unfix();
			englishCategory.fix();
			pronunciationCategory.fix();
			break;
		case MATCH_ALL:
			chineseCategory.unfix();
			englishCategory.unfix();
			pronunciationCategory.unfix();
			break;
		default:
		}

	}

	// public void setSpeechHelper(SpeechHelper speechHelper) {
	// this.speechHelper = speechHelper;
	// }

	public void setPronunciationCategory(Category pronunciationChooser) {
		this.pronunciationCategory = pronunciationChooser;
	}

	public void setSize(int size) {
		this.size = size;
	}

	private void shuffle(List<Choice> choices) {
		Collections.sort(choices, new ChoiceStatisticComparator(
				ChoiceStatisticComparator.LOWEST_CORRECT));

		ripple(choices);
	}

	public void skip() {
		String c = (String) getChineseCategory().getSelectedValue();
		String e = (String) getEnglishCategory().getSelectedValue();
		String p = (String) getPronunciationCategory().getSelectedValue();

		Choice correct = loadCorrect(c, e, p);
		removeChoice(correct);

		scoreIncorrect(correct);

		placeOnScreen(choices);

		// getSpeechHelper().speak("Ha yut gorr");
		logStats(choices);

	}

	public void start() {
		reset();
		List<Choice> choices = loadAllChoices();

		controls.logStats(choices);

		placeOnScreen(choices);
		System.out.println("Done");

		// getSpeechHelper().speak("Hoi Cheee");
		logStats(choices);
	}

	// private List toList(ListModel model) {
	// List list = new ArrayList();
	//
	// for (int i = 0; i < model.getSize(); i++) {
	// String choice = (String) model.getElementAt(i);
	//
	// list.add(choice);
	// }
	// return list;
	// }
}
