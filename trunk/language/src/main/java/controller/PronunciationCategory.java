package controller;

import java.util.Collections;
import java.util.List;

import model.Choice;
import ui.Category;

public class PronunciationCategory extends Category {

	private static final long serialVersionUID = 1L;

	public PronunciationCategory(AppController controller) {
		super(controller);
	}

	@Override
	public String getChoiceValue(Choice choice) {
		return choice.getPronunciation();
	}

	@Override
	public void shuffle() {
		if (!isFixed()) {
			List<String> currentData = toList(getModel());
			Collections.shuffle(currentData);

			setListData(currentData.toArray());
		}
	}
}
