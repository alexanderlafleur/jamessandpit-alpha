package controller;

import java.util.Collections;
import java.util.List;

import model.Choice;
import ui.Category;

public class EnglishCategory extends Category {

	private static final long serialVersionUID = 1L;

	public EnglishCategory(AppController controller) {
		super(controller);
	}

	@Override
	public String getChoiceValue(Choice choice) {
		return choice.getEnglish();
	}

	@Override
	public void shuffle() {
		if (!isFixed()) {
			List<String> currentData = toList(getModel());
			Collections.sort(currentData);

			setListData(currentData.toArray());
		}
	}
}
